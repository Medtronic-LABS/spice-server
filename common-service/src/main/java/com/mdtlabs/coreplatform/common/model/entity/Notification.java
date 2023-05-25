package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;

/**
 * <p>
 * Notification is a Java class representing a notification entity with fields for
 * email addresses, subject, body, and status.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Entity
@Data
@Table(name = TableConstants.TABLE_NOTIFICATION)
public class Notification extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.TO_EMAIL)
    private String toEmail;

    @Column(name = FieldConstants.CC_EMAILS)
    private String ccEmails;

    @Column(name = FieldConstants.SUBJECT)
    private String subject;

    @Column(name = FieldConstants.BODY)
    private String body;

    @Column(name = FieldConstants.STATUS)
    private String status;

    public Notification() {

    }

    public Notification(String subject, String body, String toEmail) {
        this.subject = subject;
        this.body = body;
        this.toEmail = toEmail;
    }

}
