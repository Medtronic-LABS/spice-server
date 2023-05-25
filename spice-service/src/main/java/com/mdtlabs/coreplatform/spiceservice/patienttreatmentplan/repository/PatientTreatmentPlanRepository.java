package com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTreatmentPlan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * PatientTreatmentPlanRepository is a Java interface that extends the JpaRepository interface and defines methods for
 * accessing and manipulating data in the patient treatment table of a database.
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface PatientTreatmentPlanRepository extends JpaRepository<PatientTreatmentPlan, Long> {

    /**
     * <p>
     * This method is used to find a list of patient treatment plans by patient track ID and whether they have been
     * deleted, ordered by the date they were last updated.
     * </p>
     *
     * @param patientTrackId patientTrackId is used to retrieve the treatment plan for a specific patient
     *                       is given
     * @param isDeleted      The boolean value that is used to filter the results of the query based on
     *                       whether the list of patient treatment plan has been marked as deleted or not is given
     * @return {@link List<PatientTreatmentPlan>} The list of PatientTreatmentPlan that match the given patientTrackId
     * and isDeleted flag is returned
     */
    List<PatientTreatmentPlan> findByPatientTrackIdAndIsDeletedOrderByUpdatedAtDesc(
            long patientTrackId, boolean isDeleted);

    /**
     * <p>
     * This method is used to find a patient treatment plan by their track ID and tenant ID.
     * </p>
     *
     * @param id       {@link Long} The id of a patient's treatment plan is given
     *                 is used to search for a specific treatment plan associated with a patient's track id.
     * @param tenantId {@link Long} The tenantId for which the patient treatment plan is being searched is given
     * @return {@link PatientTreatmentPlan} The patient treatment plan that matches the given `id` and `tenantId` is
     * retrieved from the database
     */
    PatientTreatmentPlan findByPatientTrackIdAndTenantId(Long id, Long tenantId);

    /**
     * <p>
     * This method is used to find a patient treatment plan by its ID and checks if it has been deleted.
     * </p>
     *
     * @param id        The id of the patient treatment plan that we want to retrieve from the database is given
     * @param isDeleted The boolean value that is used to filter the results of the query based on
     *                  whether the patient treatment plan has been marked as deleted or not is given
     * @return The PatientTreatmentPlan that matches the given id and isDeleted status is retrieved from the database
     */
    PatientTreatmentPlan findByIdAndIsDeleted(long id, boolean isDeleted);

    /**
     * <p>
     * This method is used to find the most recently updated PatientTreatmentPlan that matches the given patient
     * track ID, tenant ID, and is not marked as deleted.
     * </p>
     *
     * @param id           {@link Long} The ID of the patient track for which we want to find the treatment plan
     *                     is given
     * @param tenantId     {@link Long}The ID of the tenant or organization that the patient belongs is given
     * @param booleanFalse {@link Boolean} The Boolean value that is used to filter the results of the query is given
     * @return {@link PatientTreatmentPlan} The patient treatment plan for the given id, tenant ID, and booleanFalse
     * flag`is retrieved from the database
     */
    PatientTreatmentPlan findFirstByPatientTrackIdAndTenantIdAndIsDeletedOrderByUpdatedAtDesc(Long id,
                                                                                              Long tenantId, Boolean booleanFalse);

    /**
     * <p>
     * This method is used to find a list of treatment plans for a given patient track ID.
     * </p>
     *
     * @param patientTrackId patientTrackId is used to retrieve the treatment plan for a specific patient
     *                       is given
     * @return {@link List<PatientTreatmentPlan>} The list of `PatientTreatmentPlan that match the given
     * `patientTrackId`is retrieved from the database
     */
    List<PatientTreatmentPlan> findByPatientTrackId(long patientTrackId);
}
