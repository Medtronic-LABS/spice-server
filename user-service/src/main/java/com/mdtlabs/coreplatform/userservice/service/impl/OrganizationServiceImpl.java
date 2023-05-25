package com.mdtlabs.coreplatform.userservice.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountWorkflowDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteUserDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.Culture;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.userservice.AdminApiInterface;
import com.mdtlabs.coreplatform.userservice.mapper.UserMapper;
import com.mdtlabs.coreplatform.userservice.repository.OrganizationRepository;
import com.mdtlabs.coreplatform.userservice.service.OrganizationService;
import com.mdtlabs.coreplatform.userservice.service.RoleService;
import com.mdtlabs.coreplatform.userservice.service.UserService;

/**
 * <p>
 * OrganizationServiceImpl class contains all the business logic for organization module and
 * performs the CRUD operations.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private AdminApiInterface adminApiInterface;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, List<Organization>> redisTemplate;

    /**
     * {@inheritDoc}
     */
    public Organization updateOrganization(Organization organization) {
        Organization existingOrganization = organizationRepository.findByIdAndIsDeletedFalse(organization.getId());
        if (Objects.isNull(existingOrganization)) {
            throw new DataNotFoundException(5001);
        }
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(organization, existingOrganization);
        return organizationRepository.save(existingOrganization);
    }

    /**
     * {@inheritDoc}
     */
    public Organization getOrganizationById(long organizationId) {
        return organizationRepository.getOrganizationById(organizationId);
    }

    /**
     * {@inheritDoc}
     */
    public void validateParentOrganization(Long parentOrganizationId, User user) {
        List<Long> parentOrganizationIds = user.getOrganizations().stream().map(Organization::getParentOrganizationId)
                .toList();
        if (!parentOrganizationIds.contains(parentOrganizationId)) {
            throw new DataNotAcceptableException(5002);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, List<Long>> getChildOrganizations(long tenantId, String formName) {
        Map<String, List<Long>> childIds = new HashMap<>();
        List<Organization> childOrganizations;
        List<Long> childOrgIds = new ArrayList<>();
        if (formName.equalsIgnoreCase(FieldConstants.COUNTRY)) {
            childOrganizations = organizationRepository.findByParentOrganizationId(tenantId);
            childOrgIds = childOrganizations.stream().map(BaseEntity::getId).toList();
            childIds.put(Constants.ACCOUNT_IDS, childOrgIds);
        }
        if (formName.equalsIgnoreCase(FieldConstants.COUNTRY) || formName.equalsIgnoreCase(Constants.ACCOUNT)) {
            if (formName.equalsIgnoreCase(Constants.ACCOUNT)) {
                childOrganizations = organizationRepository.findByParentOrganizationId(tenantId);
            } else {
                childOrganizations = organizationRepository.findByParentOrganizationIdIn(childOrgIds);
            }
            childOrgIds = childOrganizations.stream().map(BaseEntity::getId).toList();
            childIds.put(Constants.OPERATING_UNIT_IDS, childOrgIds);
        }
        if (formName.equalsIgnoreCase(Constants.COUNTRY) || formName.equalsIgnoreCase(Constants.ACCOUNT)
                || formName.equalsIgnoreCase(Constants.OPERATING_UNIT)) {
            if (formName.equalsIgnoreCase(Constants.OPERATING_UNIT)) {
                childOrganizations = organizationRepository.findByParentOrganizationId(tenantId);
            } else {
                childOrganizations = organizationRepository.findByParentOrganizationIdIn(childOrgIds);
            }
            childOrgIds = childOrganizations.stream().map(BaseEntity::getId).toList();
            childIds.put(Constants.SITE_IDS, childOrgIds);
        }
        return childIds;
    }

    /**
     * {@inheritDoc}
     */
    public User addAdminUsers(User user, boolean isRedRisk) {
        Organization organization = organizationRepository.findByIdAndIsDeletedFalse(user.getTenantId());
        if (Objects.isNull(organization)) {
            throw new DataNotFoundException(5001);
        }
        List<String> roleNames = user.getRoles().stream().map(Role::getName).toList();
        Set<Role> roles = !roleNames.isEmpty() ? roleService.getRolesByName(roleNames) : new HashSet<>();
        if (Objects.isNull(user.getId()) || Constants.ZERO == user.getId()) {
            user.getOrganizations().add(organization);
            user.setRoles(roles);
        } else {
            User existingUser = userService.getUserById(user.getId());
            userMapper.setExistingUser(user, existingUser, organization);
            userService.redRiskUserUpdate(existingUser, isRedRisk);
            Set<String> existingRoleNames = existingUser.getRoles().stream().map(Role::getName)
                    .collect(Collectors.toSet());
            roles.forEach(role -> {
                if (!existingRoleNames.contains(role.getName())) {
                    existingUser.getRoles().add(role);
                }
            });
            return userService.addUser(existingUser);
        }
        return userService.addUser(user);
    }

    /**
     * {@inheritDoc}
     */
    public User updateAdminUsers(User user) {
        Organization organization = organizationRepository.findByIdAndIsDeletedFalse(user.getTenantId());
        if (Objects.isNull(organization)) {
            throw new DataNotFoundException(5001);
        }
        user = userService.updateOrganizationUser(user, Constants.BOOLEAN_FALSE, Constants.BOOLEAN_FALSE);
        return user;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean deleteAdminUsers(CommonRequestDTO requestDto) {
        return userService.deleteOrganizationUser(requestDto);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean activateOrDeactivateOrganization(List<Long> formdataIdList, boolean doActivate) {
        if (!Objects.isNull(formdataIdList)) {
            Set<Organization> organizations = organizationRepository.findByIsDeletedFalseAndIsActiveAndIdIn(!doActivate,
                    formdataIdList);
            List<Organization> savedOrganizations = new ArrayList<>();
            if (!Objects.isNull(organizations)) {
                organizations.forEach(organization -> organization.setActive(doActivate));
                savedOrganizations = organizationRepository.saveAll(organizations);
            }
            return !Objects.isNull(savedOrganizations);
        }
        return Boolean.FALSE;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void createCountry(CountryOrganizationDTO countryRequestDto) {
        Organization organization = new Organization(Constants.COUNTRY, countryRequestDto.getName(), null, null);
        organization = addOrganization(organization);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CountryDTO countryDto = modelMapper.map(countryRequestDto, CountryDTO.class);
        countryDto.setTenantId(organization.getId());
        Country countryResponse = adminApiInterface
                .createCountry(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(), countryDto).getBody();
        if (!Objects.isNull(countryResponse)) {
            organization.setFormDataId(countryResponse.getId());
            List<User> users = new ArrayList<>();
            countryRequestDto.getUsers().forEach(userDto -> {
                    User user = modelMapper.map(userDto, User.class);
                    user.setCountry(countryResponse);
                    users.add(user);
            });
            addOrganizationUsers(users, List.of(Constants.ROLE_REGION_ADMIN), organization,
                    Constants.BOOLEAN_FALSE, null);
            addOrganization(organization);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void createAccount(AccountOrganizationDTO accountDto) {
        Organization organization = new Organization(Constants.ACCOUNT, accountDto.getName(),
                accountDto.getParentOrganizationId(), accountDto.getParentOrganizationId());
        organization = addOrganization(organization);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        AccountWorkflowDTO account = modelMapper.map(accountDto, AccountWorkflowDTO.class);
        account.setTenantId(organization.getId());
        account.setClinicalWorkflow(accountDto.getClinicalWorkflow());
        account.setCustomizedWorkflow(accountDto.getCustomizedWorkflow());
        Account accountResponse = adminApiInterface.createAccount(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), account);
        if (!Objects.isNull(accountResponse)) {
            organization.setFormDataId(accountResponse.getId());
            List<User> users = accountDto.getUsers().stream().map(user ->
                modelMapper.map(user, User.class)).toList();
            addOrganizationUsers(users, List.of(Constants.ROLE_ACCOUNT_ADMIN), organization,
                    Constants.BOOLEAN_FALSE, null);
            addOrganization(organization);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void createOU(OperatingUnitOrganizationDTO operatingUnitRequestDto) {
        Organization organization = new Organization(Constants.OPERATING_UNIT, operatingUnitRequestDto.getName(),
                operatingUnitRequestDto.getParentOrganizationId(), operatingUnitRequestDto.getParentOrganizationId());
        organization = addOrganization(organization);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OperatingUnitDTO operatingunitDto = modelMapper.map(operatingUnitRequestDto, OperatingUnitDTO.class);
        operatingunitDto.setTenantId(organization.getId());
        Operatingunit operatingUnitResponse = adminApiInterface.createOperatingUnit(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), operatingunitDto);
        if (!Objects.isNull(operatingUnitResponse)) {
            organization.setFormDataId(operatingUnitResponse.getId());
            List<User> users = operatingUnitRequestDto.getUsers().stream().map(user ->
                modelMapper.map(user, User.class)).toList();
            addOrganizationUsers(users, List.of(Constants.ROLE_OPERATING_UNIT_ADMIN), organization,
                    Constants.BOOLEAN_FALSE, null);
            addOrganization(organization);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void createSite(SiteOrganizationDTO siteRequestDto) {
        Organization organization = new Organization(Constants.SITE, siteRequestDto.getName(),
                siteRequestDto.getParentOrganizationId(), siteRequestDto.getParentOrganizationId());
        organization = addOrganization(organization);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SiteDTO siteDto = modelMapper.map(siteRequestDto, SiteDTO.class);
        siteDto.setCulture(new Culture(siteRequestDto.getCultureId()));
        siteDto.setTenantId(organization.getId());
        Site siteResponse = adminApiInterface.createSite(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), siteDto);
        if (!Objects.isNull(siteResponse)) {
            organization.setFormDataId(siteResponse.getId());
            Map<Long, Boolean> redRisks = new HashMap<>();
            siteRequestDto.getUsers().forEach(user -> {
                if (user.isRedRisk()) {
                    user.getRoles().add(new Role(null, Constants.ROLE_RED_RISK_USER));
                }
                redRisks.put(user.getId(), user.isRedRisk());
            });
            addOrganizationUsers(siteRequestDto.getUsers().stream().map(user ->
                modelMapper.map(user, User.class)).toList(),
                null, organization, Constants.BOOLEAN_TRUE, redRisks);
            addOrganization(organization);
        }
    }

    /**
     * {@inheritDoc}
     */
    public User updateSiteUser(SiteUserDTO userDto) {
        Organization organization = organizationRepository.findByIdAndIsDeletedFalse(userDto.getTenantId());
        if (Objects.isNull(organization)) {
            throw new DataNotFoundException(5001);
        }
        return userService.updateOrganizationUser(modelMapper.map(userDto, User.class), Constants.BOOLEAN_TRUE,
                userDto.isRedRisk());
    }

    /**
     * {@inheritDoc}
     */
    public Organization getOrganizationByFormDataIdAndName(long formDataId, String formName) {
        return organizationRepository.findByFormDataIdAndFormNameAndIsDeleted(formDataId, formName, Constants.BOOLEAN_FALSE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Organization> getOrganizationsByIds(List<Long> organizationIds) {
        return organizationRepository.findByIsDeletedFalseAndIdIn(organizationIds);
    }

    /**
     * <p>
     * This method is used to add a new organization after setting its sequence to 0, saving it to the
     * repository, and deleting the corresponding Redis key.
     * </p>
     *
     * @param organization {@link Organization} The organization need to be saved is given
     * @return {@link Organization} The saved organization is returned
     */
    private Organization addOrganization(Organization organization) {
        organization.setSequence(0L);
        organization = organizationRepository.save(organization);
        redisTemplate.delete(Constants.ORGANIZATION_REDIS_KEY);
        return organization;
    }

    /**
     * <p>
     * This method is used to add organization users with specified roles and red risks to an organization,
     * validating new users and retrieving existing ones.
     * </p>
     *
     * @param organizationUsers {@link List<User>} The list of users to be added to the organization is given
     * @param roles             {@link List<User>} A list of roles that will be assigned to the organization
     *                          users is given
     * @param organization      {@link Organization} The organization to which the users will be added is given
     * @param isSiteUser        {@link Boolean} A value indicating whether the users being added are site users or
     *                          not is given
     * @param redRisks          {@link  Map} A map of user's ID as key and the red risk status as value
     *                          is given
     */
    private void addOrganizationUsers(List<User> organizationUsers, List<String> roles, Organization organization,
                                      Boolean isSiteUser, Map<Long, Boolean> redRisks) {
        List<String> newUsernames = new ArrayList<>();
        Set<Long> existingUserIds = new HashSet<>();
        organizationUsers.forEach(user -> {
            if (Objects.isNull(user.getId()) || Constants.ZERO == user.getId()) {
                newUsernames.add(user.getUsername().toLowerCase());
            } else {
                existingUserIds.add(user.getId());
            }
        });
        userService.validateUsers(newUsernames);
        Role role = null;
        if (Objects.equals(Constants.BOOLEAN_FALSE, isSiteUser)) {
            String roleName = roles.get(Constants.ZERO);
            if (!roleName.isEmpty()) {
                role = roleService.getRoleByName(roleName);
            }
        }
        Map<Long, User> existingUsers = !existingUserIds.isEmpty() ? userService.getUserByIds(existingUserIds)
                : new HashMap<>();
        addUsers(organizationUsers, organization, existingUsers, newUsernames, role, isSiteUser, redRisks);
    }

    /**
     * <p>
     * This method is used to add users to an organization with specified roles and updates existing users if necessary.
     * </p>
     *
     * @param organizationUsers {@link List<User>} The list of users to be added to the organization is given
     * @param organization      {@link Organization} The organization to which the users will be added is given
     * @param existingUsers     {@link Map} A map of user's ID as key and the existing user as value
     *                          is given
     * @param newUsernames      {@link List<String>} A list of usernames for new users that need to be added to
     *                          the organization is given
     * @param role              {@link Role}The role that will be assigned to the organization users is given
     * @param isSiteUser        {@link Boolean} A value indicating whether the users being added are site users
     *                          or not is given
     * @param redRisks          {@link  Map} A map of user's ID as key and the red risk status as
     *                          value is given
     */
    private void addUsers(List<User> organizationUsers, Organization organization, Map<Long, User> existingUsers,
                          List<String> newUsernames, Role role, Boolean isSiteUser, Map<Long, Boolean> redRisks) {
        List<User> users = new ArrayList<>();
        Set<Role> siteUserRoles;
        for (User user : organizationUsers) {
            if (Objects.isNull(user.getId()) || Constants.ZERO == user.getId()) {
                user.setUsername(user.getUsername().toLowerCase());
                user.setTenantId(organization.getId());
                user.getOrganizations().add(organization);
                if (Boolean.FALSE.equals(isSiteUser)) {
                    user.setRoles(Set.of(role));
                } else {
                    siteUserRoles = roleService.getRolesByName(user.getRoles().stream().map(Role::getName).toList());
                    user.setRoles(siteUserRoles);
                }
                users.add(user);
            } else {
                User existingUser = existingUsers.get(user.getId());
                userMapper.setExistingUser(user, existingUser, organization);
                if (Boolean.TRUE.equals(isSiteUser)) {
                    userService.redRiskUserUpdate(existingUser, redRisks.get(user.getId()));
                }
                users.add(existingUser);
            }
        }
        userService.addUsers(users, newUsernames);
    }
}
