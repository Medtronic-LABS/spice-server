package com.mdtlabs.coreplatform.userservice.controller;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.annotations.UserTenantValidation;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteUserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.userservice.message.SuccessCode;
import com.mdtlabs.coreplatform.userservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.userservice.service.OrganizationService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * Organization Controller class that defines various REST API endpoints for managing organizations,
 * including updating, retrieving, creating, and deleting various entities such as countries,
 * accounts, operating units, and sites.
 * </p>
 *
 * @author VigneshKumar created on Jan 30, 2022
 */
@RestController
@RequestMapping("/organization")
public class OrganizationController {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private OrganizationService organizationService;

    /**
     * <p>
     * This method is used to update the organization using the id from the provided organization details.
     * </p>
     *
     * @param organizationDto {@link OrganizationDTO} The organization dto to be updated is given
     * @return {@link SuccessResponse<Organization>} The organization for the given organization details is
     * updated and returned with status
     */
    @PutMapping("/update")
    public SuccessResponse<Organization> updateOrganization(@RequestBody OrganizationDTO organizationDto) {
        Organization updatedOrganization = organizationService
                .updateOrganization(modelMapper.map(organizationDto, Organization.class));
        return new SuccessResponse<>(
                (null == updatedOrganization) ? SuccessCode.ORGANIZATION_UPDATE_ERROR : SuccessCode.ORGANIZATION_UPDATE,
                updatedOrganization, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to retrieve the organization for the given id.
     * </p>
     *
     * @param organizationId The id of the organization for which the organization to be retrieved is given
     * @return {@link ResponseEntity<Organization>} The organization for the given id is retrieved with status
     */
    @GetMapping("/details/{id}")
    public ResponseEntity<Organization> getOrganizationById(
            @PathVariable(value = Constants.ID) long organizationId) {
        return ResponseEntity.ok().body(organizationService.getOrganizationById(organizationId));
    }

    /**
     * <p>
     * This method is used to add the admin user using the details provided and sets their risk level.
     * </p>
     *
     * @param userDto   {@link UserDTO} The user who need to be updated is given
     * @param isRedRisk The redRisk status is given
     * @return {@link ResponseEntity<User>} The user for the given user details is updated and returned with status
     */
    @PostMapping("/add-admin-user")
    public ResponseEntity<User> addAdminUsers(@RequestBody UserDetailsDTO userDto, @RequestParam boolean isRedRisk) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return ResponseEntity.ok()
                .body(organizationService.addAdminUsers(modelMapper.map(userDto, User.class), isRedRisk));
    }

    /**
     * <p>
     * This method is used to update the admin user using the tenant id from the provided user details.
     * </p>
     *
     * @param userDto {@link UserDTO} The user who need to be updated is given
     * @return {@link ResponseEntity<User>} The user for the given user details is updated and returned with status
     */
    @PutMapping("/update-admin-user")
    public ResponseEntity<User> updateAdminUsers(@RequestBody UserDTO userDto) {
        return ResponseEntity.ok().body(organizationService.updateAdminUsers(modelMapper.map(userDto, User.class)));
    }

    /**
     * <p>
     * This method is used to delete the admin user.
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The data of the user to be deleted is given
     * @return {@link ResponseEntity<Boolean>} The admin user is deleted and returned as boolean value with status
     */
    @DeleteMapping("/delete-admin-user")
    public ResponseEntity<Boolean> deleteAdminUser(@RequestBody CommonRequestDTO requestDto) {
        return ResponseEntity.ok().body(organizationService.deleteAdminUsers(requestDto));
    }

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
    @PostMapping("/activate-deactivate")
    public Boolean activateOrDeactivateOrganization(@RequestBody List<Long> tenantIds, @RequestParam Boolean isActive) {
        return organizationService.activateOrDeactivateOrganization(tenantIds, isActive);

    }

    /**
     * <p>
     * This method is used to create a Country for the provided country details.
     * </p>
     *
     * @param countryOrganizationDTO {@link CountryOrganizationDTO} The country details that
     *                               to be created is given
     * @return {@link SuccessResponse<CountryOrganizationDTO>} The Country for the given details is created
     * and retrieved with status
     */
    @PostMapping("/create-country")
    public SuccessResponse<CountryOrganizationDTO> createCountry(
            @Valid @RequestBody CountryOrganizationDTO countryOrganizationDTO) {
        organizationService.createCountry(countryOrganizationDTO);
        return new SuccessResponse<>(SuccessCode.ORGANIZATION_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to create an account for the provided account details.
     * </p>
     *
     * @param accountOrganizationDto {@link AccountOrganizationDTO} The Account details which
     *                               to be created is given
     * @return {@link SuccessResponse<AccountOrganizationDTO>} The Account for the given details is created
     * and retrieved with status
     */
    @UserTenantValidation
    @PostMapping("/create-account")
    public SuccessResponse<AccountOrganizationDTO> createAccount(
            @Valid @RequestBody AccountOrganizationDTO accountOrganizationDto) {
        organizationService.createAccount(accountOrganizationDto);
        return new SuccessResponse<>(SuccessCode.ORGANIZATION_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to create the operating unit for the provided operating unit details.
     * </p>
     *
     * @param operatingUnitOrganizationDTO {@link OperatingUnitOrganizationDTO} The operating unit details that
     *                                     to be created is given
     * @return {@link SuccessResponse<OperatingUnitOrganizationDTO>} The Operating unit for the given details is created
     * and retrieved with status
     */
    @UserTenantValidation
    @PostMapping("/create-operating-unit")
    public SuccessResponse<OperatingUnitOrganizationDTO> createOperatingUnit(
            @Valid @RequestBody OperatingUnitOrganizationDTO operatingUnitOrganizationDTO) {
        organizationService.createOU(operatingUnitOrganizationDTO);
        return new SuccessResponse<>(SuccessCode.ORGANIZATION_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to create the site for the provided site details.
     * </p>
     *
     * @param siteDto {@link SiteOrganizationDTO} The site details that to be created is given
     * @return {@link SuccessResponse<SiteOrganizationDTO>} The site for the given details is created and
     * retrieved with status
     */
    @UserTenantValidation
    @PostMapping("/create-site")
    public SuccessResponse<SiteOrganizationDTO> createSite(@Valid @RequestBody SiteOrganizationDTO siteDto) {
        organizationService.createSite(siteDto);
        return new SuccessResponse<>(SuccessCode.ORGANIZATION_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to update the site user using the tenant id from the provided site user details.
     * </p>
     *
     * @param user {@link SiteUserDTO} The site user dto for whom to be updated is given
     * @return {@link ResponseEntity<User>} The user for the given site user details is updated and returned with status
     */
    @PutMapping("/update-site-user")
    public ResponseEntity<User> updateSiteUsers(@RequestBody SiteUserDTO user) {
        return ResponseEntity.ok().body(organizationService.updateSiteUser(user));
    }

    /**
     * <p>
     * This method is used to retrieve the organization for the given form name and organization id.
     * </p>
     *
     * @param organizationId ID of the organization for which the organization to be retrieved
     * @param formName       {@link String} Name of the form for which the organization to be retrieved
     * @return {@link ResponseEntity<Organization>} The organization for the given form name and id is
     * retrieved with status
     */
    @GetMapping("/details/{formDataId}/{formName}")
    public ResponseEntity<Organization> getOrganizationByFormDataIdAndName(
            @PathVariable(Constants.FORM_DATA_ID) long organizationId,
            @PathVariable(Constants.FORM_NAME) String formName) {
        return ResponseEntity.ok()
                .body(organizationService.getOrganizationByFormDataIdAndName(organizationId, formName));
    }
}
