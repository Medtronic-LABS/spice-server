package com.mdtlabs.coreplatform.spiceservice.common.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLifestyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This is the repository which acts as link between server side and database.
 * This class is used to perform all the complaints module action in database.
 * In query annotation (nativeQuery = true), the below query perform like SQL.
 * Otherwise it performs like HQL.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 06, 2023
 */
@Repository
public interface PatientLifestyleRepository extends JpaRepository<PatientLifestyle, Long> {

    /**
     * <p>
     * Get list of PatientLifestyle by patientTracker.
     * </p>
     *
     * @param patientTrackId {@link long} patient track id
     * @return {@link List<PatientLifestyle>} PatientLifestyle entity
     */
    List<PatientLifestyle> findByPatientTrackId(long patientTrackId);
}
