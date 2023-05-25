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
 * PatientCurrentMedication is a Java class representing a patient's current medication
 * information with various fields and constructors.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_PATIENT_CURRENT_MEDICATION)
public class PatientCurrentMedication extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.CURRENT_MEDICATION_ID)
    private Long currentMedicationId;

    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    @Column(name = FieldConstants.PATIENT_VISIT_ID)
    private Long patientVistId;

    @Column(name = FieldConstants.IS_DRUG_ALLERGIES)
    private boolean isDrugAllergies;

    @Column(name = FieldConstants.IS_ADHERING_CURRENT_MED)
    private boolean isAdheringCurrentMed;

    @Column(name = FieldConstants.ADHERING_MED_COMMENT)
    private String adheringMedComment;

    @Column(name = FieldConstants.ALLERGIES_COMMENT)
    private String allergiesComment;

    public PatientCurrentMedication(Long tenantId, Long currentMedicationId, Long patientTrackId,
                                    Long patientVistId, boolean isDrugAllergies, boolean isAdheringCurrentMed, String adheringMedComment,
                                    String allergiesComment) {
        this.tenantId = tenantId;
        this.currentMedicationId = currentMedicationId;
        this.patientTrackId = patientTrackId;
        this.patientVistId = patientVistId;
        this.isDrugAllergies = isDrugAllergies;
        this.isAdheringCurrentMed = isAdheringCurrentMed;
        this.adheringMedComment = adheringMedComment;
        this.allergiesComment = allergiesComment;
    }

    public PatientCurrentMedication() {

    }
}
