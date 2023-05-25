package com.mdtlabs.coreplatform.spiceadminservice.accountworkflow.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountWorkflow;
import com.mdtlabs.coreplatform.common.repository.DataRepository;
import com.mdtlabs.coreplatform.spiceadminservice.accountworkflow.repository.AccountWorkflowRepository;
import com.mdtlabs.coreplatform.spiceadminservice.accountworkflow.service.impl.AccountWorkflowServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for the Account workflow service implementation.
 * </p>
 *
 * @author Divya S created on Feb 10, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountWorkflowServiceTest {

    @InjectMocks
    AccountWorkflowServiceImpl accountWorkflowService;

    @Mock
    AccountWorkflowRepository accountWorkflowRepository;

    @Mock
    DataRepository dataRepository;

    @Test
    void testCreateAccountWorkflow() {
        AccountWorkflow accountWorkflow = TestDataProvider.getAccountWorkflow();
        accountWorkflow.setViewScreens(List.of(Constants.SCREENING));
        accountWorkflow.setCountryId(TestConstants.ONE);
        when(accountWorkflowRepository
                .existsByNameIgnoreCaseAndCountryIdAndIsDeletedFalseAndTenantId(accountWorkflow.getName(),
                        accountWorkflow.getCountryId(), accountWorkflow.getTenantId())).thenReturn(Boolean.FALSE);
        when(accountWorkflowRepository.save(accountWorkflow)).thenReturn(accountWorkflow);
        AccountWorkflow actualAccountWorkflow = accountWorkflowService.createAccountWorkflow(accountWorkflow);
        assertEquals(accountWorkflow.getName(), actualAccountWorkflow.getName());
    }

    @Test
    void testGetAccountsInvalidSearch() {
        //given
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto("a)", Constants.ZERO,
                TestConstants.TEN);
        searchRequestDTO.setTenantId(TestConstants.ONE);
        searchRequestDTO.setCountryId(TestConstants.ONE);

        //when
        TestCommonMethods.init();
        TestCommonMethods.getStaticMock();
        TestCommonMethods.getStaticMockValidationFalse(searchRequestDTO.getSearchTerm());

        //then
        ResponseListDTO actualAccountWorkflows = accountWorkflowService.getAccountWorkflows(searchRequestDTO);
        TestCommonMethods.cleanUp();
        assertEquals(Constants.ZERO, actualAccountWorkflows.getTotalCount());
        assertNotNull(actualAccountWorkflows);
        assertNull(actualAccountWorkflows.getData());
    }

    @Test
    void testUpdateAccountWorkflow() {
        //given
        AccountWorkflow accountWorkflow = TestDataProvider.getAccountWorkflow();
        accountWorkflow.setId(TestConstants.ONE);

        //when
        when(accountWorkflowRepository.findByIdAndIsDeletedFalseAndTenantId(accountWorkflow.getId(), accountWorkflow.getTenantId())).thenReturn(accountWorkflow);
        when(accountWorkflowRepository.save(accountWorkflow)).thenReturn(accountWorkflow);

        //then
        AccountWorkflow actualAccountWorkflow = accountWorkflowService.updateAccountWorkflow(accountWorkflow);
        assertEquals(accountWorkflow.getId(), actualAccountWorkflow.getId());
    }

    @Test
    void testRemoveAccountWorkflowById() {
        //given
        long id = TestConstants.ONE;
        AccountWorkflow accountWorkflow = TestDataProvider.getAccountWorkflow();
        List<Account> accounts = new ArrayList<>();

        //when
        when(dataRepository.getAccountByCustomizedWorkflowIds(id)).thenReturn(accounts);
        when(accountWorkflowRepository.findByIdAndIsDeletedFalseAndTenantId(id, accountWorkflow.getTenantId()))
                .thenReturn(accountWorkflow);
        when(accountWorkflowRepository.save(accountWorkflow)).thenReturn(accountWorkflow);

        //then
        accountWorkflowService.removeAccountWorkflowByIdAndTenantId(id, accountWorkflow.getTenantId());
        verify(dataRepository, atLeastOnce()).getAccountByCustomizedWorkflowIds(id);
        verify(accountWorkflowRepository, atLeastOnce())
                .findByIdAndIsDeletedFalseAndTenantId(id, accountWorkflow.getTenantId());
        verify(accountWorkflowRepository, atLeastOnce()).save(accountWorkflow);
    }

    @Test
    void testGetAllAccountWorkFlows() {
        //given
        List<AccountWorkflow> accountWorkflows = TestDataProvider.getAccountWorkFlows();

        //when
        when(accountWorkflowRepository.findByModuleType(Constants.CLINICAL)).thenReturn(accountWorkflows);

        //then
        List<AccountWorkflow> actualAccountWorkflows = accountWorkflowService.getAllAccountWorkFlows();
        assertNotNull(actualAccountWorkflows);
        assertEquals(accountWorkflows.size(), actualAccountWorkflows.size());
        assertEquals(TestConstants.ONE, actualAccountWorkflows.get(0).getId());
        assertEquals(TestConstants.TWO, actualAccountWorkflows.get(1).getId());
    }

    @Test
    void testGetAccountWorkFlowsByIds() {
        //given
        List<AccountWorkflow> accountWorkflows = TestDataProvider.getAccountWorkFlows();
        List<Long> workflowIds = List.of(TestConstants.ONE, TestConstants.TWO);

        //when
        when(accountWorkflowRepository.findByIdInAndIsDeletedFalse(workflowIds)).thenReturn(accountWorkflows);

        //then
        List<AccountWorkflow> actualAccountWorkflows = accountWorkflowService.getAccountWorkflowsByIds(workflowIds);
        assertEquals(accountWorkflows.size(), actualAccountWorkflows.size());
        assertEquals(TestConstants.ONE, actualAccountWorkflows.get(0).getId());
        assertEquals(TestConstants.TWO, actualAccountWorkflows.get(1).getId());
        assertNotNull(actualAccountWorkflows);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0})
    void throwDataNotAcceptableException(Long countryId) {
        //given
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto("", 0, 10);
        searchRequestDTO.setCountryId(countryId);

        //then
        assertThrows(DataNotAcceptableException.class, () -> accountWorkflowService.getAccountWorkflows(searchRequestDTO));
        assertThrows(DataNotAcceptableException.class, () -> accountWorkflowService.getAccountWorkflows(searchRequestDTO));
        assertThrows(DataNotAcceptableException.class, () -> accountWorkflowService.removeAccountWorkflowByIdAndTenantId(0, TestConstants.ZERO));
    }

    @Test
    void checkNull() {
        //given
        AccountWorkflow accountWorkflow = TestDataProvider.getAccountWorkflow();
        accountWorkflow.setViewScreens(List.of(""));
        List<Account> accounts = TestDataProvider.getAccounts();

        //when
        when(dataRepository.getAccountByCustomizedWorkflowIds(TestConstants.ONE)).thenReturn(accounts);

        //then
        assertThrows(BadRequestException.class, () -> accountWorkflowService.createAccountWorkflow(accountWorkflow));
        assertThrows(BadRequestException.class, () -> accountWorkflowService.updateAccountWorkflow(null));
        assertThrows(BadRequestException.class, () -> accountWorkflowService.createAccountWorkflow(accountWorkflow));
        assertThrows(BadRequestException.class, () -> accountWorkflowService
                .removeAccountWorkflowByIdAndTenantId(TestConstants.ONE, TestConstants.ZERO));
        assertThrows(BadRequestException.class, () -> accountWorkflowService.updateAccountWorkflow(accountWorkflow));
    }

    @Test
    void throwDataConflictException() {
        //given
        AccountWorkflow accountWorkflow = TestDataProvider.getAccountWorkflow();
        accountWorkflow.setCountryId(TestConstants.ONE);

        //when
        when(accountWorkflowRepository.existsByNameIgnoreCaseAndCountryIdAndIsDeletedFalseAndTenantId(accountWorkflow
                .getName(), accountWorkflow.getCountryId(), accountWorkflow.getTenantId())).thenReturn(Boolean.TRUE);

        //then
        assertThrows(DataConflictException.class, () -> accountWorkflowService.createAccountWorkflow(accountWorkflow));
    }

    @Test
    void throwDataNotFoundException() {
        //given
        AccountWorkflow accountWorkflow = TestDataProvider.getAccountWorkflow();
        accountWorkflow.setId(TestConstants.ONE);

        //when
        when(accountWorkflowRepository.findByIdAndIsDeletedFalseAndTenantId(Constants.ONE,
                accountWorkflow.getTenantId())).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> accountWorkflowService.updateAccountWorkflow(accountWorkflow));
        assertThrows(DataNotFoundException.class,
                () -> accountWorkflowService.removeAccountWorkflowByIdAndTenantId(1, TestConstants.ZERO));
    }
}
