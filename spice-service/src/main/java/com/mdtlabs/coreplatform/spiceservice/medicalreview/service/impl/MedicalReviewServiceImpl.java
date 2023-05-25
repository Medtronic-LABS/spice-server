package com.mdtlabs.coreplatform.spiceservice.medicalreview.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CurrentMedicationDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.DiagnosisDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.InitialMedicalReviewDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientMedicalReviewDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RedRiskDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ReviewerDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Patient;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComorbidity;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComplication;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientCurrentMedication;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientDiagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalReview;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;
import com.mdtlabs.coreplatform.common.model.entity.spice.Prescription;
import com.mdtlabs.coreplatform.common.model.entity.spice.PrescriptionHistory;
import com.mdtlabs.coreplatform.common.model.entity.spice.RedRiskNotification;
import com.mdtlabs.coreplatform.spiceservice.common.RedRiskService;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.PatientMedicalReviewMapper;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientComorbidityRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientComplicationRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientCurrentMedicationRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientDiagnosisRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientLifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.RedRiskNotificationRepository;
import com.mdtlabs.coreplatform.spiceservice.medicalreview.repository.MedicalReviewRepository;
import com.mdtlabs.coreplatform.spiceservice.medicalreview.service.MedicalReviewService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ComplaintsService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CultureService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.PhysicalExaminationService;
import com.mdtlabs.coreplatform.spiceservice.patient.repository.PatientRepository;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.service.PatientLabTestService;
import com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.service.PatientNutritionLifestyleService;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.service.PatientVisitService;
import com.mdtlabs.coreplatform.spiceservice.prescription.service.PrescriptionService;

/**
 * <p>
 * This class implements the medical review service interface and contains
 * actual business logic to perform operations on medical review entity.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 06, 2023
 */
