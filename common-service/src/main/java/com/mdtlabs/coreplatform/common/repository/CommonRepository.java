package com.mdtlabs.coreplatform.common.repository;

import com.mdtlabs.coreplatform.common.model.entity.ApiRolePermission;
import com.mdtlabs.coreplatform.common.model.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This is the repository class for auditing all the values This class used to
 * perform auditing action in database. In query annotation (nativeQuery = true)
 * the below query perform like SQL. Otherwise its perform like HQL default
 * value for nativeQuery FALSE
 * </p>
 *
 * @author Prabu created on July 11, 2022
 */

@Repository
public interface CommonRepository extends JpaRepository<Audit, Long> {

    String GET_API_ROLE_PERMISSION = "select apiRolePermission from ApiRolePermission as apiRolePermission";

    /**
     * <p>
     * This method is used to get api roles permission
     * </p>
     *
     * @return List<ApiRolePermission> - list of ApiRolePermission entity
     */
    @Query(value = GET_API_ROLE_PERMISSION)
    List<ApiRolePermission> getApiRolePermission();

}