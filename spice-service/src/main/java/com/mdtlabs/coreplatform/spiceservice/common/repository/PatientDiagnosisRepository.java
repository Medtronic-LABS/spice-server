package com.mdtlabs.coreplatform.spiceservice.common.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientDiagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the PatientCurrentMedication module
 * action in database. In query annotation (nativeQuery = true) the below query
 * perform like SQL. Otherwise its perform like HQL default value for
 * nativeQuery FALSE
 * </p>
 *
 * @author Karthick Murugesan created on Feb 06, 2023
 */
@Repository
public interface PatientDiagnosisRepository extends JpaRepository<PatientDiagnosis, Long> {

    /**
     * These methods find a patientDiagnosis by patientTrackId, isActive and isDeleted Fields.
     *
     * @param patientTrackId {@link Long} patient track id
     * @param isDeleted      {@link Boolean} true or false
     * @param isActive       {@link Boolean} true or false
     * @return PatientDiagnosis Entity.
     */
    PatientDiagnosis findByPatientTrackIdAndIsActiveAndIsDeleted(Long patientTrackId,
                                                                 Boolean isActive, Boolean isDeleted);

    /**
     * <p>
     * This methods finds a patientDiagnosis by patientTrackId and isDeleted Fields.
     * </p>
     *
     * @param patientTrackId {@link Long} patient track id
     * @param isDeleted      {@link Boolean} true or false
     * @return {@link List<PatientDiagnosis>} PatientDiagnosis Entity.
     */
    List<PatientDiagnosis> findByPatientTrackIdAndIsDeleted(Long patientTrackId,
                                                            Boolean isDeleted);

    /**
     * <p>
     * Get list of PatientDiagnosis by patientTracker.
     * </p>
     *
     * @param patientTrackId {@link Long} patient track id
     * @return {@link List<PatientDiagnosis>} PatientDiagnosis entity
     */
    List<PatientDiagnosis> findByPatientTrackId(long patientTrackId);

}
