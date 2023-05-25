package com.mdtlabs.coreplatform.spiceadminservice.RegionCustomization.controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import com.mdtlabs.coreplatform.common.model.dto.spice.CustomizationRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RegionCustomizationDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.RegionCustomization;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.regioncustomization.controller.RegionCustomizationController;
import com.mdtlabs.coreplatform.spiceadminservice.regioncustomization.service.impl.RegionCustomizationServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Region customization controller.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class RegionCustomizationControllerTest {

    @InjectMocks
    private RegionCustomizationController regionCustomizationController;

    @Mock
    private RegionCustomizationServiceImpl regionCustomizationService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(RegionCustomizationController.class, "modelMapper", regionCustomizationController);
    }

    @Test
    void testAddCustomization() {
        //given
        RegionCustomization regionCustomization = TestDataProvider.getRegionCustomization();
        RegionCustomizationDTO regionCustomizationDTO = TestDataProvider.getRegionCustomizationDTO();

        //when
        when(modelMapper.map(regionCustomizationDTO, RegionCustomization.class)).thenReturn(regionCustomization);
        when(regionCustomizationService.createCustomization(regionCustomization)).thenReturn(regionCustomization);

        //then
        SuccessResponse<RegionCustomization> actualRegionCustomization = regionCustomizationController
                .addCustomization(regionCustomizationDTO);
        assertNotNull(actualRegionCustomization);
        assertEquals(HttpStatus.OK, actualRegionCustomization.getStatusCode());
    }

    @Test
    void testGetCustomization() {
        //given
        CustomizationRequestDTO customizationRequestDTO = TestDataProvider.getCustomizationRequestDTO();
        RegionCustomization regionCustomization = TestDataProvider.getRegionCustomization();

        //when
        when(regionCustomizationService.getCustomization(customizationRequestDTO)).thenReturn(regionCustomization);

        //then
        SuccessResponse<RegionCustomization> actualRegionCustomization = regionCustomizationController
                .getCustomization(customizationRequestDTO);
        assertNotNull(actualRegionCustomization);
        assertEquals(HttpStatus.OK, actualRegionCustomization.getStatusCode());
    }

    @Test
    void testUpdateCustomization() {
        //given
        RegionCustomization regionCustomization = TestDataProvider.getRegionCustomization();
        RegionCustomizationDTO regionCustomizationDTO = TestDataProvider.getRegionCustomizationDTO();

        //when
        when(modelMapper.map(regionCustomizationDTO, RegionCustomization.class)).thenReturn(regionCustomization);
        when(regionCustomizationService.updateCustomization(regionCustomization)).thenReturn(regionCustomization);

        //then
        SuccessResponse<RegionCustomization> actualRegionCustomization = regionCustomizationController
                .updateCustomization(regionCustomizationDTO);
        assertNotNull(actualRegionCustomization);
        assertEquals(HttpStatus.OK, actualRegionCustomization.getStatusCode());
    }

    @Test
    void testGetRegionCustomizations() {
        //given
        RegionCustomization regionCustomization = TestDataProvider.getRegionCustomization();
        RegionCustomization secondRegionCustomization = TestDataProvider.getRegionCustomization();
        List<RegionCustomization> regionCustomizations = List.of(regionCustomization, secondRegionCustomization);

        //when
        when(regionCustomizationService.getRegionCustomizations(TestConstants.ONE)).thenReturn(regionCustomizations);

        //then
        List<RegionCustomization> actualRegionCustomizations = regionCustomizationController
                .getRegionCustomizations(TestConstants.ONE);
        assertNotNull(actualRegionCustomizations);
        assertEquals(regionCustomizations.size(), actualRegionCustomizations.size());
        assertEquals(regionCustomizations.contains(regionCustomization), actualRegionCustomizations.contains(regionCustomization));
    }
}