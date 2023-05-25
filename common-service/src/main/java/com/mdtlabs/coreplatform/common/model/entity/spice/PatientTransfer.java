package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.model.enumeration.spice.PatientTransferStatus;

/**
 * <p>
 * PatientTransfer is a Java class representing a patient transfer entity with various attributes such as
 * transfer status, transfer reason, and transfer site.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_PATIENT_TRANSFER)
public class PatientTransfer extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @JoinColumn(name = FieldConstants.TRANSFER_TO)
    @ManyToOne(fetch = FetchType.LAZY)
    private User transferTo;

    @JoinColumn(name = FieldConstants.TRANSFER_BY)
    @ManyToOne(fetch = FetchType.LAZY)
    private User transferBy;

    @JoinColumn(name = FieldConstants.TRANSFER_SITE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Site transferSite;

    @JoinColumn(name = FieldConstants.OLD_SITE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Site oldSite;

    @Column(name = FieldConstants.TRANSFER_REASON)
    private String transferReason;

    @Column(name = FieldConstants.TRANSFER_REJECT_REASON)
    private String rejectReason;

    @Column(name = FieldConstants.OLD_PROGRAM_ID)
    private Long oldProgramId;

    @JoinColumn(name = FieldConstants.PATIENT_TRACK_ID)
    @ManyToOne(fetch = FetchType.LAZY)
    private PatientTracker patientTracker;

    @Enumerated(EnumType.STRING)
    @Column(name = FieldConstants.TRANSFER_STATUS)
    private PatientTransferStatus transferStatus;

    @Column(name = FieldConstants.IS_SHOW)
    private boolean isShow = true;
}
