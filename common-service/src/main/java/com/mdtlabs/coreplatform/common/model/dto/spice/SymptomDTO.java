package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;


/**
 * <p>
 * This is a Java class representing a SymptomDTO object with various properties and methods, including
 * a method to get the user value.
 * </p>
 */
@Data
@Validated
public class SymptomDTO {

    private Long id;

    private Long createdBy = getUserValue();

    private Long updatedBy = getUserValue();

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    private boolean isActive;

    private boolean isDeleted;

    @NotNull(message = ErrorConstants.SYMPTOM_ID_NOT_NULL)
    private Long symptomId;

    @NotEmpty(message = ErrorConstants.SYMPTOM_NAME_NOT_NULL)
    private String name;

    @NotEmpty(message = ErrorConstants.TYPE_NOT_NULL)
    private String type;

    private String otherSymptom;

    private String description;

    private int displayOrder;

    private Map<String, Boolean> categories;

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