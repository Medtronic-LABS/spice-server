package com.mdtlabs.coreplatform.spiceadminservice.labtest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.annotations.UserTenantValidation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTest;
import com.mdtlabs.coreplatform.spiceadminservice.labtest.service.LabTestService;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;

/**
 * <p>
 * Lab test controller class defines the endpoints for creating, retrieving, updating,
 * and deleting lab tests, as well as searching for lab tests and retrieving
 * lab test results.
 * </p>
 *
 * @author Niraimathi S created on Feb 09, 2023
 */
@RestController
@RequestMapping(value = "/labtest")
@Validated
public class LabTestController {

    @Autowired
    private LabTestService labTestService;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>
     * This method is used to create a new lab test result ranges using the provided lab test result range details.
     * </p>
     *
     * @param labTestDto {@link LabTestDTO} The lab test that need to be created is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with
     * the created lab test
     */
    @UserTenantValidation
    @PostMapping("/create")
    public SuccessResponse<LabTest> addLabTest(@Valid @RequestBody LabTestDTO labTestDto) {
        Logger.logInfo("In Labtest Controller, creating a new labtest");
        labTestService.addLabTest(modelMapper.map(labTestDto, LabTest.class));
        return new SuccessResponse<>(SuccessCode.LAB_TEST_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to retrieve lab test based on a given lab test result ID.
     * </p>
     *
     * @param requestObject {@link RequestDTO} The request contains necessary information to get the list of
     *                      lab test DTOs is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with the retrieved list of lab test DTOs
     * and total count
     */
    @UserTenantValidation
    @PostMapping("/list")
    public SuccessResponse<List<LabTestDTO>> getAllLabTests(@RequestBody RequestDTO requestObject) {
        Logger.logInfo("In Labtest Controller, getting labtest list");
        ResponseListDTO response = labTestService.getAllLabTests(requestObject);
        if ((Objects.isNull(response.getTotalCount()) || 0L == response.getTotalCount())) {
            return new SuccessResponse(SuccessCode.GET_LAB_TESTS, response.getData(), HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.GET_LAB_TESTS, response.getData(), response.getTotalCount(),
                HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to search a lab test using the provided request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information to get the list of
     *                   lab test DTOs is given
     * @return {@link List} The list of map containing lab test is returned
     */
    @PostMapping("/search")
    public List<Map> searchLabtests(@RequestBody RequestDTO requestDto) {
        Logger.logInfo("In Labtest Controller, searching labtest");
        List<Map> labTests = labTestService.searchLabTests(requestDto);
        return Objects.isNull(labTests) ? new ArrayList<>() : labTests;
    }

    /**
     * <p>
     * This method is used to remove a lab test based on the provided request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information to remove
     *                   the lab test is given
     * @return {@link SuccessResponse<Boolean>} The success message with the status is returned if the lab
     * test is deleted
     */
    @UserTenantValidation
    @DeleteMapping("/remove")
    public SuccessResponse<Boolean> removeLabTest(@RequestBody RequestDTO requestDto) {
        Logger.logInfo("In Labtest Controller, removing a labtest");
        labTestService.removeLabTest(requestDto, true);
        return new SuccessResponse<>(SuccessCode.LAB_TEST_DELETE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to update an existing lab test using the provided lab test details.
     * </p>
     *
     * @param labTestDto {@link LabTestDTO} The lab test that need to be updated is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with
     * the updated lab test
     */
    @UserTenantValidation
    @PatchMapping("/update")
    public SuccessResponse<LabTest> updateLabTest(@Valid @RequestBody LabTestDTO labTestDto) {
        Logger.logInfo("In Labtest Controller, updating labtest list");
        labTestService.updateLabTest(modelMapper.map(labTestDto, LabTest.class));
        return new SuccessResponse<>(SuccessCode.LAB_TEST_UPDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is to get the lab test based on given request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information like ID to
     *                   get the lab test is given
     * @return {@link SuccessResponse<LabTest>} Returns a success message and status with the retrieved lab test
     */
    @UserTenantValidation
    @PostMapping("/details")
    public SuccessResponse<LabTest> getLabTestById(@RequestBody RequestDTO requestDto) {
        Logger.logInfo("In Labtest Controller, getting a labtest details");
        return new SuccessResponse<>(SuccessCode.GET_LAB_TEST, labTestService.getLabTestById(requestDto),
                HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to retrieve lab test using labTestId.
     * </p>
     *
     * @param labTestId The ID of the lab test that needs to be retrieved is given
     * @return {@link List<LabTestResultDTO>} The list of lab test results is returned for the
     */
    @GetMapping("/labtest-result/{id}")
    public List<LabTestResultDTO> getLabTestResultsById(@PathVariable(value = Constants.ID) long labTestId) {
        Logger.logInfo("In Labtest Controller, getting a labtest results");
        return labTestService.getLabTestResultsById(labTestId);
    }

    /**
     * <p>
     * This method is used to retrieve a lab test by its name.
     * </p>
     *
     * @param searchRequestDto {@link SearchRequestDTO} The search request contains necessary information
     *                         to get the list of accounts is given
     * @return {@link ResponseEntity<LabTest>} The lab test for the given name is returned with success message with status
     */
    @PostMapping("/patient-labtest/get-by-name")
    public ResponseEntity<LabTest> getLabTestbyName(@RequestBody SearchRequestDTO searchRequestDto) {
        Logger.logInfo("In Labtest Controller, getting a labtest");
        return ResponseEntity.ok()
                .body(labTestService.getLabTestByName(searchRequestDto.getSearchTerm(),
                        searchRequestDto.getCountryId()));
    }

    /**
     * <p>
     * This method is used to retrieve a list of lab tests by their IDs.
     * </p>
     *
     * @param labTestIds {@link Set<Long>} The IDs of the lab tests that need to be retrieved is given
     * @return {@link ResponseEntity<List>} The status is returned with the retrieved lab test
     */
    @PostMapping("/patient-labtest/get-list-by-ids")
    public ResponseEntity<List<LabTest>> getLabTestsById(@RequestBody Set<Long> labTestIds) {
        Logger.logInfo("In Labtest Controller, getting labtest list");
        return ResponseEntity.ok().body(labTestService.getLabTestsById(labTestIds));
    }
}
