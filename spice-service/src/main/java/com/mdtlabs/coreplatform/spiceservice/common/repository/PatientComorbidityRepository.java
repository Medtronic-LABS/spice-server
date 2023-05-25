package com.mdtlabs.coreplatform.spiceservice.common.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientComorbidity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This is the repository which acts as link between server side and database.
 * This class is used to perform all the complaints module action in database.
 * In query annotation (nativeQuery = true), the below query perform like SQL.
 * Otherwise, it performs like HQL.
 *
 * @author Niraimathi S created on Feb 06, 2023
 */
@Repository
public interface PatientComorbidityRepository extends JpaRepository<PatientComorbidity, Long> {

    String GET_BY_TRACKER_ID = "select comorbidity from PatientComorbidity as "
            + "comorbidity where comorbidity.patientTrackId = :patientTrackId AND "
            + "comorbidity.isActive = :isActive AND comorbidity.isDeleted = :isDeleted AND "
            + "(comorbidity.otherComorbidity is null)";

    /**
     * <p>
     * Retrieves List of PatientComorbidity entities based on the patient tracker id.
     * </p>
     *
     * @param patientTrackId {@link Long} patient track id
     * @param isActive        {@link Boolean} true or false
     * @param isDeleted      {@link Boolean} true or false
     * @return {@link List<PatientComorbidity>} List of PatientComorbidity entities
     */
    @Query(value = GET_BY_TRACKER_ID)
    List<PatientComorbidity> getByTrackerId(@Param("patientTrackId") Long patientTrackId,
                                            @Param("isActive") Boolean isActive, @Param("isDeleted") Boolean isDeleted);

    /**
     * <p>
     * Retrieves List of PatientComorbidity entities based on the patient tracker id.
     * </p>
     *
     * @param patientTrackId {@link Long} patient track id
     * @param isDeleted      {@link Boolean} true or false
     * @return {@link List<PatentComorbidity>}  List of PatientComorbidity entities
     */
    List<PatientComorbidity> findByPatientTrackIdAndIsDeleted(Long patientTrackId,
                                                              Boolean isDeleted);

    /**
     * Get list of PatientComorbidity by patientTracker.
     *
     * @param patientTrackId {@link long} patient track id
     * @return {@link List<PatientComorbidity>} PatientComorbidity entity
     */
    List<PatientComorbidity> findByPatientTrackId(long patientTrackId);
}
