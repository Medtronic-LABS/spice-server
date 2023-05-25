package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.DosageForm;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.DosageFormRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.DosageFormServiceImpl;
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
 * DosageForm Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DosageFormServiceTest {

    @InjectMocks
    private DosageFormServiceImpl dosageFormService;

    @Mock
    private DosageFormRepository dosageFormRepository;

    @Test
    void findByIsDeletedFalseAndIsActiveTrue() {
        //given
        List<DosageForm> dosageForms = List.of(TestDataProvider.getDosageForm());

        //when
        when(dosageFormRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(dosageForms);

        //then
        List<DosageForm> dosageFormList = dosageFormService.getDosageFormNotOther();
        Assertions.assertEquals(dosageForms.size(), dosageFormList.size());
    }

    @Test
    void getDosageFormNotOther() {
        //given
        List<DosageForm> dosageForms = List.of(TestDataProvider.getDosageForm());

        //when
        when(dosageFormRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(dosageForms);

        //then
        List<DosageForm> dosageFormList = dosageFormService.getDosageFormNotOther();
        Assertions.assertEquals(dosageForms.size(), dosageFormList.size());
    }
}