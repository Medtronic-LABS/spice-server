package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * PrescriptionHistory is a Java class representing prescription history with various fields
 * such as medication name, dosage, frequency, and signature.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_PRESCRIPTION_HISTORY)
public class PrescriptionHistory extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = -3285039992579728818L;

    @Column(name = FieldConstants.PRESCRIPTION_ID)
    private Long prescriptionId;

    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    @Column(name = FieldConstants.PATIENT_VISIT_ID)
    private Long patientVisitId;

    @Column(name = FieldConstants.MEDICATION_NAME)
    private String medicationName;

    @Column(name = FieldConstants.MEDICATION_ID)
    private Long medicationId;

    @Column(name = FieldConstants.BRAND_NAME)
    private String brandName;

    @Column(name = FieldConstants.CLASSIFICATION_NAME)
    private String classificationName;

    @Column(name = FieldConstants.DOSAGE_FORM_NAME)
    private String dosageFormName;

    @Column(name = FieldConstants.DOSAGE_UNIT_NAME)
    private String dosageUnitName;

    @Column(name = FieldConstants.DOSAGE_UNIT_VALUE)
    private String dosageUnitValue;

    @Column(name = FieldConstants.DOSAGE_UNIT_ID)
    private Long dosageUnitId;

    @Column(name = FieldConstants.DOSAGE_FREQUENCY_NAME)
    private String dosageFrequencyName;

    @Column(name = FieldConstants.DOSAGE_FREQUENCY_ID)
    private Long dosageFrequencyId;

    @Column(name = FieldConstants.PRESCRIBED_DAYS)
    private Integer prescribedDays;

    @Column(name = FieldConstants.END_DATE, columnDefinition = "TIMESTAMP")
    private Date endDate;

    @Column(name = FieldConstants.INSTRUCTION_NOTE)
    private String instructionNote;

    @NotNull
    @Column(name = FieldConstants.SIGNATURE)
    private String signature;

    @Column(name = FieldConstants.REMAINING_PRESCRIPTION_DAYS)
    private Integer remainingPrescriptionDays;

    @Column(name = FieldConstants.PRESCRIPTION_FILLED_DAYS)
    private Integer prescriptionFilledDays = 0;

    @Column(name = FieldConstants.REASON)
    private String reason;

    @Column(name = FieldConstants.LAST_REFILL_DATE, columnDefinition = "TIMESTAMP")
    private Date lastRefillDate;
}
