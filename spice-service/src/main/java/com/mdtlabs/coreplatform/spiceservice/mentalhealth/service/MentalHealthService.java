package com.mdtlabs.coreplatform.spiceservice.mentalhealth.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.MentalHealthDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;

/**
 * <p>
 * This is an interface to perform any actions in mentalHealth related entities.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 07, 2023
 */
public interface MentalHealthService {

    /**
     * <p>
     * Create or update a mental health details for patient.
     * </p>
     *
     * @param mentalHealth {@link MentalHealth} entity
     * @return MentalHealth {@link MentalHealth} entity
     */
    MentalHealth createOrUpdateMentalHealth(MentalHealth mentalHealth);

    /**
     * <p>
     * Get a mental health details for a patient based on type.
     * </p>
     *
     * @param requestData {@link RequestDTO} a MentalHealthRequestDTO object
     * @return MentalHealthDTO {@linkn MentalHealthDTO} entity
     */
    MentalHealthDTO getMentalHealthDetails(RequestDTO requestData);

    /**
     * <p>
     * This function creates a mental health object and updates the patient tracker with the latest
     * status if specified.
     * </p>
     *
     * @param mentalHealth       {@link MentalHealth} The mental health object that needs to be created. It may contain
     *                           information such as the patient's diagnosis, treatment plan, and progress.
     * @param patientTracker     {@link PatientTracker} The patientTracker parameter is an object that tracks the patient's
     *                           information and history, such as their medical records, appointments, and treatments. It is
     *                           likely used within the createMentalHealth method to store and update the patient's mental health
     *                           information.
     * @param updateLatestStatus {@link boolean} A boolean value that determines whether the latest status of the
     *                           patient's mental health should be updated or not
     * @return {@link MentalHealth} The return type of the method `createMentalHealth` is not specified in the given code
     * snippet
     */
    MentalHealth createMentalHealth(MentalHealth mentalHealth, PatientTracker patientTracker,
                                    boolean updateLatestStatus);

    /**
     * <p>
     * This method is used to set phq4 score.
     * </p>
     *
     * @param mentalHealth {@link MentalHealth} entity
     */
    void setPhq4Score(MentalHealth mentalHealth);

    /**
     * <p>
     * To remove mental health details based on tracker id.
     * </p>
     *
     * @param trackerId {@link long} trackerId
     */
    void removeMentalHealth(long trackerId);

}
