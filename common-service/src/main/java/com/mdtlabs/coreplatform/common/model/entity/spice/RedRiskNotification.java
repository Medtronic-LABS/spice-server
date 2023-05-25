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
 * RedRiskNotification is a Java class representing a Red Risk Notification entity with various fields
 * such as bpLogId, glucoseLogId, patientTrackId, status, and assessmentLogId.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Table(name = TableConstants.TABLE_RED_RISK_NOTIFICATION)
@Entity
public class RedRiskNotification extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.BP_LOG_ID)
    private Long bpLogId;

    @Column(name = FieldConstants.GLUCOSE_LOG_ID)
    private Long glucoseLogId;

    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    @Column(name = FieldConstants.STATUS)
    private String status;

    @Column(name = FieldConstants.PATIENT_ASSESSMENT_ID)
    private Long assessmentLogId;
}
