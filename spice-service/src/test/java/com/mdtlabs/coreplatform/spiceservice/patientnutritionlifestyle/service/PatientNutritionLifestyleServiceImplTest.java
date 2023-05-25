package com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.exception.SpiceValidation;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.NutritionLifestyleResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientNutritionLifestyleRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ReviewerDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.NutritionLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientNutritionLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.spiceservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CultureService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.NutritionLifestyleService;
import com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.repository.PatientNutritionLifestyleRepository;
import com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.service.impl.PatientNutritionLifestyleServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.patienttreatmentplan.service.PatientTreatmentPlanService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PatientNutritionLifestyleServiceImplTest {

    @InjectMocks
    private PatientNutritionLifestyleServiceImpl patientNutritionLifestyleService;

    @Mock
    private PatientNutritionLifestyleRepository lifestyleRepository;

    @Mock
    private NutritionLifestyleService nutritionLifestyleService;

    @Mock
    private UserApiInterface userApiInterface;

    @Mock
    private CultureService cultureService;

    @Mock
    private PatientTreatmentPlanService treatmentPlanService;

    @Mock
    private PatientTrackerService patientTrackerService;

    private PatientNutritionLifestyle patientNutritionLifestyle;

    @Mock
    private PatientNutritionLifestyleRepository patientNutritionLifestyleRepository;

    @Test
    @DisplayName("AddPatientNutritionLifestyleWithInvalidId Test")
    void addPatientNutritionLifestyleWithInvalidId() {
        //given
        PatientNutritionLifestyleRequestDTO requestDTO = new PatientNutritionLifestyleRequestDTO();
        //when
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientNutritionLifestyleService.addPatientNutritionLifestyle(requestDTO));
    }

    @Test
    @DisplayName("AddPatientNutritionLifestyleWithValidData Test")
    void addPatientNutritionLifestyleWithValidData() {
        //given
        PatientNutritionLifestyleRequestDTO requestDTO = TestDataProvider.getNutritionLifestyleRequestDto();
        PatientNutritionLifestyle nutritionLifestyle = TestDataProvider.getPatientNutritionLifeStyle();
        nutritionLifestyle.setReferredBy(1L);
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        nutritionLifestyle.setLifestyles(null);
        requestDTO.setNutritionist(Constants.BOOLEAN_FALSE);
        TestDataProvider.init();
        //when
        TestDataProvider.getStaticMock();
        when(lifestyleRepository.save(nutritionLifestyle)).thenReturn(nutritionLifestyle);
        when(treatmentPlanService.getNextFollowUpDate(1L,
                Constants.MEDICAL_REVIEW_FREQUENCY)).thenReturn(new Date());
        when(patientTrackerService.getPatientTrackerById(1L)).thenReturn(patientTracker);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        when(patientNutritionLifestyleRepository.save(any())).thenReturn(nutritionLifestyle);
        //then
        PatientNutritionLifestyle actualResult = patientNutritionLifestyleService
                .addPatientNutritionLifestyle(requestDTO);
        TestDataProvider.cleanUp();
        Assertions.assertEquals(nutritionLifestyle, actualResult);
        Assertions.assertEquals(nutritionLifestyle.getPatientTrackId(), actualResult.getPatientTrackId());

    }

    @Test
    @DisplayName("AddPatientNutritionLifestyle Test")
    void addPatientNutritionLifestyle() {
        //given
        PatientNutritionLifestyleRequestDTO requestDTO = TestDataProvider.getNutritionLifestyleRequestDto();
        PatientNutritionLifestyle nutritionLifestyle = TestDataProvider.getPatientNutritionLifeStyle();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        nutritionLifestyle.setReferredBy(1L);
        nutritionLifestyle.setAssessedBy(1L);
        nutritionLifestyle.setAssessedDate(new Date());
        requestDTO.setNutritionist(Constants.BOOLEAN_TRUE);
        nutritionLifestyle.setLifestyleAssessment(Constants.ASSESSMENT);
        NutritionLifestyle lifestyle = TestDataProvider.getNutritionLifestyle();
        requestDTO.setLifestyle(Set.of(1L));
        TestDataProvider.init();
        nutritionLifestyle.setLifestyles(Set.of(lifestyle));
        //when
        TestDataProvider.getStaticMock();
        when(nutritionLifestyleService.getNutritionLifestyleByIds(requestDTO.getLifestyle()))
                .thenReturn(Set.of(lifestyle));
        when(lifestyleRepository.save(any())).thenReturn(nutritionLifestyle);

        when(patientTrackerService.getPatientTrackerById(anyLong())).thenReturn(patientTracker);
        when(patientNutritionLifestyleRepository.save(any())).thenReturn(nutritionLifestyle);
        //then
        PatientNutritionLifestyle actualResult = patientNutritionLifestyleService
                .addPatientNutritionLifestyle(requestDTO);
        TestDataProvider.cleanUp();
        Assertions.assertNotNull(requestDTO.getLifestyle());
        Assertions.assertTrue(requestDTO.isNutritionist());
        Assertions.assertEquals(nutritionLifestyle, actualResult);
        Assertions.assertEquals(nutritionLifestyle.getPatientTrackId(), actualResult.getPatientTrackId());

    }

    @Test
    @DisplayName("GetNutritionLifestyleListWithInvalidId Test")
    void getNutritionLifestyleListWithInvalidId() {
        //given
        RequestDTO requestDTO = new RequestDTO();
        //when
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientNutritionLifestyleService.getPatientNutritionLifeStyleList(requestDTO));
    }

    @Test
    @DisplayName("GetPatientNutritionWithTrackId Test")
    void getPatientNutritionWithTrackId() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        requestDTO.setNutritionist(Constants.BOOLEAN_TRUE);
        requestDTO.setNutritionHistoryRequired(Constants.BOOLEAN_TRUE);
        requestDTO.setPatientTrackId(1L);
        patientNutritionLifestyle = TestDataProvider.getPatientNutritionLifeStyle();
        patientNutritionLifestyle.setReferredBy(1L);
        List<PatientNutritionLifestyle> nutritionLifestyles = List.of(patientNutritionLifestyle);
        NutritionLifestyleResponseDTO lifestyleResponseDTO = TestDataProvider.getNutritionLifestyleResponseDTO();
        lifestyleResponseDTO.setLifestyle(List.of(Map.of(FieldConstants.NAME, "Stress Management", Constants.CULTURE_VALUE, "NutritionLifestyle")));
        lifestyleResponseDTO.setId(1L);
        lifestyleResponseDTO.setClinicianNote("nothing");
        lifestyleResponseDTO.setLifestyleAssessment("LifestyleAssessment Test");
        ReviewerDetailsDTO referredByDetails = TestDataProvider.getReviewerDetailsDto();
        referredByDetails.setFirstName("first_name");
        referredByDetails.setUserName(null);
        lifestyleResponseDTO.setReferredBy(referredByDetails);
        lifestyleResponseDTO.setLifestyleAssessment(null);

        List<NutritionLifestyleResponseDTO> responseDTOS = List.of(lifestyleResponseDTO);
        TestDataProvider.init();
        Map<Long, String> cultureMap = Map.of(1L, Constants.NUTRITION_LIFESTYLE);
        Constants.CULTURE_VALUES_MAP = Map.of(1L, Map.of(Constants.NUTRITION_LIFESTYLE, cultureMap));

        patientNutritionLifestyle = TestDataProvider.getPatientNutritionLifeStyle();
        patientNutritionLifestyle.setReferredBy(1L);
        List<PatientNutritionLifestyle> patientNutritionLifestyles = List.of(patientNutritionLifestyle);

        Map<Long, UserDTO> userMap = new HashMap<>();
        userMap.put(1L, TestDataProvider.getUserDTO());
        ResponseEntity<Map<Long, UserDTO>> mapResponse = new ResponseEntity<>(userMap, HttpStatus.OK);
        //when
        when(lifestyleRepository.getPatientNutritionLifestyles(requestDTO.getPatientTrackId(),
                requestDTO.getTenantId(), Constants.BOOLEAN_TRUE, null,
                null)).thenReturn(nutritionLifestyles);
        TestDataProvider.getStaticMock();
        when(userApiInterface
                .getUsers(any(), any(), any())).thenReturn(mapResponse);
        doNothing().when(cultureService).getCultureValues();

        Constants.CULTURE_VALUES_MAP = Map.of(0L, Map.of(Constants.NUTRITION_LIFESTYLE, cultureMap));

        when(patientNutritionLifestyleRepository.getPatientNutritionLifestyles(anyLong(), anyLong(), any(), any(), any())).thenReturn(patientNutritionLifestyles);

        //then
        List<NutritionLifestyleResponseDTO> actualResponse = patientNutritionLifestyleService
                .getPatientNutritionLifeStyleList(requestDTO);
        TestDataProvider.cleanUp();
        Assertions.assertEquals(responseDTOS, actualResponse);
    }

    @Test
    @DisplayName("UpdatePatientNutritionLifeStyleWithNull Test")
    void updatePatientNutritionLifeStyleWithNull() {
        //
        //when
        Assertions.assertThrows(SpiceValidation.class,
                () -> patientNutritionLifestyleService.updatePatientNutritionLifestyle(null));
    }

    @ParameterizedTest
    @DisplayName("UpdateLifeStyleWithInvalidPatientId Test")
    @CsvSource({", ", "1,", ",1",})
    void updateLifeStyleWithInvalidPatientId(Long patientTrackId, Long patientVisitId) {
        //given
        PatientNutritionLifestyleRequestDTO requestDTO = new PatientNutritionLifestyleRequestDTO();
        requestDTO.setPatientTrackId(patientTrackId);
        requestDTO.setPatientVisitId(patientVisitId);

        //when
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientNutritionLifestyleService.updatePatientNutritionLifestyle(requestDTO));
    }

    @Test
    @DisplayName("UpdatePatientNutritionLifestyleWithNull Test")
    void updatePatientNutritionLifestyleWithNull() {
        //given
        PatientNutritionLifestyleRequestDTO requestDTO = new PatientNutritionLifestyleRequestDTO();
        requestDTO.setPatientVisitId(1L);
        requestDTO.setPatientTrackId(1L);
        requestDTO.setLifestyles(List.of(new PatientNutritionLifestyle()));
        //when
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientNutritionLifestyleService.updatePatientNutritionLifestyle(requestDTO));
    }

    @Test
    @DisplayName("UpdatePatientNutritionLifestyleWithLifestyleAssessmentNull Test")
    void updatePatientNutritionLifestyleWithAssessmentNull() {
        //given
        PatientNutritionLifestyleRequestDTO requestDTO = new PatientNutritionLifestyleRequestDTO();
        requestDTO.setPatientVisitId(1L);
        requestDTO.setPatientTrackId(1L);
        PatientNutritionLifestyle lifestyle = new PatientNutritionLifestyle();
        lifestyle.setId(1L);
        requestDTO.setLifestyles(List.of(lifestyle));
        //when
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientNutritionLifestyleService.updatePatientNutritionLifestyle(requestDTO));
    }

    @Test
    @DisplayName("UpdatePatientLifestyle Test")
    void updatePatientLifestyle() {
        //given
        patientNutritionLifestyle = TestDataProvider.getPatientNutritionLifeStyle();
        PatientNutritionLifestyleRequestDTO requestDTO = TestDataProvider.getNutritionLifestyleRequestDto();
        requestDTO.setLifestyles(List.of(patientNutritionLifestyle));
        requestDTO.setLifestyleAssessment(Constants.ASSESSMENT);
        patientNutritionLifestyle.setLifestyleAssessment(Constants.ASSESSMENT);
        List<PatientNutritionLifestyle> nutritionLifestyles = List.of(patientNutritionLifestyle);
        TestDataProvider.init();
        //when
        TestDataProvider.getStaticMock();
        when(patientNutritionLifestyleRepository.getPatientNutritionLifestyleByIds(anyList(), anyLong())).thenReturn(nutritionLifestyles);
        when(patientNutritionLifestyleRepository.saveAll(any())).thenReturn(nutritionLifestyles);
        //then
        List<PatientNutritionLifestyle> actualList = patientNutritionLifestyleService
                .updatePatientNutritionLifestyle(requestDTO);
        nutritionLifestyles.get(0).setAssessedDate(null);
        actualList.get(0).setAssessedDate(null);
        Assertions.assertEquals(nutritionLifestyles, actualList);
        TestDataProvider.cleanUp();
    }

    @Test
    @DisplayName("UpdatePatientLifestyleViewWithNull Test")
    void updatePatientLifestyleViewWithNull() {
        //given
        CommonRequestDTO requestDTO = new CommonRequestDTO();
        //when
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientNutritionLifestyleService.updatePatientNutritionLifestyleView(requestDTO));
    }

    @Test
    @DisplayName("UpdatePatientLifestyleView Test")
    void updatePatientLifestyleView() {
        //given
        CommonRequestDTO requestDTO = TestDataProvider.getCommonRequestDTO();
        patientNutritionLifestyle = TestDataProvider.getPatientNutritionLifeStyle();
        List<PatientNutritionLifestyle> patientNutritionLifestyles = List.of(patientNutritionLifestyle);
        List<PatientNutritionLifestyle> lifestyles = new ArrayList<>();
        //when
        when(lifestyleRepository.getPatientNutritionLifestyles(requestDTO.getPatientTrackId(),
                requestDTO.getTenantId(), Constants.BOOLEAN_TRUE, null,
                Constants.BOOLEAN_FALSE)).thenReturn(patientNutritionLifestyles);
        when(lifestyleRepository.saveAll(lifestyles)).thenReturn(lifestyles);
        //then
        boolean actualResult = patientNutritionLifestyleService.updatePatientNutritionLifestyleView(requestDTO);
        Assertions.assertTrue(actualResult);
    }

    @Test
    @DisplayName("UpdatePatientLifestyleViewWithAssessmentDate Test")
    void updatePatientLifestyleViewWithDate() {
        //given
        CommonRequestDTO requestDTO = TestDataProvider.getCommonRequestDTO();
        patientNutritionLifestyle = TestDataProvider.getPatientNutritionLifeStyle();
        patientNutritionLifestyle.setAssessedDate(new Date());
        patientNutritionLifestyle.setViewed(Constants.BOOLEAN_TRUE);
        List<PatientNutritionLifestyle> patientNutritionLifestyles = List.of(patientNutritionLifestyle);
        List<PatientNutritionLifestyle> lifestyles = new ArrayList<>();
        lifestyles.add(patientNutritionLifestyle);
        TestDataProvider.init();
        //when
        when(lifestyleRepository.getPatientNutritionLifestyles(requestDTO.getPatientTrackId(),
                requestDTO.getTenantId(), Constants.BOOLEAN_TRUE, null,
                Constants.BOOLEAN_FALSE)).thenReturn(patientNutritionLifestyles);
        TestDataProvider.getStaticMock();
        when(lifestyleRepository.saveAll(lifestyles)).thenReturn(lifestyles);
        //then
        boolean actualResult = patientNutritionLifestyleService.updatePatientNutritionLifestyleView(requestDTO);
        TestDataProvider.cleanUp();
        Assertions.assertTrue(actualResult);
    }

    @Test
    @DisplayName("RemovePatientNutritionLifestyleWithInvalidId Test")
    void removePatientNutritionLifestyleWithInvalidId() {
        //given
        patientNutritionLifestyle = new PatientNutritionLifestyle();
        //when
        Assertions.assertThrows(DataNotFoundException.class,
                () -> patientNutritionLifestyleService.removePatientNutritionLifestyle(patientNutritionLifestyle));
    }

    @ParameterizedTest
    @DisplayName("RemovePatientWithInvalidPatientId Test")
    @CsvSource({", ", "1, ", ", 1"})
    void removePatientWithInvalidPatientId(Long patientVisitId, Long patientTrackId) {
        //given
        patientNutritionLifestyle = new PatientNutritionLifestyle();
        patientNutritionLifestyle.setId(1L);
        patientNutritionLifestyle.setPatientVisitId(patientVisitId);
        patientNutritionLifestyle.setPatientTrackId(patientTrackId);
        //when
        Assertions.assertThrows(DataNotAcceptableException.class,
                () -> patientNutritionLifestyleService.removePatientNutritionLifestyle(patientNutritionLifestyle));
    }

    @Test
    @DisplayName("RemovePatientNutritionLifestyle Test")
    void removePatientNutritionLifestyle() {
        //given
        patientNutritionLifestyle = TestDataProvider.getPatientNutritionLifeStyle();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        //when
        when(lifestyleRepository.findByIdAndPatientTrackId(patientNutritionLifestyle.getId(),
                patientNutritionLifestyle.getPatientTrackId())).thenReturn(patientNutritionLifestyle);
        when(treatmentPlanService.getNextFollowUpDate(1L,
                Constants.MEDICAL_REVIEW_FREQUENCY)).thenReturn(new Date());
        when(patientTrackerService.getPatientTrackerById(1L)).thenReturn(patientTracker);
        when(patientTrackerService.addOrUpdatePatientTracker(patientTracker)).thenReturn(patientTracker);
        when(lifestyleRepository.save(patientNutritionLifestyle)).thenReturn(patientNutritionLifestyle);
        List<PatientNutritionLifestyle> patientNutritionLifestyles = List.of(patientNutritionLifestyle);
        //when
        when(patientNutritionLifestyleRepository.findByPatientTrackId(anyLong())).thenReturn(patientNutritionLifestyles);
        when(patientNutritionLifestyleRepository.findByIdAndPatientTrackId(anyLong(), anyLong())).thenReturn(patientNutritionLifestyle);

        //then
        boolean actualStatus = patientNutritionLifestyleService
                .removePatientNutritionLifestyle(patientNutritionLifestyle);
        Assertions.assertTrue(actualStatus);
    }

    @Test
    @DisplayName("GetNutritionReviewedCount Test")
    void testGetNutritionReviewedCount() {
        //given
        Long patientTrackId = 1L;
        int count = 10;
        //when
        when(patientNutritionLifestyleRepository.getNutritionLifestyleReviewedCount(anyLong())).thenReturn(count);
        //then
        int actualCount = patientNutritionLifestyleService.getNutritionLifestyleReviewedCount(patientTrackId);
        Assertions.assertEquals(count, actualCount);
    }

    @Test
    @DisplayName("RemoveNutritionLifestyleByTrackerId Test")
    void removeNutritionLifestyleByTrackerId() {
        //given
        patientNutritionLifestyle = TestDataProvider.getPatientNutritionLifeStyle();
        List<PatientNutritionLifestyle> patientNutritionLifestyles = List.of(patientNutritionLifestyle);
        //when
        when(patientNutritionLifestyleRepository
                .findByPatientTrackId(anyLong())).thenReturn(patientNutritionLifestyles);
        patientNutritionLifestyleService.removeNutritionLifestyleByTrackerId(1L);
        Assertions.assertTrue(patientNutritionLifestyles.get(Constants.ZERO).isDeleted());

    }
}
