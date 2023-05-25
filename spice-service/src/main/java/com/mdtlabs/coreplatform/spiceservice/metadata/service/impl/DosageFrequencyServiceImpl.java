package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.DosageFrequency;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.DosageFrequencyRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.DosageFrequencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * This service class contain all the business logic for DosageFrequency module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class DosageFrequencyServiceImpl extends GenericServiceImpl<DosageFrequency> implements DosageFrequencyService {

    @Autowired
    private DosageFrequencyRepository dosageFrequencyRepository;

    /**
     * {@inheritDoc}
     */
    public List<DosageFrequency> getDosageFrequency(Sort sort) {
        if (Constants.DOSAGE_FREQUENCIES.isEmpty()) {
            Constants.DOSAGE_FREQUENCIES.addAll(dosageFrequencyRepository.findAll(sort));
        }
        return Constants.DOSAGE_FREQUENCIES;
    }

}
