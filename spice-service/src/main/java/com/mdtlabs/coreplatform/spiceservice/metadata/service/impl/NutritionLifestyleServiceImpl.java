package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.NutritionLifestyle;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.common.repository.NutritionLifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.NutritionLifestyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * This class has the service implementation for retrieving nutrition Lifestyle details.
 * </p>
 *
 * @author Shajini Varghese
 * @since Feb 03, 2023
 */
@Service
public class NutritionLifestyleServiceImpl extends GenericServiceImpl<NutritionLifestyle> implements NutritionLifestyleService {

    @Autowired
    private NutritionLifestyleRepository nutritionLifestyleRepository;

    /**
     * {@inheritDoc}
     */
    public List<NutritionLifestyle> findByIsDeletedFalseAndIsActiveTrue() {
        if (Constants.NUTRITION_LIFESTYLES.isEmpty()) {
            Constants.NUTRITION_LIFESTYLES.addAll(nutritionLifestyleRepository
                    .findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.NUTRITION_LIFESTYLES;
    }

    /**
     * {@inheritDoc}
     */
    public Set<NutritionLifestyle> getNutritionLifestyleByIds(Set<Long> ids) {
        return nutritionLifestyleRepository.findByIdInAndIsDeletedFalseAndIsActiveTrue(ids);
    }


}