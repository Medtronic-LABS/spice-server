package com.mdtlabs.coreplatform.spiceservice.patient.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.EnrollmentRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.EnrollmentResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.GetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LifestyleDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientDetailDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientGetRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTrackerDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PregnancyDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Patient;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientPregnancyDetails;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;

/**
 * This is an interface to perform any actions in patient related entities.
 *
 * @author Niraimathi S created on Feb 7, 2023
 */
public interface PatientService {

    /**
     * <p>
     * This function creates a patient enrollment response based on an enrollment request.
     * </p>
     *
     * @param patient {@link EnrollmentResponseDTO} EnrollmentRequestDTO is a data transfer object that contains the information
     *                needed to enroll a patient
     * @return {@link EnrollmentResponseDTO} An object of type EnrollmentResponseDTO is being returned.
     */
    EnrollmentResponseDTO createPatient(EnrollmentRequestDTO patient);

    /**
     * <p>
     * This function retrieves patient details based on the provided request data.
     * </p>
     *
     * @param requestData {@link PatientGetRequestDTO} requestData is an object of type PatientGetRequestDTO which contains the
     *                    necessary information to retrieve the details of a patient is given
     * @return {@link PatientTrackerDTO} The method is returning a PatientTrackerDTO object which contains the details of a
     * patient based on the PatientGetRequestDTO requestData parameter.
     */
    PatientTrackerDTO getPatientDetails(PatientGetRequestDTO requestData);

    /**
     * <p>
     * This function creates a pregnancy details object using the provided request data.
     * </p>
     *
     * @param requestData {@link PregnancyDetailsDTO} requestData is an object of type PregnancyDetailsDTO that contains the
     *                    details of a pregnancy is given
     * @return {@link PregnancyDetailsDTO} The method `createPregnancyDetails` is expected to return an object of type
     * `PregnancyDetailsDTO`.
     */
    PregnancyDetailsDTO createPregnancyDetails(PregnancyDetailsDTO requestData);

    /**
     * <p>
     * The function returns pregnancy details based on the provided request data.
     * </p>
     *
     * @param requestData {@link GetRequestDTO} This is an object of type GetRequestDTO that contains the necessary
     *                    information to retrieve pregnancy details
     * @return {@link PregnancyDetailsDTO} The method `getPregnancyDetails` is returning an object of type `PregnancyDetailsDTO`.
     */
    PregnancyDetailsDTO getPregnancyDetails(GetRequestDTO requestData);

    /**
     * <p>
     * The function updates pregnancy details of a patient based on the provided request data.
     * </p>
     *
     * @param requestData {@link PregnancyDetailsDTO} requestData is an object of type PregnancyDetailsDTO which contains the
     *                    updated pregnancy details of a patient
     * @return {@link PatientPregnancyDetails} The method `updatePregnancyDetails` is returning an object of type
     * `PatientPregnancyDetails`.
     */
    PatientPregnancyDetails updatePregnancyDetails(PregnancyDetailsDTO requestData);

    /**
     * <p>
     * This function returns the basic details of a patient given a common request.
     * </p>
     *
     * @param request {@link CommonRequestDTO} CommonRequestDTO is a data transfer object that contains common request
     *                parameters such as authentication details, request ID, and other metadata required for
     *                processing the request
     * @return {@link PatientDetailDTO} A PatientDetailDTO object containing basic details of a patient is being returned.
     */
    PatientDetailDTO getPatientBasicDetails(CommonRequestDTO request);


    /**
     * <p>
     * This function updates the details of a patient and returns a PatientDetailDTO object.
     * </p>
     *
     * @param patient {@link PatientDetailDTO} The "patient" parameter is an object of type PatientDetailDTO, which contains the
     *                details of a patient
     * @return {@link PatientDetailDTO} The method `updatePatientDetails` is returning a `PatientDetailDTO` object.
     */
    PatientDetailDTO updatePatientDetails(PatientDetailDTO patient);

    /**
     * <p>
     * This function retrieves lifestyle details of a patient based on a common request.
     * </p>
     *
     * @param request {@link CommonRequestDTO} CommonRequestDTO is a data transfer object that contains common request
     *                parameters such as patient ID, start date, end date, and other relevant information needed to
     *                retrieve patient lifestyle details
     * @return {@link List<LifestyleDTO>} A list of LifestyleDTO objects containing the lifestyle details of a patient, based on
     * the information provided in the CommonRequestDTO object.
     */
    List<LifestyleDTO> getPatientLifeStyleDetails(CommonRequestDTO request);


    /**
     * <p>
     * The function returns a list of patients based on the provided patient request DTO.
     * </p>
     *
     * @param patientRequestDto {@link PatientRequestDTO} The patientRequestDto parameter is an object of type PatientRequestDTO,
     *                          which contains the information needed to retrieve a list of patients
     * @return {@link ResponseListDTO} A list of patients that match the criteria specified in the `patientRequestDto`
     * parameter
     */
    ResponseListDTO listMyPatients(PatientRequestDTO patientRequestDto);

    /**
     * <p>
     * The function searches for patients based on the provided patient request DTO and returns a
     * response list DTO.
     * </p>
     *
     * @param patientRequestDto {@link PatientRequestDTO} This is an object of type PatientRequestDTO which contains the search
     *                          criteria for finding patients
     * @return {@link ResponseListDTO} A ResponseListDTO object containing a list of patients that match the search criteria
     * specified in the PatientRequestDTO object.
     */
    ResponseListDTO searchPatients(PatientRequestDTO patientRequestDto);

    /**
     * <p>
     * The function `patientAdvanceSearch` takes a `PatientRequestDTO` object as input and returns a
     * `ResponseListDTO` object.
     * </p>
     *
     * @param patientRequestDto {@link PatientRequestDTO} This is an object of type PatientRequestDTO which contains the search
     *                          criteria for the patient advance search
     * @return {@link ResponseListDTO} A ResponseListDTO object containing a list of patients that match the search criteria
     * specified in the PatientRequestDTO object.
     */
    ResponseListDTO patientAdvanceSearch(PatientRequestDTO patientRequestDto);

    /**
     * <p>
     * The function removes patient details based on the patient request DTO.
     * </p>
     *
     * @param patient {@link PatientRequestDTO} The "patient" parameter is an object of type PatientRequestDTO, which contains
     *                the details of a patient that needs to be removed from a system or database
     * @return {@link Patient} The method `removePatientDetails` is returning an object of type `Patient` which
     * represents the patient whose details have been removed.
     */
    Patient removePatientDetails(PatientRequestDTO patient);

}
