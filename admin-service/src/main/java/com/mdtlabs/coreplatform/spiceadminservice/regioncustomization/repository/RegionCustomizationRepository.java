package com.mdtlabs.coreplatform.spiceadminservice.regioncustomization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.RegionCustomization;

/**
 * <p>
 * RegionCustomizationRepository is a Java interface for a Spring Data JPA repository for
 * managing RegionCustomization entities. It extends the JpaRepository interface, which provides
 * basic CRUD operations for the entity.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
@Repository
public interface RegionCustomizationRepository extends JpaRepository<RegionCustomization, Long> {

    String GET_COUNTRY_CUSTOMIZATION_WITH_CONDITIONS = "SELECT regionCustomization "
            + " FROM RegionCustomization AS regionCustomization "
            + " WHERE (regionCustomization.countryId = :countryId) "
            + " AND (:category IS NULL OR regionCustomization.category = :category) "
            + " AND (:type IS NULL OR upper(regionCustomization.type) = upper(:type)) "
            + " AND regionCustomization.isDeleted= :isDeleted AND regionCustomization.tenantId = :tenantId"
            + " AND (:cultureId is null or regionCustomization.cultureId=:cultureId)";

    /**
     * <p>
     * To get a Region customization details with conditions.
     * </p>
     *
     * @param countryId The country id of the region customizations that need to be retrieved is given
     * @param category  {@link String} The category that is used as a filter to retrieve only the
     *                  region customizations that belong to a specific category is given
     * @param type      {@link String} The type that is used to filter the results of the query based
     *                  on the type of region customization
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the region customization has been marked as deleted or
     *                  not is given
     * @return {@link RegionCustomization} The list of region customizations for the given search criteria is returned
     */
    @Query(value = GET_COUNTRY_CUSTOMIZATION_WITH_CONDITIONS)
    List<RegionCustomization> findByCountryIdAndCategoryAndTypeAndTenantId(
            @Param("countryId") Long countryId, @Param("category") String category,
            @Param("type") String type, @Param("isDeleted") boolean isDeleted, @Param("tenantId") Long tenantId, @Param("cultureId") Long cultureId);

    /**
     * <p>
     * This method is used to retrieve region customization by ID and tenant ID.
     * </p>
     *
     * @param id       {@link Long} The ID of the region customization that need to be retrieved is given
     * @param tenantId {@link Long} The tenant ID of the region customization that need to be retrieved is given
     * @return {@link RegionCustomization} The region customization for given id and tenant id is retrieved
     * from the database
     */
    RegionCustomization findByIdAndTenantIdAndIsDeletedFalse(Long id, Long tenantId);

    /**
     * <p>
     * This method is used to retrieve list of region customizations by countryId and cultureId.
     * </p>
     *
     * @param countryId {@link Long} The country id of the region customizations that need to be retrieved is given
     * @param cultureId {@link Long} The culture id of the region customizations that need to be retrieved is given
     * @return The list of region customizations for the given country id and culture id is retrieved from the database
     */
    List<RegionCustomization> findByCountryIdAndCultureId(Long countryId, Long cultureId);
}
