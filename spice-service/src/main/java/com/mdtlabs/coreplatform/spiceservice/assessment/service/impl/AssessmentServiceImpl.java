package com.mdtlabs.coreplatform.spiceservice.assessment.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdtlabs.coreplatform.AuthenticationFilter;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.UnitConstants;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.SmsDTO;
import com.mdtlabs.coreplatform.common.model.dto.fhir.FhirAssessmentRequestDto;
import com.mdtlabs.coreplatform.common.model.dto.spice.AssessmentDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AssessmentResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.BpLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ComplianceDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MentalHealthDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientDetailDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RiskAlgorithmDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SymptomDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserResponseDTO;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientAssessment;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalCompliance;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientSymptom;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.common.model.entity.spice.RedRiskNotification;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.ConversionUtil;
import com.mdtlabs.coreplatform.common.util.UniqueCodeGenerator;
import com.mdtlabs.coreplatform.spiceservice.AdminApiInterface;
import com.mdtlabs.coreplatform.spiceservice.NotificationApiInterface;
import com.mdtlabs.coreplatform.spiceservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceservice.assessment.repository.PatientAssessmentRepository;
import com.mdtlabs.coreplatform.spiceservice.assessment.service.AssessmentService;
import com.mdtlabs.coreplatform.spiceservice.bplog.service.BpLogService;
import com.mdtlabs.coreplatform.spiceservice.common.RiskAlgorithm;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.MentalHealthMapper;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.PatientTrackerMapper;
import com.mdtlabs.coreplatform.spiceservice.common.repository.RedRiskNotificationRepository;
import com.mdtlabs.coreplatform.spiceservice.customizedmodules.service.CustomizedModulesService;
import com.mdtlabs.coreplatform.spiceservice.glucoselog.service.GlucoseLogService;
import com.mdtlabs.coreplatform.spiceservice.mentalhealth.service.MentalHealthService;
import com.mdtlabs.coreplatform.spiceservice.patientmedicalcompliance.service.PatientMedicalComplianceService;
import com.mdtlabs.coreplatform.spiceservice.patientsymptom.service.PatientSymptomService;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * This class implements the AssessmentService interface and contains actual
 * business logic to perform operations on assessment entity.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
@Service
public class AssessmentServiceImpl implements AssessmentService {

    private static final List<String> RED_RISK_LEVEL_LIST = Arrays.asList(Constants.HIGHER_MODERATE,
            Constants.BOTH_MODERATE, Constants.GLUCOSE_MODERATE, Constants.BOTH_HIGHER_MODERATE);

    @Autowired
    private AdminApiInterface adminApiInterface;

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Autowired
    private PatientAssessmentRepository patientAssessmentRepository;

    @Autowired
    private BpLogService bpLogService;

    @Autowired
    private CustomizedModulesService customizedModulesService;

    @Autowired
    private GlucoseLogService glucoseLogService;

    @Autowired
    private MentalHealthMapper mentalHealthMapper;

    @Autowired
    private MentalHealthService mentalHealthService;

    @Autowired
    private NotificationApiInterface notificationApiInterface;

    @Autowired
    private PatientTrackerService patientTrackerService;

    @Autowired
    private PatientTrackerMapper patientTrackerMapper;

    @Autowired
    private PatientSymptomService patientSymptomService;

    @Autowired
    private PatientMedicalComplianceService patientMedicalComplianceService;

    @Autowired
    private PatientTreatmentPlanService patientTreatmentPlanService;

    @Autowired
    private RedRiskNotificationRepository redRiskNotificationRepository;

    @Autowired
    private RiskAlgorithm riskAlgorithm;

