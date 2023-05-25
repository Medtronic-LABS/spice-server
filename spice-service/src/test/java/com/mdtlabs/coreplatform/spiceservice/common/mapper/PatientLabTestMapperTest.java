package com.mdtlabs.coreplatform.spiceservice.common.mapper;

import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTestResult;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * <p>
 * PatientLabTestMapperTest class used to test all possible positive
 * and negative cases for all methods and conditions used in PatientLabTestMapper class.
 * </p>
 *
 * @author Jaganathan created on Mar 2, 2023
 */
@ExtendWith(MockitoExtension.class)
class PatientLabTestMapperTest {

    @InjectMocks
    private PatientLabTestMapper labTestMapper;

    @Test
    @DisplayName("SetPatientLabTestDTO Test")
    void setPatientLabTestDTO() {
        //given
        PatientLabTest patientLabTest = TestDataProvider.getPatientLabTest();
        PatientLabTestDTO patientLabTestDTO = TestDataProvider.getPatientLabTestDTO();
        //then
        PatientLabTestDTO actualPatientLabTest = labTestMapper.setPatientLabTestDto(patientLabTest, patientLabTestDTO);
        Assertions.assertEquals(patientLabTestDTO, actualPatientLabTest);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void setPatientLabTestResult(boolean isAbnormal) {
        //given
        PatientLabTestResultRequestDTO patientLabTestResultRequestDTO = TestDataProvider.getLabTestResultRequestDto();
        PatientLabTest patientLabTest = TestDataProvider.getPatientLabTest();
        PatientLabTestResultDTO patientLabTestResultDTO = TestDataProvider.getPatientLabTestResultDto();
        patientLabTestResultDTO.setIsAbnormal(isAbnormal);
        PatientLabTestResult patientLabTestResult = TestDataProvider.getPatientLabTestResultData();
        //then
        PatientLabTestResult actualPatientLabTestResult = labTestMapper
                .setPatientLabTestResult(patientLabTestResultRequestDTO, patientLabTest, patientLabTestResultDTO,
                        patientLabTestResult);
        Assertions.assertEquals(patientLabTestResult, actualPatientLabTestResult);
    }

    @Test
    void setPatientLabTest() {
        //given
        TestDataProvider.init();
        PatientLabTestResultRequestDTO patientLabTestResultRequestDTO = TestDataProvider.getLabTestResultRequestDto();
        PatientLabTest patientLabTest = TestDataProvider.getPatientLabTest();
        //then
        TestDataProvider.getStaticMock();
        PatientLabTest actualPatientLabTest = labTestMapper.setPatientLabTest(patientLabTestResultRequestDTO,
                patientLabTest);
        TestDataProvider.cleanUp();
        Assertions.assertEquals(patientLabTest, actualPatientLabTest);
    }
}
