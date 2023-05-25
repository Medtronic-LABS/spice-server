package com.mdtlabs.coreplatform.spiceadminservice.labtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResult;

/**
 * <p>
 * LabTestResultRepository defines a Spring Data JPA repository interface for the `LabTestResult` entity.
 * It extends the `JpaRepository` interface, which provides basic CRUD operations for the entity.
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface LabTestResultRepository extends JpaRepository<LabTestResult, Long> {

    /**
     * <p>
     * This method is used to get the lab test result based on the lab test ids.
     * </p>
     *
     * @param id        The ID for which the lab test result need to be retrieved is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the lab test result has been marked as deleted or not is given
     * @param tenantId  {@link Long} The tenant ID for which the lab test result is being searched is given
     * @return {@link LabTestResult} The lab test result of the given ID and tenant ID is retrieved from the database
     */
    LabTestResult findByIdAndIsDeletedAndTenantId(long id, boolean isDeleted, Long tenantId);
}
