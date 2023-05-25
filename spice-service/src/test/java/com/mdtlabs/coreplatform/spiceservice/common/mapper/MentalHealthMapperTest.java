package com.mdtlabs.coreplatform.spiceservice.common.mapper;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.MentalHealthDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealthDetails;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * <p>
 * MentalHealthMapperTest class used to test all possible positive
 * and negative cases for all methods and conditions used in ScreeningMapper class.
 * </p>
 *
 * @author Jaganathan created on Mar 3, 2023
 */
@ExtendWith(MockitoExtension.class)
class MentalHealthMapperTest {

    @InjectMocks
    MentalHealthMapper mentalHealthMapper;

    @Test
    @DisplayName("SetPatientTracker Test")
    void setPatientTracker() {
        //given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();
        //then
        PatientTracker actualPatientTracker = mentalHealthMapper.setPatientTracker(patientTracker, mentalHealth);
        Assertions.assertEquals(patientTracker, actualPatientTracker);
        //given
        mentalHealth.setPhq4MentalHealth(List.of());
        //then
        actualPatientTracker = mentalHealthMapper.setPatientTracker(patientTracker, mentalHealth);
        Assertions.assertEquals(patientTracker, actualPatientTracker);
        //given
        mentalHealth.setPhq4MentalHealth(List.of(new MentalHealthDetails()));
        //then
        actualPatientTracker = mentalHealthMapper.setPatientTracker(patientTracker, mentalHealth);
        Assertions.assertEquals(patientTracker, actualPatientTracker);
        //given
        mentalHealth.setPhq9MentalHealth(List.of());
        //then
        actualPatientTracker = mentalHealthMapper.setPatientTracker(patientTracker, mentalHealth);
        Assertions.assertEquals(patientTracker, actualPatientTracker);
        //given
        mentalHealth.setPhq9MentalHealth(List.of(new MentalHealthDetails()));
        //then
        actualPatientTracker = mentalHealthMapper.setPatientTracker(patientTracker, mentalHealth);
        Assertions.assertEquals(patientTracker, actualPatientTracker);
        //given
        mentalHealth.setGad7MentalHealth(List.of());
        //then
        actualPatientTracker = mentalHealthMapper.setPatientTracker(patientTracker, mentalHealth);
        Assertions.assertEquals(patientTracker, actualPatientTracker);
        //given
        mentalHealth.setGad7MentalHealth(List.of(new MentalHealthDetails()));
        //then
        actualPatientTracker = mentalHealthMapper.setPatientTracker(patientTracker, mentalHealth);
        Assertions.assertEquals(patientTracker, actualPatientTracker);
    }

    @ParameterizedTest
    @DisplayName("SetMentalHealthDTO Test")
    @ValueSource(strings = {Constants.PHQ4, Constants.PHQ9, Constants.GAD7})
    void setMentalHealthDTO(String type) {
        //given
        MentalHealthDTO mentalHealthDTO = TestDataProvider.getMentalHealthDto();
        RequestDTO requestDTO = TestDataProvider.getRequestDto();
        MentalHealth mentalHealth = TestDataProvider.getMentalHealth();
        requestDTO.setType(type);
        //then
        MentalHealthDTO actualMentalHealthDTO = mentalHealthMapper.setMentalHealthDTO(mentalHealthDTO,
                requestDTO, mentalHealth);
        Assertions.assertEquals(mentalHealthDTO, actualMentalHealthDTO);
    }
}
