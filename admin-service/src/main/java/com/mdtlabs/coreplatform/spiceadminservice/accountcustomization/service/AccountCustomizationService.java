package com.mdtlabs.coreplatform.spiceadminservice.accountcustomization.service;

import java.util.List;
import java.util.Map;

import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CustomizationRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountCustomization;

/**
 * <p>
 *  AccountCustomizationService is a Java interface for managing CRUD (Create, Read, Update, Delete)
 *  operations for account customizations. It defines methods for creating, retrieving, updating, and
 *  removing account customizations, as well as getting a list of account customizations based on
 *  provided request details.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 08, 2023
 */
public interface AccountCustomizationService {

    /**
     * <p>
     * This method is used to create an account customization using the provided account customization details.
     * </p>
     *
     * @param accountCustomization {@link AccountCustomization} The account customization need to be created
     *                                is given
     * @return {@link AccountCustomization} The created account customization for given account customization details
     * is returned
     */
    AccountCustomization createAccountCustomization(AccountCustomization accountCustomization);

    /**
     * <p>
     * This method is used to get an account customization for the provided request details.
     * </p>
     *
     * @param customizationRequestDto {@link CustomizationRequestDTO} The customization request contains necessary
     *                                information to get the account customization is given
     * @return {@link AccountCustomization} The retrieved account customization for given request details is returned
     */
    AccountCustomization getCustomization(CustomizationRequestDTO customizationRequestDto);

    /**
     * <p>
     * This method is used to update an account customization using the provided account customization details.
     * </p>
     *
     * @param accountCustomization {@link AccountCustomization} The account customization need to be updated
     *                                is given
     * @return {@link AccountCustomization} The updated account customization for given account customization details
     * is returned
     */
    AccountCustomization updateCustomization(AccountCustomization accountCustomization);

    /**
     * <p>
     * This method is used to remove an account customization using the provided request details.
     * </p>
     *
     * @param requestData {@link CommonRequestDTO} The parameters required to delete the
     *                    customization is given
     * @return The boolean value indicating whether the account customization is deleted or not is returned
     */
    boolean removeCustomization(CommonRequestDTO requestData);

    /**
     * <p>
     * This method is used to get list of account customizations for the provided request details.
     * </p>
     *
     * @param requestData {@link Map}  The request map contains necessary information to get the list of
     *                    account customizations is given
     * @return {@link List} The list of account customizations for given request data is returned
     */
    List<AccountCustomization> getAccountCustomizations(Map<String, Object> requestData);
}
