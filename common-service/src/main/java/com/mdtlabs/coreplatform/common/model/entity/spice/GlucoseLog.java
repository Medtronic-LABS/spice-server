package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * GlucoseLog is a Java class representing a Glucose Log entity with various fields for database mapping.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_GLUCOSE_LOG)
public class GlucoseLog extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.HBA1C)
    private Double hba1c;

    @Column(name = FieldConstants.GLUCOSE_TYPE)
    private String glucoseType;

    @Column(name = FieldConstants.GLUCOSE_VALUE)
    private Double glucoseValue;

    @Column(name = FieldConstants.LAST_MEAL_TIME)
    private Date lastMealTime;

    @Column(name = FieldConstants.GLUCOSE_DATE_TIME)
    private Date glucoseDateTime;

    @Column(name = FieldConstants.GLUCOSE_UNIT)
    private String glucoseUnit;

    @Column(name = FieldConstants.HBA1C_UNIT)
    private String hba1cUnit;

    @Column(name = FieldConstants.TYPE)
    private String type;

    @Column(name = FieldConstants.IS_LATEST)
    private boolean isLatest;

    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    @Column(name = FieldConstants.IS_UPDATED_FROM_ENROLLMENT)
    private boolean isUpdatedFromEnrollment;

    @Column(name = FieldConstants.SCREENING_ID)
    private Long screeningId;

    @Column(name = FieldConstants.ASSESSMENT_TENANT_ID)
    private Long assessmentTenantId;

    @Column(name = FieldConstants.BG_TAKEN_ON)
    private Date bgTakenOn;

    @Column(name = FieldConstants.IS_OLD_RECORD)
    private boolean isOldRecord;

    @Transient
    private Boolean isBeforeDiabetesDiagnosis;

    @Transient
    private Long glucoseLogId;

    @Transient
    private String riskLevel;
}