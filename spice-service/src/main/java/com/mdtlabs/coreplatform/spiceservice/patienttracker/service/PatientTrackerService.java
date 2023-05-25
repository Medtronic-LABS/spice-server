package com.mdtlabs.coreplatform.spiceservice.patienttracker.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.ConfirmDiagnosisDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;

import java.util.Date;

/**
 * <p>
 * This is an interface to perform any actions in patient tracker related
 * entities.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 06, 2023
 */
public interface PatientTrackerService {

    /**
     * <p>
     * This method adds or update a patient tracker.
     * </p>
     *
     * @param patientTracker {@link PatientTracker} entity
     * @return PatientTracker {@link PatientTracker} Entity
     */
    PatientTracker addOrUpdatePatientTracker(PatientTracker patientTracker);

    /**
     * <p>
     * This method fetches a patient tracker by id.
     * </p>
     *
     * @param patientTrackerId {@link Long} patient tracker id
     * @return {@link PatientTracker} PatientTracker Entity
     */
    PatientTracker getPatientTrackerById(Long patientTrackerId);

    /**
     * <p>
     * This method is used to update patient tracker for bp log.
     * </p>
     *
     * @param patientTrackerId     {@link long} patient tracker id
     * @param bpLog                {@link Bplog} entity
     * @param nextBpAssessmentDate {@link Date} next bp assessment date
     */
    void updatePatientTrackerForBpLog(long patientTrackerId, BpLog bpLog, Date nextBpAssessmentDate);

    /**
     * <p>
     * This method is used to update patient tracker for glucose log.
     * </p>
     *
     * @param patientTrackerId     {@link long} patient tracker id
     * @param glucoseLog           {@link Glucoselog} entity
     * @param nextBgAssessmentDate {@link Date} next bg assessment date
     */
    void updatePatientTrackerForGlucoseLog(long patientTrackerId, GlucoseLog glucoseLog,
                                           Date nextBgAssessmentDate);

    /**
     * <p>
     * This method is used to get patient tracker by national id.
     * </p>
     *
     * @param searchNationalId {@link String} national id
     * @param booleanFalse     {@link Long} booleanFalse
     * @return PatientTracker {@link PatientTracker} entity
     */
    PatientTracker findByNationalIdIgnoreCaseAndCountryIdAndIsDeleted(String searchNationalId, Long country, boolean booleanFalse);

    /**
     * <p>
     * This method is used to list the patient details.
     * </p>
     *
     * @param patientRequestDto {@link PatientRequestDTO} entity
     * @return Map {@link ResponseListDTO} patient list map
     */
    ResponseListDTO listMyPatients(PatientRequestDTO patientRequestDto);

    /**
     * <p>
     * This method is used to search the patient with fields like national id,
     * program id etc.
     * </p>
     *
     * @param patientRequestDto {@link PatientRequestDTO} entity
     * @return Map {@link ResponseListDTO} patient map
     */
    ResponseListDTO searchPatients(PatientRequestDTO patientRequestDto);

    /**
     * <p>
     * This method is used to get the patients list with advance search.
     * </p>
     *
     * @param patientRequestDto {@link PatientRequestDTO} entity
     * @return Map {@link ResponseListDTO} patient map
     */
    ResponseListDTO patientAdvanceSearch(PatientRequestDTO patientRequestDto);

    /**
     * <p>
     * Updates isLabTestReferred based on lab tests referred to a patient.
     * </p>
     *
     * @param patientTrackId    {@link long} PatientTrackId
     * @param tenantId          {@link Long} tenantId
     * @param isLabTestReferred {@link boolean} isLabTestReferred field
     */
    void updatePatientTrackerLabtestReferral(long patientTrackId, Long tenantId, boolean isLabTestReferred);

    /**
     * <p>
     * Update confirm diagnosis details to the patient.
     * </p>
     *
     * @param confirmDiagnosis {@link ConfirmDiagnosisDTO} entity
     * @return ConfirmDiagnosisDTO {@link ConfirmDiagnosisDTO} entity
     */
    ConfirmDiagnosisDTO updateConfirmDiagnosis(ConfirmDiagnosisDTO confirmDiagnosis);

    /**
     * <p>
     * This method is used to update fill prescription.
     * </p>
     *
     * @param id                     {@link Long} fill prescription id
     * @param isMedciationPrescribed {@link boolean} true or false
     * @param lastAssessmentDate     {@link Date} last assessment date
     * @param nextMedicalReviewDate  {@link Date} next medical review date
     */
    void updateForFillPrescription(Long id, boolean isMedciationPrescribed, Date lastAssessmentDate,
                                   Date nextMedicalReviewDate);

    /**
     * <p>
     * Gets a patient tracker based on tenant id and status.
     * </p>
     *
     * @param id       {@link Long} patient tracker id
     * @param tenantId {@link Long} tenant id
     * @param status   {@link String} patient status
     * @return PatientTracker {@link PatientTracker} patient tracker entity
     */
    PatientTracker getPatientTrackerByIdAndStatus(Long id, Long tenantId, String status);
}
