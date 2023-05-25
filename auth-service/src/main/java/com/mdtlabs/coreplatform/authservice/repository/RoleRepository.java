package com.mdtlabs.coreplatform.authservice.repository;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the role module action in database.
 * In query annotation (nativeQuery = true) the below query perform like SQL.
 * Otherwise its perform like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author VigneshKumar
 * @since Aug 26, 2022
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, PagingAndSortingRepository<Role, Long> {

    String GET_ALL_ROLES = "select role from Role as role where role.isActive =:status ";

    /**
     * <p>
     * This method is used to get the list of roles from the database
     * who are all either active or inactive.
     * </p>
     *
     * @param status The status that is used to filter the list of roles that have
     *               the specified active status is given
     * @return {@link List<Role>} The list of roles who are all either active or
     * inactive is retrieved and returned from database is given
     */
    @Query(value = GET_ALL_ROLES)
    List<Role> getAllRoles(@Param(FieldConstants.STATUS) boolean status);
}
