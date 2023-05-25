package com.mdtlabs.coreplatform.spiceadminservice.account.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.annotations.UserTenantValidation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountWorkflowDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.spiceadminservice.account.service.AccountService;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;

/**
 * <p>
 * Account Controller class defines the endpoints for managing accounts, including creating,
 * updating, and retrieving account details, activating and deactivating accounts,
 * and managing account admins.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 7, 2023
 */
@RestController
@RequestMapping(value = "/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>
     * This method is used to create a new account using the accountWorkflowDto data.
     * </p>
     *
     * @param accountWorkflowDto {@link AccountWorkflowDTO} The account workflow dto that contains
     *                           necessary information to create account is given
     * @return {@link Account} The account which is created for given account details is returned
     */
    @PostMapping("/create")
    public Account addAccount(@Valid @RequestBody AccountWorkflowDTO accountWorkflowDto) {
        Logger.logInfo("In Account controller, creating an account");
        return accountService.createAccount(accountWorkflowDto);
    }

    /**
     * <p>
     * This method is used to update a existing account using the accountWorkflowDto data.
     * </p>
     *
     * @param accountWorkflowDto {@link AccountWorkflowDTO} The account workflow dto that contains
     *                           necessary information to update account is given
     * @return {@link SuccessResponse<Account>} Returns a success message and status with the updated account
     */
    @UserTenantValidation
    @PutMapping("/update")
    public SuccessResponse<Account> updateAccount(@Valid @RequestBody AccountWorkflowDTO accountWorkflowDto) {
        Logger.logInfo("In Account controller, updating an account");
        return new SuccessResponse<>(SuccessCode.ACCOUNT_UPDATE, HttpStatus.OK,
                accountService.updateAccount(accountWorkflowDto).getName());
    }

    /**
     * <p>
     * This method is used to get list of account details based on given request
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The search request contains necessary information
     *                   to get the list of account details is given
     * @return {@link SuccessResponse<AccountDetailsDTO>} Returns a success message and status with the retrieved
     * list of account details
     */
    @UserTenantValidation
    @PostMapping("/details")
    public SuccessResponse<AccountDetailsDTO> getAccountDetails(@RequestBody CommonRequestDTO requestDto) {
        Logger.logInfo("In Account controller, getting an account details");
        return new SuccessResponse<>(SuccessCode.GET_ACCOUNT,
                accountService.getAccountDetails(requestDto), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to activate an account based on the provided request DTO.
     * </p>
     *
     * @param requestDTO {@link RequestDTO} The request contains necessary information
     *                   to activate the list of accounts is given
     * @return {@link SuccessResponse} Returns a success message and status
     * after activating the account for the given id
     */
    @UserTenantValidation
    @PutMapping("/activate")
    public SuccessResponse<String> activateAccountById(@RequestBody RequestDTO requestDTO) {
        Logger.logInfo("In Account controller, activating an account organization");
        RequestDTO requestDto = new RequestDTO();
        requestDto.setIsActive(true);
        requestDto.setTenantId(requestDTO.getTenantId());
        accountService.activateOrDeactivateAccount(requestDto);
        return new SuccessResponse<>(SuccessCode.ACCOUNT_ACTIVATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to deactivate an account based on the provided request DTO.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information
     *                   to deactivate the list of accounts is given
     * @return {@link SuccessResponse} Returns a success message and status
     * after deactivating the account for the given id
     */
    @UserTenantValidation
    @PutMapping("/deactivate")
    public SuccessResponse<String> deactivateAccountById(@RequestBody RequestDTO requestDto) {
        Logger.logInfo("In Account controller, deactivating an account organization");
        requestDto.setIsActive(false);
        requestDto.setTenantId(requestDto.getTenantId());
        accountService.activateOrDeactivateAccount(requestDto);
        return new SuccessResponse<>(SuccessCode.ACCOUNT_DEACTIVATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to retrieve a list of deactivated accounts based on a search request.
     * </p>
     *
     * @param searchRequestDto {@link SearchRequestDTO} Request object containing search term and pagination
     *                         information to get deactivated accounts is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with the retrieved
     * list of deactivated accounts and total count
     */
    @UserTenantValidation
    @PostMapping("/deactivate-list")
    public SuccessResponse<List<AccountDTO>> getDeactivatedAccounts(@RequestBody SearchRequestDTO searchRequestDto) {
        Logger.logInfo("In Account controller, getting deactivated account list");
        ResponseListDTO response = accountService.getDeactivatedAccounts(searchRequestDto);
        if ((Objects.isNull(response.getTotalCount()) || 0L == response.getTotalCount())) {
            return new SuccessResponse(SuccessCode.GET_DEACTIVATED_ACCOUNT, new ArrayList<>(), HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.GET_DEACTIVATED_ACCOUNT, response.getData(),
                response.getTotalCount(), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to retrieve an account by its ID from the account service.
     * </p>
     *
     * @param id The id of the account that need to be retrieved is given
     * @return {@link Account} The account for the given id is retrieved and returned
     */
    @GetMapping("/get-account/{id}")
    public Account getAccount(@PathVariable(Constants.ID) long id) {
        Logger.logInfo("In Account controller, getting account");
        return accountService.getAccountById(id);
    }

    /**
     * <p>
     * This method is used to clear API permissions in the account controller.
     * </p>
     */
    @GetMapping("/clear")
    public void clearApiPermissions() {
        Logger.logInfo("In Account controller, clearing API permissions");
        accountService.clearApiPermissions();
    }

    /**
     * <p>
     * This method is used to get list of account details with child organization counts.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The search request contains necessary information
     *                   to get the list of accounts is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with the retrieved
     * list of accounts and total count
     */
    @UserTenantValidation
    @PostMapping("/list")
    public SuccessResponse<List<AccountListDTO>> getAccountList(@RequestBody RequestDTO requestDto) {
        Logger.logInfo("In Account controller, getting account list");
        ResponseListDTO response = accountService.getAccountList(requestDto);
        if ((Objects.isNull(response.getTotalCount()) || 0L == response.getTotalCount())) {
            return new SuccessResponse(SuccessCode.GET_ACCOUNTS, response.getData(),
                    HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.GET_ACCOUNTS, response.getData(),
                response.getTotalCount(), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to retrieve a list of accounts based on a search request.
     * </p>
     *
     * @param searchRequestDto {@link SearchRequestDTO} The search request contains necessary information
     *                         to get the list of accounts is given
     * @return {@link SuccessResponse} Returns a success message and status with the retrieved
     * list of accounts and total count
     */
    @UserTenantValidation
    @PostMapping("/account-list")
    public SuccessResponse<List<AccountDTO>> getAccounts(@RequestBody SearchRequestDTO searchRequestDto) {
        Logger.logInfo("In Account controller, getting account list");
        ResponseListDTO response = accountService.getAccounts(searchRequestDto);
        if ((Objects.isNull(response.getTotalCount()) || 0L == response.getTotalCount())) {
            return new SuccessResponse(SuccessCode.GET_ACCOUNTS, response.getData(), HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.GET_ACCOUNTS, response.getData(), response.getTotalCount(),
                HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to create an account admin with the provided details.
     * </p>
     *
     * @param userDto {@link UserDTO} The account admin need to be added is given
     * @return {@link SuccessResponse<User>} The user is added and a success message with the
     * added user's first name and status is returned
     */
    @UserTenantValidation
    @PostMapping("/user-add")
    public SuccessResponse<User> addAccountAdmin(@RequestBody @Valid UserDetailsDTO userDto) {
        Logger.logInfo("In Account controller, creating an account admin");
        User user = accountService.createAccountAdmin(userDto);
        return new SuccessResponse<>(SuccessCode.ACCOUNT_ADMIN_SAVE, HttpStatus.CREATED, user.getFirstName());
    }

    /**
     * <p>
     * This method is used to update an account admin with the provided details.
     * </p>
     *
     * @param userDto {@link UserDTO} The account admin need to be updated is given
     * @return {@link SuccessResponse<User>} The user is updated and a success message with the
     * updated user's first name and status is returned
     */
    @UserTenantValidation
    @PutMapping("/user-update")
    public SuccessResponse<User> updateAccountAdmin(@RequestBody @Valid UserDetailsDTO userDto) {
        Logger.logInfo("In Account controller, updating an account admin");
        User user = accountService.updateAccountAdmin(modelMapper.map(userDto, User.class));
        return new SuccessResponse<>(SuccessCode.ACCOUNT_ADMIN_UPDATE, HttpStatus.OK, user.getFirstName());
    }

    /**
     * <p>
     * This method is used to delete an account admin for the given request
     * </p>
     *
     * @param commonRequestDto {@link CommonRequestDTO} The parameters required to delete the
     *                         admin is given
     * @return {@link SuccessResponse<User>} The user for the given request is
     * deleted and success message with status is returned
     */
    @UserTenantValidation
    @DeleteMapping("/user-remove")
    public SuccessResponse<User> deleteAccountAdmin(@RequestBody CommonRequestDTO commonRequestDto) {
        Logger.logInfo("In Account controller, removing an account admin");
        accountService.deleteAccountAdmin(commonRequestDto);
        return new SuccessResponse<>(SuccessCode.ACCOUNT_ADMIN_DELETE, HttpStatus.OK);
    }
}
