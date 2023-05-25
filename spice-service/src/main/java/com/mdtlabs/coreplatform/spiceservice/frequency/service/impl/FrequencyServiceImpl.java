package com.mdtlabs.coreplatform.spiceservice.frequency.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.entity.spice.Frequency;
import com.mdtlabs.coreplatform.spiceservice.frequency.repository.FrequencyRepository;
import com.mdtlabs.coreplatform.spiceservice.frequency.service.FrequencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * This class implements the Frequency interface and contains actual business
 * logic to perform operations on frequency entities.
 * </p>
 *
 * @author Niraimathi
 * @since Jun 20, 2022
 */
@Service
public class FrequencyServiceImpl implements FrequencyService {

    @Autowired
    private FrequencyRepository frequencyRepository;

    /**
     * {@inheritDoc}
     */
    public Frequency addFrequency(Frequency frequency) {
        if (Objects.isNull(frequency)) {
            throw new BadRequestException(1651);
        }
        return frequencyRepository.save(frequency);
    }

    /**
     * {@inheritDoc}
     */
    public Frequency getFrequencyById(long id) {
        return findByIsDeletedFalseAndIsActiveTrue().stream().filter(frequency -> frequency.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    public Frequency getFrequencyByFrequencyNameAndType(String name, String type) {
        return findByIsDeletedFalseAndIsActiveTrue().stream().filter(
                frequency -> frequency.getName().equalsIgnoreCase(name) && frequency.getType().equalsIgnoreCase(type)).findFirst().orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    public List<Frequency> getFrequencyListByRiskLevel(String riskLevel) {
        return findByIsDeletedFalseAndIsActiveTrue().stream().filter(frequency -> (!Objects.isNull(frequency)
                && !Objects.isNull(frequency.getRiskLevel()) && frequency.getRiskLevel().equals(riskLevel))).toList();
    }

    /**
     * {@inheritDoc}
     */
    public List<Frequency> findByIsDeletedFalseAndIsActiveTrue() {
        if (Constants.FREQUENCY_LIST.isEmpty()) {
            Constants.FREQUENCY_LIST.addAll(frequencyRepository.findByIsDeletedFalseAndIsActiveTrueOrderByDisplayOrderAsc());
        }
        return Constants.FREQUENCY_LIST;
    }

    /**
     * {@inheritDoc}
     */
    public List<Frequency> getFrequencies() {
        return findByIsDeletedFalseAndIsActiveTrue();
    }

    /**
     * {@inheritDoc}
     */
    public Frequency getFrequencyByType(String type) {
        return findByIsDeletedFalseAndIsActiveTrue().stream().filter(frequency -> frequency.getType().equalsIgnoreCase(type))
                .findFirst().orElseThrow(() -> new DataNotFoundException(1654));
    }

}
