package com.mdtlabs.coreplatform.spiceservice.patient.controller;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.EnrollmentRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.EnrollmentResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LifestyleDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MyPatientListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientDetailDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientGetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTrackerDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PregnancyDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchPatientListDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Patient;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientPregnancyDetails;
import com.mdtlabs.coreplatform.common.util.spice.EnrollmentInfo;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.patient.service.PatientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * This is the controller class for the patient entity. It maintains the
 * request and response for the Patient Entity.
 *
 * @author Niraimathi S created on Feb 7, 2023
 */
@RestController
@RequestMapping(value = "/patient")
public class PatientController {

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private PatientService patientService;

    /**
     * <p>
     * This function creates a new patient enrollment and returns a success response with the created
     * patient's information.
     * </p>
     *
     * @param patient {@link EnrollmentRequestDTO} The parameter "patient" is of type EnrollmentRequestDTO and is annotated with
     * @return {@link SuccessResponse<EnrollmentResponseDTO>} A SuccessResponse object containing an EnrollmentResponseDTO object, with a success code
     * of PATIENT_SAVE and HTTP status code of CREATED
     * @RequestBody
     */
    @PostMapping("/enrollment")
    @TenantValidation
    public SuccessResponse<EnrollmentResponseDTO> createPatient(
            @Validated(EnrollmentInfo.class) @RequestBody EnrollmentRequestDTO patient) {
        Logger.logInfo("In Patient controller, creating a new patient");
        return new SuccessResponse<>(SuccessCode.PATIENT_SAVE, patientService.createPatient(patient),
                HttpStatus.CREATED);
    }

    /**
     * <p>
     * This is a Java function that returns a success response containing patient details based on the
     * request data received.
     * </p>
     *
     * @param requestData {@link PatientGetRequestDTO} requestData is an object of type PatientGetRequestDTO which is being passed
     *                    as a request body in the POST request to the "/details" endpoint
     * @return {@link SuccessResponse<PatientTrackerDTO>} A SuccessResponse object containing a PatientTrackerDTO object, with a success code and
     * HTTP status of OK.
     */
    @PostMapping("/details")
    public SuccessResponse<PatientTrackerDTO> getPatientDetails(@RequestBody PatientGetRequestDTO requestData) {
        Logger.logInfo("In Patient controller, getting a patient details");
        return new SuccessResponse<>(SuccessCode.GET_PATIENT, patientService.getPatientDetails(requestData),
                HttpStatus.OK);
    }

    /**
     * <p>
     * This Java function creates a patient's pregnancy details and returns a success response with the
     * created data.
     * </p>
     *
     * @param requestData {@link PregnancyDetailsDTO} requestData is an object of type PregnancyDetailsDTO which is being passed as
     *                    a request body in the POST request to the "/pregnancy-details/create" endpoint
     * @return {@link SuccessResponse<PregnancyDetailsDTO>} A SuccessResponse object containing a PregnancyDetailsDTO object, with a success code
     * and HTTP status code
     */
    @PostMapping("/pregnancy-details/create")
    @TenantValidation
    public SuccessResponse<PregnancyDetailsDTO> createPregnancyDetails(@RequestBody PregnancyDetailsDTO requestData) {
        Logger.logInfo("In Patient controller, creating a patient's pregnancy details");
        return new SuccessResponse<>(SuccessCode.PATIENT_PREGNANCY_SAVE,
                patientService.createPregnancyDetails(requestData), HttpStatus.CREATED);
    }

    /**
     * <p>
     * This Java function retrieves a patient's pregnancy details and returns a success response with the
     * details.
     * </p>
     *
     * @param requestData {@link GetRequestDTO} It is a request body parameter of type GetRequestDTO which contains the
     *                    necessary information to retrieve a patient's pregnancy details.
     * @return {@link SuccessResponse<PregnancyDetailsDTO>} A SuccessResponse object containing a PregnancyDetailsDTO object, with a success code of
     * GET_PATIENT_PREGNANCY and an HTTP status of OK.
     */
    @PostMapping("/pregnancy-details/info")
    @TenantValidation
    public SuccessResponse<PregnancyDetailsDTO> getPregnancyDetails(@RequestBody GetRequestDTO requestData) {
        Logger.logInfo("In Patient controller, getting a patient's pregnancy details");
        return new SuccessResponse<>(SuccessCode.GET_PATIENT_PREGNANCY, patientService.getPregnancyDetails(requestData),
                HttpStatus.OK);
    }

