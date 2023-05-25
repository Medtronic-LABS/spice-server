package com.mdtlabs.coreplatform.spiceservice.common;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.RedRiskDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RiskAlgorithmDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.Symptom;
import com.mdtlabs.coreplatform.spiceservice.symptom.service.SymptomService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * <p>
 * Risk Algorithm Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Mar 03, 2023
 */
@ExtendWith(MockitoExtension.class)
class RiskAlgorithmTest {

    @InjectMocks
    private RiskAlgorithm riskAlgorithm;

    @Mock
    private SymptomService symptomService;

    @Test
    void getRiskLevelInAssessmentDbm() {
        //given
        RiskAlgorithmDTO riskAlgorithmDTO = TestDataProvider.getRiskAlgorithmDto();

        //then
        String riskLevel = riskAlgorithm.getRiskLevelInAssessmentDbm(riskAlgorithmDTO);
        Assertions.assertEquals(Constants.HIGH, riskLevel);
        riskAlgorithmDTO.setIsPregnant(null);
        riskAlgorithmDTO.setRiskLevel(null);
        riskLevel = riskAlgorithm.getRiskLevelInAssessmentDbm(riskAlgorithmDTO);
        Assertions.assertEquals(null, riskLevel);


    }

    @Test
    void toVerifyGetRiskLevelInAssessmentDbm() {
        //given
        RiskAlgorithmDTO riskAlgorithmDTO = new RiskAlgorithmDTO();
        riskAlgorithmDTO.setRiskLevel(Constants.CVD_RISK_MEDIUM);
        riskAlgorithmDTO.setAvgSystolic(50);
        riskAlgorithmDTO.setAvgDiastolic(80);

        //then
        String riskLevel = riskAlgorithm.getRiskLevelInAssessmentDbm(riskAlgorithmDTO);
        Assertions.assertEquals(Constants.LOW, riskLevel);
    }

    @Test
    void toVerifyGetRiskLevelInAssessmentDbmWithNull() {
        //given
        RiskAlgorithmDTO riskAlgorithmDTO = new RiskAlgorithmDTO();
        riskAlgorithmDTO.setRiskLevel(null);
        riskAlgorithmDTO.setIsPregnant(null);

        //then
        String riskLevel = riskAlgorithm.getRiskLevelInAssessmentDbm(riskAlgorithmDTO);
        Assertions.assertNull(riskLevel);
    }

    @Test
    void calculateRiskLevelFromBpReading() {
        //then
        String highRiskLevel = riskAlgorithm.calculateRiskLevelFromBpReading(170, null,
                Constants.HIGH);
        Assertions.assertEquals(Constants.HIGH, highRiskLevel);

        //then
        highRiskLevel = riskAlgorithm.calculateRiskLevelFromBpReading(null, 105, Constants.HIGH);
        Assertions.assertEquals(Constants.HIGH, highRiskLevel);

        //then
        highRiskLevel = riskAlgorithm.calculateRiskLevelFromBpReading(170, 105, Constants.HIGH);
        Assertions.assertEquals(Constants.HIGHER_MODERATE, highRiskLevel);

        highRiskLevel = riskAlgorithm.calculateRiskLevelFromBpReading(170, 105, Constants.LOW);
        Assertions.assertNotEquals(Constants.HIGHER_MODERATE, highRiskLevel);

        //then
        highRiskLevel = riskAlgorithm.calculateRiskLevelFromBpReading(200, 105, Constants.HIGH);
        Assertions.assertEquals(Constants.HIGHER_MODERATE, highRiskLevel);

        //then
        highRiskLevel = riskAlgorithm.calculateRiskLevelFromBpReading(185, 200, Constants.HIGH);
        Assertions.assertEquals(Constants.HIGHER_MODERATE, highRiskLevel);

        //then
        highRiskLevel = riskAlgorithm.calculateRiskLevelFromBpReading(200, 115, Constants.HIGH);
        Assertions.assertEquals(Constants.HIGHER_MODERATE, highRiskLevel);

        highRiskLevel = riskAlgorithm.calculateRiskLevelFromBpReading(187, 111, Constants.LOW);
        Assertions.assertEquals(Constants.HIGHER_MODERATE, highRiskLevel);

        //then
        String moderateRiskLevel = riskAlgorithm.calculateRiskLevelFromBpReading(150, 95,
                Constants.HIGH);
        Assertions.assertEquals(Constants.MODERATE, moderateRiskLevel);

    }

