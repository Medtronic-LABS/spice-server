package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * This is a Java class representing a Glucose Log Data Transfer Object (DTO) with various properties
 * and methods.
 * </p>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GlucoseLogDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Double hba1c;

    private Long createdBy = getUserValue();

    private Long updatedBy = getUserValue();

    @UpdateTimestamp
    private Date updatedAt;

    private boolean isDeleted;

    private String hba1cUnit;

    private String glucoseType;

    private Double glucoseValue;

    private Date lastMealTime;

    private Date glucoseDateTime;

    private String glucoseUnit;

    private String type;

    private Boolean isBeforeDiabetesDiagnosis;

    private Long glucoseId;

    private boolean isLatest;

    private Long patientTrackId;

    private boolean isUpdatedFromEnrollment;

    private Long screeningId;

    private Long assessmentTenantId;

    private Date bgTakenOn;

    private boolean isOldRecord;

    private Long tenantId;

    @CreationTimestamp
    private Date createdAt;

    private List<String> symptoms;

    private Long glucoseLogId;

    private Boolean isBeforeDiabeticDiagnosis;

    /**
     * <p>
     * This is a constructor for the GlucoseLogDTO class
     * </p>
     *
     * @param glucoseUnit     glucoseUnit param to instance is given
     * @param glucoseValue    glucoseValue param to instance is given
     * @param lastMealTime   lastMealTime param to instance is given
     * @param glucoseDateTime glucoseDateTime param to instance is given
     * @param glucoseUnit glucoseUnit param to instance is given
     */
    public GlucoseLogDTO(String glucoseType, Double glucoseValue, Date lastMealTime, Date glucoseDateTime,
                         String glucoseUnit) {
        this.glucoseType = glucoseType;
        this.glucoseValue = glucoseValue;
        this.lastMealTime = lastMealTime;
        this.glucoseDateTime = glucoseDateTime;
        this.glucoseUnit = glucoseUnit;
    }

    /**
     * <p>
     * This is a constructor for the GlucoseLogDTO class
     * </p>
     *
     * @param glucoseUnit  glucoseUnit param to instance is given
     * @param glucoseType  glucoseType param to instance is given
     * @param glucoseValue glucoseValue param to instance is given
     */
    public GlucoseLogDTO(String glucoseType, Double glucoseValue, String glucoseUnit) {
        this.glucoseType = glucoseType;
        this.glucoseValue = glucoseValue;
        this.glucoseUnit = glucoseUnit;
    }

    public GlucoseLogDTO() {
    }

    /**
     * <p>
     * This is a constructor for the GlucoseLogDTO class that takes in several parameters and
     * initializes the corresponding instance variables with those values.
     * </p>
     *
     * @param id              id param to instance is given
     * @param hba1c           hba1c param to instance is given
     * @param hba1cUnit       hba1cUnit param to instance is given
     * @param glucoseType     glucoseType param to instance is given
     * @param glucoseValue    glucoseValue param to instance is given
     * @param glucoseDateTime glucoseDateTime param to instance is given
     * @param glucoseUnit     glucoseUnit param to instance is given
     * @param createdAt       createdAt param to instance is given
     */
    public GlucoseLogDTO(Long id, Double hba1c, String hba1cUnit, String glucoseType, Double glucoseValue,
                         Date glucoseDateTime, String glucoseUnit, Date createdAt) {
        this.id = id;
        this.hba1c = hba1c;
        this.hba1cUnit = hba1cUnit;
        this.glucoseType = glucoseType;
        this.glucoseValue = glucoseValue;
        this.glucoseDateTime = glucoseDateTime;
        this.glucoseUnit = glucoseUnit;
        this.createdAt = createdAt;
    }

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