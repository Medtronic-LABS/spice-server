package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.ClassificationBrand;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.ClassificationBrandRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.ClassificationBrandServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * <p>
 * Classification Brand Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
class ClassificationBrandServiceTest {

    @InjectMocks
    private ClassificationBrandServiceImpl classificationBrandService;

    @Mock
    private ClassificationBrandRepository classificationBrandRepository;

    @Test
    void getByCountryAndClassificationId() {
        //given
        List<ClassificationBrand> classificationBrandList = List.of(TestDataProvider.getClassificationBrand());

        //when
        when(classificationBrandRepository.findByCountryIdAndClassificationIdAndIsDeletedFalse(1L,
                1L)).thenReturn(classificationBrandList);

        //then
        List<ClassificationBrand> classificationBrands = classificationBrandService
                .getByCountryAndClassificationId(1L, 1L);
        Assertions.assertEquals(classificationBrandList.size(), classificationBrands.size());
    }
}