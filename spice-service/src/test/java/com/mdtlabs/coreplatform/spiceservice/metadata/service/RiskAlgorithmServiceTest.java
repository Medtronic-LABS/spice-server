package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.RiskAlgorithm;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.RiskAlgorithmRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.RiskAlgorithmServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * <p>
 * Risk Algorithm Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RiskAlgorithmServiceTest {

    @InjectMocks
    private RiskAlgorithmServiceImpl riskAlgorithmService;

    @Mock
    private RiskAlgorithmRepository riskAlgorithmRepository;

    @Test
    void getRiskAlogorithms() {
        //given
        List<RiskAlgorithm> riskAlgorithms = List.of(TestDataProvider.getMetaRiskAlgorithm());

        //when
        when(riskAlgorithmRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(riskAlgorithms);

        //then
        List<RiskAlgorithm> riskAlgorithmList = riskAlgorithmService.getRiskAlgorithms();
        Assertions.assertNotNull(riskAlgorithmList);

        //then
        riskAlgorithmList = riskAlgorithmService.getRiskAlgorithms();
        Assertions.assertFalse(Constants.RISK_ALGORITHMS.isEmpty());
    }

    @Test
    void testGetRiskAlogorithms() {
        //given
        Constants.RISK_ALGORITHMS = new ArrayList<>();
        List<RiskAlgorithm> riskAlgorithms = List.of(TestDataProvider.getMetaRiskAlgorithm());

        //when
        when(riskAlgorithmRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(riskAlgorithms);

        //then
        RiskAlgorithm riskAlgorithm = riskAlgorithmService.getRiskAlgorithms(1L);
        Assertions.assertNotNull(riskAlgorithm);

        //then
        riskAlgorithm = riskAlgorithmService.getRiskAlgorithms(0L);
        Assertions.assertFalse(Constants.RISK_ALGORITHMS.isEmpty());
    }
}