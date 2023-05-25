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
 * PatientSymptom is a Java class representing a patient symptom entity with various fields such as name, type, patientTrackId,
 * symptomId, and log IDs for blood pressure, glucose, and patient assessment.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_PATIENT_SYMPTOM)
public class PatientSymptom extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    // @NotNull
    @Column(name = FieldConstants.NAME)
    private String name;

    //    @NotNull
    @Column(name = FieldConstants.TYPE)
    private String type;

    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    @Column(name = FieldConstants.SYMPTOM_ID)
    private Long symptomId;

    @Column(name = FieldConstants.OTHER_SYMPTOM)
    private String otherSymptom;

    @Column(name = FieldConstants.BP_LOG_ID)
    private Long bpLogId;

    @Column(name = FieldConstants.GLUCOSE_LOG_ID)
    private Long glucoseLogId;

    @Column(name = FieldConstants.PATIENT_ASSESSMENT_ID)
    private Long assessmentLogId;
}
