package com.mdtlabs.coreplatform.spiceservice.frequency.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.Frequency;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This is an interface to perform any actions in frequency entities.
 *
 * @author Niraimathi
 * @since Jun 20, 2022
 */
@Service
public interface FrequencyService {

    /**
     * Retrieves frequency using frequencyId.
     *
     * @param id {@link long} frequency id
     * @return {@link Frequency} Entity.
     */
    Frequency getFrequencyById(long id);

    /**
     * Gets a frequency based on risk level value.
     *
     * @param riskLevel {@link String} level of risk
     * @return {@link List<Frequency>} Frequency Entity
     */
    List<Frequency> getFrequencyListByRiskLevel(String riskLevel);

    /**
     * Retrieves a frequency based on its name and type.
     *
     * @param name {@link String} frequency name
     * @param type {@link String} frequency type
     * @return {@link Frequency} Entity.
     */
    Frequency getFrequencyByFrequencyNameAndType(String name, String type);

    /**
     * Gets list of frequency based on its deleted and active status.
     *
     * @return {@link List<Frequency>} List of Frequency
     */
    List<Frequency> findByIsDeletedFalseAndIsActiveTrue();

    /**
     * <p>
     * Gets list of frequency.
     * </p>
     *
     * @return {@link List<Frequency>} List of Frequency
     */
    List<Frequency> getFrequencies();

    /**
     * <p>
     * Gets frequency by type.
     * </p>
     *
     * @param type {@link String} type
     * @return {@link Frequency} Frequency entity.
     */
    Frequency getFrequencyByType(String type);
}
