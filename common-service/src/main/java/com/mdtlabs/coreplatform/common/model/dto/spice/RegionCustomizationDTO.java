package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * This is a Java class representing a data transfer object for region customization, with various
 * fields and a method to get the user value.
 * </p>
 */
@Data
public class RegionCustomizationDTO {

    @NotBlank(message = ErrorConstants.TYPE_NOT_NULL)
    private String type;

    @NotBlank(message = ErrorConstants.CATEGORY_NOT_NULL)
    private String category;

    @NotBlank(message = ErrorConstants.FORM_INPUT_NOT_NULL)
    private String formInput;

    private Long cultureId;

    @NotNull(message = ErrorConstants.COUNTRY_ID_NOT_NULL)
    private Long countryId;

    private boolean isDefault;

    private Long id;

    private Long tenantId;

    private Long createdBy = getUserValue();

    /**
     * <p>
     * This method is used to get user value
     * </p>
     *
     * @return String - user value
     */
    @JsonIgnore
    public long getUserValue() {
        if (null != UserContextHolder.getUserDto()) {
            return UserContextHolder.getUserDto().getId();
        }
        return Constants.ZERO;
    }

}
