package com.mdtlabs.coreplatform.spiceservice.patienttransfer.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTransferRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTransferUpdateRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;

import java.util.Map;

public interface PatientTransferService {

    /**
     * <p>
     * This function creates a patient transfer request using a PatientTransferRequestDTO object.
     * </p>
     *
     * @param patientTransferDto {@link PatientTransferRequestDTO} The parameter "patientTransferDto" is of type
     *                           PatientTransferRequestDTO, which is likely a data transfer object (DTO) that contains
     *                           information about a patient transfer request is given
     */
    void createPatientTransfer(PatientTransferRequestDTO patientTransferDto);

    /**
     * <p>
     * This function returns a map of patient transfer counts based on the provided request DTO.
     * </p>
     *
     * @param requestDTO {@link RequestDTO} RequestDTO is an object that contains information needed to retrieve patient
     *                   transfer count is given
     * @return {@link Map<String, Long>} A Map object is being returned, where the keys are of type String and the values are of
     * type Long is returned
     */
    Map<String, Long> getPatientTransferCount(RequestDTO requestDTO);

    /**
     * <p>
     * The function returns a map of patient transfer information based on a request object.
     * </p>
     *
     * @param requestDTO {@link RequestDTO} RequestDTO is an object that contains the necessary information to make a
     *                   request for patient transfer list is given
     * @return {@link Map<String, Object>} A Map object containing a list of patient transfers, where the keys are strings and the
     * values are objects is returned
     */
    Map<String, Object> getPatientTransferList(RequestDTO requestDTO);

    /**
     * <p>
     * The function validates a patient transfer request.
     * </p>
     *
     * @param requestDTO {@link RequestDTO} The parameter "requestDTO" is an object of type RequestDTO that contains
     *                   information related to a patient transfer request is given
     */
    void validatePatientTransfer(RequestDTO requestDTO);

    /**
     * <p>
     * This function updates a patient transfer request with the information provided in the
     * PatientTransferUpdateRequestDTO object.
     * </p>
     *
     * @param patientTransferDto {@link PatientTransferUpdateRequestDTO} The parameter "patientTransferDto" is an object of type
     *                           PatientTransferUpdateRequestDTO, which contains the information needed to update a patient
     *                           transfer is given 
     */
    void updatePatientTransfer(PatientTransferUpdateRequestDTO patientTransferDto);

    /**
     * <p>
     * The function removes a patient transfer identified by a tracker ID.
     * </p>
     *
     * @param trackerId {@link long} The trackerId parameter is a unique identifier for a patient transfer that
     *                  needs to be removed from the system is given
     */
    void removePatientTransfer(long trackerId);
}
