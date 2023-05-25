package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.Unit;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This interface is responsible for performing database operations between
 * server and Unit entity.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
@Repository
public interface UnitRepository extends GenericRepository<Unit> {

    /**
     * <p>
     * Gets list of Units using Name.
     * </p>
     *
     * @param name {@link String} unit name
     * @return {@link List<Unit>} List of Unit entities
     */
    List<Unit> findByNameNotLike(String name);

    /**
     * <p>
     * Gets list of Units using tyoe.
     * </p>
     *
     * @param type {@link String} unit type
     * @return {@link List<Unit>} List of Unit entities
     */
	List<Unit> findByType(String type);

}
