package com.mdtlabs.coreplatform.spiceservice.bplog.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.BpLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientBpLogsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientAssessment;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientSymptom;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.spiceservice.assessment.repository.PatientAssessmentRepository;
import com.mdtlabs.coreplatform.spiceservice.bplog.repository.BpLogRepository;
import com.mdtlabs.coreplatform.spiceservice.bplog.service.BpLogService;
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
 * <p>
 * This class implements the BpLogService interface and contains actual business
 * logic to perform operations on BpLog entity.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
@Service
public class BpLogServiceImpl implements BpLogService {

    @Autowired
    private BpLogRepository bpLogRepository;

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
    public BpLog addBpLog(BpLog bpLog, boolean isPatientTrackerUpdate) {
        Logger.logInfo("In BpLogServiceImpl, create new bp log information");
        validationCheck(bpLog);
        bpLog.setAssessmentTenantId(UserSelectedTenantContextHolder.get());
        BpLog bpLogResponse = bpLogRepository.save(bpLog);
        if (isPatientTrackerUpdate) {
            PatientTreatmentPlan patientTreatmentPlan = patientTreatmentPlanService
                    .getPatientTreatmentPlan(bpLog.getPatientTrackId());
            Date nextBpAssessmentDate = null;
            if (!Objects.isNull(patientTreatmentPlan)) {
                nextBpAssessmentDate = patientTreatmentPlanService
                        .getTreatmentPlanFollowupDate(patientTreatmentPlan.getBpCheckFrequency(), Constants.DEFAULT);
            }
            patientTrackerService.updatePatientTrackerForBpLog(bpLog.getPatientTrackId(), bpLog, nextBpAssessmentDate);
            patientAssessmentRepository.save(new PatientAssessment(bpLog.getId(), null, bpLog.getType(),
                    bpLog.getTenantId(), bpLog.getPatientTrackId()));
        }
        return bpLogResponse;
    }

    /**
     * Validation check on bp log entity.
     *
     * @param bpLog {@link BpLog} entity is given
     */
    private void validationCheck(BpLog bpLog) {
        if (Objects.isNull(bpLog)) {
            throw new BadRequestException(1000);
        }
        if (Objects.isNull(bpLog.getId()) && bpLog.isLatest()) {
            BpLog existingBpLog = getBpLogByPatientTrackIdAndIsLatest(bpLog.getPatientTrackId(), Constants.BOOLEAN_TRUE);
            if (!Objects.isNull(existingBpLog)) {
                existingBpLog.setLatest(Constants.BOOLEAN_FALSE);
                bpLogRepository.save(existingBpLog);
            }
        }
        if (Objects.isNull(bpLog.getBpTakenOn())) {
            bpLog.setBpTakenOn(new Date());
        }
    }

    /**
     * {@inheritDoc}
     */
    public BpLog getPatientTodayBpLog(long patientTrackId) {
        Logger.logInfo("In BpLogServiceImpl, get bp log value by patient track id");
        Calendar calender = Calendar.getInstance();
        calender.add(Calendar.DATE, Constants.MINUS_ONE);
        List<BpLog> bpLogs = bpLogRepository.findByPatientTrackIdAndIsCreatedToday(patientTrackId, calender.getTime());
        return (Objects.isNull(bpLogs) || bpLogs.isEmpty()) ? null : bpLogs.get(Constants.ZERO);
    }

    /**
     * {@inheritDoc}
     */
    public BpLog getBpLogByPatientTrackIdAndIsLatest(long patientTrackId, boolean isLatest) {
        Logger.logInfo("In BpLogServiceImpl, get bp log value by patient track id and isLatest");
        return bpLogRepository.findByPatientTrackIdAndIsDeletedAndIsLatest(patientTrackId, Constants.BOOLEAN_FALSE,
                isLatest);
    }

    /**
     * {@inheritDoc}
     */
    public PatientBpLogsDTO getPatientBpLogsWithSymptoms(RequestDTO requestData) {
        Logger.logInfo("In BpLogServiceImpl, get patient bp log value with symptoms");
        if (Objects.isNull(requestData.getPatientTrackId())) {
            throw new DataNotFoundException(1252);
        }
        Pageable pagination = Pagination.setPagination(requestData.getSkip(), requestData.getLimit(),
                Constants.BP_TAKEN_ON, !(!StringUtils.isEmpty(requestData.getSortField())
                        && Constants.BP_TAKEN_ON.equalsIgnoreCase(requestData.getSortField())));
        Page<BpLog> bpLogs = bpLogRepository.findByPatientTrackIdAndIsDeletedFalse(
                requestData.getPatientTrackId(), pagination);
        PatientBpLogsDTO bpLogsDTO = new PatientBpLogsDTO();
        setSymptomList(requestData, bpLogs, bpLogsDTO);
        bpLogsDTO.setLimit(requestData.getLimit());
        bpLogsDTO.setSkip(requestData.getSkip());
        bpLogsDTO.setBpThreshold(Map.of(Constants.SYSTOLIC, Constants.BP_THRESHOLD_SYSTOLIC, Constants.DIASTOLIC,
                Constants.BP_THRESHOLD_DIASTOLIC));
        return bpLogsDTO;
    }

