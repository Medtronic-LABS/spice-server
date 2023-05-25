package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.PhysicalExamination;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * This an interface class for physical examination module you can implemented
 * this class in any class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface PhysicalExaminationService {

    /**
     * <p>
     * This service retrieves Physical Examinations.
     * <p>
     *
     * @return {@link List<PhysicalExamination>} Physical Examination List
     */
    List<PhysicalExamination> getPhysicalExamination();

    /**
     * <p>
     * This service retrieves Physical Examination by id.
     * <p>
     *
     * @param ids {@link Set<Long>} set of Physical Examination ids
     * @return {@link Set<PhysicalExamination>} Physical Examination Set
     */
    Set<PhysicalExamination> getPhysicalExaminationByIds(Set<Long> ids);

}
