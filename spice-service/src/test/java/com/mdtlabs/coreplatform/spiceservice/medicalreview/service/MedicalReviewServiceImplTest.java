package com.mdtlabs.coreplatform.spiceservice.medicalreview.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.ContinuousMedicalReviewDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.InitialMedicalReviewDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RedRiskDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.Culture;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complaints;
import com.mdtlabs.coreplatform.common.model.entity.spice.Patient;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComorbidity;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComplication;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientCurrentMedication;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientDiagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalReview;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;
import com.mdtlabs.coreplatform.common.model.entity.spice.PhysicalExamination;
import com.mdtlabs.coreplatform.common.model.entity.spice.Prescription;
import com.mdtlabs.coreplatform.common.model.entity.spice.RedRiskNotification;
import com.mdtlabs.coreplatform.spiceservice.common.RedRiskService;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.PatientMedicalReviewMapper;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientComorbidityRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientComplicationRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientCurrentMedicationRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientDiagnosisRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientLifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.RedRiskNotificationRepository;
import com.mdtlabs.coreplatform.spiceservice.medicalreview.repository.MedicalReviewRepository;
import com.mdtlabs.coreplatform.spiceservice.medicalreview.service.impl.MedicalReviewServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ComplaintsService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CultureService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.PhysicalExaminationService;
import com.mdtlabs.coreplatform.spiceservice.patient.repository.PatientRepository;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.service.PatientLabTestService;
import com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.service.PatientNutritionLifestyleService;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.service.PatientVisitService;
import com.mdtlabs.coreplatform.spiceservice.prescription.service.PrescriptionService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>
 * MedicalReviewServiceImplTest class used to test all possible positive
 * and negative cases for all methods and conditions used in MedicalReviewServiceImpl class.
 * </p>
 *
 * @author Jaganathan created on Feb 8, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MedicalReviewServiceImplTest {

    @InjectMocks
    private MedicalReviewServiceImpl medicalReviewService;

    @Mock
    private MedicalReviewRepository medicalReviewRepository;

    @Mock
    private PatientTrackerService patientTrackerService;

    @Mock
    private PrescriptionService prescriptionService;

    @Mock
    private PatientLabTestService patientLabTestService;

    @Mock
    private PatientNutritionLifestyleService nutritionLifestyleService;

    @Mock
    private RedRiskService redRiskService;

    @Mock
    private PatientComorbidityRepository comorbidityRepository;

    @Mock
    private PatientMedicalReviewMapper medicalReviewMapper;

    @Mock
    private PatientCurrentMedicationRepository medicationRepository;

    @Mock
    private PatientComplicationRepository complicationRepository;

    @Mock
    private PatientDiagnosisRepository diagnosisRepository;

    @Mock
    private PatientLifestyleRepository lifestyleRepository;

    @Mock
    private PatientVisitService patientVisitService;

    @Mock
    private PatientTreatmentPlanService treatmentPlanService;

    @Mock
    private ComplaintsService complaintService;

    @Mock
    private PhysicalExaminationService physicalExaminationService;

    @Mock
    private PatientComorbidityRepository patientComorbidityRepository;

    @Mock
    private PatientComplicationRepository patientComplicationRepository;

    @Mock
    private RedRiskNotificationRepository redRiskNotificationRepository;

    @Mock
    private CultureService cultureService;

    @Mock
    private PatientTreatmentPlanService patientTreatmentPlanService;

    @Mock
    private PatientDiagnosisRepository patientDiagnosisRepository;

    @Mock PatientLifestyleRepository patientLifestyleRepository;

    @Mock
    private PatientRepository patientRepository;

    @Test
    void BadRequestException() {
        //given
        MedicalReviewDTO newMedicalReviewDTO = new MedicalReviewDTO();
        MedicalReviewDTO medicalReviewDTO = new MedicalReviewDTO();
        medicalReviewDTO.setPatientTrackId(1L);

        //then
        Assertions.assertThrows(BadRequestException.class,
                () -> medicalReviewService.addMedicalReview(newMedicalReviewDTO));
        Assertions.assertThrows(BadRequestException.class,
                () -> medicalReviewService.addMedicalReview(medicalReviewDTO));
    }

    @Test
    void DataNotAcceptableException() {
        //given
        MedicalReviewDTO medicalReviewDTO = new MedicalReviewDTO();
        medicalReviewDTO.setPatientTrackId(1L);
        medicalReviewDTO.setContinuousMedicalReview(new ContinuousMedicalReviewDTO());
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();

        //when
        when(patientTrackerService.getPatientTrackerById(medicalReviewDTO.getPatientTrackId()))
                .thenReturn(patientTracker);

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> medicalReviewService.addMedicalReview(medicalReviewDTO));
        verify(patientTrackerService, atLeastOnce()).getPatientTrackerById(medicalReviewDTO.getPatientTrackId());

    }

    @Test
    void addMedicalReview() {
        //given
        LocalDate localDate = LocalDate.of(2023, 3, 8);
        Date date = java.sql.Date.valueOf(localDate);
        InitialMedicalReviewDTO initialMedicalReviewDTO = TestDataProvider.getInitialMedicalReviewDTO();
        initialMedicalReviewDTO.setComorbidities(null);
        initialMedicalReviewDTO.setDiagnosis(null);
        MedicalReviewDTO medicalReviewDTO = TestDataProvider.getMedicalReviewDTO();
        medicalReviewDTO.setInitialMedicalReview(initialMedicalReviewDTO);
        medicalReviewDTO.setContinuousMedicalReview(new ContinuousMedicalReviewDTO());
        medicalReviewDTO.getInitialMedicalReview().setIsPregnant(Constants.BOOLEAN_TRUE);
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        PatientDiagnosis patientDiagnosis = TestDataProvider.getPatientDiagnosis();
        patientDiagnosis.setDiabetesDiagControlledType(Constants.DIABETES_MELLITUS_TYPE_1);
        patientDiagnosis.setDiabetesPatientType(Constants.TYPE);
        Patient patient = TestDataProvider.getPatient();
        PatientMedicalReview medicalReview = TestDataProvider.getPatientMedicalReview();
        medicalReview.setCreatedAt(date);
        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        PatientTreatmentPlan treatmentPlan = TestDataProvider.getPatientTreatmentPlan();

        //when
        when(patientTrackerService.getPatientTrackerById(medicalReviewDTO.getPatientTrackId()))
                .thenReturn(patientTracker);
        when(patientDiagnosisRepository.findByPatientTrackIdAndIsActiveAndIsDeleted(patientTracker.getId(),
                Constants.BOOLEAN_TRUE, Constants.BOOLEAN_FALSE)).thenReturn(patientDiagnosis);
        when(redRiskService.getPatientRiskLevel(patientTracker, patientDiagnosis, new RedRiskDTO(12,
                "", ""))).thenReturn("");
        //  when(patientService.getPatientById(patientTracker.getPatientId())).thenReturn(patient);
        when(medicalReviewMapper.setMedicalReviewDto(medicalReviewDTO)).thenReturn(medicalReview);
        when(medicalReviewRepository.save(medicalReview)).thenReturn(medicalReview);
        when(patientVisitService.getPatientVisitById(medicalReviewDTO.getPatientVisitId())).thenReturn(patientVisit);
        when(patientVisitService.updatePatientVisit(patientVisit)).thenReturn(patientVisit);
        when(treatmentPlanService.getPatientTreatmentPlan(patientTracker.getId())).thenReturn(treatmentPlan);
        when(treatmentPlanService.getTreatmentPlanFollowupDate(treatmentPlan.getMedicalReviewFrequency(),
                Constants.DEFAULT)).thenReturn(date);
        when(patientTrackerService.addOrUpdatePatientTracker(any())).thenReturn(patientTracker);
        when(patientRepository.findByIdAndIsDeleted(anyLong(), anyBoolean())).thenReturn(patient);


        //then
        medicalReviewService.addMedicalReview(medicalReviewDTO);
        verify(patientTrackerService, atLeastOnce()).getPatientTrackerById(medicalReviewDTO.getPatientTrackId());
        verify(patientDiagnosisRepository, atLeastOnce()).findByPatientTrackIdAndIsActiveAndIsDeleted(patientTracker
                .getId(), Constants.BOOLEAN_TRUE, Constants.BOOLEAN_FALSE);
        verify(patientTrackerService, atLeastOnce()).addOrUpdatePatientTracker(patientTracker);
        verify(medicalReviewRepository, atLeastOnce()).save(medicalReview);
        verify(patientVisitService, atLeastOnce()).getPatientVisitById(medicalReviewDTO.getPatientVisitId());
        verify(patientVisitService, atLeastOnce()).updatePatientVisit(patientVisit);

        //then
        medicalReviewDTO.getInitialMedicalReview().setIsPregnant(null);
        medicalReviewService.addMedicalReview(medicalReviewDTO);
        verify(patientTrackerService, atLeastOnce()).getPatientTrackerById(medicalReviewDTO.getPatientTrackId());
        verify(patientDiagnosisRepository, atLeastOnce()).findByPatientTrackIdAndIsActiveAndIsDeleted(patientTracker
                .getId(), Constants.BOOLEAN_TRUE, Constants.BOOLEAN_FALSE);
        verify(patientTrackerService, atLeastOnce()).addOrUpdatePatientTracker(patientTracker);
        verify(medicalReviewRepository, atLeastOnce()).save(medicalReview);
        verify(patientVisitService, atLeastOnce()).getPatientVisitById(medicalReviewDTO.getPatientVisitId());
        verify(patientVisitService, atLeastOnce()).updatePatientVisit(patientVisit);

        medicalReviewDTO.getInitialMedicalReview().setIsPregnant(Constants.BOOLEAN_TRUE);

        when(patientRepository.findByIdAndIsDeleted(anyLong(), anyBoolean())).thenReturn(null);
        Assertions.assertThrows(DataNotFoundException.class, () -> medicalReviewService.addMedicalReview(medicalReviewDTO));
    }

    @Test
    @DisplayName("AddMedicalReviewWithNull Test")
    void addMedicalReviewWithNull() {
        //given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setPatientStatus(Constants.SCREENED);
        MedicalReviewDTO medicalReviewDTO = TestDataProvider.getMedicalReviewDTO();
        medicalReviewDTO.setInitialMedicalReview(TestDataProvider.getInitialMedicalReviewDTO());
        medicalReviewDTO.setContinuousMedicalReview(new ContinuousMedicalReviewDTO());
        PatientMedicalReview medicalReview = TestDataProvider.getPatientMedicalReview();
        PatientDiagnosis patientDiagnosis = TestDataProvider.getPatientDiagnosis();
        Set<PatientCurrentMedication> currentMedications = Set.of(TestDataProvider.getPatientCurrentMedication());
        List<PatientCurrentMedication> currentMedicationList = List.of(TestDataProvider.getPatientCurrentMedication());
        PatientComorbidity comorbidity = TestDataProvider.getPatientComorbidity();
        Set<PatientComorbidity> comorbiditySet = Set.of(comorbidity);
        List<PatientComorbidity> comorbidityList = List.of(comorbidity);
        PatientComplication patientComplication = TestDataProvider.getPatientComplication();
        Set<PatientComplication> complications = Set.of(patientComplication);
        List<PatientComplication> complicationList = List.of(patientComplication);
        Set<PatientLifestyle> lifestyles = Set.of(TestDataProvider.getPatientLifestyle());
        List<PatientLifestyle> lifestyleList = List.of(TestDataProvider.getPatientLifestyle());
        PatientVisit patientVisit = TestDataProvider.getPatientVisit();

        //when
        when(patientTrackerService.getPatientTrackerById(medicalReviewDTO.getPatientTrackId()))
                .thenReturn(patientTracker);
        when(redRiskService.getPatientRiskLevel(patientTracker, patientDiagnosis, new RedRiskDTO(12,
                "", ""))).thenReturn("");
        when(medicationRepository.saveAll(currentMedications)).thenReturn(currentMedicationList);
        when(comorbidityRepository.saveAll(comorbiditySet)).thenReturn(comorbidityList);
        when(complicationRepository.saveAll(complications)).thenReturn(complicationList);
        when(diagnosisRepository.save(patientDiagnosis)).thenReturn(patientDiagnosis);
        when(lifestyleRepository.saveAll(lifestyles)).thenReturn(lifestyleList);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        when(medicalReviewMapper.setMedicalReviewDto(medicalReviewDTO)).thenReturn(medicalReview);
        when(medicalReviewRepository.save(medicalReview)).thenReturn(medicalReview);
        when(patientVisitService.getPatientVisitById(medicalReviewDTO.getPatientVisitId())).thenReturn(patientVisit);
        when(patientVisitService.updatePatientVisit(patientVisit)).thenReturn(patientVisit);
        when(treatmentPlanService.getPatientTreatmentPlan(patientTracker.getId())).thenReturn(null);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);

        //then
        medicalReviewService.addMedicalReview(medicalReviewDTO);
        verify(patientTrackerService, atLeastOnce()).getPatientTrackerById(medicalReviewDTO.getPatientTrackId());
        verify(patientDiagnosisRepository, atLeastOnce()).findByPatientTrackIdAndIsActiveAndIsDeleted(patientTracker
                .getId(), Constants.BOOLEAN_TRUE, Constants.BOOLEAN_FALSE);
        verify(patientTrackerService, atLeastOnce()).addOrUpdatePatientTracker(patientTracker);
        verify(medicalReviewRepository, atLeastOnce()).save(medicalReview);
        verify(patientVisitService, atLeastOnce()).getPatientVisitById(medicalReviewDTO.getPatientVisitId());
        verify(patientVisitService, atLeastOnce()).updatePatientVisit(patientVisit);
    }

    @Test
    @DisplayName("AddMedicalReviewWithNull Test")
    void testAddMedicalReviewWithNull() {
        //given
        Complaints complaints = TestDataProvider.getComplaints();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setRedRiskPatient(Boolean.TRUE);
        MedicalReviewDTO medicalReviewDTO = TestDataProvider.getMedicalReviewDTO();
        medicalReviewDTO.setInitialMedicalReview(TestDataProvider.getInitialMedicalReviewDTO());
        medicalReviewDTO.setContinuousMedicalReview(TestDataProvider.getContinuousMedicalReviewDTO());
        PatientMedicalReview medicalReview = TestDataProvider.getPatientMedicalReview();
        PatientDiagnosis patientDiagnosis = TestDataProvider.getPatientDiagnosis();
        Set<PatientCurrentMedication> currentMedications = Set.of(TestDataProvider.getPatientCurrentMedication());
        List<PatientCurrentMedication> currentMedicationList = List.of(TestDataProvider.getPatientCurrentMedication());
        PatientComorbidity comorbidity = TestDataProvider.getPatientComorbidity();
        Set<PatientComorbidity> comorbiditySet = Set.of(comorbidity);
        List<PatientComorbidity> comorbidityList = List.of(comorbidity);
        PatientComplication patientComplication = TestDataProvider.getPatientComplication();
        Set<PatientComplication> complications = Set.of(patientComplication);
        List<PatientComplication> complicationList = List.of(patientComplication);
        Set<PatientLifestyle> lifestyles = Set.of(TestDataProvider.getPatientLifestyle());
        List<PatientLifestyle> lifestyleList = List.of(TestDataProvider.getPatientLifestyle());
        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        PatientTreatmentPlan treatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        PhysicalExamination physicalExamination = TestDataProvider.getPhysicalExamination();
        List<RedRiskNotification> redRiskNotification = List.of(TestDataProvider.getRedRiskNotification());

        //when
        when(patientTrackerService.getPatientTrackerById(medicalReviewDTO.getPatientTrackId()))
                .thenReturn(patientTracker);
        when(redRiskService.getPatientRiskLevel(patientTracker, patientDiagnosis, new RedRiskDTO(12,
                "", ""))).thenReturn("");
        when(medicationRepository.saveAll(currentMedications)).thenReturn(currentMedicationList);
        when(comorbidityRepository.saveAll(comorbiditySet)).thenReturn(comorbidityList);
        when(complicationRepository.saveAll(complications)).thenReturn(complicationList);
        when(diagnosisRepository.save(patientDiagnosis)).thenReturn(patientDiagnosis);
        when(lifestyleRepository.saveAll(lifestyles)).thenReturn(lifestyleList);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        when(medicalReviewMapper.setMedicalReviewDto(medicalReviewDTO)).thenReturn(medicalReview);
        when(medicalReviewRepository.save(medicalReview)).thenReturn(medicalReview);
        when(patientVisitService.getPatientVisitById(medicalReviewDTO.getPatientVisitId())).thenReturn(patientVisit);
        when(patientVisitService.updatePatientVisit(patientVisit)).thenReturn(patientVisit);
        when(treatmentPlanService.getPatientTreatmentPlan(patientTracker.getId())).thenReturn(treatmentPlan);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        when(complaintService.getComplaintsByIds(medicalReviewDTO.getContinuousMedicalReview().getComplaints()))
                .thenReturn(Set.of(complaints));
        when(physicalExaminationService.getPhysicalExaminationByIds(medicalReviewDTO.getContinuousMedicalReview()
                .getPhysicalExams())).thenReturn(Set.of(physicalExamination));
        when(patientComorbidityRepository.saveAll(comorbiditySet)).thenReturn(comorbidityList);
        when(patientComplicationRepository.saveAll(complications)).thenReturn(complicationList);
        when(redRiskNotificationRepository
                .findByPatientTrackIdAndIsDeletedFalseAndStatusIgnoreCase(patientTracker.getId(), Constants.NEW))
                .thenReturn(redRiskNotification);
        when(redRiskNotificationRepository.saveAll(redRiskNotification)).thenReturn(redRiskNotification);

        //then
        medicalReviewService.addMedicalReview(medicalReviewDTO);
        verify(patientTrackerService, atLeastOnce()).getPatientTrackerById(medicalReviewDTO.getPatientTrackId());
        verify(patientDiagnosisRepository, atLeastOnce()).findByPatientTrackIdAndIsActiveAndIsDeleted(patientTracker
                .getId(), Constants.BOOLEAN_TRUE, Constants.BOOLEAN_FALSE);
        verify(patientTrackerService, atLeastOnce()).addOrUpdatePatientTracker(patientTracker);
        verify(medicalReviewRepository, atLeastOnce()).save(medicalReview);
        verify(patientVisitService, atLeastOnce()).getPatientVisitById(medicalReviewDTO.getPatientVisitId());
        verify(patientVisitService, atLeastOnce()).updatePatientVisit(patientVisit);
    }

    @Test
    @DisplayName("NullCheckWithTrackerIdNull Test")
    void NullCheckWithTrackIdNull() {
        //given
        RequestDTO requestDTO = new RequestDTO();

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> medicalReviewService.getMedicalReviewHistory(requestDTO));
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> medicalReviewService.getMedicalReviewSummaryHistory(requestDTO));
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> medicalReviewService.getPrescriptionAndLabtestCount(requestDTO));
    }

    @Test
    @DisplayName("GetPrescriptionAndLabTestCount Test")
    void getPrescriptionAndLabTestCountTest() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        Map<String, Integer> response = Map.of("nutritionLifestyleReviewedCount",
                10, "nonReviewedTestCount", 10, "prescriptionDaysCompletedCount", 0);
        Date endDate = new Date(12 / 12 / 12);
        Long patientTrackId = 1L;
        int count = 10;

        //when
        when(prescriptionService.getPrescriptionCount(endDate, patientTrackId)).thenReturn(count);
        when(patientLabTestService.getLabTestCount(patientTrackId)).thenReturn(count);
        when(nutritionLifestyleService.getNutritionLifestyleReviewedCount(patientTrackId)).thenReturn(count);

        //then
        Map<String, Integer> actualResponse = medicalReviewService.getPrescriptionAndLabtestCount(requestDTO);
        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(response, actualResponse);
    }

    @Test
    @DisplayName("GetMedicalReviewSummaryHistoryWithInvalidVisitId Test")
    void getMedicalReviewSummaryHistoryWithInvalidVisitId() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setPatientTrackId(null);

        //then
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> medicalReviewService.getMedicalReviewSummaryHistory(requestDTO));
    }

    @Test
    void testGetMedicalReviewSummaryHistory() {
        //given
        TestDataProvider.init();
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setLatestRequired(Constants.BOOLEAN_TRUE);
        requestDTO.setMedicalReviewSummary(Constants.BOOLEAN_TRUE);
        requestDTO.setDetailedSummaryRequired(Constants.BOOLEAN_FALSE);
        PhysicalExamination physicalExamination = TestDataProvider.getPhysicalExamination();
        physicalExamination.setName(Constants.NAME);
        Complaints complaints = TestDataProvider.getComplaints();
        complaints.setName(Constants.NAME);
        List<PatientVisit> patientVisits = List.of(TestDataProvider.getPatientVisit());
        PatientMedicalReview patientMedicalReview = TestDataProvider.getPatientMedicalReview();
        patientMedicalReview.setPhysicalExams(Set.of(physicalExamination));
        patientMedicalReview.setComplaints(Set.of(complaints));
        List<PatientMedicalReview> patientMedicalReviews = List.of(patientMedicalReview);
        List<PatientLabTest> patientLabTests = List.of(TestDataProvider.getPatientLabTest());
        Map<String, Map<Long, String>> cultureMap = Map.of(Constants.CULTURE_VALUE_PHYSICAL_EXAMINATION,
                Map.of(1L, Constants.CULTURE_VALUE), Constants.CULTURE_VALUE_COMPLAINTS,
                Map.of(1L, Constants.CULTURE_VALUE));
        Constants.CULTURE_VALUES_MAP = Map.of(1L, cultureMap);

        //when
        TestDataProvider.getStaticMock();
        when(cultureService.loadCultureValues()).thenReturn(1L);
        when(patientVisitService.getPatientVisitDates(1L, null,
                Constants.BOOLEAN_TRUE, null)).thenReturn(patientVisits);
        when(medicalReviewRepository.getPatientMedicalReview(
                requestDTO.getPatientTrackId(), requestDTO.getPatientVisitId())).thenReturn(patientMedicalReviews);
        when(medicalReviewRepository.getPatientMedicalReview(
                requestDTO.getPatientTrackId(), requestDTO.getPatientVisitId())).thenReturn(patientMedicalReviews);
        when(patientLabTestService.getPatientLabTest(requestDTO.getPatientTrackId(), requestDTO.getPatientVisitId()))
                .thenReturn(patientLabTests);

        //then
        MedicalReviewResponseDTO responseDTO = medicalReviewService.getMedicalReviewSummaryHistory(requestDTO);
        Assertions.assertEquals(1L, responseDTO.getMedicalReviews().get(0).getPatientVisitId());
        Assertions.assertNotNull(responseDTO);
        Assertions.assertFalse(responseDTO.getMedicalReviews().isEmpty());
        Assertions.assertNull(responseDTO.getPatientDetails());
        Assertions.assertTrue(responseDTO.getPrescriptions().isEmpty());
        Assertions.assertNotNull(responseDTO.getMedicalReviews().get(0).getPhysicalExams());
        TestDataProvider.cleanUp();
    }

    @Test
    void getMedicalReviewHistory() {
        //given
        TestDataProvider.init();
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setPatientVisitId(null);
        requestDTO.setLatestRequired(Constants.BOOLEAN_TRUE);
        requestDTO.setMedicalReviewSummary(Constants.BOOLEAN_TRUE);
        requestDTO.setDetailedSummaryRequired(Constants.BOOLEAN_FALSE);
        List<PatientVisit> patientVisits = List.of(TestDataProvider.getPatientVisit());
        Culture culture = TestDataProvider.getCulture();

        //when
        TestDataProvider.getStaticMockNull();
        when(cultureService.loadCultureValues()).thenReturn(1L);
        when(patientVisitService.getPatientVisitDates(requestDTO.getPatientTrackId(), null,
                Constants.BOOLEAN_TRUE, null)).thenReturn(patientVisits);
        when(medicalReviewRepository.getPatientMedicalReview(requestDTO.getPatientTrackId(),
                requestDTO.getPatientVisitId())).thenReturn(null);
        when(cultureService.getCultureByName(Constants.DEFAULT_CULTURE_VALUE)).thenReturn(culture);
        Map<String, Map<Long, String>> cultureMap = Map.of(Constants.CULTURE_VALUE_PHYSICAL_EXAMINATION,
                Map.of(1L, Constants.CULTURE_VALUE), Constants.CULTURE_VALUE_COMPLAINTS,
                Map.of(1L, Constants.CULTURE_VALUE));
        Constants.CULTURE_VALUES_MAP = Map.of(1L, cultureMap);

        //then
        MedicalReviewResponseDTO responseDTO = medicalReviewService.getMedicalReviewHistory(requestDTO);
        Assertions.assertNotNull(responseDTO.getPatientReviewDates());
        Assertions.assertTrue(responseDTO.getPatientMedicalReview().isEmpty());
        Assertions.assertNotNull(responseDTO);
        Assertions.assertNull(responseDTO.getPrescriptions());
        TestDataProvider.cleanUp();
    }

    @Test
    void toVerifyGetMedicalReviewSummaryHistory() {
        //given
        TestDataProvider.init();
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setLatestRequired(Constants.BOOLEAN_FALSE);
        requestDTO.setMedicalReviewSummary(Constants.BOOLEAN_TRUE);
        requestDTO.setDetailedSummaryRequired(Constants.BOOLEAN_TRUE);
        PatientVisit patientVisit = TestDataProvider.getPatientVisit();
        patientVisit.setMedicalReview(Constants.BOOLEAN_FALSE);
        patientVisit.setInvestigation(Constants.BOOLEAN_FALSE);
        Prescription prescription = TestDataProvider.getPrescription();
        List<Prescription> prescriptions = List.of(prescription);
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        PatientTreatmentPlan patientTreatmentPlan = TestDataProvider.getPatientTreatmentPlan();

        //when
        TestDataProvider.getStaticMock();
        when(patientVisitService.getPatientVisitById(requestDTO.getPatientVisitId())).thenReturn(patientVisit);
        when(prescriptionService.findByPatientTrackIdAndPatientVisitIdAndIsDeleted(requestDTO.getPatientTrackId(),
                requestDTO.getPatientVisitId(), Constants.BOOLEAN_FALSE)).thenReturn(prescriptions);
        when(patientTrackerService.getPatientTrackerById(requestDTO.getPatientTrackId())).thenReturn(patientTracker);
        when(patientTreatmentPlanService.getPatientTreatmentPlanDetails(requestDTO.getPatientTrackId()))
                .thenReturn(patientTreatmentPlan);

        //then
        MedicalReviewResponseDTO responseDTO = medicalReviewService.getMedicalReviewSummaryHistory(requestDTO);
        Assertions.assertNotNull(responseDTO);
        Assertions.assertNotNull(responseDTO.getReviewerDetails());
        Assertions.assertTrue(responseDTO.getInvestigations().isEmpty());
        Assertions.assertTrue(responseDTO.getPatientReviewDates().isEmpty());
        Assertions.assertNull(responseDTO.getPatientDetails().getConfirmDiagnosis());
        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("RemoveMedicalReview Test")
    void removeMedicalReview() {
        medicalReviewService.removeMedicalReview(1L);

        PatientMedicalReview medicalReview = TestDataProvider.getPatientMedicalReview();
        List<PatientMedicalReview> patientMedicalReviewList = List.of(medicalReview);
        when(medicalReviewRepository.findByPatientTrackId(anyLong())).thenReturn(patientMedicalReviewList);
        medicalReviewService.removeMedicalReview(1L);
        Assertions.assertTrue(patientMedicalReviewList.get(0).isDeleted());

    }
}


