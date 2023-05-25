package com.mdtlabs.coreplatform.spiceservice.devicedetails.service;

import com.mdtlabs.coreplatform.common.model.entity.DeviceDetails;

import java.util.Map;

/**
 * <p>
 * This is an interface to perform any actions in deviceDetails related
 * entities.
 * </p>
 *
 * @author Nandhakumar Karthikeyan created on Jun 20, 2022
 */
public interface DeviceDetailsService {

    /**
     * <p>
     * The function takes in device details and returns a map of string keys and long values after
     * validating them.
     * </p>
     *
     * @param deviceDetails {@link DeviceDetails} DeviceDetails is an object that contains information about a device. It may
     *                      include attributes such as device ID, device name, device type, device status, and other
     *                      relevant details
     * @return {@link Map<String, Long>} A `Map` object is being returned, where the keys are of type `String` and the values are
     * of type `Long`
     */
    Map<String, Long> validateDeviceDetails(DeviceDetails deviceDetails);

}
