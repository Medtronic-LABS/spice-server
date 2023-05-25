package com.mdtlabs.coreplatform.spiceservice.patienttransfer.controller;

import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTransferRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientTransferUpdateRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTransfer;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.patienttransfer.service.PatientTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/patient-transfer")
@Validated
public class PatientTransferController {

    @Autowired
    private PatientTransferService patientTransferService;

    /**
     * <p>
     * This Java function creates a patient transfer using the data provided in the request body.
     * </p>
     *
     * @param patientTransferDto {@link PatientTransferRequestDTO} It is a request object of type PatientTransferRequestDTO that contains
     *                           the information needed to create a new patient transfer
     * @return {@link SuccessResponse<PatientTransfer>} A SuccessResponse object containing a success code and HTTP status code.
     */
    @PostMapping(value = "/create")
    public SuccessResponse<PatientTransfer> createPatientTransfer(
            @Valid @RequestBody PatientTransferRequestDTO patientTransferDto) {
        patientTransferService.createPatientTransfer(patientTransferDto);
        return new SuccessResponse<>(SuccessCode.PATIENT_TRANSFER_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This Java function validates a patient transfer request and returns a success response with a
     * map of strings.
     * </p>
     *
     * @param requestDTO {@link RequestDTO}  requestDTO is an object of type RequestDTO that is being passed as a request
     *                   body in the POST request
     * @return {@link SuccessResponse<Map<String, String>>} A SuccessResponse object with a generic type of Map<String, String> is being returned.
     * The SuccessResponse object contains a success code and an HTTP status code.
     */
    @PostMapping(value = "/validate")
    @TenantValidation
    public SuccessResponse<Map<String, String>> validatePatientTransfer(
            @RequestBody RequestDTO requestDTO) {
        patientTransferService.validatePatientTransfer(requestDTO);
        return new SuccessResponse<>(SuccessCode.PATIENT_TRANSFER_VALIDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This is a Java POST API endpoint that returns a list of patient transfers based on the request
     * parameters.
     * </p>
     *
     * @param requestDTO {@link RequestDTO} The parameter `requestDTO` is an object of type `RequestDTO` which is being
     *                   passed as the request body in the POST request is given
     * @return {@link SuccessResponse<Map<String, Object>>} A SuccessResponse object containing a map of String keys and Object values, which
     * represents the list of patient transfers obtained from the patientTransferService.getPatientTransferList()
     * method is returned
     */
    @PostMapping(value = "/list")
    @TenantValidation
    public SuccessResponse<Map<String, Object>> getPatientTransferList(
            @RequestBody RequestDTO requestDTO) {
        return new SuccessResponse<>(SuccessCode.PATIENT_TRANSFER_LIST,
                patientTransferService.getPatientTransferList(requestDTO), HttpStatus.OK);
    }

    /**
     * <p>
     * This Java function updates a patient transfer record and returns a success response with the
     * updated transfer status.
     * </p>
     *
     * @param patientTransferDto {@link PatientTransferUpdateRequestDTO} patientTransferDto is an object of type
     *                           PatientTransferUpdateRequestDTO which is annotated with @RequestBody is given
     * @return {@link SuccessResponse<PatientTransfer>} A SuccessResponse object containing a PatientTransfer object with updated information,
     * along with a success code and HTTP status code is returned
     */
    @PostMapping(value = "/update")
    public SuccessResponse<PatientTransfer> updatePatientTransfer(
            @Valid @RequestBody PatientTransferUpdateRequestDTO patientTransferDto) {
        patientTransferService.updatePatientTransfer(patientTransferDto);
        return new SuccessResponse<>(SuccessCode.PATIENT_TRANSFER_UPDATE, HttpStatus.OK, patientTransferDto.getTransferStatus().toString().toLowerCase());
    }

    /**
     * <p>
     * This is a Java function that returns the count of patient transfers in a SuccessResponse object.
     * </p>
     *
     * @param requestDTO requestDTO {@link RequestDTO} is an object of type RequestDTO which is being passed as a request
     *                   body in the POST request is given
     * @return {@link SuccessResponse<PatientTransfer>} A SuccessResponse object containing the patient transfer count, with a success code and
     * HTTP status OK is returned
     */
    @PostMapping(value = "/notification-count")
    @TenantValidation
    public SuccessResponse<PatientTransfer> getPatientTransferCount(
            @RequestBody RequestDTO requestDTO) {
        return new SuccessResponse<>(SuccessCode.PATIENT_TRANSFER_COUNT,
                patientTransferService.getPatientTransferCount(requestDTO), HttpStatus.OK);
    }
}
