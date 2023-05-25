package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * <p>
 * This DTO class handles the patient transfer requests
 * </p>
 *
 * @author Praveen created on Feb 10, 2023
 */
@Data
public class PatientTransferRequestDTO {

    @NotNull(message = ErrorConstants.PATIENT_TRACK_ID_NOT_NULL)
    private Long patientTrackId;

    @NotNull(message = ErrorConstants.TRANSFER_TO_NOT_NULL)
    private Long transferTo;

    @NotNull(message = ErrorConstants.TRANSFER_SITE_NOT_NULL)
    private Long transferSite;

    @NotNull(message = ErrorConstants.OLD_SITE_NOT_NULL)
    private Long oldSite;

    @NotNull(message = ErrorConstants.TRANSFER_REASON_NOT_NULL)
    private String transferReason;
}
