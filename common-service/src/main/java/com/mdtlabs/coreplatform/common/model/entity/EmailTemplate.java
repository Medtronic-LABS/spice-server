/*
 * Copyright (C) 2008 Ideas2IT Technologies Pvt Ltd. (http://www.ideas2it.com/).
 */
package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;

/**
 * <p>
 * EmailTemplate is a Java class representing an email template entity with various fields
 * and a one-to-many relationship with email template values.
 * </p>
 *
 * @author VigneshKumar created on Feb 19, 2019
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_EMAIL_TEMPLATE)
public class EmailTemplate implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = FieldConstants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = FieldConstants.TYPE)
    private String type;

    @Column(name = FieldConstants.VM_CONTENT)
    private String vmContent;

    @Column(name = FieldConstants.SUBJECT)
    private String subject;

    @Column(name = FieldConstants.BODY)
    private String body;

    @Column(name = FieldConstants.TITLE)
    private String title;

    @Column(name = FieldConstants.APP_URL)
    private String appUrl;

    @Column(name = FieldConstants.APP_TYPE)
    private String appType;

    @OneToMany
    @JoinColumn(name = FieldConstants.EMAIL_TEMPLATE_ID)
    private List<EmailTemplateValue> emailTemplateValues;
}
