package com.mdtlabs.coreplatform.common.aspects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.exception.Validation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.repository.OrganizationRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * This class is the business logic implementation for validation annotation
 * used under @UserTenantValidation annotation.
 * </p>
 *
 * @author VigneshKumar created on Feb 09, 2023
 */
@Aspect
@Component
public class UserTenantPermissionValidation {

    /**
     * <p>
     * Creating an instance of the `ObjectMapper` class from the Jackson library, which is used to
     * convert Java objects to JSON and vice versa
     * </p>
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * <p>
     * OrganizationRepository interface, used to perform CRUD operations
     * on the `Organization` entity.
     * </p>
     */
    @Autowired
    public OrganizationRepository organizationRepository;
    @Autowired
    private RedisTemplate<String, List<Organization>> redisTemplate;
    private List<Organization> organizationList = new ArrayList<>();

    private Map<String, Object> requestBody;

    /**
     * <p>
     * This is a Java aspect that validates user and tenant information in the request body for certain
     * HTTP methods, unless the user has super user or super admin roles.
     * </p>
     *
     * @param joinPoint {@link JoinPoint} object represents the execution of a method in the program
     * @param body      The body parameter object represents the request body of an HTTP request.
     */
    @Before("@annotation(com.mdtlabs.coreplatform.common.annotations.UserTenantValidation)  && args(.., @RequestBody body)")
    public void validateAspect(JoinPoint joinPoint, final Object body) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        UserDTO user = UserContextHolder.getUserDto();
        if ((Constants.METHOD_POST.equalsIgnoreCase(request.getMethod())
                || Constants.METHOD_PUT.equalsIgnoreCase(request.getMethod())
                || Constants.METHOD_DELETE.equalsIgnoreCase(request.getMethod())
                || Constants.METHOD_PATCH.equalsIgnoreCase(request.getMethod()))
                && user.getRoles().stream().noneMatch(role -> role.getName().equals(Constants.ROLE_SUPER_USER))
                && user.getRoles().stream().noneMatch(role -> role.getName().equals(Constants.ROLE_SUPER_ADMIN))) {
            if (body.getClass().equals(ArrayList.class)) {
                List<Object> bodyList = mapper.convertValue(body, ArrayList.class);
                bodyList.forEach(object -> {
                    requestBody = mapper.convertValue(object, Map.class);
                    validationCheck(requestBody);
                });
            } else {
                requestBody = mapper.convertValue(body, Map.class);
                validationCheck(requestBody);
            }
            requestBody.clear();

        }
    }

    /**
     * <p>
     * Validation check on id and tenant id.
     * </p>
     *
     * @param requestBody {@link Map<String,Object> } input object
     */
    private void validationCheck(Map<String, Object> requestBody) {
        Logger.logInfo("====requestBody===" + requestBody);
        if (!requestBody.containsKey(Constants.TENANT_PARAMETER_NAME)) {
            throw new Validation(20006);
        }
        Set<Long> childIds = new HashSet<>();
        List<List<Organization>> organizationListRedis = redisTemplate.opsForList()
                .range(Constants.ORGANIZATION_REDIS_KEY, Constants.ZERO, organizationList.size());
        if (!Objects.isNull(organizationListRedis) && organizationListRedis.isEmpty()) {
            organizationListRedis = loadRedisData(organizationListRedis);
        }
        if (organizationListRedis != null) {
            getChildOrganizations(UserContextHolder.getUserDto().getTenantId(), childIds, organizationListRedis);
        }
        Logger.logInfo("=========" + childIds);
        if (childIds.stream().noneMatch(id -> requestBody.get(Constants.TENANT_PARAMETER_NAME).equals(id))) {
            throw new Validation(20005);
        }
        if (!childIds.contains(Long.parseLong(requestBody.get(Constants.TENANT_PARAMETER_NAME).toString()))) {
            throw new Validation(20005);
        }
        childIds.clear();
    }

    /**
     * <p>
     * Loads organization data into redis.
     * </p>
     *
     * @param organizationListRedis {@link List<List<Organization>>} list of organization
     * @return List  {@link List<List<Organization>>} list of organization
     */
    private List<List<Organization>> loadRedisData(List<List<Organization>> organizationListRedis) {
        organizationList.clear();
        organizationListRedis.clear();
        organizationList = organizationRepository.findAll();
        redisTemplate.opsForList().leftPush(Constants.ORGANIZATION_REDIS_KEY, organizationList);
        organizationListRedis = redisTemplate.opsForList()
                .range(Constants.ORGANIZATION_REDIS_KEY, Constants.ZERO, organizationList.size());
        return organizationListRedis;
    }

    /**
     * <p>
     * Gets child organizations from redis with tenant id.
     * </p>
     *
     * @param tenantId              tenant id
     * @param childIds              {@link Set<Long>} set of child ids
     * @param organizationListRedis {@link List<List<Organization>>} list of organization
     */
    public void getChildOrganizations(Long tenantId, Set<Long> childIds,
                                      List<List<Organization>> organizationListRedis) {
        String formName = organizationListRedis.get(Constants.ZERO).stream().filter(org -> org.getId().equals(tenantId))
                .toList().get(Constants.ZERO).getFormName();
        List<Organization> organizations = organizationListRedis.get(Constants.ZERO);
        Set<Long> childOrgIds = new HashSet<>();

        if (formName.equalsIgnoreCase(Constants.COUNTRY)) {
            childOrgIds = getTenantIds(organizations, tenantId);
            childIds.addAll(getTenantIds(organizations, tenantId));
        }
        childOrgIds = getAccountChildTenants(tenantId, childIds, formName, organizations, childOrgIds);
        if (formName.equalsIgnoreCase(Constants.COUNTRY) || formName.equalsIgnoreCase(Constants.ACCOUNT)
                || formName.equalsIgnoreCase(Constants.OPERATING_UNIT)) {
            if (formName.equalsIgnoreCase(Constants.OPERATING_UNIT)) {
                childIds.addAll(getTenantIds(organizations, tenantId));
            } else {
                for (Long childOrg : childOrgIds) {
                    Set<Long> ids = getTenantIds(organizations, childOrg);
                    childIds.addAll(ids);
                }
            }
        }
        childIds.add(tenantId);
        organizations.clear();
    }

    /**
     * <p>
     * Gets the Child organization for an organization of Account type.
     * </p>
     *
     * @param tenantId      Account tenant ID
     * @param childIds      {@link Set<Long>} child organization IDs
     * @param formName      Organization form name
     * @param organizations {@link List<Organization>} List of Organizations
     * @param childOrgIds   {@link Set<Long>} List of child Organization IDs
     * @return {@link Set<Long>} Set  List of child organization IDs
     */
    private Set<Long> getAccountChildTenants(Long tenantId, Set<Long> childIds, String formName,
                                             List<Organization> organizations, Set<Long> childOrgIds) {
        if (formName.equalsIgnoreCase(Constants.COUNTRY) || formName.equalsIgnoreCase(Constants.ACCOUNT)) {
            if (formName.equalsIgnoreCase(Constants.ACCOUNT)) {
                childOrgIds = getTenantIds(organizations, tenantId);
                childIds.addAll(childOrgIds);
            } else {
                Set<Long> childs = new HashSet<>();
                for (Long childOrg : childOrgIds) {
                    Set<Long> ids = getTenantIds(organizations, childOrg);
                    childs.addAll(ids);
                    childIds.addAll(ids);
                }
                childOrgIds = childs;
            }
        }
        return childOrgIds;
    }

    /**
     * <p>
     * This function takes a list of organizations and a parent ID, filters the organizations to find
     * those with a matching parent ID, and returns a set of the IDs of those filtered organizations.
     * </p>
     *
     * @param organizations {@link List<Organization>} A list of Organization objects is given.
     * @param parentId      The parentId represents the ID of the parent organization to retrieve the
     *                      tenant IDs is given.
     * @return {@link Set<Long>} The method is returning a set of Long values representing the IDs of the child
     * organizations that have a parent organization with the specified parentId is given.
     */
    public Set<Long> getTenantIds(List<Organization> organizations, Long parentId) {
        List<Organization> filteredTenants = organizations.stream()
                .filter(org -> !Objects.isNull(org.getParentOrganizationId())
                        && org.getParentOrganizationId().equals(parentId))
                .toList();
        return filteredTenants.stream().map(BaseEntity::getId).collect(Collectors.toSet());
    }
}