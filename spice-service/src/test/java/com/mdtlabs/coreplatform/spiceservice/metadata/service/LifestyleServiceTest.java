package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Lifestyle;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.LifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.LifestyleServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * <p>
 * Lifestyle Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LifestyleServiceTest {

    @InjectMocks
    private LifestyleServiceImpl lifestyleService;

    @Mock
    private LifestyleRepository lifestyleRepository;

    @Test
    void getLifestyles() {
        //given
        Constants.LIFESTYLES = new ArrayList<>();
        List<Lifestyle> lifestyleList = List.of(TestDataProvider.getLifestyle());

        //when
        when(lifestyleRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(lifestyleList);

        //then
        List<Lifestyle> lifestyles = lifestyleService.getLifestyles();
        Assertions.assertNotNull(lifestyles);

        //then
        lifestyles = lifestyleService.getLifestyles();
        Assertions.assertFalse(Constants.LIFESTYLES.isEmpty());
    }

    @Test
    void getLifestylesByIds() {
        //given
        Constants.LIFESTYLES = new ArrayList<>();
        List<Lifestyle> lifestyles = List.of(TestDataProvider.getLifestyle());
        List<Long> ids = List.of(1L, 2L);

        //when
        when(lifestyleRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(lifestyles);

        //then
        List<Lifestyle> lifestyleList = lifestyleService.getLifestylesByIds(ids);
        Assertions.assertNotNull(lifestyleList);

        //then
        lifestyleList = lifestyleService.getLifestylesByIds(ids);
        Assertions.assertFalse(Constants.LIFESTYLES.isEmpty());
    }
}