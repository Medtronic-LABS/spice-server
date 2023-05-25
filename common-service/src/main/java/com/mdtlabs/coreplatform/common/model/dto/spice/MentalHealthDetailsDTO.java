package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The MentalHealthDetailsDTO class contains data related to a mental health question, including the
 * question ID, answer ID, display order, and score.
 * </p>
 */
@Data
public class MentalHealthDetailsDTO {

    private Long questionId;

    private Long answerId;

    private int displayOrder;

    private int score;

}
