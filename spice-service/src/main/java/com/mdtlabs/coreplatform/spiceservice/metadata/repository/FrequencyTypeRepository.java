package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.FrequencyType;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;

/**
 * <p>
 * This interface is responsible for performing database operations between
 * server and FrequencyType entity.
 * </p>
 *
 * @author VigneshKumar
 */
@Repository
public interface FrequencyTypeRepository extends GenericRepository<FrequencyType> {
    
    List<FrequencyType> findByIsDeletedFalse();
}
