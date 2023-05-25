package com.mdtlabs.coreplatform.common.repository;

import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
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
 * @author Prabu created on Oct 20, 2022
 */
@Repository
public interface GenericRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

}
