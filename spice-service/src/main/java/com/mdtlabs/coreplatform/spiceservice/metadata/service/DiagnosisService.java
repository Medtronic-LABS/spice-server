package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.Diagnosis;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;

/**
 * <p>
 * This an interface class for diagnosis module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface DiagnosisService extends GenericService<Diagnosis> {

    /**
     * <p>
     * Gets all Diagnosis
     * </p>
     *
     * @return {@link List<Diagnosis>} List of Diagnosis entities
     */
    List<Diagnosis> getDiagnosis();

}
