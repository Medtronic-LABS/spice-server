package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serializable;

import lombok.Data;

/**
 * <p>
 * The MentalHealthDetails class is a serializable Java class with four fields: questionId, answerId, displayOrder, and
 * score.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
public class MentalHealthDetails implements Serializable {

    private Long questionId;

    private Long answerId;

    private int displayOrder;

    private int score;
}
