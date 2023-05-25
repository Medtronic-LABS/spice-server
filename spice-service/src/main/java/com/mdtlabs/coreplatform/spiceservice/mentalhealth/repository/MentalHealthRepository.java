package com.mdtlabs.coreplatform.spiceservice.mentalhealth.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <p>
 * This repository class is responsible for communication between database and
 * server side.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 07, 2023
 */
@Repository
public interface MentalHealthRepository extends JpaRepository<MentalHealth, Long> {

    String UPDATE_LATEST_STATUS = "Update MentalHealth as mentalhealth "
            + "set mentalhealth.isLatest=false "
            + "where mentalhealth.patientTrackId=:patientTrackId and mentalhealth.isLatest=true"
            + " and mentalhealth.isDeleted=false";

    /**
     * <p>
     * This method is used to get mental health by patient track id.
     * </p>
     *
     * @param patientTrackId {@link Long} patient track id
     * @param isDeleted      {@link Boolean} true or false
     * @param isLatest       {@link Boolean} true or false
     * @return {@link MentalHealthMental} health - entity
     */
    MentalHealth findByPatientTrackIdAndIsDeletedAndIsLatest(Long patientTrackId, Boolean isDeleted,
                                                             Boolean isLatest);

    /**
     * <p>
     * This method is used to get mental health by id.
     * </p>
     *
     * @param id        {@link Long} mental health id
     * @param isDeleted {@link Boolean} true or false
     * @return MentalHealth {@link MentalHealth} entity
     */
    MentalHealth findByIdAndIsDeleted(Long id, Boolean isDeleted);

    /**
     * <p>
     * This is a Java function that updates the latest status of a patient track record.
     * </p>
     *
     * @param patientTrackId {@link long} patient track id
     * @return int {@link int} The method is returning an integer value, which represents the number of rows that were
     * affected by the update operation.
     */
    @Modifying
    @Transactional
    @Query(value = UPDATE_LATEST_STATUS)
    int updateLatestStatus(@Param("patientTrackId") long patientTrackId);

    /**
     * <p>
     * This function finds mental health records by patient track ID and whether they have been
     * deleted.
     * </p>
     *
     * @param patientTrackId {@link Long} This is a Long type parameter that represents the unique identifier of a
     *                       patient's mental health record. It is used to search for mental health records associated with a
     *                       specific patient.
     * @param isDeleted      {@link Boolean} isDeleted is a Boolean parameter that is used to filter the MentalHealth
     *                       objects based on whether they have been marked as deleted or not. If the value of isDeleted is
     *                       true, then only the MentalHealth objects that have been marked as deleted will be returned. If
     *                       the value of isDeleted is false
     * @return {@link List<MentalHealth>} The method `findByPatientTrackIdAndIsDeleted` is likely returning a list of
     * `MentalHealth` objects that match the given `patientTrackId` and `isDeleted` parameters.
     */
    List<MentalHealth> findByPatientTrackIdAndIsDeleted(Long patientTrackId,
                                                        Boolean isDeleted);

    /**
     * <p>
     * This function finds a list of mental health records by patient track ID.
     * </p>
     *
     * @param patientTrackId {@link long} patientTrackId is a unique identifier assigned to a patient's mental
     *                       health record. This method is used to retrieve a list of MentalHealth objects that are
     *                       associated with a specific patientTrackId.
     * @return {@link List<MentalHealth>} This method is likely part of a mental health tracking system and it returns a list of
     * MentalHealth objects that are associated with a specific patient track ID.
     */
    List<MentalHealth> findByPatientTrackId(long patientTrackId);
}
