package com.mdtlabs.coreplatform.spiceservice.patient.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.UnitConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.DiagnosisDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.EnrollmentRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LifestyleDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientDetailDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientGetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTrackerDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PregnancyDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriberDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserListDTO;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.CustomizedModule;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.Lifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.Patient;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientAssessment;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientPregnancyDetails;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;
import com.mdtlabs.coreplatform.common.model.entity.spice.Prescription;
import com.mdtlabs.coreplatform.common.model.entity.spice.PrescriptionHistory;
import com.mdtlabs.coreplatform.common.model.entity.spice.RedRiskNotification;
import com.mdtlabs.coreplatform.common.model.entity.spice.ScreeningLog;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.ConversionUtil;
import com.mdtlabs.coreplatform.spiceservice.AdminApiInterface;
import com.mdtlabs.coreplatform.spiceservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceservice.assessment.repository.PatientAssessmentRepository;
import com.mdtlabs.coreplatform.spiceservice.bplog.repository.BpLogRepository;
import com.mdtlabs.coreplatform.spiceservice.bplog.service.BpLogService;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.PatientMapper;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientComorbidityRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientComplicationRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientCurrentMedicationRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientDiagnosisRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientLifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.RedRiskNotificationRepository;
import com.mdtlabs.coreplatform.spiceservice.customizedmodules.service.CustomizedModulesService;
import com.mdtlabs.coreplatform.spiceservice.glucoselog.repository.GlucoseLogRepository;
import com.mdtlabs.coreplatform.spiceservice.glucoselog.service.GlucoseLogService;
import com.mdtlabs.coreplatform.spiceservice.medicalreview.repository.MedicalReviewRepository;
import com.mdtlabs.coreplatform.spiceservice.medicalreview.service.MedicalReviewService;
import com.mdtlabs.coreplatform.spiceservice.mentalhealth.repository.MentalHealthRepository;
import com.mdtlabs.coreplatform.spiceservice.mentalhealth.service.MentalHealthService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.LifestyleService;
import com.mdtlabs.coreplatform.spiceservice.patient.repository.PatientPregnancyDetailsRepository;
import com.mdtlabs.coreplatform.spiceservice.patient.repository.PatientRepository;
import com.mdtlabs.coreplatform.spiceservice.patient.service.impl.PatientServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.repository.PatientLabTestRepository;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.repository.PatientLabTestResultRepository;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.service.PatientLabTestService;
import com.mdtlabs.coreplatform.spiceservice.patientmedicalcompliance.repository.PatientMedicalComplianceRepository;
import com.mdtlabs.coreplatform.spiceservice.patientmedicalcompliance.service.PatientMedicalComplianceService;
import com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.repository.PatientNutritionLifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.service.PatientNutritionLifestyleService;
import com.mdtlabs.coreplatform.spiceservice.patientsymptom.repository.PatientSymptomRepository;
import com.mdtlabs.coreplatform.spiceservice.patientsymptom.service.PatientSymptomService;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttransfer.repository.PatientTransferRepository;
import com.mdtlabs.coreplatform.spiceservice.patienttransfer.service.PatientTransferService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.repository.PatientTreatmentPlanRepository;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.repository.PatientVisitRepository;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.service.PatientVisitService;
import com.mdtlabs.coreplatform.spiceservice.prescription.repository.PrescriptionHistoryRepository;
import com.mdtlabs.coreplatform.spiceservice.prescription.repository.PrescriptionRepository;
import com.mdtlabs.coreplatform.spiceservice.prescription.service.PrescriptionService;
import com.mdtlabs.coreplatform.spiceservice.screeninglog.service.ScreeningLogService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * <p>
 * PatientServiceImplTest class used to test all possible positive
 * and negative cases for all methods and conditions used in PatientServiceImpl class.
 * </p>
 *
 * @author Jaganathan created on Feb 8, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PatientServiceImplTest {

    @InjectMocks
    private PatientServiceImpl patientService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientTrackerService patientTrackerService;

    @Mock
    private BpLogRepository bpLogRepository;

    @Mock
    private BpLogService bpLogService;

    @Mock
    private GlucoseLogRepository glucoseLogRepository;

    @Mock
    private GlucoseLogService glucoseLogService;

    @Mock
    private ScreeningLogService screeningLogService;

    @Mock
    private RedRiskNotificationRepository redRiskNotificationRepository;

    @Mock
    private CustomizedModulesService customizedModulesService;

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private PrescriptionHistoryRepository prescriptionHistoryRepository;

    @Mock
    private PatientLabTestRepository labTestRepository;

    @Mock
    private PatientLabTestResultRepository labTestResultRepository;

    @Mock
    private PatientLifestyleRepository patientLifestyleRepository;

    @Mock
    private PatientLabTestService patientLabTestService;

    @Mock
    private PatientLabTestRepository patientLabTestRepository;

    @Mock
    private PatientNutritionLifestyleService patientNutritionLifestyleService;

    @Mock
    private LifestyleService lifestyleService;

    @Mock
    private PatientNutritionLifestyleRepository nutritionLifestyleRepository;

    @Mock
    private MentalHealthRepository mentalHealthRepository;

    @Mock
    private MedicalReviewRepository medicalReviewRepository;

    @Mock
    private MentalHealthService mentalHealthService;

    @Mock
    private MedicalReviewService medicalReviewService;

    @Mock
    private PatientTransferService patientTransferService;

    @Mock
    private PatientTransferRepository patientTransferRepository;

    @Mock
    private PatientAssessmentRepository patientAssessmentRepository;

    @Mock
    private PatientVisitRepository patientVisitRepository;

    @Mock
    private PatientComorbidityRepository comorbidityRepository;

    @Mock
    private PatientComplicationRepository complicationRepository;

    @Mock
    private PatientCurrentMedicationRepository medicationRepository;

    @Mock
    private PatientDiagnosisRepository diagnosisRepository;

    @Mock
    private PatientMedicalComplianceRepository medicalComplianceRepository;

    @Mock
    private PatientPregnancyDetailsRepository pregnancyRepository;

    @Mock
    private PatientSymptomRepository symptomRepository;

    @Mock
    private PatientTreatmentPlanRepository treatmentPlanRepository;

    @Mock
    private PatientTreatmentPlanService patientTreatmentPlanService;

    @Mock
    private PatientVisitService patientVisitService;

    @Mock
    private PatientMedicalComplianceService patientMedicalComplianceService;

    @Mock
    private PatientSymptomService patientSymptomService;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private AdminApiInterface adminApiInterface;

    @Mock
    private UserApiInterface userApiInterface;

    @Mock
    private DataSource dataSource;

    @Mock
    private ModelMapper mapper;

    @Mock
    private PrescriptionService prescriptionService;

    @Test
    @DisplayName("GetPatientBasicDetailsWithIdNUll Test")
    void getPatientBasicDetailsWithTrackIdNull() {
        //given
        CommonRequestDTO requestDTO = new CommonRequestDTO();
        //when
        Assertions.assertNull(requestDTO.getPatientTrackId());
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientService.getPatientBasicDetails(requestDTO));
    }

    @Test
    @DisplayName("GetPatientBasicDetailsWithValidId")
    void getPatientBasicDetailsWithValidId() {
        //given
        CommonRequestDTO requestDTO = TestDataProvider.getCommonRequestDTO();
        Patient patient = TestDataProvider.getPatient();
        PatientDetailDTO patientDetailDTO = TestDataProvider.getPatientDetailsDTO();
        patientDetailDTO.setPatientTrackId(null);
        //when
        when(patientRepository.findByIdAndIsDeleted(requestDTO.getId(),
                Constants.BOOLEAN_FALSE)).thenReturn(patient);
        when(mapper.map(patient, PatientDetailDTO.class)).thenReturn(patientDetailDTO);
        //then
        PatientDetailDTO actualDetails = patientService.getPatientBasicDetails(requestDTO);
        Assertions.assertEquals(patientDetailDTO, actualDetails);
        Assertions.assertEquals(patientDetailDTO.getPatientStatus(), actualDetails.getPatientStatus());
        Assertions.assertEquals(patientDetailDTO.getPatientTrackId(), actualDetails.getPatientTrackId());
    }

    @Test
    @DisplayName("ListMyPatients Test")
    void listMyPatients() {
        //given
        PatientRequestDTO requestDTO = new PatientRequestDTO();
        ResponseListDTO responseListDTO = new ResponseListDTO();
        //when
        when(patientTrackerService.listMyPatients(requestDTO)).thenReturn(responseListDTO);
        //then
        ResponseListDTO actualResponse = patientService.listMyPatients(requestDTO);
        Assertions.assertEquals(responseListDTO, actualResponse);
        //given
        requestDTO = TestDataProvider.getPatientRequestDTO();
        responseListDTO = TestDataProvider.getResponseListDTO();
        //when
        when(patientTrackerService.listMyPatients(requestDTO)).thenReturn(responseListDTO);
        //then
        actualResponse = patientService.listMyPatients(requestDTO);
        Assertions.assertEquals(responseListDTO, actualResponse);
        Assertions.assertEquals(responseListDTO.getTotalCount(), actualResponse.getTotalCount());
    }

    @Test
    @DisplayName("SearchPatients Test")
    void searchPatients() {
        //given
        PatientRequestDTO requestDTO = new PatientRequestDTO();
        ResponseListDTO responseListDTO = new ResponseListDTO();
        //when
        when(patientTrackerService.searchPatients(requestDTO)).thenReturn(responseListDTO);
        //then
        ResponseListDTO actualResponse = patientService.searchPatients(requestDTO);
        Assertions.assertEquals(responseListDTO, actualResponse);
        //given
        requestDTO = TestDataProvider.getPatientRequestDTO();
        responseListDTO = TestDataProvider.getResponseListDTO();
        //when
        when(patientTrackerService.searchPatients(requestDTO)).thenReturn(responseListDTO);
        //then
        actualResponse = patientService.searchPatients(requestDTO);
        Assertions.assertEquals(responseListDTO, actualResponse);
        Assertions.assertEquals(responseListDTO.getTotalCount(), actualResponse.getTotalCount());
    }

    @Test
    @DisplayName("PatientAdvanceSearch Test")
    void patientAdvanceSearch() {
        //given
        PatientRequestDTO requestDTO = new PatientRequestDTO();
        ResponseListDTO responseListDTO = new ResponseListDTO();
        //when
        when(patientTrackerService.patientAdvanceSearch(requestDTO)).thenReturn(responseListDTO);
        //then
        ResponseListDTO actualResponse = patientService.patientAdvanceSearch(requestDTO);
        Assertions.assertEquals(responseListDTO, actualResponse);
        //given
        requestDTO = TestDataProvider.getPatientRequestDTO();
        responseListDTO = TestDataProvider.getResponseListDTO();
        //when
        when(patientTrackerService.patientAdvanceSearch(requestDTO)).thenReturn(responseListDTO);
        //then
        actualResponse = patientService.patientAdvanceSearch(requestDTO);
        Assertions.assertEquals(responseListDTO, actualResponse);
        Assertions.assertEquals(responseListDTO.getTotalCount(), actualResponse.getTotalCount());
    }

    @ParameterizedTest
    @DisplayName("RemovePatientDetailsWithInvalidId Test")
    @CsvSource({", ", ", 1", "1,"})
    void removePatientDetailsWithInvalidId(Long id, Long patientTrackId) {
        //given
        PatientRequestDTO requestDTO = new PatientRequestDTO();
        requestDTO.setId(id);
        requestDTO.setPatientTrackId(patientTrackId);
        //when
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientService.removePatientDetails(requestDTO));
    }

    @Test
    @DisplayName("RemovePatientDetailsWithNull Test")
    void removePatientDetailsWithNull() {
        //given
        PatientRequestDTO requestDTO = TestDataProvider.getPatientRequestDTO();
        Patient patient = null;
        Long tenantId = null;
        //when
        when(patientRepository.findByIdAndIsDeletedAndTenantId(requestDTO.getId(),
                Constants.BOOLEAN_FALSE, null)).thenReturn(null);
        //then
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientService.removePatientDetails(requestDTO));
    }

    @Test
    @DisplayName("removePatientDetailsWithTrackerNull Test")
    void removePatientDetailsWithTrackerNull() {
        //given
        PatientRequestDTO requestDTO = TestDataProvider.getPatientRequestDTO();
        Patient patient = TestDataProvider.getPatient();
        Long tenantId = 4L;
        PatientTracker patientTracker = null;
        MockedStatic<UserSelectedTenantContextHolder>
                userSelectedTenantContextHolder = mockStatic(UserSelectedTenantContextHolder.class);
        //when
        userSelectedTenantContextHolder.when(UserSelectedTenantContextHolder::get).thenReturn(tenantId);
        when(patientRepository.findByIdAndIsDeletedAndTenantId(requestDTO.getId(),
                Constants.BOOLEAN_FALSE, tenantId)).thenReturn(patient);
        when(patientTrackerService.getPatientTrackerByIdAndStatus(requestDTO.getPatientTrackId(),
                tenantId, Constants.ENROLLED)).thenReturn(null);
        //then
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientService.removePatientDetails(requestDTO));
        userSelectedTenantContextHolder.close();
    }

    @Test
    @DisplayName("removePatientDetails Test")
    void removePatientDetails() {
        //given
        PatientRequestDTO requestDTO = TestDataProvider.getPatientRequestDTO();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setDeleteReason("patient is dead");
        patientTracker.setDeleteOtherReason("Patient is Not Available");
        Long tenantId = 4L;
        Patient patient = TestDataProvider.getPatient();
        MockedStatic<UserSelectedTenantContextHolder>
                userSelectedTenantContextHolder = mockStatic(UserSelectedTenantContextHolder.class);
        List<BpLog> bpLogsList = List.of(TestDataProvider.getBpLog());
        List<GlucoseLog> glucoseLogs = List.of(TestDataProvider.getGlucoseLog());
        List<RedRiskNotification> redRiskNotifications = List.of(TestDataProvider.getRedRiskNotification());
        List<PatientAssessment> patientAssessments = List.of(TestDataProvider.getPatientAssessment());
        List<PatientVisit> patientVisits = List.of(TestDataProvider.getPatientVisit());
        PatientPregnancyDetails pregnancyDetails = TestDataProvider.getPatientPregnancyDetails();
        //when
        userSelectedTenantContextHolder.when(UserSelectedTenantContextHolder::get).thenReturn(tenantId);
        when(patientRepository.findByIdAndIsDeletedAndTenantId(requestDTO.getId(),
                Constants.BOOLEAN_FALSE, tenantId)).thenReturn(patient);
        when(patientTrackerService.getPatientTrackerByIdAndStatus(requestDTO.getPatientTrackId(),
                tenantId, Constants.ENROLLED)).thenReturn(patientTracker);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);

        when(bpLogRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(bpLogsList);
        when(bpLogRepository.saveAll(bpLogsList)).thenReturn(bpLogsList);

        when(glucoseLogRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(glucoseLogs);
        when(glucoseLogRepository.saveAll(glucoseLogs)).thenReturn(glucoseLogs);

        when(redRiskNotificationRepository.findByPatientTrackId(requestDTO.getPatientTrackId()))
                .thenReturn(redRiskNotifications);
        when(redRiskNotificationRepository.saveAll(redRiskNotifications)).thenReturn(redRiskNotifications);

        doNothing().when(customizedModulesService).removeCustomizedModule(requestDTO.getPatientTrackId());
        doNothing().when(prescriptionService).removePrescription(requestDTO.getPatientTrackId());
        doNothing().when(patientLabTestService).removeLabTest(requestDTO.getPatientTrackId());
        doNothing().when(patientNutritionLifestyleService).removeNutritionLifestyleByTrackerId(requestDTO.getPatientTrackId());
        doNothing().when(mentalHealthService).removeMentalHealth(requestDTO.getPatientTrackId());
        doNothing().when(medicalReviewService).removeMedicalReview(requestDTO.getPatientTrackId());
        doNothing().when(patientTransferService).removePatientTransfer(requestDTO.getPatientTrackId());
        doNothing().when(patientVisitService).removePatientVisit(requestDTO.getPatientTrackId());
        doNothing().when(patientMedicalComplianceService).removeMedicalCompliance(requestDTO.getPatientTrackId());
        doNothing().when(patientSymptomService).removePatientSymptom(requestDTO.getPatientTrackId());
        doNothing().when(patientTreatmentPlanService).removePatientTreatmentPlan(requestDTO.getPatientTrackId());

        when(patientAssessmentRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(patientAssessments);
        when(patientAssessmentRepository.saveAll(patientAssessments)).thenReturn(patientAssessments);

        when(patientVisitRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(patientVisits);
        when(patientVisitRepository.saveAll(patientVisits)).thenReturn(patientVisits);

        when(pregnancyRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(null);
        when(pregnancyRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(List.of(pregnancyDetails));
        when(pregnancyRepository.save(pregnancyDetails)).thenReturn(pregnancyDetails);

        when(mapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(patientRepository.save(patient)).thenReturn(patient);
        //then
        Patient actualPatient = patientService.removePatientDetails(requestDTO);
        userSelectedTenantContextHolder.close();
        Assertions.assertEquals(patient, actualPatient);
    }

    @Test
    @DisplayName("CreatePatientWithDuplicate Test")
    void createPatientWithDuplicate() {
        //given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        EnrollmentRequestDTO requestDTO = TestDataProvider.getEnrollmentRequestDto();
        requestDTO.setPatientTrackId(null);
        patientTracker.setPatientStatus(Constants.ENROLLED);
        //when
        when(patientTrackerService.findByNationalIdIgnoreCaseAndCountryIdAndIsDeleted(requestDTO.getBioData()
                .getNationalId(), requestDTO.getBioData().getCountry(), Constants.BOOLEAN_FALSE)).thenReturn(patientTracker);
        //given
        requestDTO.setPatientTrackId(1L);
        //when
        when(patientTrackerService.getPatientTrackerById(requestDTO.getPatientTrackId())).thenReturn(patientTracker);
        Assertions.assertThrows(DataConflictException.class, () -> patientService.createPatient(requestDTO));
    }

    @Test
    @DisplayName("CreatePatientWithOrganizationNull Test")
    void createPatientWithOrganizationNull() {
        //given
        Patient enrollPatient = TestDataProvider.getPatient();
        enrollPatient.setCountryId(1L);
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        EnrollmentRequestDTO requestDTO = TestDataProvider.getEnrollmentRequestDto();
        PatientTracker exisitingPatientTracker = null;
        Site site = null;
        ResponseEntity<Site> response = new ResponseEntity<>(null, HttpStatus.OK);
        TestDataProvider.init();
        Organization organization = null;
        ResponseEntity<Organization> organizationResponse = new ResponseEntity<>(null, HttpStatus.OK);
        //when
        when(patientTrackerService.getPatientTrackerById(requestDTO.getPatientTrackId()))
                .thenReturn(null);
        when(patientMapper.setPatient(requestDTO)).thenReturn(enrollPatient);
        when(patientRepository.save(enrollPatient)).thenReturn(enrollPatient);
        TestDataProvider.getStaticMock();
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withFunctionName(Constants.UPDATE_VIRTUAL_ID);
        when(userApiInterface.getOrganizationByFormDataIdAndName(Constants.AUTH_TOKEN_SUBJECT,
                1L, enrollPatient.getCountryId(), Constants.COUNTRY)).thenReturn(organizationResponse);
        //then
        Assertions.assertThrows(DataNotFoundException.class, () -> patientService.createPatient(requestDTO));
        TestDataProvider.cleanUp();
    }

    //@Test(jdbc call)
    @DisplayName("CreatePatientWithSiteNull Test")
    void createPatientWithSiteNull() {
        //given
        Patient patient = TestDataProvider.getPatient();
        Patient enrollPatient = TestDataProvider.getPatient();
        EnrollmentRequestDTO requestDTO = TestDataProvider.getEnrollmentRequestDto();
        PatientTracker exisitingPatientTracker = null;
        Site site = null;
        ResponseEntity<Site> response = new ResponseEntity<>(null, HttpStatus.OK);
        TestDataProvider.init();
        Organization organization = TestDataProvider.getOrganization();
        ResponseEntity<Organization> organizationResponse = new ResponseEntity<>(organization, HttpStatus.OK);
        //when
        when(patientTrackerService.getPatientTrackerById(requestDTO.getPatientTrackId()))
                .thenReturn(null);
        when(patientMapper.setPatient(requestDTO)).thenReturn(patient);
        when(patientRepository.save(enrollPatient)).thenReturn(enrollPatient);
        TestDataProvider.getStaticMock();
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withFunctionName(Constants.UPDATE_VIRTUAL_ID);
        when(userApiInterface.getOrganizationByFormDataIdAndName(Constants.AUTH_TOKEN_SUBJECT,
                1L, enrollPatient.getCountryId(), Constants.COUNTRY)).thenReturn(organizationResponse);
        SqlParameterSource in = new MapSqlParameterSource().addValue(Constants.INPUT_ID, patient.getId())
                .addValue(Constants.INPUT_TENANT_ID, organization.getId());
        Long virtualId = jdbcCall.executeFunction(Long.class, in);

        when(adminApiInterface.getSiteById(Constants.AUTH_TOKEN_SUBJECT,
                1L, enrollPatient.getSiteId())).thenReturn(response);
        //then
        Assertions.assertThrows(DataNotFoundException.class, () -> patientService.createPatient(requestDTO));
        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("UpdatePregnancyDetailsWithNull Test")
    void updatePregnancySDetailsWithNull() {
        //given
        PregnancyDetailsDTO requestDTO = TestDataProvider.getPregnancyDetails();
        requestDTO.setPatientTrackId(null);
        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientService.updatePregnancyDetails(requestDTO));
        //given
        requestDTO.setPatientTrackId(1L);
        PatientPregnancyDetails pregnancyDetails = null;
        //when
        when(pregnancyRepository.findByIdAndIsDeleted(requestDTO.getPatientPregnancyId(),
                Constants.BOOLEAN_FALSE)).thenReturn(null);
        //then
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientService.updatePregnancyDetails(requestDTO));
        //given
        MockedStatic<ConversionUtil> conversionUtil = mockStatic(ConversionUtil.class);
        requestDTO.setUnitMeasurement(UnitConstants.IMPERIAL);
        //when
        when(pregnancyRepository.findByIdAndIsDeleted(requestDTO.getPatientPregnancyId(),
                Constants.BOOLEAN_FALSE)).thenReturn(null);
        conversionUtil.when(() -> ConversionUtil.convertTemperature(requestDTO.getTemperature(),
                UnitConstants.METRIC)).thenReturn(requestDTO.getTemperature());
        //then
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientService.updatePregnancyDetails(requestDTO));
        conversionUtil.close();
    }

    @Test
    @DisplayName("UpdatePregnancyDetails Test")
    void updatePregnancyDetails() {
        //given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        PregnancyDetailsDTO requestDTO = TestDataProvider.getPregnancyDetails();
        PatientPregnancyDetails pregnancyDetails = TestDataProvider.getPatientPregnancyDetails();
        requestDTO.setUnitMeasurement(UnitConstants.IMPERIAL);
        MockedStatic<ConversionUtil> conversionUtil = mockStatic(ConversionUtil.class);
        //when
        when(pregnancyRepository.findByIdAndIsDeleted(requestDTO.getPatientPregnancyId(),
                Constants.BOOLEAN_FALSE)).thenReturn(pregnancyDetails);
        when(mapper.map(requestDTO, PatientPregnancyDetails.class)).thenReturn(pregnancyDetails);
        when(pregnancyRepository.save(pregnancyDetails)).thenReturn(pregnancyDetails);
        conversionUtil.when(() -> ConversionUtil.convertTemperature(requestDTO.getTemperature(),
                UnitConstants.METRIC)).thenReturn(requestDTO.getTemperature());
        when(patientTrackerService.getPatientTrackerById(requestDTO.getPatientTrackId())).thenReturn(patientTracker);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        //then
        PatientPregnancyDetails actualDetails = patientService.updatePregnancyDetails(requestDTO);
        Assertions.assertEquals(pregnancyDetails, actualDetails);
        Assertions.assertEquals(pregnancyDetails.getTemperature(), actualDetails.getTemperature());
        Assertions.assertEquals(pregnancyDetails.getDiagnosis(), actualDetails.getDiagnosis());
        //given
        requestDTO.setDiagnosis(List.of(Constants.CULTURE_VALUE_DIAGNOSIS));
        pregnancyDetails.setDiagnosis(List.of(Constants.CULTURE_VALUE_DIAGNOSIS));
        //when

        when(pregnancyRepository.findByIdAndIsDeleted(requestDTO.getPatientPregnancyId(),
                Constants.BOOLEAN_FALSE)).thenReturn(pregnancyDetails);
        when(mapper.map(requestDTO, PatientPregnancyDetails.class)).thenReturn(pregnancyDetails);
        when(pregnancyRepository.save(pregnancyDetails)).thenReturn(pregnancyDetails);
        when(patientTrackerService.getPatientTrackerById(requestDTO.getPatientTrackId())).thenReturn(patientTracker);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        //then
        actualDetails = patientService.updatePregnancyDetails(requestDTO);
        conversionUtil.close();
        Assertions.assertEquals(pregnancyDetails, actualDetails);
        Assertions.assertEquals(pregnancyDetails.getTemperature(), actualDetails.getTemperature());
        Assertions.assertEquals(pregnancyDetails.getDiagnosis(), actualDetails.getDiagnosis());
    }

    @Test
    @DisplayName("GetPregnancyDetailsWithNull Test")
    void getPregnancyDetailsWithNull() {
        //given
        GetRequestDTO requestDTO = new GetRequestDTO();
        //then
        Assertions.assertThrows(DataNotAcceptableException.class, () -> patientService.getPregnancyDetails(requestDTO));

    }

    @Test
    @DisplayName("GetPregnancyDetailsWithPregnancyIdNull Test")
    void getPregnancyDetailsWithPregnancyIdNull() {
        //given
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();
        requestDTO.setPatientPregnancyId(null);
        PatientPregnancyDetails pregnancyDetails;
        //when
        when(pregnancyRepository.findByPatientTrackIdAndIsDeleted(requestDTO.getPatientTrackId(),
                Constants.BOOLEAN_FALSE)).thenReturn(null);
        //then
        PregnancyDetailsDTO actualDetails = patientService.getPregnancyDetails(requestDTO);
        Assertions.assertNull(actualDetails);
        //given
        pregnancyDetails = TestDataProvider.getPatientPregnancyDetails();
        TestDataProvider.init();
        PregnancyDetailsDTO pregnancyDetailsDTO = TestDataProvider.getPregnancyDetails();
        pregnancyDetailsDTO.setUnitMeasurement(null);

        //when
        when(pregnancyRepository.findByPatientTrackIdAndIsDeleted(requestDTO.getPatientTrackId(),
                Constants.BOOLEAN_FALSE)).thenReturn(pregnancyDetails);
        TestDataProvider.getStaticMock();
        when(mapper.map(pregnancyDetails, PregnancyDetailsDTO.class)).thenReturn(pregnancyDetailsDTO);
        //then
        actualDetails = patientService.getPregnancyDetails(requestDTO);
        TestDataProvider.cleanUp();
        Assertions.assertEquals(pregnancyDetailsDTO, actualDetails);
        //given
        TestDataProvider.init();
        UserDTO userDTO = TestDataProvider.getUserDTO();
        userDTO.getCountry().setUnitMeasurement(UnitConstants.IMPERIAL);
        //when
        when(pregnancyRepository.findByPatientTrackIdAndIsDeleted(requestDTO.getPatientTrackId(),
                Constants.BOOLEAN_FALSE)).thenReturn(pregnancyDetails);
        TestDataProvider.getStaticMock();
        when(mapper.map(pregnancyDetails, PregnancyDetailsDTO.class)).thenReturn(pregnancyDetailsDTO);
        //then
        actualDetails = patientService.getPregnancyDetails(requestDTO);
        Assertions.assertEquals(pregnancyDetailsDTO, actualDetails);
        TestDataProvider.cleanUp();

    }

    //    @Test
    @DisplayName("GetPregnancyDetailsWithUnitImperial Test")
    void getPregnancyDetailsWithUnitImperial() {
        //given
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();
        PatientPregnancyDetails pregnancyDetails = TestDataProvider.getPatientPregnancyDetails();
        PregnancyDetailsDTO pregnancyDetailsDTO = TestDataProvider.getPregnancyDetails();
        pregnancyDetailsDTO.setUnitMeasurement(null);
        UserDTO userDTO = TestDataProvider.getUserDTO();
        Country country = TestDataProvider.getCountry();
        country.setUnitMeasurement(UnitConstants.IMPERIAL);
        userDTO.setCountry(country);
        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);
        MockedStatic<ConversionUtil> conversionUtil = mockStatic(ConversionUtil.class);
        //when
        when(pregnancyRepository.findByIdAndIsDeleted(requestDTO.getPatientPregnancyId(),
                Constants.BOOLEAN_FALSE)).thenReturn(pregnancyDetails);
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
        conversionUtil.when(() -> ConversionUtil.convertTemperature(pregnancyDetails.getTemperature(),
                UnitConstants.IMPERIAL)).thenReturn(pregnancyDetails.getTemperature());
        when(mapper.map(pregnancyDetails, PregnancyDetailsDTO.class)).thenReturn(pregnancyDetailsDTO);
        //then
        PregnancyDetailsDTO actualDetails = patientService.getPregnancyDetails(requestDTO);
        userContextHolder.close();
        conversionUtil.close();
        // Assertions.assertEquals(pregnancyDetailsDTO, actualDetails);
        Assertions.assertEquals(pregnancyDetailsDTO.getPatientPregnancyId(), actualDetails.getPatientPregnancyId());
        Assertions.assertEquals(pregnancyDetailsDTO.getPatientTrackId(), actualDetails.getPatientTrackId());
    }

    //    @Test
    @DisplayName("GetPregnancyDetails Test")
    void getPregnancyDetails() {
        //given
        GetRequestDTO requestDTO = TestDataProvider.getGetRequestDTO();
        requestDTO.setPatientPregnancyId(1L);
        PatientPregnancyDetails pregnancyDetails = TestDataProvider.getPatientPregnancyDetails();
        TestDataProvider.init();
        PregnancyDetailsDTO pregnancyDetailsDTO = TestDataProvider.getPregnancyDetails();
        pregnancyDetailsDTO.setUnitMeasurement(null);

        //when
        when(pregnancyRepository.findByIdAndIsDeleted(requestDTO.getPatientPregnancyId(),
                Constants.BOOLEAN_FALSE)).thenReturn(pregnancyDetails);
        TestDataProvider.getStaticMock();
        when(mapper.map(pregnancyDetails, PregnancyDetailsDTO.class)).thenReturn(pregnancyDetailsDTO);
        //then
        PregnancyDetailsDTO actualDetails = patientService.getPregnancyDetails(requestDTO);
        TestDataProvider.cleanUp();
        Assertions.assertEquals(pregnancyDetailsDTO, actualDetails);
        //given
        TestDataProvider.init();
        UserDTO userDTO = TestDataProvider.getUserDTO();
        userDTO.getCountry().setUnitMeasurement(UnitConstants.IMPERIAL);
        //when
        when(pregnancyRepository.findByIdAndIsDeleted(requestDTO.getPatientPregnancyId(),
                Constants.BOOLEAN_FALSE)).thenReturn(pregnancyDetails);
        TestDataProvider.getStaticMock();
        when(mapper.map(pregnancyDetails, PregnancyDetailsDTO.class)).thenReturn(pregnancyDetailsDTO);
        //then
        actualDetails = patientService.getPregnancyDetails(requestDTO);
        TestDataProvider.cleanUp();
        // Assertions.assertEquals(pregnancyDetailsDTO, actualDetails);
        Assertions.assertEquals(requestDTO.getPatientTrackId(), actualDetails.getPatientTrackId());
        Assertions.assertEquals(requestDTO.getPatientPregnancyId(), actualDetails.getPatientPregnancyId());
    }

    @Test
    @DisplayName("CreatePregnancyDetailsWithNull Test")
    void createPregnancyDetailsWithNull() {
        //given
        PregnancyDetailsDTO pregnancyDetailsDTO = new PregnancyDetailsDTO();
        //when
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientService.createPregnancyDetails(pregnancyDetailsDTO));
    }

    @Test
    @DisplayName("createPregnancyDetails Test")
    void createPregnancyDetails() {
        //given
        PregnancyDetailsDTO requestDTO = TestDataProvider.getPregnancyDetails();
        requestDTO.setUnitMeasurement(UnitConstants.METRIC);
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        PatientPregnancyDetails pregnancyDetails = TestDataProvider.getPatientPregnancyDetails();
        //when
        when(patientTrackerService.getPatientTrackerById(requestDTO.getPatientTrackId())).thenReturn(patientTracker);
        when(mapper.map(requestDTO, PatientPregnancyDetails.class)).thenReturn(pregnancyDetails);
        when(pregnancyRepository.save(pregnancyDetails)).thenReturn(pregnancyDetails);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        doNothing().when(mapper).map(pregnancyDetails, requestDTO);
        //then
        PregnancyDetailsDTO actualDetails = patientService.createPregnancyDetails(requestDTO);
        Assertions.assertEquals(requestDTO, actualDetails);
        Assertions.assertEquals(requestDTO.getPatientTrackId(), actualDetails.getPatientTrackId());
        Assertions.assertEquals(requestDTO.getPatientPregnancyId(), actualDetails.getPatientPregnancyId());

        //given
        requestDTO.setUnitMeasurement(UnitConstants.IMPERIAL);
        MockedStatic<ConversionUtil> conversionUtil = mockStatic(ConversionUtil.class);
        //when
        when(patientTrackerService.getPatientTrackerById(requestDTO.getPatientTrackId())).thenReturn(patientTracker);
        when(mapper.map(requestDTO, PatientPregnancyDetails.class)).thenReturn(pregnancyDetails);
        when(pregnancyRepository.save(pregnancyDetails)).thenReturn(pregnancyDetails);
        conversionUtil.when(() -> ConversionUtil.convertTemperature(requestDTO.getTemperature(),
                UnitConstants.METRIC)).thenReturn(requestDTO.getTemperature());
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        doNothing().when(mapper).map(pregnancyDetails, requestDTO);
        //then
        actualDetails = patientService.createPregnancyDetails(requestDTO);
        conversionUtil.close();
        // Assertions.assertEquals(requestDTO, actualDetails);
        Assertions.assertEquals(requestDTO.getPatientTrackId(), actualDetails.getPatientTrackId());
        Assertions.assertEquals(requestDTO.getPatientPregnancyId(), actualDetails.getPatientPregnancyId());
    }

    //    @Test
    @DisplayName("GetPatientDetails Test")
    void getPatientDetails() {
        //given
        PatientGetRequestDTO requestDTO = TestDataProvider.getPatientGetRequestDTO();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setScreeningLogId(1L);
        patientTracker.setCreatedAt(new Date());
        PatientTrackerDTO patientTrackerDTO = TestDataProvider.getPatientTrackerDTO();
        patientTrackerDTO.setEnrollmentAt(null);
        TestDataProvider.init();
        BpLog bpLog = TestDataProvider.getBpLog();
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        ScreeningLog screeningLog = TestDataProvider.getScreeningLog();
        //when
        when(patientTrackerService.getPatientTrackerById(requestDTO.getId())).thenReturn(patientTracker);
        when(mapper.map(patientTracker, PatientTrackerDTO.class)).thenReturn(patientTrackerDTO);
        TestDataProvider.getStaticMock();
        when(bpLogService.getBpLogByPatientTrackIdAndIsLatest(requestDTO.getId(),
                Constants.BOOLEAN_TRUE)).thenReturn(bpLog);
        when(glucoseLogService.getGlucoseLogByPatientTrackIdAndIsLatest(requestDTO.getId(),
                Constants.BOOLEAN_TRUE)).thenReturn(glucoseLog);
        when(screeningLogService.getByIdAndIsLatest(patientTracker.getScreeningLogId())).thenReturn(screeningLog);
        //then
        PatientTrackerDTO actualTrackerDTO = patientService.getPatientDetails(requestDTO);
        TestDataProvider.cleanUp();
        Assertions.assertEquals(patientTrackerDTO, actualTrackerDTO);
        Assertions.assertEquals(patientTrackerDTO.getPatientId(), actualTrackerDTO.getPatientId());
    }

    @Test
    @DisplayName("GetPatientDetailsWithNegative Test")
    void getPatientDetailsWithNegative() {
        //given
        PatientGetRequestDTO requestDTO = TestDataProvider.getPatientGetRequestDTO();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setScreeningLogId(1L);
        patientTracker.setEnrollmentAt(new Date());
        patientTracker.setPhq4FirstScore(Constants.TWO);
        patientTracker.setPhq4SecondScore(Constants.TWO);
        patientTracker.setCreatedAt(new Date());
        PatientTrackerDTO patientTrackerDTO = TestDataProvider.getPatientTrackerDTO();
        patientTrackerDTO.setEnrollmentAt(new Date());
        patientTrackerDTO.setPhq4FirstScore(Constants.TWO);
        patientTrackerDTO.setPhq4SecondScore(Constants.TWO);
        UserDTO userDTO = TestDataProvider.getUserDTO();
        Country country = TestDataProvider.getCountry();
        country.setUnitMeasurement(UnitConstants.IMPERIAL);
        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);
        BpLog bpLog = TestDataProvider.getBpLog();
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        ScreeningLog screeningLog = TestDataProvider.getScreeningLog();
        //when
        when(patientTrackerService.getPatientTrackerById(requestDTO.getId())).thenReturn(patientTracker);
        when(mapper.map(patientTracker, PatientTrackerDTO.class)).thenReturn(patientTrackerDTO);
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
        when(bpLogService.getBpLogByPatientTrackIdAndIsLatest(requestDTO.getId(),
                Constants.BOOLEAN_TRUE)).thenReturn(bpLog);
        when(glucoseLogService.getGlucoseLogByPatientTrackIdAndIsLatest(requestDTO.getId(),
                Constants.BOOLEAN_TRUE)).thenReturn(glucoseLog);
        when(screeningLogService.getByIdAndIsLatest(patientTracker.getScreeningLogId())).thenReturn(screeningLog);
        //then
        PatientTrackerDTO actualTrackerDTO = patientService.getPatientDetails(requestDTO);
        userContextHolder.close();
        // Assertions.assertEquals(patientTrackerDTO, actualTrackerDTO);
        Assertions.assertEquals(patientTrackerDTO.getPatientId(), actualTrackerDTO.getPatientId());
        Assertions.assertEquals(patientTrackerDTO.getPhq4FirstScore(), actualTrackerDTO.getPhq4FirstScore());
    }

    @Test
    @DisplayName("GetPatientLifeStyleDetailsWithNull Test")
    void getPatientLifeStyleDetailsWithNull() {
        //given
        CommonRequestDTO requestDTO = new CommonRequestDTO();
        //when
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientService.getPatientLifeStyleDetails(requestDTO));

    }

    @Test
    @DisplayName("GetPatientLifeStyleDetails Test")
    void getPatientLifeStyleDetails() {
        //given
        CommonRequestDTO requestDTO = TestDataProvider.getRequestDto();
        PatientLifestyle patientLifestyle = TestDataProvider.getPatientLifestyle();
        List<PatientLifestyle> lifestyles;
        List<Lifestyle> lifestyleList = List.of(TestDataProvider.getLifestyle());
        List<LifestyleDTO> response = new ArrayList<>();
        //when
        when(patientLifestyleRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(null);
        //then
        List<LifestyleDTO> actualResponse = patientService.getPatientLifeStyleDetails(requestDTO);
        Assertions.assertEquals(response, actualResponse);
        //given
        response = List.of(new LifestyleDTO(null, Constants.NAME, Constants.DIABETES, null));
        lifestyles = List.of(patientLifestyle);
        List<Long> ids = List.of(1L);

        //when
        when(patientLifestyleRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(lifestyles);
        when(lifestyleService.getLifestylesByIds(ids)).thenReturn(lifestyleList);
        //then
        actualResponse = patientService.getPatientLifeStyleDetails(requestDTO);
        Assertions.assertEquals(response, actualResponse);
    }

    @Test
    @DisplayName("GetPatientDetailsWithImperial Test")
    void getPatientDetailsWithImperial() {
        //given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setInitialReview(Constants.BOOLEAN_TRUE);
        PatientGetRequestDTO requestDTO = TestDataProvider.getPatientGetRequestDTO();
        requestDTO.setAssessmentRequired(Constants.BOOLEAN_FALSE);
        requestDTO.setPrescriberRequired(Constants.BOOLEAN_TRUE);
        requestDTO.setLifeStyleRequired(Constants.BOOLEAN_TRUE);
        PatientTrackerDTO patientTrackerDTO = TestDataProvider.getPatientTrackerDTO();
        UserDTO userDTO = TestDataProvider.getUserDTO();
        Country country = TestDataProvider.getCountry();
        country.setUnitMeasurement(UnitConstants.IMPERIAL);
        userDTO.setCountry(country);
        UserListDTO userListDTO = TestDataProvider.getUserListDTO();
        PrescriptionHistory prescriptionHistory = TestDataProvider.getPrescriptionHistory();
        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);
        MockedStatic<ConversionUtil> conversionUtil = mockStatic(ConversionUtil.class);
        Prescription prescription = TestDataProvider.getPrescription();
        MockedStatic<CommonUtil> commonUtil = mockStatic(CommonUtil.class);
        MockedStatic<UserSelectedTenantContextHolder> userSelectedTenantContextHolder
                = mockStatic(UserSelectedTenantContextHolder.class);
        PrescriberDTO prescriberDTO = TestDataProvider.getPrescriberDTO();
        //when
        when(patientTrackerService.getPatientTrackerById(requestDTO.getId())).thenReturn(patientTracker);
        when(mapper.map(patientTracker, PatientTrackerDTO.class)).thenReturn(patientTrackerDTO);
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
        when(prescriptionRepository.findFirstByPatientTrackIdAndTenantIdOrderByUpdatedByDesc(1L,
                4L)).thenReturn(prescription);
        conversionUtil.when(() -> ConversionUtil.calculatePatientAge(32,
                patientTracker.getCreatedAt())).thenReturn(patientTrackerDTO.getAge());
        commonUtil.when(CommonUtil::getAuthToken).thenReturn(Constants.AUTH_TOKEN_SUBJECT);
        userSelectedTenantContextHolder.when(UserSelectedTenantContextHolder::get).thenReturn(4L);
        when(userApiInterface.getPrescriberDetails(Constants.AUTH_TOKEN_SUBJECT,
                4L, prescription.getUpdatedBy())).thenReturn(userListDTO);
        when(prescriptionService.getPatientPrescribedDetails(1L, 1L)).thenReturn(prescriberDTO);
        when(prescriptionHistoryRepository
                .findFirstByPatientTrackIdAndPrescriptionFilledDaysGreaterThanOrderByUpdatedAtDesc(1L,
                        Constants.ZERO)).thenReturn(prescriptionHistory);
        //then
        PatientTrackerDTO actualPatient = patientService.getPatientDetails(requestDTO);
        userContextHolder.close();
        commonUtil.close();
        userSelectedTenantContextHolder.close();
        // Assertions.assertEquals(patientTrackerDTO, actualPatient);
        Assertions.assertEquals(patientTrackerDTO.getPatientId(), actualPatient.getPatientId());
        Assertions.assertEquals(patientTrackerDTO.getAge(), actualPatient.getAge());
    }

    @Test
    @DisplayName("GetPatientDetailsWithBioMetrics Test")
    void getPatientDetailsWithBioMetrics() {
        //given
        PatientGetRequestDTO requestDTO = TestDataProvider.getPatientGetRequestDTO();
        requestDTO.setAssessmentRequired(Constants.BOOLEAN_TRUE);
        requestDTO.setLifeStyleRequired(Constants.BOOLEAN_TRUE);
        requestDTO.setPrescriberRequired(Constants.BOOLEAN_FALSE);
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setHeight(173.6);
        patientTracker.setWeight(73.5);
        patientTracker.setInitialReview(Constants.BOOLEAN_FALSE);
        PatientTrackerDTO patientTrackerDTO = TestDataProvider.getPatientTrackerDTO();
        patientTrackerDTO.setHeight(173.6);
        patientTrackerDTO.setWeight(73.5);
        UserDTO userDTO = TestDataProvider.getUserDTO();
        Country country = TestDataProvider.getCountry();
        country.setUnitMeasurement(UnitConstants.IMPERIAL);
        userDTO.setCountry(country);
        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);
        MockedStatic<ConversionUtil> conversionUtil = mockStatic(ConversionUtil.class);
        //when
        when(patientTrackerService.getPatientTrackerById(requestDTO.getId())).thenReturn(patientTracker);
        when(mapper.map(patientTracker, PatientTrackerDTO.class)).thenReturn(patientTrackerDTO);
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
        conversionUtil.when(() -> ConversionUtil.convertHeight(patientTrackerDTO.getHeight(),
                UnitConstants.METRIC)).thenReturn(patientTrackerDTO.getHeight());
        conversionUtil.when(() -> ConversionUtil.convertWeight(patientTrackerDTO.getWeight(),
                UnitConstants.METRIC)).thenReturn(patientTrackerDTO.getWeight());
        when(bpLogService.getBpLogByPatientTrackIdAndIsLatest(requestDTO.getId(),
                Constants.BOOLEAN_TRUE)).thenReturn(null);
        when(glucoseLogService.getGlucoseLogByPatientTrackIdAndIsLatest(requestDTO.getId(),
                Constants.BOOLEAN_TRUE)).thenReturn(null);
        conversionUtil.when(() -> ConversionUtil.calculatePatientAge(patientTrackerDTO.getAge(),
                patientTrackerDTO.getEnrollmentAt())).thenReturn(patientTrackerDTO.getAge());
        when(screeningLogService.getByIdAndIsLatest(1L)).thenReturn(null);
        //then
        PatientTrackerDTO actualPatient = patientService.getPatientDetails(requestDTO);
        userContextHolder.close();
        conversionUtil.close();
        // Assertions.assertEquals(patientTrackerDTO, actualPatient);
        Assertions.assertEquals(patientTrackerDTO.getPatientId(), actualPatient.getPatientId());
        Assertions.assertEquals(patientTrackerDTO.getAge(), actualPatient.getAge());
    }

    @ParameterizedTest
    @DisplayName("UpdatePatientDetailsWithIdNull Test")
    @CsvSource({" , ", " , 1", "1, "})
    void updatePatientDetailsWithIdNull(Long id, Long patientTrackId) {
        //given
        PatientDetailDTO patientDetailDTO = new PatientDetailDTO();
        patientDetailDTO.setId(id);
        patientDetailDTO.setPatientTrackId(patientTrackId);
        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientService.updatePatientDetails(patientDetailDTO));
    }

    @Test
    @DisplayName("UpdatePatientDetailsWithPatientNull Test")
    void updatePatientDetailsWithPatientNull() {
        //given
        PatientDetailDTO patientDetailDTO = TestDataProvider.getPatientDetailsDTO();
        TestDataProvider.init();
        //when
        TestDataProvider.getStaticMock();
        when(patientRepository.findByIdAndIsDeletedAndTenantId(1L,
                Constants.BOOLEAN_FALSE, 4L)).thenReturn(null);
        //then
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientService.updatePatientDetails(patientDetailDTO));
        TestDataProvider.cleanUp();

    }

    @Test
    @DisplayName("UpdatePatientDetailsWithTrackerNull Test")
    void updatePatientDetailsWithTrackerNull() {
        //given
        PatientDetailDTO patientDetailDTO = TestDataProvider.getPatientDetailsDTO();
        Patient patient = TestDataProvider.getPatient();
        TestDataProvider.init();
        //when
        TestDataProvider.getStaticMock();
        when(patientRepository.findByIdAndIsDeletedAndTenantId(1L,
                Constants.BOOLEAN_FALSE, 1L)).thenReturn(patient);
        when(patientTrackerService.getPatientTrackerByIdAndStatus(patientDetailDTO.getPatientTrackId(),
                1L, Constants.ENROLLED)).thenReturn(null);
        //then
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientService.updatePatientDetails(patientDetailDTO));
        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("UpdatePatientDetails Test")
    void updatePatientDetails() {
        //given
        PatientDetailDTO patientDetailDTO = TestDataProvider.getPatientDetailsDTO();
        Patient patient = TestDataProvider.getPatient();
        patientDetailDTO.setHeight(5.5f);
        patientDetailDTO.setWeight(55.5f);
        patientDetailDTO.setBmi(32.5f);
        patientDetailDTO.setPhoneNumber("PHONE_NUMBER");
        patientDetailDTO.setFirstName("FIRST_NAME");
        patientDetailDTO.setLastName("LAST_NAME");
        patientDetailDTO.setGender("MALE");
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        TestDataProvider.init();
        //when
        TestDataProvider.getStaticMock();
        when(patientRepository.findByIdAndIsDeletedAndTenantId(anyLong(),
                anyBoolean(), anyLong())).thenReturn(patient);
        when(patientTrackerService.getPatientTrackerByIdAndStatus(patientDetailDTO.getPatientTrackId(),
                1L, Constants.ENROLLED)).thenReturn(patientTracker);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        when(mapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        doNothing().when(mapper).map(patient, patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(mapper.map(patient, PatientDetailDTO.class)).thenReturn(patientDetailDTO);
        //then
        PatientDetailDTO actualDetail = patientService.updatePatientDetails(patientDetailDTO);
        actualDetail.setPatientTrackId(1L);
        patientDetailDTO.setHeight(null);
        patientDetailDTO.setWeight(null);
        patientDetailDTO.setBmi(null);
        Assertions.assertEquals(patientDetailDTO, actualDetail);
        TestDataProvider.cleanUp();

        //given
        patientDetailDTO.setHeight(null);
        patientDetailDTO.setWeight(null);
        patientDetailDTO.setBmi(null);
        patientDetailDTO.setPhoneNumber("phone_number");
        patientDetailDTO.setFirstName("FIRST_NAME");
        patientDetailDTO.setLastName("LAST_NAME");
        patientDetailDTO.setGender("Male");
        TestDataProvider.init();
        //when
        TestDataProvider.getStaticMock();
        when(patientRepository.findByIdAndIsDeletedAndTenantId(1L,
                Constants.BOOLEAN_FALSE, 1L)).thenReturn(patient);
        when(patientTrackerService.getPatientTrackerByIdAndStatus(patientDetailDTO.getPatientTrackId(),
                1L, Constants.ENROLLED)).thenReturn(patientTracker);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        when(mapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        doNothing().when(mapper).map(patient, patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(mapper.map(patient, PatientDetailDTO.class)).thenReturn(patientDetailDTO);
        //then
        actualDetail = patientService.updatePatientDetails(patientDetailDTO);
        patientDetailDTO.setPatientTrackId(null);
        Assertions.assertEquals(patientDetailDTO, actualDetail);
        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("GetConfirmDiagnosisValues Test")
    void getConfirmDiagnosis() {
        //given
        List<String> diagnosisList = new ArrayList<>();
        List<String> overallDiagnosis;
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        DiagnosisDTO diagnosisDTO = TestDataProvider.getDiagnosisDTO();
        diagnosisDTO.setHtnDiagnosis(Constants.HYPERTENSION);
        diagnosisList.add(diagnosisDTO.getDiabetesDiagnosis());
        diagnosisList.add(diagnosisDTO.getHtnDiagnosis());
        //then
        List<String> actualList = patientService.getConfirmDiagnosisValues(patientTracker, diagnosisDTO);
        Assertions.assertEquals(diagnosisList, actualList);
        Assertions.assertEquals(diagnosisList.size(), actualList.size());
        //given
        patientTracker.setConfirmDiagnosis(diagnosisList);
        overallDiagnosis = List.of("Diabetes", "Hypertension", "Diabetes");
        //then
        actualList = patientService.getConfirmDiagnosisValues(patientTracker, diagnosisDTO);
        Assertions.assertEquals(overallDiagnosis, actualList);
        Assertions.assertEquals(overallDiagnosis.size(), actualList.size());
        //given
        patientTracker.setConfirmDiagnosis(null);
        diagnosisDTO.setDiabetesDiagnosis(null);
        diagnosisDTO.setHtnDiagnosis(null);
        //then
        actualList = patientService.getConfirmDiagnosisValues(patientTracker, diagnosisDTO);
        Assertions.assertTrue(actualList.isEmpty());
        //given
        patientTracker.setConfirmDiagnosis(List.of("Diabetes", "Hypertension", "Diabetes"));
        //then
        actualList = patientService.getConfirmDiagnosisValues(patientTracker, diagnosisDTO);
        Assertions.assertNotNull(actualList);
    }

    @Test
    @DisplayName("RemovePatientWithNull Test")
    void removePatientWithNull() {
        //given
        PatientRequestDTO requestDTO = TestDataProvider.getPatientRequestDTO();
        Patient patient = TestDataProvider.getPatient();
        patient.setDeleted(Constants.BOOLEAN_TRUE);
        patient.setActive(Constants.BOOLEAN_FALSE);
        requestDTO.setDeleteReason(Constants.EMPTY);
        requestDTO.setDeleteOtherReason(Constants.EMPTY);
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setId(1L);
        patientTracker.setActive(false);
        patientTracker.setDeleted(true);
        TestDataProvider.init();
        List<CustomizedModule> customizedModules = List.of(TestDataProvider.getCustomizedModule());

        //when
        TestDataProvider.getStaticMock();
        when(patientRepository.findByIdAndIsDeletedAndTenantId(patient.getId(),
                Constants.BOOLEAN_FALSE, 1L)).thenReturn(patient);
        when(patientTrackerService.getPatientTrackerByIdAndStatus(requestDTO.getPatientTrackId(),
                1L, Constants.ENROLLED)).thenReturn(patientTracker);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);

        //    when(customizedModuleRepository.saveAll(customizedModules)).thenReturn(customizedModules);
        when(redRiskNotificationRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(null);
        when(patientLifestyleRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(null);
        when(mentalHealthRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(null);
        when(medicalReviewRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(null);
        when(patientTransferRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(null);
        when(patientAssessmentRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(null);
        when(patientVisitRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(null);
        when(comorbidityRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(null);
        when(complicationRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(null);
        when(medicationRepository.findBypatientTrackId(requestDTO.getPatientTrackId())).thenReturn(null);
        when(pregnancyRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(null);
        when(symptomRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(null);
        when(treatmentPlanRepository.findByPatientTrackId(requestDTO.getPatientTrackId())).thenReturn(List.of());
        when(mapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(patientRepository.save(patient)).thenReturn(patient);

        //then
        Patient actualPatient = patientService.removePatientDetails(requestDTO);
        TestDataProvider.cleanUp();
        Assertions.assertEquals(patient, actualPatient);
    }
}