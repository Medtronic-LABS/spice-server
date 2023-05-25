package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * Patient is a Java class representing a patient entity with various attributes and methods for setting some
 * attributes in uppercase.
 * </p>
 *
 * @author Niraimathi S created on Feb 07, 2023
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_PATIENT)
public class Patient extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.NATIONAL_ID)
    private String nationalId;

    @Column(name = FieldConstants.FIRST_NAME)
    private String firstName;

    @Column(name = FieldConstants.MIDDLE_NAME)
    private String middleName;

    @Column(name = FieldConstants.LAST_NAME)
    private String lastName;

    @Column(name = FieldConstants.GENDER)
    private String gender;

    @Column(name = FieldConstants.DATE_OF_BIRTH)
    private Date dateOfBirth;

    @Column(name = FieldConstants.AGE)
    private Integer age;

    @Column(name = FieldConstants.IS_PREGNANT)
    private Boolean isPregnant;

    @Column(name = FieldConstants.PHONE_NUMBER)
    private String phoneNumber;

    @Column(name = FieldConstants.PHONE_NUMBER_CATEGORY)
    private String phoneNumberCategory;

    @NotNull
    @Column(name = FieldConstants.COUNTRY_ID)
    private Long countryId;

    @NotNull
    @Column(name = FieldConstants.COUNTY_ID)
    private Long countyId;

    @NotNull
    @Column(name = FieldConstants.SUB_COUNTY_ID)
    private Long subCountyId;

    @NotNull
    @Column(name = FieldConstants.SITE_ID)
    private Long siteId;

    @Column(name = FieldConstants.LANDMARK)
    private String landmark;

    @Column(name = FieldConstants.OCCUPATION)
    private String occupation;

    @Column(name = FieldConstants.LEVEL_OF_EDUCATION)
    private String levelOfEducation;

    @Column(name = FieldConstants.INSURANCE_STATUS)
    private Boolean insuranceStatus;

    @Column(name = FieldConstants.INSURANCE_TYPE)
    private String insuranceType;

    @Column(name = FieldConstants.INSURANCE_ID)
    private String insuranceId;

    @Column(name = FieldConstants.OTHER_INSURANCE)
    private String otherInsurance;

    @Column(name = FieldConstants.IS_SUPPORT_GROUP)
    private Boolean isSupportGroup;

    @Column(name = FieldConstants.SUPPORT_GROUP)
    private String supportGroup;

    @Column(name = FieldConstants.IS_REGULAR_SMOKER)
    private Boolean isRegularSmoker;

    @Column(name = FieldConstants.PROGRAM_ID)
    private Long programId;

    @Column(name = FieldConstants.INITIAL)
    private String initial;

    @Column(name = FieldConstants.OTHER_ID_TYPE)
    private String otherIdType;

    @Column(name = FieldConstants.LANGUAGES)
    private String languages;

    @Column(name = FieldConstants.ETHNICITY)
    private String ethnicity;

    @Column(name = FieldConstants.ID_TYPE)
    private String idType;

    @Column(name = FieldConstants.OTHER_LANGUAGES)
    private String otherLanguages;

    @Column(name = FieldConstants.ER_VISIT_REASON)
    private String erVisitReason;

    @Column(name = FieldConstants.LOTE)
    private Boolean lote;

    @Column(name = FieldConstants.HOME_MEDICAL_DEVICES)
    private String homeMedicalDevices;

    @Column(name = FieldConstants.ER_VISIT_FREQUENCY)
    private Integer erVisitFrequency;

    @Column(name = FieldConstants.EMR_NUMBER)
    private Long emrNumber;

    @Column(name = FieldConstants.IS_ER_VISIT_HISTORY)
    private Boolean isErVisitHistory;

    @Column(name = FieldConstants.ZIP_CODE)
    private Long zipCode;

    @Column(name = FieldConstants.VIRTUAL_ID)
    private Long virtualId;

    /**
     * <p>
     * This function sets the national ID of the patient object and converts it to uppercase.
     * </p>
     *
     * @param nationalId {@link String} The national ID of the patient object is given
     */
    public void setNationalId(String nationalId) {
        this.nationalId = nationalId.toUpperCase();
    }

    /**
     * <p>
     * This function sets the first name of the patient object and converts it to uppercase.
     * </p>
     *
     * @param firstName {@link String} The first name of a patient is given
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName.toUpperCase();
    }

    /**
     * <p>
     * This function sets the middle name of the patient object to uppercase if it is not null.
     * </p>
     *
     * @param middleName The middle name of a patient is given
     */
    public void setMiddleName(String middleName) {
        if (!Objects.isNull(middleName)) {
            this.middleName = middleName.toUpperCase();
        }
    }

    /**
     * <p>
     * This function sets the last name of the patient object and converts it to uppercase.
     * </p>
     *
     * @param lastName {@link String} The last name of a patient is given
     */
    public void setLastName(String lastName) {
        this.lastName = lastName.toUpperCase();
    }
}
