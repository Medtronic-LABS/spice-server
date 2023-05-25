package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * This class is a DTO for PregnancyDetails entity.
 * </p>
 *
 * @author Niraimathi S created on Feb 7, 2023
 */
@Data
public class PregnancyDetailsDTO {
    private Long id;

    private List<String> diagnosis;

    private Date diagnosisTime;

    private Date estimatedDeliveryDate;

    private int gravida;

    private Boolean isOnTreatment;

    private Date lastMenstrualPeriodDate;

    private int parity;

    private Long patientTrackId;

    private Integer pregnancyFetusesNumber;

    private Double temperature;

    private Long tenantId;

    private String unitMeasurement;

    private Long patientPregnancyId; // for update request
}
