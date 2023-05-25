package com.mdtlabs.coreplatform.userservice.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.userservice.repository.RoleRepository;
import com.mdtlabs.coreplatform.userservice.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * RoleServiceImpl class contains all the business logic for Role module and
 * performs the CRUD operations.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * {@inheritDoc}
     */
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    /**
     * {@inheritDoc}
     */
    public List<Role> getAllRoles() {
        return roleRepository.getAllRoles(Boolean.TRUE);
    }

    /**
     * {@inheritDoc}
     */
    public Role updateRole(Role role) {
        return roleRepository.save(role);
    }

    /**
     * {@inheritDoc}
     */
    public int deleteRoleById(long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new DataNotFoundException(1102));
        role.setActive(Boolean.FALSE);
        roleRepository.save(role);
        return Constants.ONE;
    }

    /**
     * {@inheritDoc}
     */
    public Role getRoleById(long roleId) {
        return roleRepository.getRoleById(roleId);
    }

    /**
     * {@inheritDoc}
     */
    public Role getRoleByName(String name) {
        return roleRepository.getRoleByName(name);
    }

    /**
     * {@inheritDoc}
     */
    public Set<Role> getRolesByName(List<String> roleNames) {
        return roleRepository.findByIsDeletedFalseAndIsActiveTrueAndNameIn(roleNames);
    }

    /**
     * {@inheritDoc}
     */
    public Set<Role> getRolesByIds(List<Long> roleIds) {
        return roleRepository.findByIsDeletedFalseAndIsActiveTrueAndIdIn(roleIds);
    }
}
