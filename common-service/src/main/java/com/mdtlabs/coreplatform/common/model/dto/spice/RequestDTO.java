package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.model.entity.spice.PatientLabTest;
import lombok.Data;

import java.util.List;


/**
 * <p>
 * This is an DTO which contains fields related to common request.
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Data
public class RequestDTO extends CommonRequestDTO {

    private Long prescriptionId;

    private boolean isLatestRequired;

    private String searchTerm;

    private int limit;

    private int skip;

    private String sortField;

    private Long countryId;

    private Long operatingUnitId;

    private Long accountId;

    private Long siteId;

    private boolean isPaginated;

    private String type;

    private String category;

    private String searchId;

    private Boolean isSearchUserOrgPatient;

    private boolean isAssessmentDataRequired;

    private PatientFilterDTO patientFilter;

    private PatientSortDTO patientSort;

    private List<PatientLabTest> labTest;

    private boolean isDetailedSummaryRequired;

    private String discontinuedReason;

    private Boolean isActive;

    private boolean isMedicalReviewSummary;

    private Boolean isDeleted = false;

    private Long screeningId;

    private boolean isNutritionist;

    private boolean isNutritionHistoryRequired;

    private boolean isSortByAsc = false;

    private String name;

    private String reason;

    private String status;

    private String newPassword;

    private Long userId;

    private String email;

    private Long parentOrganizationId;

    private Long ignoreTenantId;

    private String locationId;
}
