package com.mdtlabs.coreplatform.spiceservice.patient.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientPregnancyDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <p>
 * This interface handles the connection between service layer and database and
 * is responsible for database operations for PatientPregnancyDetails entity.
 * </p>
 *
 * @author Niraimathi S created on Feb 08, 2023
 */
public interface PatientPregnancyDetailsRepository extends JpaRepository<PatientPregnancyDetails, Long> {

    /**
     * <p>
     * This function finds pregnancy details of a patient based on their track ID and whether they have
     * been deleted.
     * </p>
     *
     * @param patientTrackId {@link Long} patientTrackId is a unique identifier assigned to a patient's pregnancy
     *                       track
     * @param isDeleted      {@link Boolean} isDeleted is a boolean parameter that indicates whether the patient pregnancy
     *                       details have been marked as deleted or not is given
     * @return {@link PatientPregnancyDetails} The method `findByPatientTrackIdAndIsDeleted` returns a `PatientPregnancyDetails` object
     * that matches the given `patientTrackId` and `isDeleted` parameters.
     */
    PatientPregnancyDetails findByPatientTrackIdAndIsDeleted(Long patientTrackId, Boolean isDeleted);

    /**
     * <p>
     * This function finds a patient's pregnancy details by their ID and checks if it has been deleted.
     * </p>
     *
     * @param patientPregnancyId {@link Long} This is a unique identifier for a specific patient pregnancy record in
     *                           a database is given
     * @param isDeleted          {@link Boolean} isDeleted is a boolean parameter that indicates whether the patient pregnancy
     *                           details have been marked as deleted or not is given
     * @return {@link PatientPregnancyDetails} The method is returning a PatientPregnancyDetails object that matches the given
     * patientPregnancyId and isDeleted flag is given
     */
    PatientPregnancyDetails findByIdAndIsDeleted(Long patientPregnancyId, Boolean isDeleted);

    /**
     * <p>
     * This function finds pregnancy details of a patient based on their track ID.
     * </p>
     *
     * @param patientTrackId {@link long} patientTrackId is a unique identifier for a patient's medical record or
     *                       history
     * @return {@link List<PatientPregnancyDetails>} The method `findByPatientTrackId` returns a list of `PatientPregnancyDetails` objects
     * that match the given `patientTrackId`.
     */

    List<PatientPregnancyDetails> findByPatientTrackId(long patientTrackId);
}
