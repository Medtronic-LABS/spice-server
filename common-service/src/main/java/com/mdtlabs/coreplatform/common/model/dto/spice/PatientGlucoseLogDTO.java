package com.mdtlabs.coreplatform.common.model.dto.spice;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * The PatientGlucoseLogDTO class contains information about a patient's glucose log, including the
 * total number of logs, limit and skip values for pagination, a list of glucose log DTOs, the latest
 * glucose log DTO, and a list of glucose threshold maps.
 * </p>
 */
@Data
public class PatientGlucoseLogDTO {

    private long total;

    private int limit;

    private int skip;

    private List<GlucoseLogDTO> glucoseLogList;

    private GlucoseLogDTO latestGlucoseLog;

    private List<Map<String, Object>> glucoseThreshold;
}
