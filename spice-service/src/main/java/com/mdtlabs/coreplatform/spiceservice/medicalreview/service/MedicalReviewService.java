package com.mdtlabs.coreplatform.spiceservice.medicalreview.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;

import java.util.Map;

/**
 * <p>
 * This is an interface to perform any actions in medical review related
 * entities.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 06, 2023
 */
public interface MedicalReviewService {

    /**
     * <p>
     * Create patient medical review data(Initial medical review and continuous
     * medical review).
     * </p>
     *
     * @param medicalReviewDto {@link MedicalReviewDTO} medical review DTO
     */
    void addMedicalReview(MedicalReviewDTO medicalReviewDto);

    /**
     * <p>
     * Gets medical review summary history.
     * </p>
     *
     * @param medicalReviewLis {@link RequestDTO} request DTO
     * @return MedicalReviewResponseDTO {@link MedicalReviewResponseDTO} medical review response DTO
     */
    MedicalReviewResponseDTO getMedicalReviewSummaryHistory(RequestDTO medicalReviewLis);

    /**
     * <p>
     * Gets the medical review history.
     * </p>
     *
     * @param medicalReviewLis {@link RequestDTO} requestDTO
     * @return MedicalReviewResponeDTO {@link MedicalReviewResponseDTO} medical review response DTO
     */
    MedicalReviewResponseDTO getMedicalReviewHistory(RequestDTO medicalReviewLis);

    /**
     * <p>
     * Gets prescription and lab test count.
     * </p>
     *
     * @param request {@link RequestDTO} request DTO
     * @return Map {@link Map<String, Integer>} count map
     */
    Map<String, Integer> getPrescriptionAndLabtestCount(RequestDTO request);

    /**
     * <p>
     * To remove medical review details based on tracker id.
     * </p>
     *
     * @param trackerId {@link long} trackerId
     */
    void removeMedicalReview(long trackerId);

}
