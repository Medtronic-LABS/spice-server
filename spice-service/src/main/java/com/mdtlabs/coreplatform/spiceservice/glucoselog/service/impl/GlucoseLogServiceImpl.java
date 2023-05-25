package com.mdtlabs.coreplatform.spiceservice.glucoselog.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.GlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientGlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientAssessment;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientSymptom;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.spiceservice.assessment.repository.PatientAssessmentRepository;
import com.mdtlabs.coreplatform.spiceservice.glucoselog.repository.GlucoseLogRepository;
import com.mdtlabs.coreplatform.spiceservice.glucoselog.service.GlucoseLogService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CultureService;
import com.mdtlabs.coreplatform.spiceservice.patientsymptom.service.PatientSymptomService;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * This class implements the GlucoseLogService interface and contains actual
 * business logic to perform operations on GlucoseLog entity.
 *
 * @author Karthick Murugesan
 * @since Jun 30, 2022
 */
@Service
public class GlucoseLogServiceImpl implements GlucoseLogService {

    @Autowired
    private GlucoseLogRepository glucoseLogRepository;

    @Autowired
    private CultureService cultureService;

    @Autowired
    private PatientSymptomService patientSymptomService;

    @Autowired
    private PatientTrackerService patientTrackerService;

    @Autowired
    private PatientTreatmentPlanService patientTreatmentPlanService;

    @Autowired
    private PatientAssessmentRepository patientAssessmentRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    public GlucoseLog addGlucoseLog(GlucoseLog glucoseLog, boolean isPatientTrackerUpdate) {
        validateGlucoseLog(glucoseLog);
        if (Objects.isNull(glucoseLog.getId()) && glucoseLog.isLatest()) {
            updateGlucoseLogLatestStatus(glucoseLog);
        }
        glucoseLog.setBgTakenOn(Objects.isNull(glucoseLog.getBgTakenOn()) ? new Date() : glucoseLog.getBgTakenOn());
        glucoseLog.setAssessmentTenantId(UserSelectedTenantContextHolder.get());
        GlucoseLog glucoseLogResponse = glucoseLogRepository.save(glucoseLog);
        if (isPatientTrackerUpdate) {
            PatientTreatmentPlan patientTreatmentPlan = patientTreatmentPlanService
                    .getPatientTreatmentPlan(glucoseLog.getPatientTrackId());
            Date nextBgAssessmentDate = null;
            if (!Objects.isNull(patientTreatmentPlan)) {
                nextBgAssessmentDate = patientTreatmentPlanService
                        .getTreatmentPlanFollowupDate(patientTreatmentPlan.getBgCheckFrequency(),
                                Constants.DEFAULT);
            }
            patientTrackerService.updatePatientTrackerForGlucoseLog(glucoseLog.getPatientTrackId(), glucoseLog,
                    nextBgAssessmentDate);
            patientAssessmentRepository.save(new PatientAssessment(null, glucoseLog.getId(), glucoseLog.getType(),
                    glucoseLog.getTenantId(), glucoseLog.getPatientTrackId()));

        }
        return glucoseLogResponse;
    }

    /**
     * {@inheritDoc}
     */
    public void validateGlucoseLog(GlucoseLog glucoseLog) {
        if ((!Objects.isNull(glucoseLog.getGlucoseValue()) || !Objects.isNull(glucoseLog.getGlucoseType())
                || !Objects.isNull(glucoseLog.getGlucoseDateTime()) || !Objects.isNull(glucoseLog.getLastMealTime()))
                && (Objects.isNull(glucoseLog.getGlucoseValue()) || Objects.isNull(glucoseLog.getGlucoseType())
                || Objects.isNull(glucoseLog.getGlucoseDateTime()) || Objects.isNull(glucoseLog.getGlucoseUnit()))) {
            throw new BadRequestException(7005);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void updateGlucoseLogLatestStatus(GlucoseLog glucoseLog) {
        List<GlucoseLog> glucoseLogs = glucoseLogRepository.getGlucoseLogLatestStatus(glucoseLog.getPatientTrackId());
        if (!Objects.isNull(glucoseLogs)) {
            glucoseLogs.forEach(log -> log.setLatest(false));
            glucoseLogRepository.saveAll(glucoseLogs);
        }
    }

    /**
     * {@inheritDoc}
     */
    public GlucoseLog getGlucoseLogById(long glucoseLogId) {
        Optional<GlucoseLog> glucoseLog = glucoseLogRepository.findById(glucoseLogId);
        return glucoseLog.orElseThrow(() -> new DataNotFoundException(7004));
    }

    /**
     * {@inheritDoc}
     */
    public GlucoseLog getGlucoseLogByPatientTrackId(long patientTrackId) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, Constants.MINUS_ONE);
        Date yesterday = cal.getTime();
        List<GlucoseLog> glucoseLogs = glucoseLogRepository.findByPatientTrackIdAndIsCreatedToday(patientTrackId, yesterday);
        return (null == glucoseLogs || glucoseLogs.isEmpty()) ? null : glucoseLogs.get(Constants.ZERO);
    }

    /**
     * {@inheritDoc}
     */
    public GlucoseLog getGlucoseLogByPatientTrackIdAndIsLatest(long patientTrackId, boolean isLatest) {
        return glucoseLogRepository.findByPatientTrackIdAndIsDeletedAndIsLatest(patientTrackId, Constants.BOOLEAN_FALSE,
                isLatest);
    }

