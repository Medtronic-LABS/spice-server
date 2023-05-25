package com.mdtlabs.coreplatform.spiceservice.mentalhealth.controller;

import com.mdtlabs.coreplatform.common.model.dto.spice.MentalHealthDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import com.mdtlabs.coreplatform.spiceservice.mentalhealth.service.MentalHealthService;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.when;

/**
 * <p>
 * MentalHealthControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in MentalHealthController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MentalHealthControllerTest {

    @InjectMocks
    private MentalHealthController mentalHealthController;

    @Mock
    private MentalHealthService mentalHealthService;

    @Mock
    private ModelMapper mapper;

    private RequestDTO request;

    private SuccessResponse<MentalHealthDTO> response;

    private MentalHealth mentalHealth = TestDataProvider.getMentalHealth();

    private MentalHealthDTO mentalHealthDTO = TestDataProvider.getMentalHealthDto();

    @Test
    @DisplayName("MentalHealthCreate Test")
    void createMentalHealth() {
        //given
        MentalHealthDTO newMentalHealthDTO = null;
        MentalHealth newMentalHealth = null;
        //when
        when(mapper.map(newMentalHealthDTO, MentalHealth.class)).thenReturn(newMentalHealth);
        when(mapper.map(mentalHealthDTO, MentalHealth.class)).thenReturn(mentalHealth);
        when(mentalHealthService.createOrUpdateMentalHealth(mentalHealth)).thenReturn(mentalHealth);
        when(mentalHealthService.createOrUpdateMentalHealth(newMentalHealth)).thenReturn(newMentalHealth);
        //then
        response = mentalHealthController.createMentalHealth(mentalHealthDTO);
        SuccessResponse<MentalHealthDTO> newResponse = mentalHealthController.createMentalHealth(new MentalHealthDTO());
        Assertions.assertNotNull(response);
        MentalHealth actualHealth = mentalHealthService.createOrUpdateMentalHealth(newMentalHealth);
        MentalHealth actualMentalHealth = mentalHealthService.createOrUpdateMentalHealth(mentalHealth);
        Assertions.assertEquals(HttpStatus.CREATED,
                response.getStatusCode(), "SuccessCode Should be Same");
        Assertions.assertEquals(actualMentalHealth.getId(),
                mentalHealth.getId(), "MentalHeath Id Should be Same");
        Assertions.assertEquals(newMentalHealth, actualHealth, "MentalHealth Should  be Null");

    }

    @Test
    @DisplayName("UpdateMentalHealth Test")
    void updateMentalHealth() {
        //given
        MentalHealthDTO newMentalHealthDTO = new MentalHealthDTO();
        MentalHealth newMentalHealth = null;
        //when
        when(mapper.map(newMentalHealthDTO, MentalHealth.class)).thenReturn(newMentalHealth);
        when(mapper.map(mentalHealthDTO, MentalHealth.class)).thenReturn(mentalHealth);
        when(mentalHealthService.createOrUpdateMentalHealth(newMentalHealth)).thenReturn(newMentalHealth);
        //then
        response = mentalHealthController.updateMentalHealth(newMentalHealthDTO);
        SuccessResponse<MentalHealthDTO> newResponse = mentalHealthController.updateMentalHealth(newMentalHealthDTO);
        Assertions.assertEquals(HttpStatus.OK,
                response.getStatusCode(), "SuccessCode Should be Same");
        Assertions.assertEquals(null,
                mentalHealthService.createOrUpdateMentalHealth(newMentalHealth),
                "MentalHealth Should Not be Null");
        //given
        mentalHealth.setPhq9RiskLevel("medium");
        //when
        when(mentalHealthService.createOrUpdateMentalHealth(mentalHealth)).thenReturn(mentalHealth);
        //then
        response = mentalHealthController.updateMentalHealth(mentalHealthDTO);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(mentalHealthService.createOrUpdateMentalHealth(mentalHealth));
        Assertions.assertEquals(HttpStatus.OK,
                response.getStatusCode(), "SuccessCode Should be Same");
        Assertions.assertEquals(mentalHealthService.createOrUpdateMentalHealth(mentalHealth).getPhq4RiskLevel(),
                mentalHealth.getPhq4RiskLevel(), "MentalHeath Phq4RiskLevel Should be Same");
        Assertions.assertNotEquals(null,
                mentalHealthService.createOrUpdateMentalHealth(mentalHealth),
                "MentalHealth Should Not be Null");
    }

    @Test
    @DisplayName("GetMentalHealthDetails Test")
    void getMentalHealthDetails() {
        //given
        request = TestDataProvider.getRequestDto();
        //when
        when(mentalHealthService.getMentalHealthDetails(request)).thenReturn(mentalHealthDTO);
        //then
        response = mentalHealthController.getMentalHealthDetails(request);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), "SuccessCode Should be Same");
        Assertions.assertEquals(mentalHealthDTO.getId(),
                mentalHealthService.getMentalHealthDetails(request).getId(), "MentalHealth Id Should be Same");

        Assertions.assertNotEquals(null, mentalHealthService.getMentalHealthDetails(request),
                "Request Should Not be Empty");
    }
}
