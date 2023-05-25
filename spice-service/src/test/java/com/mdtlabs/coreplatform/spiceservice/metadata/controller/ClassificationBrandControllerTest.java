package com.mdtlabs.coreplatform.spiceservice.metadata.controller;

import com.mdtlabs.coreplatform.common.model.entity.spice.ClassificationBrand;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ClassificationBrandService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * <p>
 * ClassificationBrandControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in ClassificationBrandController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class ClassificationBrandControllerTest {

    @InjectMocks
    private ClassificationBrandController classificationBrandController;

    @Mock
    private ClassificationBrandService classificationBrandService;

    private List<ClassificationBrand> classificationBrands = new ArrayList<>();

    private ClassificationBrand classificationBrand = TestDataProvider.getClassificationBrand();

    @Test
    @DisplayName("GetClassificationBrandByCountryId Test")
    void getByCountryAndClassificationId() {
        //given
        classificationBrands.add(classificationBrand);
        //when
        when(classificationBrandService.getByCountryAndClassificationId(1l, 1l))
                .thenReturn(classificationBrands);
        //then
        Assertions.assertNotNull(classificationBrands);
        List<ClassificationBrand> response = classificationBrandController
                .getByCountryAndClassificationId(1l, 1l);
        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals(classificationBrandService
                .getByCountryAndClassificationId(1l, 1l).size(), classificationBrands.size());

    }
}
