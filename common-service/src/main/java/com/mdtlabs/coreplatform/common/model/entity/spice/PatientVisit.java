package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * PatientVisit is a Java class representing a patient visit with various attributes such as visit date,
 * prescription status, investigation status, and medical review status.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_PATIENT_VISIT)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientVisit extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    @Column(name = FieldConstants.VISIT_DATE, columnDefinition = "TIMESTAMP")
    private Date visitDate;

    @Column(name = FieldConstants.IS_PRESCRIPTION)
    private boolean isPrescription;

    @Column(name = FieldConstants.IS_INVESTIGATION)
    private boolean isInvestigation;

    @Column(name = FieldConstants.IS_MEDICAL_REVIEW)
    private boolean isMedicalReview;
}
