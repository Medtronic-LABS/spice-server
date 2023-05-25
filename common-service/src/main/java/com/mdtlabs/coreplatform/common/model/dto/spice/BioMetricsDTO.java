package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The BioMetricsDTO class contains fields for various biometric data such as gender, age, height,
 * weight, BMI, smoking status, pregnancy status, physical activity, and diabetes history.
 * </p>
 */
@Data
public class BioMetricsDTO {

    private String gender;

    private Integer age;

    private Double height;

    private Double weight;

    private Double bmi;

    private Boolean isRegularSmoker;

    private Boolean isPregnant;

    private Boolean isphysicallyActive;

    private boolean isFamilyDiabetesHistory;

    private boolean isBeforeGestationalDiabetes;

}