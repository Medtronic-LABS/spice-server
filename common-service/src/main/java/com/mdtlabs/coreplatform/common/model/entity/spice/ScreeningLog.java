package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.dto.spice.GlucoseLogDTO;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * ScreeningLog is a Java class representing a screening log entity with various fields and annotations.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_SCREENING_LOG)
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
public class ScreeningLog extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = FieldConstants.LATITUDE)
    private String latitude;

    @NotNull
    @Column(name = FieldConstants.LONGITUDE)
    private String longitude;

    @NotNull
    @Column(name = FieldConstants.FIRST_NAME)
    private String firstName;

    @Column(name = FieldConstants.MIDDLE_NAME)
    private String middleName;

    @NotNull
    @Column(name = FieldConstants.LAST_NAME)
    private String lastName;

    @NotNull
    @Column(name = FieldConstants.NATIONAL_ID)
    private String nationalId;

    @NotNull
    @Column(name = FieldConstants.PHONE_NUMBER)
    private String phoneNumber;

    @NotNull
    @Column(name = FieldConstants.PHONE_NUMBER_CATEGORY)
    private String phoneNumberCategory;

    @Column(name = FieldConstants.LANDMARK)
    private String landmark;

    @NotNull
    @Column(name = FieldConstants.GENDER)
    private String gender;

    @Column(name = FieldConstants.DATE_OF_BIRTH)
    private Date dateOfBirth;

    @NotNull
    @Column(name = FieldConstants.AGE)
    private Integer age;

    @Column(name = FieldConstants.HEIGHT)
    private Double height;

    @Column(name = FieldConstants.WEIGHT)
    private Double weight;

    @Column(name = FieldConstants.BMI)
    private Double bmi;

    @NotNull
    @Column(name = FieldConstants.IS_REGULAR_SMOKER)
    private Boolean isRegularSmoker;

    @Column(name = FieldConstants.COUNTRY_ID)
    private Long countryId;

    @Column(name = FieldConstants.COUNTY_ID)
    private Long countyId;

    @Column(name = FieldConstants.SUB_COUNTY_ID)
    private Long subCountyId;

    @NotNull
    @Column(name = FieldConstants.CATEGORY)
    private String category;

    @NotNull
    @Column(name = FieldConstants.AVG_SYSTOLIC)
    private Integer avgSystolic;

    @NotNull
    @Column(name = FieldConstants.AVG_DIASTOLIC)
    private Integer avgDiastolic;

    @Column(name = FieldConstants.AVG_PULSE)
    private Integer avgPulse;

    @Column(name = FieldConstants.GLUCOSE_TYPE)
    private String glucoseType;

    @Column(name = FieldConstants.GLUCOSE_VALUE)
    private Double glucoseValue;

    @Column(name = FieldConstants.GLUCOSE_UNIT)
    private String glucoseUnit;

    @Column(name = FieldConstants.LAST_MEAL_TIME)
    private Date lastMealTime;

    @Column(name = FieldConstants.GLUCOSE_DATE_TIME)
    private Date glucoseDateTime;

    @Column(name = FieldConstants.IS_BEFORE_DIABETIC_DIAGNOSIS)
    private Boolean isBeforeDiabetesDiagnosis;

    @Column(name = FieldConstants.PHQ4_SCORE)
    private Integer phq4score;

    @Column(name = FieldConstants.PHQ4_RISK_LEVEL)
    private String phq4RiskLevel;

    @Column(name = FieldConstants.CVD_RISK_LEVEL)
    private String cvdRiskLevel;

    @Column(name = FieldConstants.CVD_RISK_SCORE)
    private Integer cvdRiskScore;

    @NotNull
    @Column(name = FieldConstants.REFER_ASSESSMENT)
    private Boolean isReferAssessment;

    @Column(name = FieldConstants.IS_LATEST)
    private Boolean isLatest;

    @Column(name = FieldConstants.IS_BEFORE_HTN_DIAGNOSIS)
    private Boolean isBeforeHtnDiagnosis;

    @Column(name = FieldConstants.SITE_ID)
    private Long siteId;

    @Column(name = FieldConstants.OPERATING_UNIT)
    private Long operatingUnitId;

    @Column(name = FieldConstants.ACCOUNT_ID)
    private Long accountId;

    @Column(name = FieldConstants.BP_ARM)
    private String bpArm;

    @Column(name = FieldConstants.BP_POSITION)
    private String bpPosition;

    @Column(name = FieldConstants.PHYSICALLY_ACTIVE)
    private Boolean physicallyActive;

    @Column(name = FieldConstants.TYPE)
    private String type;

    @Column(name = FieldConstants.DEVICE_INFO_ID)
    private Long deviceInfoId;

    @Type(type = "jsonb")
    @Column(name = FieldConstants.PHQ4_MENTAL_HEALTH, columnDefinition = "jsonb")
    private List<MentalHealthDetails> phq4MentalHealth;

    @Column(name = FieldConstants.IS_BEFORE_GESTATIONAL_DIABETES)
    private boolean isBeforeGestationalDiabetes;

    @Column(name = FieldConstants.ID_TYPE)
    private String idType;

    @Column(name = FieldConstants.OTHER_ID_TYPE)
    private String otherIdType;

    @Column(name = FieldConstants.IS_FAMILY_DIABETES_HISTORY)
    private Boolean isFamilyDiabetesHistory;

    @Column(name = FieldConstants.PREFERRED_NAME)
    private String preferredName;

    @Column(name = FieldConstants.SCREENING_DATE_TIME)
    private Date screeningDateTime;

    @Type(type = FieldConstants.JSONB)
    @Column(name = FieldConstants.BP_LOG_DETAILS, columnDefinition = FieldConstants.JSONB)
    private List<BpLogDetails> bpLogDetails;

    @Transient
    private GlucoseLogDTO glucoseLog;

    @Transient
    private Long bpLogId;
}
