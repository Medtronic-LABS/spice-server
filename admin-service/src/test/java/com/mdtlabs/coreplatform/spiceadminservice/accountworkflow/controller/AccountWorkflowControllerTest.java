package com.mdtlabs.coreplatform.spiceadminservice.accountworkflow.controller;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountDetailsWorkflowDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountWorkflow;
import com.mdtlabs.coreplatform.spiceadminservice.accountworkflow.service.AccountWorkflowService;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Account workflow controller.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class AccountWorkflowControllerTest {

    @InjectMocks
    private AccountWorkflowController accountWorkflowController;

    @Mock
    private AccountWorkflowService accountWorkflowService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(AccountWorkflowController.class, "modelMapper", accountWorkflowController);
    }

    @Test
    void testAddAccountWorkflow() {
        //given
        AccountWorkflow accountWorkflow = TestDataProvider.getAccountWorkflow();
        AccountDetailsWorkflowDTO accountDetailsWorkflowDTO = TestDataProvider.getAccountDetailsWorkflowDTO();

        //when
        when(modelMapper.map(accountDetailsWorkflowDTO, AccountWorkflow.class)).thenReturn(accountWorkflow);
        when(accountWorkflowService.createAccountWorkflow(accountWorkflow)).thenReturn(accountWorkflow);

        //then
        SuccessResponse<AccountWorkflow> actualAccountWorkflow = accountWorkflowController
                .addAccountWorkflow(accountDetailsWorkflowDTO);
        assertNotNull(actualAccountWorkflow);
        assertEquals(HttpStatus.CREATED, actualAccountWorkflow.getStatusCode());
    }

    @Test
    void testUpdateAccountWorkflow() {
        //given
        AccountWorkflow accountWorkflow = TestDataProvider.getAccountWorkflow();
        AccountDetailsWorkflowDTO accountDetailsWorkflowDTO = TestDataProvider.getAccountDetailsWorkflowDTO();

        //when
        when(modelMapper.map(accountDetailsWorkflowDTO, AccountWorkflow.class)).thenReturn(accountWorkflow);
        when(accountWorkflowService.updateAccountWorkflow(accountWorkflow)).thenReturn(accountWorkflow);

        //then
        SuccessResponse<AccountWorkflow> actualAccountWorkflow = accountWorkflowController
                .updateAccountWorkflow(accountDetailsWorkflowDTO);
        assertNotNull(actualAccountWorkflow);
        assertEquals(HttpStatus.OK, actualAccountWorkflow.getStatusCode());
    }

    @Test
    void testRemoveAccountWorkflowById() {
        //when
        doNothing().when(accountWorkflowService).removeAccountWorkflowByIdAndTenantId(TestConstants.ONE, TestConstants.ONE);
        CommonRequestDTO request = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);

        //then
        SuccessResponse<AccountWorkflow> actualAccountWorkflow = accountWorkflowController
                .deleteAccountWorkflowById(request);
        assertNotNull(actualAccountWorkflow);
        assertEquals(HttpStatus.OK, actualAccountWorkflow.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("provideSearchRequestDTOs")
    void testGetAccountWorkflows(String searchTerm, int skip, int limit, HttpStatus httpStatus) {
        //given
        List<AccountWorkflow> accountWorkflows = TestDataProvider.getAccountWorkflows();
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto(searchTerm, skip, limit);
        ResponseListDTO workflows = new ResponseListDTO(accountWorkflows, 10l);

        //when
        when(accountWorkflowService.getAccountWorkflows(searchRequestDTO)).thenReturn(workflows);

        //then
        SuccessResponse<AccountWorkflow> actualAccountWorkflows = accountWorkflowController
                .getAccountWorkflows(searchRequestDTO);
        assertNotNull(actualAccountWorkflows);
        assertEquals(httpStatus, actualAccountWorkflows.getStatusCode());
    }

    @Test
    void testGetAllAccountWorkFlows() {
        //given
        List<AccountWorkflow> accountWorkflows = TestDataProvider.getAccountWorkFlows();

        //when
        when(accountWorkflowService.getAllAccountWorkFlows()).thenReturn(accountWorkflows);

        //then
        List<AccountWorkflow> actualAccountWorkflows = accountWorkflowController.getAllAccountWorkFlows();
        assertNotNull(actualAccountWorkflows);
        assertFalse(accountWorkflows.isEmpty());
        assertEquals(accountWorkflows.size(), actualAccountWorkflows.size());
        assertEquals(accountWorkflows.get(0), actualAccountWorkflows.get(0));
    }

    @ParameterizedTest
    @MethodSource("provideSearchRequestDTOs")
    void testEmptyAccountWorkflows(String searchTerm, int skip, int limit, HttpStatus httpStatus) {
        //given
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto(searchTerm, skip, limit);

        //when
        when(accountWorkflowService.getAccountWorkflows(searchRequestDTO)).thenReturn(new ResponseListDTO(null, Constants.LONG_ZERO));

        //then
        SuccessResponse<AccountWorkflow> actualAccountWorkflows = accountWorkflowController
                .getAccountWorkflows(searchRequestDTO);
        assertNotNull(actualAccountWorkflows);
        assertEquals(httpStatus, actualAccountWorkflows.getStatusCode());
    }

    private static Stream<Arguments> provideSearchRequestDTOs() {
        return Stream.of(
                Arguments.of(Constants.EMPTY, Constants.ZERO, TestConstants.TEN, HttpStatus.OK),
                Arguments.of(Constants.EMPTY, Constants.ZERO, Constants.ONE, HttpStatus.OK),
                Arguments.of(null, Constants.ZERO, TestConstants.TEN, HttpStatus.OK)
        );
    }
}