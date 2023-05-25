package com.mdtlabs.coreplatform.spiceservice.medicalreview.repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the PatientMedicalReview module
 * action in database. In query annotation (nativeQuery = true) the below query
 * perform like SQL. Otherwise its perform like HQL default value for
 * nativeQuery FALSE
 * </p>
 *
 * @author Karthick Murugesan created on Feb 06, 2023
 */
@Repository
public interface MedicalReviewRepository extends JpaRepository<PatientMedicalReview, Long> {

    String GET_PATIENT_MEDICAL_REVIEW = "from PatientMedicalReview as "
            + "medicalreview where medicalreview.patientTrackId=:patientTrackId "
            + "and medicalreview.isDeleted=false and (:patientVisitId is null or "
            + "medicalreview.patientVisitId=:patientVisitId)";

    /**
     * Gets medical review for a patient based on patientTrackId and patientVisitId.
     *
     * @param patientTrackId {@link Long} Long
     * @param patientVisitId {@link Long} Long
     * @return {@link List<PatientMedicalReview>} List of patientMedicalReview
     */
    @Query(value = GET_PATIENT_MEDICAL_REVIEW)
    List<PatientMedicalReview> getPatientMedicalReview(@Param(Constants.PATIENT_TRACK_ID) Long patientTrackId,
                                                       @Param(Constants.PATIENT_VISIT_ID) Long patientVisitId);

    /**
     * <p>
     * Get list of PatientMedicalReview by patientTracker.
     * </p>
     *
     * @param patientTrackId {@link Long} Long
     * @return {@link List<PatientMedicalReview>} List of patientMedicalReview
     */
    List<PatientMedicalReview> findByPatientTrackId(long patientTrackId);

}
