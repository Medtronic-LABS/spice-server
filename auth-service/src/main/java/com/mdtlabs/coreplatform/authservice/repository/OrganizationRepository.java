package com.mdtlabs.coreplatform.authservice.repository;

import com.mdtlabs.coreplatform.common.model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the organization module action in
 * database. In query annotation (nativeQuery = true) the below query perform
 * like SQL. Otherwise its perform like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author Jeyaharini T A created on Feb 03, 2023
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    /**
     * <p>
     * This method is used to get list of organizations by its parent organization id's.
     * </P>
     *
     * @param tenantId child Organization IDs is given
     * @return {@link List<Organization>} list of organizations is given
     */
    List<Organization> findByParentOrganizationId(long tenantId);

    /**
     * <p>
     * This method is used to get list of organizations by its parent organization id's.
     * </P>
     *
     * @param childOrgIds {@link Set<Long>} child Organization IDs is given
     * @return {@link List<Organization>} list of organizations is given
     */
    List<Organization> findByParentOrganizationIdIn(Set<Long> childOrgIds);

}
