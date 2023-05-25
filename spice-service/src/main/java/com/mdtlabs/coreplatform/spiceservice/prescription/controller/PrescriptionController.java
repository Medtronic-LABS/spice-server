package com.mdtlabs.coreplatform.spiceservice.prescription.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.exception.SpiceValidation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.FillPrescriptionRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.FillPrescriptionResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionHistoryResponse;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PrescriptionHistory;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.prescription.service.PrescriptionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * PrescriptionController class defines the endpoints for managing prescription requests and history, including
 * adding/updating prescriptions, listing prescriptions, removing prescriptions, and retrieving prescription
 * refill history.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 10, 2023
 */
@RestController
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    /**
     * <p>
     * This method is used to update a prescription request with a signature file.
     * </p>
     *
     * @param prescriptionRequest {@link String} The prescriptionRequest contains a JSON representation of a
     *                            PrescriptionRequestDTO object is given
     * @param signatureFile       {@link MultipartFile} The signatureFile representing the signature file uploaded by
     *                            the user is given
     * @return {@link SuccessResponse<String>} The SuccessResponse object containing a success code and HTTP status code
     * is returned
     */
    @PostMapping("/prescription/update")
    public SuccessResponse<String> addPrescription(
            @RequestParam(Constants.PRESCRIPTION_REQUEST) String prescriptionRequest,
            @RequestParam(Constants.SIGNATURE_FILE) MultipartFile signatureFile) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PrescriptionRequestDTO prescriptionRequestDto = mapper.readValue(prescriptionRequest,
                    PrescriptionRequestDTO.class);
            prescriptionRequestDto.setSignatureFile(signatureFile);
            createOrUpdatePrescriptionRequest(prescriptionRequestDto);
        } catch (JsonMappingException mappingException) {
            Logger.logError(mappingException);
            throw new SpiceValidation(1513, mappingException.getMessage());
        } catch (JsonProcessingException processingException) {
            throw new SpiceValidation(1513, processingException.getMessage());
        }
        return new SuccessResponse<>(SuccessCode.PRESCRIPTION_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to create or update a prescription request and logs any IOExceptions that occur.
     * </p>
     *
     * @param prescriptionRequestDto {@link PrescriptionRequestDTO} The PrescriptionRequest contains information about
     *                               a prescription request, such as the patient's name, the medication being
     *                               prescribed, and the dosage instructions is given
     */
    private void createOrUpdatePrescriptionRequest(PrescriptionRequestDTO prescriptionRequestDto) {
        try {
            prescriptionService.createOrUpdatePrescription(prescriptionRequestDto);
        } catch (IOException iOException) {
            Logger.logError(iOException);
            throw new SpiceValidation(1513, iOException.getMessage());
        }
    }

    /**
     * <p>
     * This method is used to list prescriptions and returns a success response with the prescription list and its size.
     * </p>
     *
     * @param prescriptionListRequestDto {@link RequestDTO} The prescription list request contains the necessary
     *                                   parameters required to fetch a list of prescriptions is given
     * @return {@link SuccessResponse<List<PrescriptionDTO>>} The SuccessResponse object containing a list of
     * PrescriptionDTO objects, the size of the list, and an HTTP status code is returned
     */
    @PostMapping("/prescription/list")
    @TenantValidation
    public SuccessResponse<List<PrescriptionDTO>> listPrescription(@RequestBody RequestDTO prescriptionListRequestDto) {
        List<PrescriptionDTO> prescriptionList = prescriptionService.getPrescriptions(prescriptionListRequestDto);
        return new SuccessResponse(SuccessCode.PRESCRIPTION_GET, prescriptionList, prescriptionList.size(),
                HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to list prescription history data and returns a success response with the data.
     * </p>
     *
     * @param prescriptionListRequestDto {@link RequestDTO} The prescription list request contains the necessary
     *                                   information to retrieve a list of prescription history data is given
     * @return {@link SuccessResponse<PrescriptionHistoryResponse>} The SuccessResponse object containing a
     * PrescriptionHistoryResponse object, with a success code of PRESCRIPTION_HISTORY_GET and an HTTP status of OK is
     * returned
     */
    @PostMapping("/prescription-history/list")
    @TenantValidation
    public SuccessResponse<PrescriptionHistoryResponse> listPrescriptionHistoryData(
            @RequestBody RequestDTO prescriptionListRequestDto) {
        PrescriptionHistoryResponse prescriptionList = prescriptionService
                .listPrescriptionHistoryData(prescriptionListRequestDto);
        return new SuccessResponse<>(SuccessCode.PRESCRIPTION_HISTORY_GET,
                prescriptionList, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to remove a prescription based on the request body and returns a success response.
     * </p>
     *
     * @param prescriptionListRequestDto {@link RequestDTO} The request contains the data related to the prescription
     *                                   that needs to be removed is given
     * @return {@link SuccessResponse<String>} Returns a success message and status after removing the prescription
     * for the given request
     */
    @PutMapping("/prescription/remove")
    @TenantValidation
    public SuccessResponse<String> removePrescription(@RequestBody RequestDTO prescriptionListRequestDto) {
        prescriptionService.removePrescription(prescriptionListRequestDto);
        return new SuccessResponse<>(SuccessCode.PRESCRIPTION_DELETE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to list fill prescriptions based on a search request and returns a success response with the
     * prescription list or a "no data" message.
     * </p>
     *
     * @param searchRequestDto {@link SearchRequestDTO} The search request containing search term and pagination
     *                         information to get prescription history is given
     * @return {@link SuccessResponse<List<FillPrescriptionResponseDTO>>} The SuccessResponse containing a list of
     * FillPrescriptionResponseDTO objects, along with a success code, the size of the list, and an HTTP status code.
     * If the prescriptionList is not empty, it will contain the list of FillPrescriptionResponseDTO objects, otherwise
     * it will return a SuccessResponse object with a success code indicating that there is no data available.
     */
    @PostMapping("/fill-prescription/list")
    @TenantValidation
    public SuccessResponse<List<FillPrescriptionResponseDTO>> listFillPrescription(
            @RequestBody SearchRequestDTO searchRequestDto) {
        List<FillPrescriptionResponseDTO> prescriptionList = prescriptionService.getFillPrescriptions(searchRequestDto);
        if (!prescriptionList.isEmpty()) {
            return new SuccessResponse(SuccessCode.FILL_PRESCRIPTION_GET, prescriptionList, prescriptionList.size(),
                    HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.FILL_PRESCRIPTION_GET, Constants.NO_DATA_LIST, Constants.ZERO, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to update the fill-prescription data along with its associated tables like
     * fill-prescription history, prescription and prescription history.
     * </p>
     *
     * @param fillPrescriptionRequestDto {@link FillPrescriptionRequestDTO} The fillPrescriptionRequest contains the
     *                                   data required to update a prescription fill request is given
     * @return {@link SuccessResponse<String>} Returns a success message and status after updating the fill prescription
     * for the given request
     */
    @PostMapping("/fill-prescription/update")
    @TenantValidation
    public SuccessResponse<String> updateFillPrescription(
            @RequestBody FillPrescriptionRequestDTO fillPrescriptionRequestDto) {
        prescriptionService.updateFillPrescription(fillPrescriptionRequestDto);
        return new SuccessResponse<>(SuccessCode.FILL_PRESCRIPTION_UPDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to retrieve a list of prescription refill history based on a search request and returns a
     * success response with the list or a "no data" message.
     * </p>
     *
     * @param searchRequestDto {@link SearchRequestDTO} The search request containing search term and pagination
     *                         information to get prescription history is given
     * @return {@link SuccessResponse<List<PrescriptionHistory>>} The SuccessResponse object containing a list of
     * PrescriptionHistory objects, along with a success code, the size of the list, and an HTTP status code.
     * If the prescriptionList is empty, a SuccessResponse object with a success code indicating no data is returned.
     */
    @PostMapping(path = "/prescription/refill-history")
    @TenantValidation
    public SuccessResponse<List<PrescriptionHistory>> getReFillPrescriptionHistory(
            @RequestBody SearchRequestDTO searchRequestDto) {
        List<PrescriptionHistory> prescriptionList = prescriptionService.getRefillPrescriptionHistory(searchRequestDto);
        if (!prescriptionList.isEmpty()) {
            return new SuccessResponse(SuccessCode.REFILL_PRESCRIPTION_GET, prescriptionList, prescriptionList.size(),
                    HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.REFILL_PRESCRIPTION_GET, Constants.NO_DATA_LIST, Constants.ZERO, HttpStatus.OK);
    }
}
