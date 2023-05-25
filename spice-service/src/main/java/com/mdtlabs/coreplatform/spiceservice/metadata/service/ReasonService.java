package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.Reason;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;

/**
 * <p>
 * This an interface class for Reason module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface ReasonService extends GenericService<Reason> {

    /**
     * <p>
     * Gets all reasons
     * </p>
     *
     * @return {@link List<Reason>} List of reasons
     */
    List<Reason> getReasons();

}
