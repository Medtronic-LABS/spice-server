package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLogDetails;
import com.mdtlabs.coreplatform.common.util.spice.EnrollmentInfo;
import com.mdtlabs.coreplatform.common.util.spice.ScreeningInfo;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * Bp log is used to handle bp log information.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BpLogDTO {

    private Long id;

    private Long tenantId;

    private Long createdBy = getUserValue();

    private Long updatedBy = getUserValue();

    @UpdateTimestamp
    private Date updatedAt;

    private boolean isDeleted;

    @NotNull(message = ErrorConstants.AVG_SYSTOLIC_NOT_NULL, groups = {Default.class, ScreeningInfo.class,
            EnrollmentInfo.class})
    private Integer avgSystolic;

    @NotNull(message = ErrorConstants.AVG_DIASTOLIC_NOT_NULL, groups = {Default.class, ScreeningInfo.class,
            EnrollmentInfo.class})
    private Integer avgDiastolic;

    private Integer avgPulse;

    private Double height;

    private Double weight;

    private Double bmi;

    private Double temperature;

    private String cvdRiskLevel;

    private Integer cvdRiskScore;

    private boolean isLatest;

    private Boolean isRegularSmoker;

    private String type;

    private Long patientTrackId;

    private Long screeningId;

    @NotNull(message = ErrorConstants.BP_LOG_DETAILS_NOT_EMPTY, groups = {EnrollmentInfo.class, ScreeningInfo.class})
    @Size(min = 2, message = ErrorConstants.BP_LOG_DETAILS_MIN_SIZE, groups = {EnrollmentInfo.class, ScreeningInfo.class})
    private List<BpLogDetails> bpLogDetails;

    private String riskLevel;

    private boolean isUpdatedFromEnrollment;

    private String bpArm;

    private String bpPosition;

    private String covidVaccStatus;

    private String assessmentCategory;

    private String assessmentLandmark;

    private String notes;

    private String insuranceStatus;

    private String insuranceType;

    private String insuranceId;

    private String otherInsurance;

    private Date bpTakenOn;

    private Long assessmentTenantId;

    private boolean isRedRiskPatient;

    private Boolean isBeforeHtnDiagnosis;

    private String unitMeasurement;

    private boolean isOldRecord;

    @CreationTimestamp
    private Date createdAt;

    private List<String> symptoms;

    /**
     * <p>
     * Creates a new BpLogDTO object with values for avgSystolic, avgDiastolic, bmi, cvdRiskLevel, and
     * cvdRiskScore.
     * </p>
     *
     * @param avgSystolic  The average systolic value for the blood pressure log.
     * @param avgDiastolic The average diastolic value for the blood pressure log.
     * @param bmi          The body mass index for the blood pressure log.
     * @param cvdRiskLevel The cardiovascular disease risk level for the blood pressure log.
     * @param cvdRiskScore The cardiovascular disease risk score for the blood pressure log.
     */
    public BpLogDTO(
            Integer avgSystolic,
            Integer avgDiastolic,
            Double bmi, String cvdRiskLevel, Integer cvdRiskScore) {
        this.avgSystolic = avgSystolic;
        this.avgDiastolic = avgDiastolic;
        this.bmi = bmi;
        this.cvdRiskLevel = cvdRiskLevel;
        this.cvdRiskScore = cvdRiskScore;
    }

    public BpLogDTO() {
    }

    /**
     * <p>
     * Constructor for BpLogDTO that sets average systolic, diastolic and BMI values.
     * </p>
     *
     * @param avgSystolic  the average systolic value
     * @param avgDiastolic the average diastolic value
     * @param bmi          the body mass index value
     */
    public BpLogDTO(
            Integer avgSystolic,
            Integer avgDiastolic,
            Double bmi) {
        this.avgSystolic = avgSystolic;
        this.avgDiastolic = avgDiastolic;
        this.bmi = bmi;
    }

    /**
     * <p>
     * Constructor method for BpLogDTO class.
     * Initializes id, systolic and diastolic values, pulse, creation and BP taken dates.
     * </p>
     *
     * @param id           - The id of the blood pressure log.
     * @param avgSystolic  - The average systolic value of the blood pressure.
     * @param avgDiastolic - The average diastolic value of the blood pressure.
     * @param avgPulse     - The average pulse value of the blood pressure.
     * @param createdAt    - The date the log was created.
     * @param bpTakenOn    - The date the blood pressure was taken.
     */
    public BpLogDTO(Long id, Integer avgSystolic, Integer avgDiastolic,
                    Integer avgPulse, Date createdAt, Date bpTakenOn) {
        super();
        this.id = id;
        this.avgSystolic = avgSystolic;
        this.avgDiastolic = avgDiastolic;
        this.avgPulse = avgPulse;
        this.createdAt = createdAt;
        this.bpTakenOn = bpTakenOn;
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