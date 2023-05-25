package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * This is a Java class representing a data transfer object for confirming a diagnosis, including a
 * boolean flag, a list of diagnosis codes, and comments.
 * </p>
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmDiagnosisDTO extends CommonRequestDTO {

    private Boolean isConfirmDiagnosis;

    private List<String> confirmDiagnosis;

    private String diagnosisComments;

}
