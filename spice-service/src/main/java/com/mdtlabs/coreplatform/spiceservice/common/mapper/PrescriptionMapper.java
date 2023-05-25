package com.mdtlabs.coreplatform.spiceservice.common.mapper;

import com.mdtlabs.coreplatform.common.model.dto.spice.FillPrescriptionRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Prescription;
import org.springframework.stereotype.Component;

/**
 * <p>
 * This is the mapper class to map the entity and POJO class.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 10, 2023
 */
@Component
public class PrescriptionMapper {

    /**
     * This function updates an existing prescription with new information and returns the updated
     * prescription.
     *
     * @param existingPrescription {@link Prescription} an object of the Prescription class that represents the prescription
     *                             that already exists in the system and needs to be updated.
     * @param prescription         {@link Prescription} The new prescription object that contains updated information.
     * @param requestDto           {@link FillPrescriptionRequestDTO} an object of type FillPrescriptionRequestDTO that contains information about
     *                             the patient visit, tenant, and patient track.
     * @return {@link Prescription} The method is returning an object of type Prescription.
     */
    public Prescription setPrescription(Prescription existingPrescription, Prescription prescription,
                                        FillPrescriptionRequestDTO requestDto) {
        existingPrescription.setReason(prescription.getReason());
        existingPrescription.setPrescriptionFilledDays(prescription.getPrescriptionFilledDays());
        existingPrescription.setRemainingPrescriptionDays(
                existingPrescription.getRemainingPrescriptionDays() - prescription.getPrescriptionFilledDays());
        existingPrescription.setPatientVisitId(requestDto.getPatientVisitId());
        existingPrescription.setTenantId(requestDto.getTenantId());
        existingPrescription.setPatientTrackId(requestDto.getPatientTrackId());
        return existingPrescription;
    }

}
