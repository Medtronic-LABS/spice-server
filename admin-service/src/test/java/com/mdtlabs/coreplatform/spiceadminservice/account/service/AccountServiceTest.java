package com.mdtlabs.coreplatform.spiceadminservice.account.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.InheritingConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
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
import com.mdtlabs.coreplatform.common.model.dto.spice.UserOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountWorkflow;
import com.mdtlabs.coreplatform.spiceadminservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceadminservice.account.repository.AccountRepository;
import com.mdtlabs.coreplatform.spiceadminservice.account.service.impl.AccountServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.accountworkflow.service.AccountWorkflowService;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.operatingunit.service.OperatingUnitService;
import com.mdtlabs.coreplatform.spiceadminservice.site.service.SiteService;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Account service implementation.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountServiceTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private OperatingUnitService operatingUnitService;

    @Mock
    private SiteService siteService;

    @Mock
    private AccountWorkflowService accountWorkflowService;

    @Mock
    private UserApiInterface userApiInterface;

    @Mock
    private RedisTemplate<String, List<Organization>> redisTemplate;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(AccountServiceImpl.class, "modelMapper", accountService);
    }

    @Test
    void testCreateAccount() {
        //given
        List<Long> workflowIds = List.of(TestConstants.ONE, TestConstants.TWO);
        AccountWorkflowDTO accountWorkflowDTO = TestDataProvider.getAccountworkFlowDto();
        accountWorkflowDTO.setClinicalWorkflow(workflowIds);
        accountWorkflowDTO.setCustomizedWorkflow(workflowIds);
        List<AccountWorkflow> accountWorkflows = TestDataProvider.getAccountWorkFlows();
        Account account = TestDataProvider.getAccount();

        //when
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(accountWorkflowDTO, Account.class)).thenReturn(account);
        when(accountWorkflowService.getAccountWorkflowsByIds(workflowIds)).thenReturn(accountWorkflows);
        when(accountRepository.existsByNameIgnoreCaseAndIsDeletedFalse(accountWorkflowDTO.getName().strip()))
                .thenReturn(false);
        when(accountRepository.save(account)).thenReturn(account);
        when(redisTemplate.delete(Constants.ORGANIZATION_REDIS_KEY)).thenReturn(Boolean.TRUE);

        //then
        Account actualAccount = accountService.createAccount(accountWorkflowDTO);
        assertNotNull(actualAccount);
        assertEquals(accountWorkflowDTO.getName(), actualAccount.getName());
    }

    @Test
    void testCreateAccountWithNullWorkflow() {
        //given
        List<Long> workflowIds = List.of(TestConstants.ONE, TestConstants.TWO);
        AccountWorkflowDTO accountWorkflowDTO = TestDataProvider.getAccountworkFlowDto();
        accountWorkflowDTO.setClinicalWorkflow(workflowIds);
        accountWorkflowDTO.setCustomizedWorkflow(null);
        List<AccountWorkflow> accountWorkflows = TestDataProvider.getAccountWorkFlows();
        Account account = TestDataProvider.getAccount();

        //when
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(accountWorkflowDTO, Account.class)).thenReturn(account);
        when(accountWorkflowService.getAccountWorkflowsByIds(workflowIds)).thenReturn(accountWorkflows);
        when(accountRepository.existsByNameIgnoreCaseAndIsDeletedFalse(accountWorkflowDTO.getName().strip()))
                .thenReturn(false);
        when(accountRepository.save(account)).thenReturn(account);

        //then
        Account actualAccount = accountService.createAccount(accountWorkflowDTO);
        assertNotNull(actualAccount);
        assertEquals(accountWorkflowDTO.getName(), actualAccount.getName());
    }

    @Test
    void testUpdateAccount() {
        //given
        List<Long> workflowIds = List.of(TestConstants.ONE, TestConstants.TWO);
        AccountWorkflowDTO accountWorkflowDTO = TestDataProvider.getAccountworkFlowDto();
        accountWorkflowDTO.setId(TestConstants.ONE);
        accountWorkflowDTO.setTenantId(TestConstants.ONE);
        accountWorkflowDTO.setClinicalWorkflow(workflowIds);
        accountWorkflowDTO.setCustomizedWorkflow(workflowIds);
        Account account = TestDataProvider.getAccount();
        Organization organization = TestDataProvider.getOrganization();
        organization.setId(accountWorkflowDTO.getTenantId());
        organization.setName(accountWorkflowDTO.getName());
        List<AccountWorkflow> accountWorkflows = TestDataProvider.getAccountWorkFlows();

        //when
        TestCommonMethods.init();
        TestCommonMethods.getStaticMock();
        when(accountRepository.findByIdAndIsDeleted(accountWorkflowDTO.getId(), false)).thenReturn(account);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(accountWorkflowDTO, Account.class)).thenReturn(account);
        when(accountWorkflowService.getAccountWorkflowsByIds(workflowIds)).thenReturn(accountWorkflows);
        when(userApiInterface.updateOrganization(TestConstants.TEST_TOKEN, TestConstants.ONE, new OrganizationDTO()))
                .thenReturn(organization);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.existsByNameIgnoreCaseAndIsDeletedFalseAndIdNot(accountWorkflowDTO.getName(), accountWorkflowDTO.getId())).thenReturn(Boolean.FALSE);

        //then
        Account actualAccount = accountService.updateAccount(accountWorkflowDTO);
        TestCommonMethods.cleanUp();
        assertEquals(account.getId(), actualAccount.getId());
    }

    @Test
    void testGetAccountById() {
        //given
        Account account = TestDataProvider.getAccount();
        account.setId(TestConstants.ONE);

        //when
        when(accountRepository.findByIdAndIsActiveAndIsDeleted(TestConstants.ONE, Boolean.TRUE, Boolean.FALSE))
                .thenReturn(account);

        //then
        Account actualAccount = accountService.getAccountById(TestConstants.ONE);
        assertEquals(account.getId(), actualAccount.getId());
    }

    @Test
    void testGetAccountDetails() {
        //given
        List<User> users = List.of(TestDataProvider.getUser());
        AccountDetailsDTO accountDetailsDTO = TestDataProvider.getAccountDetailsDTO();
        List<AccountWorkflow> accountWorkflows = TestDataProvider.getAccountWorkFlows();
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        CountryDTO countryDTO = TestDataProvider.getCountryDto();
        accountDetailsDTO.setCountryCode(countryDTO.getCountryCode());
        accountDetailsDTO.setCountry(countryDTO);
        Account account = TestDataProvider.getAccount();
        account.setTenantId(TestConstants.ONE);
        account.setCountry(modelMapper.map(countryDTO, Country.class));
        account.setClinicalWorkflows(accountWorkflows);
        account.setCustomizedWorkflows(accountWorkflows);
        accountDetailsDTO.setClinicalWorkflow(account.getClinicalWorkflows());
        accountDetailsDTO.setCustomizedWorkflow(account.getCustomizedWorkflows());
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(accountRepository.findByIdAndTenantIdAndIsActiveAndIsDeleted(commonRequestDTO.getId(),
                commonRequestDTO.getTenantId(), true, false)).thenReturn(account);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(account, AccountDetailsDTO.class)).thenReturn(new AccountDetailsDTO());
        when(userApiInterface.getUsersByTenantIds(TestConstants.TEST_TOKEN, TestConstants.ONE,
                List.of(account.getTenantId()))).thenReturn(List.of(new UserOrganizationDTO()));
        when(modelMapper.map(users, new TypeToken<List<UserOrganizationDTO>>() {
        }.getType()))
                .thenReturn(List.of(countryDTO));
        when(modelMapper.map(account.getCountry(), CountryDTO.class)).thenReturn(countryDTO);

        //then
        AccountDetailsDTO actualAccountDetailsDTO = accountService.getAccountDetails(commonRequestDTO);
        TestCommonMethods.cleanUp();
        assertEquals(accountDetailsDTO.getCustomizedWorkflow(), actualAccountDetailsDTO.getCustomizedWorkflow());
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = {"", "a"})
    void testGetDeactivatedAccounts(String searchTerm) {
        //given
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto(searchTerm, Constants.ZERO, TestConstants.TEN);
        searchRequestDTO.setTenantId(TestConstants.ONE);
        searchRequestDTO.setIsPaginated(Boolean.TRUE);
        ResponseListDTO responseListDTO = TestDataProvider.getResponseListDTO();
        String formattedSearchTerm = searchRequestDTO.getSearchTerm().strip();
        Page<Account> accounts = new PageImpl<>(TestDataProvider.getAccounts());
        responseListDTO.setData(modelMapper.map(accounts.stream().toList(), new TypeToken<List<AccountDTO>>() {
        }.getType()));
        responseListDTO.setTotalCount(accounts.getTotalElements());
        Pageable pageable = PageRequest.of(Constants.ZERO, TestConstants.TEN, (Constants.BOOLEAN_FALSE ?
                Sort.by(Constants.UPDATED_AT).ascending() : Sort.by(Constants.UPDATED_AT).descending()));
        TestCommonMethods.init();
        Organization organization = TestDataProvider.getOrganization();
        ResponseEntity<Organization> expectedOrganization = new ResponseEntity<>(organization, HttpStatus.OK);

        //when
        TestCommonMethods.getStaticMock();
        TestCommonMethods.getStaticMockValidation(searchTerm);
        when(userApiInterface.getOrganizationById(TestConstants.TEST_TOKEN, TestConstants.ONE, TestConstants.ONE))
                .thenReturn(expectedOrganization);
        when(accountRepository.getDeactivatedAccounts(formattedSearchTerm, TestConstants.ONE, pageable))
                .thenReturn(accounts);

        //then
        ResponseListDTO actualResponseListDTO = accountService.getDeactivatedAccounts(searchRequestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(actualResponseListDTO);
        assertEquals(Constants.TWO, actualResponseListDTO.getTotalCount());
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @NullSource    // pass a null value
    @ValueSource(strings = {"", "a"})
    void testGetAccountList(String searchTerm) {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(searchTerm, Constants.ZERO,
                TestConstants.TEN);
        requestDTO.setTenantId(TestConstants.ONE);
        String formattedSearchTerm = "a".replaceAll(Constants.SEARCH_TERM, Constants.EMPTY);
        ResponseListDTO responseListDTO = TestDataProvider.getResponseListDTO();
        Organization organization = TestDataProvider.getOrganization();
        ResponseEntity<Organization> expectedOrganization = new ResponseEntity<>(organization, HttpStatus.OK);
        Page<Account> accounts = new PageImpl<>(TestDataProvider.getAccounts());
        Pageable pageable = PageRequest.of(Constants.ZERO, TestConstants.TEN);
        Map<Long, Long> sitesCount = Map.of(1L, 1L, 2L, 1L);
        Map<Long, Long> operatingUnitsCount =Map.of(1L, 1L, 2L, 1L);
        AccountListDTO accountListDTO = TestDataProvider.getAccountListDTO(TestConstants.ONE,
                TestConstants.ACCOUNT_NAME, Constants.THREE);
        accountListDTO.setOuCount(1L);
        accountListDTO.setSiteCount(1L);
        AccountListDTO secondAccountListDTO = TestDataProvider.getAccountListDTO(TestConstants.TWO,
                TestConstants.SECOND_ACCOUNT_NAME, TestConstants.FOUR);
        secondAccountListDTO.setSiteCount(1L);
        secondAccountListDTO.setOuCount(1L);
        List<AccountListDTO> accountListDTOs = new ArrayList<>();
        accountListDTOs.add(accountListDTO);
        accountListDTOs.add(secondAccountListDTO);
        List<Long> accountIds = List.of(TestConstants.ONE, TestConstants.TWO);

        //when
        TestCommonMethods.init();
        TestCommonMethods.getStaticMock();
        when(userApiInterface.getOrganizationById(TestConstants.TEST_TOKEN, TestConstants.ONE, TestConstants.ONE))
                .thenReturn(expectedOrganization);
        when(accountRepository.findAccountList(formattedSearchTerm, TestConstants.ONE, pageable)).thenReturn(accounts);
        when(accountRepository.findAccountList(Constants.EMPTY, TestConstants.ONE, pageable)).thenReturn(accounts);
        responseListDTO.setTotalCount(accounts.getTotalElements());
        when(siteService.getCountByAccountIds(accountIds)).thenReturn(sitesCount);
        when(operatingUnitService.getOperatingUnitCountByAccountIds(accountIds))
                .thenReturn(operatingUnitsCount);

        //then
        ResponseListDTO actualAccounts = accountService.getAccountList(requestDTO);
        TestCommonMethods.cleanUp();
        assertEquals(Constants.TWO, actualAccounts.getTotalCount());
    }

    @Test
    void testCreateAccountAdmin() {
        //given
        User user = TestDataProvider.getUser();
        UserDetailsDTO userDetailsDTO = TestDataProvider.getUserDetailsDTO();
        userDetailsDTO.setFirstName(TestConstants.USER_FIRST_NAME);
        userDetailsDTO.setTenantId(TestConstants.ONE);
        Role role = new Role();
        role.setName(Constants.ROLE_ACCOUNT_ADMIN);
        user.setRoles(Set.of(role));
        ResponseEntity<User> userResponseEntity = new ResponseEntity<>(user, HttpStatus.OK);

        //when
        TestCommonMethods.init();
        TestCommonMethods.getStaticMock();
        when(userApiInterface.addAdminUser(TestConstants.TEST_TOKEN, TestConstants.ONE, userDetailsDTO, Boolean.FALSE))
                .thenReturn(userResponseEntity);

        //then
        User actualUser = accountService.createAccountAdmin(userDetailsDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(actualUser);
        assertEquals(TestConstants.ONE, actualUser.getRoles().size());
    }

    @Test
    void testUpdateAccountAdmin() {
        //given
        User user = TestDataProvider.getUser();
        user.setId(TestConstants.ONE);
        user.setTenantId(TestConstants.ONE);
        ResponseEntity<User> userResponseEntity = new ResponseEntity<>(user, HttpStatus.OK);
        UserDTO userDTO = TestDataProvider.getUserDTO();

        //when
        TestCommonMethods.init();
        TestCommonMethods.getStaticMock();
        when(userApiInterface.updateAdminUser(TestConstants.TEST_TOKEN, TestConstants.ONE, userDTO))
                .thenReturn(userResponseEntity);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        //then
        User actualUser = accountService.updateAccountAdmin(user);
        TestCommonMethods.cleanUp();
        assertNotNull(actualUser);
        assertEquals(user.getId(), actualUser.getId());
        assertEquals(user.getUsername(), actualUser.getUsername());
    }

    @Test
    void testDeleteAccountAdmin() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        ResponseEntity<Boolean> booleanResponseEntity = new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);

        //when
        TestCommonMethods.init();
        TestCommonMethods.getStaticMock();
        when(userApiInterface.deleteAdminUser(TestConstants.TEST_TOKEN, TestConstants.ONE, commonRequestDTO))
                .thenReturn(booleanResponseEntity);

        //then
        Boolean isDeleted = accountService.deleteAccountAdmin(commonRequestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(isDeleted);
        assertTrue(isDeleted);
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @NullSource    // pass a null value
    @ValueSource(strings = {"", "a"})
    void testGetAccounts(String searchTerm) {
        //given
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto(searchTerm, Constants.ZERO,
                TestConstants.TEN);
        searchRequestDTO.setTenantId(TestConstants.ONE);
        String formattedSearchTerm = searchRequestDTO.getSearchTerm();
        Organization organization = TestDataProvider.getOrganization();
        ResponseEntity<Organization> expectedOrganization = new ResponseEntity<>(organization, HttpStatus.OK);
        Page<Account> pages = new PageImpl<>(TestDataProvider.getAccounts());
        List<AccountDTO> accountDTOs = pages.stream().map(page -> modelMapper.map(page, AccountDTO.class))
                .collect(Collectors.toList());
        Pageable pageable = PageRequest.of(Constants.ZERO, TestConstants.TEN, (Constants.BOOLEAN_FALSE ?
                Sort.by(Constants.UPDATED_AT)
                        .ascending() : Sort.by(Constants.UPDATED_AT).descending()));

        //when
        TestCommonMethods.init();
        TestCommonMethods.getStaticMock();
        TestCommonMethods.getStaticMockValidation(searchTerm);
        when(userApiInterface.getOrganizationById(TestConstants.TEST_TOKEN, TestConstants.ONE, TestConstants.ONE))
                .thenReturn(expectedOrganization);
        when(accountRepository.findAccountList(Constants.EMPTY, TestConstants.ONE, pageable))
                .thenReturn(pages);
        when(accountRepository.findAccountList(formattedSearchTerm, TestConstants.ONE, pageable))
                .thenReturn(pages);
        when(modelMapper.map(pages.stream().toList(), new TypeToken<List<AccountDTO>>() {
        }.getType()))
                .thenReturn(List.of(accountDTOs));

        //then
        ResponseListDTO actualAccounts = accountService.getAccounts(searchRequestDTO);
        TestCommonMethods.cleanUp();
        assertEquals(Constants.TWO, actualAccounts.getTotalCount());
        assertNotNull(actualAccounts);
        assertEquals(List.of(accountDTOs), actualAccounts.getData());
    }

    @Test
    void testGetAccountsInvalidSearch() {
        //given
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto("gfda)", Constants.ZERO,
                TestConstants.TEN);
        searchRequestDTO.setTenantId(TestConstants.ONE);

        //when
        TestCommonMethods.init();
        TestCommonMethods.getStaticMock();
        TestCommonMethods.getStaticMockValidationFalse(searchRequestDTO.getSearchTerm());

        //then
        ResponseListDTO actualAccounts = accountService.getAccounts(searchRequestDTO);
        TestCommonMethods.cleanUp();
        assertEquals(Constants.ZERO, actualAccounts.getTotalCount());
        assertNotNull(actualAccounts);
        assertNull(actualAccounts.getData());
    }

    @Test
    void testGetAccountCountByCountryIds() {
        //given
        List<Long> accountIds = List.of(TestConstants.ONE, TestConstants.TWO);
        List<Map<String, Object>> responses = List.of(Map.of(Constants.STRING_TWO, Constants.STRING_TWO));

        //when
        when(accountRepository.getAccountCountByCountryIds(accountIds)).thenReturn(responses);

        //then
        Map<Long, Long> actualResponse = accountService.getAccountCountByCountryIds(accountIds);
        assertFalse(actualResponse.isEmpty());
        assertNotNull(actualResponse);
        assertEquals(TestConstants.ONE, actualResponse.size());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testActivateOrDeactivateAccounts(boolean isActive) {
        //given
        List<Long> countryIds = List.of(TestConstants.ONE, TestConstants.TWO);
        List<Account> accounts = TestDataProvider.getAccounts();

        //when
        when(accountRepository.findAccountByCountryIdAndIsActive(countryIds, !isActive)).thenReturn(accounts);
        when(accountRepository.saveAll(accounts)).thenReturn(accounts);
        when(redisTemplate.delete(Constants.ORGANIZATION_REDIS_KEY)).thenReturn(Boolean.TRUE);

        //then
        List<Long> activatedOrDeactivatedAccounts = accountService.activateOrDeactivateAccounts(countryIds, isActive);
        assertNotNull(activatedOrDeactivatedAccounts);
        assertFalse(activatedOrDeactivatedAccounts.isEmpty());
        assertTrue(activatedOrDeactivatedAccounts.contains(Constants.THREE));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void toVerifyActivateOrDeactivateAccounts(boolean isActive) {
        //given
        List<Long> countryIds = List.of(TestConstants.ONE, TestConstants.TWO);
        List<Account> accounts = new ArrayList<>();

        //when
        when(accountRepository.findAccountByCountryIdAndIsActive(countryIds, !isActive)).thenReturn(accounts);
        when(accountRepository.saveAll(accounts)).thenReturn(accounts);

        //then
        List<Long> activatedOrDeactivatedAccounts = accountService.activateOrDeactivateAccounts(countryIds, isActive);
        assertNotNull(activatedOrDeactivatedAccounts);
        assertTrue(activatedOrDeactivatedAccounts.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testActivateOrDeactivateAccount(boolean isActive) {
        //given
        Account account = TestDataProvider.getAccount();
        account.setId(TestConstants.ONE);
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, 1);
        requestDTO.setIsActive(isActive);
        requestDTO.setStatus(Constants.ACTIVATE);
        requestDTO.setReason(Constants.ROLE_PROVIDER);

        //when
        TestCommonMethods.init();
        TestCommonMethods.getStaticMock();
        when(accountRepository.findByTenantIdAndIsDeletedFalseAndIsActive(requestDTO.getTenantId(),
                !requestDTO.getIsActive())).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(operatingUnitService.activateOrDeactivateOperatingUnits(null, account.getId(), account.isActive()))
                .thenReturn(List.of(TestConstants.ONE, TestConstants.TWO, Constants.THREE));
        when(siteService.activateOrDeactivateSites(null, account.getId(), null,
                account.isActive())).thenReturn(List.of(TestConstants.ONE, TestConstants.TWO, Constants.THREE));
        when(userApiInterface.activateOrDeactivateOrg(TestConstants.TEST_TOKEN, TestConstants.ONE,
                List.of(TestConstants.ONE, TestConstants.TWO, Constants.THREE),
                requestDTO.getIsActive())).thenReturn(Boolean.TRUE);
        doNothing().when(userApiInterface).activateOrDeactivateUser(TestConstants.TEST_TOKEN, TestConstants.ONE,
                List.of(TestConstants.ONE, TestConstants.TWO, Constants.THREE), requestDTO.getIsActive());

        //then
        Boolean isActivatedOrDeactivated = accountService.activateOrDeactivateAccount(requestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(isActivatedOrDeactivated);
        assertTrue(isActivatedOrDeactivated);
    }

    @ParameterizedTest
    @CsvSource({"activate, role provider", ","})
    void testActivateOrDeactivateAccount(String status, String reason) {
        //given
        Account account = TestDataProvider.getAccount();
        account.setId(TestConstants.ONE);
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, 1);
        requestDTO.setStatus(status);
        requestDTO.setReason(reason);

        //when
        TestCommonMethods.init();
        TestCommonMethods.getStaticMock();
        when(accountRepository.findByTenantIdAndIsDeletedFalseAndIsActive(requestDTO.getTenantId(),
                !requestDTO.getIsActive())).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(operatingUnitService.activateOrDeactivateOperatingUnits(null, account.getId(), account.isActive()))
                .thenReturn(List.of(TestConstants.ONE, TestConstants.TWO, Constants.THREE));
        when(siteService.activateOrDeactivateSites(null, account.getId(), null,
                account.isActive())).thenReturn(List.of(TestConstants.ONE, TestConstants.TWO, Constants.THREE));
        when(userApiInterface.activateOrDeactivateOrg(TestConstants.TEST_TOKEN, TestConstants.ONE,
                List.of(TestConstants.ONE, TestConstants.TWO, Constants.THREE),
                requestDTO.getIsActive())).thenReturn(Boolean.TRUE);
        doNothing().when(userApiInterface).activateOrDeactivateUser(TestConstants.TEST_TOKEN, TestConstants.ONE,
                List.of(TestConstants.ONE, TestConstants.TWO, Constants.THREE), requestDTO.getIsActive());

        //then
        Boolean isActivatedOrDeactivated = accountService.activateOrDeactivateAccount(requestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(isActivatedOrDeactivated);
        assertTrue(isActivatedOrDeactivated);
    }

    @Test
    void checkNull() {
        //given
        AccountWorkflowDTO accountWorkflowDTO = TestDataProvider.getAccountworkFlowDto();
        accountWorkflowDTO.setId(TestConstants.ONE);
        accountWorkflowDTO.setClinicalWorkflow(new ArrayList<>());
        AccountWorkflowDTO secondAccountWorkflowDTO = TestDataProvider.getAccountworkFlowDto();
        List<Long> clinicalWorkflowIds = new ArrayList<>();
        clinicalWorkflowIds.add(TestConstants.ONE);
        clinicalWorkflowIds.add(null);
        secondAccountWorkflowDTO.setId(TestConstants.ONE);
        secondAccountWorkflowDTO.setClinicalWorkflow(clinicalWorkflowIds);
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        commonRequestDTO.setId(null);
        commonRequestDTO.setTenantId(null);
        Account account = TestDataProvider.getAccount();

        //when
        when(accountRepository.findByIdAndIsDeleted(accountWorkflowDTO.getId(), Boolean.FALSE))
                .thenReturn(account);
        when(accountRepository.findByIdAndIsDeleted(secondAccountWorkflowDTO.getId(), Boolean.FALSE))
                .thenReturn(TestDataProvider.getAccount());

        //then
        assertThrows(BadRequestException.class, () -> accountService.createAccount(null));
        assertThrows(BadRequestException.class, () -> accountService.createAccount(accountWorkflowDTO));
        assertThrows(BadRequestException.class, () -> accountService.updateAccount(null));
        assertThrows(BadRequestException.class, () -> accountService.updateAccount(accountWorkflowDTO));
        assertThrows(BadRequestException.class, () -> accountService.updateAccount(secondAccountWorkflowDTO));
        assertThrows(DataNotAcceptableException.class, () -> accountService.getAccountDetails(commonRequestDTO));
        commonRequestDTO.setId(TestConstants.ONE);
        assertThrows(DataNotAcceptableException.class, () -> accountService.getAccountDetails(commonRequestDTO));
        commonRequestDTO.setId(null);
        commonRequestDTO.setTenantId(TestConstants.ONE);
        assertThrows(DataNotAcceptableException.class, () -> accountService.getAccountDetails(commonRequestDTO));
    }

    @Test
    void throwDataNotFoundException() {
        //given
        List<Long> countryIds = List.of(TestConstants.ONE, TestConstants.TWO);
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        List<Long> workflowIds = List.of(TestConstants.ONE, TestConstants.TWO);
        AccountWorkflowDTO accountWorkflowDTO = TestDataProvider.getAccountworkFlowDto();
        accountWorkflowDTO.setClinicalWorkflow(workflowIds);
        accountWorkflowDTO.setCustomizedWorkflow(workflowIds);
        accountWorkflowDTO.setId(TestConstants.ONE);
        Account account = TestDataProvider.getAccount();
        RequestDTO requestDTO = TestDataProvider.getRequestDto(true, 1);

        //when
        when(accountRepository.findByIdAndIsActiveAndIsDeleted(TestConstants.ONE, Boolean.TRUE, Boolean.FALSE)).thenReturn(null);
        when(accountRepository.findAccountByCountryIdAndIsActive(countryIds, Boolean.FALSE)).thenReturn(null);
        when(accountRepository.findByIdAndTenantIdAndIsActiveAndIsDeleted(commonRequestDTO.getId(),
                commonRequestDTO.getTenantId(), Boolean.TRUE, Boolean.FALSE)).thenReturn(null);
        when(accountRepository.findByTenantIdAndIsDeletedFalseAndIsActive(requestDTO.getTenantId(),
                !requestDTO.getIsActive())).thenReturn(null);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(accountWorkflowDTO, Account.class)).thenReturn(account);
        when(accountWorkflowService.getAccountWorkflowsByIds(workflowIds)).thenReturn(Collections.emptyList());
        when(accountRepository.findByIdAndIsDeleted(accountWorkflowDTO.getId(), Boolean.FALSE)).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> accountService.getAccountById(TestConstants.ONE));
        assertThrows(DataNotFoundException.class, () -> accountService.getAccountDetails(commonRequestDTO));
        assertThrows(DataNotFoundException.class, () -> accountService.activateOrDeactivateAccount(requestDTO));
        assertThrows(DataNotFoundException.class, () -> accountService.createAccount(accountWorkflowDTO));
        assertThrows(DataNotFoundException.class, () -> accountService.updateAccount(accountWorkflowDTO));
    }


    @Test
    void throwDataConflictException() {
        //given
        List<Long> workflowIds = List.of(TestConstants.ONE, TestConstants.TWO);
        AccountWorkflowDTO accountWorkflowDTO = TestDataProvider.getAccountworkFlowDto();
        accountWorkflowDTO.setId(TestConstants.ONE);
        accountWorkflowDTO.setClinicalWorkflow(workflowIds);
        accountWorkflowDTO.setCustomizedWorkflow(workflowIds);
        Account account = TestDataProvider.getAccount();

        //when
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(accountRepository.existsByNameIgnoreCaseAndIsDeletedFalse(accountWorkflowDTO.getName().strip()))
                .thenReturn(Boolean.TRUE);
        when(accountRepository.findByIdAndIsDeleted(accountWorkflowDTO.getId(), Boolean.FALSE)).thenReturn(account);
        when(accountRepository.existsByNameIgnoreCaseAndIsDeletedFalseAndIdNot(accountWorkflowDTO.getName(), accountWorkflowDTO.getId())).thenReturn(true);

        //then
        assertThrows(DataConflictException.class, () -> accountService.createAccount(accountWorkflowDTO));
        assertThrows(DataConflictException.class, () -> accountService.updateAccount(accountWorkflowDTO));
    }
}

