package com.mdtlabs.coreplatform.spiceadminservice.accountcustomization.service;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CustomizationRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountCustomization;
import com.mdtlabs.coreplatform.common.repository.OrganizationRepository;
import com.mdtlabs.coreplatform.spiceadminservice.accountcustomization.repository.AccountCustomizationRepository;
import com.mdtlabs.coreplatform.spiceadminservice.accountcustomization.service.impl.AccountCustomizationServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Account customization service implementation.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountCustomizationServiceTest {

    @InjectMocks
    private AccountCustomizationServiceImpl accountCustomizationService;

    @Mock
    private AccountCustomizationRepository accountCustomizationRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(AccountCustomizationServiceImpl.class, "modelMapper", accountCustomizationService);
    }

    @Test
    void testCreateAccountCustomization() {
        //given
        AccountCustomization accountCustomization = TestDataProvider.getAccountCustomization();

        //when
        when(accountCustomizationRepository.save(accountCustomization)).thenReturn(accountCustomization);

        //then
        AccountCustomization actualAccountCustomization = accountCustomizationService
                .createAccountCustomization(accountCustomization);
        assertNotNull(actualAccountCustomization);
        assertEquals(accountCustomization.getType(), actualAccountCustomization.getType());
    }

    @Test
    void testGetCustomization() {
        //given
        CustomizationRequestDTO customizationRequestDTO = TestDataProvider.getCustomizationRequestDTO();
        CustomizationRequestDTO secondCustomizationRequestDTO = TestDataProvider.getCustomizationRequestDTO();
        secondCustomizationRequestDTO.setAccountId(TestConstants.TWO);
        customizationRequestDTO.setClinicalWorkflowId(TestConstants.TWO);
        List<AccountCustomization> accountCustomizations = TestDataProvider.getAccountCustomizations();

        //when
        when(accountCustomizationRepository.getAccountCustomization(customizationRequestDTO.getCountryId(),
                customizationRequestDTO.getAccountId(), customizationRequestDTO.getCategory(),
                customizationRequestDTO.getType(), customizationRequestDTO.getClinicalWorkflowId(),
                Constants.BOOLEAN_FALSE, customizationRequestDTO.getTenantId())).thenReturn(accountCustomizations);
        when(accountCustomizationRepository.getAccountCustomization(secondCustomizationRequestDTO.getCountryId(),
                secondCustomizationRequestDTO.getAccountId(), secondCustomizationRequestDTO.getCategory(),
                secondCustomizationRequestDTO.getType(), secondCustomizationRequestDTO.getClinicalWorkflowId(),
                Constants.BOOLEAN_FALSE, secondCustomizationRequestDTO.getTenantId())).thenReturn(null);

        //then
        AccountCustomization accountCustomization = accountCustomizationService
                .getCustomization(customizationRequestDTO);
        AccountCustomization nullAccountCustomization = accountCustomizationService
                .getCustomization(secondCustomizationRequestDTO);
        assertNull(nullAccountCustomization);
        assertNotNull(accountCustomization);
        assertEquals(accountCustomization.getId(), accountCustomizations.get(Constants.ZERO).getId());
        assertEquals(Constants.TWO, accountCustomization.getClinicalWorkflowId());
    }

    @Test
    void testUpdateCustomization() {
        //given
        AccountCustomization accountCustomization = TestDataProvider.getAccountCustomization();
        AccountCustomization existingAccountCustomization = TestDataProvider.getAccountCustomization();

        //when
        when(accountCustomizationRepository.findByIdAndIsDeletedAndTenantId(accountCustomization.getId(),
                Constants.BOOLEAN_FALSE, TestConstants.ONE)).thenReturn(existingAccountCustomization);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        doNothing().when(modelMapper).map(accountCustomization, existingAccountCustomization);
        when(accountCustomizationRepository.save(existingAccountCustomization)).thenReturn(existingAccountCustomization);

        //then
        AccountCustomization actualAccountCustomization = accountCustomizationService
                .updateCustomization(accountCustomization);
        assertNotNull(actualAccountCustomization);
        assertEquals(existingAccountCustomization.getId(), actualAccountCustomization.getId());
        assertEquals(Constants.TYPE, actualAccountCustomization.getType());
    }

    @Test
    void testRemoveCustomization() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        Map<String, Object> requestData = Map.of(Constants.ID, TestConstants.ONE, Constants.TENANT_PARAMETER_NAME,
                TestConstants.ONE);
        AccountCustomization accountCustomization = TestDataProvider.getAccountCustomization();
        Organization organization = TestDataProvider.getOrganization();
        organization.setId(TestConstants.ONE);
        List<Organization> organizationList = List.of(organization);

        //when
        when(accountCustomizationRepository.findByIdAndIsDeletedAndTenantId(commonRequestDTO.getId(), false,
                commonRequestDTO.getTenantId())).thenReturn(accountCustomization);
        when(organizationRepository.findAll()).thenReturn(organizationList);
        when(accountCustomizationRepository.findByIdAndIsDeleted(Long.parseLong(requestData.get(Constants.ID).toString()),
                false)).thenReturn(accountCustomization);
        when(accountCustomizationRepository.save(accountCustomization)).thenReturn(accountCustomization);

        //then
        boolean isRemoved = accountCustomizationService.removeCustomization(commonRequestDTO);
        assertTrue(isRemoved);
    }

    @Test
    void testGetAccountCustomizations() {
        //given
        Map<String, Object> requestData = Map.of(Constants.COUNTRY_ID, Constants.ONE, TestConstants.SCREEN_TYPES,
                List.of(Constants.SCREENING),
                TestConstants.CATEGORY, List.of(Constants.INPUT_FORM));
        Long countryId = Long.parseLong(requestData.get(Constants.COUNTRY_ID).toString());
        List<String> screenTypes = (List<String>) requestData.get(TestConstants.SCREEN_TYPES);
        List<String> categories = (List<String>) requestData.get(TestConstants.CATEGORY);
        List<AccountCustomization> accountCustomizationList = TestDataProvider.getAccountCustomizations();

        //when
        when(accountCustomizationRepository.findByCountryIdAndCategoryInAndTypeInAndIsDeletedFalse(countryId, categories, screenTypes))
                .thenReturn(accountCustomizationList);

        //then
        List<AccountCustomization> accountCustomizations = accountCustomizationService
                .getAccountCustomizations(requestData);
        assertNotNull(accountCustomizations);
        assertEquals(Constants.TWO, accountCustomizations.size());
        assertEquals(TestConstants.ONE, accountCustomizations.get(0).getId());
        assertEquals(TestConstants.TWO, accountCustomizations.get(1).getClinicalWorkflowId());
    }

    @Test
    void checkNull() {
        //given
        CustomizationRequestDTO customizationRequestDto = TestDataProvider.getCustomizationRequestDTO();
        customizationRequestDto.setCountryId(null);
        CommonRequestDTO requestData = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        requestData.setId(null);

        //then
        assertThrows(BadRequestException.class, () -> accountCustomizationService.updateCustomization(null));
        assertThrows(BadRequestException.class, () -> accountCustomizationService.createAccountCustomization(null));
        assertThrows(DataNotAcceptableException.class, () -> accountCustomizationService.getCustomization(null));
        assertThrows(BadRequestException.class, () -> accountCustomizationService.removeCustomization(requestData));
    }

    @Test
    void throwDataNotFoundException() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        CustomizationRequestDTO customizationRequestDTO = TestDataProvider.getCustomizationRequestDTO();
        customizationRequestDTO.setClinicalWorkflowId(TestConstants.TWO);
        Map<String, Object> requestData = Map.of(Constants.ID, TestConstants.ONE, Constants.TENANT_PARAMETER_NAME,
                TestConstants.ONE);
        AccountCustomization accountCustomization = TestDataProvider.getAccountCustomization();
        Organization organization = TestDataProvider.getOrganization();
        organization.setId(TestConstants.ONE);
        List<Organization> organizationList = List.of(organization);

        //when
        when(organizationRepository.findAll()).thenReturn(organizationList);
        when(accountCustomizationRepository.findByIdAndIsDeleted(Long.parseLong(requestData.get(Constants.ID).toString()),
                false)).thenReturn(accountCustomization);
        when(accountCustomizationRepository.findByIdAndIsDeletedAndTenantId(accountCustomization.getId(),
                Constants.BOOLEAN_FALSE, TestConstants.ONE)).thenReturn(null);
        when(accountCustomizationRepository.findByIdAndIsDeleted(Long.parseLong(requestData.get(Constants.ID)
                .toString()), false)).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> accountCustomizationService
                .updateCustomization(accountCustomization));
        assertThrows(DataNotFoundException.class, () -> accountCustomizationService.removeCustomization(commonRequestDTO));
    }
}