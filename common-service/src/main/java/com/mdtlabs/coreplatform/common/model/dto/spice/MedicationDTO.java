package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

/**
 * <p>
 * The MedicationDTO class is a data transfer object that contains information about a medication,
 * including its name, country, classification, dosage form, brand, display order, type, status, and
 * ID.
 * </p>
 */
@Data
public class MedicationDTO extends CommonRequestDTO {

    private String medicationName;

    private Long countryId;

    private Long classificationId;

    private Long dosageFormId;

    private String dosageFormName;

    private String dosageUnitName;

    private Long brandId;

    private String classificationName;

    private String brandName;

    private int displayOrder;

    private String type;

    private boolean status;

    private Long id;

    private Long tenantId;
}
