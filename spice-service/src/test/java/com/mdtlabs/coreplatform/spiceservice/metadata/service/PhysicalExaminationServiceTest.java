package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.PhysicalExamination;
import com.mdtlabs.coreplatform.spiceservice.common.repository.PhysicalExaminationRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.PhysicalExaminationServiceImpl;
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
 * Physical Examination Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PhysicalExaminationServiceTest {

    @InjectMocks
    private PhysicalExaminationServiceImpl physicalExaminationService;

    @Mock
    private PhysicalExaminationRepository physicalExaminationRepository;

    @Test
    void getPhysicalExamination() {
        //given
        List<PhysicalExamination> physicalExaminations = List.of(TestDataProvider.getPhysicalExamination());

        //when
        when(physicalExaminationRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(physicalExaminations);

        //then
        List<PhysicalExamination> physicalExaminationList = physicalExaminationService.getPhysicalExamination();
        Assertions.assertNotNull(physicalExaminationList);

        //then
        physicalExaminationList = physicalExaminationService.getPhysicalExamination();
        Assertions.assertFalse(Constants.PHYSICAL_EXAMINATIONS.isEmpty());
    }

    @Test
    void getPhysicalExaminationByIds() {
        //given
        Set<Long> ids = Set.of(1L, 2L);
        List<PhysicalExamination> physicalExaminations = List.of(TestDataProvider.getPhysicalExamination());

        //when
        when(physicalExaminationRepository.findByIsDeletedFalseAndIsActiveTrue())
                .thenReturn(physicalExaminations);

        //then
        Set<PhysicalExamination> physicalExaminationSet = physicalExaminationService.getPhysicalExaminationByIds(ids);
        Assertions.assertNotNull(physicalExaminationSet);
    }

}