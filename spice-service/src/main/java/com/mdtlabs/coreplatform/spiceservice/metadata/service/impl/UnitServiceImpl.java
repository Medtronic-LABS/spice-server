package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.model.entity.spice.Unit;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.UnitRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.UnitService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * This service class contain all the business logic for unit module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class UnitServiceImpl extends GenericServiceImpl<Unit> implements UnitService {

    @Autowired
    private UnitRepository unitRepository;

    /**
     * {@inheritDoc}
     */
    public List<Unit> getUnitsByType(String type) {
        return unitRepository.findByType(type.toUpperCase());
    }

}
