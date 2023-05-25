package com.mdtlabs.coreplatform.userservice.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.AccountOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteUserDTO;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.User;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * OrganizationService interface is an interface for organization module that can be implemented
 * in any class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface OrganizationService {

    /**
     * <p>
     * This method is used to update the organization from the provided organization details.
     * </p>
     *
     * @param organization {@link Organization} The organization to be updated is given with details
     * @return {@link Organization} The updated organization is returned
     */
    Organization updateOrganization(Organization organization);

    /**
     * <p>
     * This method is used to retrieve the organization for the given id.
     * </p>
     *
     * @param organizationId The id of the organization for which the organization to be retrieved
     * @return {@link Organization} The organization of the given id is returned
     */
    Organization getOrganizationById(long organizationId);

    /**
     * <p>
     * This method is used to get a map of child organizations ids for a given tenant ID and form name.
     * </p>
     *
     * @param tenantId The tenant id of the organization is given
     * @param formName {@link String} The form name of the organization is given
     * @return {@link Map} A map of key and list of ids as value is returned
     */
    Map<String, List<Long>> getChildOrganizations(long tenantId, String formName);

    /**
     * <p>
     * This method is used to add the admin user using the details provided and sets their risk level.
     * </p>
     *
     * @param user      {@link User} The user who need to be updated is given
     * @param isRedRisk The redRisk status is given
     * @return {@link User} The added admin user is returned
     */
    User addAdminUsers(User user, boolean isRedRisk);

    /**
     * <p>
     * This method is used to update the admin user using the tenant id from the provided user details.
     * </p>
     *
     * @param user {@link User} The user who need to be updated is given
     * @return {@link User} The user for the given user details is updated and returned
     */
    User updateAdminUsers(User user);

    /**
     * <p>
     * This method is used to delete the admin user using the requested details.
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The data of the user to be deleted is given
     * @return {@link Boolean} Returns a Boolean value indicating success or failure
     */
    Boolean deleteAdminUsers(CommonRequestDTO requestDto);

    /**
     * <p>
     * This method is used to validate whether the user belongs to a parent organization or not.
     * </p>
     *
     * @param parentOrganizationId {@link Long} The ID of the parent organization that needs to be validated is given
     * @param user                 {@link User} The user to be validated with the parent organization id is given
     */
    void validateParentOrganization(Long parentOrganizationId, User user);

    /**
     * <p>
     * This method is used to activate or deactivate an organization of the given list of tenant ids.
     * </p>
     *
     * @param tenantIds {@link List<Long>} The list of tenantIds that belongs to the organizations which to
     *                  be activated or deactivated is given
     * @param isActive  {@link Boolean} Active status of the organization is given
     * @return {@link Boolean} Returns true if the organizations are activated or deactivated otherwise false
     */
    Boolean activateOrDeactivateOrganization(List<Long> tenantIds, boolean isActive);

    /**
     * <p>
     * This method is used to create a Country for the provided country details.
     * </p>
     *
     * @param countryOrganizationDto {@link CountryOrganizationDTO} The country details that
     *                               to be created is given
     */
    void createCountry(CountryOrganizationDTO countryOrganizationDto);

    /**
     * <p>
     * This method is used to create the operating unit for the provided operating unit details.
     * </p>
     *
     * @param operatingUnitDto {@link OperatingUnitOrganizationDTO} The operating unit details that
     *                         to be created is given
     */
    void createOU(OperatingUnitOrganizationDTO operatingUnitDto);

    /**
     * <p>
     * This method is used to create an account for the provided account details.
     * </p>
     *
     * @param accountDto {@link AccountOrganizationDTO} The Account details which
     *                   to be created is given
     */
    void createAccount(AccountOrganizationDTO accountDto);

    /**
     * <p>
     * This method is used to create a site using the provided site details.
     * </p>
     *
     * @param siteDto {@link SiteOrganizationDTO} The site details that to be created is given
     */
    void createSite(SiteOrganizationDTO siteDto);

    /**
     * <p>
     * This method is used to update a site user using the given site user details.
     * </p>
     *
     * @param user {@link SiteUserDTO} The site user to be updated is given with details
     * @return {@link User} The updated Site user is returned
     */
    User updateSiteUser(SiteUserDTO user);

    /**
     * <p>
     * This method is used to retrieve Organization based on a given form data ID and form name.
     * </p>
     *
     * @param formDataId The form data id of the organization that need to retrieve from the
     *                   database is given
     * @param formName   {@link String}  The form name of the organization need to retrieve from
     *                   the database is given
     * @return The organization for the given form name and id is retrieved
     */
    Organization getOrganizationByFormDataIdAndName(long formDataId, String formName);

    /**
     * <p>
     * This method is used to retrieve a list of organizations based on their IDs.
     * </p>
     *
     * @param organizationIds {@link List<Long>} The ids of the Organizations that need to be retrieved is given
     * @return {@link List<Organization>} A list of organizations identified by the provided list of organization
     * IDs is returned.
     */
    List<Organization> getOrganizationsByIds(List<Long> organizationIds);
}
