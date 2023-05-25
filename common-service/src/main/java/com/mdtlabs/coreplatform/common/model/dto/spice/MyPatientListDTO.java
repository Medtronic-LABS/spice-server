package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.util.ConversionUtil;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * Fields needs to be listed in the My patients list.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 07, 2023
 */
@Data
public class MyPatientListDTO {

    private Long id;

    private String nationalId;

    private Long programId;

    private String firstName;

    private String lastName;

    private String gender;

    private Integer age;

    private boolean isInitialReview;

    private String patientStatus;

    private boolean isRedRiskPatient;

    private Date enrollmentAt;

    private Long patientId;

    private Long screeningLogId;

    private Date createdAt;

    /**
     * <p>
     * This function sets the age of a patient and calculates their age based on their enrollment or
     * creation date if available.
     * </p>
     *
     * @param age The age of the patient. It is of type Integer.
     */
    public void setAge(Integer age) {
        this.age = age;
        if (!Objects.isNull(this.enrollmentAt)) {
            this.age = ConversionUtil.calculatePatientAge(this.age, this.enrollmentAt);
        } else if (!Objects.isNull(this.createdAt)) {
            this.age = ConversionUtil.calculatePatientAge(this.age, this.createdAt);
        }
    }

}
