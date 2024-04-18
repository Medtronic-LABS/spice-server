package com.mdtlabs.coreplatform.spiceservice.prescription.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.exception.SpiceValidation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.FillPrescriptionRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.FillPrescriptionResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OtherMedicationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriberDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionHistoryResponse;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserListDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;
import com.mdtlabs.coreplatform.common.model.entity.spice.Prescription;
import com.mdtlabs.coreplatform.common.model.entity.spice.PrescriptionHistory;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.DateUtil;
import com.mdtlabs.coreplatform.spiceservice.AdminApiInterface;
import com.mdtlabs.coreplatform.spiceservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.PrescriptionMapper;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.service.PatientVisitService;
import com.mdtlabs.coreplatform.spiceservice.prescription.repository.PrescriptionHistoryRepository;
import com.mdtlabs.coreplatform.spiceservice.prescription.repository.PrescriptionRepository;
import com.mdtlabs.coreplatform.spiceservice.prescription.service.PrescriptionService;

/**
 * <p>
 * PrescriptionServiceImpl class implements various methods for managing prescription, including adding, updating,
 * and retrieving prescription.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
@Validated
public class PrescriptionServiceImpl implements PrescriptionService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${app.file-path}")
    private String filePath;

    @Value("${app.is-prescription-signature-uploaded-to-s3}")
    private boolean isPrescriptionSignatureUploadedToS3;

    @Value("${app.is-prescription-signature-uploaded-to-minio}")
    private boolean isPrescriptionSignatureUploadedToMinio;

    @Value("${application.minio-bucket.name}")
    String minioBucketName;

    @Value("${cloud.minio.credentials.console-url}")
    String minioUrl;

    @Autowired
    private AdminApiInterface adminApiInterface;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private PrescriptionHistoryRepository prescriptionHistoryRepository;

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PatientTrackerService patientTrackerService;

    @Autowired
    private PatientTreatmentPlanService patientTreatmentPlanService;

    @Autowired
    private PatientVisitService patientVisitService;

    @Autowired
    private UserApiInterface userApiInterface;

    @Autowired
    MinioClient minioClient;

    /**
     * {@inheritDoc}
     *
     * @throws IOException
     */
    @Transactional
    public void createOrUpdatePrescription(PrescriptionRequestDTO prescriptionRequest) throws IOException, GeneralSecurityException, MinioException {
        if (Objects.isNull(prescriptionRequest)) {
            throw new BadRequestException(1000);
        }

        String signature = uploadSignature(prescriptionRequest.getSignatureFile(),
                prescriptionRequest.getPatientTrackId(), prescriptionRequest.getPatientVisitId());
        List<PrescriptionDTO> prescriptionList = prescriptionRequest.getPrescriptionList();
        boolean isOtherPrescriptionPresent = prescriptionList.stream()
                .anyMatch(prescriptionDto -> prescriptionDto.getMedicationId() == Constants.MINUS_ONE);

        OtherMedicationDTO otherMedication = isOtherPrescriptionPresent ? getOtherMedication() : null;
        constructAndSavePrescriptionList(prescriptionList, prescriptionRequest, otherMedication, signature);
    }

    /**
     * <p>
     * This method is used to retrieve other medication data based on the user's country ID.
     * </p>
     *
     * @return {@link OtherMedicationDTO} The method returns OtherMedication
     */
    private OtherMedicationDTO getOtherMedication() {
        long countryId = UserContextHolder.getUserDto().getCountry().getId();
        return adminApiInterface
                .getOtherMedication(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(), countryId)
                .getBody();
    }

    /**
     * <p>
     * This method is used to add new prescriptions to the prescription repository and updates prescription
     * history data.
     * </p>
     *
     * @param prescriptions {@link List<Prescription>} the list of Prescription objects that need to added is given
     */
    private void addNewPrescriptions(List<Prescription> prescriptions) {
        List<Prescription> addedPrescriptions = prescriptionRepository.saveAll(prescriptions);
        addPrescriptionHistoryData(addedPrescriptions, Constants.BOOLEAN_FALSE);
    }

    /**
     * <p>
     * The method is used to update a list of prescriptions by checking if they exist in the database, resetting
     * some fields, and saving the updated prescriptions.
     * </p>
     *
     * @param prescriptions {@link List<Prescription>} The list of Prescription objects that need to be updated is given
     */
    private void updatePrescriptions(List<Prescription> prescriptions) {
        Set<Long> prescriptionIds = prescriptions.stream().map(Prescription::getId)
                .collect(Collectors.toSet());
        if (prescriptionIds.size() != prescriptions.size()) {
            throw new DataNotAcceptableException(1505);
        }
        List<Prescription> existingPrescriptions = prescriptionRepository.getPrescriptions(prescriptionIds);
        if (prescriptions.size() != existingPrescriptions.size()) {
            throw new DataNotAcceptableException(1505);
        }
        List<Prescription> existingPrescriptionsToUpdate = new ArrayList<>();
        for (Prescription existingPrescription : existingPrescriptions) {
            Prescription prescriptionToUpdate = prescriptions.stream()
                    .filter(prescription -> (Objects.equals(prescription.getId(), existingPrescription.getId())))
                    .findFirst().orElseThrow(() -> new DataNotAcceptableException(1506));

            prescriptionToUpdate.setPrescriptionFilledDays(Constants.ZERO);
            prescriptionToUpdate.setRemainingPrescriptionDays(prescriptionToUpdate.getPrescribedDays());
            existingPrescriptionsToUpdate.add(prescriptionToUpdate);
        }

        List<Prescription> updatedPrescriptions = prescriptionRepository.saveAll(existingPrescriptionsToUpdate);
        addPrescriptionHistoryData(updatedPrescriptions, Constants.BOOLEAN_FALSE);
    }

    /**
     * <p>
     * This method is used to add prescription history data to the database, including a refill date if specified.
     * </p>
     *
     * @param prescriptions {@link List<Prescription>} The list of Prescription objects that will be mapped to
     *                      PrescriptionHistory objects and saved to the prescriptionHistoryRepository is given
     * @param addRefillDate The boolean value indicating whether not to add a last refill date to the prescription
     *                      history data is given
     */
    private void addPrescriptionHistoryData(List<Prescription> prescriptions,
                                            boolean addRefillDate) {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull()).setFullTypeMatchingRequired(true);
        List<PrescriptionHistory> prescriptionHistories = prescriptions.stream().map(
                prescription -> modelMapper.map(prescription, PrescriptionHistory.class)
        ).toList();

        if (addRefillDate) {
            prescriptionHistories.forEach(prescriptionHistory -> {
                prescriptionHistory.setLastRefillDate(new Date());
                prescriptionHistory.setId(null);
            });
        } else {
            prescriptionHistories.forEach(prescriptionHistory -> prescriptionHistory.setId(null));
        }
        prescriptionHistoryRepository.saveAll(prescriptionHistories);
    }

    /**
     * <p>
     * This method is used to update the prescription status of a patient visit based on the prescription request.
     * </p>
     *
     * @param prescriptionRequest {@link PrescriptionRequestDTO} The prescriptionRequest contains information related
     *                            to a prescription request made by a patient is given
     */
    public void updatePrescriptionPatientVisit(PrescriptionRequestDTO prescriptionRequest) {
        PatientVisit patientVisit = patientVisitService.getPatientVisitById(prescriptionRequest.getPatientVisitId());
        patientVisit.setPrescription(Constants.BOOLEAN_TRUE);
        patientVisitService.updatePatientVisit(patientVisit);
    }

    /**
     * {@inheritDoc}
     */
    public List<PrescriptionDTO> getPrescriptions(RequestDTO request) {
        if (Objects.isNull(request.getPatientTrackId())) {
            throw new DataNotAcceptableException(1256);
        }
        List<Prescription> prescriptions = prescriptionRepository.findByPatientTrackIdAndIsDeleted(
                request.getPatientTrackId(), request.getIsDeleted());
        if (prescriptions.isEmpty()) {
            return new ArrayList<>();
        }
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull())
                .setFullTypeMatchingRequired(Constants.BOOLEAN_TRUE);
        List<PrescriptionDTO> prescriptionsList = new ArrayList<>();
        prescriptions.forEach(prescription -> {
            Date prescribedSince = DateUtil.subtractDates(prescription.getEndDate(),
                    (prescription.getPrescribedDays() - 1));
            int prescriptionRemainingDays = Constants.ZERO;
            if (DateUtil.isSameDate(new Date(), prescription.getEndDate(), Calendar.DATE)) {
                prescriptionRemainingDays = Constants.ONE;
            } else if (prescription.getEndDate().after(new Date())) {
                prescriptionRemainingDays = DateUtil.getCalendarDiff(new Date(), prescription.getEndDate())
                        + Constants.TWO;
            }

            PrescriptionDTO prescriptionDto = new PrescriptionDTO();
            prescriptionDto.setPrescribedSince(prescribedSince);
            prescriptionDto.setPrescriptionRemainingDays(prescriptionRemainingDays);
            prescription.setRemainingPrescriptionDays(prescriptionRemainingDays);
            modelMapper.map(prescription, prescriptionDto);
            prescriptionsList.add(prescriptionDto);
        });
        return prescriptionsList;
    }

    /**
     * {@inheritDoc}
     */
    public List<PrescriptionHistory> getPrescriptions(Long patientVisitId) {
        return prescriptionHistoryRepository.getPrescriptions(patientVisitId);
    }

    /**
     * {@inheritDoc}
     */
    public PrescriptionHistoryResponse listPrescriptionHistoryData(RequestDTO request) {
        if (Objects.isNull(request.getPatientTrackId())) {
            throw new DataNotAcceptableException(1256);
        }
        List<Map<String, Object>> patientVisitDates = new ArrayList<>();
        if (request.isLatestRequired()
                && Objects.isNull(request.getPatientVisitId())) {
            List<PatientVisit> patientVisits = patientVisitService.getPatientVisitDates(
                    request.getPatientTrackId(), null, null, Constants.BOOLEAN_TRUE);
            if (!patientVisits.isEmpty()) {
                patientVisits.forEach(patientVisit -> patientVisitDates
                        .add(Map.of(Constants.ID, patientVisit.getId(), Constants.VISIT_DATE, patientVisit.getVisitDate())));
                request.setPatientVisitId(patientVisits.get(patientVisits.size() - Constants.ONE).getId());
            }
        }

        List<PrescriptionHistory> prescriptionHistories = prescriptionHistoryRepository.getPrescriptionHistory(
                request.getPrescriptionId(), request.getPatientVisitId(),
                request.getPatientTrackId());
        PrescriptionHistoryResponse prescriptionHistoryResponse = new PrescriptionHistoryResponse();
        prescriptionHistoryResponse.setPatientPrescription(prescriptionHistories);
        prescriptionHistoryResponse.setPrescriptionHistoryDates(patientVisitDates);
        return prescriptionHistoryResponse;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void removePrescription(RequestDTO request) {
        if (Objects.isNull(request.getPatientVisitId())) {
            throw new DataNotAcceptableException(1508);
        }
        if (Objects.isNull(request.getPatientTrackId())) {
            throw new DataNotAcceptableException(1256);
        }
        Prescription prescriptionToBeDeleted = prescriptionRepository.findById(request.getId())
                .orElseThrow(() -> new DataNotAcceptableException(1506));

        prescriptionToBeDeleted.setDeleted(Constants.BOOLEAN_TRUE);
        prescriptionToBeDeleted.setDiscontinuedOn(new Date());
        prescriptionToBeDeleted.setDiscontinuedReason(request.getDiscontinuedReason());
        prescriptionToBeDeleted.setTenantId(request.getTenantId());
        prescriptionRepository.save(prescriptionToBeDeleted);
        updateMedicationPrescribed(request.getPatientTrackId(), Constants.BOOLEAN_FALSE,
                Constants.BOOLEAN_TRUE);
    }

    /**
     * {@inheritDoc}
     */
    public List<FillPrescriptionResponseDTO> getFillPrescriptions(SearchRequestDTO request) {
        if (Objects.isNull(request.getPatientTrackId())) {
            throw new DataNotAcceptableException(1256);
        }
        List<Prescription> prescriptions = prescriptionRepository
                .getRefillPrescriptions(request.getPatientTrackId(), Constants.ZERO, Constants.BOOLEAN_FALSE);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull())
                .setFullTypeMatchingRequired(Constants.BOOLEAN_TRUE);
        return prescriptions.stream().map(
                prescription -> modelMapper.map(prescription, FillPrescriptionResponseDTO.class)
        ).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<Prescription> updateFillPrescription(FillPrescriptionRequestDTO fillPrescriptionRequest) {
        if (Objects.isNull(fillPrescriptionRequest.getPatientTrackId())
                || Objects.isNull(fillPrescriptionRequest.getPatientVisitId())) {
            throw new DataNotAcceptableException(1502);
        }
        if (Objects.isNull(fillPrescriptionRequest.getPrescriptions())
                || fillPrescriptionRequest.getPrescriptions().isEmpty()) {
            throw new DataNotAcceptableException(1512);
        }
        List<Prescription> prescriptions = fillPrescriptionRequest.getPrescriptions();
        List<Long> prescriptionIds = prescriptions.stream().map(Prescription::getId).toList();
        List<Prescription> existingPrescriptions = prescriptionRepository.getActivePrescriptions(prescriptionIds,
                Constants.BOOLEAN_FALSE);
        List<Prescription> prescriptionsToUpdate = new ArrayList<>();
        for (Prescription prescription : prescriptions) {
            Prescription existingPrescription = existingPrescriptions.stream()
                    .filter(existing -> Objects.equals(existing.getId(), prescription.getId())).findFirst()
                    .orElseThrow(() -> new DataNotFoundException(1510));
            if (existingPrescription.getRemainingPrescriptionDays() < prescription.getPrescriptionFilledDays()) {
                throw new DataNotAcceptableException(1511);
            }
            prescriptionsToUpdate.add(
                    prescriptionMapper.setPrescription(existingPrescription, prescription, fillPrescriptionRequest));
        }
        prescriptions = prescriptionRepository.saveAll(prescriptionsToUpdate);
        addPrescriptionHistoryData(prescriptions, Constants.BOOLEAN_TRUE);
        updateMedicationPrescribed(fillPrescriptionRequest.getPatientTrackId(), Constants.BOOLEAN_FALSE,
                Constants.BOOLEAN_FALSE);
        return prescriptions;
    }

    /**
     * <p>
     * This method is used to update a patient's medication prescription status and related dates in the patient
     * tracker service.
     * </p>
     *
     * @param patientTrackId         {@link Long} The patientTrack ID need to set is given
     * @param isMedicationPrescribed The boolean value indicating whether medication is prescribed for the patient
     *                               or not is given
     * @param updateDate             The boolean value indicating whether not to update the next medical review date
     *                               and last review date is given
     */
    private void updateMedicationPrescribed(Long patientTrackId, boolean isMedicationPrescribed, boolean updateDate) {
        Date nextMedicalReviewDate = null;
        Date lastReviewDate = null;
        if (updateDate) {
            nextMedicalReviewDate = patientTreatmentPlanService.getNextFollowUpDate(patientTrackId,
                    Constants.MEDICAL_REVIEW_FREQUENCY);
            lastReviewDate = new Date();
        }
        if (!isMedicationPrescribed) {
            if (prescriptionRepository
                    .findByPatientTrackIdAndRemainingPrescriptionDaysGreaterThanAndIsDeletedFalse(patientTrackId, Constants.ZERO)
                    .isEmpty()) {
                patientTrackerService.updateForFillPrescription(patientTrackId, Constants.BOOLEAN_FALSE,
                        lastReviewDate, nextMedicalReviewDate);
            }
        } else {
            patientTrackerService.updateForFillPrescription(patientTrackId, Constants.BOOLEAN_TRUE, lastReviewDate,
                    nextMedicalReviewDate);
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<PrescriptionHistory> getRefillPrescriptionHistory(SearchRequestDTO request) {
        if (Objects.isNull(request.getPatientTrackId())
                || Objects.isNull(request.getLastRefillVisitId())) {
            throw new DataNotAcceptableException(1502);
        }

        return prescriptionHistoryRepository.getFillPrescriptionHistory(request.getPatientTrackId(),
                request.getLastRefillVisitId(), Constants.ZERO);
    }

    /**
     * <p>
     * This method is used to upload a signature file to either an S3 bucket or a local file path and returns the
     * location of the uploaded file.
     * </p>
     *
     * @param file           {@link MultipartFile} The file that needs to be uploaded, of type MultipartFile is given
     * @param patientTrackId {@link Long} The patientTrack ID need to set is given
     * @param patientVisitId {@link Long} The patientVisit ID need to set is given
     * @return {@link String} The method returns a String representing the location of the uploaded signature file
     */
    public String uploadSignature(MultipartFile file, Long patientTrackId, Long patientVisitId) throws IOException, MinioException, GeneralSecurityException {
        File fileObj = convertMultipartFileToFile(file);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.SIGNATURE_DATE_FORMAT)
                .withZone(ZoneId.of(Constants.TIMEZONE_UTC));
        String timestamp = formatter.format(Instant.now());
        String fileName = patientTrackId.toString().concat(Constants.UNDER_SCORE).concat(patientVisitId.toString())
                .concat(Constants.UNDER_SCORE).concat(timestamp).concat(Constants.UNDER_SCORE)
                .concat(Constants.PRESCRIPTION_FORMAT);

        String location;

        if (isPrescriptionSignatureUploadedToS3) {
            s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
            location = s3Client.getUrl(bucketName, fileName).toString();
            if (Objects.nonNull(fileObj)) {
                Files.delete(fileObj.toPath());
            }
        } else if (isPrescriptionSignatureUploadedToMinio) {
            FileInputStream inputStream = new FileInputStream(fileObj);
            PutObjectArgs putObjectArgs = PutObjectArgs.builder().bucket(minioBucketName)
                    .object(fileName).stream(inputStream, 1567, -1).build();
            ObjectWriteResponse response = minioClient.putObject(putObjectArgs);
            location = minioUrl + Constants.FORWARD_SLASH + Constants.BROWSER + Constants.FORWARD_SLASH + response.etag();
        } else {
            try (InputStream inputStream = new FileInputStream(fileObj)) {
                String destinationLocation = filePath + Constants.FORWARD_SLASH + fileName;
                File fileDestination = new File(destinationLocation);
                try (OutputStream outputStream = new FileOutputStream(fileDestination)) {
                    location = destinationLocation;
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        }
        return location;
    }

    /**
     * <p>
     * This method is used to convert a MultipartFile object to a File object and returns it.
     * </p>
     *
     * @param file {@link MultipartFile} The MultipartFile object representing the file to be converted to a
     *             File object is given
     * @return {@link File} The File for the given MultipartFile is converted and returned
     */
    private File convertMultipartFileToFile(MultipartFile file) {
        System.out.println("Hiiiiiiiiiiiiiiiii");
        File convertedFile = null;
        String targetDirectory = filePath;
        Path targetPath = new File(targetDirectory).toPath().normalize();
        if (Objects.nonNull(file) && Objects.nonNull(file.getOriginalFilename())) {
            convertedFile = new File(targetPath + file.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                Logger.logError(e);
                throw new SpiceValidation(1514, e.getMessage());
            }
        }
        return convertedFile;
    }

    /**
     * {@inheritDoc}
     */
    public List<Prescription> findByPatientTrackIdAndPatientVisitIdAndIsDeleted(Long patientTrackId,
                                                                                Long patientVisitId, boolean isDeleted) {
        return prescriptionRepository.findByPatientTrackIdAndPatientVisitIdAndIsDeleted(patientTrackId, patientVisitId,
                isDeleted);
    }

    /**
     * {@inheritDoc}
     */
    public int getPrescriptionCount(Date endDate, Long patientTrackId) {
        return prescriptionRepository.getPrecriptionCount(endDate, patientTrackId);
    }

    /**
     * <p>
     * This method is used to construct and saves a list of prescriptions based on the given prescription request
     * and medication information.
     * </p>
     *
     * @param prescriptionList    {@link List<PrescriptionDTO>} The list of Prescription that contains information
     *                            about prescriptions is given
     * @param prescriptionRequest {@link PrescriptionRequestDTO} The prescriptionRequest containing information about
     *                            the prescription request, such as patient information and doctor information is given
     * @param otherMedication     {@link OtherMedicationDTO} The otherMedication contains information about a medication
     *                            that is not in the system's database is given
     * @param signature           {@link String} The signature is a String parameter that represents the signature of
     *                            the person who prescribed the medication is given
     */
    private void constructAndSavePrescriptionList(List<PrescriptionDTO> prescriptionList,
                                                  PrescriptionRequestDTO prescriptionRequest, OtherMedicationDTO otherMedication, String signature) {
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull()).setFullTypeMatchingRequired(true);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Prescription> prescriptionListToCreate = new ArrayList<>();
        List<Prescription> prescriptionListToUpdate = new ArrayList<>();
        for (PrescriptionDTO prescriptionDto : prescriptionList) {
            if (Objects.equals(Constants.BOOLEAN_TRUE, prescriptionDto.getIsDeleted())) {
                throw new DataNotAcceptableException(1503);
            }
            if (prescriptionDto.getPrescribedDays() < Constants.ZERO) {
                throw new DataNotAcceptableException(1504);
            }
            Prescription prescription = new Prescription();
            modelMapper.map(prescriptionRequest, prescription);
            modelMapper.map(prescriptionDto, prescription);
            prescription.setSignature(signature);
            if (prescriptionDto.getMedicationId() == Constants.MINUS_ONE && Objects.nonNull(otherMedication)) {
                prescription.setMedicationId(otherMedication.getId());
                prescription.setBrandName(otherMedication.getBrandName());
                prescription.setClassificationName(otherMedication.getClassificationName());
            }
            prescription.setEndDate(DateUtil.addDate(DateUtil.subtractDates(DateUtil.getCurrentDay(), Constants.ONE),
                    prescriptionDto.getPrescribedDays()));
            if (Objects.isNull(prescriptionDto.getId())) {
                prescription.setRemainingPrescriptionDays(prescription.getPrescribedDays());
                prescription.setPrescriptionFilledDays(Constants.ZERO);
                prescriptionListToCreate.add(prescription);
            } else {
                prescriptionListToUpdate.add(prescription);
            }
        }
        savePrescriptions(prescriptionListToCreate, prescriptionListToUpdate, prescriptionRequest);
    }

    /**
     * <p>
     * This method is used to save prescriptions by adding new ones, updating existing ones, and updating medication
     * prescribed for a patient visit.
     * </p>
     *
     * @param prescriptionListToCreate {@link List<Prescription>} The list of new prescriptions to be created is given
     * @param prescriptionListToUpdate {@link List<Prescription>} The list of Prescription need to be updated in the
     *                                 database is given
     * @param prescriptionRequest      {@link PrescriptionRequestDTO} PrescriptionRequest contains information about the
     *                                 prescription request, including patient information, medication information,
     *                                 and visit information is given
     */
    private void savePrescriptions(List<Prescription> prescriptionListToCreate,
                                   List<Prescription> prescriptionListToUpdate, PrescriptionRequestDTO prescriptionRequest) {
        if (!prescriptionListToCreate.isEmpty()) {
            addNewPrescriptions(prescriptionListToCreate);
        }
        if (!prescriptionListToUpdate.isEmpty()) {
            updatePrescriptions(prescriptionListToUpdate);
        }
        updatePrescriptionPatientVisit(prescriptionRequest);
        updateMedicationPrescribed(prescriptionRequest.getPatientTrackId(), Constants.BOOLEAN_TRUE,
                Constants.BOOLEAN_TRUE);
    }

    /**
     * {@inheritDoc}
     */
    public void removePrescription(long trackerId) {
        List<Prescription> prescriptions = prescriptionRepository.findBypatientTrackId(trackerId);
        if (!Objects.isNull(prescriptions)) {
            prescriptions.forEach(prescription -> {
                prescription.setActive(false);
                prescription.setDeleted(true);
            });
            prescriptionRepository.saveAll(prescriptions);
        }
        List<PrescriptionHistory> prescriptionHistories = prescriptionHistoryRepository
                .findByPatientTrackId(trackerId);
        if (!Objects.isNull(prescriptionHistories)) {
            prescriptionHistories.forEach(prescription -> {
                prescription.setActive(false);
                prescription.setDeleted(true);
            });
            prescriptionHistoryRepository.saveAll(prescriptionHistories);
        }
    }

    /**
     * {@inheritDoc}
     */
    public PrescriberDTO getPatientPrescribedDetails(long patientTrackId, long tenantId) {
        PrescriberDTO prescriber = new PrescriberDTO();
        Prescription prescription = prescriptionRepository
                .findFirstByPatientTrackIdAndTenantIdOrderByUpdatedByDesc(patientTrackId, tenantId);
        if (!Objects.isNull(prescription)) {
            UserListDTO userDto = userApiInterface.getPrescriberDetails(CommonUtil.getAuthToken(),
                    UserSelectedTenantContextHolder.get(), prescription.getCreatedBy());
            PrescriptionHistory presHistory = prescriptionHistoryRepository
                    .findFirstByPatientTrackIdAndPrescriptionFilledDaysGreaterThanOrderByUpdatedAtDesc(patientTrackId,
                            Constants.ZERO);
            if (!Objects.isNull(presHistory)) {
                prescriber.setLastRefillDate(presHistory.getUpdatedAt());
                prescriber.setLastRefillVisitId(presHistory.getPatientVisitId());
            }
            prescriber.setFirstName(userDto.getFirstName());
            prescriber.setLastName(userDto.getLastName());
            prescriber.setCountryCode(userDto.getCountryCode());
            prescriber.setPhoneNumber(userDto.getPhoneNumber());
        }
        return prescriber;
    }
}
