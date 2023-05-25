package com.mdtlabs.coreplatform.spiceservice.devicedetails.service.impl;

import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.entity.DeviceDetails;
import com.mdtlabs.coreplatform.spiceservice.devicedetails.repository.DeviceDetailsRepository;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * <p>
 * DeviceDetailsServiceImplTest class used to test all possible positive
 * and negative cases for all methods and conditions used in DeviceDetailsServiceImpl class.
 * </p>
 *
 * @author Nandhakumar Karthikeyan created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
class DeviceDetailsServiceImplTest {

    @InjectMocks
    private DeviceDetailsServiceImpl deviceDetailsServiceImpl;

    @Mock
    private DeviceDetailsRepository deviceDetailsRepository;

    @Test
    @DisplayName("validate device details Test")
    void validateDeviceDetails() {
        //given
        DeviceDetails deviceDetails = TestDataProvider.getDeviceDetails();
        UserDTO userDto = TestDataProvider.getUserDTO();
        MockedStatic<UserContextHolder> userHolder = mockStatic(UserContextHolder.class);

        //when
        userHolder.when(UserContextHolder::getUserDto).thenReturn(userDto);
        when(deviceDetailsRepository
                .findByUserIdAndTenantIdAndDeviceId(userDto.getId(), deviceDetails.getTenantId(), deviceDetails.getDeviceId())).thenReturn(deviceDetails);
        when(deviceDetailsRepository.save(deviceDetails)).thenReturn(deviceDetails);

        //then
        Map<String, Long> response = deviceDetailsServiceImpl.validateDeviceDetails(deviceDetails);
        userHolder.close();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(deviceDetails.getId(), response.get("id"));
    }

    @Test
    @DisplayName("validate device details Test")
    void validateDeviceDetailsNullCheck() {
        //given
        DeviceDetails deviceDetails = TestDataProvider.getDeviceDetails();
        UserDTO userDto = TestDataProvider.getUserDTO();
        MockedStatic<UserContextHolder> userHolder = mockStatic(UserContextHolder.class);

        //when
        userHolder.when(UserContextHolder::getUserDto).thenReturn(userDto);
        when(deviceDetailsRepository
                .findByUserIdAndTenantIdAndDeviceId(userDto.getId(), deviceDetails.getTenantId(), deviceDetails.getDeviceId())).thenReturn(null);
        when(deviceDetailsRepository.save(deviceDetails)).thenReturn(deviceDetails);

        //then
        Map<String, Long> response = deviceDetailsServiceImpl.validateDeviceDetails(deviceDetails);
        userHolder.close();
        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.get("id"), deviceDetails.getId());
    }
}
