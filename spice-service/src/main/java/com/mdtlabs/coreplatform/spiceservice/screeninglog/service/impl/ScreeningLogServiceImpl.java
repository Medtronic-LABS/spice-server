package com.mdtlabs.coreplatform.spiceservice.screeninglog.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.UnitConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.DiagnosisDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ScreeningLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ScreeningResponseDTO;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientAssessment;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientDiagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.ScreeningLog;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.ConversionUtil;
import com.mdtlabs.coreplatform.spiceservice.AdminApiInterface;
import com.mdtlabs.coreplatform.spiceservice.assessment.repository.PatientAssessmentRepository;
import com.mdtlabs.coreplatform.spiceservice.bplog.service.BpLogService;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.ScreeningMapper;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientDiagnosisRepository;
import com.mdtlabs.coreplatform.spiceservice.customizedmodules.service.CustomizedModulesService;
import com.mdtlabs.coreplatform.spiceservice.glucoselog.service.GlucoseLogService;
import com.mdtlabs.coreplatform.spiceservice.mentalhealth.service.MentalHealthService;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.screeninglog.repository.ScreeningLogRepository;
import com.mdtlabs.coreplatform.spiceservice.screeninglog.service.ScreeningLogService;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * ScreeningLogServiceImpl class implements various methods for managing screeningLog,including adding and
 * retrieving screeningLog.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
