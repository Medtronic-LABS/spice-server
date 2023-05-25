package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.util.Date;
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
 * PatientNutritionLifestyle is a Java class representing a patient's nutrition and lifestyle information,
 * including assessments, notes, and referred and assessed dates.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_PATIENT_NUTRITION_LIFESTYLE)
public class PatientNutritionLifestyle extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = TableConstants.TABLE_PATIENT_NUTRITION_LIFESTYLE_LIFESTYLE, joinColumns = {
            @JoinColumn(name = FieldConstants.PATIENT_NUTRITION_LIFESTYLE_ID)}, inverseJoinColumns = {@JoinColumn(name = FieldConstants.NUTRITION_LIFESTYLE_ID)})
    private Set<NutritionLifestyle> lifestyles;

    @Column(name = FieldConstants.LIFESTYLE_ASSESSMENT)
    private String lifestyleAssessment;

    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    @Column(name = FieldConstants.PATIENT_VISIT_ID)
    private Long patientVisitId;

    @Column(name = FieldConstants.REFERRED_BY)
    private Long referredBy;

    @Column(name = FieldConstants.REFERRED_DATE)
    private Date referredDate;

    @Column(name = FieldConstants.ASSESSED_BY)
    private Long assessedBy;

    @Column(name = FieldConstants.ASSESSED_DATE)
    private Date assessedDate;

    @Column(name = FieldConstants.CLINICIAN_NOTE)
    private String clinicianNote;

    @Column(name = FieldConstants.OTHER_NOTE)
    private String otherNote;

    @Column(name = FieldConstants.IS_VIEWED)
    private boolean isViewed;
}
