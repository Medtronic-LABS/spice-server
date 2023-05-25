package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.Complication;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;

/**
 * <p>
 * This an interface class for complication module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface ComplicationService extends GenericService<Complication> {

    /**
     * <p>
     * This method is used to get complications by id's.
     * </p>
     *
     * @return {@link List<Complication>} Complications List is returned
     */
    List<Complication> getComplications();

}
