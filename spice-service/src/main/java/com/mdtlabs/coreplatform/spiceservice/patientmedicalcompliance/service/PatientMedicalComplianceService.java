package com.mdtlabs.coreplatform.spiceservice.patientmedicalcompliance.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientMedicalCompliance;

import java.util.List;

/**
 * <p>
 * This is an interface to perform any actions in PatientMedicalCompliance related entities.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 08, 2023
 */
public interface PatientMedicalComplianceService {

    /**
     * <p>
     * This function adds a list of patient medical compliance records.
     * </p>
     *
     * @param patientMedicalCompliance {@link List<PatientMedicalCompliance>} A list of objects of type PatientMedicalCompliance that contains
     *                                 information about a patient's medical compliance
     * @return {@link List<PatientMedicalCompliance>} The method is returning a List of PatientMedicalCompliance objects after adding new
     * PatientMedicalCompliance objects to the existing list.
     */
    List<PatientMedicalCompliance> addPatientMedicalCompliance(
            List<PatientMedicalCompliance> patientMedicalCompliance);

    /**
     * <p>
     * The function removes medical compliance data for a specific tracker ID.
     * </p>
     *
     * @param trackerId {@link long} The parameter `trackerId` is a `long` data type that represents the unique
     *                  identifier of a medical compliance tracker that needs to be removed.
     */
    void removeMedicalCompliance(long trackerId);

}

