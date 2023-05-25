package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

/**
 * <p>
 * This class is an DTO class contains fields for medical review static data.
 * </p>
 *
 * @author Niraimathi S created on Feb 17, 2023
 */
@Data
@JsonInclude(Include.NON_NULL)
public class MedicalReviewStaticDataListDTO {
    private Long id;
    private String name;
    private Integer displayOrder;
    private String cultureValue;

    /**
     * <p>
     * This constructor can be used to create an instance of the `MedicalReviewStaticDataListDTO`
     * class with the `id` field initialized to a specific value.
     * </p>
     *
     * @param id id param of long type
     */
    public MedicalReviewStaticDataListDTO(Long id) {
        super();
        this.id = id;
    }

    /**
     * <p>
     * This is used to create an instance of the class with default values for its fields.
     * </p>
     */
    public MedicalReviewStaticDataListDTO() {
    }
}
