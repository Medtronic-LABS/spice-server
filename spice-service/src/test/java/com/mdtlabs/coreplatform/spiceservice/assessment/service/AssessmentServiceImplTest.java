package com.mdtlabs.coreplatform.spiceservice.assessment.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.UnitConstants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.SmsDTO;
import com.mdtlabs.coreplatform.common.model.dto.fhir.FhirAssessmentRequestDto;
import com.mdtlabs.coreplatform.common.model.dto.fhir.FhirSiteRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AssessmentDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AssessmentResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ComplianceDTO;
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
import com.mdtlabs.coreplatform.common.util.spice.SpiceUtil;
import com.mdtlabs.coreplatform.spiceservice.AdminApiInterface;
import com.mdtlabs.coreplatform.spiceservice.NotificationApiInterface;
import com.mdtlabs.coreplatform.spiceservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceservice.assessment.repository.PatientAssessmentRepository;
import com.mdtlabs.coreplatform.spiceservice.assessment.service.impl.AssessmentServiceImpl;
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
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * <p>
 * AssessmentServiceImplTest class used to test all possible positive
 * and negative cases for all methods and conditions used in AssessmentServiceImpl class.
 * </p>
 *
 * @author Jaganathan created on Feb 8, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AssessmentServiceImplTest {

    @InjectMocks()
    private AssessmentServiceImpl assessmentService;

    @Mock
    private AdminApiInterface adminApiInterface;

    @Mock
    private RiskAlgorithm riskAlgorithm;

    @Mock
    private PatientAssessmentRepository assessmentRepository;

    @Mock
    private PatientTrackerService patientTrackerService;

    @Mock
    private GlucoseLogService glucoseLogService;

    @Mock
    private BpLogService bpLogService;

    @Mock
    private PatientSymptomService patientSymptomService;

    @Mock
    private PatientMedicalComplianceService patientMedicalComplianceService;

    @Mock
    private NotificationApiInterface notificationApiInterface;

    @Mock
    private RedRiskNotificationRepository notificationRepository;

    @Mock
    private UserApiInterface userApiInterface;

    @Mock
    private ModelMapper mapper;

    @Mock
    private MentalHealthMapper mentalHealthMapper;
    @Mock
    private PatientTrackerMapper patientTrackerMapper;
    @Mock
    private MentalHealthService mentalHealthService;

    @Mock
    private PatientTreatmentPlanService treatmentPlanService;

    @Mock
    private CustomizedModulesService customizedModulesService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private AssessmentDTO assessmentDTO = TestDataProvider.getAssessmentDto();

    private PatientTracker patientTracker = TestDataProvider.getPatientTracker();

    @Test
    @DisplayName("AssessmentDTO NullTest")
    void assessmentDTONullCheck() {
        assertThrows(BadRequestException.class, () -> assessmentService.createAssessment(null));
    }

    @Test
    @DisplayName("AssessmentNotNull Test")
    void testAssessmentNotNull() {
        //given
        AssessmentDTO assessment = new AssessmentDTO();
        PatientTracker tracker = new PatientTracker();

        Assertions.assertNotNull(assessmentDTO);
        Assertions.assertEquals(UnitConstants.METRIC, assessmentDTO.getUnitMeasurement());
        Assertions.assertNotNull(assessmentDTO.getPatientTrackId());
        when(patientTrackerService.getPatientTrackerById(assessmentDTO.getPatientTrackId())).thenReturn(patientTracker);
        PatientTracker response = patientTrackerService.getPatientTrackerById(assessmentDTO.getPatientTrackId());
        Assertions.assertEquals(response.getPatientId(), patientTracker.getPatientId());
        Assertions.assertEquals(response.getId(), patientTracker.getId());
        Assertions.assertNull(assessment.getPatientTrackId());
        Assertions.assertNull(assessment.getBpLog());
        //given
        Assertions.assertNotNull(tracker);
        tracker.setCvdRiskLevel(assessmentDTO.getCvdRiskLevel());
        tracker.setCvdRiskScore(assessmentDTO.getCvdRiskScore());
        Assertions.assertNull(assessment.getGlucoseLog());
        Assertions.assertNull(assessment.getPhq4());
        when(patientTrackerService.addOrUpdatePatientTracker(tracker)).thenReturn(patientTracker);
        PatientTracker trackerResponse = patientTrackerService.addOrUpdatePatientTracker(tracker);
        Assertions.assertEquals(tracker.getCvdRiskLevel(), patientTracker.getCvdRiskLevel());
        Assertions.assertEquals(tracker.getCvdRiskScore(), trackerResponse.getCvdRiskScore());
    }

    @Test
    @DisplayName("CreateRedRiskNotification Test")
    void createRedRiskNotification() {
        RedRiskNotification notification = TestDataProvider.getRedRiskNotification();
        when(notificationRepository.save(notification)).thenReturn(notification);
        RedRiskNotification redRiskResponse = assessmentService.createRedRiskNotification(patientTracker,
                notification.getBpLogId(), notification.getGlucoseLogId(), notification.getAssessmentLogId());
        Assertions.assertEquals(notification, redRiskResponse);
        Assertions.assertEquals(notification.getStatus(), redRiskResponse.getStatus());
    }

    @Test
    @DisplayName("CreatePatientCompliances Test")
    void createCompliances() {
        List<ComplianceDTO> complianceDTOS = List.of(TestDataProvider.getComplianceDTO());
        List<PatientMedicalCompliance> patientMedicalCompliances = new ArrayList<>();
        when(patientMedicalComplianceService.addPatientMedicalCompliance(patientMedicalCompliances))
                .thenReturn(patientMedicalCompliances);
        List<PatientMedicalCompliance> medicalCompliances = patientMedicalComplianceService
                .addPatientMedicalCompliance(patientMedicalCompliances);
        List<PatientMedicalCompliance> response = assessmentService.createPatientComplianceList(complianceDTOS,
                1l, 1l, 1l);
        Assertions.assertEquals(medicalCompliances.size(), patientMedicalCompliances.size());
        Assertions.assertEquals(response, medicalCompliances);
    }

    @Test
    @DisplayName("ConstructGlucoseLog Test")
    void constructGlucoseLog() {
        Assertions.assertNotNull(assessmentDTO);
        GlucoseLog glucoseLog = assessmentDTO.getGlucoseLog();
        GlucoseLog glucoseLogResponse = assessmentService.constructGlucoseLog(assessmentDTO);
        Assertions.assertEquals(glucoseLog, glucoseLogResponse);
    }

    @Test
    @DisplayName("ConstructBpLog Test")
    void constructBpLog() {
        Assertions.assertNotNull(assessmentDTO);
        BpLog bpLog = assessmentDTO.getBpLog();
        bpLog.setUnitMeasurement(UnitConstants.METRIC);
        Assertions.assertNotNull(assessmentDTO.getUnitMeasurement());
        BpLog response = assessmentService.constructBpLog(assessmentDTO, assessmentDTO.getCvdRiskLevel());
        Assertions.assertEquals(bpLog, response);
        Assertions.assertEquals(UnitConstants.METRIC, assessmentDTO.getUnitMeasurement());
        Assertions.assertEquals(bpLog.getCvdRiskLevel(), assessmentDTO.getCvdRiskLevel());
        assessmentDTO.setUnitMeasurement(null);
        Assertions.assertNull(assessmentDTO.getUnitMeasurement());
        assessmentDTO.setUnitMeasurement(UnitConstants.METRIC);
        Assertions.assertEquals(bpLog.getUnitMeasurement(), assessmentDTO.getUnitMeasurement());
        Assertions.assertNotNull(bpLog.getBpTakenOn());
        bpLog.setBpTakenOn(null);
        Assertions.assertNull(bpLog.getBpTakenOn());
        bpLog.setBpTakenOn(new Date());
    }

   @Test
   @DisplayName("CalculateRiskLevel Test")
   void calculateRiskLevel() {
       String response = "RedRisk in Assessment";
       patientTracker.setInitialReview(Constants.BOOLEAN_TRUE);
       RiskAlgorithmDTO algorithmDTO = TestDataProvider.getRiskAlgorithmDto();
       algorithmDTO.setPatientTrackId(assessmentDTO.getPatientTrackId());
       Assertions.assertNotNull(assessmentDTO.getGlucoseLog());
       algorithmDTO.setGlucoseType(assessmentDTO.getGlucoseLog().getGlucoseType());
       algorithmDTO.setGlucoseValue(assessmentDTO.getGlucoseLog().getGlucoseValue());
       Assertions.assertNotNull(assessmentDTO.getBpLog());
       algorithmDTO.setAvgDiastolic(assessmentDTO.getBpLog().getAvgDiastolic());
       algorithmDTO.setAvgSystolic(assessmentDTO.getBpLog().getAvgSystolic());
       algorithmDTO.setIsPregnant(patientTracker.getIsPregnant());
       algorithmDTO.setRiskLevel(patientTracker.getRiskLevel());
       Assertions.assertNotNull(assessmentDTO.getSymptoms());
       algorithmDTO.setSymptoms(assessmentDTO.getSymptoms()
               .stream().map(SymptomDTO::getSymptomId).collect(Collectors.toSet()));
       when(riskAlgorithm.getRiskLevelInAssessmentDbm(algorithmDTO)).thenReturn(response);
       String riskMessage = assessmentService.calculateRiskLevel(assessmentDTO.getBpLog(), assessmentDTO.getGlucoseLog(), assessmentDTO.getSymptoms(), patientTracker);
       Assertions.assertNotNull(riskMessage);
   }

    @Test
    @DisplayName("CalculateRiskLevelWithNull Test")
    void calculateRiskLevelWithNullParam() {
        AssessmentDTO assessment = new AssessmentDTO();
        RiskAlgorithmDTO algorithmDTO = new RiskAlgorithmDTO();
        Assertions.assertNull(assessment.getGlucoseLog());
        Assertions.assertNotNull(patientTracker);
        algorithmDTO.setPatientTrackId(assessment.getPatientTrackId());
        when(riskAlgorithm.getRiskLevelInAssessmentDbm(algorithmDTO)).thenReturn(null);
        String response = riskAlgorithm.getRiskLevelInAssessmentDbm(algorithmDTO);
        Assertions.assertEquals(null, response);
    }

    @Test
    @DisplayName("CreateAssessmentGlucoseLogWithNull Test")
    void createAssessmentGlucoseLogWithNull() {
        //given
        GlucoseLog glucoseLog = null;
        //when
        Assertions.assertThrows(DataNotFoundException.class,
                () -> assessmentService.createAssessmentGlucoseLog(glucoseLog));
    }

    @Test
    @DisplayName("CreateAssessmentGlucoseLog Test")
    void createAssessmentGlucoseLogTest() {

        //given
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        glucoseLog.setType(Constants.ASSESSMENT);
        GlucoseLog exisitingGlucoseLog = glucoseLog;
        PatientAssessment assessment = TestDataProvider.getPatientAssessment();
        //when
        when(glucoseLogService.findFirstByPatientTrackIdAndIsDeletedOrderByBgTakenOnDesc(glucoseLog.getPatientTrackId(),
                Constants.BOOLEAN_FALSE)).thenReturn(exisitingGlucoseLog);
        when(patientTrackerService.getPatientTrackerById(glucoseLog.getPatientTrackId())).thenReturn(patientTracker);
        when(glucoseLogService.addGlucoseLog(glucoseLog, Constants.BOOLEAN_TRUE)).thenReturn(glucoseLog);
        //then
        GlucoseLog actualGlucoseLog = assessmentService.createAssessmentGlucoseLog(glucoseLog);
        Assertions.assertEquals(glucoseLog, actualGlucoseLog);
        Assertions.assertEquals(glucoseLog.getGlucoseLogId(), actualGlucoseLog.getGlucoseLogId());
        //given
        glucoseLog.setBgTakenOn(new Date(28 / 02 / 23));
        exisitingGlucoseLog.setBgTakenOn(new Date());
        glucoseLog.setOldRecord(Constants.BOOLEAN_TRUE);
        //when
        when(glucoseLogService.findFirstByPatientTrackIdAndIsDeletedOrderByBgTakenOnDesc(glucoseLog.getPatientTrackId(),
                Constants.BOOLEAN_FALSE)).thenReturn(exisitingGlucoseLog);
        when(assessmentRepository.save(assessment)).thenReturn(assessment);
        when(glucoseLogService.addGlucoseLog(glucoseLog, Constants.BOOLEAN_TRUE)).thenReturn(glucoseLog);
        when(glucoseLogService.addGlucoseLog(glucoseLog, Constants.BOOLEAN_FALSE)).thenReturn(glucoseLog);
        //then
        actualGlucoseLog = assessmentService.createAssessmentGlucoseLog(glucoseLog);
        Assertions.assertEquals(glucoseLog, actualGlucoseLog);
        Assertions.assertEquals(glucoseLog.getGlucoseLogId(), actualGlucoseLog.getGlucoseLogId());
    }

    @Test
    @DisplayName("AddRedRiskNotification Test")
    void addRedRiskNotification() {
        //given
        List<SmsDTO> smsDTOS = List.of(TestDataProvider.getSmsDTO());
        List<UserResponseDTO> users = List.of(TestDataProvider.getUserResponseDTO());
        Long bpLogId = 1l;
        Long assessmentId = 1l;
        Long glucoseId = 1l;
        ResponseEntity<List<UserResponseDTO>> responseEntity = new ResponseEntity<>(users, HttpStatus.OK);
        TestDataProvider.init();
        patientTracker.setRedRiskPatient(Constants.BOOLEAN_TRUE);
        RedRiskNotification redRiskNotification = TestDataProvider.getRedRiskNotification();
        //when
        when(notificationRepository.save(redRiskNotification)).thenReturn(redRiskNotification);
        TestDataProvider.getStaticMock();
        when(userApiInterface.getUsersByRoleName(Constants.AUTH_TOKEN_SUBJECT,
                1l, Constants.ROLE_RED_RISK_USER)).thenReturn(responseEntity);
        when(notificationApiInterface.saveOutBoundSms(Constants.AUTH_TOKEN_SUBJECT,
                1l, smsDTOS)).thenReturn("");
        //then
        assessmentService.addRedRiskNotification(patientTracker, bpLogId, glucoseId, assessmentId);
        verify(notificationRepository).save(redRiskNotification);
        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("CreatePatientSymptoms Test")
    void createPatientSymptoms() {
        //given
        List<PatientSymptom> patientSymptomList = List.of(TestDataProvider.getPatientSymptom());
        SymptomDTO symptomDTO = TestDataProvider.getSymptomDTO();
        List<SymptomDTO> symptomDTOS = List.of(symptomDTO);
        Long bpLogId = 1l;
        Long glucoseLogId = 1l;
        Long assessmentId = 1l;
        Long patientTrackId = 1l;
        //when
        when(patientSymptomService.addPatientSymptoms(patientSymptomList)).thenReturn(patientSymptomList);
        //then
        List<PatientSymptom> actualList = assessmentService.createPatientSymptoms(symptomDTOS,
                bpLogId, glucoseLogId, patientTrackId, assessmentId);
        Assertions.assertEquals(patientSymptomList, actualList);
        //given
        symptomDTO.setType(Constants.HYPERTENSION);
        patientSymptomList.get(0).setType(Constants.HYPERTENSION);
        patientSymptomList.get(0).setBpLogId(1L);
        patientSymptomList.get(0).setGlucoseLogId(null);
        //when
        when(patientSymptomService.addPatientSymptoms(patientSymptomList)).thenReturn(patientSymptomList);
        //then
        actualList = assessmentService.createPatientSymptoms(symptomDTOS,
                bpLogId, glucoseLogId, patientTrackId, assessmentId);
        Assertions.assertEquals(patientSymptomList, actualList);
        //given
        symptomDTO.setType(Constants.OTHER);
        patientSymptomList.get(0).setType(Constants.OTHER);
        patientSymptomList.get(0).setBpLogId(null);
        patientSymptomList.get(0).setGlucoseLogId(null);
        //when
        when(patientSymptomService.addPatientSymptoms(patientSymptomList)).thenReturn(patientSymptomList);
        //then
        actualList = assessmentService.createPatientSymptoms(symptomDTOS,
                bpLogId, glucoseLogId, patientTrackId, assessmentId);
        Assertions.assertEquals(patientSymptomList, actualList);
    }

    @Test
    @DisplayName("CreateAssessmentBpLogNullCheck Test")
    void createAssessmentBpLogTest() {
        //given
        BpLog bpLog = null;
        //when
        Assertions.assertThrows(BadRequestException.class,
                () -> assessmentService.createAssessmentBpLog(bpLog));
    }

    @Test
    @DisplayName("CreateAssessmentWithBpLogTrackIdNull Test")
    void createAssessmentWithBpLogTrackIdNull() {
        //given
        BpLog bpLog = TestDataProvider.getBpLog();
        bpLog.setPatientTrackId(null);
        //when
        Assertions.assertThrows(DataNotFoundException.class,
                () -> assessmentService.createAssessmentBpLog(bpLog));
    }

    @ParameterizedTest
    @DisplayName("CreateAssessmentWithBpLogVales Test")
    @CsvSource({",  ,  ", ",  , false", ",  , true", "1,  ,  ", ", 1, ", "1,  , true", ", 1, true", "1, 1, "})
    void createAssessmentWithBpLogVales(Integer avgDiastolic, Integer avgSystolic, Boolean isRegularSomker) {
        //given
        BpLog bpLog = new BpLog();
        bpLog.setAvgDiastolic(avgDiastolic);
        bpLog.setAvgSystolic(avgSystolic);
        bpLog.setIsRegularSmoker(isRegularSomker);
        //when
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> assessmentService.createAssessmentBpLog(bpLog));
    }

    @Test
    @DisplayName("CreateAssessmentBpLog Test")
    void createAssessmentBpLog() {
        //given
        BpLog bpLog = TestDataProvider.getBpLog();
        patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setInitialReview(Constants.BOOLEAN_TRUE);
        patientTracker.setRedRiskPatient(Constants.BOOLEAN_FALSE);
        //when
        when(bpLogService.findFirstByPatientTrackIdAndIsDeletedOrderByBpTakenOnDesc(bpLog.getPatientTrackId(),
                Constants.BOOLEAN_FALSE)).thenReturn(bpLog);
        when(patientTrackerService.getPatientTrackerById(bpLog.getPatientTrackId())).thenReturn(patientTracker);
        when(bpLogService.addBpLog(bpLog, Constants.BOOLEAN_TRUE)).thenReturn(bpLog);
        //then
        BpLog actualBplog = assessmentService.createAssessmentBpLog(bpLog);
        Assertions.assertEquals(bpLog, actualBplog);
        Assertions.assertEquals(bpLog.getPatientTrackId(), actualBplog.getPatientTrackId());
        //given
        BpLog existingBpLog = null;
        patientTracker.setPatientStatus(Constants.ASSESSMENT);
        patientTracker.setInitialReview(Constants.BOOLEAN_FALSE);
        patientTracker.setRedRiskPatient(Constants.BOOLEAN_FALSE);
        //when
        when(bpLogService.findFirstByPatientTrackIdAndIsDeletedOrderByBpTakenOnDesc(bpLog.getPatientTrackId(),
                Constants.BOOLEAN_FALSE)).thenReturn(existingBpLog);
        when(patientTrackerService.getPatientTrackerById(bpLog.getPatientTrackId())).thenReturn(patientTracker);
        when(bpLogService.addBpLog(bpLog, Constants.BOOLEAN_TRUE)).thenReturn(bpLog);
        //then
        actualBplog = assessmentService.createAssessmentBpLog(bpLog);
        Assertions.assertEquals(bpLog, actualBplog);
    }

    @Test
    @DisplayName("CreateAssessment Test")
    void createAssessment() {
        //given
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        BpLog bpLog = TestDataProvider.getBpLog();
        bpLog.setId(1L);
        PatientTreatmentPlan treatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        bpLog.setCvdRiskLevel(Constants.CVD_RISK_MEDIUM);
        bpLog.setCvdRiskScore(Constants.TWO);
        bpLog.setLatest(Constants.BOOLEAN_TRUE);
        bpLog.setIsRegularSmoker(Constants.BOOLEAN_FALSE);
        bpLog.setType(Constants.ASSESSMENT);
        bpLog.setRiskLevel(Constants.EMPTY);
        bpLog.setUnitMeasurement(UnitConstants.IMPERIAL);
        bpLog.setBpTakenOn(new Date());
        patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setRiskLevel(Constants.HIGH);
        PatientAssessment assessment = TestDataProvider.getPatientAssessment();
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();
        AssessmentResponseDTO responseDTO = TestDataProvider.getAssessmentResponseDto();
        assessmentDTO.setUnitMeasurement(UnitConstants.IMPERIAL);
        assessmentDTO.setBpLog(bpLog);
        assessmentDTO.setGlucoseLog(glucoseLog);
        assessmentDTO.setPhq4(mentalHealth);
        Site site = TestDataProvider.getSite();
        TestDataProvider.init();
        MockedStatic<SpiceUtil> spiceUtil = mockStatic(SpiceUtil.class);
        ResponseEntity<Site> siteResponse = new ResponseEntity<>(site, HttpStatus.OK);
        PatientAssessment patientAssessment = new PatientAssessment(1l, null,
                Constants.ASSESSMENT, 4L,
                1L);
        //when
        spiceUtil.when(() -> SpiceUtil.convertBpLogUnits(assessmentDTO.getBpLog(), assessmentDTO.getUnitMeasurement()))
                .thenReturn(bpLog);
        when(patientTrackerService.getPatientTrackerById(assessmentDTO.getPatientTrackId())).thenReturn(patientTracker);
        spiceUtil.when(() -> SpiceUtil.convertBpLogUnits(bpLog, UnitConstants.METRIC)).thenReturn(bpLog);
        BpLog constructBpLog = assessmentService.constructBpLog(assessmentDTO, Constants.CVD_RISK_MEDIUM);
        when(glucoseLogService.addGlucoseLog(glucoseLog, Constants.BOOLEAN_FALSE)).thenReturn(glucoseLog);
        when(bpLogService.addBpLog(constructBpLog, Constants.BOOLEAN_FALSE)).thenReturn(constructBpLog);
        when(assessmentRepository.save(new PatientAssessment(1l, 1l,
                Constants.ASSESSMENT, assessmentDTO.getTenantId(),
                assessmentDTO.getPatientTrackId()))).thenReturn(assessment);
        when(mapper.map(assessmentDTO.getPhq4(), new TypeToken<MentalHealth>() {
        }.getType())).thenReturn(mentalHealth);
        doNothing().when(mentalHealthService).setPhq4Score(mentalHealth);
        when(mentalHealthService.createMentalHealth(mentalHealth, patientTracker,
                Constants.BOOLEAN_TRUE)).thenReturn(mentalHealth);
        when(mentalHealthMapper.setPatientTracker(patientTracker, mentalHealth)).thenReturn(patientTracker);
        doNothing().when(patientTrackerMapper).setPatientTrackerFromBpLog(patientTracker, bpLog);
        TestDataProvider.getStaticMock();
        when(adminApiInterface.getSiteById(Constants.AUTH_TOKEN_SUBJECT, 1l, 1l))
                .thenReturn(siteResponse);
        glucoseLog = assessmentService.constructGlucoseLog(assessmentDTO);
        when(treatmentPlanService.getPatientTreatmentPlan(assessmentDTO.getPatientTrackId())).thenReturn(treatmentPlan);

        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        when(assessmentRepository.save(patientAssessment)).thenReturn(patientAssessment);
        //then
        AssessmentResponseDTO response = assessmentService.createAssessment(assessmentDTO);
        spiceUtil.close();
        TestDataProvider.cleanUp();
        Assertions.assertEquals(null, response.getPatientDetails().getId());
    }

    @Test
    @DisplayName("CreateAssessmentWithFhir Test")
    void createAssessmentWithEnableFhir() {
        //given
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        BpLog bpLog = TestDataProvider.getBpLog();
        bpLog.setId(1L);
        PatientTreatmentPlan treatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        bpLog.setCvdRiskLevel(Constants.CVD_RISK_MEDIUM);
        bpLog.setCvdRiskScore(Constants.TWO);
        bpLog.setLatest(Constants.BOOLEAN_TRUE);
        bpLog.setIsRegularSmoker(Constants.BOOLEAN_FALSE);
        bpLog.setType(Constants.ASSESSMENT);
        bpLog.setRiskLevel(Constants.EMPTY);
        bpLog.setUnitMeasurement(UnitConstants.IMPERIAL);
        bpLog.setBpTakenOn(new Date());
        patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setRiskLevel(Constants.HIGH);
        PatientAssessment assessment = TestDataProvider.getPatientAssessment();
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();
        AssessmentResponseDTO responseDTO = TestDataProvider.getAssessmentResponseDto();
        assessmentDTO.setUnitMeasurement(UnitConstants.IMPERIAL);
        assessmentDTO.setBpLog(bpLog);
        assessmentDTO.setGlucoseLog(glucoseLog);
        assessmentDTO.setPhq4(mentalHealth);
        Site site = TestDataProvider.getSite();
        TestDataProvider.init();
        MockedStatic<SpiceUtil> spiceUtil = mockStatic(SpiceUtil.class);
        ResponseEntity<Site> siteResponse = new ResponseEntity<>(site, HttpStatus.OK);
        PatientAssessment patientAssessment = new PatientAssessment(1l, null,
                Constants.ASSESSMENT, 4L,
                1L);
        ReflectionTestUtils.setField(assessmentService, "enableFhir", true);
        ReflectionTestUtils.setField(assessmentService, "routingKey", "routingKey");
        ReflectionTestUtils.setField(assessmentService, "exchange", "exchangeName");
        //when
        spiceUtil.when(() -> SpiceUtil.convertBpLogUnits(assessmentDTO.getBpLog(), assessmentDTO.getUnitMeasurement()))
                .thenReturn(bpLog);
        when(patientTrackerService.getPatientTrackerById(assessmentDTO.getPatientTrackId())).thenReturn(patientTracker);
        spiceUtil.when(() -> SpiceUtil.convertBpLogUnits(bpLog, UnitConstants.METRIC)).thenReturn(bpLog);
        BpLog constructBpLog = assessmentService.constructBpLog(assessmentDTO, Constants.CVD_RISK_MEDIUM);
        when(glucoseLogService.addGlucoseLog(glucoseLog, Constants.BOOLEAN_FALSE)).thenReturn(glucoseLog);
        when(bpLogService.addBpLog(constructBpLog, Constants.BOOLEAN_FALSE)).thenReturn(constructBpLog);
        when(assessmentRepository.save(new PatientAssessment(1l, 1l,
                Constants.ASSESSMENT, assessmentDTO.getTenantId(),
                assessmentDTO.getPatientTrackId()))).thenReturn(assessment);
        when(mapper.map(assessmentDTO.getPhq4(), new TypeToken<MentalHealth>() {
        }.getType())).thenReturn(mentalHealth);
        doNothing().when(mentalHealthService).setPhq4Score(mentalHealth);
        when(mentalHealthService.createMentalHealth(mentalHealth, patientTracker,
                Constants.BOOLEAN_TRUE)).thenReturn(mentalHealth);
        when(mentalHealthMapper.setPatientTracker(patientTracker, mentalHealth)).thenReturn(patientTracker);
        doNothing().when(patientTrackerMapper).setPatientTrackerFromBpLog(patientTracker, bpLog);
        TestDataProvider.getStaticMock();
        when(adminApiInterface.getSiteById(Constants.AUTH_TOKEN_SUBJECT, 1l, 1l))
                .thenReturn(siteResponse);
        glucoseLog = assessmentService.constructGlucoseLog(assessmentDTO);
        when(treatmentPlanService.getPatientTreatmentPlan(assessmentDTO.getPatientTrackId())).thenReturn(treatmentPlan);

        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        when(assessmentRepository.save(patientAssessment)).thenReturn(patientAssessment);
        doNothing().when(rabbitTemplate).convertAndSend("exchangeName", "routingKey", "");
        //then
        AssessmentResponseDTO response = assessmentService.createAssessment(assessmentDTO);
        spiceUtil.close();
        TestDataProvider.cleanUp();
        Assertions.assertEquals(null, response.getPatientDetails().getId());

    }

    @Test
    @DisplayName("CreateAssessmentForRedRiskPatient Test")
    void createAssessmentForRedRiskPatient() {
        //given
        AssessmentResponseDTO responseDTO = TestDataProvider.getAssessmentResponseDto();
        responseDTO.getPatientDetails().setPatientStatus(Constants.ENROLLED);
        responseDTO.setRiskMessage("Take your prescribed medication and come back in 3 days for a BP recheck. Your doctor may ask you to come for medical review");
        patientTracker.setRedRiskPatient(Constants.BOOLEAN_FALSE);
        patientTracker.setInitialReview(Constants.BOOLEAN_TRUE);
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        RiskAlgorithmDTO riskAlgorithmDTO = TestDataProvider.getRiskAlgorithmDto();
        BpLog bpLog = TestDataProvider.getBpLog();
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();
        assessmentDTO.setBpLog(bpLog);
        assessmentDTO.setGlucoseLog(glucoseLog);
        assessmentDTO.setPhq4(mentalHealth);
        assessmentDTO.setProvisionalDiagnosis(List.of(Constants.PROVISIONALDIAGNOSIS));
        assessmentDTO.setCustomizedWorkflows(List.of(Map.of("CustomizedModule", TestDataProvider.getCustomizedModule())));
        assessmentDTO.setCompliances(List.of(TestDataProvider.getComplianceDTO()));
        PatientAssessment assessment = TestDataProvider.getPatientAssessment();
        Site site = TestDataProvider.getSite();
        TestDataProvider.init();
        ResponseEntity<Site> siteResponse = new ResponseEntity<>(site, HttpStatus.OK);
        //when
        when(patientTrackerService.getPatientTrackerById(assessmentDTO.getPatientTrackId())).thenReturn(patientTracker);
        when(riskAlgorithm.getRiskLevelInAssessmentDbm(riskAlgorithmDTO)).thenReturn(Constants.HIGH);
        BpLog constructBpLog = assessmentService.constructBpLog(assessmentDTO, Constants.CVD_RISK_MEDIUM);
        when(bpLogService.addBpLog(constructBpLog, Constants.BOOLEAN_FALSE)).thenReturn(bpLog);
        GlucoseLog constructGlucoseLog = assessmentService.constructGlucoseLog(assessmentDTO);
        when(glucoseLogService.addGlucoseLog(constructGlucoseLog, Constants.BOOLEAN_FALSE)).thenReturn(glucoseLog);
        when(assessmentRepository.save(new PatientAssessment(bpLog.getId(), null,
                Constants.ASSESSMENT, assessmentDTO.getTenantId(),
                assessmentDTO.getPatientTrackId()))).thenReturn(assessment);
        when(mapper.map(assessmentDTO.getPhq4(), new TypeToken<MentalHealth>() {
        }.getType())).thenReturn(mentalHealth);
        doNothing().when(mentalHealthService).setPhq4Score(mentalHealth);
        when(mentalHealthService.createMentalHealth(mentalHealth,
                patientTracker, Constants.BOOLEAN_TRUE)).thenReturn(mentalHealth);
        TestDataProvider.getStaticMock();
        when(adminApiInterface.getSiteById(Constants.AUTH_TOKEN_SUBJECT, 1l, 1l))
                .thenReturn(siteResponse);
        when(treatmentPlanService.getPatientTreatmentPlan(patientTracker.getId())).thenReturn(null);
        doNothing().when(customizedModulesService).createCustomizedModules(assessmentDTO.getCustomizedWorkflows(),
                Constants.WORKFLOW_ASSESSMENT, patientTracker.getId());
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        responseDTO.setConfirmDiagnosis(patientTracker.getConfirmDiagnosis());
        //then
        AssessmentResponseDTO actualResponse = assessmentService.createAssessment(assessmentDTO);
        TestDataProvider.cleanUp();
        Assertions.assertEquals(responseDTO.getConfirmDiagnosis(), actualResponse.getConfirmDiagnosis());
        Assertions.assertEquals(responseDTO.getRiskLevel(), actualResponse.getRiskLevel());
        Assertions.assertEquals(responseDTO.getRiskMessage(), actualResponse.getRiskMessage());
    }

    @Test
    @DisplayName("CreateAssessmentWithNeagtive Test")
    void createAssessmentWithNegative() {
        //given
        Long assessmentLogId = 1l;
        AssessmentResponseDTO responseDTO = TestDataProvider.getAssessmentResponseDto();
        responseDTO.getPatientDetails().setPatientStatus(Constants.SCREENED);
        responseDTO.setRiskMessage("Take your prescribed medication and come back in 3 days for a BP recheck. Your doctor may ask you to come for medical review");
        patientTracker.setRedRiskPatient(Constants.BOOLEAN_FALSE);
        patientTracker.setInitialReview(Constants.BOOLEAN_TRUE);
        patientTracker.setPatientStatus(Constants.SCREENED);
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        glucoseLog.setHba1c(12.2d);
        RiskAlgorithmDTO riskAlgorithmDTO = TestDataProvider.getRiskAlgorithmDto();
        BpLog bpLog = TestDataProvider.getBpLog();
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();
        PatientAssessment assessment = TestDataProvider.getPatientAssessment();
        Site site = TestDataProvider.getSite();
        TestDataProvider.init();
        ResponseEntity<Site> siteResponse = new ResponseEntity<>(site, HttpStatus.OK);
        //when
        when(patientTrackerService.getPatientTrackerById(assessmentDTO.getPatientTrackId())).thenReturn(patientTracker);
        when(riskAlgorithm.getRiskLevelInAssessmentDbm(riskAlgorithmDTO)).thenReturn(Constants.HIGH);
        BpLog constructBpLog = assessmentService.constructBpLog(assessmentDTO, Constants.CVD_RISK_MEDIUM);
        when(bpLogService.addBpLog(constructBpLog, Constants.BOOLEAN_FALSE)).thenReturn(bpLog);
        GlucoseLog constructGlucoseLog = assessmentService.constructGlucoseLog(assessmentDTO);
        when(glucoseLogService.addGlucoseLog(constructGlucoseLog, Constants.BOOLEAN_FALSE)).thenReturn(glucoseLog);
        when(assessmentRepository.save(new PatientAssessment(bpLog.getId(), null,
                Constants.ASSESSMENT, assessmentDTO.getTenantId(),
                assessmentDTO.getPatientTrackId()))).thenReturn(assessment);
        when(mapper.map(assessmentDTO.getPhq4(), new TypeToken<MentalHealth>() {
        }.getType())).thenReturn(mentalHealth);
        TestDataProvider.getStaticMock();
        when(adminApiInterface.getSiteById(Constants.AUTH_TOKEN_SUBJECT, 1l, 1l))
                .thenReturn(siteResponse);
        when(treatmentPlanService.getPatientTreatmentPlan(patientTracker.getId())).thenReturn(null);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        responseDTO.setConfirmDiagnosis(patientTracker.getConfirmDiagnosis());
        //then
        AssessmentResponseDTO actualResponse = assessmentService.createAssessment(assessmentDTO);
        TestDataProvider.cleanUp();
        Assertions.assertEquals(responseDTO.getConfirmDiagnosis(), actualResponse.getConfirmDiagnosis());
        Assertions.assertEquals(null, actualResponse.getRiskLevel());
        Assertions.assertEquals(responseDTO.getRiskMessage(), actualResponse.getRiskMessage());
    }
}
