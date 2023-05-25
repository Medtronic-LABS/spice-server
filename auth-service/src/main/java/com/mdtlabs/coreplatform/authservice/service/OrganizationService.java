package com.mdtlabs.coreplatform.authservice.service;

import java.util.Set;

/**
 * <p>
 * This an interface class for organization module you can implement this
 * class in any class.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 03, 2023
 */
public interface OrganizationService {

    /**
     * <p>
     * This method is used to get child organization IDs of an organization.
     * </p>
     *
     * @param tenantId tenantId of the organization  is given
     * @param formName organization form name is given
     * @return {@link Set<Long>} collection of child organization IDs is given
     */
    Set<Long> getChildOrganizations(long tenantId, String formName);

}
