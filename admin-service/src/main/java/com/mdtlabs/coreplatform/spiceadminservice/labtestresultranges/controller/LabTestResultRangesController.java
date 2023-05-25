package com.mdtlabs.coreplatform.spiceadminservice.labtestresultranges.controller;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.annotations.UserTenantValidation;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResultRange;
import com.mdtlabs.coreplatform.spiceadminservice.labtestresultranges.service.LabTestResultRangesService;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;

/**
 * <p>
 * LabTestResultRangesController class that handles HTTP requests related to lab test result ranges,
 * including creating, updating, removing, and retrieving lab test result ranges.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 7, 2023
 */
@RestController
@RequestMapping(value = "/labtest-result-ranges")
@Validated
public class LabTestResultRangesController {

    @Autowired
    private LabTestResultRangesService labTestResultRangesService;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>
     * This method is used to create a new lab test result ranges using the provided lab test result range details.
     * </p>
     *
     * @param labTestResultRangeRequestDto {@link LabTestResultRangeRequestDTO} The lab test result range that need to be
     *                               created is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with
     * the created lab test result range
     */
    @UserTenantValidation
    @PostMapping("/create")
    public SuccessResponse<List<LabTestResultRange>> addLabTestResultRanges(
            @Valid @RequestBody LabTestResultRangeRequestDTO labTestResultRangeRequestDto) {
        labTestResultRangesService.addLabTestResultRanges(labTestResultRangeRequestDto);
        return new SuccessResponse<>(SuccessCode.LAB_TEST_RESULT_RANGE_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to update an existing lab test result ranges using the provided lab test result range details.
     * </p>
     *
     * @param labTestResultRangeRequestDto {@link LabTestResultRangeRequestDTO} The lab test result range that need to be
     *                               updated is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with
     * the updated lab test result range
     */
    @UserTenantValidation
    @PutMapping("/update")
    public SuccessResponse<List<LabTestResultRange>> updateLabTestResultRanges(
            @Valid @RequestBody LabTestResultRangeRequestDTO labTestResultRangeRequestDto) {
        labTestResultRangesService.updateLabTestResultRanges(labTestResultRangeRequestDto);
        return new SuccessResponse(SuccessCode.LAB_TEST_RESULT_RANGE_UPDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to remove a lab test result range based on the provided request.
     * </p>
     *
     * @param request {@link CommonRequestDTO} The common request contains necessary information to remove
     *                                        the lab test result range is given
     * @return {@link SuccessResponse<Boolean>} The success message with the status is returned if the lab
     * test result range is deleted
     */
    @UserTenantValidation
    @PutMapping("/remove")
    public SuccessResponse<Boolean> removeLabTestResultRanges(@RequestBody CommonRequestDTO request) {
        labTestResultRangesService.removeLabTestResultRange(request.getId(), request.getTenantId());
        return new SuccessResponse<>(SuccessCode.LAB_TEST_RESULT_RANGE_DELETE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to retrieve lab test result ranges based on a given lab test result ID.
     * </p>
     *
     * @param labTestResultId the ID of a lab test result for which the lab test result ranges are
     *                        being retrieved is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with the retrieved list of lab test result range DTOs
     * and total count
     */
    @GetMapping("/details/{labTestResultId}")
    public SuccessResponse<List<LabTestResultRangeDTO>> getLabTestResultRanges(@PathVariable long labTestResultId) {
        List<LabTestResultRange> retrievedLabTestResultRanges = labTestResultRangesService
                .getLabTestResultRange(labTestResultId);
        if (!retrievedLabTestResultRanges.isEmpty()) {
            List<LabTestResultRangeDTO> labTestResultRanges = modelMapper.map(retrievedLabTestResultRanges,
                    new TypeToken<List<LabTestResultRangeDTO>>() {
                    }.getType());
            return new SuccessResponse(SuccessCode.GET_LAB_TEST_RESULT_RANGE, labTestResultRanges,
                    labTestResultRanges.size(), HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.GET_LAB_TEST_RESULT_RANGE, retrievedLabTestResultRanges, Constants.ZERO,
                HttpStatus.OK);
    }
}
