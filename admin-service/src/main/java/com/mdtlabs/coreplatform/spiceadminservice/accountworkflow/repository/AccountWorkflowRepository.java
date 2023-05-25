package com.mdtlabs.coreplatform.spiceadminservice.accountworkflow.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.AccountWorkflow;

/**
 * <p>
 * AccountWorkflowRepository is a Java interface that extends the JpaRepository interface,
 * which is part of the Spring Data JPA framework. It defines methods for performing CRUD
 * operations on the AccountWorkflow entity.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
@Repository
public interface AccountWorkflowRepository extends JpaRepository<AccountWorkflow, Long> {
    String GET_ACCOUNT_WORKFLOWS = "SELECT accountWorkflow FROM AccountWorkflow"
            + " AS accountWorkflow WHERE (accountWorkflow.countryId = :countryId OR accountWorkflow.countryId IS NULL)"
            + " AND accountWorkflow.isDeleted=false AND accountWorkflow.isActive=true "
            + " AND (:searchTerm IS NULL OR lower(accountWorkflow.name) "
            + " LIKE CONCAT('%',lower(:searchTerm),'%') )";

    /**
     * <p>
     * This method is used to check if a record exists in a database table based on the name, country ID, tenant
     * ID, and deletion status.
     * </p>
     *
     * @param name      {@link String} The name of the account workflow to be checked is given
     * @param countryId {@link Long} The ID of the country for which the account workflows
     *                  are being searched is given
     * @param tenantId  {@link Long} The tenant ID for which the account workflows are being searched is given
     * @return A boolean value is returned by checking if an account workflow with the
     * given name exists in the database, ignoring case sensitivity
     */
    boolean existsByNameIgnoreCaseAndCountryIdAndIsDeletedFalseAndTenantId(String name, long countryId, Long tenantId);

    /**
     * <p>
     * This method is used to retrieve a page of account workflows based on a country ID and
     * search term.
     * </p>
     *
     * @param countryId  {@link Long} The ID of the country for which the account workflows
     *                   are being searched is given
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to filter the
     *                   results is given
     * @param pageable   {@link Pageable} The pagination information that contains information such as the page number,
     *                   page size, sorting criteria, and more is given
     * @return {@link Page<AccountWorkflow>}  A Page of account workflows that match the search criteria with applied
     * pagination is retrieved and returned from the database
     */
    @Query(value = GET_ACCOUNT_WORKFLOWS)
    Page<AccountWorkflow> getAccountWorkflowsWithPagination(@Param("countryId") long countryId,
                                                            @Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * <p>
     * This method is used to retrieve a list of account workflows by their IDs, excluding
     * any that have been deleted.
     * </p>
     *
     * @param workflowIds {@link List} The list of ids for which the workflows need to be retrieved is given
     * @return {@link List} The list of non-deleted account workflows for the given list of IDs is retrieved
     * and returned from database
     */
    List<AccountWorkflow> findByIdInAndIsDeletedFalse(List<Long> workflowIds);

    /**
     * <p>
     * This method is used to retrieve a list of AccountWorkflow objects based on a specified module type.
     * </p>
     *
     * @param moduleType {@link String} The category that is used as a filter to retrieve
     *                   only the account workflows that belong to a
     *                   specific module type is given
     * @return {@link List} The list of account workflows for the given module type is retrieved
     * and returned from database
     */
    List<AccountWorkflow> findByModuleType(String moduleType);

    /**
     * <p>
     * This method is used to find an account workflow by its ID and tenant ID, and ensures
     * that it has not deleted.
     * </p>
     *
     * @param id       The ID for which the account workflow is being searched is given
     * @param tenantId {@link Long} The tenant ID for which the account workflows
     *                 are being searched is given
     * @return {@link AccountWorkflow} The non-deleted accountWorkflow that matches the
     * given ID and tenant ID is returned
     */
    AccountWorkflow findByIdAndIsDeletedFalseAndTenantId(long id, Long tenantId);
}
