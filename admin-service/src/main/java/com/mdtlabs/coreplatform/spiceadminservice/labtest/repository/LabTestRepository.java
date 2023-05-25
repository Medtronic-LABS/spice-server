package com.mdtlabs.coreplatform.spiceadminservice.labtest.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTest;

/**
 * <p>
 * LabTestRepository is a Java interface that extends the JpaRepository interface and defines methods for accessing and
 * manipulating data in the LabTest table of a database.
 * </p>
 *
 * @author Niraimathi S created on Feb 09, 2023
 */
@Repository
public interface LabTestRepository extends JpaRepository<LabTest, Long> {

    String SELECT_BASE_QUERY = "select labtest from LabTest as labtest";
    String GET_ALL_LAB_TESTS = SELECT_BASE_QUERY + " where(:countryId is null or labtest.countryId=:countryId) AND labtest.isDeleted=false"
            + " and labtest.tenantId=:tenantId and (:searchTerm is null or "
            + "lower(labtest.name) LIKE CONCAT('%',lower(:searchTerm),'%')) ORDER BY labtest.updatedAt desc ";

    String GET_LAB_TEST_BY_NAME = SELECT_BASE_QUERY + " where lower(labtest.name) LIKE CONCAT('%',lower(:searchTerm),'%')"
            + " AND labtest.countryId=:countryId AND labtest.isDeleted=false and "
            + "(:isActive is null or labtest.isActive=:isActive) and lower(labtest.name) NOT LIKE 'other' ";

    /**
     * <p>
     * Used to retrieve all the lab tests in a country.
     * </p>
     *
     * @param countryId The ID of the country associated with the lab test that is being searched is given
     * @param pageable  {@link Pageable} The pagination information that contains information such as the page number,
     *                  page size, sorting criteria, and more is given
     * @return {@link Page<LabTest>} A Page of lab tests that match the search criteria with applied
     * pagination is retrieved and returned from the database
     */
    @Query(value = GET_ALL_LAB_TESTS)
    Page<LabTest> getAllLabTests(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm,
                                 @Param(Constants.COUNTRY_ID) Long countryId, @Param(Constants.TENANT_PARAMETER_NAME) Long tenantId,
                                 Pageable pageable);

    /**
     * <p>
     * This method is used to get lab test based on the given search term and country ID and active status.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to
     *                   filter the results is given
     * @param countryId  The ID of the country associated with the lab test that is being searched is given
     * @param isActive   {@link Boolean} The boolean value that is used to filter the results of the query based
     *                   on whether the lab test has been marked as active or not is given
     * @return {@link List<LabTest>} The list of lab tests of the given search term, country ID and active
     * status is retrieved from the database
     */
    @Query(value = GET_LAB_TEST_BY_NAME)
    List<LabTest> searchLabTests(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm,
                                 @Param(Constants.COUNTRY_ID) Long countryId, @Param(Constants.IS_ACTIVE) Boolean isActive);


    LabTest findByCountryIdAndNameAndIsDeletedAndTenantId(long countryId, String name, boolean isDeleted, Long tenantId);

    /**
     * <p>
     * This method is used to get lab test by id with specified deletion status.
     * </p>
     *
     * @param labTestId {@link Long} The ID for which the lab test is being searched is given
     * @param isDeleted {@link Boolean} The boolean value that is used to filter the results of the query based
     *                  on whether the lab tests has been marked as deleted or not is given
     * @return {@link LabTest} The lab test of the given ID is retrieved from the database
     */
    LabTest findByIdAndIsDeleted(Long labTestId, Boolean isDeleted);

    /**
     * <p>
     * This method is used to get the lab test based on the ID and tenant ID with the specified deletion status.
     * </p>
     *
     * @param labTestId {@link Long} The ID for which the lab test need to be retrieved is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the lab test has been marked as deleted or not is given
     * @param tenantId  {@link Long} The tenant ID for which the lab test is being searched is given
     * @return {@link LabTest} The lab test of the given ID, tenant ID and deletion status
     * is retrieved from the database
     */
    LabTest findByIdAndIsDeletedAndTenantId(Long labTestId, Boolean isDeleted, Long tenantId);

    /**
     * <p>
     * This method is used to get the lab test based on name and country ID.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to
     *                   filter the results is given
     * @param countryId  The ID of the country associated with the lab test that
     *                   is being searched is given
     * @return {@link LabTest} The non-deleted lab test for the given search term (ignoring case)
     * and country ID is retrieved from the database
     */
    LabTest findByNameIgnoreCaseAndCountryId(String searchTerm, long countryId);

    /**
     * <p>
     * This method is used to get the lab test  based on the lab test ids.
     * </p>
     *
     * @param id        {@link Long} The ID for which the lab test need to be retrieved is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the lab test has been marked as deleted or not is given
     * @param tenantId  {@link Long} The tenant ID for which the lab test is being searched is given
     * @return {@link LabTest} The lab test of the given ID and tenant ID is retrieved from the database
     */
    LabTest findByIdAndIsDeletedAndTenantIdOrderByDisplayOrderAsc(Long id, Boolean isDeleted, Long tenantId);

    /**
     * <p>
     * This method is used to get the list of lab tests based on the set of IDs with the specified deletion status.
     * </p>
     *
     * @param ids       {@link Set<Long>} The set of ids for which the lab tests need to be retrieved is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the lab test has been marked as deleted or not is given
     * @return {@link List<LabTest>} The list of lab tests of the given set of IDs and deletion
     * status is retrieved from the database
     */
    List<LabTest> findByIdInAndIsDeleted(Set<Long> ids, boolean isDeleted);
}
