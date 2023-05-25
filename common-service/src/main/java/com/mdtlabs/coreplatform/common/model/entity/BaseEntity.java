package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.CustomDateSerializer;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;

/**
 * <p>
 * BaseEntity is a Java class that serves as a base entity for other classes, containing fields for ID,
 * creation and update timestamps, and user information.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Data
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 4174505913611242103L;

    @Column(name = FieldConstants.CREATED_BY, updatable = false, nullable = false)
    private Long createdBy = getUserValue();

    @Id
    @Column(name = FieldConstants.ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = FieldConstants.UPDATED_BY, nullable = true)
    private Long updatedBy = getUserValue();

    @Column(name = FieldConstants.CREATED_AT, columnDefinition = Constants.TIMESTAMP, nullable = false, updatable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createdAt;

    @Column(name = FieldConstants.UPDATED_AT, columnDefinition = Constants.TIMESTAMP)
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date updatedAt;

    @Column(name = FieldConstants.IS_ACTIVE)
    private boolean isActive = true;

    @Column(name = FieldConstants.IS_DELETED)
    private boolean isDeleted = false;

    public BaseEntity() {

    }

    public BaseEntity(Long id) {
        this.id = id;
    }

    /**
     * <p>
     * This function returns the user ID if it exists in the UserContextHolder, otherwise it returns zero.
     * </p>
     *
     * @return The ID of the user stored in the`UserContextHolder` is returned
     */
    @JsonIgnore
    public long getUserValue() {
        if (null != UserContextHolder.getUserDto()) {
            return UserContextHolder.getUserDto().getId();
        }
        return Constants.ZERO;
    }

    /**
     * <p>
     * This method is used to set the "updatedBy" field to the ID
     * of the current user before persisting or updating an entity.
     * </p>
     */
    @PrePersist
    @PreUpdate
    public void prePersistAndUpdate() {
        if (!Objects.isNull(UserContextHolder.getUserDto())) {
            this.updatedBy = UserContextHolder.getUserDto().getId();
        }
    }
}
