package com.mdtlabs.coreplatform.spiceservice.symptom.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.Symptom;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;

/**
 * <p>
 * SymptomService is a java interface for symptom module. It defines method for retrieving a list of symptom
 * customizations
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface SymptomService extends GenericService<Symptom> {

    /**
     * <p>
     * This method is used to returns a list of Symptom
     * </p>
     *
     * @return {@link List<Symptom>} The List of Symptom is being returned.
     */
    List<Symptom> getSymptoms();
}
