package com.mdtlabs.coreplatform.spiceservice.devicedetails.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.entity.DeviceDetails;
import com.mdtlabs.coreplatform.spiceservice.devicedetails.repository.DeviceDetailsRepository;
import com.mdtlabs.coreplatform.spiceservice.devicedetails.service.DeviceDetailsService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This class implements the DeviceDetailsService interface and contains actual
 * business logic to perform operations on PatientVisit entity.
 * </p>
 *
 * @author Nandhakumar Karthikeyan
 */
@Service
public class DeviceDetailsServiceImpl implements DeviceDetailsService {

    @Autowired
    private DeviceDetailsRepository deviceDetailsRepository;

    /**
     * {@inheritDoc}
     */
    public Map<String, Long> validateDeviceDetails(DeviceDetails deviceDetails) {
        DeviceDetails response;
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        UserDTO userDto = UserContextHolder.getUserDto();
        DeviceDetails existingDeviceDetails = deviceDetailsRepository
                .findByUserIdAndTenantIdAndDeviceId(userDto.getId(), deviceDetails.getTenantId(),
                        deviceDetails.getDeviceId());
        if (!Objects.isNull(existingDeviceDetails)) {
            mapper.map(deviceDetails, existingDeviceDetails);
            existingDeviceDetails.setLastLoggedIn(new Date());
            response = deviceDetailsRepository.save(existingDeviceDetails);
        } else {
            deviceDetails.setUserId(userDto.getId());
            deviceDetails.setLastLoggedIn(new Date());
            response = deviceDetailsRepository.save(deviceDetails);
        }
        return Map.of(Constants.ID, response.getId());
    }
}