package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealthDetails;
import lombok.Data;

import java.util.List;


/**
 * <p>
 * This is a Java class representing a data transfer object for mental health information, with fields
 * for various scores and risk levels, as well as methods for getting user values and creating objects
 * with pre-set values.
 * </p>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MentalHealthDTO {

    private Long id;

    private Long createdBy = getUserValue();

    private Long tenantId;

    private String phq9RiskLevel;

    private Integer phq9Score;

    private String gad7RiskLevel;

    private Integer gad7Score;

    private String phq4RiskLevel;

    private Integer phq4Score;

    private Integer phq4FirstScore;

    private Integer phq4SecondScore;

    private Long patientTrackId;

    private List<MentalHealthDetails> phq9MentalHealth;

    private List<MentalHealthDetails> gad7MentalHealth;

    private List<MentalHealthDetails> phq4MentalHealth;

    private Long assessmentTenantId;

    /**
     * <p>
     * This constructor can be used to create a `MentalHealthDTO` object with pre-set values for
     * `phq4RiskLevel` and `phq4Score`.
     * </p>
     *
     * @param phq4RiskLevel phq4RiskLevel param is passed in MentalHealthDTO is given 
     * @param phq4Score phq4Score param is passed in MentalHealthDTO is given
     */
    public MentalHealthDTO(String phq4RiskLevel, Integer phq4Score) {
        this.phq4RiskLevel = phq4RiskLevel;
        this.phq4Score = phq4Score;
    }

    public MentalHealthDTO() {
    }

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
