package com.mdtlabs.coreplatform.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * <p>
 * This is the repository class for tenant values. In query annotation
 * (nativeQuery = true) the below query perform like SQL. Otherwise its perform
 * like HQL default value for nativeQuery FALSE
 * </p>
 *
 * @author Vignesh created on July 30, 2022
 */
@NoRepositoryBean
public interface TenantableRepository<T> extends JpaRepository<T, Long> {

    /**
     * <p>
     * get the tenant value by id.
     * </p>
     *
     * @param id  tenant id
     * @return {@link Optional<T>}  tenant entity
     */
    Optional<T> findOneById(Long id);
}