package com.mdtlabs.coreplatform.common.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.TestConstants;
import com.mdtlabs.coreplatform.common.TestDataProvider;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;
import com.mdtlabs.coreplatform.common.repository.GenericTenantRepository;
import com.mdtlabs.coreplatform.common.service.impl.GenericTenantServiceImpl;

/**
 * <p>
 * GenericTenantServiceImplTest class has the test methods for the GenericTenantServiceImpl class.
 * </p>
 *
 * @author JohnKennedy created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
class GenericTenantServiceImplTest {

    @InjectMocks
    private GenericTenantServiceImpl genericTenantService;

    @Mock
    private GenericTenantRepository genericTenantRepository;

    @Test
    void findAll() {
        //given
        TenantBaseEntity tenantBaseEntity = TestDataProvider.getTenantBaseEntity();
        tenantBaseEntity.setId(TestConstants.ONE);
        tenantBaseEntity.setTenantId(TestConstants.ONE);
        List<TenantBaseEntity> tenantBaseEntities = List.of(tenantBaseEntity);

        //when
        when(genericTenantRepository.findAll()).thenReturn(tenantBaseEntities);

        //then
        List<TenantBaseEntity> result = genericTenantService.findAll();
        Assertions.assertEquals(TestConstants.ONE, result.get(Constants.ZERO).getTenantId());
        Assertions.assertNotNull(result);
    }

    @Test
    void toVerifyFindAllWithException() {
        //when
        when(genericTenantRepository.findAll()).thenThrow(new RuntimeException(TestConstants.EMPTY));

        //then
        assertThrows(Exception.class, () -> genericTenantService.findAll());
    }

    @Test
    void findById() {
        //given
        TenantBaseEntity tenantBaseEntity = TestDataProvider.getTenantBaseEntity();
        tenantBaseEntity.setId(TestConstants.ONE);
        tenantBaseEntity.setTenantId(TestConstants.ONE);

        //when
        when(genericTenantRepository.findById(TestConstants.ONE)).thenReturn(Optional.of(tenantBaseEntity));

        //then
        TenantBaseEntity result = genericTenantService.findById(TestConstants.ONE);
        Assertions.assertEquals(TestConstants.ONE, result.getTenantId());
        Assertions.assertNotNull(result);
    }

    @Test
    void toVerifyFindByIdWithException() {
        //given
        TenantBaseEntity tenantBaseEntity = TestDataProvider.getTenantBaseEntity();
        tenantBaseEntity.setId(TestConstants.ONE);
        tenantBaseEntity.setTenantId(TestConstants.ONE);

        //when
        when(genericTenantRepository.findById(TestConstants.ONE)).thenReturn(null);

        //then
        Assertions.assertThrows(Exception.class, () -> genericTenantService.findById(TestConstants.ONE));
    }

    @Test
    void toVerifyFindByIdWithNull() {
        //when
        when(genericTenantRepository.findById(TestConstants.ONE)).thenReturn(Optional.empty());

        //then
        TenantBaseEntity result = genericTenantService.findById(TestConstants.ONE);
        Assertions.assertNull(result);
    }

    @Test
    void save() {
        //given
        TenantBaseEntity tenantBaseEntity = TestDataProvider.getTenantBaseEntity();
        tenantBaseEntity.setId(TestConstants.ONE);
        tenantBaseEntity.setTenantId(TestConstants.ONE);

        //when
        when(genericTenantRepository.save(tenantBaseEntity)).thenReturn(tenantBaseEntity);

        //then
        TenantBaseEntity result = genericTenantService.save(tenantBaseEntity);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(TestConstants.ONE, tenantBaseEntity.getId());
    }

    @Test
    void toVerifySaveWithException() {
        //given
        TenantBaseEntity tenantBaseEntity = TestDataProvider.getTenantBaseEntity();
        tenantBaseEntity.setId(TestConstants.ONE);
        tenantBaseEntity.setTenantId(TestConstants.ONE);

        //when
        when(genericTenantRepository.save(tenantBaseEntity)).thenThrow(new RuntimeException(TestConstants.EMPTY));

        //then
        assertThrows(Exception.class, () -> genericTenantService.save(tenantBaseEntity));
    }
}