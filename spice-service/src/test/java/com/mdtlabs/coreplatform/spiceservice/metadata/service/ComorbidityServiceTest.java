package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Comorbidity;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.ComorbidityRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.ComorbidityServiceImpl;
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

import static org.mockito.Mockito.when;

/**
 * <p>
 * Comorbidity Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ComorbidityServiceTest {

    @InjectMocks
    private ComorbidityServiceImpl comorbidityService;

    @Mock
    private ComorbidityRepository comorbidityRepository;

    @Test
    void findByIsDeletedFalseAndIsActiveTrue() {
        //given
        List<Comorbidity> comorbidities = List.of(TestDataProvider.getComorbidity());

        //when
        when(comorbidityRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(comorbidities);

        //then
        List<Comorbidity> comorbidityList = comorbidityService.getComorbidityList();
        Assertions.assertEquals(comorbidities.size(), comorbidityList.size());

        //then
        comorbidityList = comorbidityService.getComorbidityList();
        Assertions.assertFalse(Constants.COMORBIDITIES.isEmpty());
    }
}