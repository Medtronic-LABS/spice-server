package com.mdtlabs.coreplatform.spiceadminservice.labtestresultranges.service;

import java.util.List;
import java.util.Map;

import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResultRange;

/**
 * <p>
 * This is a Java interface for a service that maintains CRUD operations for lab test result ranges.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 7, 2023
 */
public interface LabTestResultRangesService {

    /**
     * <p>
     * This method is used to create a new lab test result ranges using the provided lab test result range details.
     * </p>
     *
     * @param labTestResultRangeDto {@link LabTestResultRangeRequestDTO} The lab test result range that need to be
     *                              created is given
     * @return {@link List<LabTestResultRange>} The created list of lab test result ranges is returned
     */
    List<LabTestResultRange> addLabTestResultRanges(LabTestResultRangeRequestDTO labTestResultRangeDto);

    /**
     * <p>
     * This method is used to update a new lab test result ranges using the provided lab test result range details.
     * </p>
     *
     * @param labTestResultRangeRequestDto {@link LabTestResultRangeRequestDTO} The lab test result range that
     *                                     need to be updated is given
     * @return {@link List<LabTestResultRange>} The updated list of lab test result ranges is returned
     */
    List<LabTestResultRange> updateLabTestResultRanges(
            LabTestResultRangeRequestDTO labTestResultRangeRequestDto);

    /**
     * <p>
     * This method is used to remove a lab test result range based on the provided ID and tenant ID.
     * </p>
     *
     * @param id       The ID for which the lab test result range that need to be removed is given
     * @param tenantId {@link Long} The tenant ID for which the lab test result range that need to be removed is given
     * @return
     */
    boolean removeLabTestResultRange(long id, Long tenantId);

    /**
     * <p>
     * This method is used to retrieve lab test result ranges based on a given lab test result ID.
     * </p>
     *
     * @param labTestResultId the ID of a lab test result for which the lab test result ranges are
     *                        being retrieved is given
     * @return {@link List<LabTestResultRange>} Returns the retrieved list of lab test result ranges
     * for the given lab test result ID
     */
    List<LabTestResultRange> getLabTestResultRange(long labTestResultId);

    /**
     * <p>
     * This method is used to retrieve lab test result ranges based on a given lab test result ID.
     * </p>
     *
     * @param labTestResultIds {@link List<Long>} The list of ids for which the
     *                         lab test result ranges need to be retrieved is given
     * @return {@link List<LabTestResultRange>} Returns a map of the retrieved list of lab test result
     * range DTOs for the given list of lab test result IDs
     */
    Map<Long, List<LabTestResultRangeDTO>> getLabTestResultRange(List<Long> labTestResultIds);
}
