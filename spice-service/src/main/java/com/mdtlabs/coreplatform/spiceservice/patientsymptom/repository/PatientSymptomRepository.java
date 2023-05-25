package com.mdtlabs.coreplatform.spiceservice.patientsymptom.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientSymptom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the user module action in database.
 * In query annotation (nativeQuery = true) the below query perform like SQL.
 * Otherwise its perform like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface PatientSymptomRepository extends JpaRepository<PatientSymptom, Long> {

    /**
     * <p>
     * Get list of PatientSymptom by patientTracker.
     * </p>
     *
     * @param patientTrackId {@link Long} patient tracker id
     * @return {@link List<PatientSymptom>} PatientSymptom entity
     */
    List<PatientSymptom> findByPatientTrackIdAndIsDeleted(Long patientTrackId, Boolean isDeleted);

    /**
     * <p>
     * Get list of PatientSymptom by patientTracker.
     * </p>
     *
     * @param patientTrackerId {@link long} patient tracker id
     * @return {@link List<PatientSymptom>} PatientSymptom entity
     */
    List<PatientSymptom> findByPatientTrackId(long patientTrackerId);

    /**
     * <p>
     * Get list of patient symptoms by patient tracker id and glucose log ID.
     * </p>
     *
     * @param patientTrackerId {@link long} patient tracker ID
     * @param glucoseLogId     {@link long} glucoseLog ID
     * @return {@link List<PatientSymptom>} List of Symptom entity
     */
    List<PatientSymptom> findByPatientTrackIdAndGlucoseLogIdOrderByUpdatedAtDesc(long patientTrackerId,
                                                                                 long glucoseLogId);

    /**
     * <p>
     * Get list of patient symptoms by patient tracker id.
     * </p>
     *
     * @param patientTrackerId {@link long} patient tracker ID
     * @param bpLogId          {@link long} BpLog ID
     * @return {@link List<PatientSymptom>} List of Symptom entity
     */
    List<PatientSymptom> findByPatientTrackIdAndBpLogIdOrderByUpdatedAtDesc(long patientTrackerId,
                                                                            long bpLogId);
}
