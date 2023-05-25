package com.mdtlabs.coreplatform.spiceadminservice.accountcustomization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountCustomization;

/**
 * <p>
 * AccountCustomizationRepository is a Java interface that extends the JpaRepository interface and defines methods
 * for accessing and manipulating data in the AccountCustomization table in a database.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 08, 2023
 */
@Repository
public interface AccountCustomizationRepository extends JpaRepository<AccountCustomization, Long> {

    String GET_ACCOUNT_CUSTOMIZATION = "SELECT accountcustomization FROM "
            + " AccountCustomization as accountcustomization WHERE (accountcustomization.countryId = :countryId) "
            + " AND (:accountId IS NULL OR accountcustomization.accountId = :accountId) "
            + " AND (:category IS NULL OR accountcustomization.category = :category)"
            + " AND (:type IS NULL OR upper(accountcustomization.type) = upper(:type))"
            + " AND (:clinicalWorkflowId IS NULL OR clinical_workflow_id = :clinicalWorkflowId) "
            + " AND accountcustomization.isDeleted= :isDeleted AND accountcustomization.tenantId=:tenantId";

    /**
     * <p>
     * This method is used to get a account customization for the given ID and deletion status.
     * </p>
     *
     * @param id        {@link Long} The ID for which the account customizations
     *                  is being searched is given
     * @param isDeleted {@link Boolean} The boolean value that is used to filter the results of the query based
     *                  on whether the account customization has been marked as deleted or not is given
     * @return {@link AccountCustomization} The AccountCustomization is returned for the
     * given search criteria like ID and isDeleted status
     */
    AccountCustomization findByIdAndIsDeleted(Long id, Boolean isDeleted);

    /**
     * <p>
     * This method is used to find an account customization by its ID, isDeleted status, and tenant ID.
     * </p>
     *
     * @param id        {@link Long} The ID for which the account customizations
     *                  are being searched is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the account customization has been marked as deleted or not is given
     * @param tenantId  {@link Long} The tenant ID for which the account customizations
     *                  are being searched is given
     * @return {@link List} The AccountCustomization is returned for the given search criteria like ID,
     * isDeleted status and tenant ID
     */
    AccountCustomization findByIdAndIsDeletedAndTenantId(Long id, Boolean isDeleted, Long tenantId);

    /**
     * <p>
     * This method is used to retrieve a list of account customizations based on given parameters.
     * </p>
     *
     * @param countryId          {@link Long} The ID of the country for which the account customizations
     *                           are being searched is given
     * @param accountId          {@link Long} The ID of the account for which the account customizations
     *                           are being searched is given
     * @param category           {@link String} The category that is used as a filter to retrieve only the
     *                           account customizations that belong to a specific category is given
     * @param type               {@link String} The type that is used to filter the results of the query based
     *                           on the type of account customization
     * @param clinicalWorkflowId {@link Long} The ID of the clinical workflow for which the account customization
     *                           is being retrieved is given
     * @param isDeleted          The boolean value that is used to filter the results of the query based
     *                           on whether the account customization has been marked as deleted or not is given
     * @param tenantId           {@link Long} The tenant ID for which the account customizations
     *                           are being searched is given
     * @return {@link AccountCustomization} The list of account customizations for the given search criteria is returned
     */
    @Query(value = GET_ACCOUNT_CUSTOMIZATION)
    List<AccountCustomization> getAccountCustomization(@Param(Constants.COUNTRY_ID) Long countryId,
                                                       @Param(Constants.ACCOUNT_ID) Long accountId,
                                                       @Param(FieldConstants.CATEGORY) String category,
                                                       @Param(Constants.TYPE) String type,
                                                       @Param(Constants.CLINICAL_WORKFLOW_ID) Long clinicalWorkflowId,
                                                       @Param(Constants.IS_DELETED) boolean isDeleted,
                                                       @Param(Constants.TENANT_PARAMETER_NAME) Long tenantId);

    /**
     * <p>
     * This method is used to find non-deleted account customizations based on country ID, categories and screen types.
     * </p>
     *
     * @param countryId   {@link Long} The ID of the country for which the account customizations
     *                    are being searched is given
     * @param categories  {@link List} The list of categories for which the
     *                    account customizations are being searched is given
     * @param screenTypes {@link List} The list that represents the types of screens for which the
     *                    account customizations are being searched is given
     * @return {@link List} The list of AccountCustomizations those are not deleted is returned for the
     * given search criteria like country ID, categories and types
     */
    List<AccountCustomization> findByCountryIdAndCategoryInAndTypeInAndIsDeletedFalse(Long countryId, List<String> categories,
                                                                                      List<String> screenTypes);
}
