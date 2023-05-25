package com.mdtlabs.coreplatform.spiceservice.common.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.RedRiskNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This is the repository which acts as link between server side and database.
 * This class is used to perform all the complaints module action in database.
 * In query annotation (nativeQuery = true), the below query perform like SQL.
 * Otherwise, it performs like HQL.
 *
 * @author Karthick Murugesan
 * @since Jun 30, 2022
 */
@Repository
public interface RedRiskNotificationRepository extends JpaRepository<RedRiskNotification, Long> {


    /**
     * <p>
     * This function finds RedRiskNotifications by patientTrackId.
     * </p>
     * 
     * @param patientTrackId patientTrackId is a long type variable that represents the unique
     * identifier of a patient's track. This method is used to find all the RedRiskNotification objects
     * associated with a particular patient track identified by the patientTrackId.
     * @return The method `findByPatientTrackId` returns a list of `RedRiskNotification` objects that
     * match the given `patientTrackId`.
     */
    List<RedRiskNotification> findByPatientTrackId(long patientTrackId);

    /**
     * <p>
     * This function finds RedRiskNotifications by patientTrackId, where isDeleted is false and status
     * is not case sensitive.
     * </p>
     * 
     * @param patientTrackId This is a Long type parameter that represents the unique identifier of a
     * patient's track
     * @param status The "status" parameter is a String that represents the status of a
     * RedRiskNotification object
     * @return The method is returning a list of RedRiskNotification objects that match the given
     * patientTrackId and have not been deleted (isDeletedFalse) and have a status that matches the
     * given status (ignoring case).
     */
    List<RedRiskNotification> findByPatientTrackIdAndIsDeletedFalseAndStatusIgnoreCase(Long patientTrackId,
                                                                                       String status);
}
