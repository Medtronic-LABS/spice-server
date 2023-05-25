package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.Culture;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


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
public interface CultureRepository extends GenericRepository<Culture> {

    /**
     * <p>
     * To get the active cultures.
     * </p>
     *
     * @return {@link List<Culture>} List of culture entities.
     */
    List<Culture> findByIsDeletedFalseAndIsActiveTrue();

    /**
     * <p>
     * To get culture by name.
     * <p>
     *
     * @param name {@link String} name
     * @return Culture {@link Culture} entity.
     */
    Culture findByNameIgnoreCase(String name);

}
