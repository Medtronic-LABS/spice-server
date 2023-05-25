package com.mdtlabs.coreplatform.spiceservice.medicalreview.controller;

import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.spiceservice.medicalreview.service.MedicalReviewService;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * This class is a controller class to perform operation on MedicalReview
 * entity.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 06, 2023
 */
@RestController
@RequestMapping(value = "/medical-review")
public class MedicalReviewController {

    @Autowired
    private MedicalReviewService medicalReviewService;

    /**
     * <p>
     * This method is used to add a medical review.
     * </p>
     *
     * @param medicalReviewDto {@link MedicalReviewDTO} entity
     * @return medicalReviewDTO {@link SuccessResponse<MedicalReviewDTO>}Entity.
     */
    @PostMapping("/create")
    @TenantValidation
    public SuccessResponse<MedicalReviewDTO> addMedicalReview(@RequestBody MedicalReviewDTO medicalReviewDto) {
        medicalReviewService.addMedicalReview(medicalReviewDto);
        return new SuccessResponse<>(SuccessCode.MEDICAL_REVIEW_SAVE, HttpStatus.CREATED);
    }

    /**
     * This method is used to get medical review summary.
     *
     * @param request {@link RequestDTO} request dto
     * @return MedicalReviewResponseDto {@link SuccessResponse<MedicalReviewResponseDTO>} success response
     */
    @PostMapping("/summary")
    @TenantValidation
    public SuccessResponse<MedicalReviewResponseDTO> getMedicalReviewSummary(@RequestBody RequestDTO request) {
        return new SuccessResponse<>(SuccessCode.GET_MEDICAL_REVIEW_SUMMARY,
                medicalReviewService.getMedicalReviewSummaryHistory(request), HttpStatus.OK);
    }

    /**
     * Gets medical review list.
     *
     * @param medicalReviewList {2link RequestDTO} request dto
     * @return MedicalReviewResponseDTO {@link SuccessResponse<MedicalReviewResponseDTO>} entity
     */
    @PostMapping("/list")
    @TenantValidation
    public SuccessResponse<MedicalReviewResponseDTO> getMedicalReviewList(@RequestBody RequestDTO medicalReviewList) {
        return new SuccessResponse<>(SuccessCode.GET_MEDICAL_REVIEW_LIST,
                medicalReviewService.getMedicalReviewHistory(medicalReviewList), HttpStatus.OK);
    }

    /**
     * Gets medical review count.
     *
     * @param request {@link RequestDTO} request dto
     * @return SuccessResponse {@link SuccessResponse<Map<String, Integer>>} response
     */
    @PostMapping("/count")
    @TenantValidation
    public SuccessResponse<Map<String, Integer>> getMedicalReviewCount(@RequestBody RequestDTO request) {
        return new SuccessResponse<>(SuccessCode.GET_MEDICAL_REVIEW_COUNT,
                medicalReviewService.getPrescriptionAndLabtestCount(request), HttpStatus.OK);
    }
}
