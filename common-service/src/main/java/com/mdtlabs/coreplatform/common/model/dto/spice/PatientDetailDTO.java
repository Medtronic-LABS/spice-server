package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * This class contains fields needed for patient details. It is an DTO for
 * Patient entity.
 * </p>
 *
 * @author Niraimathi S created on Feb 07, 2023
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientDetailDTO {

    private Long id;

    private String nationalId;

    private String firstName;

    private String middleName;

    private String lastName;

    private String gender;

    private Date dateOfBirth;

    private Integer age;

    private Boolean isPregnant;

    private String phoneNumber;

    private String phoneNumberCategory;

    private Long countryId;

    private Long countyId;

    private Long subCountyId;

    private Long siteId;

    private String landmark;

    private String occupation;

    private String levelOfEducation;

    private Boolean insuranceStatus;

    private String insuranceType;

    private String insuranceId;

    private String otherInsurance;

    private Boolean isSupportGroup;

    private String supportGroup;

    private Boolean isRegularSmoker;

    private Long programId;

    private String initial;

    private String otherIdType;

    private String languages;

    private String ethnicity;

    private String idType;

    private String otherLanguages;

    private Boolean lote;

    private String homeMedicalDevices;

    private long emrNumber;

    private String zipCode;

    private String patientStatus;

    private String siteName;

    private Date enrollmentDate;

    private Long virtualId;

    private Float height;

    private Float weight;

    private Float bmi;

    private Long patientTrackId;
}