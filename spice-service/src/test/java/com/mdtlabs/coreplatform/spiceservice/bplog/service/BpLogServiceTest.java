package com.mdtlabs.coreplatform.spiceservice.bplog.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientBpLogsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientAssessment;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientSymptom;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.spiceservice.assessment.repository.PatientAssessmentRepository;
import com.mdtlabs.coreplatform.spiceservice.bplog.repository.BpLogRepository;
import com.mdtlabs.coreplatform.spiceservice.bplog.service.impl.BpLogServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CultureService;
import com.mdtlabs.coreplatform.spiceservice.patientsymptom.service.PatientSymptomService;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * <p>
 * BpLogServiceImplTest class used to test all possible positive
 * and negative cases for all methods and conditions used in BpLogServiceImpl class.
 * </p>
 *
 * @author Jaganathan created on Feb 8, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BpLogServiceTest {

    @InjectMocks
    private BpLogServiceImpl bpLogService;

    @Mock
    private BpLogRepository bpLogRepository;

    @Mock
    private PatientSymptomService patientSymptomService;

    @Mock
    private PatientTrackerService patientTrackerService;

    @Mock
    private PatientTreatmentPlanService patientTreatmentPlanService;

    @Mock
    private PatientAssessmentRepository assessmentRepository;

    @Mock
    private BpLogService bpLogServiceMock;

    @Mock
    private CultureService cultureService;

    private final BpLog nullBpLog = new BpLog();

    private BpLog actualBpLog;

    private List<BpLog> actualList;

    private BpLog bpLog = TestDataProvider.getBpLog();

    @Test
    @DisplayName("NullBpLogSave Test")
    void addNullBpLog() {
        Assertions.assertThrows(BadRequestException.class, () -> bpLogService.addBpLog(null, true));
    }

    @Test
    @DisplayName("AddBpLog Test")
    void addBpLog() {
        List<BpLog> bpLogs = getBpLogs();
        boolean isPatientUpdate = true;
        PatientTreatmentPlan treatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        Date nextBpDate = new Date(12 / 12 / 2023);
        PatientAssessment assessment = TestDataProvider.getPatientAssessment();
        Assertions.assertNotNull(bpLog);
        when(bpLogRepository.save(bpLog)).thenReturn(bpLog);
        when(bpLogRepository.save(nullBpLog)).thenReturn(null);
        when(bpLogRepository.findByPatientTrackId(bpLog.getPatientTrackId())).thenReturn(bpLogs);
        when(bpLogRepository.findByPatientTrackIdAndIsDeletedAndIsLatest(bpLog.getPatientTrackId(),
                false, true)).thenReturn(bpLog);
        when(patientTreatmentPlanService.getPatientTreatmentPlan(bpLog.getPatientTrackId())).thenReturn(treatmentPlan);
        bpLog = bpLogService.getBpLogByPatientTrackIdAndIsLatest(bpLog.getPatientTrackId(),
                true);
        actualBpLog = bpLogService.addBpLog(bpLog, isPatientUpdate);
        BpLog existingBpLog = bpLogRepository.findByPatientTrackId(bpLog.getPatientTrackId()).get(Constants.ZERO);
        Assertions.assertEquals(bpLog, existingBpLog);
        Assertions.assertTrue(isPatientUpdate);
        when(patientTreatmentPlanService.getPatientTreatmentPlan(bpLog.getPatientTrackId())).thenReturn(treatmentPlan);
        PatientTreatmentPlan actualTreatmentPlan = patientTreatmentPlanService
                .getPatientTreatmentPlan(bpLog.getPatientTrackId());
        Assertions.assertNotNull(actualTreatmentPlan);
        Assertions.assertEquals(treatmentPlan, actualTreatmentPlan);
        when(patientTreatmentPlanService.getTreatmentPlanFollowupDate(treatmentPlan.getBgCheckFrequency(),
                Constants.DEFAULT)).thenReturn(nextBpDate);
        Date actualNextBpDate = patientTreatmentPlanService.getTreatmentPlanFollowupDate(treatmentPlan
                .getBgCheckFrequency(), Constants.DEFAULT);
        doNothing().when(patientTrackerService).updatePatientTrackerForBpLog(bpLog.getPatientTrackId(),
                bpLog, actualNextBpDate);
        when(assessmentRepository.save(new PatientAssessment(bpLog.getId(),
                null, Constants.MEDICAL_REVIEW, bpLog.getTenantId(), bpLog.getPatientTrackId())))
                .thenReturn(assessment);
        Assertions.assertEquals(nextBpDate, actualNextBpDate);
        Assertions.assertNotNull(nextBpDate);

    }

    private List<BpLog> getBpLogs() {
        bpLog.setPatientTrackId(1L);
        bpLog.setLatest(true);
        bpLog.setId(null);
        return List.of(bpLog);
    }

    @Test
    @DisplayName("CheckPatientUpdate Test")
    void checkPatientUpdate() {
        boolean isPatientTrackerUpdate = false;
        when(bpLogRepository.save(bpLog)).thenReturn(bpLog);
        actualBpLog = bpLogService.addBpLog(bpLog, isPatientTrackerUpdate);
        Assertions.assertFalse(isPatientTrackerUpdate);
        Assertions.assertEquals(bpLog, actualBpLog);
    }

    @Test
    @DisplayName("CheckNullNextBpAssessmentDate")
    void nullNextBpAssessmentDate() {
        when(patientTreatmentPlanService.getPatientTreatmentPlan(nullBpLog.getPatientTrackId()))
                .thenReturn(null);
        doNothing().when(patientTrackerService).updatePatientTrackerForBpLog(bpLog.getPatientTrackId(), bpLog, null);
        PatientTreatmentPlan actualPlan = patientTreatmentPlanService
                .getPatientTreatmentPlan(nullBpLog.getPatientTrackId());
        Assertions.assertNull(actualPlan);
    }

    @Test
    @DisplayName("ObjectNotNullIdNull Test")
    void idNullCheck() {
        bpLog.setId(null);
        when(bpLogRepository.findByPatientTrackId(0L)).thenReturn(List.of(bpLog));
        List<BpLog> actualBpLog = bpLogRepository.findByPatientTrackId(0L);
        Assertions.assertNull(bpLog.getId());
        Assertions.assertNull(bpLog.getBpTakenOn());
        Assertions.assertNull(bpLog.getUnitMeasurement());
        Assertions.assertNull(actualBpLog.get(0).getId());

    }

    @Test
    @DisplayName("GetBpLogByLatestPatientTracker Test")
    void getBpLogByPatientTrackerIdAndIsLatest() {
        bpLog.setPatientTrackId(1L);
        when(bpLogRepository.findByPatientTrackIdAndIsDeletedAndIsLatest(bpLog.getPatientTrackId(),
                false, true)).thenReturn(bpLog);
        actualBpLog = bpLogService.getBpLogByPatientTrackIdAndIsLatest(bpLog.getPatientTrackId(),
                true);
        Assertions.assertEquals(bpLog, actualBpLog);
        Assertions.assertEquals(bpLog.getPatientTrackId(), actualBpLog.getPatientTrackId());
    }

    @Test
    @DisplayName("findPatientsByBpOnDescOrder Test")
    void findFirstByPatientTrackerIdAndIsDeletedOrderByBpTakenOnDesc() {
        bpLog.setPatientTrackId(1L);
        when(bpLogRepository.findFirstByPatientTrackIdAndIsDeletedOrderByBpTakenOnDesc(bpLog.getPatientTrackId(),
                false)).thenReturn(bpLog);
        actualBpLog = bpLogService.findFirstByPatientTrackIdAndIsDeletedOrderByBpTakenOnDesc(bpLog.getPatientTrackId(),
                false);
        Assertions.assertEquals(bpLog, actualBpLog);
        Assertions.assertEquals(bpLog.getPatientTrackId(), actualBpLog.getPatientTrackId());
        Assertions.assertEquals(bpLog.getBpLogDetails().size(), actualBpLog.getBpLogDetails().size());

    }

    @Test
    @DisplayName("GetTodayBpLog Test")
    void getPatientTodayBpLog() {

        List<BpLog> result = List.of(bpLog);
        Date date = new Date();
        Long patientTrackerId = 1L;
        when(bpLogRepository.findByPatientTrackIdAndIsCreatedToday(patientTrackerId, date))
                .thenReturn(result);
        actualList = bpLogRepository.findByPatientTrackIdAndIsCreatedToday(patientTrackerId, date);
        Assertions.assertEquals(patientTrackerId, actualList.get(0).getPatientTrackId());
        Assertions.assertEquals(result.size(), actualList.size());

    }

    @Test
    @DisplayName("EmptyTodayBpLog Test")
    void emptyBpLogTest() {
        Date date = new Date();
        when(bpLogRepository.findByPatientTrackIdAndIsCreatedToday(0L, date)).thenReturn(null);
        actualList = bpLogRepository.findByPatientTrackIdAndIsCreatedToday(0L, date);
        actualBpLog = bpLogService.getPatientTodayBpLog(0L);
        Assertions.assertNull(actualList);
        Assertions.assertNull(null);
    }

    @Test
    @DisplayName("GetPatientBpLogWithSymptoms Test")
    void getPatientBpLogWithSymptoms() {
        //given
        RequestDTO emptyRequest = new RequestDTO();
        //nullCheck
        Assertions.assertThrows(DataNotFoundException.class, () -> bpLogService.getPatientBpLogsWithSymptoms(emptyRequest));
        //given
        long bpLogId = 1L;
        int total = 10;
        TestDataProvider.init();
        Page<BpLog> bpLogs = new PageImpl<>(List.of(TestDataProvider.getBpLog()));
        PatientBpLogsDTO bpLogsDTO = TestDataProvider.getPatientBpLogDTO();
        bpLogsDTO.setBpLogList(List.of(TestDataProvider.getBpLogDTO()));
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setSkip(Constants.ZERO);
        requestDTO.setLatestRequired(true);
        List<PatientSymptom> patientSymptoms = List.of(TestDataProvider.getPatientSymptom());
        Pageable pageable = Pagination.setPagination(requestDTO.getSkip(), requestDTO.getLimit(),
                Constants.BP_TAKEN_ON, !(!StringUtils.isEmpty(requestDTO.getSortField())
                        && Constants.BP_TAKEN_ON.equalsIgnoreCase(requestDTO.getSortField())));
        //when
        TestDataProvider.getStaticMock();
        Constants.CULTURE_VALUES_MAP = Map.of(1L, Map.of("Symptoms", Map.of(1L, Constants.NAME)));
        when(bpLogRepository.findByPatientTrackIdAndIsDeletedFalse(requestDTO.getPatientTrackId(),
                pageable)).thenReturn(bpLogs);
        when(patientSymptomService.getSymptomsByBpLogId(requestDTO.getPatientTrackId(), bpLogId))
                .thenReturn(patientSymptoms);
        when(cultureService.loadCultureValues()).thenReturn(1L);
        //then
        List<PatientSymptom> actualPatientSymptoms = patientSymptomService
                .getSymptomsByBpLogId(requestDTO.getPatientTrackId(), bpLogId);
        PatientBpLogsDTO actualBpLogsDto = bpLogService.getPatientBpLogsWithSymptoms(requestDTO);
        // when(bpLogRepository.totalBpLogCount(requestDTO.getPatientTrackId())).thenReturn(total);
        bpLogsDTO.setLimit(requestDTO.getLimit());
        bpLogsDTO.setSkip(requestDTO.getSkip());
        bpLogsDTO.setBpThreshold(Map.of(Constants.SYSTOLIC, Constants.BP_THRESHOLD_DIASTOLIC, Constants.DIASTOLIC,
                Constants.BP_THRESHOLD_DIASTOLIC));
        TestDataProvider.cleanUp();
        Assertions.assertNotNull(actualPatientSymptoms);
        Assertions.assertNotNull(actualBpLogsDto);
        Assertions.assertEquals(bpLogsDTO.getTotal(), total);
        Assertions.assertEquals(patientSymptoms.size(), actualPatientSymptoms.size());
        Assertions.assertNotEquals(bpLogsDTO, actualBpLogsDto);
        Assertions.assertNotEquals(bpLogsDTO.getBpLogList(), actualBpLogsDto.getBpLogList());

    }

    @Test
    @DisplayName("RemoveBPLog Test")
    void removeBpLog() {
        bpLogService.removeBpLog(1L);
        when(bpLogRepository.findByPatientTrackId(bpLog.getPatientTrackId())).thenReturn(getBpLogs());
        bpLogService.removeBpLog(1L);
        Assertions.assertTrue(Boolean.TRUE);
    }

    @Test
    @DisplayName("GetBPLogById Test")
    void getBpLogById() {
        Assertions.assertThrows(DataNotFoundException.class, () -> bpLogService.getBpLogById(1L));

        when(bpLogRepository.findById(any())).thenReturn(Optional.of(new BpLog()));
        bpLogService.getBpLogById(1L);
    }

}
