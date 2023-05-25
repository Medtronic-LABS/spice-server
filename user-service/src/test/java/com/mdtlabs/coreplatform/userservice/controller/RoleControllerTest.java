package com.mdtlabs.coreplatform.userservice.controller;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.RoleDTO;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.userservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.userservice.service.impl.RoleServiceImpl;
import com.mdtlabs.coreplatform.userservice.util.TestConstants;
import com.mdtlabs.coreplatform.userservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

/**
 * <p>
 * Role Controller Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Jan 25, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleServiceImpl roleService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestDataProvider.setUp(RoleController.class, "modelMapper", roleController);
    }

    @Test
    void testAddRole() {
        //given
        Role role = TestDataProvider.getRole();
        RoleDTO roleDTO = TestDataProvider.getRoleDTO();

        //when
        when(roleService.addRole(role)).thenReturn(role);
        when(modelMapper.map(roleDTO, Role.class)).thenReturn(role);

        //then
        SuccessResponse<Role> successResponse = roleController.addRole(roleDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        Assertions.assertNotNull(role);
    }

    @Test
    void toVerifyAddRoleWithNull() {
        //given
        Role role = TestDataProvider.getRole();
        RoleDTO roleDTO = TestDataProvider.getRoleDTO();

        //when
        when(roleService.addRole(role)).thenReturn(role);
        when(modelMapper.map(roleDTO, Role.class)).thenReturn(role);

        //then
        SuccessResponse<Role> successResponse = roleController.addRole(roleDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testGetAllRoles() {
        //given
        List<Role> roles = TestDataProvider.getRoles();

        //when
        when(roleService.getAllRoles()).thenReturn(roles);

        //then
        SuccessResponse<List<Role>> successResponse = roleController.getAllRoles();
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void toVerifyGetAllRolesWithNull() {
        //given
        List<Role> newRoles = Collections.emptyList();

        //when
        when(roleService.getAllRoles()).thenReturn(newRoles);

        //then
        SuccessResponse<List<Role>> successResponse = roleController.getAllRoles();
        Assertions.assertEquals(Boolean.TRUE, newRoles.isEmpty());
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testUpdateRole() {
        //given
        Role role = TestDataProvider.getRole();
        RoleDTO roleDTO = TestDataProvider.getRoleDTO();
        role.setName(TestConstants.NAME);
        role.setLevel(TestConstants.LEVEL);
        roleDTO.setName(TestConstants.NAME);
        roleDTO.setLevel(TestConstants.LEVEL);

        //when
        when(roleService.updateRole(role)).thenReturn(role);
        when(modelMapper.map(roleDTO, Role.class)).thenReturn(role);

        //then
        SuccessResponse<Role> successResponse = roleController.updateRole(roleDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void toVerifyUpdateRoleWithNull() {
        Role role = TestDataProvider.getRole();
        RoleDTO roleDTO = TestDataProvider.getRoleDTO();
        role.setName(TestConstants.NAME);
        role.setLevel(TestConstants.LEVEL);
        roleDTO.setName(TestConstants.NAME);
        roleDTO.setLevel(TestConstants.LEVEL);

        //when
        when(roleService.updateRole(role)).thenReturn(null);
        when(modelMapper.map(roleDTO, Role.class)).thenReturn(role);

        //then
        SuccessResponse<Role> successResponse = roleController.updateRole(roleDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testDeleteRoleById() {
        //given
        int updatedRole = Constants.ONE;

        //when
        when(roleService.deleteRoleById(TestConstants.ONE)).thenReturn(updatedRole);

        //then
        SuccessResponse<Role> successResponse = roleController.deleteRoleById(TestConstants.ONE);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void toVerifyDeleteRoleById() {
        //given
        int updatedRole = Constants.ZERO;

        //when
        when(roleService.deleteRoleById(TestConstants.ZERO)).thenReturn(updatedRole);

        //then
        SuccessResponse<Role> successResponse = roleController.deleteRoleById(TestConstants.ZERO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testGetRoleById() {
        //given
        Role role = TestDataProvider.getRole();

        //when
        when(roleService.getRoleById(TestConstants.ONE)).thenReturn(role);

        //then
        SuccessResponse<Role> successResponse = roleController.getRoleById(TestConstants.ONE);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        Assertions.assertEquals(TestConstants.NAME, roleService.getRoleById(TestConstants.ONE).getName());
    }

    @Test
    void testGetRoleByName() {
        //given
        Role role = TestDataProvider.getRole();

        //when
        when(roleService.getRoleByName(TestConstants.NAME)).thenReturn(role);

        //then
        SuccessResponse<Role> successResponse = roleController.getRoleByName(TestConstants.NAME);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        Assertions.assertEquals(TestConstants.LEVEL, roleService.getRoleByName(TestConstants.NAME).getLevel());
    }

    @Test
    void testGetRolesByIds() {
        //given
        Role role = TestDataProvider.getRole();
        Set<Role> roleSet = new HashSet<>();
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(role.getId());
        roleSet.add(role);

        //when
        when(roleService.getRolesByIds(roleIds)).thenReturn(roleSet);

        //then
        SuccessResponse<Set<Role>> successResponse = roleController.getRolesByIds(roleIds);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        Assertions.assertEquals(roleService.getRolesByIds(roleIds).size(), roleSet.size());
    }
}
