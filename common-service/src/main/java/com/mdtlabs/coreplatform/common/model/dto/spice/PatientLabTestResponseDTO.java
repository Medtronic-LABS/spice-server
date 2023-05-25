package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * Patient lab test response DTO is used to store response of patient
 * lab test.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Data
public class PatientLabTestResponseDTO {

    private List<PatientLabTestDTO> patientLabTest;

    private List patientLabtestDates;
}
