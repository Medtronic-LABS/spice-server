package com.mdtlabs.coreplatform.spiceadminservice.account.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.mdtlabs.coreplatform.common.model.dto.spice.AccountDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountWorkflowDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.User;

/**
 * <p>
 * AccountService is defining an interface called "AccountService" which contains several
 * methods for managing user accounts.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 7, 2023
 */
public interface AccountService {

    /**
     * <p>
     * This method is used to create a new account using the accountWorkflowDto data.
     * </p>
     *
     * @param accountWorkflowDto {@link AccountWorkflowDTO} The account workflow dto that contains
     *                           necessary information to create account is given
     * @return {@link Account} The account which is created for given account details is returned
     */
    Account createAccount(AccountWorkflowDTO accountWorkflowDto);

    /**
     * <p>
     * This method is used to update a existing account using the accountWorkflowDto data.
     * </p>
     *
     * @param accountWorkflowDto {@link AccountWorkflowDTO} The account workflow dto that contains
     *                           necessary information to update account is given
     * @return {@link Account} The account which is updated for given account details is returned
     */
    Account updateAccount(AccountWorkflowDTO accountWorkflowDto);

    /**
     * <p>
     * This method is used to retrieve an account by its ID.
     * </p>
     *
     * @param id The id of the account that need to be retrieved is given
     * @return {@link Account} The account for the given id is retrieved and returned
     */
    Account getAccountById(Long id);

    /**
     * <p>
     * This method is used to get list of account details based on given request
     * </p>
     *
     * @param commonRequestDto {@link CommonRequestDTO} The search request contains necessary information
     *                         to get the list of account details is given
     * @return {@link AccountDetailsDTO} The retrieved list of account details for given request is returned
     */
    AccountDetailsDTO getAccountDetails(CommonRequestDTO commonRequestDto);

    /**
     * <p>
     * This method is used to activate an account based on the provided request DTO.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information to activate or
     *                   deactivate the list of accounts is given
     * @return {@link Boolean} A boolean indicating whether the account is activated or Deactivated is returned
     */
    Boolean activateOrDeactivateAccount(RequestDTO requestDto);

    /**
     * <p>
     * This method is used to retrieve a list of deactivated accounts based on a search request.
     * </p>
     *
     * @param searchRequestDto {@link SearchRequestDTO} Request object containing search term and pagination
     *                         information to get deactivated accounts is given
     * @return {@link ResponseListDTO} The response list DTO containing retrieved list of deactivated accounts
     * and total count is returned
     */
    ResponseListDTO getDeactivatedAccounts(SearchRequestDTO searchRequestDto);

    /**
     * <p>
     * This method is used to clear API permissions in the account controller.
     * </p>
     */
    void clearApiPermissions();

    /**
     * <p>
     * This method is used to get list of account details with child organization counts.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The search request contains necessary information
     *                   to get the list of accounts is given
     * @return {@link ResponseListDTO} The response list DTO containing retrieved list of accounts
     * and total count is returned
     */
    ResponseListDTO getAccountList(RequestDTO requestDto);

    /**
     * <p>
     * This method is used to update an account admin with the provided details.
     * </p>
     *
     * @param user {@link User} The account admin need to be added is given
     * @return {@link User} The user is added using the provided details and returned
     */
    User createAccountAdmin(@Valid UserDetailsDTO user);

    /**
     * <p>
     * This method is used to update an account admin with the provided details.
     * </p>
     *
     * @param user {@link User} The account admin need to be updated is given
     * @return {@link User} The user is added using the provided details and returned
     */
    User updateAccountAdmin(@Valid User user);

    /**
     * <p>
     * This method is used to delete an account admin for the given request
     * </p>
     *
     * @param commonRequestDto {@link CommonRequestDTO} The parameters required to delete the
     *                         admin is given
     * @return {@link Boolean} A boolean value indicating whether the user is deleted or not is returned
     */
    Boolean deleteAccountAdmin(CommonRequestDTO commonRequestDto);

    /**
     * <p>
     * This method is used to retrieve a list of accounts based on a search request.
     * </p>
     *
     * @param searchRequestDto {@link SearchRequestDTO} The search request contains necessary information
     *                         to get the list of accounts is given
     * @return {@link ResponseListDTO} The response list DTO containing retrieved list of accounts
     * and total count is returned
     */
    ResponseListDTO getAccounts(SearchRequestDTO searchRequestDto);

    /**
     * <p>
     * This method is used to activate or deactivate the accounts based on the given account IDs.
     * </p>
     *
     * @param accountIds {@link List} The list of account IDs those need to be either activated or Deactivated
     *                   is given
     * @param isActive   {@link Boolean} The boolean value that is used to filter the accounts is given
     * @return {@link Boolean} The list of account IDs that are either activated or Deactivated is returned
     */
    List<Long> activateOrDeactivateAccounts(List<Long> accountIds, boolean isActive);

    /**
     * <p>
     * This method is used to get a list of map containing country IDs with corresponding count of accounts that
     * is searched using the given country IDs.
     * </p>
     *
     * @param countryIds {@link List} The list of country IDs associated with the accounts that
     *                    are being searched is given
     * @return {@link List<Map>} A list of map containing key as country IDs and value as count of accounts
     * for the corresponding country IDs provided is returned
     */
    Map<Long, Long> getAccountCountByCountryIds(List<Long> countryIds);
}
