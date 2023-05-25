package com.mdtlabs.coreplatform.spiceservice.patientmedicalcompliance.service.impl;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalCompliance;
import com.mdtlabs.coreplatform.spiceservice.patientmedicalcompliance.repository.PatientMedicalComplianceRepository;
import com.mdtlabs.coreplatform.spiceservice.patientmedicalcompliance.service.PatientMedicalComplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * This service class contain all the business logic for patient medical compliance module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class PatientMedicalComplianceServiceImpl implements PatientMedicalComplianceService {

    @Autowired
    private PatientMedicalComplianceRepository patientMedicalComplianceRepository;

    /**
     * {@inheritDoc}
     */
    public List<PatientMedicalCompliance> addPatientMedicalCompliance(
            List<PatientMedicalCompliance> patientMedicalComplianceList) {
        return patientMedicalComplianceRepository.saveAll(patientMedicalComplianceList);
    }

    /**
     * {@inheritDoc}
     */
    public void removeMedicalCompliance(long trackerId) {
        List<PatientMedicalCompliance> patientMedicalCompliance = patientMedicalComplianceRepository
                .findByPatientTrackId(trackerId);
        if (!Objects.isNull(patientMedicalCompliance)) {
            patientMedicalCompliance.forEach(compliance -> {
                compliance.setActive(false);
                compliance.setDeleted(true);
            });
            patientMedicalComplianceRepository.saveAll(patientMedicalCompliance);
        }
    }

}
