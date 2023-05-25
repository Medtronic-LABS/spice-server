package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.DosageFrequency;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.DosageFrequencyRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.DosageFrequencyServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * <p>
 * Diagnosis Frequency Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Mar 14, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DosageFrequencyServiceTest {

    @InjectMocks
    private DosageFrequencyServiceImpl dosageFrequencyService;

    @Mock
    private DosageFrequencyRepository dosageFrequencyRepository;

    @Test
    void getDosageFrequency() {
        //given
        List<DosageFrequency> dosageFrequencies = List.of(TestDataProvider.getDosageFrequency());

        //when
        when(dosageFrequencyRepository.findAll(Sort.sort(DosageFrequencyServiceImpl.class))).thenReturn(dosageFrequencies);

        //then
        List<DosageFrequency> result = dosageFrequencyService.getDosageFrequency(Sort.sort(DosageFrequencyServiceImpl.class));
        Assertions.assertNotNull(result);

        //then
        result = dosageFrequencyService.getDosageFrequency(Sort.sort(DosageFrequencyServiceImpl.class));
        Assertions.assertFalse(Constants.DOSAGE_FREQUENCIES.isEmpty());
    }
}
