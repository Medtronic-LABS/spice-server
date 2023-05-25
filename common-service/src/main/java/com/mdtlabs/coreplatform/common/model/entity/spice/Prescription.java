package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * Prescription is a Java class representing a Prescription entity with various fields such as medication name,
 * dosage, frequency, and signature.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@DynamicUpdate
@Table(name = TableConstants.PRESCRIPTION)
public class Prescription extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 5953566040431779092L;

    @NotNull
    @Column(name = FieldConstants.PATIENT_VISIT_ID)
    private Long patientVisitId;

    @NotNull
    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    @Column(name = FieldConstants.MEDICATION_NAME)
    private String medicationName;

    @NotNull
    @Column(name = FieldConstants.MEDICATION_ID)
    private Long medicationId;

    @Column(name = FieldConstants.CLASSIFICATION_NAME)
    private String classificationName;

    @Column(name = FieldConstants.BRAND_NAME)
    private String brandName;

    @Column(name = FieldConstants.DOSAGE_FORM_NAME)
    private String dosageFormName;

    @Column(name = FieldConstants.DOSAGE_UNIT_NAME)
    private String dosageUnitName;

    @Column(name = FieldConstants.DOSAGE_UNIT_VALUE)
    private String dosageUnitValue;

    @Column(name = FieldConstants.DOSAGE_UNIT_ID)
    private Long dosageUnitId;

    @Column(name = FieldConstants.DOSAGE_FREQUENCY_ID)
    private Long dosageFrequencyId;

    @Column(name = FieldConstants.DOSAGE_FREQUENCY_NAME)
    private String dosageFrequencyName;

    @Column(name = FieldConstants.PRESCRIBED_DAYS)
    private Integer prescribedDays;

    @NotNull
    @Column(name = FieldConstants.END_DATE, columnDefinition = "TIMESTAMP")
    private Date endDate;

    @Column(name = FieldConstants.INSTRUCTION_NOTE)
    private String instructionNote;

    @NotNull
    @Column(name = FieldConstants.SIGNATURE)
    private String signature;

    @Column(name = FieldConstants.DISCONTINUED_REASON)
    private String discontinuedReason;

    @Column(name = FieldConstants.DISCONTINUED_ON, columnDefinition = "TIMESTAMP")
    private Date discontinuedOn;

    @Column(name = FieldConstants.REMAINING_PRESCRIPTION_DAYS)
    private Integer remainingPrescriptionDays = 0;

    @Column(name = FieldConstants.PRESCRIPTION_FILLED_DAYS)
    private Integer prescriptionFilledDays = 0;

    @Column(name = FieldConstants.REASON)
    private String reason;
}
