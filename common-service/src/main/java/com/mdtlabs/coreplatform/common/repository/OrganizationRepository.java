package com.mdtlabs.coreplatform.common.repository;

import com.mdtlabs.coreplatform.common.model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This Repository interface maintains connection between Organization entity and
 * database.
 * </p>
 *
 * @author Karthick M created on feb 09, 2023
 */
@Repository(value = "organizationRepo")
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    /**
     * <p>
     * Gets the organization based on is active and is deleted.
     * </p>
     *
     * @return {@link List<Organization>}  list of entities
     */
    List<Organization> findByIsDeletedFalseAndIsActiveTrue();

}
