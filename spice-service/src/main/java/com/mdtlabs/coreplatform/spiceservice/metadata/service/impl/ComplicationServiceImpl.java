package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complication;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.ComplicationRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ComplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * This service class contain all the business logic for complication module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class ComplicationServiceImpl extends GenericServiceImpl<Complication> implements ComplicationService {

    @Autowired
    private ComplicationRepository complicationRepository;

    /**
     * {@inheritDoc}
     */
    public List<Complication> getComplications() {
        if (Constants.COMPLICATIONS.isEmpty()) {
            Constants.COMPLICATIONS.addAll(complicationRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.COMPLICATIONS;
    }

}
