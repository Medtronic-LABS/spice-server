package com.mdtlabs.coreplatform.spiceservice.patienttransfer.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTransferRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTransferUpdateRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.CustomizedModule;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientAssessment;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComorbidity;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComplication;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientCurrentMedication;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientDiagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTestResult;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalCompliance;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalReview;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientNutritionLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientPregnancyDetails;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientSymptom;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTransfer;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;
import com.mdtlabs.coreplatform.common.model.entity.spice.Prescription;
import com.mdtlabs.coreplatform.common.model.entity.spice.PrescriptionHistory;
import com.mdtlabs.coreplatform.common.model.entity.spice.RedRiskNotification;
import com.mdtlabs.coreplatform.common.model.enumeration.spice.PatientTransferStatus;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.spiceservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceservice.assessment.repository.PatientAssessmentRepository;
import com.mdtlabs.coreplatform.spiceservice.bplog.repository.BpLogRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientComorbidityRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientComplicationRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientCurrentMedicationRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientDiagnosisRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientLifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.RedRiskNotificationRepository;
import com.mdtlabs.coreplatform.spiceservice.customizedmodules.repository.CustomizedModuleRepository;
import com.mdtlabs.coreplatform.spiceservice.glucoselog.repository.GlucoseLogRepository;
import com.mdtlabs.coreplatform.spiceservice.medicalreview.repository.MedicalReviewRepository;
import com.mdtlabs.coreplatform.spiceservice.mentalhealth.repository.MentalHealthRepository;
import com.mdtlabs.coreplatform.spiceservice.patient.repository.PatientPregnancyDetailsRepository;
import com.mdtlabs.coreplatform.spiceservice.patient.repository.PatientRepository;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.repository.PatientLabTestRepository;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.repository.PatientLabTestResultRepository;
import com.mdtlabs.coreplatform.spiceservice.patientmedicalcompliance.repository.PatientMedicalComplianceRepository;
import com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.repository.PatientNutritionLifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.patientsymptom.repository.PatientSymptomRepository;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.repository.PatientTrackerRepository;
import com.mdtlabs.coreplatform.spiceservice.patienttransfer.repository.PatientTransferRepository;
import com.mdtlabs.coreplatform.spiceservice.patienttransfer.service.PatientTransferService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.repository.PatientTreatmentPlanRepository;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.repository.PatientVisitRepository;
import com.mdtlabs.coreplatform.spiceservice.prescription.repository.PrescriptionHistoryRepository;
import com.mdtlabs.coreplatform.spiceservice.prescription.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class PatientTransferServiceImpl implements PatientTransferService {

    @Autowired
    DataSource dataSource;

    @Autowired
    private PatientTransferRepository patientTransferRepository;

    @Autowired
    private PatientTrackerRepository patientTrackerRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private GlucoseLogRepository glucoseLogRepository;

    @Autowired
    private BpLogRepository bpLogRepository;

    @Autowired
    private RedRiskNotificationRepository redRiskNotificationRepository;

    @Autowired
    private CustomizedModuleRepository customizedModuleRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionHistoryRepository prescriptionHistoryRepository;

    @Autowired
    private PatientLabTestRepository patientLabTestRepository;

    @Autowired
    private PatientLabTestResultRepository patientLabTestResultRepository;

    @Autowired
    private PatientLifestyleRepository patientLifestyleRepository;

    @Autowired
    private PatientNutritionLifestyleRepository patientNutritionLifestyleRepository;

    @Autowired
    private MentalHealthRepository mentalHealthRepository;

    @Autowired
    private PatientVisitRepository patientVisitRepository;

    @Autowired
    private PatientAssessmentRepository patientAssessmentRepository;

    @Autowired
    private MedicalReviewRepository medicalReviewRepository;

    @Autowired
    private PatientComorbidityRepository patientComorbidityRepository;

    @Autowired
    private PatientComplicationRepository patientComplicationRepository;

    @Autowired
    private PatientCurrentMedicationRepository patientCurrentMedicationRepository;

    @Autowired
    private PatientDiagnosisRepository patientDiagnosisRepository;

    @Autowired
    private PatientMedicalComplianceRepository patientMedicalComplianceRepository;

    @Autowired
    private PatientPregnancyDetailsRepository patientPregnancyDetailsRepository;

    @Autowired
    private PatientSymptomRepository patientSymptomRepository;

    @Autowired
    private PatientTreatmentPlanRepository patientTreatmentPlanRepository;

    @Autowired
    private UserApiInterface userApiInterface;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createPatientTransfer(PatientTransferRequestDTO patientTransferDto) {
        Long patientTrackId = patientTransferDto.getPatientTrackId();
        RequestDTO requestDto = new RequestDTO();
        requestDto.setPatientTrackId(patientTrackId);
        this.validatePatientTransfer(requestDto);
        PatientTransfer patientTransfer = new PatientTransfer();
        patientTransfer.setPatientTracker(new PatientTracker(patientTrackId));
        patientTransfer.setTransferSite(new Site(patientTransferDto.getTransferSite()));
        patientTransfer.setOldSite(new Site(patientTransferDto.getOldSite()));
        patientTransfer.setTransferTo(new User(patientTransferDto.getTransferTo()));
        patientTransfer.setTransferBy(new User(UserContextHolder.getUserDto().getId()));
        patientTransfer.setTransferReason(patientTransferDto.getTransferReason());
        patientTransfer.setTransferStatus(PatientTransferStatus.PENDING);
        patientTransfer.setTenantId(UserSelectedTenantContextHolder.get());
        patientTransferRepository.save(patientTransfer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validatePatientTransfer(RequestDTO requestDTO) {
        Long patientTrackId = requestDTO.getPatientTrackId();
        if (Objects.isNull(patientTrackId)) {
            throw new DataNotAcceptableException(1256);
        }
        PatientTracker patientTracker = patientTrackerRepository
                .findByIdAndIsDeleted(patientTrackId, false);
        if (Objects.isNull(patientTracker)) {
            throw new DataNotAcceptableException(1252);
        }
        if (!patientTracker.getPatientStatus().equals(Constants.ENROLLED)) {
            throw new DataNotAcceptableException(16006);
        }
        PatientTransfer existingPatientTransfer = patientTransferRepository
                .findByPatientTrackIdAndTransferStatus(patientTrackId, PatientTransferStatus.PENDING);
        if (!Objects.isNull(existingPatientTransfer)) {
            throw new DataNotAcceptableException(16007);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Long> getPatientTransferCount(RequestDTO requestDTO) {
        Long siteId = requestDTO.getSiteId();
        if (Objects.isNull(siteId)) {
            throw new DataNotAcceptableException(16008);
        }
        Map<String, Long> notification = new HashMap<>();
        notification.put(Constants.COUNT,
                patientTransferRepository.getPatientTransferCount(siteId, UserContextHolder.getUserDto().getId()));
        return notification;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getPatientTransferList(RequestDTO requestDTO) {
        Long siteId = requestDTO.getSiteId();
        if (Objects.isNull(siteId)) {
            throw new DataNotAcceptableException(16008);
        }
        Map<String, Object> data = new HashMap<>();
        Long userId = UserContextHolder.getUserDto().getId();
        List<PatientTransfer> incomingList = patientTransferRepository.getIncomingList(siteId, userId,
                PatientTransferStatus.PENDING);
        List<PatientTransfer> outgoingList = patientTransferRepository.getOutgoingList(siteId, userId);
        List<Map<String, Object>> incomingTransferList = this.getPopulatedTransferList(incomingList);
        List<Map<String, Object>> outgoingTransferList = this.getPopulatedTransferList(outgoingList);
        data.put("incomingPatientList", incomingTransferList);
        data.put("outgoingPatientList", outgoingTransferList);
        return data;
    }

    /**
     * This method is used to get populated transfer list.
     *
     * @param transferList transferList
     */
    private List<Map<String, Object>> getPopulatedTransferList(List<PatientTransfer> transferList) {
        List<Map<String, Object>> patientTransferList = new ArrayList<>();
        for (PatientTransfer patientTransfer : transferList) {
            Map<String, Object> data = new HashMap<>();
            data.put(Constants.ID, patientTransfer.getId());
            data.put(Constants.TENANT_IDS_CLAIM, patientTransfer.getTenantId());
            data.put(Constants.PATIENT, this.getPopulatedPatientData(patientTransfer));
            data.put(Constants.TRANSFERSTATUS, patientTransfer.getTransferStatus());
            data.put(Constants.TRANSFERREASON, patientTransfer.getTransferReason());
            data.put(Constants.TRANSFERBY, this.getPopulatedUser(patientTransfer, true));
            data.put(Constants.TRANSFERTO, this.getPopulatedUser(patientTransfer, false));
            data.put(Constants.OLDSITE, this.getPopulatedSite(patientTransfer, true));
            data.put(Constants.TRANSFERSITE, this.getPopulatedSite(patientTransfer, false));
            patientTransferList.add(data);
        }
        return patientTransferList;
    }

    /**
     * This method is used to get populated patient data.
     *
     * @param patientTransfer patientTransfer
     */
    private Map<String, Object> getPopulatedPatientData(PatientTransfer patientTransfer) {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> pregnancyData = new HashMap<>();
        data.put(Constants.ID, patientTransfer.getPatientTracker().getId());
        data.put(Constants.PARAM_FIRST_NAME, patientTransfer.getPatientTracker().getFirstName().toUpperCase());
        data.put(Constants.PARAM_LAST_NAME, patientTransfer.getPatientTracker().getLastName().toUpperCase());
        data.put(Constants.GENDER, patientTransfer.getPatientTracker().getGender());
        data.put(Constants.AGE, patientTransfer.getPatientTracker().getAge());
        data.put(Constants.PARAM_PHONE_NUMBER, patientTransfer.getPatientTracker().getPhoneNumber());
        data.put(Constants.PARAM_NATIONAL_ID, patientTransfer.getPatientTracker().getNationalId());
        data.put(Constants.PROGRAM_ID, patientTransfer.getPatientTracker().getProgramId());
        data.put(Constants.PROVISIONALDIAGNOSIS, patientTransfer.getPatientTracker().getProvisionalDiagnosis());
        data.put(Constants.CONFIRMDIAGNOSIS, patientTransfer.getPatientTracker().getConfirmDiagnosis());
        data.put(Constants.CVDRISKLEVEL, patientTransfer.getPatientTracker().getCvdRiskLevel());
        data.put(Constants.CVDRISKSCORE, patientTransfer.getPatientTracker().getCvdRiskScore());
        data.put(Constants.ISREDRISKPATIENT, patientTransfer.getPatientTracker().isRedRiskPatient());
        data.put(Constants.ENROLLMENTAT, patientTransfer.getPatientTracker().getEnrollmentAt());
        data.put(Constants.BMI, patientTransfer.getPatientTracker().getBmi());
        pregnancyData.put(Constants.LASTMENSTRUALPERIODDATE,
                patientTransfer.getPatientTracker().getLastMenstrualPeriodDate());
        pregnancyData.put(Constants.ESTIMATEDDELIVERYDATE,
                patientTransfer.getPatientTracker().getEstimatedDeliveryDate());
        data.put(Constants.PREGNANCYDETAILS, pregnancyData);
        return data;
    }

    /**
     * This method is used to get populated transfer list.
     */
    private Map<String, Object> getPopulatedUser(PatientTransfer patientTransfer, boolean isTransferBy) {
        Map<String, Object> data = new HashMap<>();
        data.put("firstName",
                isTransferBy ? patientTransfer.getTransferBy().getFirstName() : patientTransfer.getTransferTo().getFirstName());
        data.put("lastName",
                isTransferBy ? patientTransfer.getTransferBy().getLastName() : patientTransfer.getTransferTo().getLastName());
        return data;
    }

    /**
     * This method is used to get populated site.
     *
     * @param patientTransfer patientTransfer
     * @param isOldSite       isOldSite
     */
    private Map<String, Object> getPopulatedSite(PatientTransfer patientTransfer, boolean isOldSite) {
        Map<String, Object> data = new HashMap<>();
        data.put(Constants.ID, isOldSite ? patientTransfer.getOldSite().getId() : patientTransfer.getTransferSite().getId());
        data.put(Constants.NAME, isOldSite ? patientTransfer.getOldSite().getName() : patientTransfer.getTransferSite().getName());
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void updatePatientTransfer(PatientTransferUpdateRequestDTO patientTransferDto) {
        PatientTransfer patientTransfer = patientTransferRepository.findByIdAndIsDeleted(patientTransferDto.getId(), false);
        if (Objects.isNull(patientTransfer)) {
            throw new DataNotFoundException(16009);
        }
        if (patientTransfer.getTransferSite().isDeleted()) {
            throw new DataNotFoundException(16010);
        }

        PatientTransferStatus transferStatus = patientTransfer.getTransferStatus();
        if ((transferStatus == PatientTransferStatus.ACCEPTED || transferStatus == PatientTransferStatus.REJECTED
                || transferStatus == PatientTransferStatus.CANCELED) || !patientTransfer.isShow()) {
            throw new DataNotAcceptableException(16011);
        }
        this.updatePatientTransferStatus(patientTransferDto, patientTransfer);
    }

    /**
     * This method is used to update patient transfer status
     *
     * @param patientTransferDto patientTransferDto
     * @param patientTransfer    patientTransfer
     */
    private void updatePatientTransferStatus(PatientTransferUpdateRequestDTO patientTransferDto,
                                             PatientTransfer patientTransfer) {
        PatientTransferStatus transferStatus = patientTransferDto.getTransferStatus();
        switch (transferStatus) {
            case ACCEPTED -> {
                this.userValidateInStatusUpdate(patientTransfer.getTransferTo().getId());
                patientTransfer.setTransferStatus(transferStatus);
                patientTransfer.setOldProgramId(patientTransfer.getPatientTracker().getProgramId());
                patientTransfer.setTenantId(UserSelectedTenantContextHolder.get());
                this.updatePatientRecords(patientTransfer);
            }
            case REJECTED -> {
                String rejectReason = patientTransferDto.getRejectReason();
                if (Objects.isNull(rejectReason)) {
                    throw new DataNotAcceptableException(16013);
                }
                this.userValidateInStatusUpdate(patientTransfer.getTransferTo().getId());
                patientTransfer.setRejectReason(rejectReason);
                patientTransfer.setTransferStatus(transferStatus);
            }
            case REMOVED -> {
                this.userValidateInStatusUpdate(patientTransfer.getTransferBy().getId());
                patientTransfer.setShow(false);
            }
            case CANCELED -> {
                this.userValidateInStatusUpdate(patientTransfer.getTransferBy().getId());
                patientTransfer.setTransferStatus(transferStatus);
                patientTransfer.setShow(false);
            }
            default -> throw new DataNotAcceptableException(16012);
        }

        patientTransferRepository.save(patientTransfer);
    }

    /**
     * This method is used to validate user in status update
     *
     * @param userId userId
     */
    private void userValidateInStatusUpdate(long userId) {
        if (userId != UserContextHolder.getUserDto().getId()) {
            throw new BadCredentialsException(ErrorConstants.ERROR_USER_DOES_NOT_ROLE);
        }
    }

    /**
     * This method is used to update patient records
     *
     * @param patientTransfer patientTransfer
     */
    private void updatePatientRecords(PatientTransfer patientTransfer) {
        Long virtualId = this.incrementVirtualId(patientTransfer);
        Long tenantId = UserSelectedTenantContextHolder.get();
        patientTrackerRepository.findById(patientTransfer.getPatientTracker().getId()).ifPresent(patientTracker -> {
            patientTracker.setOperatingUnitId(patientTransfer.getTransferSite().getOperatingUnit().getId());
            patientTracker.setAccountId(patientTransfer.getTransferSite().getAccountId());
            patientTracker.setSiteId(patientTransfer.getTransferSite().getId());
            patientTracker.setTenantId(tenantId);
            patientTracker.setProgramId(virtualId);
            patientTrackerRepository.save(patientTracker);
        });
        patientRepository.findById(patientTransfer.getPatientTracker().getPatientId()).ifPresent(patient -> {
            patient.setTenantId(tenantId);
            patient.setSiteId(patientTransfer.getTransferSite().getId());
            patientRepository.save(patient);
        });
        this.updatePatientRelatedRecords(patientTransfer);
    }

    /**
     * This method is used to increment virtual id
     *
     * @param patientTransfer patientTransfer
     */
    private Long incrementVirtualId(PatientTransfer patientTransfer) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withFunctionName(Constants.UPDATE_VIRTUAL_ID);
        Organization organization = userApiInterface.getOrganizationByFormDataIdAndName(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), patientTransfer.getPatientTracker().getCountryId(), Constants.COUNTRY).getBody();
        if (Objects.isNull(organization)) {
            throw new DataNotFoundException();
        }
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue(Constants.INPUT_ID, patientTransfer.getPatientTracker().getPatientId())
                .addValue(Constants.INPUT_TENANT_ID, organization.getId());
        return jdbcCall.executeFunction(Long.class, in);
    }

    /**
     * This method is used to update patient related records
     *
     * @param patientTransfer patientTransfer
     */
    protected void updatePatientRelatedRecords(PatientTransfer patientTransfer) {
        Long patientTrackId = patientTransfer.getPatientTracker().getId();
        Long tenantId = UserSelectedTenantContextHolder.get();
        this.updatePatientAssessmentRecords(patientTrackId, tenantId);
        this.updatePatientPrescriptionRecords(patientTrackId, tenantId);
        this.updatePatientLabTestRecords(patientTrackId, tenantId);
        this.updatePatientLifestyleRecords(patientTrackId, tenantId);
        this.updatePatientMedicalReviewRecords(patientTrackId, tenantId);
        this.updatePatientOtherMedicalRecords(patientTrackId, tenantId);
    }

    /**
     * This method is used to update patient assessment records
     */
    private void updatePatientAssessmentRecords(Long patientTrackId, Long tenantId) {
        List<GlucoseLog> glucoseLogs = glucoseLogRepository.findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(glucoseLogs)) {
            glucoseLogs.forEach(glucoseLog -> glucoseLog.setTenantId(tenantId));
            glucoseLogRepository.saveAll(glucoseLogs);
        }
        List<BpLog> bpLogs = bpLogRepository.findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(bpLogs)) {
            bpLogs.forEach(bpLog -> bpLog.setTenantId(tenantId));
            bpLogRepository.saveAll(bpLogs);
        }
        List<RedRiskNotification> redRiskNotifications = redRiskNotificationRepository
                .findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(redRiskNotifications)) {
            redRiskNotifications.forEach(redRiskNotification -> redRiskNotification.setTenantId(tenantId));
            redRiskNotificationRepository.saveAll(redRiskNotifications);
        }
        List<CustomizedModule> customizedModules = customizedModuleRepository.findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(customizedModules)) {
            customizedModules.forEach(customizedModule -> customizedModule.setTenantId(tenantId));
            customizedModuleRepository.saveAll(customizedModules);
        }
        List<PatientAssessment> patientAssessments = patientAssessmentRepository.findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(patientAssessments)) {
            patientAssessments.forEach(patientAssessment -> patientAssessment.setTenantId(tenantId));
            patientAssessmentRepository.saveAll(patientAssessments);
        }
    }

    /**
     * This method is used to update patient prescription records
     */
    private void updatePatientPrescriptionRecords(Long patientTrackId, Long tenantId) {
        List<Prescription> prescriptions = prescriptionRepository.findBypatientTrackId(patientTrackId);
        if (!Objects.isNull(prescriptions)) {
            prescriptions.forEach(prescription -> prescription.setTenantId(tenantId));
            prescriptionRepository.saveAll(prescriptions);
        }
        List<PrescriptionHistory> prescriptionHistories = prescriptionHistoryRepository
                .findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(prescriptionHistories)) {
            prescriptionHistories.forEach(prescriptionHistory -> prescriptionHistory.setTenantId(tenantId));
            prescriptionHistoryRepository.saveAll(prescriptionHistories);
        }
    }

    /**
     * This method is used to update patient labTest records
     */
    private void updatePatientLabTestRecords(Long patientTrackId, Long tenantId) {
        List<PatientLabTest> patientLabTests = patientLabTestRepository.findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(patientLabTests)) {
            patientLabTests.forEach(patientLabTest -> patientLabTest.setTenantId(tenantId));
            patientLabTestRepository.saveAll(patientLabTests);
        }
        List<PatientLabTestResult> patientLabTestResults = patientLabTestResultRepository
                .findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(patientLabTestResults)) {
            patientLabTestResults.forEach(patientLabTestResult -> patientLabTestResult.setTenantId(tenantId));
            patientLabTestResultRepository.saveAll(patientLabTestResults);
        }
    }

    /**
     * This method is used to update patient lifestyle records.
     */
    private void updatePatientLifestyleRecords(Long patientTrackId, Long tenantId) {
        List<PatientLifestyle> patientLifestyles = patientLifestyleRepository.findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(patientLifestyles)) {
            patientLifestyles.forEach(patientLifestyle -> patientLifestyle.setTenantId(tenantId));
            patientLifestyleRepository.saveAll(patientLifestyles);
        }
        List<PatientNutritionLifestyle> patientNutritionLifestyles = patientNutritionLifestyleRepository
                .findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(patientNutritionLifestyles)) {
            patientNutritionLifestyles.forEach(patientNutritionLifestyle -> patientNutritionLifestyle.setTenantId(tenantId));
            patientNutritionLifestyleRepository.saveAll(patientNutritionLifestyles);
        }
    }

    /**
     * This method is used to update patient medical review records
     */
    private void updatePatientMedicalReviewRecords(Long patientTrackId, Long tenantId) {
        List<MentalHealth> mentalHealths = mentalHealthRepository.findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(mentalHealths)) {
            mentalHealths.forEach(mentalHealth -> mentalHealth.setTenantId(tenantId));
            mentalHealthRepository.saveAll(mentalHealths);
        }
        List<PatientVisit> patientVisits = patientVisitRepository.findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(patientVisits)) {
            patientVisits.forEach(patientVisit -> patientVisit.setTenantId(tenantId));
            patientVisitRepository.saveAll(patientVisits);
        }
        List<PatientMedicalReview> medicalReviews = medicalReviewRepository.findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(medicalReviews)) {
            medicalReviews.forEach(medicalReview -> medicalReview.setTenantId(tenantId));
            medicalReviewRepository.saveAll(medicalReviews);
        }
        List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository
                .findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(patientComorbidityList)) {
            patientComorbidityList.forEach(patientComorbidity -> patientComorbidity.setTenantId(tenantId));
            patientComorbidityRepository.saveAll(patientComorbidityList);
        }
        List<PatientComplication> patientComplications = patientComplicationRepository
                .findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(patientComplications)) {
            patientComplications.forEach(patientComplication -> patientComplication.setTenantId(tenantId));
            patientComplicationRepository.saveAll(patientComplications);
        }
        List<PatientCurrentMedication> patientCurrentMedications = patientCurrentMedicationRepository
                .findBypatientTrackId(patientTrackId);
        if (!Objects.isNull(patientCurrentMedications)) {
            patientCurrentMedications.forEach(patientCurrentMedication -> patientCurrentMedication.setTenantId(tenantId));
            patientCurrentMedicationRepository.saveAll(patientCurrentMedications);
        }
    }

    /**
     * This method is used to update patient other medical records
     */
    private void updatePatientOtherMedicalRecords(Long patientTrackId, Long tenantId) {
        List<PatientDiagnosis> patientDiagnoses = patientDiagnosisRepository.findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(patientDiagnoses)) {
            patientDiagnoses.forEach(patientDiagnosis -> patientDiagnosis.setTenantId(tenantId));
            patientDiagnosisRepository.saveAll(patientDiagnoses);
        }
        List<PatientMedicalCompliance> patientMedicalComplianceList = patientMedicalComplianceRepository
                .findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(patientMedicalComplianceList)) {
            patientMedicalComplianceList.forEach(patientMedicalCompliance -> patientMedicalCompliance.setTenantId(tenantId));
            patientMedicalComplianceRepository.saveAll(patientMedicalComplianceList);
        }
        List<PatientPregnancyDetails> patientPregnancyDetails = patientPregnancyDetailsRepository
                .findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(patientPregnancyDetails)) {
            patientPregnancyDetails.forEach(patientPregnancyDetail -> patientPregnancyDetail.setTenantId(tenantId));
            patientPregnancyDetailsRepository.saveAll(patientPregnancyDetails);
        }
        List<PatientSymptom> patientSymptoms = patientSymptomRepository.findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(patientSymptoms)) {
            patientSymptoms.forEach(patientSymptom -> patientSymptom.setTenantId(tenantId));
            patientSymptomRepository.saveAll(patientSymptoms);
        }
        List<PatientTreatmentPlan> patientTreatmentPlans = patientTreatmentPlanRepository
                .findByPatientTrackId(patientTrackId);
        if (!Objects.isNull(patientTreatmentPlans)) {
            patientTreatmentPlans.forEach(patientTreatmentPlan -> patientTreatmentPlan.setTenantId(tenantId));
            patientTreatmentPlanRepository.saveAll(patientTreatmentPlans);
        }
    }

    public void removePatientTransfer(long trackerId) {
        List<PatientTransfer> patientTransfers = patientTransferRepository.findByPatientTrackId(trackerId);
        if (!Objects.isNull(patientTransfers)) {
            patientTransfers.forEach(patientTransfer -> {
                patientTransfer.setActive(false);
                patientTransfer.setDeleted(true);
            });
            patientTransferRepository.saveAll(patientTransfers);
        }
    }
}