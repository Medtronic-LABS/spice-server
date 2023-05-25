package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import lombok.Data;

/**
 * <p>
 * This class is an Request DTO class for device details.
 * </p>
 *
 * @author Prabu created on 16 Feb 2023
 */
@Data
public class FrequencyDTO {

    private Long id;

    private String name;

    private String type;

    private Integer duration;

    private String period;

    private String riskLevel;

    private String title;

    private Integer displayOrder;

    private String cultureValue;

    private Long cultureId;

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
