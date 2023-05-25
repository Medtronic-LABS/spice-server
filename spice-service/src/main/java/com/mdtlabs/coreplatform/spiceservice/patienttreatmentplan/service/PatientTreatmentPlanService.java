package com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * PatientTreatmentPlanService is a Java interface for managing CRUD (Create, Read, Update, Delete)
 * operations for PatientTreatmentPlan customizations. It defines methods for creating, retrieving, updating, and
 * removing PatientTreatmentPlan customizations.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface PatientTreatmentPlanService {

    /**
     * <p>
     * This method is used to retrieve the treatment plan for a patient with a specific track ID.
     * </p>
     *
     * @param patientTrackId {@link Long} patientTrackId is used to retrieve the treatment plan for a specific patient
     *                       is given
     * @return {@link PatientTreatmentPlan} The PatientTreatmentPlan for the given patientTrackId is returned
     */
    PatientTreatmentPlan getPatientTreatmentPlan(Long patientTrackId);

    /**
     * <p>
     * This method is used to retrieve the details of a patient's treatment plan based on their ID.
     * </p>
     *
     * @param id {@link Long} The id is used to retrieve the details of a particular PatientTreatmentPlan from a
     *           database or other data source is given
     * @return {@link PatientTreatmentPlan} The PatientTreatmentPlan for the given id is returned
     */
    PatientTreatmentPlan getPatientTreatmentPlanDetails(Long id);

    /**
     * <p>
     * This method is used to retrieve the treatment plan details for a patient with a given ID,
     * cardiovascular disease risk level, and tenant ID.
     * </p>
     *
     * @param id           {@link Long} The ID of the patient treatment plan is given
     * @param cvdRiskLevel {@link String} The cvdRiskLevel of the patient is given
     * @param tenantId     {@link Long} The tenantId is used to ensure that the correct data is being
     *                     accessed for the specific tenant or organization is given
     * @return {@link PatientTreatmentPlan} The patient treatment plan for the given id, cvdRiskLevel, and tenantId
     * is returned
     */
    PatientTreatmentPlan getPatientTreatmentPlanDetails(Long id, String cvdRiskLevel, Long tenantId);

    /**
     * <p>
     * This method is used to create a provisional treatment plan for a patient based on their cardiovascular disease
     * risk level and tenant ID.
     * </p>
     *
     * @param patientTracker {@link PatientTracker} The patientTracker contains information about the patient's medical
     *                       history, current health status, and any ongoing treatments or medications is given
     * @param cvdRiskLevel   {@link String} The cvdRiskLevel of the patient is given
     * @param tenantId       {@link Long} The tenantId is used to ensure that the provisional treatment plan is
     *                       created for the correct tenant or organization is given
     * @return {@link List<Map<String, String>>} The list of map of provisional treatment plan for the given
     * patientTracker, cvdRiskLevel, and tenantId is returned
     */
    List<Map<String, String>> createProvisionalTreatmentPlan(PatientTracker patientTracker,
                                                             String cvdRiskLevel, Long tenantId);

    /**
     * <p>
     * This method is used to return a date for the follow-up of a treatment plan based on the frequency name and type.
     * </p>
     *
     * @param frequencyName {@link String} The name of the treatment plan frequency. For example,
     *                      "Weekly", "Monthly", "Quarterly", etc. is given
     * @param frequencyType {@link String} The frequencyType represents the type of frequency for the treatment
     *                      plan follow-up. It could be "days", "weeks", "months", or "years".is given
     * @return {@link Date} The date represents the follow-up date for a treatment plan based on the given frequency
     * name and frequency type is returned
     */
    Date getTreatmentPlanFollowupDate(String frequencyName, String frequencyType);

    /**
     * <p>
     * The method is used to update the treatment plan data for a patient.
     * </p>
     *
     * @param patientTreatmentPlan {@link PatientTreatmentPlan} The PatientTreatmentPlan contains information about a
     *                             patient's treatment plan. such as the patient's diagnosis, prescribed medications,
     *                             recommended therapies, and any other relevant medical information. is given
     * @return Returns boolean if the treatment plan is updated
     */
    boolean updateTreatmentPlanData(PatientTreatmentPlan patientTreatmentPlan);

    /**
     * <p>
     * This method is used to return the next follow-up date for a patient based on their tracking ID
     * and the frequency of follow-up.
     * </p>
     *
     * @param patientTrackId patientTrackId is used to track the patient's progress and history over time is given
     * @param frequencyName  {@link String}The frequencyName is used to determine the interval between
     *                       follow-up appointments for the patient is given
     * @return {@link Date}The next follow-up date for a patient with the given patientTrackId and frequencyName
     * is returned.
     */
    Date getNextFollowUpDate(long patientTrackId, String frequencyName);

    /**
     * <p>
     * The method is used to remove a treatment plan for a patient identified by a tracker ID.
     * </p>
     *
     * @param trackerId The trackerId is used to remove the patient treatment plan from the database is given
     */
    void removePatientTreatmentPlan(long trackerId);
}
