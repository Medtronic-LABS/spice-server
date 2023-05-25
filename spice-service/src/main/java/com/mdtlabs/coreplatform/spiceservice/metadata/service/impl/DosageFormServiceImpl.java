package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.DosageForm;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.DosageFormRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.DosageFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * This service class contain all the business logic for dosage form module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class DosageFormServiceImpl extends GenericServiceImpl<DosageForm> implements DosageFormService {

    @Autowired
    private DosageFormRepository dosageFormRepository;

    /**
     * {@inheritDoc}
     */
    public List<DosageForm> getDosageFormNotOther() {
        if (Constants.DOSAGE_FORMS.isEmpty()) {
            Constants.DOSAGE_FORMS.addAll(dosageFormRepository
                    .findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.DOSAGE_FORMS.stream().filter(obj -> !obj.getName().equals(Constants.OTHER)).toList();
    }

}
