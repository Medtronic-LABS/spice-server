package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Diagnosis;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.DiagnosisRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.DiagnosisServiceImpl;
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
 * Diagnosis Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Mar 14, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DiagnosisServiceTest {

    @InjectMocks
    private DiagnosisServiceImpl diagnosisService;

    @Mock
    private DiagnosisRepository diagnosisRepository;

    @Test
    void getDiagnosis() {
        //given
        List<Diagnosis> diagnosisList = List.of(TestDataProvider.getDiagnosis());

        //when
        when(diagnosisRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(diagnosisList);

        //then
        List<Diagnosis> result = diagnosisService.getDiagnosis();
        Assertions.assertNotNull(result);

        //then
        result = diagnosisService.getDiagnosis();
        Assertions.assertFalse(Constants.DIAGNOSIS.isEmpty());
    }
}