    @Test
    void calculateRiskFromSymptomAndBpReading() {
        //given
        RiskAlgorithmDTO riskAlgorithmDTO = TestDataProvider.getRiskAlgorithmDto();
        Symptom symptom = TestDataProvider.getSymptomData();

        //when
        when(symptomService.getSymptoms()).thenReturn(List.of(symptom));

        //then
        String riskLevel = riskAlgorithm.calculateRiskFromSymptomAndBpReading(riskAlgorithmDTO);
        Assertions.assertEquals(Constants.CVD_RISK_MEDIUM, riskLevel);
    }

    @Test
    void toVerifyCalculateRiskFromSymptomAndBpReading() {
        //given
        RiskAlgorithmDTO riskAlgorithmDTO = new RiskAlgorithmDTO();
        riskAlgorithmDTO.setAvgSystolic(200);
        riskAlgorithmDTO.setAvgDiastolic(200);

        //then
        String riskLevel = riskAlgorithm.calculateRiskFromSymptomAndBpReading(riskAlgorithmDTO);
        Assertions.assertEquals(Constants.HIGH, riskLevel);
    }

    @Test
    void testCalculateRiskFromSymptomAndBpReading() {
        //given
        RiskAlgorithmDTO riskAlgorithmDTO = new RiskAlgorithmDTO();
        riskAlgorithmDTO.setAvgSystolic(100);
        riskAlgorithmDTO.setAvgDiastolic(120);

        //then
        String riskLevel = riskAlgorithm.calculateRiskFromSymptomAndBpReading(riskAlgorithmDTO);
        Assertions.assertEquals(Constants.HIGH, riskLevel);
    }

    @Test
    void getPatientSymptomsCategoriesCount() {
        //given
        RiskAlgorithmDTO riskAlgorithmDTO = TestDataProvider.getRiskAlgorithmDto();
        Symptom symptom = new Symptom();
        symptom.setName(Constants.OTHER);

        //when
        when(symptomService.getSymptoms()).thenReturn(List.of(symptom));

        //then
        Map<String, Integer> riskLevel = riskAlgorithm.getPatientSymptomsCategoriesCount(riskAlgorithmDTO);
        Assertions.assertEquals(4, riskLevel.size());
    }

    @Test
    void toVerifyGetPatientSymptomsCategoriesCount() {
        //given
        Map<String, Boolean> booleanMap = new HashMap<>();
        booleanMap.put(Constants.STRING_TWO, true);
        booleanMap.put(Constants.STRING_THREE, true);
        booleanMap.put(Constants.STRING_FOUR, true);
        booleanMap.put(Constants.STRING_FIVE, true);
        RiskAlgorithmDTO riskAlgorithmDTO = TestDataProvider.getRiskAlgorithmDto();
        Symptom symptom = new Symptom();
        symptom.setId(1L);
        symptom.setCategories(booleanMap);

        //when
        when(symptomService.getSymptoms()).thenReturn(List.of(symptom));

        //then
        Map<String, Integer> riskLevel = riskAlgorithm.getPatientSymptomsCategoriesCount(riskAlgorithmDTO);
        Assertions.assertEquals(4, riskLevel.size());
    }

