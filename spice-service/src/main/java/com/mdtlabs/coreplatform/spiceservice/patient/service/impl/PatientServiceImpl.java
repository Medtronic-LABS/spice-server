package com.mdtlabs.coreplatform.spiceservice.patient.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.UnitConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.exception.Validation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.SmsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.BpLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.DiagnosisDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.EnrollmentRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.EnrollmentResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LifestyleDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MentalHealthDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientDetailDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientGetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTrackerDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PregnancyDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RedRiskDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.SMSTemplate;
import com.mdtlabs.coreplatform.common.model.entity.SMSTemplateValues;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.Lifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import com.mdtlabs.coreplatform.common.model.entity.spice.Patient;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientAssessment;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientDiagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientPregnancyDetails;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.RedRiskNotification;
import com.mdtlabs.coreplatform.common.model.entity.spice.ScreeningLog;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.ConversionUtil;
import com.mdtlabs.coreplatform.common.util.StringUtil;
import com.mdtlabs.coreplatform.spiceservice.AdminApiInterface;
import com.mdtlabs.coreplatform.spiceservice.NotificationApiInterface;
import com.mdtlabs.coreplatform.spiceservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceservice.assessment.repository.PatientAssessmentRepository;
import com.mdtlabs.coreplatform.spiceservice.bplog.service.BpLogService;
import com.mdtlabs.coreplatform.spiceservice.common.RedRiskService;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.MentalHealthMapper;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.PatientMapper;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientDiagnosisRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientLifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.RedRiskNotificationRepository;
import com.mdtlabs.coreplatform.spiceservice.customizedmodules.service.CustomizedModulesService;
import com.mdtlabs.coreplatform.spiceservice.glucoselog.service.GlucoseLogService;
import com.mdtlabs.coreplatform.spiceservice.medicalreview.service.MedicalReviewService;
import com.mdtlabs.coreplatform.spiceservice.mentalhealth.service.MentalHealthService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.LifestyleService;
import com.mdtlabs.coreplatform.spiceservice.patient.repository.PatientPregnancyDetailsRepository;
import com.mdtlabs.coreplatform.spiceservice.patient.repository.PatientRepository;
import com.mdtlabs.coreplatform.spiceservice.patient.service.PatientService;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.service.PatientLabTestService;
import com.mdtlabs.coreplatform.spiceservice.patientmedicalcompliance.service.PatientMedicalComplianceService;
import com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.service.PatientNutritionLifestyleService;
import com.mdtlabs.coreplatform.spiceservice.patientsymptom.service.PatientSymptomService;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttransfer.service.PatientTransferService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.service.PatientVisitService;
import com.mdtlabs.coreplatform.spiceservice.prescription.service.PrescriptionService;
import com.mdtlabs.coreplatform.spiceservice.screeninglog.service.ScreeningLogService;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This is the class that implements PatientService class and contains the
 * actual business logic for Patient Entity.
 * </p>
 *
 * @author Niraimathi S created on Feb 7, 2023
 */
@Service
public class PatientServiceImpl implements PatientService {

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private AdminApiInterface adminApiInterface;

    @Autowired
    private BpLogService bpLogService;

    @Autowired
    private CustomizedModulesService customizedModulesService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private GlucoseLogService glucoseLogService;

    @Autowired
    private LifestyleService lifeStyleService;

    @Autowired
    private MentalHealthService mentalHealthService;

    @Autowired
    private MentalHealthMapper mentalHealthMapper;

    @Autowired
    @Lazy
    private MedicalReviewService medicalReviewService;

    @Autowired
    private PatientTransferService patientTransferService;

    @Autowired
    private NotificationApiInterface notificationApiInterface;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private PatientPregnancyDetailsRepository pregnancyDetailsRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientTrackerService patientTrackerService;

    @Autowired
    private PatientTreatmentPlanService patientTreatmentPlanService;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PatientLabTestService patientLabTestService;

    @Autowired
    private PatientNutritionLifestyleService patientNutritionLifestyleService;

    @Autowired
    private PatientVisitService patientVisitService;

    @Autowired
    private PatientAssessmentRepository patientAssessmentRepository;

    @Autowired
    private PatientDiagnosisRepository patientDiagnosisRepository;

    @Autowired
    private PatientLifestyleRepository patientLifestyleRepository;

    @Autowired
    private PatientMedicalComplianceService patientMedicalComplianceService;

    @Autowired
    private PatientSymptomService patientSymptomService;

    @Autowired
    private RedRiskService redRiskService;

    @Autowired
    private RedRiskNotificationRepository redRiskNotificationRepository;

    @Autowired
    private ScreeningLogService screeningLogService;

    @Autowired
    private UserApiInterface userApiInterface;

    @Value("${app.send-enrollment-notification}")
    private boolean sendEnrollmentNotification;