    /**
     * <p>
     * This Java function updates a patient's pregnancy details and returns a success response.
     * </p>
     *
     * @param requestData {@link PregnancyDetailsDTO} The parameter "requestData" is of type PregnancyDetailsDTO, which is a data
     *                    transfer object that contains the updated pregnancy details of a patient
     * @return {@link SuccessResponse<PatientPregnancyDetails>} A SuccessResponse object containing the updated PatientPregnancyDetails object, with a
     * success code and HTTP status of OK.
     */
    @PostMapping("/pregnancy-details/update")
    @TenantValidation
    public SuccessResponse<PatientPregnancyDetails> updatePregnancyDetails(
            @RequestBody PregnancyDetailsDTO requestData) {
        Logger.logInfo("In Patient controller, updating a patient's pregnancy details");
        return new SuccessResponse<>(SuccessCode.UPDATE_PATIENT_PREGNANCY,
                patientService.updatePregnancyDetails(requestData), HttpStatus.OK);
    }

    /**
     * <p>
     * This Java function returns a list of patients based on the provided request DTO and validates
     * the tenant before returning a success response.
     * </p>
     *
     * @param patientRequestDto {@linkPatientRequestDTO} It is a DTO (Data Transfer Object) that contains the request parameters
     *                          for retrieving a list of patients
     * @return {@link SuccessResponse<List<MyPatientListDTO>>} A SuccessResponse object containing a list of MyPatientListDTO objects, which is
     * obtained by calling the listMyPatients method of the patientService object with the
     * patientRequestDto parameter
     */
    @PostMapping("/list")
    @TenantValidation
    public SuccessResponse<List<MyPatientListDTO>> getMyPatientsList(@RequestBody PatientRequestDTO patientRequestDto) {
        Logger.logInfo("In Patient controller, getting patient list");
        ResponseListDTO response = patientService.listMyPatients(patientRequestDto);
        return !Objects.isNull(response.getTotalCount())
                ? new SuccessResponse(SuccessCode.SEARCH_PATIENTS, response.getData(), response.getTotalCount(), HttpStatus.OK)
                : new SuccessResponse(SuccessCode.SEARCH_PATIENTS, response.getData(), HttpStatus.OK);
    }

    /**
     * <p>
     * This is a Java function that searches for patients based on the given request and returns a
     * success response with the search results.
     * </p>
     *
     * @param patientRequestDto {@link PatientRequestDTO} It is a DTO (Data Transfer Object) that contains the search criteria
     *                          for searching patients
     * @return {@link SuccessResponse<List<SearchPatientListDTO>>} The method is returning a SuccessResponse object that contains a list of
     * SearchPatientListDTO objects, which are the results of a patient search operation
     */
    @PostMapping("/search")
    @TenantValidation
    public SuccessResponse<List<SearchPatientListDTO>> searchPatients(
            @RequestBody PatientRequestDTO patientRequestDto) {
        Logger.logInfo("In Patient controller, searching a patient");
        ResponseListDTO response = patientService.searchPatients(patientRequestDto);
        return !Objects.isNull(response.getTotalCount())
                ? new SuccessResponse(SuccessCode.SEARCH_PATIENTS, response.getData(), response.getTotalCount(), HttpStatus.OK)
                : new SuccessResponse(SuccessCode.SEARCH_PATIENTS, response.getData(), HttpStatus.OK);
    }

    /**
     * <p>
     * This function searches for patients globally or by country based on the provided request
     * parameters and returns a list of patient data along with a success response.
     * </p>
     *
     * @param patientRequestDto {@link PatientRequestDTO} It is an object of type PatientRequestDTO which is received as a request
     *                          body in the POST mapping
     * @return {@link SuccessResponse<List<SearchPatientListDTO>>} A SuccessResponse object containing a list of SearchPatientListDTO objects, which are the
     * result of a patient search based on the provided PatientRequestDTO object
     */
    @PostMapping("/advance-search/country")
    @TenantValidation
    public SuccessResponse<List<SearchPatientListDTO>> getCountryWisePatientsWithAdvanceSearch(
            @RequestBody PatientRequestDTO patientRequestDto) {
        Logger.logInfo("In Patient controller, searching patients globally");
        if (Objects.isNull(patientRequestDto.getOperatingUnitId())) {
            patientRequestDto.setGlobally(Constants.BOOLEAN_TRUE);
        }
        ResponseListDTO response = patientService.patientAdvanceSearch(patientRequestDto);
        return !Objects.isNull(response.getTotalCount())
                ? new SuccessResponse(SuccessCode.SEARCH_PATIENTS, response.getData(), response.getTotalCount(), HttpStatus.OK)
                : new SuccessResponse(SuccessCode.SEARCH_PATIENTS, response.getData(), HttpStatus.OK);
    }