    @Autowired
    private UserApiInterface userApiInterface;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key.name}")
    private String routingKey;

    @Value("${app.enableFhir}")
    private boolean enableFhir;
    /**
     * {@inheritDoc}
     */
    @Transactional
    public AssessmentResponseDTO createAssessment(AssessmentDTO assessmentRequest) {
        Logger.logInfo("In AssessmentServiceImpl, creating assessment information");
        if (Objects.isNull(assessmentRequest)) {
            throw new BadRequestException(1000);
        }
        if (UnitConstants.IMPERIAL.equals(assessmentRequest.getUnitMeasurement())) {
            ConversionUtil.convertBpLogUnits(assessmentRequest.getBpLog(), UnitConstants.IMPERIAL);
        }
        AssessmentResponseDTO assessmentResponse = new AssessmentResponseDTO();
        PatientTracker patientTracker = patientTrackerService.getPatientTrackerById(assessmentRequest.getPatientTrackId());
        customizedModulesService.createCustomizedModules(assessmentRequest.getCustomizedWorkflows(),
                Constants.WORKFLOW_ASSESSMENT, patientTracker.getId());
        createMentalHealth(assessmentRequest, patientTracker, assessmentResponse);
        setPatientTracker(assessmentRequest, patientTracker);

        String riskLevel = calculateRiskLevel(assessmentRequest.getBpLog(), assessmentRequest.getGlucoseLog(),
                assessmentRequest.getSymptoms(), patientTracker);
        if (StringUtils.isBlank(riskLevel)) {
            riskLevel = RED_RISK_LEVEL_LIST.contains(riskLevel) ? Constants.MODERATE : riskLevel;
            patientTracker.setRiskLevel(riskLevel);
        }
        BpLog bpLog = createBpLog(assessmentRequest, assessmentResponse, riskLevel);
        GlucoseLog glucoseLog = createGlucoseLog(assessmentRequest, assessmentResponse);
        Long glucoseId = glucoseLog.getId();
        PatientAssessment assessmentLog = patientAssessmentRepository.save(new PatientAssessment(bpLog.getId(), glucoseId,
                Constants.ASSESSMENT, assessmentRequest.getTenantId(), assessmentRequest.getPatientTrackId()));
        Long assessmentLogId = assessmentLog.getId();
        if(enableFhir) {
            try {
                setAndSendFhirAssessmentRequest(glucoseLog, bpLog, assessmentLog, patientTracker);
            } catch (JsonProcessingException e) {
                Logger.logError("ERROR Converting from Object to String");
            } catch (AmqpException amqpException) {
                Logger.logError("ERROR Connecting to RabbitMQ Queue");
                throw new AmqpException(amqpException);
            }
        }
        if (Constants.HIGH.equals(riskLevel)) {
            addRedRiskNotification(patientTracker, assessmentRequest.getBpLog().getId(), glucoseId, assessmentLogId);
        }
        createSymptomsAndCompliance(assessmentRequest, bpLog.getId(), glucoseId, assessmentLogId, assessmentResponse);
        patientTrackerService.addOrUpdatePatientTracker(patientTracker);
        constructPatientResponse(assessmentRequest, assessmentResponse, patientTracker);
        return assessmentResponse;
    }

    private void setAndSendFhirAssessmentRequest(GlucoseLog glucoseLog, BpLog bpLog,
                                                 PatientAssessment assessmentLog, PatientTracker patientTracker) throws JsonProcessingException {
        FhirAssessmentRequestDto fhirAssessmentRequestDto = new FhirAssessmentRequestDto();
        fhirAssessmentRequestDto.setGlucoseLog(glucoseLog);
        fhirAssessmentRequestDto.setBpLog(bpLog);
        fhirAssessmentRequestDto.setType(Constants.ASSESSMENT_DATA);
        fhirAssessmentRequestDto.setPatientAssessment(assessmentLog);
        fhirAssessmentRequestDto.setCreatedBy(assessmentLog.getCreatedBy());
        fhirAssessmentRequestDto.setUpdatedBy(assessmentLog.getUpdatedBy());
        fhirAssessmentRequestDto.setPatientTrackId(assessmentLog.getPatientTrackId());
        fhirAssessmentRequestDto.setPatientTracker(patientTrackerService.addOrUpdatePatientTracker(patientTracker));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(fhirAssessmentRequestDto);
        String deduplicationId = UniqueCodeGenerator.generateUniqueCode(jsonString);

        Map<String, Object> message = new HashMap<>();
        message.put(Constants.DEDUPLICATION_ID, deduplicationId);
        message.put(Constants.BODY,jsonString);
        String jsonMessage = objectMapper.writeValueAsString(message);
        Logger.logDebug("In AssessmentServiceImpl, setAndSendFhirAssessmentRequest :: "+ jsonMessage);
        rabbitTemplate.convertAndSend(exchange,routingKey,jsonMessage);
    }

    /**
     * {@inheritDoc}
     */
    private void createMentalHealth(AssessmentDTO assessmentRequest, PatientTracker patientTracker, AssessmentResponseDTO response) {
        if (!Objects.isNull(assessmentRequest.getPhq4())) {
            MentalHealth mentalHealth = assessmentRequest.getPhq4();
            mentalHealth = mentalHealthService.createMentalHealth(mentalHealth, patientTracker, Constants.BOOLEAN_TRUE);
            mentalHealthMapper.setPatientTracker(patientTracker, mentalHealth);
            response.setPhq4(new MentalHealthDTO(mentalHealth.getPhq4RiskLevel(), mentalHealth.getPhq4Score()));
        }
    }

    /**
     * {@inheritDoc}
     */
    private void createSymptomsAndCompliance(AssessmentDTO assessmentRequest, Long bpLogId, Long glucoseId, Long assessmentLogId,
                                             AssessmentResponseDTO response) {
        if (!Objects.isNull(assessmentRequest.getSymptoms()) && !assessmentRequest.getSymptoms().isEmpty()) {
            List<PatientSymptom> patientSymptoms = createPatientSymptoms(assessmentRequest.getSymptoms(), bpLogId,
                    glucoseId, assessmentRequest.getPatientTrackId(), assessmentLogId);
            response.setSymptoms(patientSymptoms);
        }
        if (!Objects.isNull(assessmentRequest.getCompliances()) && !assessmentRequest.getCompliances().isEmpty()) {
            List<PatientMedicalCompliance> patientMedicalComplianceList = createPatientComplianceList(
                    assessmentRequest.getCompliances(), bpLogId, assessmentRequest.getPatientTrackId(), assessmentLogId);
            response.setMedicalCompliance(patientMedicalComplianceList);
        }
    }

    /**
     * <p>
     * Sets risk message bases on the risk level.
     * </p>
     *
     * @param riskLevel {@link String} risk level is given
     * @return String {@link String} risk message is returned
     */
    private String setRiskMessage(String riskLevel) {
        Logger.logInfo("In AssessmentServiceImpl, set risk message");
        Map<String, String> riskMessageMap = new HashMap<>();
        riskMessageMap.put(Constants.GLUCOSE_MODERATE, Constants.DBM_GLUCOSE_MODERATE);
        riskMessageMap.put(Constants.BOTH_MODERATE, Constants.DBM_BOTH_MODERATE);
        riskMessageMap.put(Constants.BOTH_HIGHER_MODERATE, Constants.DBM_BOTH_MODERATE);
        riskMessageMap.put(Constants.HIGHER_MODERATE, Constants.DBM_HIGHER_MODERATE);
        riskMessageMap.put(Constants.HIGH, Constants.DBM_HIGH);
        riskMessageMap.put(Constants.LOW, Constants.DBM_LOW);
        return StringUtils.isEmpty(riskMessageMap.get(riskLevel)) ? Constants.DBM_MODERATE
                : riskMessageMap.get(riskLevel);
    }


    /**
     * <p>
     * This function sets patient tracker information based on an assessment request and updates the
     * treatment plan accordingly.
     * </p>
     *
     * @param assessmentRequest {@link AssessmentDTO} An object of type AssessmentDTO which contains information related to a
     *                          patient's assessment is given
     * @param patientTracker    {@link PatientTracker} An object of type PatientTracker that contains information about the
     *                          patient's status and treatment plan is given
     */
    private void setPatientTracker(AssessmentDTO assessmentRequest, PatientTracker patientTracker) {
        Logger.logInfo("In AssessmentServiceImpl, set patient tracker information");
        constructPatientTracker(patientTracker, assessmentRequest);
        if (Constants.SCREENED.equals(patientTracker.getPatientStatus())) {
            patientTracker.setPatientStatus(Constants.NONE);
        }
        patientTracker.setLastAssessmentDate(new Date());
        if (!Objects.isNull(assessmentRequest.getProvisionalDiagnosis())
                && !assessmentRequest.getProvisionalDiagnosis().isEmpty()) {
            patientTracker.setProvisionalDiagnosis(assessmentRequest.getProvisionalDiagnosis());
        }
        updateTreatmentPlan(patientTracker, CommonUtil.isGlucoseLogGiven(assessmentRequest.getGlucoseLog()));
    }

    /**
     * <p>
     * Constructs patient details information for assessment.
     * </p>
     *
     * @param response       {@link AssessmentResponseDTO} AssessmentResponseDto is given
     * @param patientTracker {@link PatientTracker} entity is given
     * @param riskLevel      {@link String} risk level is given
     */
    private void constructPatientDetails(AssessmentResponseDTO response, PatientTracker patientTracker, String siteName,
                                         String riskLevel) {
        Logger.logInfo("In AssessmentServiceImpl, construct patient details");
        PatientDetailDTO patientDetails = new PatientDetailDTO();
        patientTrackerMapper.setPatientDetails(patientTracker, patientDetails);
        patientDetails.setSiteName(siteName);
        response.setRiskLevel(riskLevel);
        response.setRiskMessage(setRiskMessage(riskLevel));
        response.setPatientDetails(patientDetails);
    }

    /**
     * <p>
     * This function constructs a patient response by retrieving site information and patient details
     * from a patient tracker object.
     * </p>
     *
     * @param assessmentRequest {@link AssessmentDTO} An object of type AssessmentDTO that contains information about the
     *                          assessment request is given
     * @param response          {@link AssessmentResponseDTO} AssessmentResponseDTO object that will be constructed with patient details and
     *                          diagnosis confirmation is given
     * @param patientTracker    {@link PatientTracker} An object of type PatientTracker that contains information about a
     *                          patient's assessment and tracking details is given
     */

    private void constructPatientResponse(AssessmentDTO assessmentRequest, AssessmentResponseDTO response,
                                          PatientTracker patientTracker) {
        Logger.logInfo("In AssessmentServiceImpl, constructing patient response");
        Site site = adminApiInterface
                .getSiteById(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(), assessmentRequest.getSiteId())
                .getBody();
        if (null != site) {
            constructPatientDetails(response, patientTracker, site.getName(), patientTracker.getRiskLevel());
        }
        response.setConfirmDiagnosis(patientTracker.getConfirmDiagnosis());
    }

    /**
     * <p>
     * This function creates a glucose log and adds it to the assessment response if it is given in the
     * assessment request.
     * </p>
     *
     * @param assessmentRequest {@link AssessmentDTO} An object of type AssessmentDTO which contains information about an
     *                          assessment request is given
     * @param response          {@link AssessmentResponseDTO} The response parameter is an object of type AssessmentResponseDTO, which is used
     *                          to store the response data for an assessment is given
     * @return {@link Long} The method is returning a Long value, which is the ID of the glucose log created. If no
     * glucose log was created, the method returns null is returned
     */
    private GlucoseLog createGlucoseLog(AssessmentDTO assessmentRequest, AssessmentResponseDTO response) {
        Logger.logInfo("In AssessmentServiceImpl, constructing glucose log information");
        GlucoseLog glucoseLog =new GlucoseLog();
        if (CommonUtil.isGlucoseLogGiven(assessmentRequest.getGlucoseLog())) {
             glucoseLog = constructGlucoseLog(assessmentRequest);
            glucoseLog = glucoseLogService.addGlucoseLog(glucoseLog, Constants.BOOLEAN_FALSE);
            response.setGlucoseLog(new GlucoseLogDTO(glucoseLog.getGlucoseType(), glucoseLog.getGlucoseValue(),
                    glucoseLog.getGlucoseUnit()));
        }
        return glucoseLog;
    }

    /**
     * <p>
     * Constructs bp log information for the patient.
     * </p>
     *
     * @param response  {@link AssessmentDTO} assessment response dto is given
     * @param riskLevel {@link AssessmentResponseDTO} risk level is given
     * @return BpLog  {@link String} entity is returned
     */
    private BpLog createBpLog(AssessmentDTO assessmentRequest, AssessmentResponseDTO response, String riskLevel) {
        Logger.logInfo("In AssessmentServiceImpl, constructing bp log information");
        BpLog bpLog = constructBpLog(assessmentRequest, riskLevel);
        bpLog = bpLogService.addBpLog(bpLog, Constants.BOOLEAN_FALSE);
        response.setBpLog(new BpLogDTO(bpLog.getAvgSystolic(), bpLog.getAvgDiastolic(), bpLog.getBmi(),
                bpLog.getCvdRiskLevel(), bpLog.getCvdRiskScore()));
        return bpLog;
    }

    /**
     * <p>
     * This function updates the treatment plan information for a patient in a patient tracker system,
     * including setting the next blood pressure and blood glucose assessment dates.
     * </p>
     *
     * @param patientTracker    {@link PatientTracker} An object of the class PatientTracker which contains information about a
     *                          patient's health status and treatment plan is given
     * @param isGlucoseLogGiven {@link boolean} A boolean value indicating whether a glucose log has been given or not is returned
     */
    private void updateTreatmentPlan(PatientTracker patientTracker, boolean isGlucoseLogGiven) {
        Logger.logInfo("In AssessmentServiceImpl, updating treatment plan information");
        PatientTreatmentPlan patientTreatmentPlan = patientTreatmentPlanService
                .getPatientTreatmentPlan(patientTracker.getId());
        if (!Objects.isNull(patientTreatmentPlan)) {
            Date nextBPAssessmentDate = patientTreatmentPlanService
                    .getTreatmentPlanFollowupDate(patientTreatmentPlan.getBpCheckFrequency(), Constants.DEFAULT);
            patientTracker.setNextBpAssessmentDate(nextBPAssessmentDate);
            if (isGlucoseLogGiven) {
                Date nextBGAssessmentDate = patientTreatmentPlanService
                        .getTreatmentPlanFollowupDate(patientTreatmentPlan.getBgCheckFrequency(), Constants.DEFAULT);
                if (!Objects.isNull(nextBGAssessmentDate)) {
                    patientTracker.setNextBgAssessmentDate(nextBGAssessmentDate);
                }
            }
        }
    }

    /**
     * This function constructs patient tracker information by setting values from blood pressure and
     * glucose logs.
     *
     * @param patientTracker {@link PatientTracker} It is an object of the class PatientTracker, which is used to track the
     *                       health information of a patient is given
     * @param assessmentDto  {@link AssessmentDTO} AssessmentDTO is an object that contains information related to a patient's
     *                       assessment, such as blood pressure log and glucose log is returned
     */
    private void constructPatientTracker(PatientTracker patientTracker, AssessmentDTO assessmentDto) {
        Logger.logInfo("In AssessmentServiceImpl, constructing patient tracker information");
        patientTrackerMapper.setPatientTrackerFromBpLog(patientTracker, assessmentDto.getBpLog());
        if (CommonUtil.isGlucoseLogGiven(assessmentDto.getGlucoseLog())) {
            patientTrackerMapper.setPatientTrackerFromGlucoseLog(patientTracker, assessmentDto.getGlucoseLog());
        }
    }

    /**
     * <p>
     * This function calculates the risk level of a patient based on their medical data and symptoms.
     * </p>
     *
     * @param bpLog          {@link BpLog} Blood pressure log of the patient is given
     * @param glucoseLog     {@link GlucoseLog} A log of glucose readings for the patient is given
     * @param symptoms       {@link List<SymptomDTO>} A list of SymptomDTO objects representing the symptoms reported by the patient is given
     * @param patientTracker {@link PatientTracker} an object that tracks the status and risk level of a patient is given
     * @return {@link String} The method returns a String value which represents the calculated risk level based on
     * the given parameters is returned
     */
    public String calculateRiskLevel(BpLog bpLog, GlucoseLog glucoseLog, List<SymptomDTO> symptoms,
                                     PatientTracker patientTracker) {
        Logger.logInfo("In AssessmentServiceImpl, calculate risk level");
        if (!Objects.isNull(patientTracker.getPatientStatus())
                && patientTracker.getPatientStatus().equals(Constants.ENROLLED)
                && patientTracker.isInitialReview()
                && !patientTracker.isRedRiskPatient()) {
            RiskAlgorithmDTO riskAlgorithmDto = new RiskAlgorithmDTO();
            riskAlgorithmDto.setPatientTrackId(patientTracker.getId());
            if (CommonUtil.isGlucoseLogGiven(glucoseLog)) {
                riskAlgorithmDto.setGlucoseType(glucoseLog.getGlucoseType());
                riskAlgorithmDto.setGlucoseValue(glucoseLog.getGlucoseValue());
            }
            if (!Objects.isNull(bpLog)) {
                riskAlgorithmDto.setAvgDiastolic(bpLog.getAvgDiastolic());
                riskAlgorithmDto.setAvgSystolic(bpLog.getAvgSystolic());
            }
            riskAlgorithmDto.setIsPregnant(patientTracker.getIsPregnant());
            riskAlgorithmDto.setRiskLevel(patientTracker.getRiskLevel());
            if (!Objects.isNull(symptoms)) {
                riskAlgorithmDto.setSymptoms(
                        symptoms.stream().map(SymptomDTO::getSymptomId).collect(Collectors.toSet()));
            }
            return riskAlgorithm.getRiskLevelInAssessmentDbm(riskAlgorithmDto);
        }
        return null;
    }

    /**
     * <p>
     * This function constructs a BpLog object using information from an AssessmentDTO object and a
     * risk level string.
     * </p>
     *
     * @param assessmentDTO {@link AssessmentDTO} An object of type AssessmentDTO which contains information related to a
     *                      patient's assessment is given
     * @param riskLevel     {@link String} The risk level calculated based on the assessment is given
     * @return {@link BpLog} The method is returning a BpLog object is given
     */
    public BpLog constructBpLog(AssessmentDTO assessmentDTO, String riskLevel) {
        Logger.logInfo("In AssessmentServiceImpl, constructing bp log information");
        BpLog bpLog = assessmentDTO.getBpLog();
        bpLog.setCvdRiskScore(assessmentDTO.getCvdRiskScore());
        bpLog.setCvdRiskLevel(assessmentDTO.getCvdRiskLevel());
        bpLog.setIsRegularSmoker(assessmentDTO.getIsRegularSmoker());
        bpLog.setType(Constants.ASSESSMENT);
        bpLog.setPatientTrackId(assessmentDTO.getPatientTrackId());
        bpLog.setRiskLevel(riskLevel);
        bpLog.setLatest(Constants.BOOLEAN_TRUE);
        bpLog.setBpTakenOn(Objects.isNull(bpLog.getBpTakenOn()) ? new Date() : bpLog.getBpTakenOn());
        bpLog.setTenantId(assessmentDTO.getTenantId());
        return bpLog;
    }

    /**
     * <p>
     * This function constructs a GlucoseLog object using information from an AssessmentDTO object.
     * </p>
     *
     * @param assessmentDto {@link AssessmentDTO} An object of type AssessmentDTO which contains information related to a
     *                      patient's assessment is given
     * @return {@link GlucoseLog} The method is returning a GlucoseLog object is returned
     */
    public GlucoseLog constructGlucoseLog(AssessmentDTO assessmentDto) {
        Logger.logInfo("In AssessmentServiceImpl, creating glucose log information");
        GlucoseLog glucoseLog = assessmentDto.getGlucoseLog();
        glucoseLog.setPatientTrackId(assessmentDto.getPatientTrackId());
        glucoseLog.setType(Constants.ASSESSMENT);
        glucoseLog.setLatest(Constants.BOOLEAN_TRUE);
        glucoseLog.setTenantId(assessmentDto.getTenantId());
        return glucoseLog;
    }

    /**
     * <p>
     * This function creates a new red risk notification object and saves it to the repository.
     * </p>
     *
     * @param patientTracker  {@link PatientTracker} An object of type PatientTracker that contains information about the
     *                        patient being assessed is given
     * @param bpLogId         {@link Long} The ID of the blood pressure log associated with the red risk notification being
     *                        created is given
     * @param glucoseLogId    {@link Long}  The ID of the glucose log associated with the red risk notification being
     *                        created is given
     * @param assessmentLogId {@link Long}  The ID of the assessment log associated with the red risk notification
     *                        being created is given
     * @return {@link RedRiskNotification} The method is returning a RedRiskNotification object that has been created and saved in
     * the redRiskNotificationRepository  is returned
     */
    public RedRiskNotification createRedRiskNotification(PatientTracker patientTracker, Long bpLogId, Long glucoseLogId,
                                                         Long assessmentLogId) {
        Logger.logInfo("In AssessmentServiceImpl, creating red risk notification information");
        RedRiskNotification redRiskNotification = new RedRiskNotification();
        redRiskNotification.setPatientTrackId(patientTracker.getId());
        redRiskNotification.setBpLogId(bpLogId);
        redRiskNotification.setGlucoseLogId(glucoseLogId);
        redRiskNotification.setTenantId(patientTracker.getTenantId());
        redRiskNotification.setAssessmentLogId(assessmentLogId);
        redRiskNotification.setStatus(Constants.NEW);
        return redRiskNotificationRepository.save(redRiskNotification);

    }

    /**
     * <p>
     * This function creates a list of patient symptoms based on symptom DTOs and logs them in the
     * patient symptom service.
     * </p>
     *
     * @param symptomDTOS      {@link List<SymptomDTO>} A list of SymptomDTO objects containing information about the symptoms
     *                         experienced by a patient  is given
     * @param bpLogId          {@link Long} The ID of the blood pressure log associated with the patient's symptom is given
     * @param glucoseLogId     {@link Long} The ID of the glucose log associated with the patient's symptom is given
     * @param patientTrackerId {@link Long} The ID of the patient tracker, which is used to track the patient's
     *                         health information over time is given
     * @param assessmentLogId  {@link Long} The ID of the assessment log for which the patient symptoms are being
     *                         created is given
     * @return {@link List<PatientSymptom>} The method is returning a list of PatientSymptom objects that have been created and
     * added to the patientSymptomList is returned
     */
    public List<PatientSymptom> createPatientSymptoms(List<SymptomDTO> symptomDTOS, Long bpLogId, Long glucoseLogId,
                                                      Long patientTrackerId, Long assessmentLogId) {
        Logger.logInfo("In AssessmentServiceImpl, creating patient symptoms information");
        List<PatientSymptom> patientSymptomList = new ArrayList<>();

        for (SymptomDTO symptom : symptomDTOS) {
            PatientSymptom patientSymptom = new PatientSymptom();
            if (Constants.HYPERTENSION.equals(symptom.getType())) {
                patientSymptom.setBpLogId(bpLogId);
            }
            if (Constants.DIABETES.equals(symptom.getType()) && !Objects.isNull(glucoseLogId)) {
                patientSymptom.setGlucoseLogId(glucoseLogId);
            }
            patientSymptom.setType(symptom.getType());
            patientSymptom.setName(symptom.getName());
            patientSymptom.setSymptomId(symptom.getSymptomId());
            patientSymptom.setOtherSymptom(symptom.getOtherSymptom());
            patientSymptom.setPatientTrackId(patientTrackerId);
            patientSymptom.setAssessmentLogId(assessmentLogId);
            patientSymptomList.add(patientSymptom);
        }
        return patientSymptomService.addPatientSymptoms(patientSymptomList);
    }

    /**
     * <p>
     * This function creates a list of patient medical compliance objects based on a list of compliance
     * DTOs and adds them to the database.
     * </p>
     *
     * @param complianceList  {@link List<ComplianceDTO>} A list of ComplianceDTO objects containing information about the patient's
     *                        medical compliance is given
     * @param bpLogId         {@link Long} The ID of the blood pressure log associated with the patient compliance
     *                        information being created is given
     * @param patientTrackId  {@link Long} The ID of the patient being tracked for medical compliance is given
     * @param assessmentLogId {@link Long} It is the ID of the assessment log for which the patient compliance
     *                        information is being created is given
     * @return {@link List<PatientMedicalCompliance>}  The method is returning a List of PatientMedicalCompliance objects is returned
     */
    public List<PatientMedicalCompliance> createPatientComplianceList(List<ComplianceDTO> complianceList, Long bpLogId,
                                                                      Long patientTrackId, Long assessmentLogId) {
        Logger.logInfo("In AssessmentServiceImpl, creating patient compliance information");
        List<PatientMedicalCompliance> patientMedicalComplianceList = new ArrayList<>();
        for (ComplianceDTO compliance : complianceList) {
            PatientMedicalCompliance patientMedicalCompliance = new PatientMedicalCompliance();
            patientMedicalCompliance.setBpLogId(bpLogId);
            patientMedicalCompliance.setComplianceId(compliance.getComplianceId());
            patientMedicalCompliance.setPatientTrackId(patientTrackId);
            patientMedicalCompliance.setName(compliance.getName());
            patientMedicalCompliance.setAssessmentLogId(assessmentLogId);
            patientMedicalCompliance.setOtherCompliance(compliance.getOtherCompliance());
            patientMedicalComplianceList.add(patientMedicalCompliance);
        }
        return patientMedicalComplianceService.addPatientMedicalCompliance(patientMedicalComplianceList);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public BpLog createAssessmentBpLog(BpLog bpLog) {
        Logger.logInfo("In AssessmentServiceImpl, creating assessment bp log information");
        validateBpLog(bpLog);
        BpLog existingBpLog = bpLogService.findFirstByPatientTrackIdAndIsDeletedOrderByBpTakenOnDesc(
                bpLog.getPatientTrackId(), Constants.BOOLEAN_FALSE);
        bpLog.setType(Constants.ASSESSMENT);
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
        bpLog.setOldRecord(
                !Objects.isNull(bpLog.getBpTakenOn()) && (Constants.ZERO > formatter.format(bpLog.getBpTakenOn()).compareTo(formatter.format(new Date()))));
        if (!Objects.isNull(existingBpLog) && !Objects.isNull(existingBpLog.getBpTakenOn())
                && !Objects.isNull(bpLog.getBpTakenOn())
                && (Constants.ZERO > bpLog.getBpTakenOn().compareTo(existingBpLog.getBpTakenOn()))) {
            bpLog.setLatest(Constants.BOOLEAN_FALSE);
            bpLogService.addBpLog(bpLog, Constants.BOOLEAN_FALSE);
        } else {
            PatientTracker patientTracker = patientTrackerService.getPatientTrackerById(bpLog.getPatientTrackId());
            String riskLevel;
            bpLog.setLatest(Constants.BOOLEAN_TRUE);
            AssessmentDTO assessmentDto = new AssessmentDTO();
            assessmentDto.setPatientTrackId(bpLog.getPatientTrackId());
            assessmentDto.setBpLog(bpLog);
            riskLevel = calculateRiskLevel(bpLog, null, null, patientTracker);
            if (StringUtils.isBlank(riskLevel)) {
                bpLog.setRiskLevel(RED_RISK_LEVEL_LIST.contains(riskLevel) ? Constants.MODERATE : riskLevel);
            }
            bpLog = bpLogService.addBpLog(bpLog, Constants.BOOLEAN_TRUE);
            if (Objects.equals(riskLevel, Constants.HIGH)) {
                addRedRiskNotification(patientTracker, Objects.nonNull(existingBpLog) ? existingBpLog.getId() : null,
                        null, null);
            }
        }
        patientAssessmentRepository.save(new PatientAssessment(bpLog.getId(), null, Constants.MEDICAL_REVIEW,
                bpLog.getTenantId(), bpLog.getPatientTrackId()));
        return bpLog;
    }

    /**
     * <p>
     * The function validates the information in a blood pressure log and throws exceptions if
     * necessary.
     * </p>
     *
     * @param bpLog {@link BpLog} an object of type BpLog, which contains information about a patient's blood
     *              pressure log, including average diastolic and systolic readings, whether the patient is a
     *              regular smoker, and a patient track ID is given
     */
    private void validateBpLog(BpLog bpLog) {
        Logger.logInfo("In AssessmentServiceImpl, validating bp log information");
        if (Objects.isNull(bpLog)) {
            throw new BadRequestException(1000);
        }
        if (Objects.isNull(bpLog.getAvgDiastolic()) || Objects.isNull(bpLog.getAvgSystolic())
                || Objects.isNull(bpLog.getIsRegularSmoker())) {
            throw new DataNotAcceptableException(8002);
        }

        if (Objects.isNull(bpLog.getPatientTrackId())) {
            throw new DataNotFoundException(1252);
        }
    }

    /**
     * <p>
     * This function adds red risk notification information to a patient tracker and sends SMS
     * notifications to users with the "ROLE_RED_RISK_USER" role.
     * </p>
     *
     * @param patientTracker  {@link PatientTracker} An object representing the patient being assessed and tracked is given
     * @param bpLogId         {@link Long} The ID of the blood pressure log associated with the red risk notification is given
     * @param glucoseId       {@link Long} The ID of the glucose log associated with the red risk notification being
     *                        added is given
     * @param assessmentLogId {@link Long} The ID of the assessment log for which the red risk notification is being
     *                        added is given
     */
    public void addRedRiskNotification(PatientTracker patientTracker, Long bpLogId, Long glucoseId,
                                       Long assessmentLogId) {
        Logger.logInfo("In AssessmentServiceImpl, adding red risk notification information");
        patientTracker.setRedRiskPatient(Constants.BOOLEAN_TRUE);
        RedRiskNotification notification = createRedRiskNotification(patientTracker, bpLogId, glucoseId,
                assessmentLogId);
        List<UserResponseDTO> users = userApiInterface.getUsersByRoleName(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), Constants.ROLE_RED_RISK_USER).getBody();
        List<SmsDTO> smsList = new ArrayList<>();
        if (Objects.nonNull(users) && !users.isEmpty()) {
            users.forEach(user -> {
                if (user.getTenantId() == UserSelectedTenantContextHolder.get()) {
                    smsList.add(new SmsDTO(user.getCountryCode() + user.getPhoneNumber(), user.getUsername(),
                            notification.getTenantId(), patientTracker.getPatientId(), notification.getId()));
                }
            });
        }
        notificationApiInterface.saveOutBoundSms(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                smsList);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public GlucoseLog createAssessmentGlucoseLog(GlucoseLog glucoseLog) {
        Logger.logInfo("In AssessmentServiceImpl, creating assessment glucose log information");
        if (Objects.isNull(glucoseLog)) {
            throw new DataNotFoundException(1000);
        }
        GlucoseLog existingGlucoseLog = glucoseLogService.findFirstByPatientTrackIdAndIsDeletedOrderByBgTakenOnDesc(
                glucoseLog.getPatientTrackId(), Constants.BOOLEAN_FALSE);
        glucoseLog.setType(Constants.ASSESSMENT);
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
        glucoseLog.setOldRecord(!Objects.isNull(glucoseLog.getBgTakenOn())
                && (Constants.ZERO > formatter.format(glucoseLog.getBgTakenOn()).compareTo(formatter.format(new Date()))));
        if (!Objects.isNull(existingGlucoseLog) && !Objects.isNull(glucoseLog.getBgTakenOn())
                && !Objects.isNull(existingGlucoseLog.getBgTakenOn())
                && (Constants.ZERO > glucoseLog.getBgTakenOn().compareTo(existingGlucoseLog.getBgTakenOn()))) {
            glucoseLog = glucoseLogService.addGlucoseLog(glucoseLog, Constants.BOOLEAN_FALSE);
            patientAssessmentRepository.save(new PatientAssessment(null, glucoseLog.getId(), Constants.MEDICAL_REVIEW,
                    glucoseLog.getTenantId(), glucoseLog.getPatientTrackId()));
        } else {
            PatientTracker patientTracker = patientTrackerService.getPatientTrackerById(glucoseLog.getPatientTrackId());
            String riskLevel;
            glucoseLog.setLatest(Constants.BOOLEAN_TRUE);
            riskLevel = calculateRiskLevel(null, glucoseLog, null, patientTracker);
            if (StringUtils.isBlank(riskLevel)) {
                glucoseLog.setRiskLevel(RED_RISK_LEVEL_LIST.contains(riskLevel) ? Constants.MODERATE : riskLevel);
            }
            glucoseLog = glucoseLogService.addGlucoseLog(glucoseLog, Constants.BOOLEAN_TRUE);
            if (Objects.equals(riskLevel, Constants.HIGH)) {
                addRedRiskNotification(patientTracker, null, glucoseLog.getId(), null);
            }
        }
        return glucoseLog;
    }

    @Override
    public void clearApiPermissions() {
        Logger.logInfo("In AssessmentServiceImpl, clearing api permission");
        authenticationFilter.apiPermissionMap.clear();
    }
}
