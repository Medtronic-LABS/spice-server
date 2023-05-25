package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.CurrentMedication;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.CurrentMedicationRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.CurrentMedicationServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

/**
 * <p>
 * Current Medication Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
class CurrentMedicationServiceTest {

    @InjectMocks
    private CurrentMedicationServiceImpl currentMedicationService;

    @Mock
    private CurrentMedicationRepository currentMedicationRepository;

    @Test
    void findByIsDeletedFalseAndIsActiveTrue() {
        //given
        List<CurrentMedication> currentMedications = List.of(TestDataProvider.getCurrentMedication());

        //when
        when(currentMedicationRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(currentMedications);

        //then
        List<CurrentMedication> medicationList = currentMedicationService.findByIsDeletedFalseAndIsActiveTrue();
        Assertions.assertEquals(currentMedications.size(), medicationList.size());
    }

    @Test
    void getCurrentMedicationByIds() {
        //given
        Set<Long> ids = Set.of(1L, 2L);
        List<CurrentMedication> currentMedications = List.of(TestDataProvider.getCurrentMedication());

        //when
        when(currentMedicationRepository.findByIdInAndIsDeletedFalseAndIsActiveTrue(ids)).thenReturn(currentMedications);

        //then
        List<CurrentMedication> medicationList = currentMedicationService.getCurrentMedicationByIds(ids);
        Assertions.assertEquals(currentMedications.size(), medicationList.size());
    }
}