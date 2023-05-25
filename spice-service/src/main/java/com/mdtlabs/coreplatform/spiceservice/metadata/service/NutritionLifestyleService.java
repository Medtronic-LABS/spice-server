package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.NutritionLifestyle;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.Set;

/**
 * <p>
 * This an interface class for Nutrition Lifestyle module services.
 * </p>
 *
 * @author Shajini Varghese
 * @since Feb 03, 2023
 */
public interface NutritionLifestyleService extends GenericService<NutritionLifestyle> {

    /**
     * <p>
     * This service retrieves Nutrition Lifestyle by id.
     * </p>
     *
     * @param ids {@link Set<Long>} set of Nutrition Lifestyle ids
     * @return {@link Set<NutritionLifestyle>} Nutrition Lifestyle Set
     */
    Set<NutritionLifestyle> getNutritionLifestyleByIds(Set<Long> ids);

}