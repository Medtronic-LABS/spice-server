package com.mdtlabs.coreplatform.spiceservice.common;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.RedRiskDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComorbidity;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientDiagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientComorbidityRepository;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PatientDiagnosisRepository;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * <p>
 * Red Risk Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Mar 07, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RedRiskServiceTest {

    @InjectMocks
    private RedRiskService redRiskService;

    @Mock
    private PatientDiagnosisRepository patientDiagnosisRepository;

    @Mock
    private PatientComorbidityRepository patientComorbidityRepository;

    @Mock
    private RiskAlgorithm riskAlgorithm;

    @ParameterizedTest
    @CsvSource({",Diabetes Mellitus Type 1", "unknown, Diabetes Mellitus Type 1", "known,", "known, Diabetes Mellitus Type 1"})
    void getPatientRiskLevel(String diabetesType, String type) {
        //given
        String riskLevel = Constants.HIGH;
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        PatientDiagnosis patientDiagnosis = TestDataProvider.getPatientDiagnosis();
        patientDiagnosis.setDiabetesDiagControlledType(type);
        RedRiskDTO redRiskDTO = new RedRiskDTO(1, Constants.DIABETES_MELLITUS_TYPE_1,
                diabetesType);
        PatientComorbidity patientComorbidity = TestDataProvider.getPatientComorbidity();

        //when
        when(patientDiagnosisRepository.findByPatientTrackIdAndIsActiveAndIsDeleted(patientTracker.getId(),
                true, false)).thenReturn(patientDiagnosis);
        when(patientComorbidityRepository.getByTrackerId(patientTracker.getId(), Constants.BOOLEAN_TRUE,
                Constants.BOOLEAN_FALSE)).thenReturn(List.of(patientComorbidity));
        when(riskAlgorithm.getRiskLevelForNewPatient(patientTracker, redRiskDTO)).thenReturn(riskLevel);

        //then
        String result = redRiskService.getPatientRiskLevel(patientTracker, patientDiagnosis, redRiskDTO);
        Assertions.assertEquals(result, riskLevel);
    }

    @Test
    void testGetPatientRiskLevelWithNull() {
        //given
        String riskLevel = Constants.HIGH;
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        RedRiskDTO redRiskDTO = new RedRiskDTO(1, Constants.DIABETES_UNCONTROLLED_OR_POORLY_CONTROLLED,
                Constants.PATIENT);
        PatientComorbidity patientComorbidity = TestDataProvider.getPatientComorbidity();
        PatientDiagnosis patientDiagnosis = TestDataProvider.getPatientDiagnosis();

        //when
        when(patientDiagnosisRepository.findByPatientTrackIdAndIsActiveAndIsDeleted(patientTracker.getId(),
                true, false)).thenReturn(null);
        when(patientComorbidityRepository.getByTrackerId(patientTracker.getId(), Constants.BOOLEAN_TRUE,
                Constants.BOOLEAN_FALSE)).thenReturn(List.of(patientComorbidity));
        when(riskAlgorithm.getRiskLevelForNewPatient(patientTracker, redRiskDTO)).thenReturn(riskLevel);

        //then
        String resultWithNull = redRiskService.getPatientRiskLevel(patientTracker, null, redRiskDTO);
        String result = redRiskService.getPatientRiskLevel(patientTracker, patientDiagnosis, redRiskDTO);

        Assertions.assertEquals(result, riskLevel);
        Assertions.assertEquals(resultWithNull, riskLevel);
    }

    @Test
    void getPatientRiskLevel() {
        //given
        String riskLevel = Constants.HIGH;
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        RedRiskDTO redRiskDTO = new RedRiskDTO(1, Constants.DIABETES_UNCONTROLLED_OR_POORLY_CONTROLLED,
                Constants.PATIENT);
        PatientComorbidity patientComorbidity = TestDataProvider.getPatientComorbidity();
        PatientDiagnosis patientDiagnosis = TestDataProvider.getPatientDiagnosis();
        patientDiagnosis.setDiabetesDiagControlledType(null);

        //when
        when(patientDiagnosisRepository.findByPatientTrackIdAndIsActiveAndIsDeleted(patientTracker.getId(),
                true, false)).thenReturn(patientDiagnosis);
        when(patientComorbidityRepository.getByTrackerId(patientTracker.getId(), Constants.BOOLEAN_TRUE,
                Constants.BOOLEAN_FALSE)).thenReturn(List.of(patientComorbidity));
        when(riskAlgorithm.getRiskLevelForNewPatient(patientTracker, redRiskDTO)).thenReturn(riskLevel);

        //then
        String result = redRiskService.getPatientRiskLevel(patientTracker, patientDiagnosis, redRiskDTO);
        Assertions.assertEquals(result, riskLevel);
    }
}