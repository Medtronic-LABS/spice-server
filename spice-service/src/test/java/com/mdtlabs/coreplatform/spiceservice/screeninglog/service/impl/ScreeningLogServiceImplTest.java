package com.mdtlabs.coreplatform.spiceservice.screeninglog.service.impl;

import com.google.common.reflect.TypeToken;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserTenantsContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.BioMetricsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ScreeningLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ScreeningResponseDTO;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLogDetails;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientAssessment;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientDiagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.ScreeningLog;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.spiceservice.AdminApiInterface;
import com.mdtlabs.coreplatform.spiceservice.assessment.repository.PatientAssessmentRepository;
import com.mdtlabs.coreplatform.spiceservice.bplog.service.BpLogService;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.ScreeningMapper;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientDiagnosisRepository;
import com.mdtlabs.coreplatform.spiceservice.customizedmodules.service.CustomizedModulesService;
import com.mdtlabs.coreplatform.spiceservice.devicedetails.service.DeviceDetailsService;
import com.mdtlabs.coreplatform.spiceservice.glucoselog.service.GlucoseLogService;
import com.mdtlabs.coreplatform.spiceservice.mentalhealth.service.MentalHealthService;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.screeninglog.repository.ScreeningLogRepository;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.InheritingConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * <p>
 * ScreeningLogServiceImplTest class used to test all possible positive
 * and negative cases for all methods and conditions used in
 * ScreeningLogServiceImpl class.
 * </p>
 *
 * @author Nandhakumar Karthikeyan created on Feb 10, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ScreeningLogServiceImplTest {

    @InjectMocks
    ScreeningLogServiceImpl screeningLogServiceImpl;

    @Mock
    AdminApiInterface apiInterface;

    @Mock
    BpLogService bpLogService;

    @Mock
    CustomizedModulesService customizedModulesService;

    @Mock
    DeviceDetailsService deviceDetailsService;

    @Mock
    GlucoseLogService glucoseLogService;

    @Mock
    MentalHealthService mentalHealthService;

    @Mock
    PatientAssessmentRepository assessmentRepository;

    @Mock
    PatientTrackerService patientTrackerService;

    @Mock
    ScreeningLogRepository screeningLogRepository;

    @Mock
    ScreeningMapper screeningMapper;

    @Mock
    PatientDiagnosisRepository patientDiagnosisRepository;

    @Mock
    ModelMapper modelMapper;

    @Mock
    private PatientAssessmentRepository patientAssessmentRepository;

    @Test
    @DisplayName("addScreeningLog screeningLogDTO Data Null Test")
    void addScreeningLogNullTest() {
        ScreeningLogDTO screeningLogDTO = TestDataProvider.getScreeningLogDTO();
        screeningLogDTO.getBpLog().setBpLogDetails(null);
        Assertions.assertThrows(BadRequestException.class, () -> {
            screeningLogServiceImpl.createScreeningLog(screeningLogDTO);
        });
    }

    @Test
    @DisplayName("addScreeningLog BpLogDetails Null Test")
    void addScreeningLogNullTestNull() {
        ScreeningLogDTO screeningLogDTO = TestDataProvider.getScreeningLogDTO();
        screeningLogDTO.getBpLog().setBpLogDetails(null);
        Assertions.assertThrows(BadRequestException.class, () -> {
            screeningLogServiceImpl.createScreeningLog(screeningLogDTO);
        });
    }

    @Test
    @DisplayName("addScreeningLog screeningLogDTO Null Test")
    void addScreeningLogEmpty() {
        ScreeningLogDTO screeningLogDTO = TestDataProvider.getScreeningLogDTO();
        screeningLogDTO.getBpLog().setBpLogDetails(null);
        Assertions.assertThrows(BadRequestException.class, () -> {
            screeningLogServiceImpl.createScreeningLog(null);
        });
    }

    //@Test
    @DisplayName("addScreeningLog Test Positive Cases")
    void addScreeningLog() {
        // given
        ScreeningLogDTO screeningLogDTO = TestDataProvider.getScreeningLogDTO();
        ScreeningLog screeningLog = TestDataProvider.getScreeningLog();
        screeningLogDTO.getBpLog().setBpLogDetails(TestDataProvider.getBpLogDetails());
        Site site = TestDataProvider.getSiteObject();
        MockedStatic<CommonUtil> commonUtil = mockStatic(CommonUtil.class);
        MockedStatic<UserSelectedTenantContextHolder> userHolder = mockStatic(UserSelectedTenantContextHolder.class);
        ScreeningLog screeningLogWithId = TestDataProvider.getScreeningLog();
        ScreeningLog screeningLogWithOutId = new ScreeningLog();
        ScreeningLog newScreeningLog = new ScreeningLog();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        Map<String, Object> customizationModule = new HashMap<>();
        PatientAssessment assessment = TestDataProvider.getPatientAssessment();
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();

        // Test Cases Values
        screeningLogDTO.setCustomizedWorkflows(List.of(customizationModule));
        screeningLogDTO.setGlucoseLog(glucoseLog);
        screeningLogWithId.setId(1L);
        screeningLogWithOutId.setIsLatest(true);
        ResponseEntity<Site> siteDetails = new ResponseEntity<>(site, HttpStatus.OK);
        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);
        UserDTO userDTO = TestDataProvider.getUserDTO();
        userDTO.setId(1L);

        // when
        commonUtil.when(CommonUtil::getAuthToken).thenReturn("BearerTest");
        commonUtil.when(() -> CommonUtil.isGlucoseLogGiven(screeningLogDTO.getGlucoseLog())).thenReturn(true);
        userHolder.when(UserSelectedTenantContextHolder::get).thenReturn(1L);
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(apiInterface.getSiteById("BearerTest", 1l, 1L)).thenReturn(siteDetails);
        when(patientTrackerService.findByNationalIdIgnoreCaseAndCountryIdAndIsDeleted("123qwe456",
                1l, Constants.BOOLEAN_FALSE)).thenReturn(patientTracker);
        when(assessmentRepository.save(new PatientAssessment(1l,
                null, Constants.SCREENING, 4l, 1l))).thenReturn(assessment);
        customizedModulesService.createCustomizedModules(screeningLogDTO.getCustomizedWorkflows(),
                Constants.WORKFLOW_SCREENING, 1l);
        doNothing().when(screeningMapper).mapScreeningLog(screeningLogDTO, newScreeningLog, site);
        doNothing().when(screeningMapper).mapPatientTracker(patientTracker, screeningLogDTO, site);
        when(screeningLogRepository.save(screeningLogWithOutId)).thenReturn(screeningLogWithId);

        // then
        ScreeningLog response = screeningLogServiceImpl.createScreeningLog(screeningLogDTO);
        Assertions.assertNotNull(siteDetails.getBody());
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals(site.getId(), response.getSiteId());
        Assertions.assertEquals(site.getCountryId(), response.getCountryId());
        commonUtil.close();
        userContextHolder.close();
        userHolder.close();
    }

    @Test
    @DisplayName("addScreeningLog site Null Test")
    void addScreeningLogSiteNullTest() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        RequestDTO emptyRequest = new RequestDTO();

        // then
        Assertions.assertThrows(DataNotAcceptableException.class, () -> {
            screeningLogServiceImpl.getScreeningDetails(emptyRequest);
        });
        Assertions.assertThrows(DataNotAcceptableException.class, () -> {
            screeningLogServiceImpl.getScreeningDetails(requestDTO);
        });
    }

    @Test
    @DisplayName("addScreeningLog Test Positive Cases")
    void addScreeningLogNullBioData() {
        // given
        ScreeningLogDTO screeningLogDTO = TestDataProvider.getScreeningLogDTO();
        screeningLogDTO.setBioMetrics(null);
        ScreeningLog screeningLog = TestDataProvider.getScreeningLog();
        screeningLogDTO.getBpLog().setBpLogDetails(TestDataProvider.getBpLogDetails());
        Site site = TestDataProvider.getSiteObject();
        MockedStatic<CommonUtil> commonUtil = mockStatic(CommonUtil.class);
        MockedStatic<UserSelectedTenantContextHolder> userHolder = mockStatic(UserSelectedTenantContextHolder.class);
        ScreeningLog screeningLogWithId = TestDataProvider.getScreeningLog();
        ScreeningLog screeningLogWithOutId = new ScreeningLog();
        ScreeningLog newScreeningLog = new ScreeningLog();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        Map<String, Object> customizationModule = new HashMap<String, Object>();
        screeningLogDTO.setCustomizedWorkflows(List.of(customizationModule));
        screeningLogDTO.setGlucoseLog(null);
        screeningLogWithId.setId(1L);
        screeningLogWithOutId.setIsLatest(true);
        ResponseEntity<Site> siteDetails = new ResponseEntity<>(site, HttpStatus.OK);
        // when
        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);
        UserDTO userDTO = TestDataProvider.getUserDTO();
        userDTO.setId(1L);


        // when
        commonUtil.when(CommonUtil::getAuthToken).thenReturn("BearerTest");
        userHolder.when(UserSelectedTenantContextHolder::get).thenReturn(1L);
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(apiInterface.getSiteById("BearerTest", 1l, 1L)).thenReturn(siteDetails);
        when(patientTrackerService.findByNationalIdIgnoreCaseAndCountryIdAndIsDeleted("123qwe456",
                1l, Constants.BOOLEAN_FALSE)).thenReturn(patientTracker);
        doNothing().when(screeningMapper).mapScreeningLog(screeningLogDTO, newScreeningLog, site);
        doNothing().when(screeningMapper).mapPatientTracker(patientTracker, screeningLogDTO, site);
        doNothing().when(screeningMapper).mapPatientTracker(patientTracker, screeningLogDTO, site);
        screeningLogDTO.setBioMetrics(new BioMetricsDTO());
        screeningLogDTO.setGlucoseLog(TestDataProvider.getGlucoseLog());
        BpLog bpLog = TestDataProvider.getBpLog(80, 70, null, 0);
        commonUtil.when(() -> CommonUtil.isGlucoseLogGiven(screeningLogDTO.getGlucoseLog())).thenReturn(true);
        when(screeningLogRepository.save(any())).thenReturn(screeningLogWithId);
        when(bpLogService.addBpLog(any(), anyBoolean())).thenReturn(bpLog);

        //then
        ScreeningLog response = screeningLogServiceImpl.createScreeningLog(screeningLogDTO);
        Assertions.assertNotNull(siteDetails.getBody());
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertEquals(site.getId(), response.getSiteId());
        Assertions.assertEquals(site.getCountryId(), response.getCountryId());
        commonUtil.close();
        userHolder.close();
        userContextHolder.close();
    }

    @Test
    @DisplayName("getByIdAndIsLatest Test")
    void getByIdAndIsLatest() {
        // given
        ScreeningLog screeningLog = TestDataProvider.getScreeningLog();
        screeningLog.setIsLatest(true);

        // when
        when(screeningLogRepository.findByIdAndIsDeletedFalseAndIsLatestTrue(1L)).thenReturn(screeningLog);

        // then
        ScreeningLog response = screeningLogServiceImpl.getByIdAndIsLatest(1L);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(screeningLog.getId(), response.getId());
        Assertions.assertTrue(response.getIsLatest());
    }

    @Test
    @DisplayName("getByIdAndIsLatest Test PatientTrackId Null")
    void getScreeningDetailsPatientTrackIdNull() {
        // given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setPatientTrackId(null);

        // then
        Assertions.assertThrows(DataNotAcceptableException.class, () -> {
            screeningLogServiceImpl.getScreeningDetails(requestDTO);
        });
    }

    @Test
    @DisplayName("getByIdAndIsLatest Test PatientTrackId Null")
    void getScreeningDetailsScreeningIdNull() {
        // given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setScreeningId(null);

        // then
        Assertions.assertThrows(DataNotAcceptableException.class, () -> {
            screeningLogServiceImpl.getScreeningDetails(requestDTO);
        });
    }

    @Test
    @DisplayName("getByIdAndIsLatest Test")
    void getScreeningDetails() {
        // given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setScreeningId(1L);
        ScreeningLog screeningLog = TestDataProvider.getScreeningLog();
        screeningLog.setCreatedAt(new Date());
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        GlucoseLogDTO glucoseLogDTO = TestDataProvider.getGlucoseLogDTO();
        BpLog bpLog = TestDataProvider.getBpLog(80, 70, null, 0);
        ScreeningResponseDTO screeningLogResponseDTO = TestDataProvider.getScreeningResponseDTO();
        screeningLogResponseDTO.setGlucoseLog(glucoseLogDTO);
        bpLog.setHeight(10.15d);
        bpLog.setIsRegularSmoker(false);
        bpLog.setWeight(150.44d);
        PatientDiagnosis patientDiagnosis = TestDataProvider.getPatientDiagnosis();

        // Test Data
        List<BpLogDetails> bpLogDetails = new ArrayList<BpLogDetails>();
        BpLogDetails bpLogDetailsFirst = new BpLogDetails();
        bpLogDetailsFirst.setSystolic(80);
        bpLogDetailsFirst.setDiastolic(90);
        BpLogDetails bpLogDetailsSecond = new BpLogDetails();
        bpLogDetailsSecond.setSystolic(80);
        bpLogDetailsSecond.setDiastolic(90);
        bpLogDetails.add(bpLogDetailsSecond);
        bpLogDetails.add(bpLogDetailsFirst);
        bpLog.setBpLogDetails(bpLogDetails);
        bpLog.setId(1L);

        TestDataProvider.init();
        TestDataProvider.getStaticMock();
        // when
        when(screeningLogRepository.findByIdAndIsDeletedAndIsLatest(1l, false, true)).thenReturn(screeningLog);
        when(modelMapper.map(screeningLog, new TypeToken<ScreeningResponseDTO>() {
        }.getType())).thenReturn(screeningLogResponseDTO);
        when(modelMapper.map(glucoseLog, new TypeToken<GlucoseLogDTO>() {
        }.getType())).thenReturn(glucoseLogDTO);

        when(modelMapper.map(screeningLog, new TypeToken<ScreeningResponseDTO>() {
        }.getType())).thenReturn(screeningLogResponseDTO);
        when(modelMapper.map(screeningLog, ScreeningResponseDTO.class)).thenReturn(screeningLogResponseDTO);

        when(patientDiagnosisRepository.findByPatientTrackIdAndIsActiveAndIsDeleted(
                1l, Boolean.TRUE, Boolean.FALSE)).thenReturn(patientDiagnosis);
        when(glucoseLogService.getGlucoseLogByPatientTrackId(1L)).thenReturn(glucoseLog);
        when(bpLogService.getPatientTodayBpLog(1L)).thenReturn(bpLog);

        // then
        ScreeningResponseDTO response = screeningLogServiceImpl.getScreeningDetails(requestDTO);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(bpLog.getHeight(), response.getHeight());
        Assertions.assertEquals(bpLog.getWeight(), response.getWeight());
        Assertions.assertEquals(bpLog.getBpLogDetails().size(), response.getBpLogDetails().size());
        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("getScreeningDetailsNullGlucose Test")
    void getScreeningDetailsNullGlucose() {
        // given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setScreeningId(1L);
        ScreeningLog screeningLog = TestDataProvider.getScreeningLog();
        screeningLog.setCreatedAt(new Date());
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        GlucoseLogDTO glucoseLogDTO = TestDataProvider.getGlucoseLogDTO();
        ScreeningResponseDTO screeningLogResponseDTO = TestDataProvider.getScreeningResponseDTO();
        PatientDiagnosis patientDiagnosis = TestDataProvider.getPatientDiagnosis();
        TestDataProvider.init();
        TestDataProvider.getStaticMock();
        // when
        when(screeningLogRepository.findByIdAndIsDeletedAndIsLatest(1l, false, true)).thenReturn(screeningLog);
        when(modelMapper.map(screeningLog, ScreeningResponseDTO.class)).thenReturn(screeningLogResponseDTO);
        when(modelMapper.map(screeningLog, new TypeToken<ScreeningResponseDTO>() {
        }.getType())).thenReturn(screeningLogResponseDTO);
        when(patientDiagnosisRepository.findByPatientTrackIdAndIsActiveAndIsDeleted(
                1l, Boolean.TRUE, Boolean.FALSE)).thenReturn(patientDiagnosis);
        when(modelMapper.map(glucoseLog, new TypeToken<GlucoseLogDTO>() {
        }.getType())).thenReturn(glucoseLogDTO);
        when(glucoseLogService.getGlucoseLogByPatientTrackId(1L)).thenReturn(null);
        when(bpLogService.getPatientTodayBpLog(1L)).thenReturn(null);

        // then
        ScreeningResponseDTO response = screeningLogServiceImpl.getScreeningDetails(requestDTO);
        Assertions.assertNotNull(response);
        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("getScreeningDetailsNullGlucose Test")
    void getScreeningDetailsTest() {
        // given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setScreeningId(1L);
        ScreeningLog screeningLog = TestDataProvider.getScreeningLog();
        screeningLog.setCreatedAt(new Date());
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        GlucoseLogDTO glucoseLogDTO = TestDataProvider.getGlucoseLogDTO();
        ScreeningResponseDTO screeningLogResponseDTO = TestDataProvider.getScreeningResponseDTO();
        PatientDiagnosis patientDiagnosis = TestDataProvider.getPatientDiagnosis();

        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);
        MockedStatic<UserSelectedTenantContextHolder> userSelectedTenantContextHolder = mockStatic(UserSelectedTenantContextHolder.class);
        MockedStatic<CommonUtil> commonUtil = mockStatic(CommonUtil.class);
        MockedStatic<UserTenantsContextHolder> userTenantsContext = mockStatic(UserTenantsContextHolder.class);
        UserDTO userDTO = TestDataProvider.getUserDTO();
        userDTO.getCountry().setUnitMeasurement("imperial");
        // when
        when(screeningLogRepository.findByIdAndIsDeletedAndIsLatest(1l, false, true)).thenReturn(screeningLog);
        when(modelMapper.map(screeningLog, ScreeningResponseDTO.class)).thenReturn(screeningLogResponseDTO);
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);

        when(modelMapper.map(screeningLog, new TypeToken<ScreeningResponseDTO>() {
        }.getType())).thenReturn(screeningLogResponseDTO);
        when(patientDiagnosisRepository.findByPatientTrackIdAndIsActiveAndIsDeleted(
                1l, Boolean.TRUE, Boolean.FALSE)).thenReturn(patientDiagnosis);
        when(modelMapper.map(glucoseLog, new TypeToken<GlucoseLogDTO>() {
        }.getType())).thenReturn(glucoseLogDTO);
        when(glucoseLogService.getGlucoseLogByPatientTrackId(1L)).thenReturn(null);
        when(bpLogService.getPatientTodayBpLog(1L)).thenReturn(null);

        // then
        ScreeningResponseDTO response = screeningLogServiceImpl.getScreeningDetails(requestDTO);
        Assertions.assertNotNull(response);
        userSelectedTenantContextHolder.close();
        commonUtil.close();
        userContextHolder.close();
        userTenantsContext.close();
    }
}