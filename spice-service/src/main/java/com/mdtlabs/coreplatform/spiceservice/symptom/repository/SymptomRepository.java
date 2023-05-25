package com.mdtlabs.coreplatform.spiceservice.symptom.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.Symptom;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * SymptomRepository is a Java interface that extends the JpaRepository interface and defines methods for
 * accessing and manipulating data in the symptom table of a database.
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
@Repository
public interface SymptomRepository extends GenericRepository<Symptom> {

    /**
     * <p>
     * This method is used to return a list of active symptoms that have not been deleted.
     * </p>
     *
     * @return {@link List<Symptom>} The list of Symptom that have the "isDeleted" property set to false
     * and the "isActive" property set to true is retrieved from the database
     */
    List<Symptom> findByIsDeletedFalseAndIsActiveTrue();
}