@Service
public class MedicalReviewServiceImpl implements MedicalReviewService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private MedicalReviewRepository medicalReviewRepository;

    @Autowired
    private PatientComorbidityRepository patientComorbidityRepository;

    @Autowired
    private PatientComplicationRepository patientComplicationRepository;

    @Autowired
    private PatientLifestyleRepository patientLifestyleRepository;

    @Autowired
    private PatientCurrentMedicationRepository patientCurrentMedicationRepository;

    @Autowired
    private PatientDiagnosisRepository patientDiagnosisRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientLabTestService patientLabTestService;

    @Autowired
    private ComplaintsService complaintService;

    @Autowired
    private PatientMedicalReviewMapper patientMedicalReviewMapper;

    @Autowired
    private PatientNutritionLifestyleService nutritionLifestyleService;

    @Autowired
    private PatientTrackerService patientTrackerService;

    @Autowired
    private PatientTreatmentPlanService patientTreatmentPlanService;

    @Autowired
    private PatientVisitService patientVisitService;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PhysicalExaminationService physicalExaminationService;

    @Autowired
    private RedRiskService redRiskService;

    @Autowired
    private RedRiskNotificationRepository redRiskNotificationRepository;

    @Autowired
    private CultureService cultureService;

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void addMedicalReview(MedicalReviewDTO medicalReviewDto) {
        Logger.logInfo("Adding medical review.");

        if (Objects.isNull(medicalReviewDto.getPatientTrackId())) {
            throw new BadRequestException(1256);
        }
        if (Objects.isNull(medicalReviewDto.getContinuousMedicalReview())) {
            throw new BadRequestException(6001);
        }
        PatientTracker patientTracker = patientTrackerService
                .getPatientTrackerById(medicalReviewDto.getPatientTrackId());

        if (!Objects.isNull(medicalReviewDto.getInitialMedicalReview())) {
            createPatientInitialEncounter(medicalReviewDto, patientTracker);
        }
        createContinuousMedicalReview(medicalReviewDto, patientTracker);
    }

    /**
     * <p>
     * Creates continuous medical review for given patient.
     * </p>
     *
     * @param medicalReviewDto medicalReviewDTO request date
     * @param patientTracker   PatientTracker
     */
    private void createContinuousMedicalReview(MedicalReviewDTO medicalReviewDto, PatientTracker patientTracker) {
        Logger.logInfo("Creating continuous medical review.");

        if (!patientTracker.isInitialReview()) {
            throw new DataNotAcceptableException(6002);
        }
        PatientMedicalReview patientMedicalReview = patientMedicalReviewMapper.setMedicalReviewDto(medicalReviewDto);
        if (!Objects.isNull(medicalReviewDto.getContinuousMedicalReview().getComplaints())) {
            patientMedicalReview.setComplaints(complaintService
                    .getComplaintsByIds(medicalReviewDto.getContinuousMedicalReview().getComplaints()));
        }
        if (!Objects.isNull(medicalReviewDto.getContinuousMedicalReview().getPhysicalExams())) {
            patientMedicalReview.setPhysicalExams(physicalExaminationService
                    .getPhysicalExaminationByIds(medicalReviewDto.getContinuousMedicalReview().getPhysicalExams()));
        }
        createPatientComorbidity(medicalReviewDto.getContinuousMedicalReview().getComorbidities(), medicalReviewDto.getPatientTrackId(),
                medicalReviewDto.getPatientVisitId(), medicalReviewDto.getTenantId());
        createPatientComplication(medicalReviewDto.getContinuousMedicalReview().getComplications(), medicalReviewDto.getPatientTrackId(),
                medicalReviewDto.getPatientVisitId(), medicalReviewDto.getTenantId());

        patientMedicalReview = medicalReviewRepository.save(patientMedicalReview);
        PatientVisit patientVisit = patientVisitService.getPatientVisitById(medicalReviewDto.getPatientVisitId());
        patientVisit.setMedicalReview(Constants.BOOLEAN_TRUE);
        patientVisitService.updatePatientVisit(patientVisit);
        updatePatientTrackerForMedicalReview(patientTracker, patientMedicalReview);
    }

    /**
     * <p>
     * Creates a patient initial encounter from given patient.
     * </p>
     *
     * @param medicalReview  MedicalReview Request Data
     * @param patientTracker PatientTracker
     */
    private void createPatientInitialEncounter(MedicalReviewDTO medicalReview, PatientTracker patientTracker) {
        Logger.logInfo("Creating patient initial encounter.");
        if (Constants.ENROLLED.equals(patientTracker.getPatientStatus())) {
            int comorbidityCount = Constants.ZERO;
            String diabetesDiagControlledType = Constants.EMPTY;
            String diabetesPatientType = Constants.EMPTY;

            PatientDiagnosis patientDiagnosis = new PatientDiagnosis();
            comorbidityCount = setComorbidityCount(medicalReview, comorbidityCount);

            if (!Objects.isNull(medicalReview.getInitialMedicalReview().getDiagnosis())) {
                diabetesDiagControlledType = setDiabetesDiagControlledType(medicalReview);
                diabetesPatientType = setDiabetesPatientType(medicalReview);

            } else {
                patientDiagnosis = patientDiagnosisRepository.findByPatientTrackIdAndIsActiveAndIsDeleted(
                        patientTracker.getId(), Constants.BOOLEAN_TRUE, Constants.BOOLEAN_FALSE);
                diabetesDiagControlledType = setDiabetesDiagControlledType(diabetesDiagControlledType, patientDiagnosis);
                if (!Objects.isNull(patientDiagnosis.getDiabetesPatientType())) {
                    diabetesPatientType = patientDiagnosis.getDiabetesPatientType();
                }
            }
            setRiskLevel(patientTracker, comorbidityCount, diabetesDiagControlledType,
                    diabetesPatientType, patientDiagnosis);
        }
        updatePregnacyStatus(medicalReview, patientTracker);
        createInitialMedicalReview(medicalReview, patientTracker);
        patientTracker.setInitialReview(Constants.BOOLEAN_TRUE);
        patientTrackerService.addOrUpdatePatientTracker(patientTracker);
    }

    /**
     * <p>
     * Sets diabetes diag controlled type by patient diagnosis.
     * </p>
     *
     * @param diabetesDiagControlledType - diabetes diag controlled type
     * @param patientDiagnosis           - entity
     * @return String - type
     */
    private String setDiabetesDiagControlledType(String diabetesDiagControlledType, PatientDiagnosis patientDiagnosis) {
        if (!Objects.isNull(patientDiagnosis.getDiabetesDiagControlledType())) {
            diabetesDiagControlledType = patientDiagnosis.getDiabetesDiagControlledType();
        }
        return diabetesDiagControlledType;
    }

    /**
     * <p>
     * This method is used to save patient information.
     * </p>
     *
     * @param medicalReview  - medical review dto
     * @param patientTracker - entity
     */
    private void updatePregnacyStatus(MedicalReviewDTO medicalReview, PatientTracker patientTracker) {
        if (!Objects.isNull(medicalReview.getInitialMedicalReview().getIsPregnant())) {
            if (!Objects.isNull(patientTracker.getPatientId())) {
                Patient patient = patientRepository.findByIdAndIsDeleted(patientTracker.getPatientId(), false);
                if (Objects.isNull(patient)) {
                    Logger.logError(("Patient not found!"));
                    throw new DataNotFoundException(1201);
                }
                patient.setIsPregnant(medicalReview.getInitialMedicalReview().getIsPregnant());
                patientRepository.save(patient);
            }
            patientTracker.setIsPregnant(medicalReview.getInitialMedicalReview().getIsPregnant());
        }
    }

    /**
     * <p>
     * Sets risk level based on various values.
     * </p>
     *
     * @param patientTracker             - entity
     * @param comorbidityCount           - comorbidity count
     * @param diabetesDiagControlledType - controlled type
     * @param diabetesPatientType        - patient type
     * @param patientDiagnosis           - entity
     */
    private void setRiskLevel(PatientTracker patientTracker, int comorbidityCount, String diabetesDiagControlledType,
                              String diabetesPatientType, PatientDiagnosis patientDiagnosis) {
        String riskLevel = redRiskService.getPatientRiskLevel(patientTracker, patientDiagnosis,
                new RedRiskDTO(comorbidityCount, diabetesDiagControlledType, diabetesPatientType));

        if (!StringUtils.isBlank(riskLevel)) {
            patientTracker.setRiskLevel(riskLevel);
        }
    }

    /**
     * <p>
     * Sets diabetes patient type by medical review.
     * </p>
     *
     * @param medicalReview - medical review dto
     * @return String - patient type
     */
    private String setDiabetesPatientType(MedicalReviewDTO medicalReview) {
        String diabetesPatientType;
        diabetesPatientType = Objects
                .isNull(medicalReview.getInitialMedicalReview().getDiagnosis().getDiabetesPatientType())
                ? Constants.EMPTY
                : medicalReview.getInitialMedicalReview().getDiagnosis().getDiabetesPatientType();
        return diabetesPatientType;
    }

    /**
     * <p>
     * Sets diabetes patient type by medical review.
     * </p>
     *
     * @param medicalReview - medical review dto
     * @return String - patient type
     */
    private String setDiabetesDiagControlledType(MedicalReviewDTO medicalReview) {
        String diabetesDiagControlledType;
        diabetesDiagControlledType = Objects.isNull(
                medicalReview.getInitialMedicalReview().getDiagnosis().getDiabetesDiagControlledType())
                ? Constants.EMPTY
                : medicalReview.getInitialMedicalReview().getDiagnosis()
                .getDiabetesDiagControlledType();
        return diabetesDiagControlledType;
    }

    /**
     * <p>
     * Sets comorbidity count by medical review dto.
     * </p>
     *
     * @param medicalReview    - medical review dto
     * @param comorbidityCount - comorbidity count
     * @return int - count
     */
    private int setComorbidityCount(MedicalReviewDTO medicalReview, int comorbidityCount) {
        if (!Objects.isNull(medicalReview.getInitialMedicalReview().getComorbidities())) {
            comorbidityCount = medicalReview.getInitialMedicalReview().getComorbidities().stream()
                    .filter(comorbidity -> Objects.isNull(comorbidity.getOtherComorbidity())
                            || comorbidity.getOtherComorbidity().isBlank())
                    .toList().size();
        }
        return comorbidityCount;
    }

    /**
     * <p>
     * Creates initial medical review.
     * </p>
     *
     * @param medicalReview MedicalReview Request Data
     */
    private void createInitialMedicalReview(MedicalReviewDTO medicalReview, PatientTracker patientTracker) {
        Logger.logInfo("Creating initial medical review.");
        InitialMedicalReviewDTO initialMedicalReview = medicalReview.getInitialMedicalReview();
        createPatientCurrentMedication(initialMedicalReview.getCurrentMedications(),
                medicalReview.getPatientTrackId(), medicalReview.getPatientVisitId(), medicalReview.getTenantId());
        createPatientComorbidity(initialMedicalReview.getComorbidities(), medicalReview.getPatientTrackId(),
                medicalReview.getPatientVisitId(), medicalReview.getTenantId());
        createPatientComplication(initialMedicalReview.getComplications(), medicalReview.getPatientTrackId(),
                medicalReview.getPatientVisitId(), medicalReview.getTenantId());
        createPatientDiagnosis(initialMedicalReview.getDiagnosis(), patientTracker,
                medicalReview.getPatientVisitId(), medicalReview.getTenantId());
        createPatientLifestyle(initialMedicalReview.getLifestyle(), medicalReview.getPatientTrackId(),
                medicalReview.getPatientVisitId(), medicalReview.getTenantId());
    }

    /**
     * <p>
     * Create patient life style for given track id and visit id.
     * </p>
     *
     * @param lifestyles     set of lifestyleDTO
     * @param patientTrackId patientTrackId
     * @param patientVisitId patientVisitId
     * @param tenantId       tenantId
     */
    private void createPatientLifestyle(Set<PatientLifestyle> lifestyles, Long patientTrackId, Long patientVisitId,
                                        Long tenantId) {
        if (!Objects.isNull(lifestyles) && !lifestyles.isEmpty()) {
            lifestyles.forEach(patientLifestyle -> {
                patientLifestyle.setTenantId(tenantId);
                patientLifestyle.setPatientTrackId(patientTrackId);
                patientLifestyle.setPatientVisitId(patientVisitId);
            });
            patientLifestyleRepository.saveAll(lifestyles);
        }
    }

    /**
     * <p>
     * Create Patient comorbiditySet for given track id and visit id.
     * </p>
     *
     * @param comorbiditySet set of ComorbidityDTO
     * @param patientTrackId patientTrackId
     * @param patientVisitId patientVisitId
     * @param tenantId       tenantId
     */
    private void createPatientComorbidity(Set<PatientComorbidity> comorbiditySet, Long patientTrackId,
                                          Long patientVisitId, Long tenantId) {
        if (!Objects.isNull(comorbiditySet) && !comorbiditySet.isEmpty()) {
            comorbiditySet.forEach(patientComorbidity -> {
                patientComorbidity.setTenantId(tenantId);
                patientComorbidity.setPatientTrackId(patientTrackId);
                patientComorbidity.setPatientVisitId(patientVisitId);
            });
            patientComorbidityRepository.saveAll(comorbiditySet);
        }
    }

    /**
     * <p>
     * Create PatientComplication for given track id and visit id.
     * </p>
     *
     * @param complications  set of ComplicationDTO
     * @param patientTrackId patientTrackId
     * @param patientVisitId patientVisitId
     * @param tenantId       tenantId
     */
    private void createPatientComplication(Set<PatientComplication> complications, Long patientTrackId,
                                           Long patientVisitId, Long tenantId) {
        if (!Objects.isNull(complications) && !complications.isEmpty()) {
            complications.forEach(patientComplication -> {
                patientComplication.setTenantId(tenantId);
                patientComplication.setPatientTrackId(patientTrackId);
                patientComplication.setPatientVisitId(patientVisitId);
            });
            patientComplicationRepository.saveAll(complications);
        }
    }

    private void createPatientDiagnosis(PatientDiagnosis patientDiagnosis, PatientTracker patientTracker,
                                        Long patientVisitId, Long tenantId) {
        if (!Objects.isNull(patientDiagnosis)) {
            patientDiagnosis.setPatientTrackId(patientTracker.getId());
            patientDiagnosis.setPatientVisitId(patientVisitId);
            patientDiagnosis.setTenantId(tenantId);
            PatientDiagnosis existingPatientDiagnosis = patientDiagnosisRepository.findByPatientTrackIdAndIsActiveAndIsDeleted(patientTracker.getId(), Constants.BOOLEAN_TRUE, Constants.BOOLEAN_FALSE);
            if (Objects.isNull(existingPatientDiagnosis)) {
                patientDiagnosisRepository.save(patientDiagnosis);
            } else {
                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                modelMapper.map(patientDiagnosis, existingPatientDiagnosis);
                patientDiagnosisRepository.save(existingPatientDiagnosis);
            }
            patientTracker.setIsHtnDiagnosis(patientDiagnosis.getIsHtnDiagnosis());
            patientTracker.setIsDiabetesDiagnosis(patientDiagnosis.getIsDiabetesDiagnosis());
        }
    }

    /**
     * <p>
     * Create PatientCurrentMedication for given track id and visit id.
     * </p>
     *
     * @param currentMedication CurrentMedicationDetailsDTO
     * @param patientTrackId    patientTrackId
     * @param patientVisitId    patientVisitId
     * @param tenantId          tenantId
     */
    private void createPatientCurrentMedication(CurrentMedicationDetailsDTO currentMedication, Long patientTrackId,
                                                Long patientVisitId, Long tenantId) {
        if (!Objects.isNull(currentMedication)
                && (!Objects.isNull(currentMedication.getMedications()))
                && !currentMedication.getMedications().isEmpty()) {
            Set<PatientCurrentMedication> patientCurrentMedications = currentMedication.getMedications().stream()
                    .map(medication -> new PatientCurrentMedication(tenantId, medication.getCurrentMedicationId(),
                            patientTrackId, patientVisitId, currentMedication.isDrugAllergies(),
                            currentMedication.isAdheringCurrentMed(), currentMedication.getAdheringMedComment(),
                            currentMedication.getAllergiesComment()))
                    .collect(Collectors.toSet());
            patientCurrentMedicationRepository.saveAll(patientCurrentMedications);
        }
    }

    /**
     * {@inheritDoc}
     */
    public MedicalReviewResponseDTO getMedicalReviewHistory(RequestDTO medicalReviewListDto) {
        if (Objects.isNull(medicalReviewListDto.getPatientTrackId())) {
            throw new DataNotAcceptableException(1256);
        }
        List<Map<String, Object>> patientVisits = new ArrayList<>();
        if (medicalReviewListDto.isLatestRequired() && Objects.isNull(medicalReviewListDto.getPatientVisitId())) {
            List<PatientVisit> existingPatientVisits = patientVisitService
                    .getPatientVisitDates(medicalReviewListDto.getPatientTrackId(), null, Constants.BOOLEAN_TRUE, null);

            medicalReviewListDto.setPatientVisitId(Constants.ZERO != existingPatientVisits.size()
                    ? existingPatientVisits.get(existingPatientVisits.size() - Constants.ONE).getId()
                    : null);

            existingPatientVisits.forEach(patientVisit -> patientVisits.add(
                    Map.of(Constants.ID, patientVisit.getId(), Constants.VISIT_DATE, patientVisit.getVisitDate())));
        }
        List<PatientMedicalReview> patientMedicalReviews = medicalReviewRepository.getPatientMedicalReview(
                medicalReviewListDto.getPatientTrackId(), medicalReviewListDto.getPatientVisitId());
        List<PatientMedicalReviewDTO> patientMedicalReviewDTOS = constructMedicalReviewResponse(patientMedicalReviews);
        return new MedicalReviewResponseDTO(patientVisits, patientMedicalReviewDTOS);
    }

    /**
     * {@inheritDoc}
     */
    public MedicalReviewResponseDTO getMedicalReviewSummaryHistory(RequestDTO medicalReviewListDto) {
        if (Objects.isNull(medicalReviewListDto.getPatientTrackId())) {
            throw new DataNotAcceptableException(1256);
        }

        List<Map<String, Object>> patientVisits = new ArrayList<>();
        PatientVisit latestPatientVisit = null;

        if (medicalReviewListDto.isLatestRequired()) {
            List<PatientVisit> existingPatientVisits = medicalReviewListDto.isMedicalReviewSummary()
                    ? patientVisitService.getPatientVisitDates(medicalReviewListDto.getPatientTrackId(), null,
                    Constants.BOOLEAN_TRUE, null)
                    : patientVisitService.getPatientVisitDates(medicalReviewListDto.getPatientTrackId());
            if (existingPatientVisits.isEmpty()) {
                return new MedicalReviewResponseDTO();
            }
            existingPatientVisits.forEach(patientVisit -> patientVisits
                    .add(Map.of(Constants.ID, patientVisit.getId(), Constants.VISIT_DATE, patientVisit.getVisitDate())));
            latestPatientVisit = existingPatientVisits.get(existingPatientVisits.size() - Constants.ONE);
            medicalReviewListDto.setPatientVisitId(latestPatientVisit.getId());
        }
        if (Objects.isNull(latestPatientVisit)) {
            latestPatientVisit = patientVisitService.getPatientVisitById(medicalReviewListDto.getPatientVisitId());
        }

        return getMedicalReviewSummary(medicalReviewListDto, latestPatientVisit, patientVisits);
    }

    /**
     * This method is used to get medical review summary.
     *
     * @param medicalReviewListDto - entity
     * @param patientVisit         - entity
     * @return MedicalReviewResponseDTO - entity
     */
    private MedicalReviewResponseDTO getMedicalReviewSummary(RequestDTO medicalReviewListDto, PatientVisit patientVisit,
                                                             List<Map<String, Object>> patientVisits) {
        MedicalReviewResponseDTO medicalReviewResponse = new MedicalReviewResponseDTO();
        medicalReviewResponse.setMedicalReviews(new ArrayList<>());
        if (patientVisit.isMedicalReview()) {
            List<PatientMedicalReview> patientMedicalReviews = medicalReviewRepository.getPatientMedicalReview(
                    medicalReviewListDto.getPatientTrackId(), medicalReviewListDto.getPatientVisitId());
            medicalReviewResponse.setMedicalReviews(constructMedicalReviewResponse(patientMedicalReviews));
        }
        medicalReviewResponse.setInvestigations(
                patientVisit.isInvestigation()
                        ? (patientLabTestService.getPatientLabTest(medicalReviewListDto.getPatientTrackId(),
                        patientVisit.getId()))
                        : new ArrayList<>());
        if (medicalReviewListDto.isDetailedSummaryRequired()) {
            constructDetailedSummaryResponse(patientVisit, medicalReviewListDto, medicalReviewResponse);
        }
        if (!medicalReviewListDto.isDetailedSummaryRequired() && patientVisit.isPrescription()) {
            medicalReviewResponse.setPrescriptions(getPrescriptionHistory(patientVisit).stream()
                    .map(prescription -> modelMapper.map(prescription, PrescriptionResponseDTO.class))
                    .toList());
        }
        if (!medicalReviewListDto.isDetailedSummaryRequired() && (patientVisit.isPrescription() || patientVisit.isInvestigation())) {
            medicalReviewResponse.setReviewedAt(patientVisit.getCreatedAt());
        }
        medicalReviewResponse.setPatientReviewDates(patientVisits);
        return medicalReviewResponse;
    }

    /**
     * <p>
     * This method constructs the medical review response.
     * </p>
     *
     * @param patientMedicalReviews patientMedicalReviews
     * @return list of PatientMedicalReviewDTO
     */
    private List<PatientMedicalReviewDTO> constructMedicalReviewResponse(List<PatientMedicalReview> patientMedicalReviews) {
        List<PatientMedicalReviewDTO> patientMedicalReviewDTOS = new ArrayList<>();
        if (!Objects.isNull(patientMedicalReviews)) {
            Set<Map<String, String>> physicalExams;
            Set<Map<String, String>> complaints;
            Long cultureId = cultureService.loadCultureValues();
            Map<Long, String> physicalExamCultureMap = Constants.CULTURE_VALUES_MAP.get(cultureId).get(Constants.CULTURE_VALUE_PHYSICAL_EXAMINATION);
            Map<Long, String> compliantsCultureMap = Constants.CULTURE_VALUES_MAP.get(cultureId).get(Constants.CULTURE_VALUE_COMPLAINTS);
            for (PatientMedicalReview patientMedicalReview : patientMedicalReviews) {
                physicalExams = patientMedicalReview
                        .getPhysicalExams().stream().map(physicalExam -> Map.of(FieldConstants.NAME,
                                physicalExam.getName(), Constants.CULTURE_VALUE, physicalExamCultureMap.get(physicalExam.getId())))
                        .collect(Collectors.toSet());
                complaints = patientMedicalReview.getComplaints().stream().map(comp -> Map.of(FieldConstants.NAME, comp.getName(), Constants.CULTURE_VALUE, compliantsCultureMap.get(comp.getId())))
                        .collect(Collectors.toSet());
                patientMedicalReviewDTOS.add(new PatientMedicalReviewDTO(patientMedicalReview.getId(),
                        patientMedicalReview.getPatientVisitId(), physicalExams, complaints,
                        patientMedicalReview.getPhysicalExamComments(), patientMedicalReview.getCompliantComments(),
                        patientMedicalReview.getCreatedAt(), patientMedicalReview.getClinicalNote()));
            }
        }
        return patientMedicalReviewDTOS;
    }

    /**
     * <p>
     * Gets a prescription history by patient visit.
     * </p>
     *
     * @param patientVisit patientVisit
     * @return list of PrescriptionHistory
     */
    private List<PrescriptionHistory> getPrescriptionHistory(PatientVisit patientVisit) {
        return prescriptionService.getPrescriptions(patientVisit.getId());
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Integer> getPrescriptionAndLabtestCount(RequestDTO request) {
        if (Objects.isNull(request.getPatientTrackId())) {
            throw new DataNotAcceptableException(1256);
        }
        int prescriptionDaysCompletedCount = prescriptionService.getPrescriptionCount(new Date(),
                request.getPatientTrackId());
        int nonReviewedTestCount = patientLabTestService.getLabTestCount(request.getPatientTrackId());
        int nutritionLifestyleReviewedCount = nutritionLifestyleService
                .getNutritionLifestyleReviewedCount(request.getPatientTrackId());
        return Map.of(Constants.COUNT_PRESCRIPTION_DAYS_COMPLETED, prescriptionDaysCompletedCount,
                Constants.COUNT_NON_REVIED_TEST, nonReviewedTestCount, Constants.COUNT_NUTRITION_LIFESTYLE_REVIED,
                nutritionLifestyleReviewedCount);
    }

    /**
     * <p>
     * Constructs MedicalReviewResponseDTO for detailed summary.
     * </p>
     *
     * @param patientVisit          patientVisit
     * @param medicalReviewListDto  medicalReviewListDto
     * @param medicalReviewResponse medicalReviewResponse
     */
    private void constructDetailedSummaryResponse(PatientVisit patientVisit,
                                                  RequestDTO medicalReviewListDto, MedicalReviewResponseDTO medicalReviewResponse) {
        if (patientVisit.isPrescription()) {
            List<Prescription> prescriptions = prescriptionService.findByPatientTrackIdAndPatientVisitIdAndIsDeleted(
                    medicalReviewListDto.getPatientTrackId(), patientVisit.getId(), Constants.BOOLEAN_FALSE);
            medicalReviewResponse.setPrescriptions(prescriptions.stream()
                    .map(prescription -> modelMapper.map(prescription, PrescriptionResponseDTO.class))
                    .toList());
            medicalReviewResponse.setIsSigned(!prescriptions.isEmpty());
        }
        PatientTracker patientTracker = patientTrackerService
                .getPatientTrackerById(medicalReviewListDto.getPatientTrackId());

        medicalReviewResponse.setPatientDetails(new DiagnosisDetailsDTO(patientTracker.getIsConfirmDiagnosis(),
                patientTracker.getProvisionalDiagnosis(), patientTracker.getConfirmDiagnosis()));

        PatientTreatmentPlan treatmentPlan = patientTreatmentPlanService
                .getPatientTreatmentPlanDetails(medicalReviewListDto.getPatientTrackId());

        if (!Objects.isNull(treatmentPlan)) {
            medicalReviewResponse.setMedicalReviewFrequency(treatmentPlan.getMedicalReviewFrequency());
        }
        medicalReviewResponse.setReviewedAt(patientVisit.getCreatedAt());
        UserDTO userDTO = UserContextHolder.getUserDto();
        medicalReviewResponse.setReviewerDetails(
                new ReviewerDetailsDTO(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getUsername()));

    }

    /**
     * <p>
     * Updates patient tracker for medical review.
     * </p>
     *
     * @param patientTracker       - Patient trackId
     * @param patientMedicalReview - Patient medical review details
     */
    private void updatePatientTrackerForMedicalReview(PatientTracker patientTracker,
                                                      PatientMedicalReview patientMedicalReview) {
        if (!Constants.ENROLLED.equals(patientTracker.getPatientStatus())) {
            patientTracker.setPatientStatus(Constants.OBSERVATION);
            patientTracker.setIsObservation(Constants.BOOLEAN_TRUE);
        } else if (patientTracker.isRedRiskPatient()) {
            List<RedRiskNotification> redRiskNotification = redRiskNotificationRepository
                    .findByPatientTrackIdAndIsDeletedFalseAndStatusIgnoreCase(patientTracker.getId(), Constants.NEW);

            if (!Objects.isNull(redRiskNotification)) {
                redRiskNotification.forEach(redRisk -> redRisk.setStatus(Constants.MEDICAL_REVIEW_COMPLETED));
                redRiskNotificationRepository.saveAll(redRiskNotification);
            }
            patientTracker.setRedRiskPatient(false);
        }
        PatientTreatmentPlan patientTreatmentPlan = patientTreatmentPlanService
                .getPatientTreatmentPlan(patientTracker.getId());

        if (!Objects.isNull(patientTreatmentPlan)) {
            patientTracker.setNextMedicalReviewDate(patientTreatmentPlanService
                    .getTreatmentPlanFollowupDate(patientTreatmentPlan.getMedicalReviewFrequency(), Constants.DEFAULT));
        }
        patientTracker.setLastReviewDate(patientMedicalReview.getCreatedAt());
        patientTrackerService.addOrUpdatePatientTracker(patientTracker);
    }

    public void removeMedicalReview(long trackerId) {
        List<PatientMedicalReview> medicalReviews = medicalReviewRepository.findByPatientTrackId(trackerId);
        if (!Objects.isNull(medicalReviews)) {
            medicalReviews.forEach(medicalReview -> {
                medicalReview.setActive(false);
                medicalReview.setDeleted(true);
            });
            medicalReviewRepository.saveAll(medicalReviews);
        }
        removePatientLifestyle(trackerId);
        removePatientComorbidity(trackerId);
        removePatientComplication(trackerId);
        removePatientCurrentMedication(trackerId);
        removePatientDiagnosis(trackerId);
    }

    private void removePatientLifestyle(long trackerId) {
        List<PatientLifestyle> patientLifeStyles = patientLifestyleRepository.findByPatientTrackId(trackerId);
        if (!Objects.isNull(patientLifeStyles)) {
            patientLifeStyles.forEach(lifeStyle -> {
                lifeStyle.setActive(false);
                lifeStyle.setDeleted(true);
            });
            patientLifestyleRepository.saveAll(patientLifeStyles);
        }
    }

    /**
     * <p>
     * To remove comorbidity details based on tracker id.
     * </p>
     *
     * @param trackerId trackerId
     */
    private void removePatientComorbidity(long trackerId) {
        List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository
                .findByPatientTrackId(trackerId);
        if (!Objects.isNull(patientComorbidityList)) {
            patientComorbidityList.forEach(patientComorbidity -> {
                patientComorbidity.setActive(false);
                patientComorbidity.setDeleted(true);
            });
            patientComorbidityRepository.saveAll(patientComorbidityList);
        }
    }

    /**
     * <p>
     * To remove patient complication details based on tracker id.
     * </p>
     *
     * @param trackerId trackerId
     */
    private void removePatientComplication(long trackerId) {
        List<PatientComplication> patientComplications = patientComplicationRepository
                .findByPatientTrackId(trackerId);
        if (!Objects.isNull(patientComplications)) {
            patientComplications.forEach(patientComplication -> {
                patientComplication.setActive(false);
                patientComplication.setDeleted(true);
            });
            patientComplicationRepository.saveAll(patientComplications);
        }
    }

    /**
     * <p>
     * To remove medication details based on tracker id.
     * </p>
     *
     * @param trackerId trackerId
     */
    private void removePatientCurrentMedication(long trackerId) {
        List<PatientCurrentMedication> patientCurrentMedications = patientCurrentMedicationRepository
                .findBypatientTrackId(trackerId);
        if (!Objects.isNull(patientCurrentMedications)) {
            patientCurrentMedications.forEach(patientCurrentMedication -> {
                patientCurrentMedication.setActive(false);
                patientCurrentMedication.setDeleted(true);
            });
            patientCurrentMedicationRepository.saveAll(patientCurrentMedications);
        }
    }

    /**
     * <p>
     * To remove diagnosis details based on tracker id.
     * </p>
     *
     * @param trackerId trackerId
     */
    private void removePatientDiagnosis(long trackerId) {
        List<PatientDiagnosis> patientDiagnosis = patientDiagnosisRepository.findByPatientTrackId(trackerId);
        if (!Objects.isNull(patientDiagnosis)) {
            patientDiagnosis.forEach(diagnosis -> {
                diagnosis.setActive(false);
                diagnosis.setDeleted(true);
            });
            patientDiagnosisRepository.saveAll(patientDiagnosis);
        }
    }
}
