package com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.controller;

import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.NutritionLifestyleResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientNutritionLifestyleDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientNutritionLifestyleRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientNutritionLifestyle;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.service.PatientNutritionLifestyleService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.mockito.Mockito.when;

/**
 * <p>
 * PatientNutritionLifestyleControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in PatientNutritionLifestyleController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class PatientNutritionLifestyleControllerTest {

    @InjectMocks
    private PatientNutritionLifestyleController lifestyleController;

    @Mock
    private PatientNutritionLifestyleService lifestyleService;

    //given
    private CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO();

    private RequestDTO request = TestDataProvider.getRequestDto();

    private PatientNutritionLifestyleRequestDTO requestDTO = TestDataProvider.getNutritionLifestyleRequestDto();

    private PatientNutritionLifestyle patientNutritionLifestyle = TestDataProvider.getPatientNutritionLifeStyle();

    private List<PatientNutritionLifestyle> nutritionLifestyles = List.of(patientNutritionLifestyle);

    private List<NutritionLifestyleResponseDTO> responseDTOList = List.of(TestDataProvider
            .getNutritionLifestyleResponseDTO());

    private SuccessResponse<PatientNutritionLifestyle> successResponse;

    private SuccessResponse<List<PatientNutritionLifestyle>> listSuccessResponse;

    @Test
    @DisplayName("AddPatientNutritionLifestyle Test")
    void addPatientNutritionLifestyle() {
        //when
        when(lifestyleService.addPatientNutritionLifestyle(requestDTO)).thenReturn(patientNutritionLifestyle);
        //then
        successResponse = lifestyleController.addPatientNutritionLifestyle(requestDTO);
        Assertions.assertEquals(HttpStatus.CREATED, successResponse.getStatusCode());
        Assertions.assertNotNull(patientNutritionLifestyle);
        Assertions.assertEquals(requestDTO.getPatientTrackId(),
                lifestyleService.addPatientNutritionLifestyle(requestDTO).getPatientTrackId());
        Assertions.assertEquals(requestDTO.getClinicianNote(),
                lifestyleService.addPatientNutritionLifestyle(requestDTO).getClinicianNote());

    }

    @Test
    @DisplayName("GetPatientNutritionLifestyle Test")
    void getPatientNutritionLifestyle() {
        //when
        when(lifestyleService.getPatientNutritionLifeStyleList(request)).thenReturn(responseDTOList);
        //then
        listSuccessResponse = lifestyleController.getPatientNutritionLifeStyleList(request);
        Assertions.assertEquals(HttpStatus.OK, listSuccessResponse.getStatusCode());
        Assertions.assertEquals(request.getId(),
                lifestyleService.getPatientNutritionLifeStyleList(request).get(0).getId());
        Assertions.assertEquals(responseDTOList.get(0).getLifestyleAssessment(),
                lifestyleService.getPatientNutritionLifeStyleList(request).get(0).getLifestyleAssessment());
    }

    @Test
    @DisplayName("UpdatePatientNutritionLifestyle Test")
    void updatePatientNutritionLifestyle() {
        //when
        when(lifestyleService.updatePatientNutritionLifestyle(requestDTO)).thenReturn(nutritionLifestyles);
        //then
        listSuccessResponse = lifestyleController.updatePatientNutritionLifestyle(requestDTO);
        Assertions.assertEquals(HttpStatus.OK, listSuccessResponse.getStatusCode());
        Assertions.assertEquals(nutritionLifestyles.get(0).getLifestyleAssessment(),
                lifestyleService.updatePatientNutritionLifestyle(requestDTO).get(0).getLifestyleAssessment());
        Assertions.assertNotEquals(nutritionLifestyles,
                lifestyleService.updatePatientNutritionLifestyle(new PatientNutritionLifestyleRequestDTO()));
    }

    @Test
    @DisplayName("UpdatePatientNutritionLifestyleView Test")
    void updatePatientNutritionLifestyleView() {
        //when
        when(lifestyleService.updatePatientNutritionLifestyleView(commonRequestDTO)).thenReturn(true);
        when(lifestyleService.updatePatientNutritionLifestyleView(new CommonRequestDTO())).thenReturn(false);
        //then
        successResponse = lifestyleController.updatePatientNutritionLifestyleView(commonRequestDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        Assertions.assertTrue(lifestyleService.updatePatientNutritionLifestyleView(commonRequestDTO));
        Assertions.assertFalse(lifestyleService.updatePatientNutritionLifestyleView(new CommonRequestDTO()));
    }

    @Test
    @DisplayName("DeletePatientNutritionLifestyle Test")
    void deletePatientNutritionLifestyle() {
        //given
        PatientNutritionLifestyleDTO patientNutritionLifestyleDTO = TestDataProvider.getPatientNutritionLifestyleDTO();
        //when
        when(lifestyleService.removePatientNutritionLifestyle(patientNutritionLifestyle)).thenReturn(true);
        //then
        successResponse = lifestyleController.deletePatientNutritionLifestyle(patientNutritionLifestyleDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
        Assertions.assertTrue(lifestyleService.removePatientNutritionLifestyle(patientNutritionLifestyle));
        Assertions.assertFalse(lifestyleService.removePatientNutritionLifestyle(new PatientNutritionLifestyle()));
    }
}

