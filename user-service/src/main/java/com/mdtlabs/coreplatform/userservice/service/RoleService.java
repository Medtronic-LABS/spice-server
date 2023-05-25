package com.mdtlabs.coreplatform.userservice.service;

import com.mdtlabs.coreplatform.common.model.entity.Role;

import java.util.List;
import java.util.Set;


/**
 * <p>
 * RoleService interface is an interface for role module that can be implemented
 * in any class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface RoleService {

    /**
     * <p>
     * This method is used to add a new role.
     * </p>
     *
     * @param role {@link Role} The role to be created and added to the database is given
     * @return {@link Role} The created role is returned
     */
    Role addRole(Role role);


    /**
     * <p>
     * This method is used to get the list of roles.
     * </p>
     *
     * @return {@link List<Role>} The list of roles retrieved is returned
     */
    List<Role> getAllRoles();

    /**
     * <p>
     * This method is used to update the role using the given role details.
     * </p>
     *
     * @param role {@link Role} The role which is to be updated with details is given
     * @return {@link Role} The role is updated and returned
     */
    Role updateRole(Role role);

    /**
     * <p>
     * This method is used to soft delete the role for the given id.
     * </p>
     *
     * @param roleId The id of the role which is to be deleted is given
     * @return The int value is returned if the role is successfully deleted for the given id
     */
    int deleteRoleById(long roleId);

    /**
     * <p>
     * This method is used to get the role of the given id.
     * </p>
     *
     * @param roleId The id of the role that need to retrieve is given
     * @return {@link Role} The role is retrieved which has the provided role id
     */
    Role getRoleById(long roleId);

    /**
     * <p>
     * This method used to get the role of the given name.
     * </p>
     *
     * @param name {@link String} The name of the role that need to retrieve is given
     * @return {@link Role} The role is retrieved which has the provided role name
     */
    Role getRoleByName(String name);

    /**
     * <p>
     * This method used to get list of roles of the given list of role names.
     * </p>
     *
     * @param roles {@link List<String>} The list of role names that need to retrieve is given
     * @return {@link Set<Role>} A set of roles that match the names provided in the name list is returned
     */
    Set<Role> getRolesByName(List<String> roles);

    /**
     * <p>
     * This method is used to get a set of roles of given list of role IDs.
     * </p>
     *
     * @param roleIds {@link List<Long>} A list of IDs of the roles that need to be retrieved is given
     * @return {@link Set<Role>} A set of roles identified by the list of role IDs is returned
     */
    Set<Role> getRolesByIds(List<Long> roleIds);
}
