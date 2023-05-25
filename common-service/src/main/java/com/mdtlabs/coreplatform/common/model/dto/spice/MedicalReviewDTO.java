package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The MedicalReviewDTO class extends CommonRequestDTO and contains information about initial and
 * continuous medical reviews, as well as a boolean indicating pregnancy status.
 * </p>
 */
@Data
public class MedicalReviewDTO extends CommonRequestDTO {

    private InitialMedicalReviewDTO initialMedicalReview;

    private ContinuousMedicalReviewDTO continuousMedicalReview;

}
