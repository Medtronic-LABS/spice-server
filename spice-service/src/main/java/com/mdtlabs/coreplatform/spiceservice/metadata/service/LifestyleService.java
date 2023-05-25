package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.Lifestyle;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;

/**
 * <p>
 * This an interface class for life style module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface LifestyleService extends GenericService<Lifestyle> {

    /**
     * <p>
     * Retrieves list of Lifestyles.
     * </p>
     *
     * @return List of Lifestyle {@link List<Lifestyle>} list of entity
     */
    List<Lifestyle> getLifestyles();

    /**
     * <p>
     * Retrieves list of Lifestyles by id's.
     * </p>
     *
     * @param lifestyleIds {@link List<Lifestyle>} lifestyleIds is given
     * @return List of Lifestyle {@link List<Long>} list of entity is given
     */
    List<Lifestyle> getLifestylesByIds(List<Long> lifestyleIds);

}
