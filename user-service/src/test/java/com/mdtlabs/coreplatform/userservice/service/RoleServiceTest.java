package com.mdtlabs.coreplatform.userservice.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.userservice.repository.RoleRepository;
import com.mdtlabs.coreplatform.userservice.service.impl.RoleServiceImpl;
import com.mdtlabs.coreplatform.userservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * <p>
 * Role Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 06, 2023
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void testAddRole() {
        //given
        Role role = TestDataProvider.getRole();

        //when
        when(roleRepository.save(role)).thenReturn(role);

        //then
        Role roleResponse = roleService.addRole(role);
        Assertions.assertEquals(role.getId(), roleResponse.getId());
    }

    @Test
    void testGetAllRoles() {
        //given
        List<Role> roles = TestDataProvider.getRoles();

        //when
        when(roleRepository.getAllRoles(Boolean.TRUE)).thenReturn(roles);

        //then
        List<Role> rolesResponse = roleService.getAllRoles();
        Assertions.assertEquals(roles.size(), rolesResponse.size());
    }

    @Test
    void testUpdateRole() {
        //given
        Role role = TestDataProvider.getRole();

        //when
        when(roleRepository.save(role)).thenReturn(role);

        //then
        Role roleResponse = roleService.updateRole(role);
        Assertions.assertEquals(role.getId(), roleResponse.getId());
    }

    @Test
    void testDeleteRoleById() {
        //given
        Role role = TestDataProvider.getRole();

        //when
        when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));

        //then
        int result = roleService.deleteRoleById(role.getId());
        Assertions.assertEquals(Constants.ONE, result);
    }

    @Test
    void testDeleteRoleByIdWithException() {
        //given
        Role role = TestDataProvider.getRole();

        //when
        when(roleRepository.findById(role.getId())).thenReturn(Optional.ofNullable(null));

        //then
        assertThrows(DataNotFoundException.class, () -> roleService.deleteRoleById(1L));
    }

    @Test
    void testGetRoleById() {
        //given
        Role role = TestDataProvider.getRole();

        //when
        when(roleRepository.getRoleById(role.getId())).thenReturn(role);

        //then
        Role roleResponse = roleService.getRoleById(role.getId());
        Assertions.assertEquals(role.getId(), roleResponse.getId());
    }

    @Test
    void testGetRoleByName() {
        //given
        Role role = TestDataProvider.getRole();

        //when
        when(roleRepository.getRoleByName(role.getName())).thenReturn(role);

        //then
        Role roleResponse = roleService.getRoleByName(role.getName());
        Assertions.assertEquals(role.getId(), roleResponse.getId());
    }

    @Test
    void testGetRolesByName() {
        //given
        Role role = TestDataProvider.getRole();
        List<String> roleNames = new ArrayList<>();
        Set<Role> roleSet = new HashSet<>();
        roleNames.add(role.getName());
        roleSet.add(role);

        //when
        when(roleRepository.findByIsDeletedFalseAndIsActiveTrueAndNameIn(roleNames)).thenReturn(roleSet);

        //then
        Set<Role> responses = roleService.getRolesByName(roleNames);
        Assertions.assertEquals(roleSet.size(), responses.size());
    }

    @Test
    void testGetRolesByIds() {
        //given
        Role role = TestDataProvider.getRole();
        List<Long> roleIds = new ArrayList<>();
        Set<Role> roles = new HashSet<>();
        roleIds.add(role.getId());
        roles.add(role);

        //when
        when(roleRepository.findByIsDeletedFalseAndIsActiveTrueAndIdIn(roleIds)).thenReturn(roles);

        //then
        Set<Role> responses = roleService.getRolesByIds(roleIds);
        Assertions.assertEquals(roles.size(), responses.size());
    }
}
