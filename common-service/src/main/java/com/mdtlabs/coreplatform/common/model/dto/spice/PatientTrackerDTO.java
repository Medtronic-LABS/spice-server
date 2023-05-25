package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLogDetails;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * This class is a DTO for PatientTracker entity.
 * </p>
 *
 * @author Karthick M created on Feb 07, 2023
 */
@Data
public class PatientTrackerDTO {

    private Long id;

    private Long tenantId;

    private Long createdBy = getUserValue();

    private Long updatedBy = getUserValue();

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    private boolean isActive;

    private boolean isDeleted;

    private String nationalId;

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private Integer age;

    private String gender;

    private String phoneNumber;

    private String phoneNumberCategory;

    private Double height;

    private Double weight;

    private Double bmi;

    private Boolean isRegularSmoker;

    private Long countryId;

    private Long siteId;

    private Long operatingUnitId;

    private Long accountId;

    private Long programId;

    private Integer avgSystolic;

    private Integer avgDiastolic;

    private Integer avgPulse;

    private String glucoseUnit;

    private String glucoseType;

    private Double glucoseValue;

    private String cvdRiskLevel;

    private Integer cvdRiskScore;

    private Long screeningLogId;

    private Date nextMedicalReviewDate;

    private Date nextBpAssessmentDate;

    private Date nextBgAssessmentDate;

    private Long patientId;

    private String patientStatus;

    private Boolean isObservation;

    private Boolean isScreening;

    private Boolean screeningReferral;

    private Date enrollmentAt;

    private Date lastReviewDate;

    private Date lastAssessmentDate;

    private Boolean isConfirmDiagnosis;

    private String diagnosisComments;

    private List<String> confirmDiagnosis;

    private List<String> provisionalDiagnosis;

    private Integer phq4Score;

    private boolean isInitialReview;

    private String phq4RiskLevel;

    private Integer phq4FirstScore;

    private Integer phq4SecondScore;

    private Integer phq9Score;

    private String phq9RiskLevel;

    private Integer gad7Score;

    private String gad7RiskLevel;

    private String riskLevel;

    private Date lastMenstrualPeriodDate;

    private Boolean isPregnant;

    private Date estimatedDeliveryDate;

    private boolean isRedRiskPatient;

    private Boolean isPhq9 = false;

    private Boolean isGad7 = false;

    private boolean isLabtestReferred;

    private boolean isMedicationPrescribed;

    private Date lastMedicationPrescribedDate;

    private Boolean isHtnDiagnosis;

    private List<BpLogDetails> bpLogDetails;

    private GlucoseLogDTO glucoseLogDetails;

    private PrescriberDTO prescriberDetails;

    private Boolean isDiabetesDiagnosis;

    private List<LifestyleDTO> patientLifestyles;

    private Date lastLabtestReferredDate;

    private long totalCount;

    private String deleteReason;

    private String deleteOtherReason;

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
