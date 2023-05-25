package com.mdtlabs.coreplatform.spiceservice.common.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.NutritionLifestyle;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * This is the repository which acts as link between server side and database.
 * This class is used to perform all the complaints module action in database.
 * In query annotation (nativeQuery = true), the below query perform like SQL.
 * Otherwise it performs like HQL.
 * </p>
 *
 * @author Karthick Murugesan
 * @since Jun 30, 2022
 */
@Repository
public interface NutritionLifestyleRepository extends GenericRepository<NutritionLifestyle> {

    /**
     * <p>
     * This method used to get the list of lifestyle.
     * </p>
     *
     * @return  {@link List<NutritionLifestyle>} List of NutritionLifestyle
     */
    List<NutritionLifestyle> findByIsDeletedFalseAndIsActiveTrue();

    /**
     * <p>
     * This method used to get the list of lifestyle.
     * </p>
     *
     * @param ids {@link Set<Long>} ids
     * @return {@link Set<NutritionLifestyle>} Set of NutritionLifestyle
     */
    Set<NutritionLifestyle> findByIdInAndIsDeletedFalseAndIsActiveTrue(Set<Long> ids);

}
