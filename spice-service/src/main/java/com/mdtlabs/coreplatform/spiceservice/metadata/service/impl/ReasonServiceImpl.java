package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Reason;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.ReasonRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * This service class contain all the business logic for Reason module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class ReasonServiceImpl extends GenericServiceImpl<Reason> implements ReasonService {

    @Autowired
    private ReasonRepository reasonRepository;

    /**
     * {@inheritDoc}
     */
    public List<Reason> getReasons() {
        if (Constants.REASONS.isEmpty()) {
            Constants.REASONS.addAll(reasonRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.REASONS;
    }
}
