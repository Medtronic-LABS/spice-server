package com.mdtlabs.coreplatform.spiceservice.common.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientCurrentMedication;
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
public interface PatientCurrentMedicationRepository extends JpaRepository<PatientCurrentMedication, Long> {

    /**
     * <p>
     * Get list of PatientCurrentMedication by patientTracker and isDeleted.
     * </p>
     *
     * @param patientTrackId {@link Long} patient track id
     * @return {@link List<PatientCurrentMedication>} PatientCurrentMedication entity
     */
    List<PatientCurrentMedication> findByPatientTrackIdAndIsDeleted(Long patientTrackId,
                                                                    Boolean isDeleted);

    /**
     * <p>
     * Get list of PatientCurrentMedication by patientTracker.
     * </p>
     *
     * @param patientTrackId {@link Long} patient track id
     * @return {@link List<PatientCurrentMedication>} PatientCurrentMedication entity
     */
    List<PatientCurrentMedication> findBypatientTrackId(long patientTrackId);
}
