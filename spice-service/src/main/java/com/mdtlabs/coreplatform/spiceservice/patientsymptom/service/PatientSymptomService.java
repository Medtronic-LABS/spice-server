package com.mdtlabs.coreplatform.spiceservice.patientsymptom.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientSymptom;

import java.util.List;

/**
 * <p>
 * This is an interface to perform any actions in PatientSymptom related entities.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
public interface PatientSymptomService {

    /**
     * <p>
     * This method is used to add patient symptoms.
     * </p>
     *
     * @param patientSymptoms {@link List<PatientSymptom>} list of entities is given
     * @return List {@link List<PatientSymptom>} list of entities is returned
     */
    List<PatientSymptom> addPatientSymptoms(List<PatientSymptom> patientSymptoms);

    /**
     * <p>
     * Gets list of symptoms by patient tracker and BpLog ID.
     * </p>
     *
     * @param bpLogId          {@link long} BpLog ID is given
     * @param patientTrackerId {@link long} patient Tracker ID is given
     * @return {@link List<PatientSymptom>} List -list of Patient Symptom is returned
     */
    List<PatientSymptom> getSymptomsByBpLogId(long patientTrackerId, long bpLogId);

    /**
     * <p>
     * Gets list of symptoms by patient tracker and BpLog ID.
     * </p>
     *
     * @param glucoseLogId     {@link long} GlucoseLog ID
     * @param patientTrackerId {@link long} patient Tracker ID
     * @return List {@link List<PatientSymptom>} list of Patient Symptom
     */
    List<PatientSymptom> getSymptomsByGlucoseLogId(long patientTrackerId, long glucoseLogId);

    /**
     * <p>
     * To remove symptom details based on tracker id.
     * </p>
     *
     * @param trackerId {@link long} trackerId
     */
    void removePatientSymptom(long trackerId);

}