    /**
     * {@inheritDoc}
     */
    public PatientGlucoseLogDTO getPatientGlucoseLogsWithSymptoms(RequestDTO requestData) {
        PatientGlucoseLogDTO glucoseLogsResponse = new PatientGlucoseLogDTO();
        if (Objects.isNull(requestData.getPatientTrackId())) {
            throw new DataNotAcceptableException(1256);
        }
        Pageable pageable = Pagination.setPagination(requestData.getSkip(), requestData.getLimit(),
                Constants.BG_TAKEN_ON, !(!StringUtils.isEmpty(requestData.getSortField())
                        && Constants.BG_TAKEN_ON.equalsIgnoreCase(requestData.getSortField())));
        Page<GlucoseLog> glucoseLogs = glucoseLogRepository.getGlucoseLogs(requestData.getPatientTrackId(), pageable);
        setSymptomList(requestData, glucoseLogs, glucoseLogsResponse);
        glucoseLogsResponse.setLimit(requestData.getLimit());
        glucoseLogsResponse.setSkip(requestData.getSkip());
        List<Map<String, Object>> thresholds = new ArrayList<>();
        thresholds.add(Map.of(Constants.FBS, Constants.FBS_MMOL_L, Constants.RBS, Constants.RBS_MMOL_L, Constants.UNIT,
                Constants.GLUCOSE_UNIT_MMOL_L));
        thresholds.add(Map.of(Constants.FBS, Constants.FBS_MG_DL, Constants.RBS, Constants.RBS_MG_DL, Constants.UNIT,
                Constants.GLUCOSE_UNIT_MG_DL));
        glucoseLogsResponse.setGlucoseThreshold(thresholds);
        return glucoseLogsResponse;
    }

    /**
     * {@inheritDoc}
     */
    private void setSymptomList(RequestDTO requestData, Page<GlucoseLog> glucoseLogs, PatientGlucoseLogDTO glucoseLogsResponse) {
        List<GlucoseLog> glucoseLogsList = new ArrayList<>();
        if (!glucoseLogs.isEmpty()) {
            if (requestData.getSkip() == 0 && requestData.isLatestRequired()) {
                GlucoseLog latestGlucoseLog = glucoseLogs.toList().get(0);
                glucoseLogsResponse.setLatestGlucoseLog(new GlucoseLogDTO(latestGlucoseLog.getId(), latestGlucoseLog.getHba1c(),
                        latestGlucoseLog.getHba1cUnit(), latestGlucoseLog.getGlucoseType(),
                        latestGlucoseLog.getGlucoseValue(), latestGlucoseLog.getGlucoseDateTime(),
                        latestGlucoseLog.getGlucoseUnit(), latestGlucoseLog.getCreatedAt()));
                List<PatientSymptom> patientSymptoms = patientSymptomService
                        .getSymptomsByGlucoseLogId(requestData.getPatientTrackId(), latestGlucoseLog.getId());
                Long cultureId = cultureService.loadCultureValues();
                Map<Long, String> symptomMap = Constants.CULTURE_VALUES_MAP.get(cultureId).get(Constants.CULTURE_VALUE_SYMPTOMS);
                glucoseLogsResponse.getLatestGlucoseLog().setSymptoms(patientSymptoms.stream().map(
                                symptom -> StringUtils.isNotBlank(symptom.getOtherSymptom()) ? symptom.getOtherSymptom() : symptomMap.get(symptom.getSymptomId()))
                        .toList());
            }
            if (!requestData.isLatestRequired()) {
                glucoseLogsList = glucoseLogs.toList();
            } else {
                glucoseLogsList = glucoseLogs.stream().sorted(
                                Comparator.comparing(GlucoseLog::getBgTakenOn))
                        .toList();
            }
        }
        setGlucoseLogList(glucoseLogsList, glucoseLogsResponse);
        glucoseLogsResponse.setTotal(glucoseLogs.getTotalElements());
    }


    /**
     * {@inheritDoc}
     */
    private void setGlucoseLogList(List<GlucoseLog> glucoseLogs,
                                   PatientGlucoseLogDTO glucoseLogsDto) {
        glucoseLogsDto.setGlucoseLogList(glucoseLogs.stream()
                .map(glucoselog -> new GlucoseLogDTO(glucoselog.getId(), glucoselog.getHba1c(),
                        glucoselog.getHba1cUnit(), glucoselog.getGlucoseType(), glucoselog.getGlucoseValue(),
                        glucoselog.getGlucoseDateTime(), glucoselog.getGlucoseUnit(), glucoselog.getCreatedAt()))
                .toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GlucoseLog findFirstByPatientTrackIdAndIsDeletedOrderByBgTakenOnDesc(Long patientTrackId,
                                                                                Boolean booleanFalse) {
        return glucoseLogRepository.findFirstByPatientTrackIdAndIsDeletedOrderByBgTakenOnDesc(patientTrackId,
                booleanFalse);
    }

    /**
     * {@inheritDoc}
     */
    public void removeGlucoseLog(long trackerId) {
        List<GlucoseLog> glucoseLogs = glucoseLogRepository.findByPatientTrackId(trackerId);
        if (!Objects.isNull(glucoseLogs)) {
            glucoseLogs.forEach(glucoseLog -> {
                glucoseLog.setActive(false);
                glucoseLog.setDeleted(true);
            });
            glucoseLogRepository.saveAll(glucoseLogs);
        }
    }
}
