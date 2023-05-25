package com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.NutritionLifestyleResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientNutritionLifestyleRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientNutritionLifestyle;

import java.util.List;

/**
 * <p>
 * This is an interface to perform any actions in Patient Nutrition Lifestyle
 * related entities.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 06, 2023
 */
public interface PatientNutritionLifestyleService {

    /**
     * <p>
     * This function adds a patient's nutrition and lifestyle information based on the provided request
     * DTO.
     * </p>
     *
     * @param patientNutritionLifestyleRequestDTO {@link PatientNutritionLifestyleRequestDTO} This is an object of type
     *                                            PatientNutritionLifestyleRequestDTO which contains the information needed to create a new
     *                                            patient nutrition lifestyle record is given
     * @return {@link PatientNutritionLifestyle} The method "addPatientNutritionLifestyle" is returning an object of type
     * "PatientNutritionLifestyle".
     */
    PatientNutritionLifestyle addPatientNutritionLifestyle(
            PatientNutritionLifestyleRequestDTO patientNutritionLifestyleRequestDTO);

    /**
     * <p>
     * This function updates a patient's nutrition and lifestyle information based on the provided
     * request data.
     * </p>
     *
     * @param patientNutritionLifestyleRequestDTO {@link List<PatientNutritionLifestyle>} It is an object of type
     *                                            PatientNutritionLifestyleRequestDTO which contains the updated information for a patient's
     *                                            nutrition and lifestyle
     * @return {@link PatientNutritionLifestyleRequestDTO} A List of PatientNutritionLifestyle objects is being returned.
     */
    List<PatientNutritionLifestyle> updatePatientNutritionLifestyle(
            PatientNutritionLifestyleRequestDTO patientNutritionLifestyleRequestDTO);

    /**
     * <p>
     * This function returns a list of NutritionLifestyleResponseDTO objects for a given patient
     * request.
     * </p>
     *
     * @param request {@link RequestDTO} The request parameter is an object of type RequestDTO which contains the
     *                necessary information required to retrieve the patient's nutrition and lifestyle list
     * @return {@link List<NutritionLifestyleResponseDTO>} A list of NutritionLifestyleResponseDTO objects for a patient's nutrition and lifestyle
     * information, based on the provided RequestDTO.
     */
    List<NutritionLifestyleResponseDTO> getPatientNutritionLifeStyleList(RequestDTO request);

    /**
     * <p>
     * The function updates the nutrition and lifestyle view of a patient based on a common request.
     * </p>
     *
     * @param request {@link CommonRequestDTO} CommonRequestDTO is a data transfer object that contains information about the
     *                request being made to update a patient's nutrition and lifestyle view
     * @return {@link boolean} A boolean value is being returned.
     */
    boolean updatePatientNutritionLifestyleView(CommonRequestDTO request);

    /**
     * <p>
     * The function removes a patient's nutrition and lifestyle information.
     * </p>
     *
     * @param patientNutritionLifestyle {@link PatientNutritionLifestyle} The parameter "patientNutritionLifestyle" is an object of the
     *                                  class "PatientNutritionLifestyle"
     * @return {@link boolean} A boolean value is being returned.
     */
    boolean removePatientNutritionLifestyle(PatientNutritionLifestyle patientNutritionLifestyle);

    /**
     * <p>
     * Get the count of reviewed nutrition lifestyle.
     * </p>
     *
     * @param patientTrackId {@link Long} patientTrackId
     * @return int {@link int} count
     */
    int getNutritionLifestyleReviewedCount(Long patientTrackId);

    /**
     * <p>
     * The function removes nutrition and lifestyle data associated with a given tracker ID.
     * </p>
     *
     * @param trackId {@link long} The trackId parameter is a unique identifier for a tracker that is used to remove
     *                a nutrition lifestyle associated with it.
     */
    void removeNutritionLifestyleByTrackerId(long trackId);
}
