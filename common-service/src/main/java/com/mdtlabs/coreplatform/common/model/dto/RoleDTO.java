package com.mdtlabs.coreplatform.common.model.dto;

import lombok.Data;

import javax.persistence.Transient;
import java.util.Date;

/**
 * <p>
 * This class is an entity class for role table.
 * </p>
 *
 * @author Prabu created on Dec 06 2022
 */
@Data
public class RoleDTO {

    private long id;

    private String name;

    private String level;
    @Transient
    private String authority;

    private Long createdBy;

    private Long updatedBy;

    private Date createdAt;

    private Date updatedAt;

    private boolean isActive;

    private boolean isDeleted;

}
