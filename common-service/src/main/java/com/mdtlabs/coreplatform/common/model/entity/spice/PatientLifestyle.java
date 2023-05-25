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
 * PatientLifestyle is a Java class representing a patient's lifestyle information with fields
 * for patient track ID, visit ID, lifestyle ID, answer, and comments.
 * </p>
 *
 * @author ArunKarthik created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_PATIENT_LIFESTYLE)
public class PatientLifestyle extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;
    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;
    @Column(name = FieldConstants.PATIENT_VISIT_ID)
    private Long patientVisitId;
    @Column(name = FieldConstants.LIFESTYLE_ID)
    private Long lifestyleId;
    @Column(name = FieldConstants.ANSWER)
    private String answer;
    @Column(name = FieldConstants.COMMENTS)
    private String comments;

    public PatientLifestyle() {
    }

    public PatientLifestyle(Long tenantId, Long patientTrackId, Long patientVisitId, Long lifestyleId, String answer,
                            String comments) {
        this.tenantId = tenantId;
        this.patientTrackId = patientTrackId;
        this.patientVisitId = patientVisitId;
        this.lifestyleId = lifestyleId;
        this.answer = answer;
        this.comments = comments;
    }
}
