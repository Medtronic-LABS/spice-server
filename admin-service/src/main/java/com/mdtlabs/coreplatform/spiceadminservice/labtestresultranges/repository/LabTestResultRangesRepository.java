package com.mdtlabs.coreplatform.spiceadminservice.labtestresultranges.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResultRange;

/**
 * <p>
 * LabTestResultRangesRepository is a Java interface defining a repository for the LabTestResultRange entity.
 * It extends the JpaRepository interface, which provides basic CRUD operations for the entity.
 * The interface includes several methods for retrieving LabTestResultRange objects from the database
 * based on various criteria, such as ID, deletion status, and tenant ID.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 7, 2023
 */
@Repository
public interface LabTestResultRangesRepository extends JpaRepository<LabTestResultRange, Long> {

    String GET_LAB_TEST_RESULT_RANGES = " FROM LabTestResultRange"
            + " WHERE Id IN (:ids) AND isDeleted = :isDeleted AND tenantId = :tenantId";

    /**
     * <p>
     * This method is used to get lab test result range by id with specified deletion status.
     * </p>
     *
     * @param id        The ID for which the lab test result range is being searched is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the lab test result range has been marked as deleted or not is given
     * @return {@link LabTestResultRange} The lab test result range of the given ID is retrieved from the database
     */
    LabTestResultRange findByIdAndIsDeleted(long id, boolean isDeleted);

    /**
     * <p>
     * This method is used to get the list of lab test result ranges based on the list of IDs and
     * tenant ID with the specified deletion status.
     * </p>
     *
     * @param ids       {@link List<Long>} The list of ids for which the
     *                  lab test result ranges need to be retrieved is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the lab test result range has been marked as deleted or not is given
     * @param tenantId  {@link Long} The tenant ID for which the lab test result range
     *                  is being searched is given
     * @return {@link List<LabTestResultRange>} The list of lab test result ranges of the given list of IDs and tenant ID
     * is retrieved from the database
     */
    @Query(value = GET_LAB_TEST_RESULT_RANGES)
    List<LabTestResultRange> findByIdsAndIsDeletedAndTenantId(@Param("ids") List<Long> ids,
                                                              @Param("isDeleted") boolean isDeleted, @Param("tenantId") Long tenantId);

    /**
     * <p>
     * This method is used to  get the list of lab test result range based on the lab test result id.
     * </p>
     *
     * @param labTestResultId The lab test result ID for which the lab test result range is being searched is given
     * @param isDeleted       The boolean value that is used to filter the results of the query based
     *                        on whether the lab test result range has been marked as deleted or not is given
     * @return {@link List<LabTestResultRange>} The list of lab test result ranges of the given lab test result ID
     * is retrieved from the database
     */
    List<LabTestResultRange> findByLabTestResultIdAndIsDeletedOrderByDisplayOrderAsc(
            long labTestResultId, boolean isDeleted);

    /**
     * <p>
     * This method is used to get the list of lab test result range based on the lab test result ids.
     * </p>
     *
     * @param labTestResultIds {@link List<Long>} The list of lab test result ids for which the
     *                         lab test result ranges need to be retrieved is given
     * @param isDeleted        The boolean value that is used to filter the results of the query based
     *                         on whether the lab test result range has been marked as deleted or not is given
     * @return {@link List<LabTestResultRange>} The list of lab test result ranges of the given
     * list of lab test result IDs is retrieved from the database
     */
    List<LabTestResultRange> findByLabTestResultIdInAndIsDeletedOrderByDisplayOrderAsc(List<Long> labTestResultIds, boolean isDeleted);

    /**
     * <p>
     * This method is used to get the lab test result range based on the lab test result id and tenant id
     * </p>
     *
     * @param id       The ID for which the lab test result range is being searched is given
     * @param tenantId {@link Long} The tenant ID for which the lab test result range
     *                 is being searched is given
     * @return {@link Optional<LabTestResultRange>} An Optional object that may contain a LabTestResultRange
     * with the specified id and tenantId, or may be empty if no such object exists.
     */
    Optional<LabTestResultRange> findByIdAndTenantId(long id, Long tenantId);
}
