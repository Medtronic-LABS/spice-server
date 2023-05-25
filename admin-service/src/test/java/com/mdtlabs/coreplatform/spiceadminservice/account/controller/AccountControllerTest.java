package com.mdtlabs.coreplatform.spiceadminservice.account.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import com.mdtlabs.coreplatform.common.Constants;
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
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Account controller.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private AccountService accountService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(AccountController.class, "modelMapper", accountController);
    }

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @ParameterizedTest
    @MethodSource("getAccountWorkflowData")
    void validateAccountWorkflowDTO(String accountName, Long countryId, List<Long> clinicalWorkflows,
                                    int violationSize) {
        validate();
        AccountWorkflowDTO accountWorkflowDTO = new AccountWorkflowDTO();
        accountWorkflowDTO.setName(accountName);
        accountWorkflowDTO.setCountryId(countryId);
        accountWorkflowDTO.setClinicalWorkflow(clinicalWorkflows);
        Set<ConstraintViolation<AccountWorkflowDTO>> violations = validator.validate(accountWorkflowDTO);
        assertThat(violations).hasSize(violationSize);
    }

    @Test
    void testCreateAccount() {
        //given
        Account account = TestDataProvider.getAccount();
        AccountWorkflowDTO accountWorkflowDTO = TestDataProvider.getAccountworkFlowDto();

        //when
        when(accountService.createAccount(accountWorkflowDTO)).thenReturn(account);

        //then
        Account actualAccount = accountController.addAccount(accountWorkflowDTO);
        assertNotNull(actualAccount);
        assertEquals(account, actualAccount);
        assertEquals(TestConstants.ACCOUNT_NAME, actualAccount.getName());
    }

    @Test
    void testUpdateAccount() {
        //given
        Account account = TestDataProvider.getAccount();
        AccountWorkflowDTO accountWorkflowDTO = TestDataProvider.getAccountworkFlowDto();

        //when
        when(accountService.updateAccount(accountWorkflowDTO)).thenReturn(account);

        //then
        SuccessResponse<Account> actualAccount = accountController.updateAccount(accountWorkflowDTO);
        assertNotNull(actualAccount);
        assertEquals(HttpStatus.OK, actualAccount.getStatusCode());
    }

    @Test
    void testActivateAccountById() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(true, 1L);

        //when
        when(accountService.activateOrDeactivateAccount(requestDTO)).thenReturn(true);

        //then

        requestDTO.setTenantId(1L);
        SuccessResponse account = accountController.activateAccountById(requestDTO);
        assertNotNull(account);
        assertEquals(HttpStatus.OK, account.getStatusCode());
    }

    @Test
    void testDeactivateAccountById() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(false, 1L);

        //when
        when(accountService.activateOrDeactivateAccount(requestDTO)).thenReturn(true);

        //then
        requestDTO.setTenantId(1L);
        SuccessResponse account = accountController.deactivateAccountById(requestDTO);
        assertNotNull(account);
        assertEquals(HttpStatus.OK, account.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("provideSearchRequestDTOs")
    void testGetDeactivatedAccounts(String searchTerm, int skip, int limit, int httpCode) {
        //given
        ResponseListDTO responseListDTO = TestDataProvider.getResponseListDTO();
        responseListDTO.setData(TestDataProvider.getAccounts());
        responseListDTO.setTotalCount(TestConstants.TWO);
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto(searchTerm, skip, limit);

        //when
        when(accountService.getDeactivatedAccounts(searchRequestDTO)).thenReturn(responseListDTO);

        //then
        SuccessResponse successResponse = accountController.getDeactivatedAccounts(searchRequestDTO);
        assertNotNull(successResponse);
        assertEquals(httpCode, successResponse.getStatusCodeValue());
    }

    @ParameterizedTest
    @MethodSource("provideSearchRequestDTOs")
    void testEmptyAccounts(String searchTerm, int skip, int limit, int httpCode) {
        //given
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto(searchTerm, skip, limit);
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(searchTerm, skip, limit);
        ResponseListDTO responseListDTO = TestDataProvider.getResponseListDTO();
        responseListDTO.setTotalCount(TestConstants.TWO);
        responseListDTO.setData(Collections.emptyList());

        //when
        when(accountService.getDeactivatedAccounts(searchRequestDTO)).thenReturn(responseListDTO);
        when(accountService.getAccounts(searchRequestDTO)).thenReturn(responseListDTO);
        when(accountService.getAccountList(requestDTO)).thenReturn(responseListDTO);

        //then
        SuccessResponse<List<AccountDTO>> actualAccounts = accountController.getDeactivatedAccounts(searchRequestDTO);
        SuccessResponse<List<AccountDTO>> actualAccountDTOs = accountController.getAccounts(searchRequestDTO);
        SuccessResponse<List<AccountListDTO>> actualAccountsList = accountController.getAccountList(requestDTO);
        assertNotNull(actualAccountDTOs);
        assertNotNull(actualAccounts);
        assertNotNull(actualAccountsList);
        assertEquals(httpCode, actualAccounts.getStatusCodeValue());
        assertEquals(httpCode, actualAccountDTOs.getStatusCodeValue());
        assertEquals(httpCode, actualAccountsList.getStatusCodeValue());
    }

    @Test
    void testGetAccount() {
        //given
        Account account = TestDataProvider.getAccount();
        account.setId(TestConstants.ONE);

        //when
        when(accountService.getAccountById(TestConstants.ONE)).thenReturn(account);

        //then
        Account actualAccount = accountController.getAccount(TestConstants.ONE);
        assertNotNull(actualAccount);
        assertEquals(TestConstants.ONE, actualAccount.getId());
        assertEquals(account.getName(), actualAccount.getName());
    }

    @ParameterizedTest
    @MethodSource("provideSearchRequestDTOs")
    void testGetAccountList(String searchTerm, int skip, int limit, int httpCode) {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(searchTerm, skip, limit);
        ResponseListDTO responseListDTO = TestDataProvider.getResponseListDTO();
        responseListDTO.setData(TestDataProvider.getAccounts());
        responseListDTO.setTotalCount(TestConstants.TWO);

        //when
        when(accountService.getAccountList(requestDTO)).thenReturn(responseListDTO);

        //then
        SuccessResponse<List<AccountListDTO>> successResponse = accountController.getAccountList(requestDTO);
        assertNotNull(successResponse);
        assertEquals(httpCode, successResponse.getStatusCodeValue());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0L})
    void testGetAccountsWithTotalCountNull(Long totalCount) {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(Constants.EMPTY, Constants.ZERO,
                TestConstants.TEN);
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto(Constants.EMPTY, Constants.ZERO,
                TestConstants.TEN);
        ResponseListDTO responseListDTO = TestDataProvider.getResponseListDTO();
        responseListDTO.setData(TestDataProvider.getAccounts());
        responseListDTO.setTotalCount(totalCount);

        //when
        when(accountService.getAccountList(requestDTO)).thenReturn(responseListDTO);
        when(accountService.getAccounts(searchRequestDTO)).thenReturn(responseListDTO);
        when(accountService.getDeactivatedAccounts(searchRequestDTO)).thenReturn(responseListDTO);

        //then
        SuccessResponse<List<AccountListDTO>> accountList = accountController.getAccountList(requestDTO);
        SuccessResponse<List<AccountDTO>> accounts = accountController.getAccounts(searchRequestDTO);
        SuccessResponse<List<AccountDTO>> deactivatedAccounts = accountController.getDeactivatedAccounts(searchRequestDTO);

        assertNotNull(deactivatedAccounts);
        assertEquals(HttpStatus.OK, deactivatedAccounts.getStatusCode());
        assertNotNull(accountList);
        assertEquals(HttpStatus.OK, accountList.getStatusCode());
        assertNotNull(accounts);
        assertEquals(HttpStatus.OK, accounts.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("provideSearchRequestDTOs")
    void testGetAccounts(String searchTerm, int skip, int limit, int httpCode) {
        //given
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto(searchTerm, skip, limit);
        ResponseListDTO responseListDTO = TestDataProvider.getResponseListDTO();
        responseListDTO.setData(TestDataProvider.getAccounts());
        responseListDTO.setTotalCount(TestConstants.TWO);

        //when
        when(accountService.getAccounts(searchRequestDTO)).thenReturn(responseListDTO);

        //then
        SuccessResponse<List<AccountDTO>> successResponse = accountController.getAccounts(searchRequestDTO);
        assertNotNull(successResponse);
        assertEquals(httpCode, successResponse.getStatusCodeValue());
    }

    @Test
    void testAddAccountAdmin() {
        //given
        UserDetailsDTO userDTO = TestDataProvider.getUserDetailsDTO();
        User user = TestDataProvider.getUser();

        //when
        when(accountService.createAccountAdmin(userDTO)).thenReturn(user);

        //then
        SuccessResponse<User> createdUser = accountController.addAccountAdmin(userDTO);
        assertNotNull(createdUser);
        assertEquals(HttpStatus.CREATED, createdUser.getStatusCode());
    }

    @Test
    void testUpdateAccountAdmin() {
        //given
        UserDetailsDTO userDTO = TestDataProvider.getUserDetailsDTO();
        User user = TestDataProvider.getUser();

        //when
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(accountService.updateAccountAdmin(user)).thenReturn(user);

        //then
        SuccessResponse<User> updatedAccountAdmin = accountController.updateAccountAdmin(userDTO);
        assertNotNull(updatedAccountAdmin);
        assertEquals(HttpStatus.OK, updatedAccountAdmin.getStatusCode());
    }

    @Test
    void testDeleteAccountAdmin() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);

        //when
        when(accountService.deleteAccountAdmin(commonRequestDTO)).thenReturn(Boolean.TRUE);

        //then
        SuccessResponse<User> successResponse = accountController.deleteAccountAdmin(commonRequestDTO);
        assertNotNull(successResponse);
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testGetAccountDetails() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        AccountDetailsDTO accountDetailsDTO = TestDataProvider.getAccountDetailsDTO();

        //when
        when(accountService.getAccountDetails(commonRequestDTO)).thenReturn(accountDetailsDTO);

        //then
        SuccessResponse<AccountDetailsDTO> successResponse = accountController.getAccountDetails(commonRequestDTO);
        assertNotNull(successResponse);
        assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testClearApiPermissions() {
        //when
        doNothing().when(accountService).clearApiPermissions();

        //then
        accountController.clearApiPermissions();
        verify(accountService, atLeastOnce()).clearApiPermissions();
    }

    private void validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Stream<Arguments> getAccountWorkflowData() {
        return Stream.of(
                Arguments.of(TestConstants.ACCOUNT_NAME, TestConstants.ONE, List.of(TestConstants.ONE,
                        TestConstants.TWO), 0),
                Arguments.of(TestConstants.ACCOUNT_NAME, null, null, 2),
                Arguments.of(null, TestConstants.ONE, List.of(TestConstants.ONE, TestConstants.TWO),
                        Constants.ONE),
                Arguments.of(null, null, null, Constants.NUMBER_THREE),
                Arguments.of(Constants.EMPTY, TestConstants.ONE, null, Constants.TWO),
                Arguments.of(Constants.SPACE, TestConstants.ONE,
                        List.of(Constants.EMPTY), Constants.ONE)
        );
    }

    private static Stream<Arguments> provideSearchRequestDTOs() {
        return Stream.of(
                Arguments.of(Constants.EMPTY, 0, 10, 200),
                Arguments.of(Constants.EMPTY, 0, 1, 200),
                Arguments.of(null, 0, 10, 200)
        );
    }
}