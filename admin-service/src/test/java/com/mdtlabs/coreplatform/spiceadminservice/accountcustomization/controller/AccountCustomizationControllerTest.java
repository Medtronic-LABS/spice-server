package com.mdtlabs.coreplatform.spiceadminservice.accountcustomization.controller;

import java.util.List;
import java.util.Map;

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

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountCustomizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CustomizationRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountCustomization;
import com.mdtlabs.coreplatform.spiceadminservice.accountcustomization.service.impl.AccountCustomizationServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Account customization controller.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class AccountCustomizationControllerTest {

    @InjectMocks
    private AccountCustomizationController accountCustomizationController;

    @Mock
    private AccountCustomizationServiceImpl accountCustomizationService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(AccountCustomizationController.class, "modelMapper", accountCustomizationController);
    }

    @Test
    void testAddCustomization() {
        //given
        AccountCustomization accountCustomization = TestDataProvider.getAccountCustomization();
        AccountCustomizationDTO accountCustomizationDto = TestDataProvider.getAccountCustomizationDto();

        //when
        when(modelMapper.map(accountCustomizationDto, AccountCustomization.class)).thenReturn(accountCustomization);
        when(accountCustomizationService.createAccountCustomization(accountCustomization))
                .thenReturn(accountCustomization);

        //then
        SuccessResponse<AccountCustomization> actualAccountCustomization = accountCustomizationController
                .addCustomization(accountCustomizationDto);
        assertNotNull(actualAccountCustomization);
        assertEquals(HttpStatus.CREATED, actualAccountCustomization.getStatusCode());
    }

    @Test
    void testGetCustomization() {
        //given
        AccountCustomization accountCustomization = TestDataProvider.getAccountCustomization();
        CustomizationRequestDTO customizationRequestDTO = TestDataProvider.getCustomizationRequestDTO();

        //when
        when(accountCustomizationService.getCustomization(customizationRequestDTO)).thenReturn(accountCustomization);

        //then
        SuccessResponse<AccountCustomization> actualAccountCustomization = accountCustomizationController
                .getCustomization(customizationRequestDTO);
        assertNotNull(actualAccountCustomization);
        assertEquals(HttpStatus.OK, actualAccountCustomization.getStatusCode());
    }

    @Test
    void testUpdateCustomization() {
        //given
        AccountCustomization accountCustomization = TestDataProvider.getAccountCustomization();
        AccountCustomizationDTO accountCustomizationDto = TestDataProvider.getAccountCustomizationDto();

        //when
        when(modelMapper.map(accountCustomizationDto, AccountCustomization.class)).thenReturn(accountCustomization);
        when(accountCustomizationService.updateCustomization(accountCustomization)).thenReturn(accountCustomization);

        //then
        SuccessResponse<AccountCustomization> actualAccountCustomization = accountCustomizationController
                .updateCustomization(accountCustomizationDto);
        assertNotNull(actualAccountCustomization);
        assertEquals(HttpStatus.OK, actualAccountCustomization.getStatusCode());
    }

    @Test
    void testRemoveCustomization() {
        //given
        CommonRequestDTO requestData = TestDataProvider.getCommonRequestDTO(138l, 208l);
        //when
        when(accountCustomizationService.removeCustomization(requestData)).thenReturn(true);

        //then
        SuccessResponse<String> actualAccountCustomization = accountCustomizationController
                .removeCustomization(requestData);
        assertNotNull(actualAccountCustomization);
        assertEquals(HttpStatus.OK, actualAccountCustomization.getStatusCode());
    }

    @Test
    void testGetAccountCustomizations() {
        //given
        List<AccountCustomization> accountCustomizations = List.of(TestDataProvider.getAccountCustomization());
        Map<String, Object> requestData = Map.of(Constants.COUNTRY_ID, Constants.ONE, TestConstants.SCREEN_TYPES,
                Constants.SCREENING);

        //when
        when(accountCustomizationService.getAccountCustomizations(requestData)).thenReturn(accountCustomizations);

        //then
        List<AccountCustomization> actualAccountCustomizations = accountCustomizationController
                .getAccountCustomizations(requestData);
        assertNotNull(actualAccountCustomizations);
        assertEquals(accountCustomizations.size(), actualAccountCustomizations.size());
    }
}
