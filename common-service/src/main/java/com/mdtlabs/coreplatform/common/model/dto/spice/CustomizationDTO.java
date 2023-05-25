package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * The CustomizationDTO class is a data transfer object that contains fields for type, category, form
 * input, country ID, and tenant ID.
 * </p>
 */
@Data
public class CustomizationDTO {
    private String type;
    private String category;
    private String formInput;
    private Long countryId;
    private long tenantId;

    /**
     * <p>
     * This is a constructor for the `CustomizationDTO` class
     * </p>
     *
     * @param type2      type2 string param  is passed into customizationDTO is given
     * @param category2  category2 param is passed into customizationDTO is given
     * @param formInput2 forminput2 param is passed into customizationDTO is given
     * @param countryId2 countryId2 param is passed into customizationDTO is given
     * @param tenantId2  tenantId2 param is passed into customizationDTO is given
     */
    public CustomizationDTO(String type2, String category2, String formInput2,
                            @NotNull(message = "Country id must not be null") Long countryId2,
                            @NotNull(message = "Tenant Id should not be null") Long tenantId2) {
    }
}