    /**
     * {@inheritDoc}
     */
    private static void setSMSData(Patient enrolledPatient, Site site, List<SMSTemplateValues> smsTemplateValues, Map<String, String> data) {
        if (smsTemplateValues != null) {
            for (SMSTemplateValues smsTemplateValue : smsTemplateValues) {
                if (FieldConstants.NAME.equals(smsTemplateValue.getKey())) {
                    data.put(FieldConstants.NAME,
                            enrolledPatient.getFirstName().toUpperCase().concat(Constants.SPACE).concat(
                                    enrolledPatient.getLastName().toUpperCase()));
                }
                if (Constants.SITE.equals(smsTemplateValue.getKey())) {
                    data.put(Constants.SITE, site.getName());
                }
                if (FieldConstants.PATIENT_ID.equals(smsTemplateValue.getKey())) {
                    data.put(FieldConstants.PATIENT_ID, enrolledPatient.getId().toString());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public EnrollmentResponseDTO createPatient(EnrollmentRequestDTO enrollmentRequest) {
        Logger.logDebug(("Creating patient details. National Id: ").concat(enrollmentRequest.getBioData().getNationalId()));
        PatientTracker existingPatientTracker = validateEnrollmentRequest(enrollmentRequest);
        EnrollmentResponseDTO response = new EnrollmentResponseDTO();
        Patient patient = patientMapper.setPatient(enrollmentRequest);
        updateVirtualId(patient);
        Patient enrolledPatient = patientRepository.save(patient);
        Site site = adminApiInterface.getSiteById(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), enrolledPatient.getSiteId()).getBody();
        PatientTracker patientTracker = constructPatientTracker(enrollmentRequest, existingPatientTracker, enrolledPatient,
                site);
        patientTracker = patientTrackerService.addOrUpdatePatientTracker(patientTracker);
        List<Map<String, String>> treatmentPlanDurations = patientTreatmentPlanService
                .createProvisionalTreatmentPlan(patientTracker, enrollmentRequest.getCvdRiskLevel(), enrolledPatient.getTenantId());
        response.setTreatmentPlan(treatmentPlanDurations);
        createMentalHealth(enrollmentRequest, patientTracker, response);
        PatientDiagnosis patientDiagnosis = createPatientDiagnosis(enrollmentRequest, patientTracker);
        setRiskLevelForEnrollment(enrollmentRequest, patientTracker, patientDiagnosis);
        createAssessment(enrollmentRequest, response, patientTracker);
        customizedModulesService.createCustomizedModules(enrollmentRequest.getCustomizedWorkflows(),
                Constants.WORKFLOW_ENROLLMENT, patientTracker.getId());
        if (site != null) {
            constructEnrollmentResponse(response, enrolledPatient, site, patientTracker);
        }
        sendEnrollmentNotification(enrolledPatient, site);
        return response;
    }

    /**
     * {@inheritDoc}
     */
    private PatientTracker validateEnrollmentRequest(EnrollmentRequestDTO enrollmentRequest) {
        if (UnitConstants.IMPERIAL.equals(enrollmentRequest.getUnitMeasurement())) {
            ConversionUtil.convertBpLogUnits(enrollmentRequest.getBpLog(), UnitConstants.IMPERIAL);
        }
        PatientTracker existingPatientTracker = (!Objects.isNull(enrollmentRequest.getPatientTrackId()))
                ? patientTrackerService.getPatientTrackerById(enrollmentRequest.getPatientTrackId())
                : patientTrackerService.findByNationalIdIgnoreCaseAndCountryIdAndIsDeleted(
                enrollmentRequest.getBioData().getNationalId().toUpperCase(), enrollmentRequest.getBioData().getCountry(), Constants.BOOLEAN_FALSE);
        if (!Objects.isNull(existingPatientTracker)
                && existingPatientTracker.getPatientStatus().equals(Constants.ENROLLED)) {
            Logger.logError("Patient already Enrolled.");
            throw new DataConflictException(1202, existingPatientTracker.getProgramId().toString()); // patient Already Enrolled
        }
        return existingPatientTracker;
    }

    /**
     * Updates patient virtual Id.
     *
     * @param enrolledPatient enrolled patient data.
     */
    private void updateVirtualId(Patient enrolledPatient) {
        Logger.logInfo("Updating virtual id for the patient id - " + enrolledPatient.getId());
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withFunctionName(Constants.UPDATE_VIRTUAL_ID);
        Organization organization = userApiInterface.getOrganizationByFormDataIdAndName(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), enrolledPatient.getCountryId(), Constants.COUNTRY).getBody();
        if (Objects.isNull(organization)) {
            throw new DataNotFoundException();
        }
        SqlParameterSource in = new MapSqlParameterSource().addValue(Constants.INPUT_TENANT_ID, organization.getId());
        Long virtualId = jdbcCall.executeFunction(Long.class, in);
        Logger.logInfo("Virtual Id generated for the patient id " + enrolledPatient.getId() + " is " + virtualId);
        if (Objects.isNull(virtualId) ||Constants.MINUS_ONE == virtualId) {
            throw new Validation(1205);
        }
        enrolledPatient.setVirtualId(virtualId);
    }

    /**
     * <p>
     * To set risk level for patient tracker update during enrollment.
     * </p>
     *
     * @param data             - enrollment request data
     * @param patientTracker   - patient Tracker object
     * @param patientDiagnosis - Patient Diagnosis details
     */
    private void setRiskLevelForEnrollment(EnrollmentRequestDTO data, PatientTracker patientTracker,
                                           PatientDiagnosis patientDiagnosis) {
        String riskLevel = null;
        if (patientTracker.isInitialReview()) {
            if (Constants.MG_DL.equals(data.getGlucoseLog().getGlucoseUnit())) {
                patientTracker
                        .setGlucoseValue(ConversionUtil.convertMgDlToMmol(data.getGlucoseLog().getGlucoseValue()));
            }
            riskLevel = redRiskService.getPatientRiskLevel(patientTracker, patientDiagnosis, new RedRiskDTO());
        }
        if (!Objects.isNull(riskLevel)) {
            patientTracker.setRiskLevel(riskLevel);
        }
    }

    /**
     * <p>
     * Sets values for the patient tracker update during enrollment.
     * </p>
     *
     * @param data                   - enrollment request data
     * @param existingPatientTracker - existing patient tracker details if existed.
     * @param enrolledPatient        - enrolled patient details
     * @return PatientTracker details
     */
    private PatientTracker constructPatientTracker(EnrollmentRequestDTO data, PatientTracker existingPatientTracker,
                                                   Patient enrolledPatient, Site site) {
        if (Objects.isNull(existingPatientTracker)) {
            existingPatientTracker = new PatientTracker();
        }
        patientMapper.setPatientTracker(data, existingPatientTracker, site);
        existingPatientTracker.setIsScreening(!Objects.isNull(existingPatientTracker.getScreeningLogId()));
        existingPatientTracker.setProgramId(enrolledPatient.getVirtualId());
        existingPatientTracker.setPatientId(enrolledPatient.getId());

        return existingPatientTracker;
    }

    /**
     * <p>
     * Constructs enrollment response data.
     * </p>
     *
     * @param response        - enrollment response data
     * @param enrolledPatient - enrolled patient details
     * @param patientTracker  - patientTracker details
     */
    private void constructEnrollmentResponse(EnrollmentResponseDTO response,
                                             Patient enrolledPatient, Site site, PatientTracker patientTracker) {
        response.setConfirmDiagnosis(patientTracker.getConfirmDiagnosis());
        response.setIsConfirmDiagnosis(patientTracker.getIsConfirmDiagnosis());
        response.setProvisionalDiagnosis(patientTracker.getProvisionalDiagnosis());
        PatientDetailDTO enrollment = patientMapper.setPatientDetailDto(enrolledPatient);
        enrollment.setSiteName(site.getName());
        response.setEnrollment(enrollment);
    }

    /**
     * {@inheritDoc}
     */
    private void createAssessment(EnrollmentRequestDTO enrollmentRequest,
                                  EnrollmentResponseDTO enrollmentResponse, PatientTracker patientTracker) {
        BpLog bpLog = createBpLog(enrollmentRequest, enrollmentResponse, patientTracker);
        GlucoseLog glucoseLog = createGlucoseLog(enrollmentRequest, enrollmentResponse, patientTracker);
        Long glucoseId = Objects.isNull(glucoseLog) ? null : glucoseLog.getId();
        patientAssessmentRepository.save(new PatientAssessment(bpLog.getId(),
                glucoseId, Constants.ENROLLMENT, enrollmentRequest.getTenantId(), patientTracker.getId()));
    }

    /**
     * <p>
     * This method adds the glucoseLog value for a patient.
     * </p>
     *
     * @param patientTracker - patient tracker details.
     */
    private GlucoseLog createGlucoseLog(EnrollmentRequestDTO enrollmentRequest, EnrollmentResponseDTO enrollmentResponse,
                                        PatientTracker patientTracker) {
        GlucoseLog glucoseLog = enrollmentRequest.getGlucoseLog();
        GlucoseLog existingGlucoseLog = null;
        if (CommonUtil.isGlucoseLogGiven(glucoseLog)) {
            if (!Objects.isNull(glucoseLog.getGlucoseLogId())) {
                existingGlucoseLog = glucoseLogService.getGlucoseLogById(glucoseLog.getGlucoseLogId());
                mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                mapper.map(glucoseLog, existingGlucoseLog);
            }
            glucoseLog = patientMapper.setGlucoseLog(enrollmentRequest, null != existingGlucoseLog ? existingGlucoseLog : glucoseLog);
            if (!Objects.isNull(glucoseLog)) {
                glucoseLog.setPatientTrackId(patientTracker.getId());
                glucoseLog = glucoseLogService.addGlucoseLog(glucoseLog, Constants.BOOLEAN_FALSE);
            }
            if (!Objects.isNull(glucoseLog) && !Objects.isNull(glucoseLog.getId())) {
                enrollmentResponse.setGlucoseLog(new GlucoseLogDTO(glucoseLog.getId(), glucoseLog.getHba1c(),
                        glucoseLog.getHba1cUnit(), glucoseLog.getGlucoseType(), glucoseLog.getGlucoseValue(),
                        glucoseLog.getGlucoseDateTime(), glucoseLog.getGlucoseUnit(), glucoseLog.getCreatedAt()));
            }
        }
        return glucoseLog;
    }

    /**
     * <p>
     * Adds BPLog data for a patient.
     * </p>
     *
     * @param data           - enrollment request data
     * @param patientTracker - Patient tracker details
     * @return BpLog - BpLog entity
     */
    private BpLog createBpLog(EnrollmentRequestDTO data, EnrollmentResponseDTO response, PatientTracker patientTracker) {
        BpLog bpLog = data.getBpLog();
        BpLog existingBpLog = null;
        if (!Objects.isNull(bpLog.getBpLogId())) {
            existingBpLog = bpLogService.getBpLogById(bpLog.getBpLogId());
            mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            mapper.map(bpLog, existingBpLog);
        }
        bpLog = patientMapper.setBpLog(data, null != existingBpLog ? existingBpLog : bpLog);
        bpLog.setPatientTrackId(patientTracker.getId());
        bpLog = bpLogService.addBpLog(bpLog, Constants.BOOLEAN_FALSE);
        response.setBpLog(new BpLogDTO(bpLog.getAvgSystolic(), bpLog.getAvgDiastolic(), bpLog.getBmi(),
                bpLog.getCvdRiskLevel(), bpLog.getCvdRiskScore()));
        return bpLog;
    }

    /**
     * <p>
     * Sets HTN and diabetes diagnosis values for the patientTracker and patientDiagnosis.
     * </p>
     *
     * @param data           - enrollment request data
     * @param patientTracker - patientTracker details
     * @return PatientDiagnosis - patientDiagnosis details
     */
    private PatientDiagnosis createPatientDiagnosis(EnrollmentRequestDTO data, PatientTracker patientTracker) {
        PatientDiagnosis patientDiagnosis = new PatientDiagnosis();
        if (!Objects.isNull(data.getPatientStatus())) {
            List<String> diagnosisList = getConfirmDiagnosisValues(patientTracker, data.getPatientStatus());
            patientDiagnosis = patientMapper.setPatientDiagnosis(data.getPatientStatus());
            patientDiagnosis.setPatientTrackId(patientTracker.getId());
            patientDiagnosis.setTenantId(UserSelectedTenantContextHolder.get());
            PatientDiagnosis existingPatientDiagnosis = patientDiagnosisRepository.findByPatientTrackIdAndIsActiveAndIsDeleted(patientTracker.getId(), Constants.BOOLEAN_TRUE, Constants.BOOLEAN_FALSE);
            if (Objects.isNull(existingPatientDiagnosis)) {
                patientDiagnosis = patientDiagnosisRepository.save(patientDiagnosis);
            } else {
                mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                mapper.map(patientDiagnosis, existingPatientDiagnosis);
                patientDiagnosis = patientDiagnosisRepository.save(existingPatientDiagnosis);
            }

            patientTracker.setIsConfirmDiagnosis(Constants.BOOLEAN_FALSE);
            if (!diagnosisList.isEmpty()) {
                patientTracker.setIsConfirmDiagnosis(Constants.BOOLEAN_TRUE);
                patientTracker.setConfirmDiagnosis(diagnosisList);
            }
            patientTracker.setIsHtnDiagnosis(data.getPatientStatus().getIsHtnDiagnosis());
            patientTracker.setIsDiabetesDiagnosis(data.getPatientStatus().getIsDiabetesDiagnosis());
        }
        return patientDiagnosis;
    }

    /**
     * <p>
     * To send notification to the enrolled patient.
     * </p>
     *
     * @param enrolledPatient - entity
     */
    private void sendEnrollmentNotification(Patient enrolledPatient, Site site) {
        if (sendEnrollmentNotification) {
            String token = CommonUtil.getAuthToken();
            SMSTemplate smsTemplate = notificationApiInterface
                    .getSmsTemplateValues(token, UserSelectedTenantContextHolder.get(), Constants.TEMPLATE_TYPE_ENROLL_PATIENT)
                    .getBody();
            String body = null;
            if (smsTemplate != null) {
                body = smsTemplate.getBody();
            }
            List<SMSTemplateValues> smsTemplateValues = null;
            if (smsTemplate != null) {
                smsTemplateValues = smsTemplate.getSmsTemplateValues();
            }
            Map<String, String> data = new HashMap<>();
            setSMSData(enrolledPatient, site, smsTemplateValues, data);
            body = StringUtil.parseEmailTemplate(body, data);
            SmsDTO smsDto = new SmsDTO(body, (UserContextHolder.getUserDto().getCountry().getCountryCode() + enrolledPatient.getPhoneNumber()),
                    enrolledPatient.getTenantId(), enrolledPatient.getId());
            notificationApiInterface.saveOutBoundSms(token, UserSelectedTenantContextHolder.get(), List.of(smsDto));
        }
    }

    /**
     * <p>
     * Sets the mental health values in patient tracker and enrollment response.
     * </p>
     *
     * @param data           - Enrollment request data
     * @param patientTracker - patient tracker details
     * @param response       - enrollment response
     */
    private void createMentalHealth(EnrollmentRequestDTO data, PatientTracker patientTracker,
                                    EnrollmentResponseDTO response) {
        if (!Objects.isNull(data.getPhq4())) {
            MentalHealth mentalHealth = data.getPhq4();
            mentalHealth = mentalHealthService.createMentalHealth(mentalHealth, patientTracker, Constants.BOOLEAN_TRUE);
            mentalHealthMapper.setPatientTracker(patientTracker, mentalHealth);
            response.setPhq4(new MentalHealthDTO(mentalHealth.getPhq4RiskLevel(), mentalHealth.getPhq4Score()));
        }
    }

    /**
     * {@inheritDoc}
     */
    public PatientTrackerDTO getPatientDetails(PatientGetRequestDTO requestData) {
        Logger.logDebug(("Get patient tracker details by id - ").concat(requestData.getId().toString()));
        PatientTracker patientTracker = patientTrackerService.getPatientTrackerById(requestData.getId());
        PatientTrackerDTO patientTrackerDto = mapper.map(patientTracker, PatientTrackerDTO.class);
        if (UnitConstants.IMPERIAL.equals(UserContextHolder.getUserDto().getCountry().getUnitMeasurement())) {
            Logger.logInfo("Convert the value to Imperial");
            patientTrackerDto.setHeight(ConversionUtil.convertHeight(patientTrackerDto.getHeight(), UnitConstants.METRIC));
            patientTrackerDto.setWeight(ConversionUtil.convertWeight(patientTrackerDto.getWeight(), UnitConstants.METRIC));
        }
        addMentalHealthDetails(patientTrackerDto);
        if (requestData.isAssessmentRequired()) {
            addAssessmentDetails(requestData, patientTracker, patientTrackerDto);
        }
        if (requestData.isPrescriberRequired()) {
            patientTrackerDto.setPrescriberDetails(
                    prescriptionService.getPatientPrescribedDetails(patientTracker.getId(), patientTracker.getTenantId()));
        }
        if (requestData.isLifeStyleRequired() && patientTracker.isInitialReview()) {
            patientTrackerDto
                    .setPatientLifestyles(getPatientLifeStyleDetails(new CommonRequestDTO(patientTracker.getId())));
        }
        Date date = !Objects.isNull(patientTrackerDto.getEnrollmentAt()) ? patientTrackerDto.getEnrollmentAt()
                : patientTracker.getCreatedAt();
        patientTrackerDto.setAge(ConversionUtil.calculatePatientAge(patientTrackerDto.getAge(), date));
        return patientTrackerDto;
    }

    /**
     * <p>
     * Adds assessment details to the patient details.
     * </p>
     *
     * @param requestData       - Request data
     * @param patientTracker    - patient Tracker data
     * @param patientTrackerDto - response Object containing patient details.
     */
    private void addAssessmentDetails(PatientGetRequestDTO requestData, PatientTracker patientTracker,
                                      PatientTrackerDTO patientTrackerDto) {
        BpLog bpLog = bpLogService.getBpLogByPatientTrackIdAndIsLatest(requestData.getId(), Constants.BOOLEAN_TRUE);
        if (!Objects.isNull(bpLog)) {
            patientTrackerDto.setBpLogDetails(bpLog.getBpLogDetails());
        }
        GlucoseLog glucoseLog = glucoseLogService.getGlucoseLogByPatientTrackIdAndIsLatest(requestData.getId(),
                Constants.BOOLEAN_TRUE);
        if (!Objects.isNull(glucoseLog)) {
            patientTrackerDto
                    .setGlucoseLogDetails(new GlucoseLogDTO(glucoseLog.getGlucoseType(), glucoseLog.getGlucoseValue(),
                            glucoseLog.getLastMealTime(), glucoseLog.getGlucoseDateTime(), glucoseLog.getGlucoseUnit()));
        }
        if (!Objects.isNull(patientTracker.getScreeningLogId())) {
            ScreeningLog screeningLog = screeningLogService.getByIdAndIsLatest(patientTracker.getScreeningLogId());
            if (!Objects.isNull(screeningLog)) {
                patientTrackerDto.setPhoneNumberCategory(screeningLog.getPhoneNumberCategory());
            }
        }
    }

    /**
     * <p>
     * Add mental health details to the patient details.
     * </p>
     *
     * @param patientTrackerDto - patientTracker details
     */
    private void addMentalHealthDetails(PatientTrackerDTO patientTrackerDto) {
        patientTrackerDto.setIsPhq9(Constants.BOOLEAN_FALSE);
        patientTrackerDto.setIsGad7(Constants.BOOLEAN_FALSE);

        if (!Objects.isNull(patientTrackerDto.getPhq4FirstScore())
                && Constants.TWO <= patientTrackerDto.getPhq4FirstScore()) {
            patientTrackerDto.setIsGad7(Constants.BOOLEAN_TRUE);
        }
        if (!Objects.isNull(patientTrackerDto.getPhq4SecondScore())
                && Constants.TWO <= patientTrackerDto.getPhq4SecondScore()) {
            patientTrackerDto.setIsPhq9(Constants.BOOLEAN_TRUE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public PregnancyDetailsDTO createPregnancyDetails(PregnancyDetailsDTO requestData) {
        isPatientTrackIdExist(requestData.getPatientTrackId());
        Logger.logDebug(("To create pregnancy details for patient tracker id - ")
                .concat(requestData.getPatientTrackId().toString()));
        PatientTracker patientTracker = patientTrackerService.getPatientTrackerById(requestData.getPatientTrackId());
        PatientPregnancyDetails pregnancyDetails = mapper.map(requestData, PatientPregnancyDetails.class);
        pregnancyDetails.setPatientTrackId(patientTracker.getId());
        pregnancyDetails = pregnancyDetailsRepository.save(pregnancyDetails);
        Double temperature = requestData.getTemperature();
        //unit conversion
        if (requestData.getUnitMeasurement().equals(UnitConstants.IMPERIAL)) {
            Logger.logInfo("Convert the values to Imperial");
            requestData
                    .setTemperature(ConversionUtil.convertTemperature(requestData.getTemperature(), UnitConstants.METRIC));
        }
        pregnancyDetails.setTemperature(temperature);
        updatePatientTrackForPregnancyDetails(patientTracker, requestData);
        mapper.map(pregnancyDetails, requestData);
        return requestData;
    }

    /**
     * {@inheritDoc}
     */
    public PregnancyDetailsDTO getPregnancyDetails(GetRequestDTO requestData) {
        isPatientTrackIdExist(requestData.getPatientTrackId());
        Logger.logDebug(
                ("To get pregnancy details for patient tracker id - ").concat(requestData.getPatientTrackId().toString()));
        PatientPregnancyDetails pregnancyDetails = Objects.isNull(requestData.getPatientPregnancyId())
                ? pregnancyDetailsRepository.findByPatientTrackIdAndIsDeleted(requestData.getPatientTrackId(),
                Constants.BOOLEAN_FALSE)
                : pregnancyDetailsRepository.findByIdAndIsDeleted(requestData.getPatientPregnancyId(),
                Constants.BOOLEAN_FALSE);

        if (!Objects.isNull(pregnancyDetails)) {
            String organizationUnit = UserContextHolder.getUserDto().getCountry().getUnitMeasurement();
            if (UnitConstants.IMPERIAL.equals(organizationUnit)) {
                Logger.logInfo("Convert the values to Imperial");
                pregnancyDetails.setTemperature(
                        ConversionUtil.convertTemperature(pregnancyDetails.getTemperature(), UnitConstants.IMPERIAL));
            }
        }
        return !Objects.isNull(pregnancyDetails) ? mapper.map(pregnancyDetails, PregnancyDetailsDTO.class) : null;
    }

    /**
     * <p>
     * Checks if PatientTrack id exists and throws exception if not.
     * </p>
     *
     * @param patientTrackId PatientTrackId
     */
    private void isPatientTrackIdExist(Long patientTrackId) {
        if (Objects.isNull(patientTrackId)) {
            Logger.logError(ErrorConstants.PATIENT_TRACK_ID_NOT_NULL);
            throw new DataNotAcceptableException(1256);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public PatientPregnancyDetails updatePregnancyDetails(PregnancyDetailsDTO requestData) {
        isPatientTrackIdExist(requestData.getPatientTrackId());
        Logger.logDebug(("To update pregnancy details for patient tracker id - ")
                .concat(requestData.getPatientTrackId().toString()));
        if (requestData.getUnitMeasurement().equals(UnitConstants.IMPERIAL)) {
            Logger.logInfo("Convert the values to Metric");
            requestData
                    .setTemperature(ConversionUtil.convertTemperature(requestData.getTemperature(), UnitConstants.METRIC));
        }
        PatientPregnancyDetails existingPregnantDetails = pregnancyDetailsRepository
                .findByIdAndIsDeleted(requestData.getPatientPregnancyId(), Constants.BOOLEAN_FALSE);
        if (Objects.isNull(existingPregnantDetails)) {
            Logger.logError("Pregnancy details not found for this patient.");
            throw new DataNotFoundException(1203);
        }
        PatientPregnancyDetails pregnancyDetails;
        pregnancyDetails = mapper.map(requestData, PatientPregnancyDetails.class);
        pregnancyDetails.setId(requestData.getPatientPregnancyId());
        if (!Objects.isNull(pregnancyDetails.getDiagnosis())) {
            boolean isDiagnosis = pregnancyDetails.getDiagnosis().stream()
                    .anyMatch(data -> data.equalsIgnoreCase(Constants.NONE));
            if (isDiagnosis) {
                pregnancyDetails.setIsOnTreatment(existingPregnantDetails.getIsOnTreatment());
                pregnancyDetails.setDiagnosisTime(existingPregnantDetails.getDiagnosisTime());
            }
        }
        pregnancyDetails = pregnancyDetailsRepository.save(pregnancyDetails);
        PatientTracker patientTracker = patientTrackerService.getPatientTrackerById(requestData.getPatientTrackId());
        updatePatientTrackForPregnancyDetails(patientTracker, requestData);
        pregnancyDetails.setTemperature(requestData.getTemperature());
        return pregnancyDetails;
    }


    /**
     * <p>
     * Updates the pregnancy details in patientTracker for pregnant patients.
     * </p>
     *
     * @param patientTracker patientTracker object to update.
     * @param requestData    Request data with pregnancy details
     */
    private void updatePatientTrackForPregnancyDetails(PatientTracker patientTracker, PregnancyDetailsDTO requestData) {
        patientTracker.setIsPregnant(Constants.BOOLEAN_TRUE);
        patientTracker.setEstimatedDeliveryDate(requestData.getEstimatedDeliveryDate());
        patientTracker.setLastMenstrualPeriodDate(requestData.getLastMenstrualPeriodDate());
        patientTrackerService.addOrUpdatePatientTracker(patientTracker);
    }

    /**
     * {@inheritDoc}
     */
    public List<String> getConfirmDiagnosisValues(PatientTracker patientTracker, DiagnosisDTO diagnosisDto) {
        List<String> diagnosisList = new ArrayList<>();
        List<String> overallDiagnosis = new ArrayList<>();
        overallDiagnosis.addAll(Constants.DIABETES_CONFIRM_DIAGNOSIS);
        overallDiagnosis.addAll(Constants.HTM_CONFIRM_DIAGNOSIS);
        List<String> previousOtherDiagnosis = new ArrayList<>();
        if (!Objects.isNull(patientTracker.getConfirmDiagnosis())) {
            previousOtherDiagnosis = patientTracker.getConfirmDiagnosis().stream()
                    .filter(diagnosis -> !overallDiagnosis.contains(diagnosis)).toList();
        }
        if (!Objects.isNull(diagnosisDto.getDiabetesDiagnosis())) {
            diagnosisList.add(diagnosisDto.getDiabetesDiagnosis());
        } else if (!Objects.isNull(patientTracker.getConfirmDiagnosis())) {
            String previousDiabetesDiagnosis = patientTracker.getConfirmDiagnosis().stream()
                    .filter(Constants.DIABETES_CONFIRM_DIAGNOSIS::contains).findFirst().orElse(Constants.EMPTY);
            if (StringUtils.isNotEmpty(previousDiabetesDiagnosis)) {
                diagnosisList.add(previousDiabetesDiagnosis);
            }
        }
        if (!Objects.isNull(diagnosisDto.getHtnDiagnosis())) {
            diagnosisList.add(diagnosisDto.getHtnDiagnosis());
        } else if (!Objects.isNull(patientTracker.getConfirmDiagnosis())) {
            String previousHtnDiagnosis = patientTracker.getConfirmDiagnosis().stream()
                    .filter(Constants.HTM_CONFIRM_DIAGNOSIS::contains).findFirst().orElse(Constants.EMPTY);
            if (StringUtils.isNotEmpty(previousHtnDiagnosis)) {
                diagnosisList.add(previousHtnDiagnosis);
            }
        }
        diagnosisList.addAll(previousOtherDiagnosis);
        return diagnosisList;
    }

    /**
     * {@inheritDoc}
     */
    public PatientDetailDTO getPatientBasicDetails(CommonRequestDTO request) {
        isPatientTrackIdExist(request.getId());
        return mapper.map(patientRepository.findByIdAndIsDeleted(request.getId(), Constants.BOOLEAN_FALSE),
                PatientDetailDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    public PatientDetailDTO updatePatientDetails(PatientDetailDTO patient) {
        if (Objects.isNull(patient.getId()) || Objects.isNull(patient.getPatientTrackId())) {
            Logger.logError("Patient Tracker Id Should not be Empty.");
            throw new DataNotAcceptableException(1256);
        }
        Logger.logDebug(("To update patient details for patient tracker id - ").concat(patient.getId().toString()));
        Patient existingPatient = getPatientByIdAndTenantId(patient.getId());
        updatePatientTracker(patient);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(patient, existingPatient);
        existingPatient.setMiddleName(patient.getMiddleName());
        existingPatient = patientRepository.save(existingPatient);
        return mapper.map(existingPatient, PatientDetailDTO.class);
    }

    /**
     * <p>
     * This method is used to update PatientTracker details of a patient.
     * </p>
     *
     * @param patient - details to update
     */
    private void updatePatientTracker(PatientDetailDTO patient) {
        PatientTracker patientTracker = patientTrackerService.getPatientTrackerByIdAndStatus(
                patient.getPatientTrackId(), UserSelectedTenantContextHolder.get(), Constants.ENROLLED);
        if (Objects.isNull(patientTracker)) {
            Logger.logError("Patient not found for this Id.");
            throw new DataNotFoundException(1201);
        }
        patientTracker.setFirstName(
                !Objects.isNull(patient.getFirstName()) ? patient.getFirstName() :
                        patientTracker.getFirstName());
        patientTracker
                .setLastName(!Objects.isNull(patient.getLastName()) ? patient.getLastName() :
                        patientTracker.getLastName());
        patientTracker
                .setGender(!Objects.isNull(patient.getGender()) ? patient.getGender() : patientTracker.getGender());
        patientTracker.setPhoneNumber(
                !Objects.isNull(patient.getPhoneNumber()) ? patient.getPhoneNumber() : patientTracker.getPhoneNumber());
        patientTracker
                .setHeight(!Objects.isNull(patient.getHeight()) ? Double.parseDouble(patient.getHeight().toString()) : patientTracker.getHeight());
        patientTracker
                .setWeight(!Objects.isNull(patient.getWeight()) ? Double.parseDouble(patient.getWeight().toString()) : patientTracker.getWeight());
        patientTracker.setBmi(!Objects.isNull(patient.getBmi()) ? Double.parseDouble(patient.getBmi().toString()) : patientTracker.getBmi());
        patientTrackerService.addOrUpdatePatientTracker(patientTracker);
    }

    /**
     * {@inheritDoc}
     */
    public List<LifestyleDTO> getPatientLifeStyleDetails(CommonRequestDTO request) {
        isPatientTrackIdExist(request.getPatientTrackId());
        Logger.logDebug(
                ("Get patient lifestyle details for patient Track Id - ").concat(request.getPatientTrackId().toString()));
        List<PatientLifestyle> patientLifestyles = patientLifestyleRepository
                .findByPatientTrackId(request.getPatientTrackId());
        List<LifestyleDTO> lifestyleResponse = new ArrayList<>();
        if (!Objects.isNull(patientLifestyles)) {
            List<Long> lifestyleIds = patientLifestyles.stream().map(PatientLifestyle::getLifestyleId).toList();
            List<Lifestyle> lifestyles = lifeStyleService.getLifestylesByIds(lifestyleIds);
            Map<Long, Lifestyle> lifestylesMap = new HashMap<>();
            for (Lifestyle lifestyle : lifestyles) {
                lifestylesMap.put(lifestyle.getId(), lifestyle);
            }
            for (PatientLifestyle patientLifestyle : patientLifestyles) {
                lifestyleResponse.add(new LifestyleDTO(patientLifestyle.getComments(),
                        lifestylesMap.get(patientLifestyle.getLifestyleId()).getName(),
                        lifestylesMap.get(patientLifestyle.getLifestyleId()).getType(), patientLifestyle.getAnswer()));
            }
        }
        return lifestyleResponse;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO listMyPatients(PatientRequestDTO patientRequestDto) {
        return patientTrackerService.listMyPatients(patientRequestDto);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO searchPatients(PatientRequestDTO patientRequestDto) {
        return patientTrackerService.searchPatients(patientRequestDto);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO patientAdvanceSearch(PatientRequestDTO patientRequestDto) {
        return patientTrackerService.patientAdvanceSearch(patientRequestDto);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public Patient removePatientDetails(PatientRequestDTO patient) {
        if (Objects.isNull(patient.getId()) || Objects.isNull(patient.getPatientTrackId())) {
            throw new DataNotAcceptableException(1204);
        }
        Patient existingPatient = getPatientByIdAndTenantId(patient.getId());
        existingPatient.setDeleted(true);
        existingPatient.setActive(false);
        removePatientTracker(patient);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(patient, existingPatient);
        return patientRepository.save(existingPatient);
    }

    /**
     * <p>
     * This method is used to remove PatientTracker details of a patient.
     * </p>
     *
     * @param patient - details to remove
     */
    private void removePatientTracker(PatientRequestDTO patient) {
        PatientTracker patientTracker = patientTrackerService.getPatientTrackerByIdAndStatus(
                patient.getPatientTrackId(), UserSelectedTenantContextHolder.get(), Constants.ENROLLED);
        if (Objects.isNull(patientTracker)) {
            throw new DataNotFoundException(1201);
        }
        patientTracker.setActive(false);
        patientTracker.setDeleted(true);
        patientTracker.setDeleteReason(patient.getDeleteReason());
        patientTracker.setDeleteOtherReason(patient.getDeleteOtherReason());
        patientTrackerService.addOrUpdatePatientTracker(patientTracker);
        removePatientDependentTables(patientTracker.getId());
    }

    /**
     * <p>
     * This method is used to remove Patient related details of a patient.
     * </p>
     *
     * @param trackerId - tracker id of a patient
     */
    private void removePatientDependentTables(Long trackerId) {
        bpLogService.removeBpLog(trackerId);
        glucoseLogService.removeGlucoseLog(trackerId);
        removeRedRiskNotification(trackerId);
        customizedModulesService.removeCustomizedModule(trackerId);
        prescriptionService.removePrescription(trackerId);
        patientLabTestService.removeLabTest(trackerId);
        patientNutritionLifestyleService.removeNutritionLifestyleByTrackerId(trackerId);
        mentalHealthService.removeMentalHealth(trackerId);
        medicalReviewService.removeMedicalReview(trackerId);
        patientTransferService.removePatientTransfer(trackerId);
        removePatientAssessment(trackerId);
        patientVisitService.removePatientVisit(trackerId);
        patientMedicalComplianceService.removeMedicalCompliance(trackerId);
        removePatientPregnancyDetails(trackerId);
        patientSymptomService.removePatientSymptom(trackerId);
        patientTreatmentPlanService.removePatientTreatmentPlan(trackerId);
    }

    /**
     * <p>
     * To remove red risk notification based on tracker id.
     * </p>
     *
     * @param trackerId trackerId
     */
    private void removeRedRiskNotification(long trackerId) {
        List<RedRiskNotification> redRiskNotifications = redRiskNotificationRepository.findByPatientTrackId(trackerId);
        if (!Objects.isNull(redRiskNotifications)) {
            redRiskNotifications.forEach(redRiskNotification -> {
                redRiskNotification.setActive(false);
                redRiskNotification.setDeleted(true);
            });
            redRiskNotificationRepository.saveAll(redRiskNotifications);
        }
    }

    /**
     * <p>
     * To remove patient transfer details based on tracker id.
     * </p>
     *
     * @param trackerId trackerId
     */
    private void removePatientAssessment(long trackerId) {
        List<PatientAssessment> patientAssessments = patientAssessmentRepository.findByPatientTrackId(trackerId);
        if (!Objects.isNull(patientAssessments)) {
            patientAssessments.forEach(patientAssessment -> {
                patientAssessment.setActive(false);
                patientAssessment.setDeleted(true);
            });
            patientAssessmentRepository.saveAll(patientAssessments);
        }
    }

    /**
     * <p>
     * To remove pregnancy details based on tracker id.
     * </p>
     *
     * @param trackerId trackerId
     */
    private void removePatientPregnancyDetails(long trackerId) {
        List<PatientPregnancyDetails> existingPregnancyDetails = pregnancyDetailsRepository
                .findByPatientTrackId(trackerId);
        if (!Objects.isNull(existingPregnancyDetails)) {
            existingPregnancyDetails.forEach(existingPregnancyDetail -> {
                existingPregnancyDetail.setActive(false);
                existingPregnancyDetail.setDeleted(true);
            });
            pregnancyDetailsRepository.saveAll(existingPregnancyDetails);
        }
    }

    /**
     * {@inheritDoc}
     */
    private Patient getPatientByIdAndTenantId(Long id) {
        Patient patient = patientRepository.findByIdAndIsDeletedAndTenantId(id, false,
                UserSelectedTenantContextHolder.get());
        if (Objects.isNull(patient)) {
            Logger.logError(("Patient not found for this Id - ").concat(id.toString()));
            throw new DataNotFoundException(1201);
        }
        return patient;
    }
}