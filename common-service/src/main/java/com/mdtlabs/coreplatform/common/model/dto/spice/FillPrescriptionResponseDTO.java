package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * This is a Java class representing a response DTO for filling a prescription, containing various
 * medication and prescription details.
 * </p>
 */
@Data
public class FillPrescriptionResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5831078400623272997L;

    private Long id;

    private String medicationName;

    private String dosageUnitName;

    private String dosageUnitValue;

    private String dosageFormName;

    private String dosageFrequencyName;

    private String instructionNote;

    private Date endDate;

    private int remainingPrescriptionDays;

    private long tenantId;

    private long prescription;

    private Date createdAt;

    private String classificationName;

    private String brandName;

}
