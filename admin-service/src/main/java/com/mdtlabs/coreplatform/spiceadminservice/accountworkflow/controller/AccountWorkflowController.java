package com.mdtlabs.coreplatform.spiceadminservice.accountworkflow.controller;

import com.mdtlabs.coreplatform.common.annotations.UserTenantValidation;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountDetailsWorkflowDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountWorkflow;
import com.mdtlabs.coreplatform.spiceadminservice.accountworkflow.service.AccountWorkflowService;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * Account workflow controller class defines REST API endpoints for creating, retrieving,
 * updating, and deleting account workflows, as well as retrieving a
 * list of all account workflows.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
@RestController
@RequestMapping("/clinical-workflow")
public class AccountWorkflowController {

    @Autowired
    private AccountWorkflowService accountWorkflowService;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>
     * This method is used to create an account workflow using the provided account workflow details.
     * </p>
     *
     * @param accountWorkflowDto {@link AccountDetailsWorkflowDTO} The account workflow need to be created
     *                           is given
     * @return {@link SuccessResponse<AccountWorkflow>} Returns a success message and status with
     * the created account workflow
     */
    @UserTenantValidation
    @PostMapping("/create")
    public SuccessResponse<AccountWorkflow> addAccountWorkflow(@Valid @RequestBody AccountDetailsWorkflowDTO accountWorkflowDto) {
        return new SuccessResponse<>(SuccessCode.ACCOUNT_WORKFLOW_SAVE, HttpStatus.CREATED,
                accountWorkflowService.createAccountWorkflow(modelMapper.map(accountWorkflowDto, AccountWorkflow.class)).getName());
    }

    /**
     * <p>
     * This method is used to retrieve account workflow based on a search request.
     * </p>
     *
     * @param searchRequestDto {@link SearchRequestDTO} The search request contains necessary information
     *                         to get the list of account workflow details is given
     * @return {@link SuccessResponse<AccountWorkflow>} Returns a success message and status with the retrieved
     * list of account workflow details
     */
    @UserTenantValidation
    @PostMapping("/list")
    public SuccessResponse<AccountWorkflow> getAccountWorkflows(@RequestBody SearchRequestDTO searchRequestDto) {
        ResponseListDTO accountWorkFlowDetails = accountWorkflowService.getAccountWorkflows(searchRequestDto);
        return new SuccessResponse(SuccessCode.GET_ACCOUNT_WORKFLOW, accountWorkFlowDetails.getData(),
                accountWorkFlowDetails.getTotalCount(), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to update an account workflow using the provided account workflow details.
     * </p>
     *
     * @param accountWorkflowDto {@link AccountDetailsWorkflowDTO} The account workflow need to be created
     *                           is given
     * @return {@link SuccessResponse<AccountWorkflow>} Returns a success message and status with
     * the updated account workflow
     */
    @UserTenantValidation
    @PutMapping("/update")
    public SuccessResponse<AccountWorkflow> updateAccountWorkflow(@RequestBody AccountDetailsWorkflowDTO accountWorkflowDto) {
        return new SuccessResponse<>(SuccessCode.ACCOUNT_WORKFLOW_UPDATE, HttpStatus.OK,
                accountWorkflowService.updateAccountWorkflow(modelMapper.map(accountWorkflowDto, AccountWorkflow.class)).getName());
    }

    /**
     * <p>
     * This method is used to delete an account workflow by ID and tenant ID.
     * </p>
     *
     * @param request The request that contains necessary information to delete the account workflow details is given
     * @return {@link SuccessResponse<AccountWorkflow>}  The success message with the status is returned if
     * the account workflow is deleted
     */
    @UserTenantValidation
    @PutMapping("/remove")
    public SuccessResponse<AccountWorkflow> deleteAccountWorkflowById(@RequestBody CommonRequestDTO request) {
        accountWorkflowService.removeAccountWorkflowByIdAndTenantId(request.getId(), request.getTenantId());
        return new SuccessResponse<>(SuccessCode.ACCOUNT_WORKFLOW_DELETE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to retrieve a list of account workflows.
     * </p>
     *
     * @return {@link List} A list of accountWorkflows is being returned
     */
    @PostMapping("/get-all-workflows")
    public List<AccountWorkflow> getAllAccountWorkFlows() {
        return accountWorkflowService.getAllAccountWorkFlows();
    }
}
