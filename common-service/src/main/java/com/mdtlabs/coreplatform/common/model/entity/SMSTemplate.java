package com.mdtlabs.coreplatform.common.model.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;

/**
 * <p>
 * SMSTemplate is a Java class representing an SMS template with various fields and a one-to-many
 * relationship with SMS template values.
 * <p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Table(name = TableConstants.TABLE_SMS_TEMPLATE)
@Entity
public class SMSTemplate {

    @Id
    @Column(name = FieldConstants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = FieldConstants.CREATED_AT, columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = FieldConstants.UPDATED_AT, columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = FieldConstants.BODY)
    private String body;

    @Column(name = FieldConstants.TYPE)
    private String type;

    @OneToMany
    @JoinColumn(name = FieldConstants.SMS_TEMPLATE_ID)
    private List<SMSTemplateValues> smsTemplateValues;
}
