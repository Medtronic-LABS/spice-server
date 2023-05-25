package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * This is a Java class representing a patient's lab test result with various attributes such as name,
 * result value, unit, and display order.
 * </p>
 */
@Data
public class PatientLabTestResultDTO {
    private String id;
    @NotEmpty(message = ErrorConstants.PATIENT_LAB_TEST_RESULT_NAME_NOT_NULL)
    private String name;
    private String displayName;
    @NotEmpty(message = ErrorConstants.RESULT_VALUE_NOT_NULL)
    private String resultValue;
    @NotEmpty(message = ErrorConstants.UNIT_NOT_NULL)
    private String unit;
    private String resultStatus;
    private Boolean isAbnormal;
    @NotNull(message = ErrorConstants.DISPLAY_ORDER_NOT_NULL)
    private Integer displayOrder;
}
