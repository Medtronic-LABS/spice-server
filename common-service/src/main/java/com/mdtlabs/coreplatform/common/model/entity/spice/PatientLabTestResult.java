package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * PatientLabTestResult is a Java class representing a patient's lab test result with various fields such
 * as result name, value, unit, status, and IDs for tracking and ordering.
 * </p>
 *
 * @author ArunKarthik created on Jun 30, 2022
 */
@Data
@Table(name = TableConstants.TABLE_PATIENT_LAB_TEST_RESULT)
@Entity
public class PatientLabTestResult extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Column(name = FieldConstants.RESULT_NAME)
    private String resultName;

    @Column(name = FieldConstants.DISPLAY_NAME)
    private String displayName;

    @NotBlank
    @Column(name = FieldConstants.RESULT_VALUE)
    private String resultValue;

    @NotBlank
    @Column(name = FieldConstants.UNIT)
    private String unit;

    @Column(name = FieldConstants.RESULT_STATUS)
    private String resultStatus;

    @Column(name = FieldConstants.IS_ABNORMAL)
    private Boolean isAbnormal;

    @Column(name = FieldConstants.LAB_TEST_RESULT_ID)
    private Long labTestResultId;

    @Column(name = FieldConstants.PATIENT_LAB_TEST_ID)
    private Long patientLabTestId;

    @Column(name = FieldConstants.LAB_TEST_ID)
    private Long labTestId;

    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    @Column(name = FieldConstants.PATIENT_VISIT_ID)
    private Long patientVisitId;

    @Column(name = FieldConstants.DISPLAY_ORDER)
    private Integer displayOrder;
}
