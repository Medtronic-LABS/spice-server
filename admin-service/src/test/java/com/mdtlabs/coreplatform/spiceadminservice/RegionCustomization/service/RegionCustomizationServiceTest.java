package com.mdtlabs.coreplatform.spiceadminservice.RegionCustomization.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.InheritingConfiguration;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CustomizationRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.spice.RegionCustomization;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.regioncustomization.repository.RegionCustomizationRepository;
import com.mdtlabs.coreplatform.spiceadminservice.regioncustomization.service.impl.RegionCustomizationServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Region customization service implementation.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class RegionCustomizationServiceTest {

    @InjectMocks
    private RegionCustomizationServiceImpl regionCustomizationService;

    @Mock
    private RegionCustomizationRepository regionCustomizationRepository;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(RegionCustomizationServiceImpl.class, "modelMapper", regionCustomizationService);
    }

    @Test
    void testCreateCustomization() {
        //given
        RegionCustomization regionCustomization = TestDataProvider.getRegionCustomization();

        //when
        when(regionCustomizationRepository.save(regionCustomization)).thenReturn(regionCustomization);

        //then
        RegionCustomization actualRegionCustomization = regionCustomizationService
                .createCustomization(regionCustomization);
        assertNotNull(actualRegionCustomization);
        assertEquals(TestConstants.FORM_INPUT, actualRegionCustomization.getFormInput());
        assertEquals(TestConstants.ONE, actualRegionCustomization.getCountryId());
        assertEquals(Constants.SCREENING, actualRegionCustomization.getType());
    }

    @Test
    void testGetCustomization() {
        //given
        CustomizationRequestDTO customizationRequestDto = TestDataProvider.getCustomizationRequestDTO();
        customizationRequestDto.setCategory(TestConstants.CATEGORY);
        customizationRequestDto.setType(Constants.TYPE);
        RegionCustomization regionCustomization = TestDataProvider.getRegionCustomization();
        List<RegionCustomization> regionCustomizations = List.of(regionCustomization);

        //when
        when(regionCustomizationRepository.findByCountryIdAndCategoryAndTypeAndTenantId(
                customizationRequestDto.getCountryId(), customizationRequestDto.getCategory(),
                customizationRequestDto.getType(), Constants.BOOLEAN_FALSE, customizationRequestDto.getTenantId(),
                customizationRequestDto.getCultureId()))
                .thenReturn(regionCustomizations);
        //then
        RegionCustomization actualRegionCustomization = regionCustomizationService
                .getCustomization(customizationRequestDto);
        assertNotNull(regionCustomization);
        assertEquals(regionCustomization.getCountryId(), actualRegionCustomization.getCountryId());
        assertEquals(TestConstants.FORM_INPUT, actualRegionCustomization.getFormInput());
        assertEquals(TestConstants.ONE, actualRegionCustomization.getCountryId());
    }

    @Test
    void testUpdateCustomization() {
        //given
        RegionCustomization regionCustomization = TestDataProvider.getRegionCustomization();
        regionCustomization.setId(TestConstants.ONE);
        RegionCustomization existingRegionCustomization = TestDataProvider.getRegionCustomization();
        existingRegionCustomization.setId(TestConstants.ONE);

        //when
        when(regionCustomizationRepository.findByIdAndTenantIdAndIsDeletedFalse(regionCustomization.getId(),
                regionCustomization.getTenantId())).thenReturn(existingRegionCustomization);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        doNothing().when(modelMapper).map(regionCustomization, existingRegionCustomization);
        when(regionCustomizationRepository.save(existingRegionCustomization)).thenReturn(existingRegionCustomization);

        //then
        RegionCustomization actualRegionCustomization = regionCustomizationService
                .updateCustomization(regionCustomization);
        assertNotNull(actualRegionCustomization);
        assertEquals(regionCustomization.getCountryId(), actualRegionCustomization.getCountryId());
        assertEquals(TestConstants.ONE, actualRegionCustomization.getCountryId());
        assertEquals(TestConstants.ONE, actualRegionCustomization.getId());
    }

    @Test
    void testGetRegionCustomizations() {
        //given
        UserDTO userDto = TestDataProvider.getUserDTO();
        Country country = TestDataProvider.getCountry();
        country.setId(TestConstants.ONE);
        userDto.setCountry(country);
        userDto.setCultureId(TestConstants.ONE);
        RegionCustomization regionCustomization = TestDataProvider.getRegionCustomization();
        RegionCustomization secondRegionCustomization = TestDataProvider.getRegionCustomization();
        List<RegionCustomization> regionCustomizations = List.of(regionCustomization, secondRegionCustomization);
        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);

        //when
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDto);
        when(regionCustomizationRepository.findByCountryIdAndCultureId(TestConstants.ONE, TestConstants.ONE)).thenReturn(regionCustomizations);

        //then
        List<RegionCustomization> actualRegionCustomizations = regionCustomizationService
                .getRegionCustomizations(TestConstants.ONE);
        userContextHolder.close();
        assertNotNull(actualRegionCustomizations);
        assertEquals(regionCustomizations.size(), actualRegionCustomizations.size());
        assertEquals(regionCustomizations.contains(regionCustomization),
                actualRegionCustomizations.contains(regionCustomization));
        assertNotNull(actualRegionCustomizations);
    }

    @Test
    void throwDataNotFoundException() {
        //given
        CustomizationRequestDTO customizationRequestDto = TestDataProvider.getCustomizationRequestDTO();
        CustomizationRequestDTO secondCustomizationRequestDto = TestDataProvider.getCustomizationRequestDTO();
        secondCustomizationRequestDto.setCountryId(TestConstants.TWO);
        RegionCustomization regionCustomization = TestDataProvider.getRegionCustomization();

        //when
        when(regionCustomizationRepository.findByCountryIdAndCategoryAndTypeAndTenantId(
                customizationRequestDto.getCountryId(), customizationRequestDto.getCategory(),
                customizationRequestDto.getType(), Constants.BOOLEAN_FALSE, customizationRequestDto.getTenantId(),
                customizationRequestDto.getCultureId()))
                .thenReturn(null);
        when(regionCustomizationRepository.findByCountryIdAndCategoryAndTypeAndTenantId(
                secondCustomizationRequestDto.getCountryId(), secondCustomizationRequestDto.getCategory(),
                secondCustomizationRequestDto.getType(), Constants.BOOLEAN_FALSE,
                secondCustomizationRequestDto.getTenantId(), secondCustomizationRequestDto.getCultureId())).thenReturn(new ArrayList<>());
        when(regionCustomizationRepository.findByIdAndTenantIdAndIsDeletedFalse(regionCustomization.getId(),
                regionCustomization.getTenantId())).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> regionCustomizationService
                .getCustomization(customizationRequestDto));
        assertThrows(DataNotFoundException.class, () -> regionCustomizationService
                .getCustomization(secondCustomizationRequestDto));
        assertThrows(DataNotFoundException.class, () -> regionCustomizationService
                .updateCustomization(regionCustomization));
    }
}