    /**
     * <p>
     * This Java function searches for patients with advanced search criteria and returns a list of
     * patient data along with a success response.
     * </p>
     *
     * @param patientRequestDto {@link PatientRequestDTO} It is a DTO (Data Transfer Object) that contains the search criteria
     *                          for searching patients
     * @return {@link SuccessResponse<List<SearchPatientListDTO>>} A SuccessResponse object containing a list of SearchPatientListDTO objects, which are
     * the result of a patient search using advanced search criteria specified in the PatientRequestDTO
     * object passed in the request body
     */
    @PostMapping("/advance-search/site")
    @TenantValidation
    public SuccessResponse<List<SearchPatientListDTO>> getPatientsWithAdvanceSearch(
            @RequestBody PatientRequestDTO patientRequestDto) {
        Logger.logInfo("In Patient controller, searching patient site wise");
        ResponseListDTO response = patientService.patientAdvanceSearch(patientRequestDto);
        return !Objects.isNull(response.getTotalCount())
                ? new SuccessResponse(SuccessCode.SEARCH_PATIENTS, response.getData(), response.getTotalCount(), HttpStatus.OK)
                : new SuccessResponse(SuccessCode.SEARCH_PATIENTS, response.getData(), HttpStatus.OK);
    }

    /**
     * <p>
     * This Java function retrieves a patient's basic details and returns a success response with the
     * details and HTTP status code.
     * </p>
     *
     * @param request {@Link CommonRequestDTO} The request parameter is an object of type CommonRequestDTO which is annotated
     *                with @RequestBody is given
     * @return {@link SuccessResponse<PatientDetailDTO>} A SuccessResponse object containing a PatientDetailDTO object, a SuccessCode enum value,
     * and an HTTP status code is given
     */
    @PostMapping("/basic-details")
    @TenantValidation
    public SuccessResponse<PatientDetailDTO> getPatientBasicDetails(@RequestBody CommonRequestDTO request) {
        Logger.logInfo("In Patient controller, getting a patient's basic details");
        return new SuccessResponse<>(SuccessCode.PATIENT_BASIC_DETAILS, patientService.getPatientBasicDetails(request),
                HttpStatus.OK);
    }

    /**
     * <p>
     * This Java function updates a patient's details and returns a success response with the updated
     * details.
     * </p>
     *
     * @param request {@link PatientDetailDTO} The request parameter is an object of type PatientDetailDTO which is being passed
     *                as the request body in the PUT request to update a patient's details
     * @return {@link SuccessResponse<PatientDetailDTO>} A SuccessResponse object containing a PatientDetailDTO object, with a success code and
     * HTTP status OK.
     */
    @PutMapping("/update")
    public SuccessResponse<PatientDetailDTO> updatePatient(@RequestBody PatientDetailDTO request) {
        Logger.logInfo("In Patient controller, updating a patient details");
        return new SuccessResponse<>(SuccessCode.PATIENT_UPDATE, patientService.updatePatientDetails(request),
                HttpStatus.OK);
    }

    /**
     * <p>
     * This is a Java function that retrieves a patient's lifestyle details and returns them in a
     * SuccessResponse object.
     * </p>
     *
     * @param request {@link CommonRequestDTO} The request parameter is an object of type CommonRequestDTO which is being passed
     *                as the request body in the POST request to the "/lifestyle/details" endpoint
     * @return {@link SuccessResponse<List<LifestyleDTO>>} A SuccessResponse object containing a list of LifestyleDTO objects, with a success code
     * and HTTP status OK.
     */
    @PostMapping("/lifestyle/details")
    @TenantValidation
    public SuccessResponse<List<LifestyleDTO>> getPatientLifeStyleDetails(@RequestBody CommonRequestDTO request) {
        Logger.logInfo("In Patient controller, getting patient lifestyle details");
        return new SuccessResponse(SuccessCode.PATIENT_LIFESTYLE_DETAILS,
                patientService.getPatientLifeStyleDetails(request), HttpStatus.OK);
    }

    /**
     * <p>
     * This Java function removes a patient's details and returns a success response with the removed
     * patient's information.
     * </p>
     *
     * @param request {@link PatientRequestDTO} The request parameter is of type PatientRequestDTO and is annotated with
     * @return {@link SuccessResponse<PatientDetailDTO>} A SuccessResponse object containing a PatientDetailDTO object representing the removed
     * patient, with a success code and HTTP status OK.
     * @RequestBody, which means that it is expected to contain the details of a patient that needs to
     * be removed
     */
    @PostMapping("/remove")
    @TenantValidation
    public SuccessResponse<PatientDetailDTO> removePatient(@RequestBody PatientRequestDTO request) {
        Patient patient = patientService.removePatientDetails(request);
        PatientDetailDTO removedPatient = mapper.map(patient, PatientDetailDTO.class);
        return new SuccessResponse<>(SuccessCode.PATIENT_REMOVE, removedPatient, HttpStatus.OK);
    }

}
