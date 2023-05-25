package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import java.util.Map;

import com.mdtlabs.coreplatform.common.model.entity.spice.FrequencyType;
import com.mdtlabs.coreplatform.common.service.GenericService;

/**
 * <p>
 * This an interface class for FrequencyType module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface FrequencyTypeService extends GenericService<FrequencyType> {
    
    /**
     * <p>
     * Gets a all frequencyTypes based on cultureId
     * </p>
     *
     * @param {@link Long} cultureId
     * @return {@link Map<String, String> map of frequencyType and its cultureValue
     */
    Map<String, String> getFrequencyTypesByCultureId(Long cultureId);
}
