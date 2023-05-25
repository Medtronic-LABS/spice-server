package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * The `MetaFormDTO` class is a data transfer object that contains information about a form, including
 * its ID, name, and components.
 * </p>
 */
@Data
public class MetaFormDTO {

    private Long id;

    private String formName;

    private List<Map<String, Object>> components;

    public MetaFormDTO() {

    }

    /**
     * <p>
     * This is a constructor for the `MetaFormDTO` class
     * </p>
     */
    public MetaFormDTO(Long id, String formName, List<Map<String, Object>> components) {
        this.id = id;
        this.formName = formName;
        this.components = components;
    }
}
