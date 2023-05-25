package com.mdtlabs.coreplatform.spiceservice.assessment.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This repository interface has the needed customized functions for patient assessment.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
@Repository
public interface PatientAssessmentRepository extends JpaRepository<PatientAssessment, Long> {

    /**
     * <p>
     * Get list of AssessmentRepository by patientTracker.
     * </p>
     *
     * @param patientTrackId {@link long} patient track id is given
     * @return {@link List<PatientAssessment>} AssessmentRepository entity is returned
     */
    List<PatientAssessment> findByPatientTrackId(long patientTrackId);
}
