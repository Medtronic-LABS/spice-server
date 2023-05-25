package com.mdtlabs.coreplatform.spiceservice.patientsymptom.service.impl;

import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientSymptom;
import com.mdtlabs.coreplatform.spiceservice.patientsymptom.repository.PatientSymptomRepository;
import com.mdtlabs.coreplatform.spiceservice.patientsymptom.service.PatientSymptomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * This class implements the PatientSymptomService interface and contains actual
 * business logic to perform operations on PatientSymptom entity.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
@Service
public class PatientSymptomServiceImpl implements PatientSymptomService {

    @Autowired
    private PatientSymptomRepository patientSymptomRepository;

    /**
     * {@inheritDoc}
     */
    public List<PatientSymptom> getSymptomsByBpLogId(long patientTrackerId, long bpLogId) {
        List<PatientSymptom> patientSymptoms = patientSymptomRepository
                .findByPatientTrackIdAndBpLogIdOrderByUpdatedAtDesc(patientTrackerId, bpLogId);
        return Objects.isNull(patientSymptoms) ? new ArrayList<>() : patientSymptoms;
    }

    /**
     * {@inheritDoc}
     */
    public List<PatientSymptom> getSymptomsByGlucoseLogId(long patientTrackerId, long glucoseLogId) {
        return patientSymptomRepository
                .findByPatientTrackIdAndGlucoseLogIdOrderByUpdatedAtDesc(patientTrackerId, glucoseLogId);
    }

    /**
     * {@inheritDoc}
     */
    public List<PatientSymptom> addPatientSymptoms(List<PatientSymptom> patientSymptoms) {
        if (patientSymptoms.isEmpty()) {
            throw new DataNotAcceptableException(1401);
        }
        return patientSymptomRepository.saveAll(patientSymptoms);
    }

    /**
     * {@inheritDoc}
     */
    public void removePatientSymptom(long trackerId) {
        List<PatientSymptom> patientSymptoms = patientSymptomRepository.findByPatientTrackId(trackerId);
        if (!Objects.isNull(patientSymptoms)) {
            patientSymptoms.forEach(symptom -> {
                symptom.setActive(false);
                symptom.setDeleted(true);
            });
            patientSymptomRepository.saveAll(patientSymptoms);
        }
    }
}
