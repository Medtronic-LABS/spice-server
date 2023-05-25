package com.mdtlabs.coreplatform.spiceservice.patientlabtest.controller;

import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.GetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientLabTestResultRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTestResult;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.patientlabtest.service.PatientLabTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This class is the controller for the Patient Lab Test Entity. This class is responsible for receiving the api request
 * and for sending the request to the corresponding methods.
 * </p>
 *
 * @author Niraimathi S created on Feb 7, 2023
 */
@Validated
@RequestMapping(value = "/patient-labtest")
@RestController
public class PatientLabTestController {

    @Autowired
    private PatientLabTestService patientLabTestService;


    /**
     * <p>
     * This Java function creates a new patient lab test and returns a success response with a HTTP
     * status code of 201.
     * </p>
     *
     * @param requestData {@link PatientLabTestRequestDTO} It is a request object of type PatientLabTestRequestDTO which contains the
     *                    necessary information to create a new patient lab test is given
     * @return {@link SuccessResponse<PatientLabTest>} A SuccessResponse object containing a PatientLabTest object and a success code with a HTTP
     * status of CREATED is returned
     */
    @PostMapping("/create")
    @TenantValidation
    public SuccessResponse<PatientLabTest> createPatientLabTest(@Valid
                                                                @RequestBody PatientLabTestRequestDTO requestData) {
        Logger.logDebug("In PatientLabTestController, create patient lab test");
        patientLabTestService.createPatientLabTest(requestData);
        return new SuccessResponse<>(SuccessCode.PATIENT_LAB_TEST_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This is a Java function that returns a list of patient lab test results in response to a POST
     * request with a JSON payload.
     * </p>
     *
     * @param requestData {@link GetRequestDTO} It is a request body parameter of type GetRequestDTO which contains the necessary
     *                    data to retrieve a list of patient lab tests.
     * @return {@link SuccessResponse<PatientLabTestResponseDTO>} A SuccessResponse object containing a list of PatientLabTestResponseDTO objects, with a
     * success code and HTTP status code.
     */
    @PostMapping(value = "/list")
    @TenantValidation
    public SuccessResponse<PatientLabTestResponseDTO> getPatientLabTestList(@RequestBody GetRequestDTO requestData) {
        Logger.logDebug("In PatientLabTestController, get patient lab test");
        return new SuccessResponse<>(SuccessCode.GET_PATIENT_LAB_TEST_LIST,
                patientLabTestService.getPatientLabTestList(requestData), HttpStatus.OK);
    }

    /**
     * <p>
     * This is a Java function that removes a patient's lab test and returns a success response.
     * </p>
     *
     * @param requestData {@link GetRequestDTO} The parameter `requestData` is an object of type `GetRequestDTO` which is
     *                    annotated with `@RequestBody` is given
     * @return {@link SuccessResponse<String>} A SuccessResponse object with a success code and HTTP status OK is given
     */
    @PostMapping(value = "/remove")
    @TenantValidation
    public SuccessResponse<String> removePatientLabTest(@RequestBody GetRequestDTO requestData) {
        Logger.logDebug("In PatientLabTestController, remove patient lab test");
        patientLabTestService.removePatientLabTest(requestData);
        return new SuccessResponse<>(SuccessCode.PATIENT_LAB_TEST_DELETE, HttpStatus.OK);
    }

    /**
     * <p>
     * This is a Java function that reviews a patient's lab test and returns a success response.
     * </p>
     *
     * @param requestData {@link GetRequestDTO} The parameter "requestData" is an object of type GetRequestDTO which is
     *                    annotated with @RequestBody is given
     * @return {@link SuccessResponse<String>} A SuccessResponse object with a success code and HTTP status OK. The generic type of the
     * SuccessResponse object is String is returned
     */
    @PostMapping("/review")
    @TenantValidation
    public SuccessResponse<String> reviewPatientLabTest(@RequestBody GetRequestDTO requestData) {
        Logger.logDebug("In PatientLabTestController, review patient lab test");
        patientLabTestService.reviewPatientLabTest(requestData);
        return new SuccessResponse<>(SuccessCode.REVIEW_PATIENT_LAB_TEST, HttpStatus.OK);
    }

    /**
     * <p>
     * This Java function creates a patient lab test result and returns a success response.
     * </p>
     *
     * @param requestData {@link PatientLabTestResultRequestDTO} It is a request object of type PatientLabTestResultRequestDTO which contains
     *                    the necessary information to create a patient lab test result
     * @return {@link SuccessResponse<List<PatientLabTestResult>>} A SuccessResponse object containing a list of PatientLabTestResult objects is being
     * returned
     */
    @PostMapping("/result/create")
    @TenantValidation
    public SuccessResponse<List<PatientLabTestResult>> createPatientLabTestResult(
            @Valid @RequestBody PatientLabTestResultRequestDTO requestData) {
        Logger.logDebug("In PatientLabTestController, create patient lab test result");
        patientLabTestService.createPatientLabTestResult(requestData);
        return new SuccessResponse<>(SuccessCode.PATIENT_LAB_TEST_RESULT_SAVE,
                HttpStatus.CREATED);
    }

    /**
     * <p>
     * This Java function returns a SuccessResponse object containing patient lab test results based on
     * the PatientLabTestResultRequestDTO input.
     * </p>
     *
     * @param requestData {@link PatientLabTestResultRequestDTO} It is an object of type PatientLabTestResultRequestDTO which is being passed as
     *                    the request body in the POST request to the "/result/details" endpoint is given
     * @return {@link SuccessResponse<String>} A SuccessResponse object containing a success code, the result of calling the
     * getPatientLabTestResults method of the patientLabTestService object with the requestData parameter,
     * and an HTTP status code of OK is returned
     */
    @PostMapping("/result/details")
    @TenantValidation
    public SuccessResponse<String> getPatientLabTestResults(@RequestBody PatientLabTestResultRequestDTO requestData) {
        Logger.logDebug("In PatientLabTestController, get patient lab test results");
        return new SuccessResponse<>(SuccessCode.GOT_PATIENT_LAB_TEST_RESULTS,
                patientLabTestService.getPatientLabTestResults(requestData), HttpStatus.OK);
    }

    /**
     * <p>
     * This Java function searches for patient lab test results and returns a success response with the
     * results.
     * </p>
     *
     * @param requestData {@link RequestDTO} The parameter "requestData" is of type RequestDTO and is annotated with
     * @return {@link SuccessResponse<List<Map>>} A SuccessResponse object containing a list of Map objects, which represent the results
     * of a patient lab test search
     * @RequestBody
     */
    @PostMapping("/search")
    @TenantValidation
    public SuccessResponse<List<Map>> getPatientLabTest(@RequestBody RequestDTO requestData) {
        Logger.logDebug("In PatientLabTestController, search patient lab test");
        return new SuccessResponse(SuccessCode.GOT_PATIENT_LAB_TEST_RESULTS,
                patientLabTestService.getLabTest(requestData), HttpStatus.OK);
    }

    /**
     * <p>
     * This function retrieves a list of lab test results for a specific patient by their lab test ID.
     * </p>
     *
     * @param labTestId {@link Long} labTestId is a Long type variable that represents the unique identifier of a
     *                  lab test
     * @return {@link SuccessResponse<List<LabTestResultDTO>>} A SuccessResponse object containing a list of LabTestResultDTO objects, with a success
     * code and HTTP status code
     */
    @GetMapping("/result/list/{id}")
    public SuccessResponse<List<LabTestResultDTO>> getPatientLabTestResults(@PathVariable("id") Long labTestId) {
        Logger.logDebug("In PatientLabTestController, get patient lab test by id");
        return new SuccessResponse(SuccessCode.GOT_PATIENT_LAB_TEST_RESULTS,
                patientLabTestService.getLabTestResults(labTestId), HttpStatus.OK);
    }
}