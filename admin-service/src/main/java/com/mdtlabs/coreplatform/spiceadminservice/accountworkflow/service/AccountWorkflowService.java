package com.mdtlabs.coreplatform.spiceadminservice.accountworkflow.service;

import java.util.List;

import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountWorkflow;

/**
 * <p>
 * AccountWorkflowService is a Java interface for a service that maintains CRUD operations for the
 * AccountWorkflow entity.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
public interface AccountWorkflowService {

    /**
     * <p>
     * This method is used to create an account workflow using the provided account workflow details.
     * </p>
     *
     * @param accountWorkflow {@link AccountWorkflow} The account workflow need to be created is given
     * @return {@link AccountWorkflow} The account workflow is created for given account workflow details and returned
     */
    AccountWorkflow createAccountWorkflow(AccountWorkflow accountWorkflow);

    /**
     * <p>
     * This method is used to get the account workflow details based on country id
     * and name.
     * </p>
     *
     * @param searchRequestDto {@link SearchRequestDTO} The search request contains necessary information
     *                         to get the list of account workflow details is given
     * @return {@link ResponseListDTO} The list of account workflows based on the specified search criteria
     * is returned
     */
    ResponseListDTO getAccountWorkflows(SearchRequestDTO searchRequestDto);

    /**
     * <p>
     * This method is used to update an account workflow using the provided account workflow details.
     * </p>
     *
     * @param accountWorkflow {@link AccountWorkflow} The account workflow need to be updated is given
     * @return {@link AccountWorkflow} The account workflow is updated for given account workflow details and returned
     */
    AccountWorkflow updateAccountWorkflow(AccountWorkflow accountWorkflow);

    /**
     * <p>
     * This method is used to soft delete an account workflow by ID and tenant ID.
     * </p>
     *
     * @param id       The ID of the account workflow that need to be deleted is given
     * @param tenantId {@link Long} The tenant ID of the account workflow that need to be deleted is given
     */
    void removeAccountWorkflowByIdAndTenantId(long id, Long tenantId);

    /**
     * <p>
     * This method is used to retrieve a list of account workflows.
     * </p>
     *
     * @return {@link List} A list of accountWorkflows is being returned
     */
    List<AccountWorkflow> getAllAccountWorkFlows();

    /**
     * <p>
     * This method is used to retrieve a list of account workflows by given list of IDs.
     * </p>
     *
     * @param workflowIds {@link List} The list of ids for which the workflows need to be retrieved is given
     * @return {@link List} The list of account workflows for the given list of IDs is retrieved returned
     */
    List<AccountWorkflow> getAccountWorkflowsByIds(List<Long> workflowIds);
}
