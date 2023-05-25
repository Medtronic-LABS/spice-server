package com.mdtlabs.coreplatform.authservice.service.impl;

import com.mdtlabs.coreplatform.authservice.repository.OrganizationRepository;
import com.mdtlabs.coreplatform.authservice.service.OrganizationService;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * This service class contain all the business logic for organization module and
 * perform all the organization operation here.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 03, 2023
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    /**
     * {@inheritDoc}
     */
    public Set<Long> getChildOrganizations(long tenantId, String formName) {
        Set<Long> childIds = new HashSet<>();
        List<Organization> childOrganizations;
        Set<Long> childOrgIds = new HashSet<>();

        if (formName.equalsIgnoreCase(Constants.COUNTRY)) {
            childOrganizations = organizationRepository.findByParentOrganizationId(tenantId);
            childOrgIds = childOrganizations.stream().map(BaseEntity::getId).collect(Collectors.toSet());
            childIds.addAll(childOrgIds);

        }
        if (formName.equalsIgnoreCase(Constants.COUNTRY) || formName.equalsIgnoreCase(Constants.ACCOUNT)) {
            if (formName.equalsIgnoreCase(Constants.ACCOUNT)) {
                childOrganizations = organizationRepository.findByParentOrganizationId(tenantId);
            } else {
                childOrganizations = organizationRepository.findByParentOrganizationIdIn(childOrgIds);
            }
            childOrgIds = childOrganizations.stream().map(BaseEntity::getId).collect(Collectors.toSet());
            childIds.addAll(childOrgIds);

        }
        if (formName.equalsIgnoreCase(Constants.COUNTRY) || formName.equalsIgnoreCase(Constants.ACCOUNT)
                || formName.equalsIgnoreCase(Constants.OPERATING_UNIT)) {

            if (formName.equalsIgnoreCase(Constants.OPERATING_UNIT)) {
                childOrganizations = organizationRepository.findByParentOrganizationId(tenantId);
            } else {
                childOrganizations = organizationRepository.findByParentOrganizationIdIn(childOrgIds);
            }

            childOrgIds = childOrganizations.stream().map(BaseEntity::getId).collect(Collectors.toSet());
            childIds.addAll(childOrgIds);

        }

        childIds.add(tenantId);

        return childIds;
    }

}
