package com.mdtlabs.coreplatform.spiceadminservice.labtest.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTest;

/**
 * <p>
 * LabTestService is an interface for a lab test service in Java. It defines several methods for creating,
 * retrieving, updating, and deleting lab tests, as well as searching for lab tests and
 * validating lab test details.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface LabTestService {

    /**
     * <p>
     * This method is used to create a new lab test result ranges using the provided lab test result range details.
     * </p>
     *
     * @param labTest {@link LabTest} The lab test that need to be created is given
     * @return {@link LabTest} The lab test is updated for the given lab test details and returned
     */
    LabTest addLabTest(LabTest labTest);

    /**
     * <p>
     * This method is used to retrieve lab test based on a given lab test result ID.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information to get the list of
     *                   lab tests DTOs is given
     * @return {@link ResponseListDTO} A response with the retrieved list of lab test DTOs and total count
     */
    ResponseListDTO getAllLabTests(RequestDTO request);

    /**
     * <p>
     * This method is used to search a lab test using the provided request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information to get the list of
     *                   lab test is given
     * @return {@link List} The list of map containing lab test is returned
     */
    List<Map> searchLabTests(RequestDTO request);

    /**
     * <p>
     * This method is used to remove a lab test based on the provided request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information to remove
     *                   the lab test is given
     * @param status     The status of the lab test to be removed is given
     * @return A boolean value is returned indicating whether the lab test is successfully deleted or not
     */
    boolean removeLabTest(RequestDTO requestDto, boolean status);

    /**
     * <p>
     * This method is used to update a new lab test using the provided lab test details.
     * </p>
     *
     * @param labTest {@link LabTest} The lab test that need to be updated is given
     * @return {@link LabTest} The lab test is updated for the given lab test details and returned
     */
    LabTest updateLabTest(LabTest labTest);

    /**
     * <p>
     * This method is to get the lab test based on given request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information like ID to
     *                   get the lab test is given
     * @return {@link LabTest} The retrieved lab test for the given ID is returned
     */
    LabTest getLabTestById(RequestDTO request);

    /**
     * <p>
     * This method is used to validate a lab test based on the provided lab test details.
     * </p>
     *
     * @param labTest {@link LabTest} The request contains necessary information to validate
     *                the lab test is given
     */
    void validateLabTest(LabTest labTest);

    /**
     * <p>
     * This method is used to retrieve lab test using labTestId.
     * </p>
     *
     * @param labTestId The ID of the lab test that needs to be retrieved is given
     * @return {@link List<LabTestResultDTO>} The list of lab test results is returned for the
     */
    List<LabTestResultDTO> getLabTestResultsById(long labTestId);

    /**
     * <p>
     * This method is used to retrieve a lab test by its name.
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to
     *                   filter the results is given
     * @param countryId  The ID of the country associated with the lab test that
     *                   is being searched is given
     * @return {@link LabTest} The retrieved lab test for the given search term
     * and country ID is returned
     */
    LabTest getLabTestByName(String searchTerm, long countryId);

    /**
     * <p>
     * This method is used to retrieve a list of lab tests by their IDs.
     * </p>
     *
     * @param uniqueLabTestIds {@link Set<Long>} The IDs of the lab tests that need to be retrieved is given
     * @return {@link List<LabTest>} The retrieved list of lab tests for the given set of unique IDs is returned
     */
    List<LabTest> getLabTestsById(Set<Long> uniqueLabTestIds);
}
