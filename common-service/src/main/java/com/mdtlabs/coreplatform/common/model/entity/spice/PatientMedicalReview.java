package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * PatientMedicalReview is a Java class representing a patient's medical review,
 * including physical exams, complaints, and clinical notes.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_PATIENT_MEDICAL_REVIEW)
public class PatientMedicalReview extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    @Column(name = FieldConstants.PATIENT_VISIT_ID)
    private Long patientVisitId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = TableConstants.TABLE_PATIENT_MEDICAL_REVIEW_PHYSICAL_EXAMINATION, joinColumns = {
            @JoinColumn(name = FieldConstants.PATIENT_MEDICAL_REVIEW_ID)}, inverseJoinColumns = {@JoinColumn(name = FieldConstants.PHYSICAL_EXAM_ID)})
    private Set<PhysicalExamination> physicalExams;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = TableConstants.TABLE_PATIENT_MEDICAL_REVIEW_COMPLAINTS, joinColumns = {
            @JoinColumn(name = FieldConstants.PATIENT_MEDICAL_REVIEW_ID)}, inverseJoinColumns = {@JoinColumn(name = FieldConstants.COMPLIANT_ID)})
    private Set<Complaints> complaints;

    @Column(name = FieldConstants.PHYSICAL_EXAM_COMMENTS)
    private String physicalExamComments;

    @Column(name = FieldConstants.COMPLIANT_COMMENTS)
    private String compliantComments;

    @Column(name = FieldConstants.CLINICAL_NOTE)
    private String clinicalNote;
}
