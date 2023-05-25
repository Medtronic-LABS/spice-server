package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.DosageForm;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;

/**
 * <p>
 * This an interface class for dosage form module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface DosageFormService extends GenericService<DosageForm> {


    /**
     * <p>
     * Gets dosage forms.
     * </p>
     *
     * @return {@link List<DosageForm>} list of DosageForm entities.
     */
    List<DosageForm> getDosageFormNotOther();

}
