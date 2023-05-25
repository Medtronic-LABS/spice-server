package com.mdtlabs.coreplatform.spiceservice.devicedetails.controller;

import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.model.dto.spice.DeviceDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.DeviceDetails;
import com.mdtlabs.coreplatform.spiceservice.devicedetails.service.DeviceDetailsService;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * This controller class performs the REST operations on DeviceDetails entity.
 * </p>
 *
 * @author Nandhakumar Karthikeyan
 * @since Jun 30, 2022
 */
@RestController
@RequestMapping(value = "/devicedetails")
@Validated
public class DeviceDetailsController {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private DeviceDetailsService deviceDetailsService;

    /**
     * <p>
     * This method is used to validate device details.
     * </p>
     *
     * @param deviceDetailsDto {@link DeviceDetailsDTO} device details entity
     * @return {@link SuccessResponse<Map<String, Long>>} deviseDetails Entity.
     */
    @PostMapping
    @TenantValidation
    public SuccessResponse<Map<String, Long>> validateDeviceDetails(@RequestBody DeviceDetailsDTO deviceDetailsDto) {
        return new SuccessResponse<>(SuccessCode.DEVICE_DETAILS_SAVE,
                deviceDetailsService.validateDeviceDetails(modelMapper.map(deviceDetailsDto, DeviceDetails.class)),
                HttpStatus.CREATED);
    }
}
