package com.mdtlabs.coreplatform.spiceadminservice.account.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.Account;

/**
 * <p>
 * AccountRepository is a Java interface that extends the JpaRepository interface and defines methods for accessing and
 * manipulating data in a database table for the Account entity.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 7, 2023
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    String SELECT_BASE_QUERY = "SELECT account FROM Account AS account";

    String GET_DEACTIVATED_ACCOUNTS = SELECT_BASE_QUERY + " WHERE (:searchTerm IS null OR lower(account.name) LIKE CONCAT('%',lower(:searchTerm),'%')) "
            + " AND (:countryId IS NULL or account.country.id=:countryId) AND account.isActive=false AND "
            + "account.isDeleted = false";

    String GET_ACCOUNT_LIST = SELECT_BASE_QUERY + " WHERE account.country.id=:countryId "
            + " AND account.isDeleted=false and account.isActive=true "
            + " AND (:searchTerm is null or lower(account.name) LIKE CONCAT('%',lower(:searchTerm),'%'))";


    String GET_BY_COUNTRY_IDS = SELECT_BASE_QUERY + " WHERE account.country.id "
            + "IN (:countryId) AND account.isActive=:isActive AND account.isDeleted = false";

    String COUNT_BY_ACCOUNT_ID = "SELECT account.country.id as countryId, COUNT(account.id) AS count "
            + "FROM Account as account WHERE account.country.id in :list AND account.isDeleted=false AND "
            + "account.isActive=true GROUP BY account.country.id";

    /**
     * <p>
     * This method is used to get a account for the given ID and deletion status.
     * </p>
     *
     * @param id        The ID for which the account is being searched is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the account has been marked as deleted or not is given
     * @return {@link Account} The account is returned for the given search criteria like ID and isDeleted status
     */
    Account findByIdAndIsDeleted(long id, boolean isDeleted);

    /**
     * <p>
     * This method is used to get a account for the given ID, active and deletion status.
     * </p>
     *
     * @param id        The ID for which the account is being searched is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the account has been marked as deleted or not is given
     * @param isActive  The boolean value that is used to filter the results of the query based
     *                  on whether the account has been marked as active or not is given
     * @return {@link Account} The account is returned for the given search criteria like ID, isActive
     * and isDeleted status
     */
    Account findByIdAndIsActiveAndIsDeleted(long id, boolean isActive, boolean isDeleted);

    /**
     * <p>
     * This method is used to get the page of non-deleted inactive accounts for the given search term
     * with provided pagination.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to
     *                   filter the results is given
     * @param pageable   {@link Pageable} The pagination information that contains information such as the page number,
     *                   page size, sorting criteria, and more is given
     * @return {@link Page<Account>} A Page of non-deleted inactive accounts that match the search criteria with applied
     * pagination is retrieved and returned from the database
     */
    @Query(value = GET_DEACTIVATED_ACCOUNTS)
    Page<Account> getDeactivatedAccounts(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm,
                                         @Param(Constants.COUNTRY_ID) Long countryId, Pageable pageable);

    /**
     * <p>
     * This method is used to get the page of accounts for the given search term with provided pagination.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to
     *                   filter the results is given
     * @param countryId  {@link Long} The ID of the country associated with the accounts that
     *                   are being searched is given
     * @param pageable   {@link Pageable} The pagination information that contains information such as the page number,
     *                   page size, sorting criteria, and more is given
     * @return {@link Page<Account>} A Page of non-deleted and active accounts that match the search criteria with applied
     * pagination is retrieved and returned from the database
     */
    @Query(value = GET_ACCOUNT_LIST)
    Page<Account> findAccountList(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm,
                                  @Param(Constants.COUNTRY_ID) Long countryId, Pageable pageable);


    /**
     * <p>
     * This method is used to get list of account for the provided list of country IDs and active status.
     * </p>
     *
     * @param countryIds {@link List<Long>} The list of country IDs associated with the accounts that need to retrieve
     *                   is given
     * @param isActive   The boolean value that is used to filter the results of the query based
     *                   on whether the account has been marked as active or not is given
     * @return {@link List}  A list of non-deleted accounts that are either active or inactive for the given
     * list of country IDs is retrieved and returned from the database
     */
    @Query(value = GET_BY_COUNTRY_IDS)
    List<Account> findAccountByCountryIdAndIsActive(@Param(Constants.COUNTRY_ID) List<Long> countryIds,
                                                    @Param(Constants.IS_ACTIVE) boolean isActive);

    /**
     * <p>
     * This method is used to get an account by tenant ID, active status and deletion status.
     * </p>
     *
     * @param id        {@link Long} The ID of the account that need to be retrieved is given
     * @param tenantId  {@link Long} The tenant ID of the account that need to be retrieved is given
     * @param isActive  The boolean value that is used to filter the results of the query based
     *                  on whether the account has been marked as active or not is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the account has been marked as deleted or not is given
     * @return {@link Account} The account for the given tenant ID, active status and deletion status is
     * retrieved from the database and returned
     */
    Account findByIdAndTenantIdAndIsActiveAndIsDeleted(Long id, Long tenantId, boolean isActive,
                                                       boolean isDeleted);

    /**
     * <p>
     * This method is used to get list of account for the provided tenant ID and active status.
     * </p>
     *
     * @param tenantId {@link Long} The tenant ID of the account that need to be retrieved is given
     * @param isActive The boolean value that is used to filter the results of the query based
     *                 on whether the account has been marked as active or not is given
     * @return {@link Account} The non-deleted account which is either active or inactive for the given tenant ID is
     * retrieved from the database and returned
     */
    Account findByTenantIdAndIsDeletedFalseAndIsActive(Long tenantId, boolean isActive);

    /**
     * <p>
     * This method is used to get a list of map containing country IDs with corresponding count of accounts that
     * is searched using the given country IDs.
     * </p>
     *
     * @param list        {@link List} The list of country IDs associated with the accounts that
     *                    are being searched is given
     * @param booleanTrue The boolean value that is used to filter the results of the query based
     *                    on whether the account has been marked as active or not is given
     * @return {@link List<Map>} A list of map containing key as country IDs and value as count of accounts
     * for the corresponding country IDs provided is retrieved and returned from the database
     */
    @Query(value = COUNT_BY_ACCOUNT_ID)
    List<Map<String, Object>> getAccountCountByCountryIds(List<Long> list);

    /**
     * <p>
     * This method is used to check if a account exists in a database table based on the name which is not deleted.
     * </p>
     *
     * @param name {@link String} The name of the account to be checked is given
     * @return A boolean value is returned by checking if non-deleted account with the
     * given name exists in the database, ignoring case sensitivity
     */
    boolean existsByNameIgnoreCaseAndIsDeletedFalse(String name);

    /**
     * <p>
     * This method is used to check the availability of the account by its name and ID which
     * has not been deleted.
     * </p>
     *
     * @param name {@link String} The name of the account to be searched is given
     * @param id   {@link Long} The ID for which the account is being searched is given
     * @return True is returned if the account for given name and ID is already available
     * in the database otherwise false
     */
    boolean existsByNameIgnoreCaseAndIsDeletedFalseAndIdNot(String name, Long id);
}
