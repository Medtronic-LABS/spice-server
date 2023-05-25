package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.model.enumeration.spice.PatientTransferStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * This DTO class handles the patient transfer update requests
 * </p>
 *
 * @author Praveen created on Feb 10, 2023
 */
@Data
public class PatientTransferUpdateRequestDTO {

    @NotNull(message = ErrorConstants.TRANSFER_ID_NOT_NULL)
    private Long id;

    @NotNull(message = ErrorConstants.TRANSFER_STATUS_NOT_NULL)
    private PatientTransferStatus transferStatus;

    private String rejectReason;
}
