package com.mdtlabs.coreplatform.spiceadminservice.labtestresultranges.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.InheritingConfiguration;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResult;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResultRange;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.labtest.repository.LabTestResultRepository;
import com.mdtlabs.coreplatform.spiceadminservice.labtestresultranges.repository.LabTestResultRangesRepository;
import com.mdtlabs.coreplatform.spiceadminservice.labtestresultranges.service.impl.LabTestResultRangesServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for LabTestResultRanges service implementation.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LabTestResultRangesServiceTest {

    @InjectMocks
    LabTestResultRangesServiceImpl labTestResultRangesService;

    @Mock
    LabTestResultRangesRepository labTestResultRangesRepository;

    @Mock
    LabTestResultRepository labTestResultRepository;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(LabTestResultRangesServiceImpl.class, "modelMapper", labTestResultRangesService);
    }

    @Test
    void testAddLabTestResultRanges() {
        //given
        LabTestResult labTestResult = TestDataProvider.getLabTestResult();
        LabTestResultRangeRequestDTO labTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();
        LabTestResultRange labTestResultRangeEntity = new LabTestResultRange();
        labTestResultRangeEntity.setTenantId(labTestResultRangeRequestDTO.getTenantId());
        labTestResultRangeEntity.setLabTestId(labTestResult.getLabTestId());
        labTestResultRangeEntity.setLabTestResultId(labTestResult.getId());
        List<LabTestResultRange> listOfLabTestResultRangesToSave = new ArrayList<>();
        listOfLabTestResultRangesToSave.add(labTestResultRangeEntity);

        //when
        when(labTestResultRepository.findByIdAndIsDeletedAndTenantId(labTestResultRangeRequestDTO.getLabTestResultId(),
                false, labTestResultRangeRequestDTO.getTenantId())).thenReturn(labTestResult);
        when(labTestResultRangesRepository.saveAll(listOfLabTestResultRangesToSave)).thenReturn(listOfLabTestResultRangesToSave);

        //then
        List<LabTestResultRange> addLabTestResultRanges = labTestResultRangesService.addLabTestResultRanges(labTestResultRangeRequestDTO);
        assertFalse(addLabTestResultRanges.isEmpty());
        assertEquals(labTestResultRangeRequestDTO.getTenantId(), addLabTestResultRanges.get(0).getTenantId());
        assertEquals(listOfLabTestResultRangesToSave.size(), addLabTestResultRanges.size());
    }

    @Test
    void toVerifyAddLabTestResultRanges() {
        //given
        LabTestResult labTestResult = TestDataProvider.getLabTestResult();
        LabTestResultRangeRequestDTO labTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();

        LabTestResultRange labTestResultRangeEntity = new LabTestResultRange();
        labTestResultRangeEntity.setTenantId(labTestResultRangeRequestDTO.getTenantId());
        labTestResultRangeEntity.setLabTestId(labTestResult.getLabTestId());
        labTestResultRangeEntity.setLabTestResultId(labTestResult.getId());
        labTestResultRangeEntity.setMinimumValue(1.0);
        labTestResultRangeEntity.setMaximumValue(4.0);
        labTestResultRangeEntity.setDisplayName(TestConstants.LAB_TEST_NAME);
        labTestResultRangeEntity.setDisplayOrder(Constants.ONE);
        labTestResultRangeEntity.setUnit(Constants.UNIT);
        labTestResultRangeEntity.setUnitId(TestConstants.ONE);
        List<LabTestResultRange> listOfLabTestResultRangesToSave = new ArrayList<>();
        listOfLabTestResultRangesToSave.add(labTestResultRangeEntity);
        LabTestResultRangeDTO labTestResultRangeDTO = TestDataProvider.getLabTestResultRangeDTO();
        labTestResultRangeDTO.setMinimumValue(1.0);
        labTestResultRangeDTO.setMaximumValue(4.0);
        labTestResultRangeDTO.setDisplayName(TestConstants.LAB_TEST_NAME);
        labTestResultRangeDTO.setDisplayOrder(Constants.ONE);
        labTestResultRangeDTO.setUnit(Constants.UNIT);
        labTestResultRangeDTO.setUnitId(TestConstants.ONE);
        List<LabTestResultRangeDTO> labTestResultRanges = List.of(labTestResultRangeDTO);
        labTestResultRangeRequestDTO.setLabTestResultRanges(labTestResultRanges);

        //when
        when(labTestResultRepository.findByIdAndIsDeletedAndTenantId(labTestResultRangeRequestDTO.getLabTestResultId(),
                false, TestConstants.ONE)).thenReturn(labTestResult);
        when(labTestResultRangesRepository.saveAll(listOfLabTestResultRangesToSave))
                .thenReturn(listOfLabTestResultRangesToSave);

        //then
        List<LabTestResultRange> addLabTestResultRanges = labTestResultRangesService
                .addLabTestResultRanges(labTestResultRangeRequestDTO);
        assertFalse(addLabTestResultRanges.isEmpty());
        assertEquals(labTestResultRangeRequestDTO.getTenantId(), addLabTestResultRanges.get(0).getTenantId());
        assertEquals(listOfLabTestResultRangesToSave.size(), addLabTestResultRanges.size());
    }

    @Test
    void testValidateLabTestResultRanges() {
        //given
        LabTestResultRangeRequestDTO labTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();
        List<LabTestResultRange> labTestResultRanges = TestDataProvider.getLabTestResultRanges();

        //when
        when(labTestResultRangesRepository.findByLabTestResultIdAndIsDeletedOrderByDisplayOrderAsc(labTestResultRangeRequestDTO.getLabTestResultId(), false)).thenReturn(labTestResultRanges);

        //then
        List<LabTestResultRange> actualLabTestResults = labTestResultRangesService
                .validateLabTestResultRanges(labTestResultRangeRequestDTO);
        assertNotNull(actualLabTestResults);
        assertEquals(labTestResultRanges, actualLabTestResults);
    }

    @Test
    void toVerifyValidateLabTestResultRanges() {
        //given
        LabTestResultRangeRequestDTO labTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();
        LabTestResult labTestResult = TestDataProvider.getLabTestResult();
        List<LabTestResultRange> labTestResultRanges = TestDataProvider.getLabTestResultRanges();

        //when
        when(labTestResultRangesRepository.findByLabTestResultIdAndIsDeletedOrderByDisplayOrderAsc(labTestResultRangeRequestDTO.getLabTestResultId(),
                false)).thenReturn(labTestResultRanges);

        //then
        List<LabTestResultRange> actualLabTestResultRanges = labTestResultRangesService
                .validateLabTestResultRanges(labTestResultRangeRequestDTO);
        assertNotNull(actualLabTestResultRanges);
        assertEquals(labTestResultRanges, actualLabTestResultRanges);
    }

    @Test
    void testUpdateLabTestResultRanges() {
        //given
        LabTestResultRangeRequestDTO labTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();
        LabTestResultRangeDTO secondLabTestResultRangeDTO = TestDataProvider.getLabTestResultRangeDTO();
        secondLabTestResultRangeDTO.setId(TestConstants.TWO);
        labTestResultRangeRequestDTO.getLabTestResultRanges().add(secondLabTestResultRangeDTO);
        LabTestResult labTestResult = TestDataProvider.getLabTestResult();
        List<LabTestResultRange> existingLabTestResultRanges = TestDataProvider.getLabTestResultRanges();
        LabTestResultRangeDTO labTestResultRangeDto = TestDataProvider.getLabTestResultRangeDTO();
        LabTestResultRange labTestResultRange = TestDataProvider.getLabTestResultRange();
        LabTestResultRange existingLabTestResultRangeEntity = TestDataProvider.getLabTestResultRange();
        List<LabTestResultRange> labTestResultRangesToUpdate = TestDataProvider.getLabTestResultRanges();
        List<LabTestResultRange> labTestResultRanges = TestDataProvider.getLabTestResultRanges();

        //when
        when(labTestResultRangesRepository.findByLabTestResultIdAndIsDeletedOrderByDisplayOrderAsc(labTestResultRangeRequestDTO.getLabTestResultId(),
                false)).thenReturn(labTestResultRanges);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(labTestResultRangesRepository.findByIdsAndIsDeletedAndTenantId(
                List.of(TestConstants.ONE, TestConstants.TWO), false, labTestResultRangeRequestDTO.getTenantId()))
                .thenReturn(existingLabTestResultRanges);
        when(labTestResultRepository.findByIdAndIsDeletedAndTenantId(labTestResultRangeRequestDTO.getLabTestResultId(),
                false, labTestResultRangeRequestDTO.getTenantId())).thenReturn(labTestResult);
        when(modelMapper.map(labTestResultRangeDto, LabTestResultRange.class)).thenReturn(labTestResultRange);
        doNothing().when(modelMapper).map(labTestResultRangeDto, existingLabTestResultRangeEntity);
        when(labTestResultRangesRepository.saveAll(labTestResultRangesToUpdate)).thenReturn(labTestResultRangesToUpdate);

        //then
        List<LabTestResultRange> actualLabTestResultRanges = labTestResultRangesService
                .updateLabTestResultRanges(labTestResultRangeRequestDTO);
        assertNotNull(actualLabTestResultRanges);
        assertFalse(actualLabTestResultRanges.isEmpty());
        assertEquals(labTestResultRanges.size(), actualLabTestResultRanges.size());
    }

    @Test
    void toVerifyUpdateLabTestResultRanges() {
        //given
        LabTestResultRangeRequestDTO labTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();
        labTestResultRangeRequestDTO.setId(TestConstants.ZERO);
        LabTestResultRangeDTO secondLabTestResultRangeDTO = TestDataProvider.getLabTestResultRangeDTO();
        secondLabTestResultRangeDTO.setId(TestConstants.TWO);
        labTestResultRangeRequestDTO.getLabTestResultRanges().add(secondLabTestResultRangeDTO);
        LabTestResult labTestResult = TestDataProvider.getLabTestResult();
        List<LabTestResultRange> existingLabTestResultRanges = TestDataProvider.getLabTestResultRanges();
        LabTestResultRange labTestResultRange = TestDataProvider.getLabTestResultRange();
        List<LabTestResultRange> labTestResultRanges = TestDataProvider.getLabTestResultRanges();

        //when
        when(labTestResultRangesRepository.findByLabTestResultIdAndIsDeletedOrderByDisplayOrderAsc(labTestResultRangeRequestDTO.getLabTestResultId(),
                false)).thenReturn(labTestResultRanges);
        when(modelMapper.map(labTestResultRangeRequestDTO, LabTestResultRange.class)).thenReturn(labTestResultRange);
        doNothing().when(modelMapper).map(secondLabTestResultRangeDTO, existingLabTestResultRanges.get(0));
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(labTestResultRangesRepository.findByIdsAndIsDeletedAndTenantId(
                List.of(TestConstants.ONE, TestConstants.TWO), Boolean.FALSE,
                labTestResultRangeRequestDTO.getTenantId())).thenReturn(existingLabTestResultRanges);
        when(labTestResultRepository.findByIdAndIsDeletedAndTenantId(labTestResultRangeRequestDTO.getLabTestResultId(),
                false, labTestResultRangeRequestDTO.getTenantId())).thenReturn(labTestResult);

        //then
        List<LabTestResultRange> actualLabTestResultRanges = labTestResultRangesService
                .updateLabTestResultRanges(labTestResultRangeRequestDTO);
        assertNotNull(actualLabTestResultRanges);
        assertTrue(actualLabTestResultRanges.isEmpty());
    }


    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    void testRemoveLabTestResultRange(long id) {
        //given
        Optional<LabTestResultRange> labTestResultRange = Optional.of(TestDataProvider.getLabTestResultRange());
        LabTestResultRange resultRange = labTestResultRange.get();
        resultRange.setTenantId(TestConstants.ONE);
        resultRange.setDeleted(Boolean.TRUE);

        //when
        when(labTestResultRangesRepository.findByIdAndTenantId(TestConstants.ONE, TestConstants.ONE)).thenReturn(labTestResultRange);
        when(labTestResultRangesRepository.findByIdAndTenantId(TestConstants.TWO, TestConstants.ONE)).thenReturn(Optional.empty());
        when(labTestResultRangesRepository.save(resultRange)).thenReturn(resultRange);

        //then
        boolean isRemoved = labTestResultRangesService.removeLabTestResultRange(id, resultRange.getTenantId());
        assertTrue(isRemoved);
    }

    @Test
    void testGetLabTestResultRange() {
        //given
        List<LabTestResultRange> labTestResultRanges = TestDataProvider.getLabTestResultRanges();

        //when
        when(labTestResultRangesRepository.findByLabTestResultIdAndIsDeletedOrderByDisplayOrderAsc(TestConstants.ONE,
                false)).thenReturn(labTestResultRanges);

        //then
        List<LabTestResultRange> actualLabTestResultRanges = labTestResultRangesService.getLabTestResultRange(TestConstants.ONE);
        assertEquals(labTestResultRanges.size(), actualLabTestResultRanges.size());
        assertNotNull(actualLabTestResultRanges);
        assertFalse(actualLabTestResultRanges.isEmpty());
        assertEquals(labTestResultRanges.get(0), actualLabTestResultRanges.get(0));
    }


    @Test
    void checkBadRequestException() {
        //given
        LabTestResultRangeRequestDTO labTestResultRangeRequestDTO = new LabTestResultRangeRequestDTO();
        LabTestResultRangeRequestDTO secondLabTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();
        secondLabTestResultRangeRequestDTO.setLabTestResultRanges(new ArrayList<>());
        LabTestResultRangeRequestDTO thirdLabTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();
        thirdLabTestResultRangeRequestDTO.setLabTestResultId(null);

        //then
        assertThrows(BadRequestException.class, () -> labTestResultRangesService.validateLabTestResultRanges(null));
        assertThrows(BadRequestException.class, () -> labTestResultRangesService
                .validateLabTestResultRanges(labTestResultRangeRequestDTO));
        assertThrows(BadRequestException.class, () -> labTestResultRangesService
                .addLabTestResultRanges(null));
        assertThrows(BadRequestException.class, () -> labTestResultRangesService
                .addLabTestResultRanges(secondLabTestResultRangeRequestDTO));
        assertThrows(BadRequestException.class, () -> labTestResultRangesService
                .addLabTestResultRanges(thirdLabTestResultRangeRequestDTO));
    }

    @Test
    void throwDataNotAcceptableException() {
        //given
        LabTestResultRangeRequestDTO labTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();
        LabTestResultRange labTestResultRange = TestDataProvider.getLabTestResultRange();
        labTestResultRange.setId(TestConstants.FIVE);
        LabTestResultRange secondLabTestResultRange = TestDataProvider.getLabTestResultRange();
        secondLabTestResultRange.setId(TestConstants.FOUR);
        List<LabTestResultRange> labTestResultRanges = List.of(labTestResultRange, secondLabTestResultRange);

        //when
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(labTestResultRangesRepository.findByLabTestResultIdAndIsDeletedOrderByDisplayOrderAsc(labTestResultRangeRequestDTO.getLabTestResultId(), false)).thenReturn(labTestResultRanges);

        //then
        assertThrows(DataNotAcceptableException.class, () -> labTestResultRangesService.validateLabTestResultRanges(labTestResultRangeRequestDTO));
        assertThrows(DataNotAcceptableException.class, () -> labTestResultRangesService.addLabTestResultRanges(labTestResultRangeRequestDTO));
    }

    @Test
    void updateLabTestResultRangesException() {
        //given
        LabTestResultRangeRequestDTO labTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();
        LabTestResultRangeDTO secondLabTestResultRangeDTO = TestDataProvider.getLabTestResultRangeDTO();
        secondLabTestResultRangeDTO.setId(TestConstants.TWO);
        labTestResultRangeRequestDTO.getLabTestResultRanges().add(secondLabTestResultRangeDTO);
        List<LabTestResultRange> labTestResultRanges = TestDataProvider.getLabTestResultRanges();

        //when
        when(labTestResultRangesRepository.findByLabTestResultIdAndIsDeletedOrderByDisplayOrderAsc(labTestResultRangeRequestDTO.getLabTestResultId(),
                false)).thenReturn(labTestResultRanges);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(labTestResultRangesRepository
                .findByIdsAndIsDeletedAndTenantId(List.of(TestConstants.ONE, TestConstants.TWO), false, TestConstants.ONE)).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> labTestResultRangesService.updateLabTestResultRanges(labTestResultRangeRequestDTO));
    }

    @Test
    void setLabTestResultRangeException() {
        //given
        LabTestResultRangeRequestDTO labTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();
        LabTestResultRangeDTO secondLabTestResultRangeDTO = TestDataProvider.getLabTestResultRangeDTO();
        secondLabTestResultRangeDTO.setId(TestConstants.TWO);
        labTestResultRangeRequestDTO.getLabTestResultRanges().add(secondLabTestResultRangeDTO);
        List<LabTestResultRange> existingLabTestResultRanges = TestDataProvider.getLabTestResultRanges();
        existingLabTestResultRanges.get(0).setId(TestConstants.FIVE);
        List<LabTestResultRange> labTestResultRanges = TestDataProvider.getLabTestResultRanges();

        //when
        when(labTestResultRangesRepository.findByLabTestResultIdAndIsDeletedOrderByDisplayOrderAsc(labTestResultRangeRequestDTO.getLabTestResultId(),
                false)).thenReturn(labTestResultRanges);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(labTestResultRangesRepository.findByIdsAndIsDeletedAndTenantId(List.of(TestConstants.ONE, TestConstants.TWO), false, TestConstants.ONE))
                .thenReturn(existingLabTestResultRanges);

        //then
        assertThrows(DataNotFoundException.class, () -> labTestResultRangesService.updateLabTestResultRanges(labTestResultRangeRequestDTO));
    }

    @Test
    void testGetLabTestResultRangeMap() {
        //given
        List<LabTestResultRange> labTestRanges = TestDataProvider.getLabTestResultRanges();
        List<Long> labTestResultIds = List.of(TestConstants.ONE, TestConstants.ONE);
        LabTestResultRange range = labTestRanges.get(1);
        LabTestResultRangeDTO secondLabTestResultRangeDto = TestDataProvider.getLabTestResultRangeDTO();
        secondLabTestResultRangeDto.setId(TestConstants.TWO);
        secondLabTestResultRangeDto.setLabTestId(TestConstants.ONE);
        Map<Long, List<LabTestResultRangeDTO>> labtestRangesMap = new HashMap<>();
        LabTestResultRangeDTO labTestResultRangeDTO = TestDataProvider.getLabTestResultRangeDTO();
        labtestRangesMap.put(TestConstants.ONE, new ArrayList<>());
        labtestRangesMap.get(TestConstants.ONE).add(labTestResultRangeDTO);
        labtestRangesMap.get(TestConstants.ONE).add(labTestResultRangeDTO);

        //when
        when(labTestResultRangesRepository.findByLabTestResultIdInAndIsDeletedOrderByDisplayOrderAsc(labTestResultIds, Boolean.FALSE)).thenReturn(labTestRanges);
        when(modelMapper.map(range, LabTestResultRangeDTO.class)).thenReturn(labTestResultRangeDTO);

        //then
        Map<Long, List<LabTestResultRangeDTO>> actualLabtestRangesMap = labTestResultRangesService.getLabTestResultRange(labTestResultIds);
        assertNotNull(actualLabtestRangesMap);
        assertEquals(labtestRangesMap, actualLabtestRangesMap);
        assertEquals(labtestRangesMap.size(), actualLabtestRangesMap.size());
    }
}