    @Test
    void testGetPatientSymptomsCategoriesCount() {
        //given
        Map<String, Boolean> booleanMap = new HashMap<>();
        booleanMap.put(Constants.STRING_TWO, false);
        booleanMap.put(Constants.STRING_THREE, false);
        booleanMap.put(Constants.STRING_FOUR, false);
        booleanMap.put(Constants.STRING_FIVE, false);
        RiskAlgorithmDTO riskAlgorithmDTO = TestDataProvider.getRiskAlgorithmDto();
        Symptom symptom = new Symptom();
        symptom.setId(1L);
        symptom.setCategories(booleanMap);

        //when
        when(symptomService.getSymptoms()).thenReturn(List.of(symptom));

        //then
        Map<String, Integer> riskLevel = riskAlgorithm.getPatientSymptomsCategoriesCount(riskAlgorithmDTO);
        Assertions.assertEquals(4, riskLevel.size());
    }

    @Test
    void calculateRiskLevelFromSymptoms() {
        //given
        RiskAlgorithmDTO riskAlgorithmDTO = TestDataProvider.getRiskAlgorithmDto();
        Map<String, Integer> patientSymptomsCount = new HashMap<>();
        patientSymptomsCount.put(Constants.CATEGORY_FOUR_COUNT, 1);
        patientSymptomsCount.put(Constants.CATEGORY_FIVE_COUNT, 1);
        patientSymptomsCount.put(Constants.CATEGORY_THREE_COUNT, 1);

        //then
        String riskLevel = riskAlgorithm.calculateRiskLevelFromSymptoms(patientSymptomsCount, riskAlgorithmDTO);
        Assertions.assertEquals(Constants.HIGH, riskLevel);
    }

    @Test
    void testCalculateRiskLevelFromSymptoms() {
        //given
        RiskAlgorithmDTO riskAlgorithmDTO = new RiskAlgorithmDTO();
        riskAlgorithmDTO.setAvgSystolic(100);
        riskAlgorithmDTO.setAvgDiastolic(50);
        Map<String, Integer> patientSymptomsCount = new HashMap<>();
        patientSymptomsCount.put(Constants.CATEGORY_TWO_COUNT, 1);
        patientSymptomsCount.put(Constants.CATEGORY_FOUR_COUNT, 0);
        patientSymptomsCount.put(Constants.CATEGORY_FIVE_COUNT, 0);
        patientSymptomsCount.put(Constants.CATEGORY_THREE_COUNT, 0);

        //then
        String riskLevel = riskAlgorithm.calculateRiskLevelFromSymptoms(patientSymptomsCount, riskAlgorithmDTO);
        Assertions.assertEquals(Constants.HIGH, riskLevel);
    }

    @Test
    void calculateRiskLevelFromGlucoseData() {
        //then
        String riskLevel = riskAlgorithm.calculateRiskLevelFromGlucoseData(13.0, Constants.RBS,
                Constants.LOW);
        Assertions.assertEquals(Constants.HIGH, riskLevel);

        //then
        riskLevel = riskAlgorithm.calculateRiskLevelFromGlucoseData(3.0, Constants.FBS, Constants.LOW);
        Assertions.assertEquals(Constants.HIGH, riskLevel);

        //then
        riskLevel = riskAlgorithm.calculateRiskLevelFromGlucoseData(3.0, Constants.FBS, Constants.HIGH);
        Assertions.assertEquals(Constants.HIGH, riskLevel);

        //then
        riskLevel = riskAlgorithm.calculateRiskLevelFromGlucoseData(12.0, Constants.RBS, Constants.LOW);
        Assertions.assertEquals(Constants.HIGH, riskLevel);

        //then
        riskLevel = riskAlgorithm.calculateRiskLevelFromGlucoseData(11.0, Constants.RBS, Constants.LOW);
        Assertions.assertEquals(Constants.GLUCOSE_MODERATE, riskLevel);

        riskLevel = riskAlgorithm.calculateRiskLevelFromGlucoseData(8.8, Constants.RBS, Constants.LOW);
        Assertions.assertEquals(Constants.GLUCOSE_MODERATE, riskLevel);

        

        //then
        riskLevel = riskAlgorithm.calculateRiskLevelFromGlucoseData(8.0, Constants.FBS, Constants.MODERATE);
        Assertions.assertEquals(Constants.BOTH_MODERATE, riskLevel);

        //then
        riskLevel = riskAlgorithm.calculateRiskLevelFromGlucoseData(8.0, Constants.RBS,
                Constants.HIGHER_MODERATE);
        Assertions.assertEquals(Constants.BOTH_HIGHER_MODERATE, riskLevel);

        //then
        riskLevel = riskAlgorithm.calculateRiskLevelFromGlucoseData(8.0, Constants.RBS, Constants.HIGH);
        Assertions.assertEquals(Constants.HIGH, riskLevel);
    }

