package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complication;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.ComplicationRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.ComplicationServiceImpl;
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
 * Complication Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ComplicationServiceTest {

    @InjectMocks
    private ComplicationServiceImpl complicationService;

    @Mock
    private ComplicationRepository complicationRepository;

    @Test
    void findByIsDeletedFalseAndIsActiveTrue() {
        //given
        List<Complication> complications = List.of(TestDataProvider.getComplication());

        //when
        when(complicationRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(complications);

        //then
        List<Complication> complicationList = complicationService.getComplications();
        Assertions.assertEquals(complications.size(), complicationList.size());

        //then
        complicationList = complicationService.getComplications();
        Assertions.assertFalse(Constants.COMPLICATIONS.isEmpty());
    }

}