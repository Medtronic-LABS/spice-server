package com.mdtlabs.coreplatform.spiceadminservice.labtest.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTest;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.labtest.service.impl.LabTestServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Lab test controller.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class LabTestControllerTest {

    @InjectMocks
    private LabTestController labTestController;

    @Mock
    private LabTestServiceImpl labTestService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(LabTestController.class, "modelMapper", labTestController);
    }

    @Test
    void testAddLabTest() {
        //given
        LabTest labTest = TestDataProvider.getLabTest();
        LabTestDTO labTestDTO = TestDataProvider.getLabTestDTO();

        //when
        when(labTestService.addLabTest(labTest)).thenReturn(labTest);
        when(modelMapper.map(labTestDTO, LabTest.class)).thenReturn(labTest);

        //then
        SuccessResponse<LabTest> actualLabTest = labTestController.addLabTest(labTestDTO);
        assertNotNull(actualLabTest);
        assertEquals(HttpStatus.CREATED, actualLabTest.getStatusCode());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {Constants.ONE, Constants.ZERO})
    void testGetAllLabTests(Long totalCount) {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        ResponseListDTO responseListDTO = TestDataProvider.getResponseListDTO();
        responseListDTO.setTotalCount(totalCount);

        //when
        when(labTestService.getAllLabTests(requestDTO)).thenReturn(responseListDTO);

        //then
        SuccessResponse<List<LabTestDTO>> actualLabTests = labTestController.getAllLabTests(requestDTO);
        assertNotNull(actualLabTests);
        assertEquals(HttpStatus.OK, actualLabTests.getStatusCode());
    }

    @Test
    void testSearchLabTests() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);

        //when
        when(labTestService.searchLabTests(requestDTO)).thenReturn(null);

        //then
        List<Map> emptyLabTests = labTestController.searchLabtests(requestDTO);
        assertNotNull(emptyLabTests);
        assertTrue(emptyLabTests.isEmpty());
    }

    @Test
    void toVerifySearchLabTests() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.TWO);

        //when
        when(labTestService.searchLabTests(requestDTO)).thenReturn(List.of(Map.of(TestConstants.ONE, TestDataProvider.getLabTest())));

        //then
        List<Map> actualLabTests = labTestController.searchLabtests(requestDTO);
        assertNotNull(actualLabTests);
        assertEquals(TestConstants.ONE, actualLabTests.size());
    }

    @Test
    void testRemoveLabTest() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);

        //when
        when(labTestService.removeLabTest(requestDTO, Boolean.TRUE)).thenReturn(Boolean.TRUE);

        //then
        SuccessResponse<Boolean> isLabTestDeletedResponse = labTestController.removeLabTest(requestDTO);
        assertNotNull(isLabTestDeletedResponse);
        assertEquals(HttpStatus.OK, isLabTestDeletedResponse.getStatusCode());
    }

    @Test
    void testUpdateLabTest() {
        //given
        LabTest labTest = TestDataProvider.getLabTest();
        LabTestDTO labTestDTO = TestDataProvider.getLabTestDTO();

        //when
        when(labTestService.updateLabTest(labTest)).thenReturn(labTest);
        when(modelMapper.map(labTestDTO, LabTest.class)).thenReturn(labTest);

        //then
        SuccessResponse<LabTest> actualLabTest = labTestController.updateLabTest(labTestDTO);
        assertNotNull(actualLabTest);
        assertEquals(HttpStatus.OK, actualLabTest.getStatusCode());
    }


    @Test
    void testGetLabTestById() {
        //given
        LabTest labTest = TestDataProvider.getLabTest();
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);

        //when
        when(labTestService.getLabTestById(requestDTO)).thenReturn(labTest);

        //then
        SuccessResponse<LabTest> actualLabTest = labTestController.getLabTestById(requestDTO);
        assertNotNull(actualLabTest);
        assertEquals(HttpStatus.OK, actualLabTest.getStatusCode());
    }

    @Test
    void testGetLabTestResultsById() {
        //given
        List<LabTestResultDTO> labTestResults = List.copyOf(new ModelMapper().map(TestDataProvider.getLabTestResults(), new TypeToken<Set<LabTestResultDTO>>() {
        }.getType()));

        //when
        when(labTestService.getLabTestResultsById(TestConstants.ONE)).thenReturn(labTestResults);

        //then
        List<LabTestResultDTO> actualLabTestResults = labTestController.getLabTestResultsById(TestConstants.ONE);
        assertNotNull(actualLabTestResults);
        assertFalse(actualLabTestResults.isEmpty());
        assertEquals(labTestResults.size(), actualLabTestResults.size());
        assertEquals(labTestResults.get(0), actualLabTestResults.get(0));
    }

    @Test
    void testGetLabTestByName() {
        //given
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto(Constants.EMPTY, Constants.ZERO,
                TestConstants.TEN);
        searchRequestDTO.setCountryId(TestConstants.ONE);
        LabTest labTest = TestDataProvider.getLabTest();

        //when
        when(labTestService.getLabTestByName(searchRequestDTO.getSearchTerm(), searchRequestDTO.getCountryId()))
                .thenReturn(labTest);

        //then
        ResponseEntity<LabTest> actualLabTest = labTestController.getLabTestbyName(searchRequestDTO);
        assertNotNull(actualLabTest);
        assertEquals(HttpStatus.OK, actualLabTest.getStatusCode());
        assertEquals(labTest, actualLabTest.getBody());
    }

    @Test
    void testGetLabTestsById() {
        //given
        List<LabTest> labTests = TestDataProvider.getLabTests();
        Set<Long> labTestIds = Set.of(TestConstants.ONE, TestConstants.TWO);

        //when
        when(labTestService.getLabTestsById(labTestIds)).thenReturn(labTests);

        //then
        ResponseEntity<List<LabTest>> actualLabTests = labTestController.getLabTestsById(labTestIds);
        assertNotNull(actualLabTests);
        assertEquals(HttpStatus.OK, actualLabTests.getStatusCode());
        assertEquals(labTests, actualLabTests.getBody());
    }
}
