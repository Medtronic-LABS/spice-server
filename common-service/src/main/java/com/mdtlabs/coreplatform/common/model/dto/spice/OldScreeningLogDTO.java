package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.util.spice.ScreeningInfo;
import lombok.Data;
import org.json.simple.JSONObject;

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
public class OldScreeningLogDTO {

    private JSONObject bio_data;

    private JSONObject bio_metrics;

    private JSONObject bp_log;

    private JSONObject glucose_log;

    @NotEmpty(message = ErrorConstants.CATEGORY_NOT_NULL, groups = {
            ScreeningInfo.class})
    private String category;

    @NotEmpty(message = ErrorConstants.LONGITUDE_NOT_NULL, groups = {
            ScreeningInfo.class})
    private String longitude;

    @NotEmpty(message = ErrorConstants.LATITUDE_NOT_NULL, groups = {
            ScreeningInfo.class})
    private String latitude;

    private String unit_measurement;

    private Date date_of_birth;
    @NotNull(message = ErrorConstants.IS_REFER_ASSESSMENT_NOT_NULL, groups = {
            ScreeningInfo.class})
    private Boolean refer_assessment;

    private String cvd_risk_level;

    private Integer cvd_risk_score;

    private String device_info_id;

    private OldPhq4DTO phq4;

    private String cvd_risk_score_display;

    @NotNull(message = ErrorConstants.SITE_ID_NOT_NULL, groups = {
            ScreeningInfo.class})
    private String site;

    private String type;

    private Date screening_date_time;

    private List<Map<String, Object>> customized_workflows;

}
