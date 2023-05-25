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
 * PatientComplication is a Java class representing a patient complication entity with various fields and constructors.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_PATIENT_COMPLICATION)
public class PatientComplication extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    @Column(name = FieldConstants.PATIENT_VISIT_ID)
    private Long patientVisitId;

    @Column(name = FieldConstants.COMPLICATION_ID)
    private Long complicationId;

    @Column(name = FieldConstants.OTHER_COMPLICATION)
    private String otherComplication;

    public PatientComplication(Long tenantId, Long patientTrackId, Long patientVisitId, Long complicationId,
                               String otherComplication) {
        this.tenantId = tenantId;
        this.patientTrackId = patientTrackId;
        this.patientVisitId = patientVisitId;
        this.complicationId = complicationId;
        this.otherComplication = otherComplication;
    }

    public PatientComplication() {

    }

    /**
     * <p>
     * This function sets the values of three Long variables.
     * </p>
     *
     * @param tenantId       {@link Long} The ID of the tenant that the patient belongs to is given
     * @param patientTrackId {@link Long} The ID of a patient's track is given
     * @param patientVisitId {@link Long} The ID for a specific visit made by a patient is given
     */
    public void setIds(Long tenantId, Long patientTrackId, Long patientVisitId) {
        this.tenantId = tenantId;
        this.patientTrackId = patientTrackId;
        this.patientVisitId = patientVisitId;
    }
}
