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
import org.springframework.data.domain.Sort;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.TestConstants;
import com.mdtlabs.coreplatform.common.TestDataProvider;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;

/**
 * <p>
 * GenericServiceImplTest class has the test methods for the GenericServiceImpl class.
 * </p>
 *
 * @author JohnKennedy created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
class GenericServiceImplTest {

    @InjectMocks
    private GenericServiceImpl genericService;

    @Mock
    private GenericRepository genericRepository;

    @Test
    void findAll() {
        //given
        BaseEntity baseEntity = TestDataProvider.getBaseEntity();
        baseEntity.setId(TestConstants.ONE);
        List<BaseEntity> baseEntities = List.of(baseEntity);

        //when
        when(genericRepository.findAll()).thenReturn(baseEntities);

        //then
        List<BaseEntity> result = genericService.findAll();
        Assertions.assertEquals(TestConstants.ONE, result.get(Constants.ZERO).getId());
    }

    @Test
    void toVerifyFindAllWithException() {
        //when
        when(genericRepository.findAll()).thenThrow(new RuntimeException(TestConstants.EMPTY));

        //then
        assertThrows(Exception.class, () -> genericService.findAll());
    }

    @Test
    void findById() {
        //given
        BaseEntity baseEntity = TestDataProvider.getBaseEntity();
        baseEntity.setId(TestConstants.ONE);

        //when
        when(genericRepository.findById(TestConstants.ONE)).thenReturn(Optional.of(baseEntity));

        //then
        BaseEntity result = genericService.findById(TestConstants.ONE);
        Assertions.assertEquals(TestConstants.ONE, result.getId());
    }

    @Test
    void findByIdWithException() {
        //given
        BaseEntity baseEntity = TestDataProvider.getBaseEntity();
        baseEntity.setId(TestConstants.ONE);

        //when
        when(genericRepository.findById(TestConstants.ONE)).thenReturn(null);

        //then
        assertThrows(Exception.class, () -> genericService.findById(TestConstants.ONE));
    }

    @Test
    void findByIdWithNull() {
        //when
        when(genericRepository.findById(TestConstants.ONE)).thenReturn(Optional.empty());

        //then
        BaseEntity result = genericService.findById(TestConstants.ONE);
        Assertions.assertNull(result);
    }

    @Test
    void save() {
        //given
        BaseEntity baseEntity = TestDataProvider.getBaseEntity();
        baseEntity.setId(TestConstants.ONE);

        //when
        when(genericRepository.save(baseEntity)).thenReturn(baseEntity);

        //then
        BaseEntity result = genericService.save(baseEntity);
        Assertions.assertEquals(TestConstants.ONE, result.getId());
    }

    @Test
    void toVerifySaveWithException() {
        //given
        BaseEntity baseEntity = TestDataProvider.getBaseEntity();
        baseEntity.setId(TestConstants.ONE);

        //when
        when(genericRepository.save(baseEntity)).thenThrow(new RuntimeException(TestConstants.EMPTY));

        //then
        assertThrows(Exception.class, () -> genericService.save(baseEntity));
    }

    @Test
    void testFindAll() {
        //given
        BaseEntity baseEntity = TestDataProvider.getBaseEntity();
        baseEntity.setId(TestConstants.ONE);
        List<BaseEntity> baseEntities = List.of(baseEntity);
        Sort sort = Sort.by(Constants.UPDATED_AT).ascending();

        //when
        when(genericRepository.findAll(sort)).thenReturn(baseEntities);

        //then
        List<BaseEntity> result = genericService.findAll(sort);
        Assertions.assertEquals(TestConstants.ONE, result.get(Constants.ZERO).getId());
        Assertions.assertNotNull(result);
    }

    @Test
    void testFindAllWithException() {
        //given
        Sort sort = Sort.by(Constants.UPDATED_AT).ascending();

        //when
        when(genericRepository.findAll(sort)).thenThrow(new RuntimeException(TestConstants.EMPTY));

        //then
        assertThrows(Exception.class, () -> genericService.findAll(sort));
    }
}