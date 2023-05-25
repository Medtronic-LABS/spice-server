package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResult;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;

/**
 * <p>
 * The LabTestDTO class is a data transfer object that contains information about a lab test, including
 * its name, country, status, order, results, and creator/updater information.
 * </p>
 */
@Data
public class LabTestDTO extends CommonRequestDTO {

    private String name;

    private Long countryId;

    private boolean isActive;

    private int displayOrder;

    private boolean isResultTemplate;

    @UpdateTimestamp
    private Date updatedAt;

    private Set<LabTestResult> labTestResults;

    private Long id;

    private Long tenantId;

    private Long createdBy = getUserValue();

    private Long updatedBy = getUserValue();

    @CreationTimestamp
    private Date createdAt;

    private boolean isDeleted;

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
