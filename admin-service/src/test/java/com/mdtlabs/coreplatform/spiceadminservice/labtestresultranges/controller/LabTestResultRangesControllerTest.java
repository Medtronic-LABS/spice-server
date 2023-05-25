package com.mdtlabs.coreplatform.spiceadminservice.labtestresultranges.controller;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;

import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResultRange;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.labtestresultranges.service.impl.LabTestResultRangesServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for LabTestResultRanges controller.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LabTestResultRangesControllerTest {

    @InjectMocks
    LabTestResultRangesController labTestResultRangesController;

    @Mock
    LabTestResultRangesServiceImpl labTestResultRangesService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(LabTestResultRangesController.class, "modelMapper",
                labTestResultRangesController);
    }

    @Test
    void testAddLabTestResultRanges() {
        //given
        LabTestResultRangeRequestDTO labTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();
        List<LabTestResultRange> labTestResultRanges = TestDataProvider.getLabTestResultRanges();

        //when
        when(labTestResultRangesService.addLabTestResultRanges(labTestResultRangeRequestDTO))
                .thenReturn(labTestResultRanges);

        //then
        SuccessResponse<List<LabTestResultRange>> actualLabTestResultRange = labTestResultRangesController
                .addLabTestResultRanges(labTestResultRangeRequestDTO);
        assertNotNull(actualLabTestResultRange);
        assertEquals(HttpStatus.CREATED, actualLabTestResultRange.getStatusCode());
    }

    @Test
    void testUpdateLabTestResultRanges() {
        //given
        LabTestResultRangeRequestDTO labTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();
        List<LabTestResultRange> labTestResultRanges = TestDataProvider.getLabTestResultRanges();

        //when
        when(labTestResultRangesService.updateLabTestResultRanges(labTestResultRangeRequestDTO))
                .thenReturn(labTestResultRanges);

        //then
        SuccessResponse<List<LabTestResultRange>> actualLabTestResultRange = labTestResultRangesController
                .updateLabTestResultRanges(labTestResultRangeRequestDTO);
        assertNotNull(actualLabTestResultRange);
        assertEquals(HttpStatus.OK, actualLabTestResultRange.getStatusCode());
    }

    @Test
    void testRemoveLabTestResultRanges() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);

        //when
        when(labTestResultRangesService.removeLabTestResultRange(TestConstants.ONE, commonRequestDTO.getTenantId()))
                .thenReturn(true);

        //then
        SuccessResponse<Boolean> actualResponse = labTestResultRangesController
                .removeLabTestResultRanges(commonRequestDTO);
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2})
    void testGetLabTestResultRanges(long id) {
        //given
        List<LabTestResultRange> labTestResultRanges = TestDataProvider.getLabTestResultRanges();
        List<LabTestResultRangeRequestDTO> labTestResultRangeRequestDTOs = TestDataProvider
                .getLabTestResultRangeRequestDTOs();

        //when
        when(labTestResultRangesService.getLabTestResultRange(TestConstants.ONE)).thenReturn(labTestResultRanges);
        when(labTestResultRangesService.getLabTestResultRange(TestConstants.TWO)).thenReturn(new ArrayList<>());
        when(modelMapper.map(labTestResultRanges, new TypeToken<List<LabTestResultRangeDTO>>() {
        }.getType())).thenReturn(labTestResultRangeRequestDTOs);

        //then
        SuccessResponse<List<LabTestResultRangeDTO>> actualResponse = labTestResultRangesController
                .getLabTestResultRanges(id);
        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
    }
}