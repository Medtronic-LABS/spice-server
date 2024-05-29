package com.mdtlabs.coreplatform.spiceservice.staticdata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.MetaFormDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteResponseDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.County;
import com.mdtlabs.coreplatform.common.model.entity.Culture;
import com.mdtlabs.coreplatform.common.model.entity.Program;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.Subcounty;
import com.mdtlabs.coreplatform.common.model.entity.spice.Comorbidity;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complaints;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complication;
import com.mdtlabs.coreplatform.common.model.entity.spice.CurrentMedication;
import com.mdtlabs.coreplatform.common.model.entity.spice.Diagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.DosageForm;
import com.mdtlabs.coreplatform.common.model.entity.spice.DosageFrequency;
import com.mdtlabs.coreplatform.common.model.entity.spice.FormMetaUi;
import com.mdtlabs.coreplatform.common.model.entity.spice.Frequency;
import com.mdtlabs.coreplatform.common.model.entity.spice.Lifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.MedicalCompliance;
import com.mdtlabs.coreplatform.common.model.entity.spice.ModelQuestions;
import com.mdtlabs.coreplatform.common.model.entity.spice.NutritionLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PhysicalExamination;
import com.mdtlabs.coreplatform.common.model.entity.spice.Reason;
import com.mdtlabs.coreplatform.common.model.entity.spice.RegionCustomization;
import com.mdtlabs.coreplatform.common.model.entity.spice.RiskAlgorithm;
import com.mdtlabs.coreplatform.common.model.entity.spice.SideMenu;
import com.mdtlabs.coreplatform.common.model.entity.spice.Symptom;
import com.mdtlabs.coreplatform.common.model.entity.spice.Unit;
import com.mdtlabs.coreplatform.spiceservice.AdminApiInterface;
import com.mdtlabs.coreplatform.spiceservice.frequency.service.FrequencyService;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.UnitRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ComorbidityService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ComplaintsService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ComplicationService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CultureService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CurrentMedicationService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.DiagnosisService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.DosageFormService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.DosageFrequencyService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.FormMetaService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.LifestyleService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.MedicalComplianceService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ModelQuestionsService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.NutritionLifestyleService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.PhysicalExaminationService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ReasonService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.RiskAlgorithmService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.SideMenuService;
import com.mdtlabs.coreplatform.spiceservice.symptom.service.SymptomService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.InheritingConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StaticDataServiceImplTest {

    @InjectMocks
    private static StaticDataServiceImpl staticDataService;

    public static ModelMapper modelMapper;

    @Mock
    private CultureService cultureService;

    @Mock
    private AdminApiInterface adminApiInterface;

    @Mock
    private SideMenuService sideMenuService;

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private DosageFormService dosageFormService;

    @Mock
    private DosageFrequencyService dosageFrequencyService;

    @Mock
    private NutritionLifestyleService nutritionLifestyleService;

    @Mock
    private SymptomService symptomService;

    @Mock
    private MedicalComplianceService medicalComplianceService;

    @Mock
    private DiagnosisService diagnosisService;

    @Mock
    private ReasonService reasonService;

    @Mock
    private RiskAlgorithmService riskAlgorithmService;

    @Mock
    private ModelQuestionsService modelQuestionsService;

    @Mock
    private ComorbidityService comorbidityService;

    @Mock
    private ComplaintsService complaintsService;

    @Mock
    private CurrentMedicationService currentMedicationService;

    @Mock
    private ComplicationService complicationService;

    @Mock
    private PhysicalExaminationService physicalExaminationService;

    @Mock
    private FrequencyService frequencyService;

    @Mock
    private LifestyleService lifestyleService;

    @Mock
    private FormMetaService formMetaService;

    @BeforeEach
    void init() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestDataProvider.setUp(StaticDataServiceImpl.class, "mapper", staticDataService);
    }

    private void setup() {

        Culture culture = TestDataProvider.getCulture();
        List<Site> sites = TestDataProvider.getSites();
        sites.get(0).setTenantId(1L);
        Account account = TestDataProvider.getAccount();
        List<Program> programs = TestDataProvider.getPrograms();
        List<SideMenu> sideMenus = List.of(TestDataProvider.getSideMenu());
        List<Unit> units = List.of(TestDataProvider.getUnit());
        List<DosageForm> dosageForms = List.of(TestDataProvider.getDosageForm());
        List<DosageFrequency> dosageFrequencies = List.of(TestDataProvider.getDosageFrequency());
        Set<NutritionLifestyle> nutritionLifestyleSet = Set.of(TestDataProvider.getNutritionLifestyle());
        List<Symptom> symptoms = List.of(TestDataProvider.getSymptomData());
        List<MedicalCompliance> medicalComplianceList = List.of(TestDataProvider.getMedicalCompliance());
        List<Diagnosis> diagnoses = List.of(TestDataProvider.getDiagnosis());
        List<Reason> reasons = List.of(TestDataProvider.getReason());
        RiskAlgorithm riskAlgorithm = new RiskAlgorithm();
        Country country = TestDataProvider.getCountry();
        List<County> counties = List.of(TestDataProvider.getCounty());
        List<Subcounty> subCounties = List.of(TestDataProvider.getSubCounty());
        ModelQuestions modelQuestion = TestDataProvider.getModelQuestions();
        modelQuestion.setId(1L);
        modelQuestion.setType("Test");
        List<ModelQuestions> modelQuestions = List.of(modelQuestion);
        List<SiteResponseDTO> siteResponseDTOS = TestDataProvider.getSiteResponseDTOs();
        List<Comorbidity> comorbidityList = List.of(TestDataProvider.getComorbidity());

        List<Complaints> complaints = List.of(TestDataProvider.getComplaints());
        List<CurrentMedication> currentMedications = List.of(TestDataProvider.getCurrentMedication());
        List<Complication> complications = List.of(TestDataProvider.getComplication());
        List<PhysicalExamination> physicalExaminations = List.of(TestDataProvider.getPhysicalExamination());
        List<Frequency> frequencies = List.of(TestDataProvider.getFrequency());
        List<Lifestyle> lifestyles = List.of(TestDataProvider.getLifestyle());
        lifestyles.get(0).setId(1L);

        Map<String, Map<Long, Object>> jsonCultureValuesMap = new HashMap<>();
        Map<Long, Object> sideMenuMap = new HashMap<>();
        sideMenuMap.put(1L, null);
        jsonCultureValuesMap.put(Constants.CULTURE_VALUE_SIDE_MENU, sideMenuMap);
        Constants.JSON_CULTURE_VALUES_MAP.put(1L, jsonCultureValuesMap);

        Map<String, Map<Long, String>> cultureValuesMap = new HashMap<>();
        Map<Long, String> cultureMap = new HashMap<>();
        cultureMap.put(1L, null);
        cultureValuesMap.put(Constants.DOSAGE_FORM, cultureMap);

        Map<Long, String> nutritionLifestyleMap = new HashMap<>();
        nutritionLifestyleMap.put(1L, null);
        cultureValuesMap.put(Constants.NUTRITION_LIFESTYLE, nutritionLifestyleMap);

        Map<Long, String> symptomsMap = new HashMap<>();
        symptomsMap.put(1L, null);
        cultureValuesMap.put(Constants.CULTURE_VALUE_SYMPTOMS, symptomsMap);

        Map<Long, String> medicalComplianceMap = new HashMap<>();
        medicalComplianceMap.put(1L, null);
        cultureValuesMap.put(Constants.CULTURE_VALUE_MEDICAL_COMPLIANCES, medicalComplianceMap);

        Map<Long, String> diagnosisMap = new HashMap<>();
        diagnosisMap.put(1L, null);
        cultureValuesMap.put(Constants.CULTURE_VALUE_DIAGNOSIS, diagnosisMap);

        Map<Long, String> reasonsMap = new HashMap<>();
        reasonsMap.put(1L, null);
        cultureValuesMap.put(Constants.CULTURE_VALUE_REASONS, reasonsMap);

        Map<Long, String> modelQuestionsMap = new HashMap<>();
        modelQuestionsMap.put(1L, null);
        cultureValuesMap.put(Constants.CULTURE_VALUE_MODEL_QUESTIONS, modelQuestionsMap);

        Map<Long, String> comorbidityMap = new HashMap<>();
        comorbidityMap.put(1L, null);
        cultureValuesMap.put(Constants.CULTURE_VALUE_COMORBIDITIES, comorbidityMap);

        Map<Long, String> compalintsMap = new HashMap<>();
        compalintsMap.put(1L, null);
        cultureValuesMap.put(Constants.CULTURE_VALUE_COMPLAINTS, compalintsMap);

        Map<Long, String> currentMedicationMap = new HashMap<>();
        currentMedicationMap.put(1L, null);
        cultureValuesMap.put(Constants.CULTURE_VALUE_CURRENT_MEDICATION, currentMedicationMap);

        Map<Long, String> complicationsMap = new HashMap<>();
        compalintsMap.put(1L, null);
        cultureValuesMap.put(Constants.CULTURE_VALUE_COMPLICATIONS, complicationsMap);

        Map<Long, String> examinationMap = new HashMap<>();
        examinationMap.put(1L, null);
        cultureValuesMap.put(Constants.CULTURE_VALUE_PHYSICAL_EXAMINATION, examinationMap);

        Map<Long, String> lifestyleLifestyleQuestionsMap = new HashMap<>();
        lifestyleLifestyleQuestionsMap.put(1L, null);
        cultureValuesMap.put(Constants.CULTURE_VALUE_LIFESTYLE_QUESTIONS, lifestyleLifestyleQuestionsMap);

        Map<Long, String> lifestyleAnswersMap = new HashMap<>();
        lifestyleAnswersMap.put(1L, null);
        cultureValuesMap.put(Constants.CULTURE_VALUE_LIFESTYLE_ANSWERS, lifestyleAnswersMap);

        Map<Long, Object> lifestyleAnswerMap = new HashMap<>();
        lifestyleAnswersMap.put(1L, null);
        cultureValuesMap.put(Constants.CULTURE_VALUE_LIFESTYLE_ANSWERS, lifestyleAnswersMap);

        jsonCultureValuesMap.put(Constants.CULTURE_VALUE_LIFESTYLE_ANSWERS, lifestyleAnswerMap);

        Map<Long, String> frequencyMap = new HashMap<>();
        frequencyMap.put(1L, null);
        cultureValuesMap.put(Constants.CULTURE_VALUE_FREQUENCY, frequencyMap);

        Constants.CULTURE_VALUES_MAP = Map.of(1L, cultureValuesMap);

        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(sites, new TypeToken<List<SiteResponseDTO>>() {
        }.getType())).thenReturn(siteResponseDTOS);

        when(cultureService.getCultureByName(Constants.DEFAULT_CULTURE_VALUE)).thenReturn(culture);
        when(adminApiInterface.getSitesByTenantIds(anyString(), anyLong(), any())).thenReturn(sites);
        when(adminApiInterface.getAccountById(anyString(), anyLong(), anyLong())).thenReturn(account);
        when(adminApiInterface.getPrograms(anyString(), anyLong(), any())).thenReturn(programs);
        when(sideMenuService.getSideMenus(any())).thenReturn(sideMenus);

        when(comorbidityService.getComorbidityList()).thenReturn(comorbidityList);

        when(adminApiInterface.getSitesByOperatingUnitId(anyString(), anyLong(), anyLong())).thenReturn(sites);
        when(unitRepository.findByNameNotLike(Constants.OTHER)).thenReturn(units);
        when(dosageFormService.getDosageFormNotOther()).thenReturn(dosageForms);
        when(dosageFrequencyService.getDosageFrequency(any())).thenReturn(dosageFrequencies);
        when(nutritionLifestyleService.getNutritionLifestyleByIds(any())).thenReturn(nutritionLifestyleSet);
        when(symptomService.getSymptoms()).thenReturn(symptoms);
        when(medicalComplianceService.getMedicalComplianceList()).thenReturn(medicalComplianceList);
        when(diagnosisService.getDiagnosis()).thenReturn(diagnoses);
        when(reasonService.getReasons()).thenReturn(reasons);
        when(riskAlgorithmService.getRiskAlgorithms(anyLong())).thenReturn(riskAlgorithm);

        when(adminApiInterface.getCountryById(anyString(), anyLong(), anyLong())).thenReturn(country);
        when(adminApiInterface.getAllCountyByCountryId(anyString(), anyLong(), anyLong())).thenReturn(counties);
        when(adminApiInterface.getAllSubCountyByCountryId(anyString(), anyLong(), anyLong())).thenReturn(subCounties);

        when(modelQuestionsService.getModelQuestions(anyLong())).thenReturn(modelQuestions);
        when(complaintsService.getCompliantList()).thenReturn(complaints);
        when(currentMedicationService.getCurrentMedicationByIds(any())).thenReturn(currentMedications);
        when(complicationService.getComplications()).thenReturn(complications);
        when(physicalExaminationService.getPhysicalExamination()).thenReturn(physicalExaminations);
        when(frequencyService.getFrequencies()).thenReturn(frequencies);
        when(lifestyleService.getLifestyles()).thenReturn(lifestyles);
    }

    @Test
    void getStaticData() {
        setup();
        TestDataProvider.init();
        TestDataProvider.getStaticMock();
        staticDataService.getStaticData(1L);
        Assertions.assertNotNull(staticDataService.getStaticData(1L));
        TestDataProvider.cleanUp();

    }

    @Test
    void getMedicalReviewStaticData() {
        setup();
        TestDataProvider.init();
        TestDataProvider.getStaticMock();
        Assertions.assertNotNull(staticDataService.getMedicalReviewStaticData(1L));
        TestDataProvider.cleanUp();
    }

    @Test
    void removeUnselectedFormData() {

        RegionCustomization regionCustomization = TestDataProvider.getRegionCustomization();

        staticDataService.removeUnselectedFormData(regionCustomization, new ArrayList<>());
        Assertions.assertThrows(Exception.class, () -> staticDataService.removeUnselectedFormData(null, new ArrayList<>()));
    }

    @Test
    void setFormInputData() {
        Map<String, String> assessment = new HashMap<>();
        Map<String, String> enrollment = new HashMap<>();
        Map<String, String> screening = new HashMap<>();
        RegionCustomization regionCustomization = TestDataProvider.getRegionCustomization();

        regionCustomization.setType(Constants.ASSESSMENT);
        staticDataService.setFormInputData(assessment, enrollment, screening, regionCustomization);
        Assertions.assertNotNull(assessment.get(regionCustomization.getCategory()));

        regionCustomization.setType(Constants.ENROLLMENT);
        staticDataService.setFormInputData(assessment, enrollment, screening, regionCustomization);
        Assertions.assertNotNull(enrollment.get(regionCustomization.getCategory()));

        regionCustomization.setType(Constants.SCREENING);
        staticDataService.setFormInputData(assessment, enrollment, screening, regionCustomization);
        Assertions.assertNotNull(screening.get(regionCustomization.getCategory()));
    }

    @Test
    void checkAppVersionNullTest() {
        ReflectionTestUtils.setField(staticDataService, "appVersion", "1.0.5");
        boolean response = staticDataService.checkAppVersion(null);
        assertFalse(response);
    }

    @Test
    void checkAppVersionTest() {
        ReflectionTestUtils.setField(staticDataService, "appVersion", "1.0.5");
        boolean response = staticDataService.checkAppVersion("1.0.6");
        assertFalse(response);
    }

    @Test
    void checkAppVersion() {
        ReflectionTestUtils.setField(staticDataService, "appVersion", "1.0.5");
        boolean response = staticDataService.checkAppVersion("1.0.5");
        assertTrue(response);
    }

    @Test
    void getMetaFormDataTest() {
        //given
        String form = "";
        //when
        when(formMetaService.getMetaForms(form)).thenReturn(null);
        //then
        MetaFormDTO response = staticDataService.getMetaFormData(form);
        Assertions.assertNull(response);
        Assertions.assertEquals(null, response);

        //given
        String request ="";
        FormMetaUi formMetaUi = TestDataProvider.getFormMetaUi();
        //when
        when(formMetaService.getMetaForms(form)).thenReturn(formMetaUi);
        //then
        MetaFormDTO result = staticDataService.getMetaFormData(form);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(formMetaUi.getId(), result.getId());
        Assertions.assertEquals(formMetaUi.getFormName(), result.getFormName());
    }
}