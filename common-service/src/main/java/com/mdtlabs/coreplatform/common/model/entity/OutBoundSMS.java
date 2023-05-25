package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * OutBoundSMS is a Java class representing an outbound SMS message with various fields such as ID, creation and update
 * timestamps, notification ID, tenant ID, form data ID, processing status, retry attempts, username, phone number, and
 * message body.
 * <p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_OUTBOUND_SMS)
public class OutBoundSMS implements Serializable {

    @Serial
    private static final long serialVersionUID = 4174505913611242103L;

    @Id
    @Column(name = FieldConstants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = FieldConstants.CREATED_AT, columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = FieldConstants.UPDATED_AT, columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = FieldConstants.NOTIFICATION_ID)
    private Long notificationId;

    @Column(name = FieldConstants.TENANT_ID)
    private Long tenantId;

    @Column(name = FieldConstants.FORM_DATA_ID)
    private Long formDataId;

    @Column(name = FieldConstants.IS_PROCESSED)
    private boolean isProcessed = false;

    @Column(name = FieldConstants.RETRY_ATTEMPTS)
    private int retryAttempts = 0;

    @Column(name = FieldConstants.USERNAME)
    private String userName;

    @Column(name = FieldConstants.PHONE_NUMBER)
    private String phoneNumber;

    @Column(name = FieldConstants.BODY)
    private String body;

}
