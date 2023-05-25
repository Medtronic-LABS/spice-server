package com.mdtlabs.coreplatform.spiceservice.patientvisit.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * PatientVisitService is a Java interface for managing CRUD (Create, Read, Update, Delete)
 * operations for PatientVisit customizations. It defines methods for creating, retrieving, updating, and
 * removing PatientVisit customizations.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface PatientVisitService {

    /**
     * <p>
     * This method is used to create a patient visit and returns a map containing information about the visit.
     * </p>
     *
     * @param patientVisitDto {@link CommonRequestDTO} The patientVisit contains information about a patient visit,
     *                        such as the patient's name, date of visit, reason for visit, etc. is given
     * @return {@link Map<String, Long>} The patient visit in the form of map for the given patient visit information
     * is returned
     */
    Map<String, Long> createPatientVisit(CommonRequestDTO patientVisitDto);

    /**
     * <p>
     * This method is used to retrieve a patient visit object by its ID.
     * </p>
     *
     * @param id {@link Long} The id for a specific patient visit is given
     * @return {@link PatientVisit} The patient visit for the given id is returned
     */
    PatientVisit getPatientVisitById(Long id);

    /**
     * This method is used to retrieve a list of patient visit dates based on certain criteria.
     *
     * @param patientTrackId  {@link Long} The patient track is used to retrieve the patient's visit dates is given
     * @param isInvestigation {@link Boolean} The boolean value indicating whether the patient visit includes an
     *                        investigation or not is given
     * @param isMedicalReview {@link Boolean} The Boolean value indicates whether the patient visit includes a
     *                        medical review or not is given
     * @param isPrescription  {@link Boolean} The boolean value indicates whether the patient visit includes a
     *                        prescription or not is given
     * @return {@link List<PatientVisit>} The list of PatientVisit that match the given patientTrackId and the boolean
     * values for isInvestigation, isMedicalReview, and isPrescription is returned
     */
    List<PatientVisit> getPatientVisitDates(Long patientTrackId, Boolean isInvestigation,
                                            Boolean isMedicalReview, Boolean isPrescription);

    /**
     * <p>
     * The method is used to update a patient visit and returns the updated patient visit.
     * </p>
     *
     * @param patientVisit {@link PatientVisit} The patientVisit contains information about the patient visit is given
     * @return {@link PatientVisit} The patient visit for the given information is returned
     */
    PatientVisit updatePatientVisit(PatientVisit patientVisit);

    /**
     * <p>
     * This method is used to retrieve a list of patient visit dates based on a patient track ID.
     * </p>
     *
     * @param patientTrackId {@link Long} The patient track ID is used to retrieve a list of PatientVisit objects that
     *                       contain information about the dates of the patient's visits to a healthcare
     *                       facility is given
     * @return {@link List<PatientVisit>} The list of PatientVisit contains visit dates for a specific patient
     * identified by their patientTrackId is returned
     */
    List<PatientVisit> getPatientVisitDates(Long patientTrackId);

    /**
     * <p>
     * The method is used to remove a patient visit identified by a tracker ID.
     * </p>
     *
     * @param trackerId The trackerId for a patient visit that needs to be removed from a database is given
     */
    void removePatientVisit(long trackerId);

}
