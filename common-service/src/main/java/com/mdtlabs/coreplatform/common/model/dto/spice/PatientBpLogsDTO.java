package com.mdtlabs.coreplatform.common.model.dto.spice;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

@/**
 * <p>
 * The PatientBpLogsDTO class represents a data transfer object containing information about a
 * patient's blood pressure logs.
 * </p>
 */
        Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientBpLogsDTO {

    private long total;

    private int limit;

    private int skip;

    private List<BpLogDTO> bpLogList;

    private BpLogDTO latestBpLog;

    private Map<String, Integer> bpThreshold;

}
