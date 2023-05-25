package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.DosageFrequency;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * This interface is responsible for performing database operations between
 * server and DosageFrequency entity.
 * </p>
 *
 * @author Niraimathi S
 */
@Repository
public interface DosageFrequencyRepository extends GenericRepository<DosageFrequency> {

}
