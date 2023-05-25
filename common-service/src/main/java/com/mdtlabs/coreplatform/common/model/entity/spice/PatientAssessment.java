package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * PatientAssessment is a Java class representing a patient assessment entity with fields for
 * blood pressure log ID, glucose log ID, type, tenant ID, and patient track ID.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_PATIENT_ASSESSMENT)
public class PatientAssessment extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.BP_LOG_ID)
    private Long bpLogId;

    @Column(name = FieldConstants.GLUCOSE_LOG_ID)
    private Long glucoseLogId;

    @Column(name = FieldConstants.TYPE)
    private String type;

    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    public PatientAssessment() {
    }

    public PatientAssessment(Long bpLogId, Long glucoseLogId, String type, Long tenantId, Long patientTrackId) {
        super();
        this.bpLogId = bpLogId;
        this.glucoseLogId = glucoseLogId;
        this.type = type;
        this.tenantId = tenantId;
        this.patientTrackId = patientTrackId;
    }
}
