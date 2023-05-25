package com.mdtlabs.coreplatform.spiceadminservice.medication.service;

import java.util.List;
import java.util.Map;

import com.mdtlabs.coreplatform.common.model.dto.spice.MedicationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Medication;


/**
 * <p>
 * MedicationService is a Java interface for a medication service that defines methods for performing CRUD
 * operations on medication entities.
 * </p>
 *
 * @author Niraimathi created on Jun 30, 2022
 */
public interface MedicationService {

    /**
     * <p>
     * This method is used to create a new medication using the list of provided medications.
     * </p>
     *
     * @param medication {@link List<Medication>} The list of medications that need to create is given
     * @return {@link List<Medication>} The medication is created for the given list of medications is returned
     */
    List<Medication> addMedication(List<Medication> medication);

    /**
     * <p>
     * This method is used to update a existing medication using the medication Dto.
     * </p>
     *
     * @param medication {@link MedicationDTO} The medication Dto that contains necessary information
     *                   to update medication is given
     * @return {@link List<Medication>} The medication is created for the given list of medications is returned
     */
    Medication updateMedication(Medication medication);

    /**
     * <p>
     * This method is to get the medication based on given request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information like ID to
     *                   get the medication is given
     * @return {@link Medication} The retrieved medication for the given request is returned
     */
    Medication getMedicationById(RequestDTO requestDto);

    /**
     * <p>
     * This method is used to get list of medication DTOs using the given request.
     * </p>
     *
     * @param requestObject {@link RequestDTO} The request contains necessary information
     *                      to get the list of medication DTOs is given
     * @return {@link Map} A map containing the retrieved list of medication DTOs and total count for
     * the given request
     */
    Map<String, Object> getAllMedications(RequestDTO requestObject);

    /**
     * <p>
     * This method is used to remove a medication based on the provided request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The common request contains necessary information to remove
     *                   the medication is given
     * @return {@link Boolean} A boolean value is returned if the medication is deleted
     */
    Boolean deleteMedicationById(RequestDTO requestDto);

    /**
     * <p>
     * This method is used to get list of medication DTOs using the given request.
     * </p>
     *
     * @param requestObject {@link RequestDTO} The request contains necessary information
     *                      to get the list of medication DTOs is given
     * @return {@link List<Medication>} The retrieved list of medication DTOs and total count is returned
     */
    List<Medication> searchMedications(RequestDTO requestObject);

    /**
     * <p>
     * This method is to validate the medication based on given medication details.
     * </p>
     *
     * @param medication {@link Medication} The medication to validate is given
     * @return {@link Boolean} The boolean value is returned after validating the medication
     */
    Boolean validateMedication(Medication medication);

    /**
     * <p>
     * This method is used to get list of other medication DTOs by provided country ID.
     * </p>
     *
     * @param countryId The country ID for which other medication DTOs are to be retrieved is given
     * @return {@link Medication} The non-deleted medication for the given ID and tenant ID is returned
     */
    Medication getOtherMedication(long countryId);
}
