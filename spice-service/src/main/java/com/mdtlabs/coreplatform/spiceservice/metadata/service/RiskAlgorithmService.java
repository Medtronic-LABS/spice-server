package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.RiskAlgorithm;

import java.util.List;

/**
 * <p>
 * This an interface class for RiskAlgorithm module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface RiskAlgorithmService {

    /**
     * <p>
     * Gets all Risk Algorithms
     * </p>
     *
     * @return {@link List<RiskAlgorithm>} List of RiskAlgorithm entities
     */
    List<RiskAlgorithm> getRiskAlgorithms();

    /**
     * <p>
     * Gets all Risk Algorithms
     * </p>
     *
     * @param countryId {@link Long} countryId
     * @return {@link RiskAlgorithm} List of RiskAlgorithm entities
     */
    RiskAlgorithm getRiskAlgorithms(Long countryId);


}
