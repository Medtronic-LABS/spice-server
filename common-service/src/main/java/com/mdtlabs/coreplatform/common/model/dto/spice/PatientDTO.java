package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * The PatientDTO class represents a patient's information including personal details, medical history,
 * and screening results.
 * </p>
 */
@Data
public class PatientDTO {
    private Long id;
    private String nationalId;
    private String firstName;
    private String lastName;
    private String gender;
    private Integer age;
    private Date enrolledAt;
    private Boolean isPregnant;
    private String phoneNumber;
    private Boolean isRegularSmoker;
    private Long programId;
    private Date lastAssesmentDate;
    private String cvdRiskLevel;
    private int cvdRiskScore;
    private int avgSystolic;
    private int avgDiastolic;
    private float bmi;
    private boolean isInitialReview;
    private boolean isConfirmDiagnosis;
    private float height;
    private float weight;
    private boolean isPhq9;
    private boolean isGad7;
    private boolean isRedRiskPatient;
    private Long screeningId;
}
