package com.mdtlabs.coreplatform.spiceadminservice.account.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.common.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mdtlabs.coreplatform.AuthenticationFilter;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountWorkflowDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountWorkflow;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.spiceadminservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceadminservice.account.repository.AccountRepository;
import com.mdtlabs.coreplatform.spiceadminservice.account.service.AccountService;
import com.mdtlabs.coreplatform.spiceadminservice.accountworkflow.service.AccountWorkflowService;
import com.mdtlabs.coreplatform.spiceadminservice.operatingunit.service.OperatingUnitService;
import com.mdtlabs.coreplatform.spiceadminservice.site.service.SiteService;

/**
 * <p>
 * AccountServiceImpl class implements various methods for managing accounts, including creating,
 * updating, and retrieving account information.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 7, 2023
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountWorkflowService accountWorkflowService;

    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Autowired
    private OperatingUnitService operatingUnitService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private UserApiInterface userApiInterface;

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private RedisTemplate<String, List<Organization>> redisTemplate;

    /**
     * {@inheritDoc}
     */
    public Account createAccount(AccountWorkflowDTO accountDto) {
        Logger.logInfo("Creating account details with given data");
        if (Objects.isNull(accountDto)) {
            Logger.logError("Request should not be empty.");
            throw new BadRequestException(1003);
        }
        if (!Objects.isNull(accountDto.getClinicalWorkflow()) && accountDto.getClinicalWorkflow().isEmpty()) {
            Logger.logError(accountDto.getName().concat("'s Clinical workflows should not be empty"));
            throw new BadRequestException(2103, accountDto.getName());
        }
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        boolean isAccountExists = accountRepository
                .existsByNameIgnoreCaseAndIsDeletedFalse(accountDto.getName().strip());
        if (isAccountExists) {
            Logger.logError(accountDto.getName().strip() + " name already exists.");
            throw new DataConflictException(2101, accountDto.getName().strip());
        }
        Account account = modelMapper.map(accountDto, Account.class);
        account.setCountry(new Country(accountDto.getCountryId()));
        account.setClinicalWorkflows(getAccountWorkflows(accountDto.getClinicalWorkflow()));
        account.setCustomizedWorkflows(getAccountWorkflows(accountDto.getCustomizedWorkflow()));
        return accountRepository.save(account);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public Account updateAccount(AccountWorkflowDTO accountWorkflowDto) {
        Logger.logInfo("Updating account with the given details");
        Account existingAccount = validateAccount(accountWorkflowDto);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(accountWorkflowDto, existingAccount);
        existingAccount.setClinicalWorkflows(getAccountWorkflows(accountWorkflowDto.getClinicalWorkflow()));
        existingAccount.setCustomizedWorkflows(
                getAccountWorkflows(accountWorkflowDto.getCustomizedWorkflow()));
        Organization organization = new Organization();
        organization.setId(accountWorkflowDto.getTenantId());
        organization.setName(accountWorkflowDto.getName());
        userApiInterface.updateOrganization(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                modelMapper.map(organization, OrganizationDTO.class));
        return accountRepository.save(existingAccount);
    }

    /**
     * {@inheritDoc}
     */
    public Account getAccountById(Long id) {
        Logger.logDebug(("Getting account information by passing id - ").concat(id.toString()));
        Account account = accountRepository.findByIdAndIsActiveAndIsDeleted(id, true, false);
        if (Objects.isNull(account)) {
            Logger.logError("No Account found for this ID.");
            throw new DataNotFoundException(2102);
        }
        return account;
    }

    /**
     * {@inheritDoc}
     */
    public AccountDetailsDTO getAccountDetails(CommonRequestDTO commonRequestDto) {
        if (Objects.isNull(commonRequestDto.getId()) || Objects.isNull(commonRequestDto.getTenantId())) {
            Logger.logError("Tenant id  should not be empty or null.");
            throw new DataNotAcceptableException(1005);
        }
        Logger.logDebug(("Getting account information by passing id - ").concat(commonRequestDto.getId().toString()));
        Account account = accountRepository.findByIdAndTenantIdAndIsActiveAndIsDeleted(commonRequestDto.getId(),
                commonRequestDto.getTenantId(), true, false);
        if (Objects.isNull(account)) {
            Logger.logError(("No Account found for this ID - ").concat(commonRequestDto.getId().toString()));
            throw new DataNotFoundException(2102);
        }
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        AccountDetailsDTO accountDetailsDto = modelMapper.map(account, AccountDetailsDTO.class);
        accountDetailsDto.setUsers(userApiInterface.getUsersByTenantIds(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), List.of(account.getTenantId())));
        CountryDTO countryDto = modelMapper.map(account.getCountry(), CountryDTO.class);
        accountDetailsDto.setCountry(countryDto);
        accountDetailsDto.setCountryCode(countryDto.getCountryCode());
        accountDetailsDto.setClinicalWorkflow(account.getClinicalWorkflows());
        accountDetailsDto.setCustomizedWorkflow(account.getCustomizedWorkflows());
        return accountDetailsDto;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO getDeactivatedAccounts(SearchRequestDTO searchRequestDto) {
        Logger.logInfo("Get inactive accounts list");
        ResponseListDTO response = new ResponseListDTO();
        String searchTerm = searchRequestDto.getSearchTerm();
        if (!CommonUtil.isValidSearchData(searchTerm, Constants.SEARCH_TERM)) {
            response.setTotalCount(0L);
            return response;
        }
        if (StringUtils.isNotBlank(searchTerm)) {
            searchTerm = searchTerm.strip();
        }
        Long countryId = null;
        if (!Objects.isNull(searchRequestDto.getTenantId()) && Constants.ZERO != searchRequestDto.getTenantId()) {
            Organization organization = userApiInterface.getOrganizationById(CommonUtil.getAuthToken(),
                    UserSelectedTenantContextHolder.get(), searchRequestDto.getTenantId()).getBody();
            countryId = !Objects.isNull(organization) ? organization.getFormDataId() : null;
        }
        Pageable pageable = Pagination.setPagination(searchRequestDto.getSkip(), searchRequestDto.getLimit(),
                Constants.UPDATED_AT, false);
        Page<Account> accounts = accountRepository.getDeactivatedAccounts(searchTerm, countryId, pageable);
        if (!Objects.isNull(accounts) && !accounts.isEmpty()) {
            response.setTotalCount(accounts.getTotalElements());
            response.setData(
                    accounts.stream().map(account -> modelMapper.map(account, AccountDTO.class)).toList());
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    public void clearApiPermissions() {
        Logger.logInfo("Clearing API permission");
        authenticationFilter.apiPermissionMap.clear();
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO getAccountList(RequestDTO requestDto) {
        Logger.logInfo("Fetch account details with given data");
        Pageable pageable = Pagination.setPagination(requestDto.getSkip(), requestDto.getLimit());
        String searchTerm = (!Objects.isNull(requestDto.getSearchTerm()))
                ? requestDto.getSearchTerm().replaceAll(Constants.SEARCH_TERM, Constants.EMPTY) : Constants.EMPTY;
        Organization organization = userApiInterface.getOrganizationById(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), requestDto.getTenantId()).getBody();
        List<AccountListDTO> accountListDTOs = new ArrayList<>();
        ResponseListDTO response = new ResponseListDTO();
        if (!Objects.isNull(organization)) {
            Page<Account> pagedAccounts = accountRepository.findAccountList(searchTerm, organization.getFormDataId(),
                    pageable);
            List<Account> accounts = pagedAccounts.stream().toList();

            response.setTotalCount(pagedAccounts.getTotalElements());
            constructAccountListResponse(accountListDTOs, accounts);
        }
        response.setData(accountListDTOs);
        return response;
    }

    /**
     * {@inheritDoc}
     */
    public User createAccountAdmin(UserDetailsDTO user) {
        Logger.logDebug(("Create new account user. RequestData - ").concat(user.toString()));
        Role role = new Role();
        role.setName(Constants.ROLE_ACCOUNT_ADMIN);
        user.setRoles(Set.of(role));
        ResponseEntity<User> response = userApiInterface.addAdminUser(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), user, Boolean.FALSE);
        return response.getBody();
    }

    /**
     * {@inheritDoc}
     */
    public User updateAccountAdmin(User user) {
        Logger.logDebug(("Update the existing account user id: ").concat(user.getId().toString()));
        ResponseEntity<User> response = userApiInterface.updateAdminUser(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), modelMapper.map(user, UserDTO.class));
        return response.getBody();
    }

    /**
     * {@inheritDoc}
     */
    public Boolean deleteAccountAdmin(CommonRequestDTO commonRequestDto) {
        Logger.logDebug(("Delete the account user id - ").concat(commonRequestDto.getId().toString()));
        ResponseEntity<Boolean> response = userApiInterface.deleteAdminUser(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), commonRequestDto);
        return response.getBody();
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO getAccounts(SearchRequestDTO searchRequestDto) {
        Logger.logDebug(("Get account list by passing tenant_id - ").concat(searchRequestDto.getTenantId().toString()));
        String searchTerm = searchRequestDto.getSearchTerm();
        Pageable pageable = null;
        ResponseListDTO response = new ResponseListDTO();
        if (!CommonUtil.isValidSearchData(searchTerm, Constants.SEARCH_TERM)) {
            response.setTotalCount(0L);
            return response;
        }
        if (0 < searchRequestDto.getLimit()) {
            pageable = Pagination.setPagination(searchRequestDto.getSkip(), searchRequestDto.getLimit(),
                    Constants.UPDATED_AT, Constants.BOOLEAN_FALSE);
        }
        Organization organization = userApiInterface.getOrganizationById(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), searchRequestDto.getTenantId()).getBody();
        if (!Objects.isNull(organization)) {
            Page<Account> accounts = accountRepository.findAccountList(searchTerm, organization.getFormDataId(),
                    pageable);
            response.setTotalCount(accounts.getTotalElements());
            if (!accounts.stream().toList().isEmpty()) {
                response.setData(modelMapper.map(accounts.stream().toList(), new TypeToken<List<AccountDTO>>() {
                }.getType()));
            }
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    public List<Long> activateOrDeactivateAccounts(List<Long> countryIds, boolean isActive) {
        String status = isActive ? Constants.ACTIVATE : Constants.DEACTIVATE;
        Logger.logInfo(status.concat(" list of accounts org id: ").concat(countryIds.toString()));
        List<Account> accounts = accountRepository.findAccountByCountryIdAndIsActive(countryIds, !isActive);
        List<Long> tenantIds = new ArrayList<>();
        if (!accounts.isEmpty()) {
            accounts.forEach(account -> {
                account.setActive(isActive);
                tenantIds.add(account.getTenantId());
            });
            accountRepository.saveAll(accounts);
            redisTemplate.delete(Constants.ORGANIZATION_REDIS_KEY);
        }
        return tenantIds;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public Boolean activateOrDeactivateAccount(RequestDTO requestDto) {
        String status = Boolean.TRUE.equals(requestDto.getIsActive()) ? Constants.ACTIVATE : Constants.DEACTIVATE;
        Logger.logDebug(status.concat(" the account org id: ").concat(requestDto.getTenantId().toString()));
        Boolean isActive = requestDto.getIsActive();
        Account account = accountRepository.findByTenantIdAndIsDeletedFalseAndIsActive(requestDto.getTenantId(),
                !isActive);
        if (Objects.isNull(account)) {
            Logger.logError("No Account found for this ID");
            throw new DataNotFoundException(2102);
        }
        account.setReason(requestDto.getReason());
        account.setStatus(requestDto.getStatus());
        account.setActive(isActive);
        account = accountRepository.save(account);
        List<Long> tenantIds = new ArrayList<>();
        tenantIds.addAll(operatingUnitService.activateOrDeactivateOperatingUnits(null, account.getId(), isActive));
        tenantIds.addAll(siteService.activateOrDeactivateSites(null, account.getId(), null, isActive));
        tenantIds.add(requestDto.getTenantId());
        userApiInterface.activateOrDeactivateOrg(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                tenantIds, isActive);
        userApiInterface.activateOrDeactivateUser(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                tenantIds, isActive);
        redisTemplate.delete(Constants.ORGANIZATION_REDIS_KEY);
        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    public Map<Long, Long> getAccountCountByCountryIds(List<Long> countryIds) {
        Map<Long, Long> accountCountsMap = new HashMap<>();
        List<Map<String, Object>> accountCounts = accountRepository.getAccountCountByCountryIds(countryIds);
        accountCounts.forEach(count ->
                accountCountsMap.put((Long) count.get(Constants.COUNTRY_ID), (Long) count.get(Constants.COUNT)));
        return accountCountsMap;
    }

    /**
     * <p>
     * This method is used to retrieve a list of account workflows based on a list of workflow IDs.
     * </p>
     *
     * @param workflowIds {@link List} A list of account workflows IDs that need to retrieve is given
     * @return {@link List} The list of account workflows retrieved for the given workflow IDs is returned
     */
    private List<AccountWorkflow> getAccountWorkflows(List<Long> workflowIds) {
        List<AccountWorkflow> workflows = null;
        if (null != workflowIds && !workflowIds.isEmpty()) {
            workflows = accountWorkflowService.getAccountWorkflowsByIds(workflowIds);
            if (workflowIds.size() != workflows.size()) {
                Logger.logError("Some of the given workflows are not in the system.");
                throw new DataNotFoundException(2104);
            }
        }
        return workflows;
    }

    /**
     * This method is used to validate an account by checking if it exists, if its name is unique,
     * and if its clinical workflows are not empty or null.
     *
     * @param accountWorkflowDto {@link AccountWorkflowDTO} The information about an account and its
     *                           associated workflows is given
     * @return {@link Account} The validated account is returned
     */
    private Account validateAccount(AccountWorkflowDTO accountWorkflowDto) {
        if (Objects.isNull(accountWorkflowDto)) {
            Logger.logError("Request should not be empty.");
            throw new BadRequestException(1003);
        }
        Account existingAccount = accountRepository.findByIdAndIsDeleted(accountWorkflowDto.getId(), Boolean.FALSE);

        if (Objects.isNull(existingAccount)) {
            Logger.logError(("No Account found for this ID : ").concat(accountWorkflowDto.getId().toString()));
            throw new DataNotFoundException(2102);
        }

        boolean exists = accountRepository.existsByNameIgnoreCaseAndIsDeletedFalseAndIdNot(accountWorkflowDto.getName(), accountWorkflowDto.getId());
        if (exists) {
            Logger.logError(accountWorkflowDto.getName().strip() + " name already exists.");
            throw new DataConflictException(2101, accountWorkflowDto.getName().strip());
        }

        if (Objects.isNull(accountWorkflowDto.getClinicalWorkflow())
                || accountWorkflowDto.getClinicalWorkflow().isEmpty()
                || accountWorkflowDto.getClinicalWorkflow().stream().anyMatch(Objects::isNull)) {
            Logger.logError(accountWorkflowDto.getName().concat("'s Clinical workflows should not be empty."));
            throw new BadRequestException(2103, accountWorkflowDto.getName());
        }
        return existingAccount;
    }

    /**
     * <p>
     * This method is used to construct a response list of account DTOs using the list of accounts.
     * </p>
     *
     * @param accountListDTOs {@link List<AccountDTO>} A list of account list DTOs to add
     *                        the constructed account list DTO is given
     * @param accounts        {@link List<Country>} A list of accounts that provide the data like
     *                        ID, tenant ID, name etc., is given
     */
    private void constructAccountListResponse(List<AccountListDTO> accountListDTOs, List<Account> accounts) {
        if (!accounts.isEmpty()) {
            List<Long> accountIds = accounts.stream().map(BaseEntity::getId).toList();
            Map<Long, Long> noOfSites = siteService.getCountByAccountIds(accountIds);
            Map<Long, Long> noOfOperatingUnits = operatingUnitService
                    .getOperatingUnitCountByAccountIds(accountIds);
            for (Account account : accounts) {
                AccountListDTO accountListDto = new AccountListDTO(account.getId(), account.getName(),
                        account.getTenantId());

                accountListDto.setOuCount(noOfOperatingUnits.get(account.getId()));
                accountListDto.setSiteCount(noOfSites.get(account.getId()));
                accountListDTOs.add(accountListDto);
            }
        }
    }
}
