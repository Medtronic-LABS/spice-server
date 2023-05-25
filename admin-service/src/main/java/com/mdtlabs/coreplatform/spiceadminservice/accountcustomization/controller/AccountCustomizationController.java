package com.mdtlabs.coreplatform.spiceadminservice.accountcustomization.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdtlabs.coreplatform.common.annotations.UserTenantValidation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountCustomizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CustomizationRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountCustomization;
import com.mdtlabs.coreplatform.spiceadminservice.accountcustomization.service.AccountCustomizationService;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;


/**
 * <p>
 * Account customization controller class defines REST API endpoints for creating,
 * retrieving, updating, and deleting account customizations, as well as getting a list
 * of account customizations based on certain criteria.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 08, 2023
 */
@RestController
@RequestMapping(value = "/account-customization")
public class AccountCustomizationController {

    @Autowired
    private AccountCustomizationService accountCustomizationService;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>
     * This method is used to create an account customization using the given account customization dto.
     * </p>
     *
     * @param accountCustomizationDto {@link AccountCustomizationDTO} The account customization need to be created
     *                                is given
     * @return {@link SuccessResponse<AccountCustomization>} Returns a success message and status after
     * the account customization is created
     */
    @UserTenantValidation
    @PostMapping("/create")
    public SuccessResponse<AccountCustomization> addCustomization(
            @Valid @RequestBody AccountCustomizationDTO accountCustomizationDto) {
        Logger.logInfo("In AccountCustomization controller, Adding an account customization");
        accountCustomizationService
                .createAccountCustomization(modelMapper.map(accountCustomizationDto, AccountCustomization.class));
        return new SuccessResponse<>(SuccessCode.ACCOUNT_CUSTOMIZATION_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is to get the account customization data details based on given request.
     * </p>
     *
     * @param customizationRequestDto {@link CustomizationRequestDTO} The customization request contains necessary
     *                                information to get the account customization is given
     * @return {@link SuccessResponse<AccountCustomization>} Returns a success message and status with the retrieved
     * account customization
     */
    @UserTenantValidation
    @PostMapping("/details")
    public SuccessResponse<AccountCustomization> getCustomization(
            @RequestBody CustomizationRequestDTO customizationRequestDto) {
        Logger.logInfo("In AccountCustomization controller, fetching an account customization");
        return new SuccessResponse<>(SuccessCode.GET_ACCOUNT_CUSTOMIZATION,
                accountCustomizationService.getCustomization(customizationRequestDto), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to update an account customization using the given account customization dto.
     * </p>
     *
     * @param accountCustomizationDto {@link AccountCustomizationDTO} The account customization need to be updated
     *                                is given
     * @return {@link SuccessResponse} Returns a success message and status after
     * the account customization is updated
     */
    @UserTenantValidation
    @PutMapping("/update")
    public SuccessResponse<AccountCustomization> updateCustomization(
            @Valid @RequestBody AccountCustomizationDTO accountCustomizationDto) {
        Logger.logInfo("In AccountCustomization controller, updating an account customization");
        accountCustomizationService
                .updateCustomization(modelMapper.map(accountCustomizationDto, AccountCustomization.class));
        return new SuccessResponse<>(SuccessCode.ACCOUNT_CUSTOMIZATION_UPDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to remove account customization based on the given request.
     * </p>
     *
     * @param requestData {@link CommonRequestDTO} The parameters required to delete the
     *                    customization is given
     * @return {@link SuccessResponse<String>} The success message with the
     * status is returned if the account customization is deleted
     */
    @PutMapping("/remove")
    public SuccessResponse<String> removeCustomization(@RequestBody CommonRequestDTO requestData) {
        Logger.logInfo("In AccountCustomization controller, removing an account customization");
        accountCustomizationService.removeCustomization(requestData);
        return new SuccessResponse<>(SuccessCode.ACCOUNT_CUSTOMIZATION_DELETE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get the list account customization data details such as screening, enrollment and
     * consent forms based on category id, type and country id.
     * </p>
     *
     * @param requestData {@link Map}  The request map contains necessary information to get the list of
     *                    account customizations is given
     * @return {@link List} The list of account customizations for given request data is returned
     */
    @PostMapping("/static-data/get-list")
    public List<AccountCustomization> getAccountCustomizations(@RequestBody Map<String, Object> requestData) {
        Logger.logInfo("In AccountCustomization controller, fetching an account customization list");
        return accountCustomizationService.getAccountCustomizations(requestData);
    }
}
