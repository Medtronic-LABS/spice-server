package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import java.util.List;

import com.mdtlabs.coreplatform.common.model.entity.spice.Unit;
import com.mdtlabs.coreplatform.common.service.GenericService;

/**
 * <p>
 * This an interface class for unit module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface UnitService extends GenericService<Unit> {

    /**
     * <p>
     * Gets all Units by type
     * </p>
     *
     * @param {@link String} type
     * @return {@link List<Unit>} List of Unit entities
     */
    public List<Unit> getUnitsByType(String type);
}
