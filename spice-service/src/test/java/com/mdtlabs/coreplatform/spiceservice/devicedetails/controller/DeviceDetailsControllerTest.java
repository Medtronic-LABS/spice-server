package com.mdtlabs.coreplatform.spiceservice.devicedetails.controller;

import com.mdtlabs.coreplatform.common.model.dto.spice.DeviceDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.DeviceDetails;
import com.mdtlabs.coreplatform.spiceservice.devicedetails.service.DeviceDetailsService;
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

import static org.mockito.Mockito.when;

/**
 * <p>
 * DeviceDetailsControllerTest class used to test all possible positive
 * and negative cases for all methods and conditions used in DeviceDetailsController class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class DeviceDetailsControllerTest {

    @InjectMocks
    private DeviceDetailsController deviceDetailsController;

    @Mock
    private DeviceDetailsService deviceDetailsService;

    private DeviceDetails deviceDetails = TestDataProvider.getDeviceDetails();

    private DeviceDetailsDTO deviceDetailsDTO = TestDataProvider.getDeviceDetailsDTO();

    @Test
    @DisplayName("Validate Device Details Test")
    void validateDeviceDetails() {
        //given
        Map<String, Long> device = new HashMap<>();
        device.put("refId", 1l);
        device.put("refId", 2l);
        SuccessResponse<Map<String, Long>> successResponse;
        //when
        when(deviceDetailsService.validateDeviceDetails(deviceDetails)).thenReturn(device);
        //then
        SuccessResponse<Map<String, Long>> response = deviceDetailsController.validateDeviceDetails(deviceDetailsDTO);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(device.size(), deviceDetailsService.validateDeviceDetails(deviceDetails).size());

    }
}
