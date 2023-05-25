package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * PatientPregnancyDetails is a Java class representing patient pregnancy details with various fields
 * such as pregnancy fetuses number, gravida, parity, temperature, last menstrual period date,
 * estimated delivery date, diagnosis, and patient track ID.
 * </p>
 *
 * @author Niraimathi S created on Feb 07, 2023
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_PATIENT_PREGNANCY_DETAILS)
@TypeDef(name = Constants.LIST_ARRAY, typeClass = ListArrayType.class)
public class PatientPregnancyDetails extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.PREGNANCY_FETUSES_NUMBER)
    private int pregnancyFetusesNumber;

    @Column(name = FieldConstants.GRAVIDA)
    private int gravida;

    @Column(name = FieldConstants.PARITY)
    private int parity;

    @Column(name = FieldConstants.TEMPERATURE)
    private Double temperature;

    @Column(name = FieldConstants.LAST_MENSTRUAL_PERIOD_DATE)
    private Date lastMenstrualPeriodDate;

    @Column(name = FieldConstants.ESTIMATED_DELIVERY_DATE)
    private Date estimatedDeliveryDate;

    @Column(name = FieldConstants.IS_ON_TREATMENT)
    private Boolean isOnTreatment;

    @Column(name = FieldConstants.DIAGNOSIS_TIME)
    private Date diagnosisTime;

    @Column(name = FieldConstants.DIAGNOSIS, columnDefinition = Constants.COLUMN_DEFINITION_VARCHAR)
    @Type(type = Constants.LIST_ARRAY)
    private List<String> diagnosis;

    @NotNull
    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;
}
