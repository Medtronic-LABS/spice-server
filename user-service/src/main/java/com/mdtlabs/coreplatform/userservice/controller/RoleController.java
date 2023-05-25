package com.mdtlabs.coreplatform.userservice.controller;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.model.dto.RoleDTO;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.userservice.message.SuccessCode;
import com.mdtlabs.coreplatform.userservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.userservice.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * This is a Java class that defines REST API endpoints for CRUD operations on roles, using a RoleService.
 * </p>
 *
 * @author VigneshKumar created on Jan 30, 2022
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private RoleService roleService;

    /**
     * <p>
     * This method is used to create and add new role for some action privileges
     * that is provided in role dto.
     * </p>
     *
     * @param roleDto {@link RoleDTO} The role to be created is given as role dto
     * @return {@link SuccessResponse<Role>} The role for the given role details is created
     * and returned with status
     */
    @PostMapping
    public SuccessResponse<Role> addRole(@RequestBody RoleDTO roleDto) {
        Role newRole = roleService.addRole(modelMapper.map(roleDto, Role.class));
        return new SuccessResponse<>((null == newRole)
                ? SuccessCode.ROLE_SAVE_ERROR : SuccessCode.ROLE_SAVE, newRole, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to retrieve all the active roles as list.
     * </p>
     *
     * @return {@link SuccessResponse<List>} Gets the list of active roles with status and message
     */
    @GetMapping
    public SuccessResponse<List<Role>> getAllRoles() {
        List<Role> roleList = roleService.getAllRoles();
        return new SuccessResponse((roleList.isEmpty())
                ? SuccessCode.GET_ROLES_ERROR : SuccessCode.GET_ROLES, roleList, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to update Role details using the id from the provided role dto.
     * </p>
     *
     * @param roleDto {@link RoleDTO} The role to be updated is given with id as role dto
     * @return {@link SuccessResponse<Role>} The given role is updated and returned with status
     */
    @PutMapping
    public SuccessResponse<Role> updateRole(@RequestBody RoleDTO roleDto) {
        Role updatedRole = roleService.updateRole(modelMapper.map(roleDto, Role.class));
        return new SuccessResponse<>((null == updatedRole)
                ? SuccessCode.ROLE_UPDATE_ERROR : SuccessCode.ROLE_UPDATE, updatedRole, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to delete the role for the given id.
     * </p>
     *
     * @param roleId The id of the role to be deleted is given
     * @return {@link SuccessResponse<Role>} The role for the given id is deleted and returned with status
     */
    @DeleteMapping
    public SuccessResponse<Role> deleteRoleById(@PathVariable(Constants.ROLE_ID_PARAM) long roleId) {
        int role = roleService.deleteRoleById(roleId);
        return new SuccessResponse<>((Constants.ONE == role)
                ? SuccessCode.ROLE_DELETE : SuccessCode.ROLE_DELETE_ERROR,
                roleService.deleteRoleById(roleId), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get role information by role id.
     * </p>
     *
     * @param roleId The id of the role to be retrieved is given
     * @return {@link SuccessResponse<Role>} The role for the given id is retrieved with status
     */
    @GetMapping("/{id}")
    public SuccessResponse<Role> getRoleById(@PathVariable(FieldConstants.ID) long roleId) {
        return new SuccessResponse<>(SuccessCode.GET_ROLE, roleService.getRoleById(roleId), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get role information by role name.
     * </p>
     *
     * @param name {@link String} The name of the role to be searched is given
     * @return {@link SuccessResponse<Role>} The role for the given name is retrieved with status
     */
    @GetMapping("/{name}")
    public SuccessResponse<Role> getRoleByName(@PathVariable(FieldConstants.NAME) String name) {
        return new SuccessResponse<>(SuccessCode.GET_ROLE, roleService.getRoleByName(name), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get role information of the given list of ids.
     * </p>
     *
     * @param roleIds {@link List<Long>} The ids of the roles to be retrieved is given
     * @return {@link SuccessResponse<Set<Role>>} Returns set of roles for the given ids
     */
    @PostMapping("/role-ids")
    public SuccessResponse<Set<Role>> getRolesByIds(@RequestBody List<Long> roleIds) {
        return new SuccessResponse<>(SuccessCode.GET_ROLES, roleService.getRolesByIds(roleIds), HttpStatus.OK);
    }
}
