package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * This is DTO class for patient lifestyle entity.
 * </p>
 *
 * @author Jeyahairini T A created Feb 07, 2023
 */
@Data
public class LifestyleDTO {

    private String comments;

    private String lifestyle;

    private String lifestyleType;

    private String lifestyleAnswer;

    /**
     * <p>
     * This constructor can be used to create a new `LifestyleDTO` object with the specified values for its
     * instance variables.
     * </p>
     *
     * @param comments        comments param in string
     * @param lifestyle       lifestyle  param in string
     * @param lifestyleType   lifestyleType param in string
     * @param lifestyleAnswer lifestyleAnswer param in string
     */
    public LifestyleDTO(String comments, String lifestyle, String lifestyleType, String lifestyleAnswer) {
        this.comments = comments;
        this.lifestyle = lifestyle;
        this.lifestyleType = lifestyleType;
        this.lifestyleAnswer = lifestyleAnswer;
    }

    public LifestyleDTO() {
    }

}
