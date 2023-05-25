package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

/**
 * <p>
 * The NotificationDTO class is a Java class that contains fields and methods for creating and updating
 * notifications, including user information and timestamps.
 * </p>
 */
@Data
public class NotificationDTO {

    private Long id;

    private Long tenantId;

    private Long createdBy = getUserValue();

    private Long updatedBy = getUserValue();

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    private boolean isActive;

    private boolean isDeleted;

    private String toEmail;

    private String ccEmails;

    private String subject;

    private String body;

    private String status;

    /**
     * <p>
     * This method is used to get user value
     * </p>
     *
     * @return String - user value
     */
    @JsonIgnore
    public long getUserValue() {
        if (null != UserContextHolder.getUserDto()) {
            return UserContextHolder.getUserDto().getId();
        }
        return Constants.ZERO;
    }


}
