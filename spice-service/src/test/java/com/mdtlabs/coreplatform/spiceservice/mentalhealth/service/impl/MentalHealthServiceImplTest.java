package com.mdtlabs.coreplatform.spiceservice.mentalhealth.service.impl;

import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.MentalHealthDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealthDetails;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.spiceservice.common.mapper.MentalHealthMapper;
import com.mdtlabs.coreplatform.spiceservice.mentalhealth.repository.MentalHealthRepository;
import com.mdtlabs.coreplatform.spiceservice.patienttracker.service.PatientTrackerService;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

/**
 * <p>
 * MentalHealthServiceImplTest class used to test all possible positive
 * and negative cases for all methods and conditions used in
 * MentalHealthServiceImpl class.
 * </p>
 *
 * @author Nandhakumar Karthikeyan created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MentalHealthServiceImplTest {

    @InjectMocks
    private MentalHealthServiceImpl mentalHealthServiceImpl;

    @Mock
    private PatientTrackerService patientTrackerService;

    @Mock
    private MentalHealthRepository mentalHealthRepository;

    @Mock
    private MentalHealthMapper mentalHealthMapper;

    static Stream<Arguments> mentalHealthData() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(2),
                Arguments.of(3),
                Arguments.of(4),
                Arguments.of(5));
    }

    @Test
    @DisplayName("MentalHealthCreate BadRequest Test")
    void createOrUpdateMentalHealthBadRequest() {
        // given
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();
        mentalHealth.setPatientTrackId(null);

        // then
        Assertions.assertThrows(BadRequestException.class, () -> {
            mentalHealthServiceImpl.createOrUpdateMentalHealth(mentalHealth);
        });
    }

    @Test
    @DisplayName("MentalHealthCreate DataNotFound Test")
    void createOrUpdateMentalHealthDataNotFound() {
        // given
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();

        // when
        when(patientTrackerService.getPatientTrackerById(mentalHealth.getPatientTrackId()))
                .thenReturn(TestDataProvider.getPatientTracker());
        when(mentalHealthRepository.findByIdAndIsDeleted(1L, false)).thenReturn(null);

        // then
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            mentalHealthServiceImpl.createOrUpdateMentalHealth(mentalHealth);
        });
    }

    @Test
    @DisplayName("MentalHealthCreate Test")
    void createOrUpdateMentalHealth() {
        // given
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();
        mentalHealth.setLatest(false);

        // when
        when(patientTrackerService.getPatientTrackerById(mentalHealth.getPatientTrackId()))
                .thenReturn(TestDataProvider.getPatientTracker());
        when(mentalHealthRepository.findByIdAndIsDeleted(1L, false)).thenReturn(mentalHealth);
        when(mentalHealthRepository.save(mentalHealth)).thenReturn(mentalHealth);

        // then
        MentalHealth mentalHealthResponse = mentalHealthServiceImpl.createOrUpdateMentalHealth(mentalHealth);
        Assertions.assertNotNull(mentalHealthResponse);
        Assertions.assertEquals(mentalHealth, mentalHealthResponse);
        Assertions.assertFalse(mentalHealthResponse.isLatest());
    }

    @Test
    @DisplayName("MentalHealthCreate Null mentalhealth Id")
    void createOrUpdateMentalHealthNullMentalHealthId() {
        // given
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();
        mentalHealth.setId(null);

        // when
        when(patientTrackerService.getPatientTrackerById(mentalHealth.getPatientTrackId()))
                .thenReturn(TestDataProvider.getPatientTracker());
        when(mentalHealthRepository.findByPatientTrackIdAndIsDeletedAndIsLatest(1L, false, true))
                .thenReturn(mentalHealth);
        when(mentalHealthRepository.save(mentalHealth)).thenReturn(mentalHealth);

        // then
        MentalHealth mentalHealthResponse = mentalHealthServiceImpl.createOrUpdateMentalHealth(mentalHealth);
        Assertions.assertNotNull(mentalHealthResponse);
        Assertions.assertEquals(mentalHealth, mentalHealthResponse);
        Assertions.assertTrue(mentalHealthResponse.isLatest());
    }

    @Test
    @DisplayName("MentalHealthCreate Null Old Mental health")
    void createOrUpdateMentalHealthNullOldMentalHeatlth() {
        // given
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();
        mentalHealth.setId(null);

        // when
        when(patientTrackerService.getPatientTrackerById(mentalHealth.getPatientTrackId()))
                .thenReturn(TestDataProvider.getPatientTracker());
        when(mentalHealthRepository.findByPatientTrackIdAndIsDeletedAndIsLatest(1L, false, true)).thenReturn(null);
        when(mentalHealthRepository.save(mentalHealth)).thenReturn(mentalHealth);

        // then
        MentalHealth mentalHealthResponse = mentalHealthServiceImpl.createOrUpdateMentalHealth(mentalHealth);
        Assertions.assertNotNull(mentalHealthResponse);
        Assertions.assertEquals(mentalHealth, mentalHealthResponse);
        Assertions.assertTrue(mentalHealthResponse.isLatest());
    }

    @ParameterizedTest
    @MethodSource("mentalHealthData")
    void createOrUpdateMentalHealthQuestionNullTest(int order) {
        // given
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();
        MentalHealthDetails mentalHealthDetails = new MentalHealthDetails();
        mentalHealthDetails.setDisplayOrder(order);
        List<MentalHealthDetails> mentalHealthList = new ArrayList<MentalHealthDetails>();
        mentalHealth.setId(null);
        mentalHealthList.add(mentalHealthDetails);

        // when
        when(patientTrackerService.getPatientTrackerById(mentalHealth.getPatientTrackId()))
                .thenReturn(TestDataProvider.getPatientTracker());
        when(mentalHealthRepository.findByPatientTrackIdAndIsDeletedAndIsLatest(1L, false, true)).thenReturn(null);
        when(mentalHealthRepository.save(mentalHealth)).thenReturn(mentalHealth);
        mentalHealth.setPhq4MentalHealth(mentalHealthList);

        // then
        MentalHealth mental = mentalHealthServiceImpl.createOrUpdateMentalHealth(mentalHealth);
        Assertions.assertNotNull(mental);
        Assertions.assertEquals(mentalHealth, mental);
    }

    @Test
    @DisplayName("createMentalHealth Test")
    void createMentalHealthTest() {
        // given
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        List<MentalHealthDetails> mentalHealthList = new ArrayList<MentalHealthDetails>();

        // when
        when(mentalHealthRepository.findByPatientTrackIdAndIsDeletedAndIsLatest(1L, false, true))
                .thenReturn(mentalHealth);
        when(mentalHealthRepository.save(mentalHealth)).thenReturn(mentalHealth);
        mentalHealth.setPhq4MentalHealth(mentalHealthList);
        mentalHealth.setPhq9MentalHealth(mentalHealthList);
        mentalHealth.setGad7MentalHealth(mentalHealthList);

        // then
        MentalHealth mentalHealthResponse = mentalHealthServiceImpl.createMentalHealth(mentalHealth, patientTracker,
                true);
        Assertions.assertNotNull(mentalHealthResponse);
        Assertions.assertEquals(mentalHealth, mentalHealthResponse);
        Assertions.assertTrue(mentalHealthResponse.isLatest());
    }

    @Test
    @DisplayName("createMentalHealth Null Test")
    void createMentalHealthNullTest() {
        // given
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        List<MentalHealthDetails> mentalHealthList = new ArrayList<MentalHealthDetails>();

        // when
        when(mentalHealthRepository.findByPatientTrackIdAndIsDeletedAndIsLatest(1L, false, true))
                .thenReturn(null);
        when(mentalHealthRepository.save(mentalHealth)).thenReturn(mentalHealth);
        mentalHealth.setPhq4MentalHealth(mentalHealthList);
        mentalHealth.setPhq9MentalHealth(mentalHealthList);
        mentalHealth.setGad7MentalHealth(mentalHealthList);

        // then
        MentalHealth mentalHealthResponse = mentalHealthServiceImpl.createMentalHealth(mentalHealth, patientTracker,
                true);
        Assertions.assertNotNull(mentalHealthResponse);
        Assertions.assertEquals(mentalHealth, mentalHealthResponse);
        Assertions.assertTrue(mentalHealthResponse.isLatest());
    }

    @Test
    @MethodSource("mentalHealthDTOData")
    void getMentalHealthDetailsTest() {
        // given
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();
        RequestDTO requestData = TestDataProvider.getRequestDto();
        MentalHealthDTO mentalHealthDTO = TestDataProvider.getMentalHealthDto();
        MentalHealthDTO mentalHealthDto = new MentalHealthDTO();
        mentalHealthDTO.setPhq4FirstScore(6);

        // when
        when(mentalHealthRepository
                .findByPatientTrackIdAndIsDeletedAndIsLatest(requestData.getPatientTrackId(),
                        false, true))
                .thenReturn(mentalHealth);
        when(mentalHealthMapper.setMentalHealthDTO(mentalHealthDto, requestData, mentalHealth))
                .thenReturn(mentalHealthDTO);

        // then
        MentalHealthDTO response = mentalHealthServiceImpl.getMentalHealthDetails(requestData);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(mentalHealthDTO.getId(), response.getId());
        Assertions.assertEquals(mentalHealthDTO.getPhq4FirstScore(), response.getPhq4FirstScore());
    }

    @Test
    @MethodSource("getMentalHealthDetailsTest Null Test")
    void getMentalHealthDetailsTestNullTest() {
        // given
        RequestDTO requestData = TestDataProvider.getRequestDto();

        // when
        when(mentalHealthRepository
                .findByPatientTrackIdAndIsDeletedAndIsLatest(requestData.getPatientTrackId(),
                        false, true))
                .thenReturn(null);

        // then
        MentalHealthDTO response = mentalHealthServiceImpl.getMentalHealthDetails(requestData);
        Assertions.assertNotNull(response);
        Assertions.assertNull(response.getId());
    }

    @Test
    @DisplayName("MentalHealthCreate DataNotFound check")
    void getMentalHealthDetailsTestNullCheck() {
        // given
        RequestDTO requestData = TestDataProvider.getRequestDto();
        requestData.setPatientTrackId(null);

        // then
        Assertions.assertThrows(DataNotAcceptableException.class, () -> {
            mentalHealthServiceImpl.getMentalHealthDetails(requestData);
        });
    }
}
