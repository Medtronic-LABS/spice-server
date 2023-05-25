package com.mdtlabs.coreplatform.spiceservice.devicedetails.repository;

import com.mdtlabs.coreplatform.common.model.entity.DeviceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the user module action in database.
 * In query annotation (nativeQuery = true) the below query perform like SQL.
 * Otherwise its perform like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface DeviceDetailsRepository extends JpaRepository<DeviceDetails, Long> {


    /**
     * <p>
     * This function finds device details based on the user ID, tenant ID, and device ID.
     * </p>
     *
     * @param userId   {@link Long} The unique identifier of the user whose device details are being searched for.
     * @param tenantId {@link Long} The tenantId parameter is a Long data type that represents the unique identifier
     *                 of a tenant in a multi-tenant system
     * @param deviceId {@link String} The unique identifier of the device that you want to find details for.
     * @return {@link DeviceDetails} The method `findByUserIdAndTenantIdAndDeviceId` returns an object of type
     * `DeviceDetails` that matches the given `userId`, `tenantId`, and `deviceId`.
     */
    DeviceDetails findByUserIdAndTenantIdAndDeviceId(Long userId, Long tenantId, String deviceId);
}