    @Test
    void getRiskLevelForNewPatient() {
        //given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        patientTracker.setIsPregnant(true);
        PatientTracker newPatientTracker = TestDataProvider.getPatientTracker();
        RedRiskDTO redRiskDTO = new RedRiskDTO(1, Constants.DIABETES_UNCONTROLLED_OR_POORLY_CONTROLLED,
                "testPatient");
        RedRiskDTO newRedRiskDTO = new RedRiskDTO(0,
                Constants.DIABETES_UNCONTROLLED_OR_POORLY_CONTROLLED, "testPatient");

        //then
        String riskLevel = riskAlgorithm.getRiskLevelForNewPatient(patientTracker, redRiskDTO);
        Assertions.assertEquals(Constants.HIGH, riskLevel);
        riskLevel = riskAlgorithm.getRiskLevelForNewPatient(newPatientTracker, newRedRiskDTO);
        Assertions.assertEquals(Constants.HIGH, riskLevel);
    }

    @Test
    void toVerifyGetRiskLevelForNewPatient() {
        //given
        PatientTracker patientTracker = new PatientTracker();
        patientTracker.setPatientId(1L);
        patientTracker.setAvgSystolic(180);
        patientTracker.setAvgDiastolic(110);
        patientTracker.setWeight(65d);
        patientTracker.setBmi(32.2d);
        patientTracker.setGender(Constants.GENDER_MALE);
        patientTracker.setTenantId(4l);
        patientTracker.setAge(55);
        patientTracker.setIsRegularSmoker(Constants.BOOLEAN_TRUE);
        patientTracker.setGlucoseType(Constants.FBS);
        patientTracker.setGlucoseValue(5.7);
        RedRiskDTO redRiskDTO = new RedRiskDTO(0, null, null);

        //then
        String riskLevel = riskAlgorithm.getRiskLevelForNewPatient(patientTracker, redRiskDTO);
        Assertions.assertEquals(Constants.HIGH, riskLevel);
    }

    @Test
    void testGetRiskLevelForNewPatient() {
        //given
        PatientTracker patientTracker = new PatientTracker();
        patientTracker.setPatientId(1L);
        patientTracker.setAvgSystolic(160);
        patientTracker.setAvgDiastolic(100);
        patientTracker.setWeight(65d);
        patientTracker.setBmi(32.2d);
        patientTracker.setGender(Constants.GENDER_MALE);
        patientTracker.setTenantId(4l);
        patientTracker.setAge(55);
        patientTracker.setIsRegularSmoker(Constants.BOOLEAN_TRUE);
        patientTracker.setGlucoseType(Constants.FBS);
        patientTracker.setGlucoseValue(5.7);
        RedRiskDTO redRiskDTO = new RedRiskDTO(0, null, null);

        //then
        String riskLevel = riskAlgorithm.getRiskLevelForNewPatient(patientTracker, redRiskDTO);
        Assertions.assertEquals(Constants.HIGH, riskLevel);
    }

    @Test
    void calculateRiskFromBpAndRiskFactors() {
        //given
        PatientTracker patientTracker = new PatientTracker();
        patientTracker.setPatientId(1L);
        patientTracker.setAvgSystolic(140);
        patientTracker.setAvgDiastolic(90);
        patientTracker.setWeight(65d);
        patientTracker.setBmi(32.2d);
        patientTracker.setGender(Constants.GENDER_MALE);
        patientTracker.setTenantId(4l);
        patientTracker.setAge(55);
        patientTracker.setIsRegularSmoker(Constants.BOOLEAN_TRUE);
        patientTracker.setGlucoseType(Constants.FBS);
        patientTracker.setGlucoseValue(5.7);
        RedRiskDTO redRiskDTO = new RedRiskDTO(0, null, null);

        //then
        String riskLevel = riskAlgorithm.getRiskLevelForNewPatient(patientTracker, redRiskDTO);
        Assertions.assertEquals(Constants.HIGH, riskLevel);
    }

