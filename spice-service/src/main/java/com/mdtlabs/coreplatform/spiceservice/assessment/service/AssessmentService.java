package com.mdtlabs.coreplatform.spiceservice.assessment.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.AssessmentDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AssessmentResponseDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;

/**
 * <p>
 * This is an interface to perform any actions in assessment related entities.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
public interface AssessmentService {

    /**
     * <p>
     * Creates a new assessment based on AssessmentDTO.
     * </p>
     *
     * @param assessmentDto {@link AssessmentDTO} Object with patient assessment data is given
     * @return {@link AssessmentResponseDTO} entity is returned
     */
    AssessmentResponseDTO createAssessment(AssessmentDTO assessmentDto);

    /**
     * <p>
     * Creates a new assessment BP Log.
     * </p>
     *
     * @param bpLog {@link BpLog} entity is given
     * @return {@link BpLog} bplog is being returned
     */
    BpLog createAssessmentBpLog(BpLog bpLog);

    /**
     * <p>
     * Create a new assessment Glucose Log.
     * </p>
     *
     * @param glucoseLog {@link GlucoseLog} entity is given
     * @return glucoseLog {@link GlucoseLog} entity is returned
     */
    GlucoseLog createAssessmentGlucoseLog(GlucoseLog glucoseLog);

    /**
     * <p>
     * This method is used to clear the api role permission map.
     * </p>
     */
    void clearApiPermissions();

}
