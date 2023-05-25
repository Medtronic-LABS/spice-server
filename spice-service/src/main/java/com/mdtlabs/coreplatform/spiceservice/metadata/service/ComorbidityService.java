package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.Comorbidity;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;

/**
 * <p>
 * This an interface class for comorbidity module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface ComorbidityService extends GenericService<Comorbidity> {

    /**
     * <p>
     * The function returns a list of comorbidities.
     * </p>
     *
     * @return {@link List<Comorbidity>} A list of Comorbidity objects is being returned.
     */
    List<Comorbidity> getComorbidityList();
}
