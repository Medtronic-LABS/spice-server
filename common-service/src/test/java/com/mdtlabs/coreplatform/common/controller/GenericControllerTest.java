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
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;

/**
 * <p>
 * GenericControllerTest class has the test methods for the GenericController class.
 * </p>
 *
 * @author JohnKennedy created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
class GenericControllerTest {

    @InjectMocks
    private GenericController genericController;

    @Mock
    private GenericServiceImpl genericService;

    @Test
    void save() {
        //given
        BaseEntity baseEntity = TestDataProvider.getBaseEntity();
        baseEntity.setId(TestConstants.ONE);

        //when
        when(genericService.save(baseEntity)).thenReturn(baseEntity);

        //then
        ResponseEntity<BaseEntity> result = genericController.save(baseEntity);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void toVerifySaveWithException() {
        //given
        BaseEntity baseEntity = TestDataProvider.getBaseEntity();
        baseEntity.setId(TestConstants.ONE);

        //when
        when(genericService.save(baseEntity)).thenThrow(new RuntimeException(TestConstants.EMPTY));

        //then
        ResponseEntity<BaseEntity> result = genericController.save(baseEntity);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void findAll() {
        //given
        BaseEntity baseEntity = TestDataProvider.getBaseEntity();
        baseEntity.setId(TestConstants.ONE);
        List<BaseEntity> baseEntities = List.of(baseEntity);

        //when
        when(genericService.findAll()).thenReturn(baseEntities);

        //then
        ResponseEntity<List<BaseEntity>> result = genericController.findAll();
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void toVerifyFindAllWithException() {
        //when
        when(genericService.findAll()).thenThrow(new RuntimeException(TestConstants.EMPTY));

        //then
        ResponseEntity<List<BaseEntity>> result = genericController.findAll();
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void findById() {
        //given
        BaseEntity baseEntity = TestDataProvider.getBaseEntity();
        baseEntity.setId(TestConstants.ONE);

        //when
        when(genericService.findById(TestConstants.ONE)).thenReturn(baseEntity);

        //then
        ResponseEntity<BaseEntity> result = genericController.findById(TestConstants.ONE);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void toVerifyFindByIdWithNull() {
        //when
        when(genericService.findById(TestConstants.ONE)).thenReturn(null);

        //then
        ResponseEntity<BaseEntity> result = genericController.findById(TestConstants.ONE);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void toVerifyFindByIdWithException() {
        //when
        when(genericService.findById(TestConstants.ONE)).thenThrow(new RuntimeException(TestConstants.EMPTY));

        //then
        ResponseEntity<BaseEntity> result = genericController.findById(TestConstants.ONE);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }
}