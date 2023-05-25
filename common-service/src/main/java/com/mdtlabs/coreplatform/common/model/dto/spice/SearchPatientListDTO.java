package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.util.ConversionUtil;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * Fields needs to be listed while searching a patient.
 * </p>
 *
 * @author Jeyaharini T A
 */
@Data
public class SearchPatientListDTO {

    private Long id;

    private String nationalId;

    private Long programId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String gender;

    private int age;

    private boolean isInitialReview;

    private String patientStatus;

    private boolean isRedRiskPatient;

    private Date enrollmentAt;

    private Boolean isRegularSmoker;

    private Boolean isConfirmDiagnosis;

    private Float bmi;

    private Float weight;

    private Float height;

    private Long screeningLogId;

    private Date createdAt;

    private Long patientId;

    public void setAge(int age) {
        this.age = age;
        if (!Objects.isNull(this.enrollmentAt)) {
            this.age = ConversionUtil.calculatePatientAge(this.age, this.enrollmentAt);
        } else if (!Objects.isNull(this.createdAt)) {
            this.age = ConversionUtil.calculatePatientAge(this.age, this.createdAt);
        }
    }

}
