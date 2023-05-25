package com.mdtlabs.coreplatform.spiceservice.glucoselog.service;

import com.mdtlabs.coreplatform.common.Constants;
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
import com.mdtlabs.coreplatform.spiceservice.glucoselog.service.impl.GlucoseLogServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CultureService;
import com.mdtlabs.coreplatform.spiceservice.patientsymptom.service.PatientSymptomService;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GlucoseLogServiceImplTest {

    @InjectMocks
    private GlucoseLogServiceImpl glucoseLogService;

    @Mock
    private GlucoseLogRepository glucoseLogRepository;

    @Mock
    private PatientSymptomService patientSymptomService;

    @Mock
    private CultureService cultureService;
    @Mock
    private PatientTrackerService patientTrackerService;

    @Mock
    private PatientTreatmentPlanService patientTreatmentPlanService;

    @Mock
    private PatientAssessmentRepository patientAssessmentRepository;

    private GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();

    private GlucoseLog actualGlucoseLog;

    @Test
    void addGlucoseLog() {
        //given
        PatientTreatmentPlan patientTreatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        PatientAssessment patientAssessment = TestDataProvider.getPatientAssessment();
        glucoseLog.setGlucoseValue(100.0);
        glucoseLog.setGlucoseDateTime(new Date());
        glucoseLog.setGlucoseUnit("testUnit");
        glucoseLog.setGlucoseType("type1");
        glucoseLog.setLatest(Boolean.FALSE);
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(glucoseLogRepository.save(glucoseLog)).thenReturn(glucoseLog);
        when(patientTreatmentPlanService.getPatientTreatmentPlan(1L)).thenReturn(patientTreatmentPlan);
        when(patientTreatmentPlanService.getTreatmentPlanFollowupDate(patientTreatmentPlan.getBgCheckFrequency(),
                Constants.DEFAULT)).thenReturn(new Date());
        doNothing().when(patientTrackerService).updatePatientTrackerForGlucoseLog(1l, glucoseLog,
                new Date());
        when(patientAssessmentRepository.save(new PatientAssessment(null, 1l,
                Constants.MEDICAL_REVIEW, 1l, 1l))).thenReturn(patientAssessment);

        //then
        GlucoseLog result = glucoseLogService.addGlucoseLog(glucoseLog, Boolean.TRUE);
        Assertions.assertEquals(glucoseLog.getGlucoseType(), result.getGlucoseType());
        Assertions.assertNotNull(patientTreatmentPlan);
        Assertions.assertNotNull(glucoseLog);
        TestDataProvider.cleanUp();
    }

    @Test
    void BadRequestException() {
        //given
        GlucoseLog glucoseValue = new GlucoseLog();
        glucoseValue.setGlucoseValue(100.0);

        GlucoseLog glucoseType = new GlucoseLog();
        glucoseType.setGlucoseValue(100.0);
        glucoseType.setGlucoseType("type1");

        GlucoseLog glucoseUnit = new GlucoseLog();
        glucoseUnit.setGlucoseValue(100.0);
        glucoseUnit.setGlucoseType("type1");
        glucoseUnit.setGlucoseUnit("testUnit");

        //then
        Assertions.assertThrows(BadRequestException.class, () -> glucoseLogService.validateGlucoseLog(glucoseValue));
        Assertions.assertThrows(BadRequestException.class, () -> glucoseLogService.validateGlucoseLog(glucoseType));
        Assertions.assertThrows(BadRequestException.class, () -> glucoseLogService.validateGlucoseLog(glucoseUnit));
    }

    @Test
    @DisplayName("UpdateGlucoseLogLatestStatusWithInvalidId Test")
    void updateGlucoseLogLatestStatusWithInvalidId() {
        //given
        List<GlucoseLog> glucoseLogs = null;

        //when
        when(glucoseLogRepository.getGlucoseLogLatestStatus(glucoseLog.getPatientTrackId()))
                .thenReturn(glucoseLogs);

        //then
        glucoseLogService.updateGlucoseLogLatestStatus(glucoseLog);
        verify(glucoseLogRepository, atLeastOnce()).getGlucoseLogLatestStatus(glucoseLog.getPatientTrackId());
    }

    @Test
    @DisplayName("UpdateGlucoseLogLatestStatusWithValidId Test")
    void updateGlucoseLogLatestStatusWithValidId() {
        //when
        when(glucoseLogRepository.getGlucoseLogLatestStatus(glucoseLog.getPatientTrackId()))
                .thenReturn(List.of(glucoseLog));
        when(glucoseLogRepository.saveAll(List.of(glucoseLog))).thenReturn(List.of(glucoseLog));

        //then
        glucoseLogService.updateGlucoseLogLatestStatus(glucoseLog);
        verify(glucoseLogRepository, atLeastOnce()).saveAll(List.of(glucoseLog));
    }

    @Test
    @DisplayName("GetGlucoseLogByInvalidId")
    void getGlucoseLogByInvalidId() {
        //given
        Optional<GlucoseLog> optionalGlucoseLog = Optional.empty();

        //when
        when(glucoseLogRepository.findById(0l)).thenReturn(optionalGlucoseLog);

        //then
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            glucoseLogService.getGlucoseLogById(0l);
        });
    }

    @Test
    @DisplayName("GetGlucoseLogById Test")
    void getGlucoseLogById() {
        //when
        when(glucoseLogRepository.findById(glucoseLog.getGlucoseLogId())).thenReturn(Optional.ofNullable(glucoseLog));

        //then
        actualGlucoseLog = glucoseLogService.getGlucoseLogById(glucoseLog.getGlucoseLogId());
        Assertions.assertEquals(glucoseLog.getGlucoseLogId(), actualGlucoseLog.getGlucoseLogId());
        Assertions.assertEquals(glucoseLog, actualGlucoseLog);
        Assertions.assertNotEquals(0l,
                glucoseLogService.getGlucoseLogById(glucoseLog.getGlucoseLogId()).getGlucoseLogId());
    }

    @Test
    @DisplayName("GetByPatientTrackerByBgOnDescOder Test")
    void findFirstByPatientTrackerIdAndIsDeletedOrderByBgTakenOnDesc() {
        //given
        Long id = glucoseLog.getPatientTrackId();

        //when
        when(glucoseLogRepository.findFirstByPatientTrackIdAndIsDeletedOrderByBgTakenOnDesc(id, false))
                .thenReturn(glucoseLog);

        //then
        actualGlucoseLog = glucoseLogService.findFirstByPatientTrackIdAndIsDeletedOrderByBgTakenOnDesc(id,
                false);
        Assertions.assertEquals(glucoseLog, actualGlucoseLog);
        Assertions.assertEquals(glucoseLog.getPatientTrackId(), actualGlucoseLog.getPatientTrackId());
        Assertions.assertEquals(glucoseLog.getType(), glucoseLog.getType());
        Assertions.assertNotEquals(new GlucoseLog(), actualGlucoseLog);
    }

    @Test
    @DisplayName("GetLatestPatientTrackerGlucoseLog Test")
    void getGlucoseLogByPatientTrackerIdAndIsLatest() {
        //given
        Long id = glucoseLog.getPatientTrackId();

        //when
        when(glucoseLogRepository.findByPatientTrackIdAndIsDeletedAndIsLatest(id, false, true))
                .thenReturn(glucoseLog);

        //then
        actualGlucoseLog = glucoseLogService.getGlucoseLogByPatientTrackIdAndIsLatest(id, true);
        Assertions.assertEquals(glucoseLog, actualGlucoseLog);
        Assertions.assertEquals(glucoseLog.getPatientTrackId(), actualGlucoseLog.getPatientTrackId());
        Assertions.assertEquals(glucoseLog.getType(), glucoseLog.getType());
        Assertions.assertNotEquals(new GlucoseLog(), actualGlucoseLog);
    }

    @Test
    @DisplayName("GetGlucoseLogWithSymptomsByInvalidId Test")
    void getGlucoseLogWithSymptomsByInvalidId() {
        //given
        RequestDTO requestDTO = new RequestDTO();

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> glucoseLogService.getPatientGlucoseLogsWithSymptoms(requestDTO));
    }

    //@Test
    @DisplayName("GetGlucoseLogsWithSymptoms Test")
    void getGlucoseLogsWithSymptoms() {
        //given
        PatientGlucoseLogDTO glucoseLogDTO = TestDataProvider.getPatientGlucoseLogDTO();
        glucoseLogDTO.setGlucoseLogList(List.of(new GlucoseLogDTO()));
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        Pageable pageable = Pagination.setPagination(requestDTO.getSkip(), requestDTO.getLimit(),
                Constants.BG_TAKEN_ON, ((!StringUtils.isEmpty(requestDTO.getSortField())
                        && Constants.BG_TAKEN_ON.equalsIgnoreCase(requestDTO.getSortField())) ? true : false));
        List<GlucoseLog> glucoseLogs = List.of(glucoseLog);
        Page<GlucoseLog> glucoseLogPage = new PageImpl<>(glucoseLogs);

        //when
        when(glucoseLogRepository.getGlucoseLogs(requestDTO.getPatientTrackId(), pageable)).thenReturn(glucoseLogPage);
        //then

        Assertions.assertFalse(glucoseLogPage.isEmpty());
        PatientGlucoseLogDTO actualGlucoseLog = glucoseLogService.getPatientGlucoseLogsWithSymptoms(requestDTO);
        Assertions.assertEquals(glucoseLogDTO, actualGlucoseLog);

    }

    @Test
    @DisplayName("GetGlucoseLogByTrackIdWithNull Test")
    void getGlucoseLogByTrackIdWithNull() {
        //given
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, Constants.MINUS_ONE);
        Date yesterday = cal.getTime();

        //when
        when(glucoseLogRepository.findByPatientTrackIdAndIsCreatedToday(0l,
                yesterday)).thenReturn(null);

        //then
        actualGlucoseLog = glucoseLogService.getGlucoseLogByPatientTrackId(0l);
        Assertions.assertEquals(null, actualGlucoseLog);

        //when
        when(glucoseLogRepository.findByPatientTrackIdAndIsCreatedToday(2l,
                yesterday)).thenReturn(new ArrayList<>());

        //then
        actualGlucoseLog = glucoseLogService.getGlucoseLogByPatientTrackId(2l);
        Assertions.assertEquals(null, actualGlucoseLog);

        //given
        List<GlucoseLog> glucoseLogs = List.of(glucoseLog);
        when(glucoseLogRepository.findByPatientTrackIdAndIsCreatedToday(1l,
                yesterday)).thenReturn(glucoseLogs);

        //then
        actualGlucoseLog = glucoseLogService.getGlucoseLogByPatientTrackId(1L);
        Assertions.assertFalse(glucoseLogs.isEmpty());
    }

    @Test
    void getPatientGlucoseLogsWithSymptoms() {
        //given
        Constants.CULTURE_VALUES_MAP = Map.of(1l, Map.of("Symptoms", Map.of(1l, Constants.CULTURE_VALUE_SYMPTOMS)));
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setLatestRequired(Boolean.TRUE);
        requestDTO.setSortField(Constants.BG_TAKEN_ON);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Constants.BG_TAKEN_ON).descending());
        List<GlucoseLog> glucoseLogs = List.of(glucoseLog);
        Page<GlucoseLog> glucoseLogPage = new PageImpl<>(glucoseLogs);
        List<PatientSymptom> patientSymptoms = List.of(TestDataProvider.getPatientSymptom());
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(glucoseLogRepository.getGlucoseLogs(1l, pageable)).thenReturn(glucoseLogPage);
        when(glucoseLogRepository.findFirstByPatientTrackIdAndIsDeletedOrderByBgTakenOnDesc(1l,
                Boolean.FALSE)).thenReturn(glucoseLog);
        when(cultureService.loadCultureValues()).thenReturn(1L);
        when(glucoseLogRepository.totalGlucoseLogCount(1L)).thenReturn(1);
        when(patientSymptomService.getSymptomsByGlucoseLogId(1l, 1l))
                .thenReturn(patientSymptoms);

        //then
        PatientGlucoseLogDTO result = glucoseLogService.getPatientGlucoseLogsWithSymptoms(requestDTO);
        Assertions.assertEquals(1, result.getTotal());
        Assertions.assertEquals(1l, result.getLatestGlucoseLog().getId());
        Assertions.assertFalse(result.getGlucoseLogList().isEmpty());
        Assertions.assertFalse(result.getGlucoseThreshold().isEmpty());
        Assertions.assertNotNull(glucoseLogs);
        TestDataProvider.cleanUp();
    }

    @Test
    void testAddGlucoseLog() {
        //given
        actualGlucoseLog = new GlucoseLog();
        actualGlucoseLog.setLatest(Boolean.TRUE);
        actualGlucoseLog.setPatientTrackId(1L);
        actualGlucoseLog.setBgTakenOn(new Date());
        PatientAssessment patientAssessment = TestDataProvider.getPatientAssessment();

        //when
        when(glucoseLogRepository.getGlucoseLogLatestStatus(glucoseLog.getPatientTrackId()))
                .thenReturn(List.of(actualGlucoseLog));
        when(glucoseLogRepository.saveAll(List.of(actualGlucoseLog))).thenReturn(List.of(actualGlucoseLog));
        when(patientTreatmentPlanService.getPatientTreatmentPlan(1L)).thenReturn(null);
        doNothing().when(patientTrackerService).updatePatientTrackerForGlucoseLog(1l, actualGlucoseLog,
                new Date());
        when(patientAssessmentRepository.save(new PatientAssessment(null, 1l,
                Constants.MEDICAL_REVIEW, 1l, 1l))).thenReturn(patientAssessment);

        //then
        GlucoseLog result = glucoseLogService.addGlucoseLog(actualGlucoseLog, Boolean.TRUE);
        Assertions.assertNull(result);
    }
}
