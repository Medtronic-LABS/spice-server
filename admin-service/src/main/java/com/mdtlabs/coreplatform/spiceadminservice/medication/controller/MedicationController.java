package com.mdtlabs.coreplatform.spiceadminservice.medication.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.annotations.UserTenantValidation;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OtherMedicationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Medication;
import com.mdtlabs.coreplatform.spiceadminservice.medication.service.MedicationService;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;

/**
 * <p>
 * MedicationController class that defines a REST API for managing medications, including creating,
 * updating, retrieving, and deleting medication records.
 * </p>
 *
 * @author Niraimathi created on Jun 30, 2022
 */
@RestController
@RequestMapping("/medication")
@Validated
public class MedicationController {

    @Autowired
    private MedicationService medicationService;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>
     * This method is used to create a new medication using the list of provided medications.
     * </p>
     *
     * @param medications {@link List<Medication>} The list of medications that need to create is given
     * @return {@link SuccessResponse<List>} Returns a success message and status after creating the medication
     */
    @PostMapping("/create")
    @UserTenantValidation
    public SuccessResponse<List<MedicationDTO>> addMedication(@Valid @RequestBody List<Medication> medications) {
        medicationService.addMedication(medications);
        return new SuccessResponse<>(SuccessCode.MEDICATION_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to update a existing medication using the medication Dto.
     * </p>
     *
     * @param medicationDto {@link MedicationDTO} The medication Dto that contains necessary information
     *                      to update medication is given
     * @return {@link SuccessResponse<Medication>} Returns a success message and status after updating
     * the medication
     */
    @UserTenantValidation
    @PutMapping("/update")
    public SuccessResponse<Medication> updateMedication(@Valid @RequestBody MedicationDTO medicationDto) {
        medicationService.updateMedication(modelMapper.map(medicationDto, Medication.class));
        return new SuccessResponse<>(SuccessCode.MEDICATION_UPDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is to get the medication based on given request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information like ID to
     *                   get the medication is given
     * @return {@link SuccessResponse<Medication>} Returns a success message and status with the retrieved medication
     */
    @GetMapping("/details")
    public SuccessResponse<Medication> getMedicationById(@RequestBody RequestDTO requestDto) {
        return new SuccessResponse<>(SuccessCode.GET_MEDICATION,
                medicationService.getMedicationById(requestDto), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get list of medication DTOs using the given request.
     * </p>
     *
     * @param requestObject {@link RequestDTO} The request contains necessary information
     *                      to get the list of medication DTOs is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with the retrieved
     * list of medication DTOs and total count
     */
    @UserTenantValidation
    @PostMapping("/list")
    public SuccessResponse<List<MedicationDTO>> getAllMedications(@RequestBody RequestDTO requestObject) {
        Map<String, Object> medications = medicationService.getAllMedications(requestObject);
        List<Medication> medicationList = medications.containsKey(Constants.MEDICATION_LIST)
                ? (List<Medication>) medications.get(Constants.MEDICATION_LIST)
                : new ArrayList<>();
        long totalMedicationCount = medications.containsKey(Constants.TOTAL_COUNT)
                ? Long.parseLong(medications.get(Constants.TOTAL_COUNT).toString())
                : 0;
        List<MedicationDTO> medicationDTOs = new ArrayList<>();
        if (!medicationList.isEmpty()) {
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            medicationDTOs = modelMapper.map(medicationList, new TypeToken<List<MedicationDTO>>() {
            }.getType());
        }
        return new SuccessResponse(SuccessCode.GET_MEDICATIONS, medicationDTOs, totalMedicationCount,
                HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to remove a medication based on the provided request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The common request contains necessary information to remove
     *                   the medication is given
     * @return {@link SuccessResponse<Boolean>} The success message with the status is returned if the medication
     * is deleted
     */
    @UserTenantValidation
    @DeleteMapping("/remove")
    public SuccessResponse<Boolean> deleteMedicationById(@RequestBody RequestDTO requestDto) {
        medicationService.deleteMedicationById(requestDto);
        return new SuccessResponse<>(SuccessCode.MEDICATION_STATUS_UPDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get list of medication DTOs using the given request.
     * </p>
     *
     * @param requestObject {@link RequestDTO} The request contains necessary information
     *                      to get the list of medication DTOs is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with the retrieved
     * list of medication DTOs and total count
     */
    @PostMapping("/search")
    public SuccessResponse<List<MedicationDTO>> searchMedications(@RequestBody RequestDTO requestObject) {
        List<Medication> medications = medicationService.searchMedications(requestObject);
        if (!medications.isEmpty()) {
            List<MedicationDTO> medicationDTOs = modelMapper.map(medications, new TypeToken<List<MedicationDTO>>() {
            }.getType());
            return new SuccessResponse(SuccessCode.GET_MEDICATIONS, medicationDTOs, medicationDTOs.size(),
                    HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.GET_MEDICATIONS, Constants.NO_DATA_LIST, 0, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is to validate the medication based on given medication details.
     * </p>
     *
     * @param medicationDto {@link MedicationDTO} The medication to validate is given
     * @return {@link SuccessResponse<Boolean>} Returns a success message and status after validating the medication
     */
    @UserTenantValidation
    @PostMapping("/validate")
    public SuccessResponse<Boolean> validateMedication(@RequestBody MedicationDTO medicationDto) {
        medicationService.validateMedication(modelMapper.map(medicationDto, Medication.class));
        return new SuccessResponse<>(SuccessCode.VALIDATE_MEDICATION, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get list of other medication DTOs by provided country ID.
     * </p>
     *
     * @param countryId The country ID for which other medication DTOs are to be retrieved is given
     * @return {@link ResponseEntity<OtherMedicationDTO>} Returns a success message and status with the retrieved
     * list of other medication DTOs
     */
    @GetMapping("/other-medication/{countryId}")
    public ResponseEntity<OtherMedicationDTO> getOtherMedications(@PathVariable(Constants.COUNTRY_ID) long countryId) {
        Medication medication = medicationService.getOtherMedication(countryId);
        if (Objects.isNull(medication)) {
            throw new DataNotAcceptableException(12008);
        }
        OtherMedicationDTO otherMedicationDto = modelMapper.map(medication, OtherMedicationDTO.class);
        return ResponseEntity.ok().body(otherMedicationDto);
    }
}