@Service
public class ScreeningLogServiceImpl implements ScreeningLogService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private AdminApiInterface apiInterface;

    @Autowired
    private BpLogService bpLogService;

    @Autowired
    private CustomizedModulesService customizedModulesService;

    @Autowired
    private GlucoseLogService glucoseLogService;

    @Autowired
    private MentalHealthService mentalHealthService;

    @Autowired
    private PatientDiagnosisRepository patientDiagnosisRepository;

    @Autowired
    private PatientTrackerService patientTrackerService;

    @Autowired
    private ScreeningLogRepository screeningLogRepository;

    @Autowired
    private ScreeningMapper screeningMapper;

    @Autowired
    private PatientAssessmentRepository patientAssessmentRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    public ScreeningLog createScreeningLog(ScreeningLogDTO screeningLogRequest) {
        Logger.logInfo("Creating new screening information");
        Site site = validateScreeningLog(screeningLogRequest);
        PatientTracker patientTracker = null;
        ScreeningLog screeningLogResponse = null;
        if (Objects.nonNull(site)) {
            ScreeningLog screeningLogData = constructScreeningLogData(screeningLogRequest, site);
            patientTracker = createPatientTracker(screeningLogRequest, site);
            screeningLogResponse = screeningLogRepository.save(screeningLogData);
            patientTracker.setScreeningLogId(screeningLogResponse.getId());
            if (Objects.isNull(patientTracker.getId()) && !patientTracker.getPatientStatus().equals(Constants.ENROLLED)) {
                patientTracker = patientTrackerService.addOrUpdatePatientTracker(patientTracker);
            }
            BpLog bpLog = createBpLog(screeningLogRequest, patientTracker, screeningLogResponse);
            GlucoseLog glucoseLog = createGlucoseLog(screeningLogRequest, patientTracker, screeningLogResponse);
            Long glucoseId = Objects.isNull(glucoseLog) ? null : glucoseLog.getId();
            patientAssessmentRepository.save(new PatientAssessment(bpLog.getId(), glucoseId, Constants.SCREENING,
                    site.getTenantId(), patientTracker.getId()));
        }
        if (Objects.nonNull(patientTracker) && Objects.nonNull(patientTracker.getId())) {
            customizedModulesService.createCustomizedModules(screeningLogRequest.getCustomizedWorkflows(),
                    Constants.WORKFLOW_SCREENING, patientTracker.getId());
        }
        return screeningLogResponse;
    }

    /**
     * <p>
     * This method is used to create glucose log object and sets its properties based on the given screening log
     * request, patient tracker, and screening log response.
     * </p>
     *
     * @param screeningLogRequest  {@link ScreeningLogDTO} The screening log request contains the request data for
     *                             creating a screening log, including a glucose log (if provided) is given
     * @param patientTracker       {@link PatientTracker} The patient tracker contains information about the patient
     *                             being screened is given
     * @param screeningLogResponse {@link ScreeningLog} The ScreeningLogResponse which need to set is given
     * @return {@link GlucoseLog} The GlucoseLog for the given details is created and returned
     */
    private GlucoseLog createGlucoseLog(ScreeningLogDTO screeningLogRequest, PatientTracker patientTracker,
                                        ScreeningLog screeningLogResponse) {
        GlucoseLog glucoseLog = null;
        if (CommonUtil.isGlucoseLogGiven(screeningLogRequest.getGlucoseLog())) {
            glucoseLog = screeningLogRequest.getGlucoseLog();
            glucoseLog.setPatientTrackId(patientTracker.getId());
            glucoseLog.setType(Constants.SCREENING);
            glucoseLog.setLatest(Constants.BOOLEAN_TRUE);
            glucoseLog.setScreeningId(screeningLogResponse.getId());
            glucoseLogService.addGlucoseLog(glucoseLog, Constants.BOOLEAN_FALSE);
        }
        return glucoseLog;
    }

    /**
     * <p>
     * This method is used to constructs a ScreeningLog by mapping data from a ScreeningLogDTO and a Site.
     * </p>
     *
     * @param screeningLogRequest {@link ScreeningLogDTO} The Screening log request contains the data for a screening
     *                            log is given
     * @param site                {@link Site} The site contains information about the screening site where the
     *                            screening log is being recorded is given
     * @return {@link ScreeningLog} The screening log for the given screeningLogRequest and site is constructed
     * and returned
     */
    private ScreeningLog constructScreeningLogData(ScreeningLogDTO screeningLogRequest, Site site) {
        Logger.logInfo("Adding screening log information");
        ScreeningLog screeningLog = new ScreeningLog();
        if (!Objects.isNull(screeningLogRequest.getBioMetrics())) {
            screeningMapper.mapBiometricsData(screeningLog, screeningLogRequest.getBioMetrics());
        }
        if (!Objects.isNull(screeningLogRequest.getBioData())) {
            screeningMapper.mapBioData(screeningLog, screeningLogRequest.getBioData());
        }
        if (!Objects.isNull(screeningLogRequest.getBpLog())) {
            screeningMapper.mapBpLog(screeningLog, screeningLogRequest.getBpLog());
        }
        if (CommonUtil.isGlucoseLogGiven(screeningLogRequest.getGlucoseLog())) {
            screeningMapper.mapGlucoseLog(screeningLog, screeningLogRequest.getGlucoseLog());
        }
        screeningMapper.mapScreeningLog(screeningLogRequest, screeningLog, site);
        return screeningLog;
    }

    /**
     * <p>
     * This method is used to create a BP log information using data from a screening log request and adds it to the
     * BP log service.
     * </p>
     *
     * @param screeningLogRequest  {@link ScreeningLogDTO} The screening log request contains the request information
     *                             for creating a BP log is given
     * @param patientTracker       {@link PatientTracker} The PatientTracker contains information about the patient
     *                             being tracked is given
     * @param screeningLogResponse {@link ScreeningLog} The screening log response which need to set is given
     * @return {@link BpLog} The BpLog for the given provided details is created and returned
     */
    public BpLog createBpLog(ScreeningLogDTO screeningLogRequest, PatientTracker patientTracker,
                             ScreeningLog screeningLogResponse) {
        Logger.logInfo("Creating BP log information");
        BpLog bpLog = screeningLogRequest.getBpLog();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(screeningLogRequest.getBioMetrics(), bpLog);
        bpLog.setType(Constants.SCREENING);
        bpLog.setCvdRiskScore(screeningLogRequest.getCvdRiskScore());
        bpLog.setCvdRiskLevel(screeningLogRequest.getCvdRiskLevel());
        bpLog.setPatientTrackId(patientTracker.getId());
        bpLog.setLatest(Constants.BOOLEAN_TRUE);
        bpLog.setScreeningId(screeningLogResponse.getId());
        return bpLogService.addBpLog(bpLog, Constants.BOOLEAN_FALSE);
    }

    /**
     * <p>
     * This method is used to validate screening log information and returns the site.
     * </p>
     *
     * @param screeningLogRequest {@link ScreeningLogDTO} The screening log request containing information about a
     *                            screening log is given
     * @return {@link Site} The Site for the given screening log request is validated and returned
     */
    private Site validateScreeningLog(ScreeningLogDTO screeningLogRequest) {
        Logger.logInfo("Validating screening log information");
        if (Objects.isNull(screeningLogRequest)) {
            Logger.logError("Request should not be empty");
            throw new BadRequestException(1000);
        }
        if (Objects.isNull(screeningLogRequest.getBpLog().getBpLogDetails())
                || Constants.TWO > screeningLogRequest.getBpLog().getBpLogDetails().size()) {
            Logger.logError("Should have minimum two BpLog details");
            throw new BadRequestException(8001);
        }
        Site site = apiInterface
                .getSiteById(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(), screeningLogRequest.getSiteId())
                .getBody();
        if (Objects.nonNull(site)) {
            screeningLogRequest.setSiteId(site.getId());
            screeningLogRequest.setCountryId(site.getCountryId());
            screeningLogRequest.getBpLog().setTenantId(site.getTenantId());
            if (CommonUtil.isGlucoseLogGiven(screeningLogRequest.getGlucoseLog())) {
                screeningLogRequest.getGlucoseLog().setTenantId(site.getTenantId());
            }
        }
        if (UnitConstants.IMPERIAL.equalsIgnoreCase(screeningLogRequest.getUnitMeasurement())) {
            screeningLogRequest.getBioMetrics().setHeight(
                    ConversionUtil.convertHeight(screeningLogRequest.getBioMetrics().getHeight(), UnitConstants.IMPERIAL));
            screeningLogRequest.getBioMetrics().setWeight(
                    ConversionUtil.convertWeight(screeningLogRequest.getBioMetrics().getWeight(), UnitConstants.IMPERIAL));
        }
        return site;
    }

    /**
     * <p>
     * This method is used to create a patient tracker object based on screening log data and site information,
     * and updates existing patient tracker data if it exists.
     * </p>
     *
     * @param screeningLogRequest {@link ScreeningLogDTO} The screening log request contains information related to a
     *                            patient's screening log such as their bio data, blood pressure and
     *                            glucose values, etc. is given
     * @param site                {@link Site} The site  contains information about the healthcare facility where the
     *                            patient tracker information is being created is given
     * @return {@link PatientTracker} The  PatientTracker for the given screening log request and site is created and
     * returned
     */
    public PatientTracker createPatientTracker(ScreeningLogDTO screeningLogRequest, Site site) {
        Logger.logInfo("Creating new patient tracker information");
        PatientTracker patientTracker;
        PatientTracker existingPatientTracker = patientTrackerService
                .findByNationalIdIgnoreCaseAndCountryIdAndIsDeleted(screeningLogRequest.getBioData().getNationalId(),
                        UserContextHolder.getUserDto().getCountry().getId(), Constants.BOOLEAN_FALSE);
        if (!Objects.isNull(existingPatientTracker)) {
            if (existingPatientTracker.getPatientStatus().equals(Constants.ENROLLED)) {
                updateBpAndGlucoseValues(existingPatientTracker, screeningLogRequest);
                patientTracker = existingPatientTracker;
            } else {
                patientTracker = constructPatientTracker(existingPatientTracker, screeningLogRequest, site);
            }
            if (!Objects.isNull(existingPatientTracker.getScreeningLogId())) {
                ScreeningLog existingScreeningLog = screeningLogRepository.findById(existingPatientTracker.getScreeningLogId())
                        .orElseThrow(() -> new DataNotFoundException(9007));
                existingScreeningLog.setIsLatest(Constants.BOOLEAN_FALSE);
                screeningLogRepository.save(existingScreeningLog);
            }
        } else {
            patientTracker = constructPatientTracker(new PatientTracker(), screeningLogRequest, site);
        }
        return patientTracker;
    }

    /**
     * <p>
     * This method is used to construct patient tracker information by mapping screening log data, updating blood
     * pressure and glucose values, and setting PHQ4 scores and risk levels if available.
     * </p>
     *
     * @param patientTracker {@link PatientTracker} The patient tracker stores information about a patient's screening
     *                       and assessment results is given
     * @param screeningLog   {@link ScreeningLogDTO} The screening log contains information related to a patient's
     *                       screening log such as blood pressure, glucose level, mental health score, etc. is given
     * @param site           {@link Site} The site where the patient screening was conducted is given
     * @return {@link PatientTracker} The PatientTracker for the given details is constructed and returned
     */
    public PatientTracker constructPatientTracker(PatientTracker patientTracker, ScreeningLogDTO screeningLog,
                                                  Site site) {
        Logger.logInfo("Constructing patient tracker information");
        screeningMapper.mapPatientTracker(patientTracker, screeningLog, site);
        updateBpAndGlucoseValues(patientTracker, screeningLog);
        patientTracker.setScreeningReferral(screeningLog.getIsReferAssessment());
        if (!Objects.isNull(screeningLog.getPhq4())) {
            MentalHealth phq4 = screeningLog.getPhq4();
            mentalHealthService.setPhq4Score(phq4);
            patientTracker.setPhq4FirstScore(phq4.getPhq4FirstScore());
            patientTracker.setPhq4SecondScore(phq4.getPhq4SecondScore());
            patientTracker.setPhq4RiskLevel(screeningLog.getPhq4().getPhq4RiskLevel());
            patientTracker.setPhq4Score(screeningLog.getPhq4().getPhq4Score());
        }
        return patientTracker;
    }

    /**
     * <p>
     * This method is used to updates a patient's blood pressure and glucose values based on a screening log.
     * </p>
     *
     * @param patientTracker {@link PatientTracker} The patient tracker which contains information about a patient's
     *                       health status and screening logs is given
     * @param screeningLog   {@link ScreeningLogDTO} The screeningLog contains screening log information of a patient,
     *                       including blood pressure (BP) and glucose values is given
     */
    public void updateBpAndGlucoseValues(PatientTracker patientTracker, ScreeningLogDTO screeningLog) {
        Logger.logInfo("Updating BP and glucose values");
        patientTracker.setAvgDiastolic(screeningLog.getBpLog().getAvgDiastolic());
        patientTracker.setAvgSystolic(screeningLog.getBpLog().getAvgSystolic());
        patientTracker.setAvgPulse(screeningLog.getBpLog().getAvgPulse());
        patientTracker.setCvdRiskScore(screeningLog.getCvdRiskScore());
        patientTracker.setCvdRiskLevel(screeningLog.getCvdRiskLevel());
        if (CommonUtil.isGlucoseLogGiven(screeningLog.getGlucoseLog())) {
            patientTracker.setGlucoseValue(screeningLog.getGlucoseLog().getGlucoseValue());
            patientTracker.setGlucoseUnit(screeningLog.getGlucoseLog().getGlucoseUnit());
            patientTracker.setGlucoseType(screeningLog.getGlucoseLog().getGlucoseType());
        }
        patientTracker.setLastAssessmentDate(new Date());
    }

    /**
     * {@inheritDoc}
     */
    public ScreeningLog getByIdAndIsLatest(long screeningId) {
        Logger.logInfo("Fetching screening log information");
        return screeningLogRepository.findByIdAndIsDeletedFalseAndIsLatestTrue(screeningId);
    }

    /**
     * {@inheritDoc}
     */
    public ScreeningResponseDTO getScreeningDetails(RequestDTO request) {
        Logger.logInfo("Fetching screening log information");
        if (Objects.isNull(request.getPatientTrackId())) {
            Logger.logError("Patient Tracker Id is Empty");
            throw new DataNotAcceptableException(1256);
        }
        if (Objects.isNull(request.getScreeningId())) {
            Logger.logError("Screening id is empty");
            throw new DataNotAcceptableException(9005);
        }
        ScreeningLog screeningLog = screeningLogRepository
                .findByIdAndIsDeletedAndIsLatest(request.getScreeningId(), false, true);
        ScreeningResponseDTO screeningResponse = modelMapper.map(screeningLog, ScreeningResponseDTO.class);
        BpLog bpLog = bpLogService.getPatientTodayBpLog(request.getPatientTrackId());
        setBpLogAndGlucoseLog(screeningResponse,
                glucoseLogService.getGlucoseLogByPatientTrackId(request.getPatientTrackId()),
                bpLog);
        screeningResponse
                .setAge(ConversionUtil.calculatePatientAge(screeningLog.getAge(), screeningLog.getCreatedAt()));
        setDiagnosis(request, screeningResponse);
        return screeningResponse;
    }

    /**
     * <p>
     * This method is used to set bp log and glucose log information in a screening response object.
     * </p>
     *
     * @param screeningResponse {@link ScreeningResponseDTO} The screening response contains information about a
     *                          patient's screening response is given
     * @param latestGlucoseLog  {@link GlucoseLog} The latest glucose log object containing information about a user's
     *                          glucose levels is given
     * @param latestBpLog       {@link BpLog} The latest blood pressure log of a user is given
     */
    private void setBpLogAndGlucoseLog(ScreeningResponseDTO screeningResponse, GlucoseLog latestGlucoseLog,
                                       BpLog latestBpLog) {
        Logger.logInfo("Constructing bp log and glucose log information");
        if (!Objects.isNull(latestBpLog)) {
            screeningResponse.setHeight(latestBpLog.getHeight());
            screeningResponse.setIsRegularSmoker(latestBpLog.getIsRegularSmoker());
            screeningResponse.setWeight(latestBpLog.getWeight());
            screeningResponse.setBpLogDetails(latestBpLog.getBpLogDetails());
            screeningResponse.setBpLogId(latestBpLog.getId());
            if (UnitConstants.IMPERIAL.equals(UserContextHolder.getUserDto().getCountry().getUnitMeasurement())) {
                Logger.logInfo("Convert the value to Imperial");
                screeningResponse.setHeight(ConversionUtil.convertHeight(latestBpLog.getHeight(), UnitConstants.METRIC));
                screeningResponse.setWeight(ConversionUtil.convertWeight(latestBpLog.getWeight(), UnitConstants.METRIC));
            }
        }
        if (!Objects.isNull(latestGlucoseLog)) {
            screeningResponse.setGlucoseLog(modelMapper.map(latestGlucoseLog, new TypeToken<GlucoseLogDTO>() {
            }.getType()));
            screeningResponse.getGlucoseLog().setGlucoseLogId(latestGlucoseLog.getId());
        }
    }

    /**
     * <p>
     * This method is used to sets the diagnosis of a patient based on their patient track ID and updates it with any
     * confirmed diagnoses from their patient tracker.
     * </p>
     *
     * @param screeningRequestDto {@link RequestDTO} The screeningRequest contains information related to a patient's
     *                            screening request is given
     * @param screeningResponse   {@link ScreeningResponseDTO} The screeningResponse is used to store the response data
     *                            for a screening request is given
     */
    private void setDiagnosis(RequestDTO screeningRequestDto, ScreeningResponseDTO screeningResponse) {
        PatientDiagnosis patientDiagnosis = patientDiagnosisRepository.findByPatientTrackIdAndIsActiveAndIsDeleted(
                screeningRequestDto.getPatientTrackId(), Boolean.TRUE, Boolean.FALSE);
        if (!Objects.isNull(patientDiagnosis)) {
            screeningResponse.setDiagnosis(modelMapper.map(patientDiagnosis, DiagnosisDTO.class));
        }
        PatientTracker patientTracker = patientTrackerService
                .getPatientTrackerById(screeningRequestDto.getPatientTrackId());
        if (!Objects.isNull(patientTracker) && !Objects.isNull(patientTracker.getConfirmDiagnosis())) {
            screeningResponse.getDiagnosis().getConfirmDiagnosis()
                    .addAll(patientTracker.getConfirmDiagnosis());
            screeningResponse.getDiagnosis().setDiagnosisDiabetes(patientTracker.getConfirmDiagnosis().stream()
                    .filter(Constants.COMMON_DIABETES_CONFIRM_DIAGNOSIS::contains).findFirst().orElse(Constants.EMPTY));
            screeningResponse.getDiagnosis().setDiagnosisHypertension(patientTracker.getConfirmDiagnosis().stream()
                    .filter(Constants.HTM_CONFIRM_DIAGNOSIS::contains).findFirst().orElse(Constants.EMPTY));
        }
    }
}
