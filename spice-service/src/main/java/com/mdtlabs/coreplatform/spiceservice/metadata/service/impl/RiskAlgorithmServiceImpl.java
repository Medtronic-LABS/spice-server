package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.RiskAlgorithm;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.RiskAlgorithmRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.RiskAlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * This service class contain all the business logic for RiskAlgorithm module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class RiskAlgorithmServiceImpl implements RiskAlgorithmService {

    @Autowired
    private RiskAlgorithmRepository riskAlgorithmRepository;

    /**
     * {@inheritDoc}
     */
    public List<RiskAlgorithm> getRiskAlgorithms() {
        if (Constants.RISK_ALGORITHMS.isEmpty()) {
            Constants.RISK_ALGORITHMS.addAll(riskAlgorithmRepository.findByIsDeletedFalseAndIsActiveTrue());
        }

        return Constants.RISK_ALGORITHMS;
    }

    /**
     * {@inheritDoc}
     */
    public RiskAlgorithm getRiskAlgorithms(Long countryId) {
        if (Constants.RISK_ALGORITHMS.isEmpty()) {
            Constants.RISK_ALGORITHMS.addAll(riskAlgorithmRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        Optional<RiskAlgorithm> riskAlgorithmOptional = Constants.RISK_ALGORITHMS.stream()
                .filter(obj -> countryId.equals(obj.getCountryId())).findFirst();
        return riskAlgorithmOptional.orElse(null);
    }

}
