package com.mdtlabs.coreplatform.common.controller;

import java.util.List;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mdtlabs.coreplatform.common.TestConstants;
import com.mdtlabs.coreplatform.common.TestDataProvider;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;
import com.mdtlabs.coreplatform.common.service.GenericTenantService;

/**
 * <p>
 * GenericTenantControllerTest class has the test methods for the GenericTenantController class.
 * </p>
 *
 * @author JohnKennedy created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
class GenericTenantControllerTest {

    @InjectMocks
    private GenericTenantController genericTenantController;

    @Mock
    private GenericTenantService genericTenantService;

    @Test
    void save() {
        //given
        TenantBaseEntity tenantBaseEntity = TestDataProvider.getTenantBaseEntity();
        tenantBaseEntity.setId(TestConstants.ONE);

        //when
        when(genericTenantService.save(tenantBaseEntity)).thenReturn(tenantBaseEntity);

        //then
        ResponseEntity<TenantBaseEntity> result = genericTenantController.save(tenantBaseEntity);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void toVerifySaveWithException() {
        //given
        TenantBaseEntity tenantBaseEntity = TestDataProvider.getTenantBaseEntity();
        tenantBaseEntity.setId(TestConstants.ONE);

        //when
        when(genericTenantService.save(tenantBaseEntity)).thenThrow(new RuntimeException(TestConstants.EMPTY));

        //then
        ResponseEntity<TenantBaseEntity> result = genericTenantController.save(tenantBaseEntity);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void findAll() {
        //given
        TenantBaseEntity tenantBaseEntity = TestDataProvider.getTenantBaseEntity();
        tenantBaseEntity.setId(TestConstants.ONE);
        List<BaseEntity> baseEntities = List.of(tenantBaseEntity);

        //when
        when(genericTenantService.findAll()).thenReturn(baseEntities);

        //then
        ResponseEntity<List<BaseEntity>> result = genericTenantController.findAll();
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void toVerifyFindAllWithException() {
        //when
        when(genericTenantService.findAll()).thenThrow(new RuntimeException(TestConstants.EMPTY));

        //then
        ResponseEntity<List<BaseEntity>> result = genericTenantController.findAll();
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void findById() {
        //given
        TenantBaseEntity tenantBaseEntity = TestDataProvider.getTenantBaseEntity();
        tenantBaseEntity.setId(TestConstants.ONE);

        //when
        when(genericTenantService.findById(TestConstants.ONE)).thenReturn(tenantBaseEntity);

        //then
        ResponseEntity<BaseEntity> result = genericTenantController.findById(TestConstants.ONE);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void toVerifyFindByIdWithNull() {
        //when
        when(genericTenantService.findById(TestConstants.ONE)).thenReturn(null);

        //then
        ResponseEntity<BaseEntity> result = genericTenantController.findById(TestConstants.ONE);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void toVerifyFindByIdWithException() {
        //when
        when(genericTenantService.findById(TestConstants.ONE)).thenThrow(new RuntimeException());

        //then
        ResponseEntity<BaseEntity> result = genericTenantController.findById(TestConstants.ONE);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }
}