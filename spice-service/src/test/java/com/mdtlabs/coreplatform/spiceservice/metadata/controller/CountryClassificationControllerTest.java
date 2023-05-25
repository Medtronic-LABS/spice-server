package com.mdtlabs.coreplatform.spiceservice.metadata.controller;

import com.mdtlabs.coreplatform.common.model.entity.spice.CountryClassification;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CountryClassificationService;
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
 * CountryClassificationControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in CountryClassificationController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class CountryClassificationControllerTest {

    @InjectMocks
    CountryClassificationController countryClassificationController;

    @Mock
    private CountryClassificationService countryClassificationService;

    private List<CountryClassification> classifications = new ArrayList<>();

    private CountryClassification countryClassification = new CountryClassification();

    @Test
    @DisplayName("GetClassificationByCountryId Test")
    void getClassificationByCountryId() {
        //given
        long countryId = 1l;
        countryClassification.setCountryId(1L);
        countryClassification.setId(1L);
        countryClassification.setTenantId(4l);
        classifications.add(countryClassification);
        //when
        when(countryClassificationService.getClassificationsByCountryId(countryId))
                .thenReturn(classifications);
        //then
        List<CountryClassification> actualList = countryClassificationController
                .getClassificationsByCountryId(countryId);
        Assertions.assertEquals(classifications, actualList);
        Assertions.assertEquals(classifications.size(), actualList.size());
        Assertions.assertEquals(classifications.get(0).getCountryId(), actualList.get(0).getCountryId());
    }
}
