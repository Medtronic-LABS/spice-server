package com.mdtlabs.coreplatform.spiceservice.patienttransfer.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTransferRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTransferUpdateRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.CustomizedModule;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import com.mdtlabs.coreplatform.common.model.entity.spice.Patient;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientAssessment;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComorbidity;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComplication;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientCurrentMedication;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientDiagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTestResult;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalCompliance;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalReview;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientNutritionLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientPregnancyDetails;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientSymptom;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTransfer;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;
import com.mdtlabs.coreplatform.common.model.entity.spice.Prescription;
import com.mdtlabs.coreplatform.common.model.entity.spice.PrescriptionHistory;
import com.mdtlabs.coreplatform.common.model.entity.spice.RedRiskNotification;
import com.mdtlabs.coreplatform.common.model.enumeration.spice.PatientTransferStatus;
import com.mdtlabs.coreplatform.spiceservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceservice.assessment.repository.PatientAssessmentRepository;
import com.mdtlabs.coreplatform.spiceservice.bplog.repository.BpLogRepository;
import com.mdtlabs.coreplatform.spiceservice.bplog.service.BpLogService;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientComorbidityRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientComplicationRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientCurrentMedicationRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientDiagnosisRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientLifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.RedRiskNotificationRepository;
import com.mdtlabs.coreplatform.spiceservice.customizedmodules.repository.CustomizedModuleRepository;
import com.mdtlabs.coreplatform.spiceservice.glucoselog.repository.GlucoseLogRepository;
import com.mdtlabs.coreplatform.spiceservice.glucoselog.service.GlucoseLogService;
import com.mdtlabs.coreplatform.spiceservice.medicalreview.repository.MedicalReviewRepository;
import com.mdtlabs.coreplatform.spiceservice.mentalhealth.repository.MentalHealthRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.LifestyleService;
import com.mdtlabs.coreplatform.spiceservice.patient.repository.PatientPregnancyDetailsRepository;
import com.mdtlabs.coreplatform.spiceservice.patient.repository.PatientRepository;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.repository.PatientLabTestRepository;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.repository.PatientLabTestResultRepository;
import com.mdtlabs.coreplatform.spiceservice.patientmedicalcompliance.repository.PatientMedicalComplianceRepository;
import com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.repository.PatientNutritionLifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.patientsymptom.repository.PatientSymptomRepository;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.repository.PatientTrackerRepository;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttransfer.repository.PatientTransferRepository;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.repository.PatientTreatmentPlanRepository;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.repository.PatientVisitRepository;
import com.mdtlabs.coreplatform.spiceservice.prescription.repository.PrescriptionHistoryRepository;
import com.mdtlabs.coreplatform.spiceservice.prescription.repository.PrescriptionRepository;
import com.mdtlabs.coreplatform.spiceservice.screeninglog.service.ScreeningLogService;
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

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>
 * Patient Transfer Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 23, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PatientTransferServiceTest {

    @InjectMocks
    private PatientTransferServiceImpl patientTransferService;

    @Mock
    private PatientTransferRepository patientTransferRepository;

    @Mock
    private PatientTrackerRepository patientTrackerRepository;

    @Mock
    private DataSource dataSource;

    @Mock
    private UserApiInterface userApiInterface;

    private static MockedStatic<UserSelectedTenantContextHolder> userSelectedTenantContextHolder;

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
    private CustomizedModuleRepository customizedModuleRepository;

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
    private LifestyleService lifestyleService;

    @Mock
    private PatientNutritionLifestyleRepository nutritionLifestyleRepository;

    @Mock
    private MentalHealthRepository mentalHealthRepository;

    @Mock
    private MedicalReviewRepository medicalReviewRepository;

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



    @Test
    void createPatientTransfer() {
        //given
        PatientTransferRequestDTO patientTransferDto = TestDataProvider.getPatientTransferRequestDTO();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        PatientTransfer patientTransfer = new PatientTransfer();
        patientTransfer.setPatientTracker(new PatientTracker(1L));
        patientTransfer.setTransferSite(new Site(patientTransferDto.getTransferSite()));
        patientTransfer.setOldSite(new Site(patientTransferDto.getOldSite()));
        patientTransfer.setTransferTo(new User(patientTransferDto.getTransferTo()));
        patientTransfer.setTransferBy(new User(1L));
        patientTransfer.setTransferReason(patientTransferDto.getTransferReason());
        patientTransfer.setTransferStatus(PatientTransferStatus.PENDING);
        patientTransfer.setTenantId(1L);
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(patientTrackerRepository.findByIdAndIsDeleted(1L, Boolean.FALSE)).thenReturn(patientTracker);
        when(patientTransferRepository.findByPatientTrackIdAndTransferStatus(1L,
                PatientTransferStatus.PENDING)).thenReturn(null);
        when(patientTransferRepository.save(patientTransfer)).thenReturn(patientTransfer);

        //then
        patientTransferService.createPatientTransfer(patientTransferDto);
        verify(patientTransferRepository, atLeastOnce()).save(patientTransfer);
        TestDataProvider.cleanUp();
    }

    @Test
    void toVerifyCreatePatientTransfer() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setPatientStatus(Constants.SCREENED);

        //when
        when(patientTrackerRepository.findByIdAndIsDeleted(1L, Boolean.FALSE)).thenReturn(patientTracker);

        //then
        assertThrows(DataNotAcceptableException.class, () -> patientTransferService.validatePatientTransfer(requestDTO));
    }

    @Test
    void DataNotAcceptableException() {
        //given
        RequestDTO requestDto = new RequestDTO();
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setPatientStatus(Constants.SCREENED);
        PatientTracker tracker = TestDataProvider.getPatientTracker();
        PatientTransfer patientTransfer = TestDataProvider.getPatientTransfer();

        //when
        when(patientTrackerRepository.findByIdAndIsDeleted(1L, Boolean.FALSE)).thenReturn(null);
        when(patientTrackerRepository.findByIdAndIsDeleted(2L, Boolean.FALSE)).thenReturn(patientTracker);
        when(patientTrackerRepository.findByIdAndIsDeleted(3L, Boolean.FALSE)).thenReturn(tracker);
        when(patientTransferRepository.findByPatientTrackIdAndTransferStatus(1L,
                PatientTransferStatus.PENDING)).thenReturn(patientTransfer);

        //then
        assertThrows(DataNotAcceptableException.class, () -> patientTransferService.validatePatientTransfer(requestDto));
        assertThrows(DataNotAcceptableException.class, () -> patientTransferService.validatePatientTransfer(requestDTO));
        assertThrows(DataNotAcceptableException.class, () -> patientTransferService.validatePatientTransfer(requestDTO));
        assertThrows(DataNotAcceptableException.class, () -> patientTransferService.validatePatientTransfer(requestDTO));
    }

    @Test
    void toVerifyValidatePatientTransfer() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();

        //when
        when(patientTrackerRepository.findByIdAndIsDeleted(2L, Boolean.FALSE)).thenReturn(null);

        //then
        assertThrows(DataNotAcceptableException.class, () -> patientTransferService.validatePatientTransfer(requestDTO));
    }

    @Test
    void toVerifyValidatePatientTransferWithNull() {
        //given
        PatientTransfer patientTransfer = TestDataProvider.getPatientTransfer();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        RequestDTO requestDTO = TestDataProvider.getRequestDto();

        //when
        when(patientTrackerRepository.findByIdAndIsDeleted(1L, Boolean.FALSE)).thenReturn(patientTracker);
        when(patientTransferRepository.findByPatientTrackIdAndTransferStatus(1L,
                PatientTransferStatus.PENDING)).thenReturn(patientTransfer);

        //then
        assertThrows(DataNotAcceptableException.class, () -> patientTransferService.validatePatientTransfer(requestDTO));
    }

    @Test
    void getPatientTransferCount() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(patientTransferRepository.getPatientTransferCount(1L, 1L)).thenReturn(1L);

        //then
        Map<String, Long> patientTransferCount = patientTransferService.getPatientTransferCount(requestDTO);
        Assertions.assertNotNull(patientTransferCount);
        TestDataProvider.cleanUp();
    }

    @Test
    void toVerifyGetPatientTransferCount() {
        //given
        RequestDTO requestDTO = new RequestDTO();

        //then
        assertThrows(DataNotAcceptableException.class, () -> patientTransferService.getPatientTransferCount(requestDTO));
    }

    @Test
    @DisplayName("GetPatientTransferList Test")
    void getPatientTransferList() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        TestDataProvider.init();

        PatientTransfer patientTransferDate = TestDataProvider.getPatientTransfer();
        patientTransferDate.getPatientTracker().setLastName("lastName");

        List<PatientTransfer> incomingList = List.of(patientTransferDate);
        List<PatientTransfer> outgoingList = List.of(patientTransferDate);

        //when
        TestDataProvider.getStaticMock();
        when(patientTransferRepository.getIncomingList(1L, 1L, PatientTransferStatus.PENDING))
                .thenReturn(incomingList);
        when(patientTransferRepository.getOutgoingList(1L, 1L)).thenReturn(outgoingList);

        //then
        Map<String, Object> patientTransferList = patientTransferService.getPatientTransferList(requestDTO);
        Assertions.assertNotNull(patientTransferList);
        TestDataProvider.cleanUp();
    }

    @Test
    void toVerifyGetPatientTransferList() {
        //given
        RequestDTO requestDTO = new RequestDTO();

        //then
        assertThrows(DataNotAcceptableException.class, () -> patientTransferService.getPatientTransferList(requestDTO));
    }

    @Test
    void updatePatientTransferRemoved() {
        //given
        PatientTransferUpdateRequestDTO patientTransferDto = TestDataProvider.getPatientTransferUpdateRequestDTO();
        patientTransferDto.setTransferStatus(PatientTransferStatus.REMOVED);
        PatientTransfer patientTransfer = TestDataProvider.getPatientTransfer();
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(patientTransferRepository.findByIdAndIsDeleted(patientTransferDto.getId(), Boolean.FALSE))
                .thenReturn(patientTransfer);
        when(patientTransferRepository.save(patientTransfer)).thenReturn(patientTransfer);

        //then
        patientTransferService.updatePatientTransfer(patientTransferDto);
        verify(patientTransferRepository, atLeastOnce()).findByIdAndIsDeleted(patientTransferDto.getId(),
                Boolean.FALSE);
        verify(patientTransferRepository, atLeastOnce()).save(patientTransfer);


        patientTransferDto.setTransferStatus(PatientTransferStatus.ACCEPTED);

        //when
        TestDataProvider.getStaticMock();
        when(patientTransferRepository.findByIdAndIsDeleted(patientTransferDto.getId(), Boolean.FALSE))
                .thenReturn(patientTransfer);
        when(patientTransferRepository.save(patientTransfer)).thenReturn(patientTransfer);

        assertThrows(DataNotAcceptableException.class, () -> patientTransferService.updatePatientTransfer(patientTransferDto));

        TestDataProvider.cleanUp();
    }

    @Test
    void updatePatientTransferRejected() {
        //given
        PatientTransferUpdateRequestDTO patientTransferDto = TestDataProvider.getPatientTransferUpdateRequestDTO();
        patientTransferDto.setTransferStatus(PatientTransferStatus.REJECTED);
        patientTransferDto.setRejectReason("rejected");
        PatientTransfer patientTransfer = TestDataProvider.getPatientTransfer();
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(patientTransferRepository.findByIdAndIsDeleted(patientTransferDto.getId(), Boolean.FALSE))
                .thenReturn(patientTransfer);
        when(patientTransferRepository.save(patientTransfer)).thenReturn(patientTransfer);

        //then
        patientTransferService.updatePatientTransfer(patientTransferDto);
        verify(patientTransferRepository, atLeastOnce()).findByIdAndIsDeleted(patientTransferDto.getId(),
                Boolean.FALSE);
        verify(patientTransferRepository, atLeastOnce()).save(patientTransfer);
        TestDataProvider.cleanUp();
    }

    @Test
    void updatePatientTransferCanceled() {
        //given
        PatientTransferUpdateRequestDTO patientTransferDto = TestDataProvider.getPatientTransferUpdateRequestDTO();
        patientTransferDto.setTransferStatus(PatientTransferStatus.CANCELED);
        PatientTransfer patientTransfer = TestDataProvider.getPatientTransfer();
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(patientTransferRepository.findByIdAndIsDeleted(patientTransferDto.getId(), Boolean.FALSE))
                .thenReturn(patientTransfer);
        when(patientTransferRepository.save(patientTransfer)).thenReturn(patientTransfer);

        //then
        patientTransferService.updatePatientTransfer(patientTransferDto);
        verify(patientTransferRepository, atLeastOnce()).findByIdAndIsDeleted(patientTransferDto.getId(),
                Boolean.FALSE);
        verify(patientTransferRepository, atLeastOnce()).save(patientTransfer);
        TestDataProvider.cleanUp();
    }

    @Test
    void toVerifyUpdatePatientTransfer() {
        //given
        PatientTransferUpdateRequestDTO patientTransferDto = TestDataProvider.getPatientTransferUpdateRequestDTO();

        //when
        when(patientTransferRepository.findByIdAndIsDeleted(patientTransferDto.getId(), Boolean.FALSE))
                .thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> patientTransferService.updatePatientTransfer(patientTransferDto));
    }

    @Test
    void DataNotFoundException() {
        //given
        PatientTransferUpdateRequestDTO patientTransferDto = TestDataProvider.getPatientTransferUpdateRequestDTO();
        Site site = new Site();
        site.setId(1L);
        site.setDeleted(Boolean.TRUE);
        PatientTransfer patientTransfer = new PatientTransfer();
        patientTransfer.setId(1L);
        patientTransfer.setTransferSite(site);

        //when
        when(patientTransferRepository.findByIdAndIsDeleted(1L, Boolean.FALSE)).thenReturn(patientTransfer);

        //then
        assertThrows(DataNotFoundException.class, () -> patientTransferService.updatePatientTransfer(patientTransferDto));
    }

    @Test
    void updatePatientRelatedRecords() {
        PatientTransfer patientTransfer = new PatientTransfer();
        patientTransfer.setPatientTracker(new PatientTracker());
        patientTransfer.getPatientTracker().setId(1L);

        userSelectedTenantContextHolder = mockStatic(UserSelectedTenantContextHolder.class);

        Patient exisitingPatient = TestDataProvider.getPatient();
        List<BpLog> bpLogsList = List.of(TestDataProvider.getBpLog());
        List<GlucoseLog> glucoseLogs = List.of(TestDataProvider.getGlucoseLog());
        List<RedRiskNotification> redRiskNotifications = List.of(TestDataProvider.getRedRiskNotification());
        List<CustomizedModule> customizedModules = List.of(TestDataProvider.getCustomizedModule());
        List<Prescription> prescriptions = List.of(TestDataProvider.getPrescription());
        List<PrescriptionHistory> prescriptionHistories = List.of(TestDataProvider.getPrescriptionHistory());
        List<PatientLabTest> patientLabTests = List.of(TestDataProvider.getPatientLabTest());
        List<PatientLabTestResult> patientLabTestResults = List.of(TestDataProvider.getPatientLabTestResultData());
        List<PatientLifestyle> patientLifestyles = List.of(TestDataProvider.getPatientLifestyle());
        List<PatientNutritionLifestyle> lifestyles = List.of(TestDataProvider.getPatientNutritionLifeStyle());
        List<MentalHealth> mentalHealths = List.of(TestDataProvider.getMentalHealth());
        List<PatientMedicalReview> medicalReviews = List.of(TestDataProvider.getPatientMedicalReview());
        List<PatientTransfer> patientTransfers = List.of(TestDataProvider.getPatientTransfer());
        List<PatientAssessment> patientAssessments = List.of(TestDataProvider.getPatientAssessment());
        List<PatientVisit> patientVisits = List.of(TestDataProvider.getPatientVisit());
        List<PatientComorbidity> comorbidityList = List.of(TestDataProvider.getPatientComorbidity());
        List<PatientComplication> complications = List.of(TestDataProvider.getPatientComplication());
        List<PatientCurrentMedication> currentMedications = List.of(TestDataProvider.getPatientCurrentMedication());
        List<PatientDiagnosis> patientDiagnoses = List.of(TestDataProvider.getPatientDiagnosis());
        List<PatientMedicalCompliance> medicalComplianceList = List.of(TestDataProvider.getPatientMedicalCompliance());
        PatientPregnancyDetails pregnancyDetails = TestDataProvider.getPatientPregnancyDetails();
        List<PatientSymptom> patientSymptoms = List.of(TestDataProvider.getPatientSymptom());
        PatientTreatmentPlan treatmentPlan = TestDataProvider.getPatientTreatmentPlan();

        when(glucoseLogRepository.findByPatientTrackId(anyLong())).thenReturn(glucoseLogs);
        when(glucoseLogRepository.saveAll(glucoseLogs)).thenReturn(glucoseLogs);

        when(bpLogRepository.findByPatientTrackId(anyLong())).thenReturn(bpLogsList);
        when(bpLogRepository.saveAll(bpLogsList)).thenReturn(bpLogsList);

        when(glucoseLogRepository.findByPatientTrackId(anyLong())).thenReturn(glucoseLogs);
        when(glucoseLogRepository.saveAll(glucoseLogs)).thenReturn(glucoseLogs);

        when(redRiskNotificationRepository.findByPatientTrackId(anyLong()))
                .thenReturn(redRiskNotifications);
        when(redRiskNotificationRepository.saveAll(redRiskNotifications)).thenReturn(redRiskNotifications);

        when(customizedModuleRepository.findByPatientTrackId(anyLong()))
                .thenReturn(customizedModules);
        when(customizedModuleRepository.saveAll(customizedModules)).thenReturn(customizedModules);

        when(prescriptionRepository.findBypatientTrackId(anyLong())).thenReturn(prescriptions);
        when(prescriptionRepository.saveAll(prescriptions)).thenReturn(prescriptions);

        when(prescriptionHistoryRepository.findByPatientTrackId(anyLong()))
                .thenReturn(prescriptionHistories);
        when(prescriptionHistoryRepository.saveAll(prescriptionHistories)).thenReturn(prescriptionHistories);

        when(labTestRepository.findByPatientTrackId(anyLong())).thenReturn(patientLabTests);
        when(labTestRepository.saveAll(patientLabTests)).thenReturn(patientLabTests);

        when(labTestResultRepository.findByPatientTrackId(anyLong()))
                .thenReturn(patientLabTestResults);
        when(labTestResultRepository.saveAll(patientLabTestResults)).thenReturn(patientLabTestResults);

        when(patientLifestyleRepository.findByPatientTrackId(anyLong()))
                .thenReturn(patientLifestyles);
        when(patientLifestyleRepository.saveAll(patientLifestyles)).thenReturn(patientLifestyles);

        when(nutritionLifestyleRepository.findByPatientTrackId(anyLong())).thenReturn(lifestyles);
        when(nutritionLifestyleRepository.saveAll(lifestyles)).thenReturn(lifestyles);

        when(mentalHealthRepository.findByPatientTrackId(anyLong())).thenReturn(mentalHealths);
        when(mentalHealthRepository.saveAll(mentalHealths)).thenReturn(mentalHealths);

        when(medicalReviewRepository.findByPatientTrackId(anyLong())).thenReturn(medicalReviews);
        when(medicalReviewRepository.saveAll(medicalReviews)).thenReturn(medicalReviews);

        when(patientTransferRepository.findByPatientTrackId(anyLong())).thenReturn(patientTransfers);
        when(patientTransferRepository.saveAll(patientTransfers)).thenReturn(patientTransfers);

        when(patientAssessmentRepository.findByPatientTrackId(anyLong())).thenReturn(patientAssessments);
        when(patientAssessmentRepository.saveAll(patientAssessments)).thenReturn(patientAssessments);

        when(patientVisitRepository.findByPatientTrackId(anyLong())).thenReturn(patientVisits);
        when(patientVisitRepository.saveAll(patientVisits)).thenReturn(patientVisits);

        when(comorbidityRepository.findByPatientTrackId(anyLong())).thenReturn(comorbidityList);
        when(comorbidityRepository.saveAll(comorbidityList)).thenReturn(comorbidityList);

        when(complicationRepository.findByPatientTrackId(anyLong())).thenReturn(complications);
        when(complicationRepository.saveAll(complications)).thenReturn(complications);

        when(medicationRepository.findByPatientTrackIdAndIsDeleted(anyLong(),
                anyBoolean())).thenReturn(currentMedications);
        when(medicationRepository.saveAll(currentMedications)).thenReturn(currentMedications);

        when(diagnosisRepository.findByPatientTrackId(anyLong())).thenReturn(patientDiagnoses);
        when(diagnosisRepository.saveAll(patientDiagnoses)).thenReturn(patientDiagnoses);

        when(medicalComplianceRepository.findByPatientTrackId(anyLong())).thenReturn(medicalComplianceList);
        when(medicalComplianceRepository.saveAll(medicalComplianceList)).thenReturn(medicalComplianceList);

        when(pregnancyRepository.findByPatientTrackId(anyLong())).thenReturn(null);
        when(pregnancyRepository.findByPatientTrackId(anyLong())).thenReturn(List.of(pregnancyDetails));
        when(pregnancyRepository.save(pregnancyDetails)).thenReturn(pregnancyDetails);

        when(symptomRepository.findByPatientTrackId(anyLong())).thenReturn(patientSymptoms);
        when(symptomRepository.saveAll(patientSymptoms)).thenReturn(patientSymptoms);

        when(treatmentPlanRepository.findByPatientTrackId(anyLong())).thenReturn(List.of(treatmentPlan));
        when(treatmentPlanRepository.save(treatmentPlan)).thenReturn(treatmentPlan);

        userSelectedTenantContextHolder.when(UserSelectedTenantContextHolder::get).thenReturn(1L);
        patientTransferService.updatePatientRelatedRecords(patientTransfer);
        userSelectedTenantContextHolder.close();
        assertNotNull(patientTransfer);
    }
}