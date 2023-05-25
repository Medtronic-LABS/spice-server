package com.mdtlabs.coreplatform.spiceservice.patientlabtest.controller;

import com.mdtlabs.coreplatform.common.model.dto.spice.GetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTestResult;
import com.mdtlabs.coreplatform.spiceservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.service.PatientLabTestService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * <p>
 * PatientLabTestControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in PatientLabTestController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class PatientLabTestControllerTest {

    @InjectMocks
    private PatientLabTestController patientLabTestController;

    @Mock
    private PatientLabTestService patientLabTestService;

    @Mock
    private UserApiInterface apiInterface;

    //given
    private List<PatientLabTest> patientLabTestList = new ArrayList<>();

    private PatientLabTestRequestDTO requestDTO = TestDataProvider.getPatientLabTestRequest();

    private GetRequestDTO getRequestDTO = TestDataProvider.getGetRequestDTO();

    private PatientLabTestResponseDTO responseDTO = TestDataProvider.getLabTestResponse();

    private PatientLabTestResultRequestDTO resultRequestDTO = TestDataProvider.getLabTestResultRequestDto();

    private PatientLabTestResultResponseDTO resultResponseDTO = TestDataProvider.getLabTestResultResponseDTO();

    private List<PatientLabTestResult> result = new ArrayList<>();

    //response
    private SuccessResponse<List<PatientLabTestResult>> resultList;

    private SuccessResponse<PatientLabTest> successResponse;

    private SuccessResponse<PatientLabTestResponseDTO> testResponseDTO;

    private SuccessResponse<String> stringSuccessResponse;

    @Test
    @DisplayName("CreateLabTest Test")
    void createPatientLabTest() {
        //given
        patientLabTestList.add(TestDataProvider.getPatientLabTest());
        //when
        when(patientLabTestService.createPatientLabTest(requestDTO)).thenReturn(patientLabTestList);
        //then
        successResponse = patientLabTestController.createPatientLabTest(requestDTO);
        Assertions.assertEquals(HttpStatus.CREATED, successResponse.getStatusCode());
        Assertions.assertEquals(patientLabTestList.get(0).getLabTestId(),
                patientLabTestService.createPatientLabTest(requestDTO).get(0).getLabTestId());
        Assertions.assertNotEquals(0, patientLabTestService.createPatientLabTest(requestDTO).size());
    }

    @Test
    @DisplayName("GetPatientLabTestList")
    void getPatientLabTestList() {
        //when
        when(patientLabTestService.getPatientLabTestList(getRequestDTO)).thenReturn(responseDTO);
        //then
        testResponseDTO = patientLabTestController.getPatientLabTestList(getRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, testResponseDTO.getStatusCode());
        Assertions.assertEquals(responseDTO.getPatientLabTest(),
                patientLabTestService.getPatientLabTestList(getRequestDTO).getPatientLabTest());
        Assertions.assertEquals(responseDTO.getPatientLabTest().size(),
                patientLabTestService.getPatientLabTestList(getRequestDTO).getPatientLabTest().size());
    }

    @Test
    @DisplayName("RemovePatientLabTest Test")
    void removePatientLabTest() {
        //given
        getRequestDTO.setDeleted(true);
        getRequestDTO.setIsActive(false);
        //when
        when(patientLabTestService.removePatientLabTest(getRequestDTO)).thenReturn(true);
        //then
        stringSuccessResponse = patientLabTestController.removePatientLabTest(getRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, stringSuccessResponse.getStatusCode());
        Assertions.assertEquals(true, patientLabTestService.removePatientLabTest(getRequestDTO));
        Assertions.assertNotEquals(false, patientLabTestService.removePatientLabTest(getRequestDTO));
    }

    @Test
    @DisplayName("ReviewPatientLabTest Test")
    void reviewPatientLabTest() {
        //when
        doNothing().when(patientLabTestService).reviewPatientLabTest(getRequestDTO);
        //then
        stringSuccessResponse = patientLabTestController.reviewPatientLabTest(getRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, stringSuccessResponse.getStatusCode());
    }

    @Test
    @DisplayName("CreatePatientLabTestResult Test")
    void createPatientLabTestResult() {
        //given
        result.add(TestDataProvider.getPatientLabTestResultData());
        //when
        when(patientLabTestService.createPatientLabTestResult(resultRequestDTO)).thenReturn(result);
        //then
        resultList = patientLabTestController.createPatientLabTestResult(resultRequestDTO);
        Assertions.assertEquals(HttpStatus.CREATED, resultList.getStatusCode());
        Assertions.assertEquals(resultRequestDTO.getPatientLabTestResults().get(0).getDisplayName(),
                patientLabTestService.createPatientLabTestResult(resultRequestDTO).get(0).getDisplayName());

    }

    @Test
    @DisplayName("GetPatientLabTestResult Test")
    void getPatientLabTestResults() {
        //when
        when(patientLabTestService.getPatientLabTestResults(resultRequestDTO)).thenReturn(resultResponseDTO);
        //then
        stringSuccessResponse = patientLabTestController.getPatientLabTestResults(resultRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, stringSuccessResponse.getStatusCode());
        Assertions.assertEquals(resultRequestDTO.getPatientLabTestResults().get(0).getDisplayName(),
                resultResponseDTO.getPatientLabTestResults().get(0).getDisplayName());
        Assertions.assertEquals(resultRequestDTO.getPatientLabTestResults().get(0).getResultValue(),
                patientLabTestService.getPatientLabTestResults(resultRequestDTO)
                        .getPatientLabTestResults().get(0).getResultValue());
    }

    @Test
    @DisplayName("GetPatientLabTest Test")
    void getPatientLabTest() {
        //given
        RequestDTO request = TestDataProvider.getRequestDto();
        List<LabTest> labTestList = List.of(TestDataProvider.getLabTestData());
        //when
        when(patientLabTestService.getLabTest(request)).thenReturn(labTestList);
        //then
        List<LabTest> actualList = patientLabTestService.getLabTest(request);
        SuccessResponse<List<Map>> successResponse = patientLabTestController.getPatientLabTest(request);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        Assertions.assertEquals(labTestList, actualList);
    }

    @Test
    @DisplayName("GetPatientLabTestResult Test")
    void getPatientLabTestResult() {
        //given
        long labTestId = 1l;
        List<LabTestResultDTO> results = List.of(TestDataProvider.getLabTestResultDTO());
        //when
        when(patientLabTestService.getLabTestResults(labTestId)).thenReturn(results);
        //then
        List<LabTestResultDTO> actualResult = patientLabTestService.getLabTestResults(labTestId);
        SuccessResponse<List<LabTestResultDTO>> successResponse = patientLabTestController
                .getPatientLabTestResults(labTestId);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        Assertions.assertEquals(results, actualResult);
        Assertions.assertEquals(results.get(0).getLabTestId(), actualResult.get(0).getLabTestId());
    }
}
