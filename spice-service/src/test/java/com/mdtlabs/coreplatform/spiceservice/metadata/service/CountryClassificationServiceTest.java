package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.CountryClassification;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.CountryClassificationRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.CountryClassificationServiceImpl;
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
 * Country Classification Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
class CountryClassificationServiceTest {

    @InjectMocks
    private CountryClassificationServiceImpl countryClassificationService;

    @Mock
    private CountryClassificationRepository countryClassificationRepository;

    @Test
    void getClassificationsByCountryId() {
        //given
        List<CountryClassification> countryClassifications = List.of(TestDataProvider.getCountryClassification());

        //when
        when(countryClassificationRepository.findByCountryIdAndIsDeletedFalse(1L)).thenReturn(countryClassifications);

        //then
        List<CountryClassification> classificationList = countryClassificationService.getClassificationsByCountryId(1L);
        Assertions.assertEquals(countryClassifications.size(), classificationList.size());
    }
}