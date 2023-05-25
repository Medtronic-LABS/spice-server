package com.mdtlabs.coreplatform.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import com.mdtlabs.coreplatform.common.model.entity.User;

/**
 * <p>
 * This class provides the data for the test classes.
 * </p>
 *
 * @author Divya S created on March 03, 2023
 */
public class TestDataProvider {

    public static Organization getOrganization() {
        Organization organization = new Organization();
        organization.setId(TestConstants.ONE);
        organization.setFormName(TestConstants.FORM_NAME);
        organization.setFormDataId(TestConstants.ONE);
        organization.setTenantId(TestConstants.ONE);
        organization.setFormDataId(TestConstants.ONE);
        organization.setActive(Boolean.TRUE);
        organization.setParentOrganizationId(TestConstants.ONE);
        return organization;
    }

    public static List<Organization> getOrganizations() {
        List<Organization> organizations = new ArrayList<>();
        organizations.add(getOrganization());
        return organizations;
    }

    public static User getUser() {
        User user = new User();
        user.setId(TestConstants.ONE);
        user.setPassword(TestConstants.PASSWORD);
        user.setFirstName(TestConstants.FIRST_NAME);
        user.setLastName(TestConstants.LAST_NAME);
        user.setPhoneNumber(TestConstants.PHONE_NUMBER);
        user.setUsername(TestConstants.USER_NAME);
        user.setForgetPasswordCount(Constants.ONE);
        user.setForgetPasswordTime(new Date());
        user.setInvalidLoginAttempts(Constants.ONE);
        user.setTenantId(TestConstants.ONE);
        user.setCountryCode(TestConstants.COUNTRY_CODE);
        user.setRoles(Set.of(getRole()));
        user.setOrganizations(getSetOrganizations());
        user.setTimezone(new Timezone());
        return user;
    }

    public static Role getRole() {
        Role role = new Role();
        role.setId(TestConstants.ONE);
        role.setName(TestConstants.ROLE_NAME);
        role.setLevel(TestConstants.LEVEL);
        return role;
    }

    public static Set<Organization> getSetOrganizations() {
        Set<Organization> setOrganizations = new HashSet<>();
        setOrganizations.add(getOrganization());
        return setOrganizations;
    }
}
