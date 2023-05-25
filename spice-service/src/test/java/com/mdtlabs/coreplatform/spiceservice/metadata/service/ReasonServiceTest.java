package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.Reason;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.ReasonRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.ReasonServiceImpl;
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
 * Reason Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReasonServiceTest {

    @InjectMocks
    private ReasonServiceImpl reasonService;

    @Mock
    private ReasonRepository reasonRepository;

    @Test
    void getReasons() {
        //given
        List<Reason> reasons = List.of(TestDataProvider.getReason());

        //when
        when(reasonRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(reasons);

        //then
        List<Reason> reasonList = reasonService.getReasons();
        Assertions.assertEquals(reasons.size(), reasonList.size());
    }

    @Test
    void testGetReasons() {
        //given
        List<Reason> reasons = List.of(TestDataProvider.getReason());

        //when
        when(reasonRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(reasons);

        //then
        List<Reason> reasonList = reasonService.getReasons();
        Assertions.assertNotNull(reasonList);
    }
}