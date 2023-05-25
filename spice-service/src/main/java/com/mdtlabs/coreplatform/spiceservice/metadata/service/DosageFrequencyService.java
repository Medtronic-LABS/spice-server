package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.DosageFrequency;
import com.mdtlabs.coreplatform.common.service.GenericService;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * <p>
 * This an interface class for DosageFrequency module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface DosageFrequencyService extends GenericService<DosageFrequency> {

    /**
     * <p>
     * Gets all DosageFrequency
     * </p>
     *
     * @param sort {@link Sort} sort
     * @return {@link List<DosageFrequency>} List of DosageFrequency entities
     */
    List<DosageFrequency> getDosageFrequency(Sort sort);

}
