package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * <p>
 * BpLogDetails is a Java class representing blood pressure log details with systolic, diastolic, and pulse readings.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BpLogDetails implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    private Integer systolic;

    @NotNull
    private Integer diastolic;

    private Integer pulse;

    public BpLogDetails(@NotNull Integer systolic, @NotNull Integer diastolic, Integer pulse) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.pulse = pulse;
    }

    public BpLogDetails() {
    }

}
