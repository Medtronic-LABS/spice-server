package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * Fields needed to search a patient
 * </p>
 *
 * @author Jeyaharini T A created on Feb 07, 2023
 */
@Data
public class PatientRequestDTO extends PaginateDTO {

    private String searchId;

    private Boolean isSearchUserOrgPatient;

    private Long tenantId;

    private Long operatingUnitId;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private boolean isGlobally = false;

    private Boolean isLabtestReferred;

    private Boolean isMedicationPrescribed;

    private PatientFilterDTO patientFilter;

    private Long id;

    private Long patientTrackId;

    private String deleteReason;

    private String deleteOtherReason;

    private PatientSortDTO patientSort;

}
