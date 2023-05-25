package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * PatientLabTest is a Java class representing a patient's lab test with various fields
 * such as lab test ID, name, result date, and more.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_PATIENT_LAB_TEST)
public class PatientLabTest extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = ErrorConstants.LAB_TEST_ID_NOT_NULL)
    @Column(name = FieldConstants.LAB_TEST_ID)
    private Long labTestId;

    @Column(name = FieldConstants.LAB_TEST_NAME)
    private String labTestName;

    @Column(name = FieldConstants.RESULT_DATE)
    private Date resultDate;

    @Column(name = FieldConstants.REFERRED_BY)
    private Long referredBy;

    @NotNull(message = ErrorConstants.IS_REVIEWED_NOT_NULL)
    @Column(name = FieldConstants.IS_REVIEWED)
    private Boolean isReviewed;

    @Column(name = FieldConstants.IS_ABNORMAL)
    private Boolean isAbnormal;

    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    @Column(name = FieldConstants.PATIENT_VISIT_ID)
    private Long patientVisitId;

    @Column(name = FieldConstants.RESULT_UPDATE_BY)
    private Long resultUpdateBy;

    @Column(name = FieldConstants.COMMENT)
    private String comment;
}
