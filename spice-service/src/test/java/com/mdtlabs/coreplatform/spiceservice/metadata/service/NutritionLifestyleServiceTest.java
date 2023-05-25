package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.NutritionLifestyle;
import com.mdtlabs.coreplatform.spiceservice.common.repository.NutritionLifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.NutritionLifestyleServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

/**
 * <p>
 * Nutrition Lifestyle Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NutritionLifestyleServiceTest {

    @InjectMocks
    private NutritionLifestyleServiceImpl nutritionLifestyleService;

    @Mock
    private NutritionLifestyleRepository nutritionLifestyleRepository;

    @Test
    void findByIsDeletedFalseAndIsActiveTrue() {
        //given
        List<NutritionLifestyle> nutritionLifestyles = List.of(TestDataProvider.getNutritionLifestyle());

        //when
        when(nutritionLifestyleRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(nutritionLifestyles);

        //then
        List<NutritionLifestyle> nutritionLifestyleList = nutritionLifestyleService
                .findByIsDeletedFalseAndIsActiveTrue();
        Assertions.assertNotNull(nutritionLifestyleList);

        //then
        nutritionLifestyleList = nutritionLifestyleService
                .findByIsDeletedFalseAndIsActiveTrue();
        Assertions.assertFalse(Constants.NUTRITION_LIFESTYLES.isEmpty());
    }

    @Test
    void getNutritionLifestyleByIds() {
        //given
        Set<Long> ids = Set.of(1L, 2L);
        Set<NutritionLifestyle> nutritionLifestyles = Set.of(TestDataProvider.getNutritionLifestyle());

        //when
        when(nutritionLifestyleRepository.findByIdInAndIsDeletedFalseAndIsActiveTrue(ids))
                .thenReturn(nutritionLifestyles);

        //then
        Set<NutritionLifestyle> nutritionLifestyleSet = nutritionLifestyleService.getNutritionLifestyleByIds(ids);
        Assertions.assertNotNull(nutritionLifestyleSet);
    }
}