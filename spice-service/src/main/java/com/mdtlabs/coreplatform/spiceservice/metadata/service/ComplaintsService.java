package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.Complaints;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * This an interface class for complaints module you can implemented this class
 * in any class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface ComplaintsService extends GenericService<Complaints> {

    /**
     * <p>
     * This service findBy IsDeletedFalse And IsActiveTrue.
     * </p>
     *
     * @return {@link List<Complaints>} Complaints List
     */
    List<Complaints> findByIsDeletedFalseAndIsActiveTrue();

    /**
     * <p>
     * This service retrieves complaints by id.
     * </p>
     *
     * @param ids {@link Set<Long>} set of complaint ids
     * @return {@link Set<Complaints>} Complaints Set
     */
    Set<Complaints> getComplaintsByIds(Set<Long> ids);

    /**
     * <p>
     * This service retrieves complaints by id's.
     * </p>
     *
     * @return {@link List<Complaints>} Complaints List
     */
    List<Complaints> getCompliantList();

}
