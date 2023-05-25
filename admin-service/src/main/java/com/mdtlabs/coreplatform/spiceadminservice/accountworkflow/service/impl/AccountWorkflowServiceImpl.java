package com.mdtlabs.coreplatform.spiceadminservice.accountworkflow.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountWorkflow;
import com.mdtlabs.coreplatform.common.repository.DataRepository;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.spiceadminservice.accountworkflow.repository.AccountWorkflowRepository;
import com.mdtlabs.coreplatform.spiceadminservice.accountworkflow.service.AccountWorkflowService;

/**
 * <p>
 * AccountWorkflowServiceImpl class implements various methods for managing account workflows,
 * including creating, updating, and removing workflows, as well as retrieving lists of
 * workflows and individual workflows by ID or reference ID.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
@Service
public class AccountWorkflowServiceImpl implements AccountWorkflowService {

    @Autowired
    private AccountWorkflowRepository accountWorkflowRepository;

    @Autowired
    private DataRepository dataRepository;


    /**
     * {@inheritDoc}
     */
    public AccountWorkflow createAccountWorkflow(AccountWorkflow accountWorkflow) {
        if (accountWorkflow.getViewScreens().stream()
                .anyMatch(StringUtils::isEmpty)) {
            throw new BadRequestException(2202, accountWorkflow.getName());
        }
        if (accountWorkflowRepository.existsByNameIgnoreCaseAndCountryIdAndIsDeletedFalseAndTenantId(accountWorkflow.getName(),
                accountWorkflow.getCountryId(), accountWorkflow.getTenantId())) {
            throw new DataConflictException(2201, accountWorkflow.getName());
        }
        accountWorkflow
                .setWorkflow(accountWorkflow.getName().toLowerCase().replaceAll(Constants.SPACE, Constants.UNDER_SCORE));
        accountWorkflow.setModuleType(FieldConstants.CUSTOMIZED);
        return accountWorkflowRepository.save(accountWorkflow);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO getAccountWorkflows(SearchRequestDTO request) {
        if (Objects.isNull(request.getCountryId()) || 0 == request.getCountryId()) {
            throw new DataNotAcceptableException(1001);
        }
        Pageable pageable = Pagination.setPagination(request.getSkip(), request.getLimit(),
                List.of(Order.asc(Constants.MODULE_TYPE), Order.desc(Constants.UPDATED_AT)));
        String searchTerm = request.getSearchTerm();
        if (!CommonUtil.isValidSearchData(searchTerm, Constants.REGEX_SEARCH_PATTERN)) {
            return new ResponseListDTO(null, Constants.LONG_ZERO);
        }
        Page<AccountWorkflow> accountWorkflows = accountWorkflowRepository
                .getAccountWorkflowsWithPagination(request.getCountryId(), searchTerm, pageable);
        return new ResponseListDTO(accountWorkflows.stream().sorted(
                        Comparator.comparing(AccountWorkflow::getModuleType).thenComparing(BaseEntity::getUpdatedAt))
                .toList(), accountWorkflows.getTotalElements());
    }

    /**
     * {@inheritDoc}
     */
    public AccountWorkflow updateAccountWorkflow(AccountWorkflow accountWorkflow) {
        if (Objects.isNull(accountWorkflow)) {
            throw new BadRequestException(1003);
        }
        if (accountWorkflow.getViewScreens().stream()
                .anyMatch(StringUtils::isEmpty)) {
            throw new BadRequestException(2202, accountWorkflow.getName());
        }
        AccountWorkflow existingAccountWorkflow = accountWorkflowRepository
                .findByIdAndIsDeletedFalseAndTenantId(accountWorkflow.getId(), accountWorkflow.getTenantId());
        if (Objects.isNull(existingAccountWorkflow)) {
            throw new DataNotFoundException(2203);
        }
        existingAccountWorkflow.setViewScreens(accountWorkflow.getViewScreens());
        return accountWorkflowRepository.save(existingAccountWorkflow);
    }

    /**
     * {@inheritDoc}
     */
    public void removeAccountWorkflowByIdAndTenantId(long id, Long tenantId) {
        if (Constants.ZERO == id) {
            throw new DataNotAcceptableException(1003);
        }
        List<Account> accounts = dataRepository.getAccountByCustomizedWorkflowIds(id);
        if (!Objects.isNull(accounts) && !accounts.isEmpty()) {
            throw new BadRequestException(2204);
        }

        AccountWorkflow existingAccountWorkflow = accountWorkflowRepository
                .findByIdAndIsDeletedFalseAndTenantId(id, tenantId);
        if (Objects.isNull(existingAccountWorkflow)) {
            throw new DataNotFoundException(2203);
        }
        existingAccountWorkflow.setDeleted(Boolean.TRUE);
        accountWorkflowRepository.save(existingAccountWorkflow);
    }

    /**
     * {@inheritDoc}
     */
    public List<AccountWorkflow> getAllAccountWorkFlows() {
        return accountWorkflowRepository.findByModuleType(Constants.CLINICAL);
    }

    /**
     * {@inheritDoc}
     */
    public List<AccountWorkflow> getAccountWorkflowsByIds(List<Long> workflowIds) {
        return accountWorkflowRepository.findByIdInAndIsDeletedFalse(workflowIds);
    }
}
