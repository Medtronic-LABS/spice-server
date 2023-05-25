package com.mdtlabs.coreplatform.spiceservice.medicalreview.controller;

import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.spiceservice.medicalreview.service.MedicalReviewService;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * <p>
 * MedicalReviewControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in MedicalReviewController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class MedicalReviewControllerTest {

    @InjectMocks
    private MedicalReviewController medicalReviewController;

    @Mock
    private MedicalReviewService medicalReviewService;

    private SuccessResponse<MedicalReviewDTO> reviewDTOSuccessResponse;

    private SuccessResponse<MedicalReviewResponseDTO> responseDTOSuccessResponse;

    private SuccessResponse<Map<String, Integer>> response;

    private MedicalReviewDTO medicalReviewDTO = TestDataProvider.getMedicalReview();

    private MedicalReviewResponseDTO medicalReviewResponseDTO = TestDataProvider.getMedicalResponseDto();

    private RequestDTO request = TestDataProvider.getRequestDto();

    @Test
    @DisplayName("AddMedicalReview Test")
    void addMedicalReviewTest() {
        //when
        doNothing().when(medicalReviewService).addMedicalReview(medicalReviewDTO);
        //then
        Assertions.assertNotNull(medicalReviewDTO);
        reviewDTOSuccessResponse = medicalReviewController.addMedicalReview(medicalReviewDTO);
        Assertions.assertEquals(HttpStatus.CREATED, reviewDTOSuccessResponse.getStatusCode(),
                "SuccessResponse Code Should be same");

    }

    @Test
    @DisplayName("GetMedicalReviewSummary Test")
    void getMedicalReviewSummaryTest() {
        //when
        when(medicalReviewService.getMedicalReviewSummaryHistory(request))
                .thenReturn(medicalReviewResponseDTO);
        //then
        responseDTOSuccessResponse = medicalReviewController.getMedicalReviewSummary(request);
        Assertions.assertEquals(HttpStatus.OK, responseDTOSuccessResponse.getStatusCode(),
                "SuccessResponse Code Should be Same");
        Assertions.assertEquals(medicalReviewService
                        .getMedicalReviewSummaryHistory(request).getReviewerDetails(),
                medicalReviewResponseDTO.getReviewerDetails(),
                "ReviewerDetails Should be Same");
        Assertions.assertEquals(medicalReviewService.getMedicalReviewSummaryHistory(request).getIsSigned(),
                medicalReviewResponseDTO.getIsSigned(), "Reviewer Sign Should Required");

    }

    @Test
    @DisplayName("GetMedicalReviewList Test")
    void getMedicalReviewListTest() {
        //when
        when(medicalReviewService.getMedicalReviewHistory(request)).thenReturn(medicalReviewResponseDTO);
        //then
        responseDTOSuccessResponse = medicalReviewController.getMedicalReviewList(request);
        Assertions.assertEquals(HttpStatus.OK, responseDTOSuccessResponse.getStatusCode(),
                "SuccessCode Should be Same");
        Assertions.assertEquals(medicalReviewService.getMedicalReviewHistory(request).getIsSigned(),
                medicalReviewResponseDTO.getIsSigned(), "MedicalReviewer sign is required");
        Assertions.assertEquals(medicalReviewService.
                        getMedicalReviewHistory(request).getReviewerDetails().getUserName()
                , medicalReviewResponseDTO.getReviewerDetails().getUserName(),
                "MedicalReviewer userName Should be Same");

    }

    @Test
    @DisplayName("GetMedicalReviewCount Test")
    void getMedicalReviewCountTest() {
        //given
        Map<String, Integer> count = new HashMap<>();
        count.put("unreviewed", 5);
        //when
        when(medicalReviewService.getPrescriptionAndLabtestCount(request)).thenReturn(count);
        //then
        response = medicalReviewController.getMedicalReviewCount(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
                "SuccessCode Should be Same");
        Assertions.assertEquals(medicalReviewService.getPrescriptionAndLabtestCount(request).size(), count.size());

    }
}
