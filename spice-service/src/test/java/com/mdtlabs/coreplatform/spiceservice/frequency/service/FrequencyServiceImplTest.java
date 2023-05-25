package com.mdtlabs.coreplatform.spiceservice.frequency.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.entity.spice.Frequency;
import com.mdtlabs.coreplatform.spiceservice.frequency.repository.FrequencyRepository;
import com.mdtlabs.coreplatform.spiceservice.frequency.service.impl.FrequencyServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * <p>
 * FrequencyServiceImplTest class used to test all possible positive
 * and negative cases for all methods and conditions used in FrequencyServiceImpl class.
 * </p>
 *
 * @author Jaganathan created on Feb 8, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FrequencyServiceImplTest {

    @InjectMocks
    private FrequencyServiceImpl frequencyService;

    @Mock
    private FrequencyRepository frequencyRepository;

    private final Frequency frequency = TestDataProvider.getFrequency();

    private List<Frequency> frequencies;

    private Frequency actualFrequency;

    private List<Frequency> actualFrequencies;

    @Test
    @DisplayName("AddFrequency Test")
    void addFrequency() {

        Assertions.assertThrows(BadRequestException.class, () -> frequencyService.addFrequency(null));
        //when
        when(frequencyRepository.save(any())).thenReturn(frequency);
        Assertions.assertEquals(frequency, frequencyService.addFrequency(frequency));

    }

    @Test
    @DisplayName("GetFrequenciesById Test")
    void getFrequencyById() {
        //given
        long id = 1L;
        frequencies = new ArrayList<>();
        List<Frequency> frequencyList = List.of(frequency);
        //when
        when(frequencyRepository.findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrderAsc()).thenReturn(frequencyList);
        //then
        actualFrequency = frequencyService.getFrequencyById(id);
        Assertions.assertEquals(frequency.getId(), actualFrequency.getId());

    }

    @Test
    @DisplayName("GetFrequencyByIdNull Test")
    void getFrequencyByIdNull() {
        //given
        long id = 0L;
        frequencies = new ArrayList<>();
        //when
        when(frequencyRepository.findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrderAsc()).thenReturn(frequencies);
        //then
        actualFrequency = frequencyService.getFrequencyById(id);
        Assertions.assertTrue(frequencies.isEmpty());
        Assertions.assertNull(actualFrequency);
    }

    @Test
    @DisplayName("GetFrequencyByNameAndType Test")
    void getFrequencyByNameAndType() {
        //given
        List<Frequency> frequencyList = List.of(frequency);
        //when
        when(frequencyRepository.findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrderAsc()).thenReturn(frequencyList);
        //then
        actualFrequency = frequencyService
                .getFrequencyByFrequencyNameAndType(frequency.getName(), frequency.getType());
        Assertions.assertEquals(frequency.getName(), actualFrequency.getName());
        Assertions.assertEquals(frequency.getType(), actualFrequency.getType());
        Assertions.assertEquals(frequency.getId(), actualFrequency.getId());
        Assertions.assertEquals(actualFrequency.getName(), frequencyList.get(0).getName());
        //given
        Constants.FREQUENCY_LIST.add(frequency);
        //when
        when(frequencyRepository.findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrderAsc()).thenReturn(frequencyList);
        //then
        actualFrequency = frequencyService.getFrequencyByFrequencyNameAndType(Constants.EMPTY, Constants.EMPTY);
        Assertions.assertNull(actualFrequency);
    }

    @Test
    @DisplayName("GetFrequencyByRiskLevel Test")
    void getFrequencyByRiskLevel() {
        //given
        List<Frequency> frequencyList = List.of(frequency);
        //when
        when(frequencyRepository.findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrderAsc()).thenReturn(frequencyList);
        //then
        Assertions.assertNotNull(frequencyList);
        actualFrequencies = frequencyService.getFrequencyListByRiskLevel(frequency.getRiskLevel());
        Assertions.assertEquals(frequency.getRiskLevel(), actualFrequencies.get(0).getRiskLevel());
        //given
        frequencies = List.of(frequency);
        //when
        when(frequencyRepository.findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrderAsc()).thenReturn(frequencyList);
        //then
        actualFrequencies = frequencyService.getFrequencyListByRiskLevel(Constants.HIGH);
        Assertions.assertEquals(List.of(), actualFrequencies);
        Assertions.assertTrue(actualFrequencies.isEmpty());
    }

    @Test
    @DisplayName("GetActiveFrequencies Test")
    void findByIsDeletedAndIsActiveTrue() {
        Constants.FREQUENCY_LIST.clear();
        //given
        List<Frequency> frequencyList = List.of(frequency);
        //when
        when(frequencyRepository.findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrderAsc()).thenReturn(frequencyList);
        //then
        actualFrequencies = frequencyService.findByIsDeletedFalseAndIsActiveTrue();
        Assertions.assertEquals(frequencyList.size(), actualFrequencies.size());
        Assertions.assertEquals(frequencyList.get(0), actualFrequencies.get(0));
        Assertions.assertEquals(frequencyList.get(0).getId(), actualFrequencies.get(0).getId());
        Assertions.assertEquals(frequencyList.get(0).getName(), actualFrequencies.get(0).getName());

        //given
        frequencies = List.of(frequency);
        actualFrequencies = frequencyService.findByIsDeletedFalseAndIsActiveTrue();
        Assertions.assertEquals(frequencies, actualFrequencies);
        Assertions.assertEquals(frequencies.size(), actualFrequencies.size());
    }

    @Test
    @DisplayName("GetFrequencies Test")
    void getFrequencies() {
        Constants.FREQUENCY_LIST.clear();
        List<Frequency> frequencyList = new ArrayList<>(List.of(frequency));
        //when
        when(frequencyRepository.findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrderAsc()).thenReturn(frequencyList);
        actualFrequencies = frequencyService.getFrequencies();
        Assertions.assertEquals(frequencyList, actualFrequencies);
        //given
        Constants.FREQUENCY_LIST.add(frequency);
        //then
        actualFrequencies = frequencyService.getFrequencies();
        Assertions.assertEquals(Constants.FREQUENCY_LIST, actualFrequencies);

        Constants.FREQUENCY_LIST.clear();
        //then
        actualFrequencies = frequencyService.getFrequencies();
        Assertions.assertEquals(Constants.FREQUENCY_LIST, actualFrequencies);
    }

    @Test
    @DisplayName("GetFrequencyByType Test")
    void getFrequencyByType() {
        //given
        String type = Constants.TYPE;
        Constants.FREQUENCY_LIST = new ArrayList<>();
        List<Frequency> frequencyList = List.of(frequency);
        //when
        when(frequencyRepository.findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrderAsc()).thenReturn(frequencyList);
        //then
        Assertions.assertTrue(Constants.FREQUENCY_LIST.isEmpty());
        Assertions.assertThrows(DataNotFoundException.class, () -> frequencyService.getFrequencyByType(type));
        //given
        frequency.setType(type);
        //when
        when(frequencyRepository.findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrderAsc()).thenReturn(frequencyList);
        //then
        Assertions.assertFalse(Constants.FREQUENCY_LIST.isEmpty());
        actualFrequency = frequencyService.getFrequencyByType(type);
        Assertions.assertEquals(frequency, actualFrequency);
        Assertions.assertEquals(frequency.getType(), actualFrequency.getType());
        Assertions.assertEquals(frequency.getId(), actualFrequency.getId());

    }
}
