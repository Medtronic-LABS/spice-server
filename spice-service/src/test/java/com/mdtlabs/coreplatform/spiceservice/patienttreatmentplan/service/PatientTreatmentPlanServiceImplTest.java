package com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.FrequencyDTO;
import com.mdtlabs.coreplatform.common.model.entity.Culture;
import com.mdtlabs.coreplatform.common.model.entity.spice.Frequency;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;
import com.mdtlabs.coreplatform.common.util.DateUtil;
import com.mdtlabs.coreplatform.spiceservice.frequency.service.FrequencyService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CultureService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.FrequencyTypeService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.repository.PatientTreatmentPlanRepository;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.impl.PatientTreatmentPlanServiceImpl;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * <p>
 * PatientTreatmentPlanServiceImplTest class used to test all possible positive
 * and negative cases for all methods and conditions used in PatientTreatmentPlanServiceImpl class.
 * </p>
 *
 * @author Jaganathan created on Feb 8, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PatientTreatmentPlanServiceImplTest {

    @InjectMocks
    private PatientTreatmentPlanServiceImpl treatmentPlanService;

    @Mock
    private FrequencyService frequencyService;

    @Mock
    private FrequencyTypeService frequencyTypeService;

    @Mock
    private CultureService cultureService;

    @Mock
    private ModelMapper mapper;

    @Mock
    private PatientTreatmentPlanRepository treatmentPlanRepository;

    private PatientTreatmentPlan treatmentPlan = null;

    private PatientTreatmentPlan response;

    @Test
    @DisplayName("GetPatientTreatmentPlan Test")
    void getPatientTreatmentPlan() {
        //given
        Long patientTrackerId = 0l;
        List<PatientTreatmentPlan> treatmentPlans = List.of();
        //when
        when(treatmentPlanRepository.findByPatientTrackIdAndIsDeletedOrderByUpdatedAtDesc(patientTrackerId,
                Constants.BOOLEAN_FALSE)).thenReturn(treatmentPlans);
        response = treatmentPlanService.getPatientTreatmentPlan(patientTrackerId);
        Assertions.assertTrue(treatmentPlans.isEmpty());
        //given
        patientTrackerId = 1L;
        treatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        treatmentPlans = List.of(treatmentPlan);
        //when
        when(treatmentPlanRepository.findByPatientTrackIdAndIsDeletedOrderByUpdatedAtDesc(patientTrackerId,
                Constants.BOOLEAN_FALSE)).thenReturn(treatmentPlans);
        response = treatmentPlanService.getPatientTreatmentPlan(patientTrackerId);
        Assertions.assertTrue(!treatmentPlans.isEmpty());
        Assertions.assertEquals(response.getPatientTrackId(), treatmentPlans.get(Constants.ZERO).getPatientTrackId());
        Assertions.assertEquals(response.getTenantId(), treatmentPlans.get(Constants.ZERO).getTenantId());
    }

   @Test
   @DisplayName("GetPatientTreatmentPlanDetailsByInvalidId Test")
   void getPatientTreatmentPlanDetailsByInvalidId() {
       //given
       Long id = 0l;
       MockedStatic<UserSelectedTenantContextHolder> userHolder = mockStatic(UserSelectedTenantContextHolder.class);
       userHolder.when(UserSelectedTenantContextHolder::get).thenReturn(4l);
       when(treatmentPlanRepository.findFirstByPatientTrackIdAndTenantIdAndIsDeletedOrderByUpdatedAtDesc(id,
               4l, Constants.BOOLEAN_FALSE)).thenReturn(treatmentPlan);
       response = treatmentPlanService.getPatientTreatmentPlanDetails(id);
       userHolder.close();
       Assertions.assertNull(treatmentPlan);
       Assertions.assertNull(response);
       Assertions.assertEquals(treatmentPlan, response);
   }

    @Test
    @DisplayName("GetPatientTreatmentPlanDetailsByValidId test")
    void getPatientTreatmentPlanDetailsByValidId() {
        //given
        Long id = 1l;
        treatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        MockedStatic<UserSelectedTenantContextHolder> userHolder = mockStatic(UserSelectedTenantContextHolder.class);
        //when
        userHolder.when(UserSelectedTenantContextHolder::get).thenReturn(4l);
        when(treatmentPlanRepository.findFirstByPatientTrackIdAndTenantIdAndIsDeletedOrderByUpdatedAtDesc(id,
                4l, Constants.BOOLEAN_FALSE)).thenReturn(treatmentPlan);
        response = treatmentPlanService.getPatientTreatmentPlanDetails(id);
        userHolder.close();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(treatmentPlan, response);
        Assertions.assertEquals(treatmentPlan.getPatientTrackId(), response.getPatientTrackId());
    }

    @Test
    @DisplayName("GetPatientTreatmentPlanDetailsWithTenantId Test")
    void getPatientTreatmentPlanDetailsWithTenantId() {
        //given
        Long id = 1l;
        Long tenantId = 4l;
        String cvdRiskLevel = Constants.CVD_RISK_LOW;
        treatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        //when
        when(treatmentPlanRepository.findByPatientTrackIdAndTenantId(id, tenantId)).thenReturn(treatmentPlan);
        //then
        response = treatmentPlanService.getPatientTreatmentPlanDetails(id, cvdRiskLevel, tenantId);
        PatientTreatmentPlan planResult = treatmentPlanRepository.findByPatientTrackIdAndTenantId(id, tenantId);
        Assertions.assertEquals(response, planResult);
        Assertions.assertEquals(response.getPatientTrackId(), planResult.getPatientTrackId());
        Assertions.assertEquals(response.getTenantId(), planResult.getTenantId());
        Assertions.assertEquals(response.getRiskLevel(), planResult.getRiskLevel());
    }

    @Test
    @DisplayName("GetPatientTreatmentPlanDetailsByInValidTenantId Test")
    void getPatientTreatmentPlanDetailsByInvalidTenantId() {
        Long id = 1l;
        Long tenantId = 0l;
        String cvdRiskLevel = Constants.CVD_RISK_LOW;
        //when
        when(treatmentPlanRepository.findByPatientTrackIdAndTenantId(id, tenantId)).thenReturn(treatmentPlan);
        response = treatmentPlanService.getPatientTreatmentPlanDetails(id, cvdRiskLevel, tenantId);
        Assertions.assertNull(response);
        Assertions.assertNull(treatmentPlan);
    }

   @Test
   @DisplayName("CreateProvisionalTreatmentPlanByInvalidId test")
   void createProvisionalTreatmentPlanByInvalidTrackerId() {
       //given
       Long id = 1l;
       Long tenantId = 4l;
       Long cultureId = 1l;
       String cvdRiskLevel = Constants.CVD_RISK_LOW;
       PatientTracker patientTracker = TestDataProvider.getPatientTracker();
       Frequency frequency = TestDataProvider.getFrequency();
       List<Frequency> frequencies = new ArrayList<>();
       List<Map<String, String>> treatmentPlanResponse = new ArrayList<>();
       Culture culture = TestDataProvider.getCulture();
       TestDataProvider.init();
       Map<Long, String> map = new HashMap<>();
       map.put(cultureId, "English-India");
		Constants.CULTURE_VALUES_MAP = Map.of(cultureId, Map.of("code", Map.of(cultureId, "English-India")));
       when(treatmentPlanRepository.findByPatientTrackIdAndTenantId(id, tenantId)).thenReturn(treatmentPlan);
       treatmentPlan = treatmentPlanService.getPatientTreatmentPlanDetails(id, cvdRiskLevel, tenantId);
       Assertions.assertNull(treatmentPlan);
       treatmentPlan = new PatientTreatmentPlan();
       treatmentPlan.setTenantId(tenantId);
       treatmentPlan.setPatientTrackId(patientTracker.getPatientId());
       treatmentPlan.setRiskLevel(cvdRiskLevel);
       treatmentPlan.setIsProvisional(Constants.BOOLEAN_TRUE);
       treatmentPlan.setBgCheckFrequency(Constants.BG_PROVISIONAL_FREQUENCY_NAME);
       frequencies = List.of(TestDataProvider.getFrequency());
       Map<Long, String> frequencyMap = new HashMap<>();
       Map<String, String> frequencyTypeMap = new HashMap<>();
       when(frequencyService.getFrequencyListByRiskLevel(cvdRiskLevel)).thenReturn(frequencies);
       when(cultureService.loadCultureValues()).thenReturn(cultureId);
	   TestDataProvider.getStaticMock();
       when(cultureService.getCultureByName(Constants.DEFAULT_CULTURE_VALUE)).thenReturn(culture);
       when(frequencyService.getFrequencyByType(Constants.FREQUENCY_BG_CHECK)).thenReturn(frequency);
       when(treatmentPlanRepository.save(treatmentPlan)).thenReturn(treatmentPlan);
       when(frequencyTypeService.getFrequencyTypesByCultureId(cultureId)).thenReturn(frequencyTypeMap);
       frequencyTypeMap.put(Constants.FREQUENCY_HBA1C_CHECK, Constants.HBA1C_CHECK_FREQUENCY);
       treatmentPlanResponse = treatmentPlanService.updateNextFollowupDetails(treatmentPlan,
               frequencies, patientTracker, frequencyMap, frequencyTypeMap);
       List<Map<String, String>> mapListResponse = treatmentPlanService
               .createProvisionalTreatmentPlan(patientTracker, cvdRiskLevel, tenantId);
       TestDataProvider.cleanUp();
       Assertions.assertEquals(treatmentPlanResponse, mapListResponse);
   }

    @DisplayName("GetTreatmentPlanFollowUpDate Test")
    void getTreatmentPlanFollowupDate() {
        Frequency frequency = new Frequency();
        //when
        when(frequencyService.getFrequencyByFrequencyNameAndType(frequency.getName(), frequency.getType()))
                .thenReturn(frequency);
        Frequency result = frequencyService.getFrequencyByFrequencyNameAndType(frequency.getName(), frequency.getType());
        Assertions.assertEquals(frequency, result);
        //given
        frequency = TestDataProvider.getFrequency();
        when(frequencyService.getFrequencyByFrequencyNameAndType(frequency.getName(), frequency.getType()))
                .thenReturn(frequency);
        result = frequencyService.getFrequencyByFrequencyNameAndType(frequency.getName(), frequency.getType());
        Assertions.assertEquals(frequency, result);
    }

    @Test
    @DisplayName("UpdateTreatmentPlanDataNullId Test")
    void updateTreatmentPlanData() {
        //given
        boolean result = Constants.BOOLEAN_TRUE;
        PatientTreatmentPlan planResponse = TestDataProvider.getPatientTreatmentPlan();
        treatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        treatmentPlan.setId(null);
        Assertions.assertNull(treatmentPlan.getId());
        //when
        when(treatmentPlanRepository.save(treatmentPlan)).thenReturn(planResponse);
        Assertions.assertNotNull(planResponse);
        boolean actualResult = treatmentPlanService.updateTreatmentPlanData(treatmentPlan);
        Assertions.assertEquals(result, actualResult);
    }

    @Test
    @DisplayName("UpdateTreatmentPlanWithId Test")
    void updateTreatmentPlanWithId() {
        //given
        boolean result = Constants.BOOLEAN_FALSE;
        treatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        treatmentPlan.setId(1L);
        //when
        Assertions.assertNotNull(treatmentPlan.getId());
        when(mapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(treatmentPlanRepository.findByIdAndIsDeleted(treatmentPlan.getId(), Constants.BOOLEAN_FALSE))
                .thenReturn(treatmentPlan);
        //then
        PatientTreatmentPlan patientTreatmentPlan = treatmentPlanRepository.findByIdAndIsDeleted(treatmentPlan.getId(),
                Constants.BOOLEAN_FALSE);
        Assertions.assertNotNull(patientTreatmentPlan);
        boolean actualResult = treatmentPlanService.updateTreatmentPlanData(patientTreatmentPlan);
        Assertions.assertEquals(result, actualResult);
    }

    @Test
    @DisplayName("UpdateTreatmentPlanWithId Test")
    void updateTreatmentPlanWithInvalidId() {
        //given
        treatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        treatmentPlan.setId(0l);
        PatientTreatmentPlan existingPlan = null;
        //when
        Assertions.assertNotNull(treatmentPlan.getId());
        when(treatmentPlanRepository.findByIdAndIsDeleted(treatmentPlan.getId(), Constants.BOOLEAN_FALSE))
                .thenReturn(existingPlan);
        assertThrows(DataNotFoundException.class, () -> {
            treatmentPlanService.updateTreatmentPlanData(treatmentPlan);
        });
    }

    @Test
    @DisplayName("GetNextFollowUpDate")
    void getNextFollowUpDateWithBgCheck() {
        //given
        String freqName = "BG Check";
        String freqType = Constants.DEFAULT;
        Date date = new Date();
        Frequency frequency = TestDataProvider.getFrequency();
        treatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        Assertions.assertNotNull(treatmentPlan);
        List<PatientTreatmentPlan> treatmentPlanList = List.of(treatmentPlan);
        //when
        when(treatmentPlanRepository.findByPatientTrackIdAndIsDeletedOrderByUpdatedAtDesc(1l,
                Constants.BOOLEAN_FALSE)).thenReturn(treatmentPlanList);
        when(frequencyService.getFrequencyByFrequencyNameAndType(freqName, freqType)).thenReturn(frequency);
        MockedStatic<DateUtil> dateUtil = mockStatic(DateUtil.class);
        dateUtil.when(() -> DateUtil.getTreatmentPlanFollowupDate(frequency.getPeriod(), frequency.getDuration()))
                .thenReturn(date);
        when(frequencyService.getFrequencyByFrequencyNameAndType(freqName, freqType)).thenReturn(frequency);
        //then
        dateUtil.close();
        Assertions.assertNotNull(treatmentPlanList.get(0));
        Assertions.assertEquals(1l, treatmentPlanList.get(0).getPatientTrackId());
        Assertions.assertNotNull(frequency);
        Date actualDate = treatmentPlanService
                .getNextFollowUpDate(treatmentPlanList.get(0).getPatientTrackId(), freqName);
        Assertions.assertNotNull(actualDate);
    }

    @Test
    @DisplayName("GetNextFollowUpDate")
    void getNextFollowUpDateWithMedicalReviewCheck() {
        //given
        String freqType = Constants.DEFAULT;
        Date date = new Date();
        Frequency frequency = TestDataProvider.getFrequency();
        treatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        Assertions.assertNotNull(treatmentPlan);
        List<PatientTreatmentPlan> treatmentPlanList = List.of(treatmentPlan);
        String freqName = treatmentPlan.getMedicalReviewFrequency();
        //when
        when(treatmentPlanRepository.findByPatientTrackIdAndIsDeletedOrderByUpdatedAtDesc(1l,
                Constants.BOOLEAN_FALSE)).thenReturn(treatmentPlanList);
        Assertions.assertNotNull(treatmentPlanList.get(0));
        Assertions.assertEquals(1l, treatmentPlanList.get(0).getPatientTrackId());
        when(frequencyService.getFrequencyByFrequencyNameAndType(freqName, freqType)).thenReturn(frequency);
        MockedStatic<DateUtil> dateUtil = mockStatic(DateUtil.class);
        dateUtil.when(() -> DateUtil.getTreatmentPlanFollowupDate(frequency.getPeriod(), frequency.getDuration()))
                .thenReturn(date);
        when(frequencyService.getFrequencyByFrequencyNameAndType(freqName, freqType)).thenReturn(frequency);
        Assertions.assertNotNull(frequency);
        Date actualDate = treatmentPlanService
                .getNextFollowUpDate(treatmentPlanList.get(0).getPatientTrackId(), freqName);
        dateUtil.close();
        Assertions.assertEquals(date, actualDate);
    }

    @Test
    @DisplayName("GetNextFollowUpDate")
    void getNextFollowUpDateWithBPCheck() {
        //given
        String freqName = Constants.FREQUENCY_BP_CHECK;
        String freqType = Constants.DEFAULT;
        Date date = new Date();
        Frequency frequency = TestDataProvider.getFrequency();
        treatmentPlan = TestDataProvider.getPatientTreatmentPlan();
        Assertions.assertNotNull(treatmentPlan);
        List<PatientTreatmentPlan> treatmentPlanList = List.of(treatmentPlan);
        //when
        when(treatmentPlanRepository.findByPatientTrackIdAndIsDeletedOrderByUpdatedAtDesc(1l,
                Constants.BOOLEAN_FALSE)).thenReturn(treatmentPlanList);
        when(frequencyService.getFrequencyByFrequencyNameAndType(freqName, freqType)).thenReturn(frequency);
        MockedStatic<DateUtil> dateUtil = mockStatic(DateUtil.class);
        dateUtil.when(() -> DateUtil.getTreatmentPlanFollowupDate(frequency.getPeriod(), frequency.getDuration()))
                .thenReturn(date);
        when(frequencyService.getFrequencyByFrequencyNameAndType(freqName, freqType)).thenReturn(frequency);
        //then
        Assertions.assertNotNull(treatmentPlanList.get(0));
        Assertions.assertEquals(1l, treatmentPlanList.get(0).getPatientTrackId());
        Assertions.assertNotNull(frequency);
        Date actualDate = treatmentPlanService.getNextFollowUpDate(treatmentPlanList.get(0)
                .getPatientTrackId(), freqName);
        dateUtil.close();
        Assertions.assertNotNull(actualDate);
    }

    @Test
    @DisplayName("GetNextFollowUpDateByInvalidId Test")
    void getNextFollowUpDateByInvalidId() {
        //given
        List<PatientTreatmentPlan> treatmentPlanList = List.of();
        String freqName = Constants.FREQUENCY_BG_CHECK;
        when(treatmentPlanRepository.findByPatientTrackIdAndIsDeletedOrderByUpdatedAtDesc(0l,
                Constants.BOOLEAN_FALSE)).thenReturn(treatmentPlanList);
        List<PatientTreatmentPlan> actualList = treatmentPlanRepository
                .findByPatientTrackIdAndIsDeletedOrderByUpdatedAtDesc(0l, Constants.BOOLEAN_FALSE);
        Assertions.assertEquals(treatmentPlanList, actualList);
        Date date = treatmentPlanService.getNextFollowUpDate(0l, freqName);
        Assertions.assertNull(date);
        Assertions.assertEquals(null, date);
    }

    @Test
    @DisplayName("GetTreatmentPlanFollowDateWithNullFrequency Test")
    void getTreatmentPlanFollowDate() {
        //given
        Frequency frequency = null;
        Date date = null;
        //when
        when(frequencyService.getFrequencyByFrequencyNameAndType("name", "default"))
                .thenReturn(frequency);
        Assertions.assertNull(frequency);
        Date actualDate = treatmentPlanService.getTreatmentPlanFollowupDate("name", "default");
        Assertions.assertNull(actualDate);
        Assertions.assertEquals(date, actualDate);
    }

    @Test
    @DisplayName("GetTreatmentPlanFollowDate")
    void getTreatmentPlanFollowDateWithFrequency() {
        //given
        Frequency frequency = TestDataProvider.getFrequency();
        Date date = new Date();
        MockedStatic<DateUtil> dateUtil = mockStatic(DateUtil.class);
        dateUtil.when(() -> DateUtil.getTreatmentPlanFollowupDate(frequency.getPeriod(), frequency.getDuration()))
                .thenReturn(date);
        when(frequencyService.getFrequencyByFrequencyNameAndType(frequency.getName(), frequency.getType()))
                .thenReturn(frequency);
        //then
        dateUtil.close();
        Frequency actualFrequency = frequencyService
                .getFrequencyByFrequencyNameAndType(frequency.getName(), frequency.getType());
        Assertions.assertNotNull(actualFrequency);
        Assertions.assertEquals(frequency, actualFrequency);
        Date actualDate = treatmentPlanService.getTreatmentPlanFollowupDate(frequency.getName(), frequency.getType());
        Assertions.assertNotNull(actualDate);
    }

    @ParameterizedTest
    @DisplayName("UpdateNextFollowUpDetails Test")
    @CsvSource({Constants.FREQUENCY_HBA1C_CHECK, Constants.FREQUENCY_BG_CHECK, Constants.BG_CHECK_FREQUENCY, Constants.MEDICAL_REVIEW_FREQUENCY})
    void updateNextFollowUpDetails() {
        //given
        Frequency frequency = TestDataProvider.getFrequency();
        frequency.setType(Constants.FREQUENCY_HBA1C_CHECK);
        Map<String, Frequency> frequencyMap = new HashMap<>();
        List<FrequencyDTO> frequencyList = new ArrayList<>();
        List<Map<String, String>> responseList = List.of(Map.of(Constants.LABEL, Constants.HBA1C_CHECK_FREQUENCY, Constants.VALUE, Constants.HYPHEN));
        Assertions.assertTrue(frequencyList.isEmpty());
        String[] frequencyNames = {Constants.FREQUENCY_HBA1C_CHECK};
        //when
        when(frequencyService.getFrequencyByType(Constants.FREQUENCY_HBA1C_CHECK)).thenReturn(frequency);
        for (String frequencyName : frequencyNames) {
            FrequencyDTO actualFrequency = new FrequencyDTO();
            actualFrequency.setId(1L);
            actualFrequency.setName("-");
            actualFrequency.setType(frequencyName);
            actualFrequency.setPeriod("03");
            actualFrequency.setDuration(Constants.TWO);
            actualFrequency.setRiskLevel(Constants.CVD_RISK_MEDIUM);
            actualFrequency.setDisplayOrder(Constants.FOUR);
            frequencyList.add(actualFrequency);
        }
        Map<String, String> frequencyTypeMap = new HashMap<>();
        frequencyTypeMap.put(Constants.FREQUENCY_HBA1C_CHECK, Constants.HBA1C_CHECK_FREQUENCY);
        List<Map<String, String>> actualList = treatmentPlanService
                .constructTreatmentPlanResponse(frequencyList, frequencyTypeMap);
        Assertions.assertEquals(responseList.get(0), actualList.get(0));
    }

   @Test
   @DisplayName("CreateProvisionalTreatmentPlanByValidId test")
   void createProvisionalTreatmentPlanByValidTrackerId() {
       //given
       Long id = 1l;
       Long tenantId = 4l;
       Long cultureId = 1l;
       Frequency frequency = TestDataProvider.getFrequency();
       FrequencyDTO frequencyDTO = new ModelMapper().map(frequency, FrequencyDTO.class);
       Culture culture = TestDataProvider.getCulture();
       TestDataProvider.init();
       Constants.CULTURE_VALUES_MAP = Map.of(cultureId, Map.of("code", Map.of(cultureId, "English-India")));
       String cvdRiskLevel = Constants.CVD_RISK_LOW;
       PatientTracker patientTracker = TestDataProvider.getPatientTracker();
       patientTracker.setNextBpAssessmentDate(new Date(12 / 03 / 23));
       Map<String, String> frequencyTypeMap = new HashMap<>();
        frequencyTypeMap.put(Constants.FREQUENCY_HBA1C_CHECK, Constants.HBA1C_CHECK_FREQUENCY);
       List<Frequency> frequencies = List.of(TestDataProvider.getFrequency());
       List<Map<String, String>> treatmentPlanResponse = new ArrayList<>();
       Map<String, String> reponse = new HashMap<>();
       reponse.put("value", null);
       reponse.put("label", null);
       treatmentPlanResponse.add(reponse);
       PatientTreatmentPlan updatedTreatmentPlan = new PatientTreatmentPlan();
       updatedTreatmentPlan.setTenantId(tenantId);
       updatedTreatmentPlan.setPatientTrackId(patientTracker.getId());
       updatedTreatmentPlan.setRiskLevel(cvdRiskLevel);
       updatedTreatmentPlan.setBgCheckFrequency(Constants.BG_PROVISIONAL_FREQUENCY_NAME);
       updatedTreatmentPlan.setIsProvisional(Constants.BOOLEAN_TRUE);
       //when
       when(treatmentPlanRepository.findByPatientTrackIdAndTenantId(id, tenantId)).thenReturn(treatmentPlan);
       when(frequencyService.getFrequencyListByRiskLevel(cvdRiskLevel)).thenReturn(frequencies);
       when(cultureService.loadCultureValues()).thenReturn(cultureId);
       TestDataProvider.getStaticMock();
       when(frequencyService.getFrequencyByType(Constants.FREQUENCY_BG_CHECK)).thenReturn(frequency);
       when(treatmentPlanRepository.save(updatedTreatmentPlan)).thenReturn(updatedTreatmentPlan);
       when(frequencyTypeService.getFrequencyTypesByCultureId(cultureId)).thenReturn(frequencyTypeMap);
       //then
       List<Map<String, String>> actualresponse = treatmentPlanService.createProvisionalTreatmentPlan(patientTracker,
               cvdRiskLevel, tenantId);
       TestDataProvider.cleanUp();
       Assertions.assertEquals(treatmentPlanResponse, actualresponse);

       //given
       TestDataProvider.init();
       treatmentPlan = TestDataProvider.getPatientTreatmentPlan();
       MockedStatic<DateUtil> dateUtil = mockStatic(DateUtil.class);
       treatmentPlanResponse = List.of(Map.of("label=Blood Pressure Check Frequency", "value=name",
               "label=Blood Glucose Check Frequency", "value=name",
               "label=Medical Review Frequency", "value=name"));
       //when
       when(treatmentPlanRepository.findByPatientTrackIdAndTenantId(id, tenantId)).thenReturn(treatmentPlan);
       when(frequencyService.getFrequencyListByRiskLevel(cvdRiskLevel)).thenReturn(frequencies);
       doNothing().when(cultureService).getCultureValues();
       TestDataProvider.getStaticMock();
       
       when(cultureService.getCultureByName(Constants.DEFAULT_CULTURE_VALUE)).thenReturn(culture);
       when(frequencyService.getFrequencyByType(Constants.FREQUENCY_BG_CHECK)).thenReturn(frequency);
       when(frequencyService.getFrequencyByFrequencyNameAndType(Constants.DEFAULT, treatmentPlan.getBpCheckFrequency()))
        .thenReturn(frequency);
       dateUtil.when(() -> DateUtil.getTreatmentPlanFollowupDate(frequency.getPeriod(),
       frequency.getDuration())).thenReturn(new Date(12 / 3 / 23));
       when(frequencyService.getFrequencyByFrequencyNameAndType(Constants.DEFAULT, treatmentPlan.getBgCheckFrequency()))
			.thenReturn(frequency);
       when(mapper.map(frequency, FrequencyDTO.class)).thenReturn(frequencyDTO);
       dateUtil.when(() -> DateUtil.getTreatmentPlanFollowupDate(frequency.getPeriod(),
               frequency.getDuration())).thenReturn(new Date(12 / 3 / 23));
       when(frequencyService.getFrequencyByFrequencyNameAndType(Constants.DEFAULT, treatmentPlan.getMedicalReviewFrequency()))
               .thenReturn(frequency);

       dateUtil.when(() -> DateUtil.getTreatmentPlanFollowupDate(frequency.getPeriod(),
               frequency.getDuration())).thenReturn(new Date(12 / 3 / 23));
       //then
       Map<Long, String> frequencyMap = new HashMap<>();
       treatmentPlanResponse = treatmentPlanService.updateNextFollowupDetailsForExisting(treatmentPlan,
               patientTracker, frequencyMap, frequencyTypeMap);
       actualresponse = treatmentPlanService.createProvisionalTreatmentPlan(patientTracker,
               cvdRiskLevel, tenantId);
       TestDataProvider.cleanUp();
       dateUtil.close();
       Assertions.assertEquals(treatmentPlanResponse, actualresponse);
   }
}
