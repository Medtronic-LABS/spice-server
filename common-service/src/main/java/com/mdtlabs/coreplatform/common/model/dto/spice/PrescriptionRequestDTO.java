package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * This is a Java class representing a prescription request with patient information, a signature, and
 * a list of prescriptions.
 * </p>
 */
@Data
public class PrescriptionRequestDTO {

    @NotNull(message = ErrorConstants.PATIENT_TRACK_ID_NOT_NULL)
    private Long patientTrackId;

    @NotNull(message = ErrorConstants.PATIENT_VISIT_ID_NOT_NULL)
    private Long patientVisitId;

    private Long tenantId;

    private String signature;

    @NotNull(message = ErrorConstants.SIGNATURE_REQUIRED)
    private MultipartFile signatureFile;

    @Valid
    private List<PrescriptionDTO> prescriptionList;

}
