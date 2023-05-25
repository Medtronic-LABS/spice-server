package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.model.entity.spice.NutritionLifestyle;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;

/**
 * <p>
 * This class is an Request DTO class for device details.
 * </p>
 *
 * @author Prabu created on 16 Feb 2023
 */
@Data
public class PatientNutritionLifestyleDTO {

    private Long id;

    private Long createdBy = getUserValue();

    private Long updatedBy = getUserValue();

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    private boolean isActive;

    private boolean isDeleted;

    private Long tenantId;

    private Set<NutritionLifestyle> lifestyles;

    private String lifestyleAssessment;

    private Long patientTrackId;

    private Long patientVisitId;

    private Long referredBy;

    private Date referredDate;

    private Long assessedBy;

    private Date assessedDate;

    private String clinicianNote;

    private String otherNote;

    private boolean isViewed;

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
