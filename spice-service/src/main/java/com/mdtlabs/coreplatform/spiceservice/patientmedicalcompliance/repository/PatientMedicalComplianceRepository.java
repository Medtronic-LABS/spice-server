package com.mdtlabs.coreplatform.spiceservice.patientmedicalcompliance.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalCompliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the patient medical compliance
 * module action in database.
 * In query annotation (nativeQuery = true) the below query perform like SQL.
 * Otherwise its perform like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface PatientMedicalComplianceRepository extends JpaRepository<PatientMedicalCompliance, Long> {

    /**
     * <p>
     * Get list of PatientMedicalCompliance by patientTracker.
     * </p>
     *
     * @param patientTrackId {@link Long} patient track id
     * @return {@link  List<PatientMedicalCompliance>} PatientMedicalCompliance entity
     */
    List<PatientMedicalCompliance> findByPatientTrackIdAndIsDeleted(Long patientTrackId,
                                                                    Boolean isDeleted);

    /**
     * <p>
     * This function finds a list of patient medical compliance records based on a given patient track
     * ID.
     * </p>
     *
     * @param patientTrackId {@link long} patientTrackId is a unique identifier for a patient's medical record or
     *                       history
     * @return {@link List<PatientMedicalCompliance>} The method `findByPatientTrackId` is returning a list of `PatientMedicalCompliance`
     * objects that match the given `patientTrackId`.
     */
    List<PatientMedicalCompliance> findByPatientTrackId(long patientTrackId);

}
