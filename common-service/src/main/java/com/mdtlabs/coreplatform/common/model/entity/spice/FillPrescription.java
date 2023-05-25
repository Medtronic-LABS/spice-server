package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;

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
 * FillPrescription is a Java class representing a prescription that has been filled,
 * with various fields including patient visit ID, prescription ID, and
 * remaining prescription days.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_FILL_PRESCRIPTION)
public class FillPrescription extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 5904531222309344526L;

    @Column(name = FieldConstants.PATIENT_VISIT_ID)
    private Long patientVisitId;

    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    @Column(name = FieldConstants.PRESCRIPTION_ID)
    private Long prescriptionId;

    @Column(name = FieldConstants.PRESCRIBED_DAYS)
    private Integer prescribedDays;

    @Column(name = FieldConstants.REMAINING_PRESCRIPTION_DAYS)
    private Integer remainingPrescriptionDays;

    @Column(name = FieldConstants.PRESCRIPTION_FILLED_DAYS)
    private Integer prescriptionFilledDays;

    @Column(name = FieldConstants.IS_ACTIVE)
    private Boolean isActive = true;

    @Column(name = FieldConstants.IS_DELETED)
    private Boolean isDeleted = false;

    @Transient
    private String instructionNote;

    @Transient
    private Boolean isInstructionUpdated;
}