    @Test
    void toVerifyCalculateRiskFromBpAndRiskFactors() {
        //given
        PatientTracker patientTracker = new PatientTracker();
        patientTracker.setPatientId(1L);
        patientTracker.setAvgSystolic(130);
        patientTracker.setAvgDiastolic(80);
        patientTracker.setWeight(65d);
        patientTracker.setBmi(32.2d);
        patientTracker.setGender(Constants.GENDER_MALE);
        patientTracker.setTenantId(4l);
        patientTracker.setAge(55);
        patientTracker.setIsRegularSmoker(Constants.BOOLEAN_TRUE);
        patientTracker.setGlucoseType(Constants.FBS);
        patientTracker.setGlucoseValue(5.7);
        RedRiskDTO redRiskDTO = new RedRiskDTO(0, null, null);

        //then
        String riskLevel = riskAlgorithm.getRiskLevelForNewPatient(patientTracker, redRiskDTO);
        Assertions.assertEquals(Constants.MODERATE, riskLevel);
    }

    @Test
    void testCalculateRiskFromBpAndRiskFactorsWithNull() {
        //given
        PatientTracker patientTracker = new PatientTracker();
        patientTracker.setPatientId(1L);
        patientTracker.setAvgSystolic(10);
        patientTracker.setAvgDiastolic(10);
        patientTracker.setWeight(65d);
        patientTracker.setBmi(32.2d);
        patientTracker.setGender(Constants.GENDER_MALE);
        patientTracker.setTenantId(4l);
        patientTracker.setAge(55);
        patientTracker.setIsRegularSmoker(Constants.BOOLEAN_TRUE);
        patientTracker.setGlucoseType(Constants.FBS);
        patientTracker.setGlucoseValue(5.7);
        RedRiskDTO redRiskDTO = new RedRiskDTO(0, null, null);

        //then
        String riskLevel = riskAlgorithm.getRiskLevelForNewPatient(patientTracker, redRiskDTO);
        Assertions.assertEquals(Constants.MODERATE, riskLevel);
    }

    @Test
    void getRiskFactorsCount() {
        //given
        PatientTracker patientTracker = new PatientTracker();
        patientTracker.setPatientId(1L);
        patientTracker.setAvgSystolic(180);
        patientTracker.setAvgDiastolic(110);
        patientTracker.setWeight(65d);
        patientTracker.setBmi(29.0);
        patientTracker.setGender(Constants.GENDER_FEMALE);
        patientTracker.setTenantId(4l);
        patientTracker.setAge(65);
        patientTracker.setIsRegularSmoker(Constants.BOOLEAN_FALSE);
        patientTracker.setGlucoseType(null);
        patientTracker.setGlucoseValue(5.0);

        //then
        int riskFactorCount = riskAlgorithm.getRiskFactorsCount(patientTracker);
        Assertions.assertEquals(1, riskFactorCount);
    }

    @Test
    void toVerifyGetRiskFactorsCount() {
        //given
        PatientTracker patientTracker = new PatientTracker();
        patientTracker.setPatientId(1L);
        patientTracker.setAvgSystolic(180);
        patientTracker.setAvgDiastolic(110);
        patientTracker.setWeight(65d);
        patientTracker.setBmi(29.0);
        patientTracker.setGender(Constants.GENDER);
        patientTracker.setTenantId(4l);
        patientTracker.setAge(10);
        patientTracker.setIsRegularSmoker(Constants.BOOLEAN_FALSE);
        patientTracker.setGlucoseType(null);
        patientTracker.setGlucoseValue(5.0);

        //then
        int riskFactorCount = riskAlgorithm.getRiskFactorsCount(patientTracker);
        Assertions.assertEquals(0, riskFactorCount);
    }
}