package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.MedicalCompliance;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;

/**
 * <p>
 * This an interface class for MedicalCompliance module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface MedicalComplianceService extends GenericService<MedicalCompliance> {

    /**
     * <p>
     * Gets a medicalCompliance.
     * </p>
     *
     * @return {@link List<MedicalCompliance>} List of MedicalCompliance entities
     */
    List<MedicalCompliance> getMedicalComplianceList();

}
