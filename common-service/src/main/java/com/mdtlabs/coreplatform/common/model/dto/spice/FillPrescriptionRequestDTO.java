package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.model.entity.spice.Prescription;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * The FillPrescriptionRequestDTO class is a data transfer object that extends CommonRequestDTO and
 * contains a list of Prescription objects.
 * </p>
 */
@Data
public class FillPrescriptionRequestDTO extends CommonRequestDTO {

    private List<Prescription> prescriptions;

}
