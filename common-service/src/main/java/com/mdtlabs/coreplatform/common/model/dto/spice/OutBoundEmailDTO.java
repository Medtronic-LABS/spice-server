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
 * This class is an Data transfer object for Out bound email Entity.
 * </p>
 *
 * @author Niraimathi S created on 17 Feb 2023
 */
@Data
public class OutBoundEmailDTO {

    private Long id;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    private Long tenantId;

    private Long formDataId;

    private boolean isProcessed = false;

    private int retryAttempts = 0;

    private String formName;

    private String to;

    private String type;

    private String subject;

    private String body;

    private String cc;

    private String bcc;

    /**
     * This method is used to get user value
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
