package com.mdtlabs.coreplatform.common.repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.County;
import com.mdtlabs.coreplatform.common.model.entity.Subcounty;
import com.mdtlabs.coreplatform.common.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the user module action in database.
 * In query annotation (nativeQuery = true) the below query perform like SQL.
 * Otherwise its perform like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author Prabu created on Oct 03, 2022
 */
@Repository
public interface DataRepository extends JpaRepository<User, Long> {

    String GET_COUNTY_BY_ID = "select county from County as county where county.id=:id AND county.isDeleted=false";
    String GET_SUB_COUNTY_BY_ID = "select subCounty from Subcounty as subCounty where subCounty.id=:id AND subCounty.isDeleted=false";
    String GET_COUNTRY_BY_ID_AND_IS_DELETED = "select country from Country as country where country.id =:id and country.isDeleted = false";
    String GET_ACCOUNT_BY_ID = "select account from Account as account where account.id= :id and account.isDeleted=false";
    String GET_BY_CUSTOMIZATION_ID = "SELECT account FROM Account AS account JOIN "
            + "account.customizedWorkflows as workflow where workflow.id=:id and account.isDeleted=false";

    /**
     * <p>
     * Used to get the County object its Id and isDeleted.
     * </p>
     *
     * @param countyId  county ID
     * @return {@link County} entity
     */
    @Query(value = GET_COUNTY_BY_ID)
    County getCountyById(@Param(FieldConstants.ID) Long countyId);

    /**
     * <p>
     * Used to get the SubCounty object its Id and isDeleted.
     * </p>
     *
     * @param subCountyId  subCounty ID
     * @return {@link SubCounty} entity
     */
    @Query(value = GET_SUB_COUNTY_BY_ID)
    Subcounty getSubCountyById(@Param(FieldConstants.ID) Long subCountyId);

    /**
     * <p>
     * Used to get the country object by id and isDeleted.
     * </p>
     *
     * @return {@link Country}  country object
     */
    @Query(value = GET_COUNTRY_BY_ID_AND_IS_DELETED)
    Country getCountryByIdAndIsDeleted(@Param(FieldConstants.ID) Long id);

    /**
     * <p>
     * Used to get the account object its Id and isDeleted.
     * </p>
     *
     * @param accountId account ID
     * @return {@link Account} entity
     */
    @Query(value = GET_ACCOUNT_BY_ID)
    Account getAccountById(@Param(FieldConstants.ID) Long accountId);

    /**
     * <p>
     * Finds account with give customized workflow ID
     * </p>
     *
     * @param id  customized workflow ID
     * @return {@link List}  List of account entity
     */
    @Query(value = GET_BY_CUSTOMIZATION_ID)
    List<Account> getAccountByCustomizedWorkflowIds(@Param(Constants.ID) long id);

}
