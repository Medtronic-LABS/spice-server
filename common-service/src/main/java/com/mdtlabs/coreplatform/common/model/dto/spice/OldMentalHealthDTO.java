package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The OldMentalHealthDTO class represents a data transfer object for mental health questions and
 * answers with their respective scores and display order.
 * </p>
 */
@Data
public class OldMentalHealthDTO {


    private String question_id;

    private String answer_id;

    private String display_order;

    private int score;

}
