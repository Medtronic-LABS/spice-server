package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import com.mdtlabs.coreplatform.common.util.spice.ScreeningInfo;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * This class is a Request DTO class for screeninglog.
 * </p>
 *
 * @author Karthick M
 */
@Data
public class ScreeningLogDTO {

    private BioDataDTO bioData;

    private BioMetricsDTO bioMetrics;
    @Valid
    private BpLog bpLog;

    private GlucoseLog glucoseLog;

    @NotEmpty(message = ErrorConstants.CATEGORY_NOT_NULL, groups = {
            ScreeningInfo.class})
    private String category;

    @NotEmpty(message = ErrorConstants.LONGITUDE_NOT_NULL, groups = {
            ScreeningInfo.class})
    private String longitude;

    @NotEmpty(message = ErrorConstants.LATITUDE_NOT_NULL, groups = {
            ScreeningInfo.class})
    private String latitude;

    private String unitMeasurement;

    private Date dateOfBirth;
    @NotNull(message = ErrorConstants.IS_REFER_ASSESSMENT_NOT_NULL, groups = {
            ScreeningInfo.class})
    private Boolean isReferAssessment;

    private String cvdRiskLevel;

    private Integer cvdRiskScore;

    private Long deviceInfoId;

    private MentalHealth phq4;

    private String cvdRiskScoreDisplay;

    @NotNull(message = ErrorConstants.SITE_ID_NOT_NULL, groups = {
            ScreeningInfo.class})
    private Long siteId;

    private List<Map<String, Object>> customizedWorkflows;

    private String type;

    private boolean isUpdatedFromEnrollment;

    private Date screeningDateTime;

    private Long countryId;

}