    /**
     * Sets patient symptoms list to bp log dto.
     *
     * @param requestData {@link RequestDTO} request dto is given
     * @param bpLogs      {@link Page<BpLog>}  pageable entity is given
     * @param bpLogsDto   {@link PatientBpLogsDTO}  patient bp logs dto is given
     */
    private void setSymptomList(RequestDTO requestData, Page<BpLog> bpLogs, PatientBpLogsDTO bpLogsDto) {
        List<BpLog> bplogList = new ArrayList<>();
        if (!bpLogs.isEmpty()) {
            if (requestData.getSkip() == Constants.ZERO && requestData.isLatestRequired()) {
                BpLog latestBpLog = bpLogs.toList().get(0);
                bpLogsDto.setLatestBpLog(
                        new BpLogDTO(latestBpLog.getId(), latestBpLog.getAvgSystolic(), latestBpLog.getAvgDiastolic(),
                                latestBpLog.getAvgPulse(), latestBpLog.getCreatedAt(), latestBpLog.getBpTakenOn()));
                List<PatientSymptom> patientSymptoms = patientSymptomService
                        .getSymptomsByBpLogId(requestData.getPatientTrackId(), latestBpLog.getId());
                Long cultureId = cultureService.loadCultureValues();
                Map<Long, String> symptomMap = Constants.CULTURE_VALUES_MAP.get(cultureId).get(Constants.CULTURE_VALUE_SYMPTOMS);
                bpLogsDto.getLatestBpLog().setSymptoms(patientSymptoms.stream().map(
                                symptom -> StringUtils.isNotBlank(symptom.getOtherSymptom()) ? symptom.getOtherSymptom() : symptomMap.get(symptom.getSymptomId()))
                        .toList());
            }
            if (!requestData.isLatestRequired()) {
                bplogList = bpLogs.toList();
            } else {
                bplogList = bpLogs.stream().sorted(
                                Comparator.comparing(BpLog::getBpTakenOn))
                        .toList();
            }
        }
        bpLogsDto.setTotal(bpLogs.getTotalElements());
        setBpLogDto(bplogList, bpLogsDto);
    }

    /**
     * Sets bp log dto by sort fields.
     *
     * @param bpLogs    {@link List<BpLog>} pageable entity is given
     * @param bpLogsDto {@link PatientBpLogsDTO} patient bp logs dto is given
     */
    private void setBpLogDto(List<BpLog> bpLogs, PatientBpLogsDTO bpLogsDto) {
        bpLogsDto.setBpLogList(bpLogs.stream().map(bplog -> new BpLogDTO(bplog.getId(), bplog.getAvgSystolic(),
                bplog.getAvgDiastolic(), bplog.getAvgPulse(), bplog.getCreatedAt(), bplog.getBpTakenOn())).toList());

    }

    /**
     * {@inheritDoc}
     */
    public BpLog findFirstByPatientTrackIdAndIsDeletedOrderByBpTakenOnDesc(Long patientTrackId, Boolean booleanFalse) {
        Logger.logInfo("In BpLogServiceImpl, get bp log value by patient track id and isDeleted");
        return bpLogRepository.findFirstByPatientTrackIdAndIsDeletedOrderByBpTakenOnDesc(patientTrackId, booleanFalse);
    }

    /**
     * This function deactivates and deletes all blood pressure logs associated with a given tracker
     * ID.
     *
     * @param trackerId {@link long} The trackerId is a unique identifier for a patient's blood pressure tracker is given
     */
    public void removeBpLog(long trackerId) {
        List<BpLog> bpLogs = bpLogRepository.findByPatientTrackId(trackerId);
        if (!Objects.isNull(bpLogs)) {
            bpLogs.forEach(bpLog -> {
                bpLog.setActive(false);
                bpLog.setDeleted(true);
            });
            bpLogRepository.saveAll(bpLogs);
        }
    }

    /**
     * {@inheritDoc}
     */
    public BpLog getBpLogById(long bpLogId) {
        Optional<BpLog> bpLog = bpLogRepository.findById(bpLogId);
        return bpLog.orElseThrow(() -> new DataNotFoundException(7004));
    }
}
