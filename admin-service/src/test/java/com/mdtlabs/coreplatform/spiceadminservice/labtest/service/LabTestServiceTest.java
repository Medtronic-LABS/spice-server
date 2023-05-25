package com.mdtlabs.coreplatform.spiceadminservice.labtest.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResult;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.labtest.repository.LabTestRepository;
import com.mdtlabs.coreplatform.spiceadminservice.labtest.service.impl.LabTestServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.labtestresultranges.service.LabTestResultRangesService;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Lab test service implementation.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LabTestServiceTest {

    @InjectMocks
    private LabTestServiceImpl labTestService;

    @Mock
    private LabTestRepository labTestRepository;

    @Mock
    private LabTestResultRangesService labTestResultRangesService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(LabTestServiceImpl.class, "mapper", labTestService);
    }

    @Test
    void testAddLabTest() {
        //given
        LabTest labTest = TestDataProvider.getLabTest();
        labTest.setTenantId(TestConstants.ONE);
        labTest.setCountryId(TestConstants.TWO);
        labTest.setLabTestResults(TestDataProvider.getLabTestResults());
        LabTest labTestCountryDetail = TestDataProvider.getLabTest();
        labTestCountryDetail.setTenantId(TestConstants.TWO);

        //when
        when(labTestRepository.save(labTest)).thenReturn(labTest);
        when(labTestRepository.findByCountryIdAndNameAndIsDeletedAndTenantId(labTest.getCountryId(), labTest.getName(), false, labTest.getTenantId()))
                .thenReturn(labTestCountryDetail);

        //then
        LabTest actualLabTest = labTestService.addLabTest(labTest);
        assertNotNull(actualLabTest);
        assertEquals(labTest.getName(), actualLabTest.getName());
        assertEquals(labTest.getLabTestResults().size(), actualLabTest.getLabTestResults().size());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testGetAllLabTests(boolean isPaginated) {
        //given
        RequestDTO requestDTOEmptyList = TestDataProvider.getRequestDtoForPagination("a",
                Constants.ZERO, TestConstants.TEN);
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(Constants.EMPTY, Constants.ZERO,
                TestConstants.TEN);
        requestDTO.setPaginated(isPaginated);
        List<LabTest> labTests = TestDataProvider.getLabTests();
        Page<LabTest> labTestsPage = new PageImpl<>(labTests);
        Pageable pageable = PageRequest.of(Constants.ZERO, requestDTO.getLimit(), Sort.by(Constants.UPDATED_AT)
                .descending());
        List<LabTestDTO> labTestDTOs = TestDataProvider.getLabTestDTOs();

        //when
        when(labTestRepository.getAllLabTests(Constants.EMPTY, requestDTO.getCountryId(),
                requestDTO.getTenantId(), pageable)).thenReturn(labTestsPage);

        when(labTestRepository.getAllLabTests(Constants.EMPTY, requestDTO.getCountryId(),
                requestDTO.getTenantId(), null)).thenReturn(labTestsPage);

        when(labTestRepository.getAllLabTests("a", requestDTO.getCountryId(),
                requestDTO.getTenantId(), pageable)).thenReturn(Page.empty());

        when(labTestRepository.getAllLabTests("a", requestDTO.getCountryId(),
                requestDTO.getTenantId(), null)).thenReturn(Page.empty());
        when(modelMapper.map(labTests.get(0), LabTestDTO.class)).thenReturn(labTestDTOs.get(0));
        when(modelMapper.map(labTests.get(1), LabTestDTO.class)).thenReturn(labTestDTOs.get(1));

        //then
        ResponseListDTO actualResponse = labTestService.getAllLabTests(requestDTO);
        ResponseListDTO actualResponseEmpty = labTestService.getAllLabTests(requestDTOEmptyList);
        assertNotNull(actualResponseEmpty);
        assertNull(actualResponseEmpty.getData());
        assertNull(actualResponseEmpty.getTotalCount());
        assertNotNull(actualResponse);
        assertEquals(TestConstants.TWO, actualResponse.getTotalCount());
        assertEquals(labTestDTOs, actualResponse.getData());
    }

    @Test
    void testSearchLabTests() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(true, TestConstants.ONE);
        requestDTO.setSearchTerm(TestConstants.LAB_TEST_NAME);
        requestDTO.setCountryId(TestConstants.ONE);
        List<LabTest> labTests = TestDataProvider.getLabTests();
        List<Map> expectedLabTests = labTests.stream().map(labTest -> Map.of(FieldConstants.ID, labTest.getId(),
                        FieldConstants.NAME, labTest.getName(), FieldConstants.COUNTRY, labTest.getCountryId()))
                .collect(Collectors.toList());

        //when
        when(labTestRepository.searchLabTests(requestDTO.getSearchTerm(), requestDTO.getCountryId(),
                requestDTO.getIsActive())).thenReturn(labTests);

        //then
        List<Map> actualLabTests = labTestService.searchLabTests(requestDTO);
        assertNotNull(actualLabTests);
        assertEquals(expectedLabTests.get(0), actualLabTests.get(0));
        assertEquals(expectedLabTests.size(), actualLabTests.size());
    }

    @Test
    void testRemoveLabTest() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(true, TestConstants.ONE);
        requestDTO.setId(TestConstants.ONE);
        LabTest labTest = TestDataProvider.getLabTest();
        labTest.setLabTestResults(TestDataProvider.getLabTestResults());

        //when
        when(labTestRepository.findByIdAndIsDeletedAndTenantId(requestDTO.getId(),
                Constants.BOOLEAN_FALSE, TestConstants.ONE)).thenReturn(labTest);
        when(labTestRepository.save(labTest)).thenReturn(labTest);

        //then
        boolean actualResponse = labTestService.removeLabTest(requestDTO, true);
        assertTrue(actualResponse);
    }

    @Test
    void testUpdateLabTest() {
        //given
        LabTest labTest = TestDataProvider.getLabTest();
        labTest.setId(TestConstants.ONE);
        labTest.setTenantId(TestConstants.ONE);
        LabTest existingLabTest = TestDataProvider.getLabTest();
        Set<LabTestResult> labTestResults = new HashSet<>(TestDataProvider.getLabTestResults());
        LabTestResult labTestResult = TestDataProvider.getLabTestResult();
        labTestResult.setId(TestConstants.FOUR);
        labTestResult.setDeleted(Boolean.TRUE);
        labTestResults.add(labTestResult);
        existingLabTest.setLabTestResults(labTestResults);
        labTest.setLabTestResults(labTestResults);

        //when
        when(labTestRepository.findByIdAndIsDeletedAndTenantId(labTest.getId(), Constants.BOOLEAN_FALSE,
                TestConstants.ONE)).thenReturn(existingLabTest);
        when(labTestRepository.save(existingLabTest)).thenReturn(existingLabTest);

        //then
        LabTest actualLabTest = labTestService.updateLabTest(labTest);
        assertNotNull(actualLabTest);
        assertEquals(labTest.getName(), actualLabTest.getName());
    }

    @Test
    void testGetLabTestById() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        LabTest labTest = TestDataProvider.getLabTest();
        LabTestResult labTestResult = TestDataProvider.getLabTestResult();
        labTestResult.setDeleted(true);
        Set<LabTestResult> labTestResults = new HashSet<>(TestDataProvider.getLabTestResults());
        labTestResults.add(labTestResult);
        labTest.setLabTestResults(labTestResults);

        //when
        when(labTestRepository.findByIdAndIsDeletedAndTenantIdOrderByDisplayOrderAsc(requestDTO.getId(),
                Constants.BOOLEAN_FALSE, requestDTO.getTenantId())).thenReturn(labTest);

        //then
        LabTest actualLabTest = labTestService.getLabTestById(requestDTO);
        assertNotNull(actualLabTest);
        assertEquals(labTest.getName(), actualLabTest.getName());
        assertEquals(TestConstants.TWO, actualLabTest.getLabTestResults().size());
    }

    @Test
    void testGetLabTestResultsById() {
        //given
        ModelMapper mapper = new ModelMapper();
        Set<LabTestResult> labTestResults = TestDataProvider.getLabTestResults();
        LabTestResult labTestResult = TestDataProvider.getLabTestResult();
        labTestResult.setId(TestConstants.ONE);
        labTestResult.setName(TestConstants.LAB_TEST_RESULT_NAME);
        LabTestResult secondLabTestResult = TestDataProvider.getLabTestResult();
        secondLabTestResult.setId(TestConstants.TWO);
        secondLabTestResult.setName(TestConstants.SECOND_LAB_TEST_RESULT_NAME);
        LabTest labTest = TestDataProvider.getLabTest();
        labTest.setLabTestResults(labTestResults);
        Map<Long, List<LabTestResultRangeDTO>> labTestResultRanges = Map.of(TestConstants.ONE, TestDataProvider
                .getLabTestResultRangeDTOs(), TestConstants.TWO, TestDataProvider.getLabTestResultRangeDTOs());
        LabTestResultDTO labTestResultDTO = mapper.map(labTestResult, LabTestResultDTO.class);
        LabTestResultDTO secondLabTestResultDTO = mapper.map(secondLabTestResult, LabTestResultDTO.class);
        labTestResultDTO.setLabTestResultRanges(labTestResultRanges.get(TestConstants.ONE));
        labTestResultDTO.setLabTestResultRanges(labTestResultRanges.get(TestConstants.TWO));
        List<LabTestResultDTO> labTestResultDTOs = new ArrayList<>();
        labTestResultDTOs.add(labTestResultDTO);
        labTestResultDTOs.add(secondLabTestResultDTO);

        //when
        when(labTestResultRangesService.getLabTestResultRange(List.of(TestConstants.ONE, TestConstants.TWO))).thenReturn(labTestResultRanges);
        when(labTestRepository.findByIdAndIsDeleted(TestConstants.ONE, Constants.BOOLEAN_FALSE)).thenReturn(labTest);
        when(modelMapper.map(labTestResult, LabTestResultDTO.class)).thenReturn(labTestResultDTO);
        when(modelMapper.map(secondLabTestResult, LabTestResultDTO.class)).thenReturn(secondLabTestResultDTO);

        //then
        List<LabTestResultDTO> actualLabTestResults = labTestService.getLabTestResultsById(TestConstants.ONE);
        assertNotNull(actualLabTestResults);
        assertFalse(actualLabTestResults.isEmpty());
        assertEquals(labTestResultDTOs.size(), actualLabTestResults.size());
    }

    @Test
    void testGetLabTestsByName() {
        //given
        LabTest labTest = TestDataProvider.getLabTest();

        //when
        when(labTestRepository.findByNameIgnoreCaseAndCountryId(TestConstants.LAB_TEST_NAME, TestConstants.ONE)).thenReturn(labTest);

        //then
        LabTest actualLabTest = labTestService.getLabTestByName(TestConstants.LAB_TEST_NAME, TestConstants.ONE);
        assertNotNull(actualLabTest);
        assertEquals(labTest.getName(), actualLabTest.getName());
        assertEquals(labTest.getCountryId(), actualLabTest.getCountryId());
    }

    @Test
    void testValidateLabTestResults() {
        //given
        LabTest labTest = TestDataProvider.getLabTest();
        LabTest emptyLabTestResults = TestDataProvider.getLabTest();
        LabTest deletedLabTestResults = TestDataProvider.getLabTest();
        LabTestResult labTestResult = TestDataProvider.getLabTestResult();
        LabTestResult secondLabTestResult = TestDataProvider.getLabTestResult();
        secondLabTestResult.setId(TestConstants.ZERO);
        secondLabTestResult.setDeleted(true);
        labTestResult.setDeleted(true);
        LabTestResult thirdLabTestResult = TestDataProvider.getLabTestResult();
        thirdLabTestResult.setId(TestConstants.ZERO);
        thirdLabTestResult.setDeleted(false);
        labTestResult.setId(TestConstants.ONE);
        Set<LabTestResult> labTestResults = new HashSet<>(TestDataProvider.getLabTestResults());
        labTestResults.add(labTestResult);
        labTestResults.add(secondLabTestResult);
        labTestResults.add(thirdLabTestResult);
        deletedLabTestResults.setLabTestResults(labTestResults);
        emptyLabTestResults.setLabTestResults(new HashSet<>());
        labTest.setLabTestResults(TestDataProvider.getLabTestResults());

        //then
        labTestService.validateLabTestResults(labTest);
        labTestService.validateLabTestResults(emptyLabTestResults);
        labTestService.validateLabTestResults(deletedLabTestResults);
    }

    @Test
    void testGetLabTestsById() {
        //given
        Set<Long> labTestIds = Set.of(TestConstants.ONE, TestConstants.TWO);
        Set<Long> labTestIdsNull = Set.of(Constants.THREE, TestConstants.TWO);
        List<LabTest> labTests = TestDataProvider.getLabTests();

        //when
        when(labTestRepository.findByIdInAndIsDeleted(labTestIds, Constants.BOOLEAN_FALSE)).thenReturn(labTests);
        when(labTestRepository.findByIdInAndIsDeleted(labTestIdsNull, Constants.BOOLEAN_FALSE)).thenReturn(null);

        //then
        List<LabTest> actualNullLabTests = labTestService.getLabTestsById(labTestIdsNull);
        List<LabTest> actualLabTests = labTestService.getLabTestsById(labTestIds);
        assertNotNull(actualNullLabTests);
        assertTrue(actualNullLabTests.isEmpty());
        assertNotNull(actualLabTests);
        assertFalse(actualLabTests.isEmpty());
        assertEquals(labTests.get(0).getName(), actualLabTests.get(0).getName());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = Constants.EMPTY)
    void checkNull(String searchTerm) {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        requestDTO.setSearchTerm(searchTerm);

        LabTest labTest = TestDataProvider.getLabTest();
        labTest.setCountryId(null);
        labTest.setName(TestConstants.SECOND_LAB_TEST_NAME);

        LabTest secondLabTest = TestDataProvider.getLabTest();
        secondLabTest.setTenantId(TestConstants.ONE);
        secondLabTest.setName(null);
        secondLabTest.setId(TestConstants.ONE);
        secondLabTest.setCountryId(TestConstants.TWO);

        LabTest thirdLabTest = TestDataProvider.getLabTest();
        thirdLabTest.setName(null);
        thirdLabTest.setId(TestConstants.ONE);
        thirdLabTest.setCountryId(TestConstants.TWO);
        thirdLabTest.setTenantId(TestConstants.ONE);

        LabTest updatedLabTest = TestDataProvider.getLabTest();
        updatedLabTest.setId(TestConstants.ONE);
        updatedLabTest.setTenantId(TestConstants.ONE);
        LabTest existingLabTest = TestDataProvider.getLabTest();
        existingLabTest.setName(TestConstants.SECOND_LAB_TEST_NAME);
        existingLabTest.setLabTestResults(TestDataProvider.getLabTestResults());
        updatedLabTest.setLabTestResults(TestDataProvider.getLabTestResults());
        labTest.setLabTestResults(TestDataProvider.getLabTestResults());

        //when
        when(labTestRepository.findByIdAndIsDeletedAndTenantId(updatedLabTest.getId(), Constants.BOOLEAN_FALSE,
                TestConstants.ONE)).thenReturn(existingLabTest);

        //then
        assertThrows(BadRequestException.class, () -> labTestService.addLabTest(null));
        assertThrows(DataNotAcceptableException.class, () -> labTestService.searchLabTests(requestDTO));
        assertThrows(DataNotAcceptableException.class, () -> labTestService.updateLabTest(labTest));
        assertThrows(DataNotAcceptableException.class, () -> labTestService.updateLabTest(updatedLabTest));
        assertThrows(DataNotAcceptableException.class, () -> labTestService.updateLabTest(labTest));
        assertThrows(DataNotAcceptableException.class, () -> labTestService.updateLabTest(secondLabTest));
        assertThrows(DataNotAcceptableException.class, () -> labTestService.updateLabTest(thirdLabTest));
    }

    @Test
    void throwDataNotFoundException() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(true, TestConstants.ONE);
        requestDTO.setId(TestConstants.ONE);
        LabTest labTest = TestDataProvider.getLabTest();
        labTest.setId(TestConstants.ONE);
        labTest.setLabTestResults(TestDataProvider.getLabTestResults());

        //when
        when(labTestRepository.findByIdAndIsDeleted(TestConstants.ONE, Constants.BOOLEAN_FALSE)).thenReturn(null);
        when(labTestRepository.findByIdAndIsDeletedAndTenantIdOrderByDisplayOrderAsc(requestDTO.getId(),
                Constants.BOOLEAN_FALSE, requestDTO.getTenantId())).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> labTestService.removeLabTest(requestDTO, Boolean.TRUE));
        assertThrows(DataNotFoundException.class, () -> labTestService.getLabTestById(requestDTO));
        assertThrows(DataNotFoundException.class, () -> labTestService.updateLabTest(labTest));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 3})
    void throwDataConflictException(Long id) {
        //given
        LabTest labTest = TestDataProvider.getLabTest();
        labTest.setId(id);
        LabTest existingLabTest = TestDataProvider.getLabTest();
        existingLabTest.setId(TestConstants.TWO);

        //when
        when(labTestRepository.findByCountryIdAndNameAndIsDeletedAndTenantId(TestConstants.ONE, labTest.getName(),
                Boolean.FALSE, labTest.getTenantId())).thenReturn(existingLabTest);
        when(labTestRepository.findByCountryIdAndNameAndIsDeletedAndTenantId(Constants.THREE, labTest.getName(),
                Boolean.FALSE, labTest.getTenantId())).thenReturn(null);

        //then
        assertThrows(DataConflictException.class, () -> labTestService.validateLabTest(labTest));
    }
}