package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.FormMetaUi;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This interface is responsible for performing database operations between
 * server and DosageFrequency entity.
 * </p>
 *
 * @author Yoheshwaran
 */
@Repository
public interface FormMetaRepository extends GenericRepository<FormMetaUi> {

    /**
     * <p>
     * This function returns a list of active and non-deleted FormMetaUi objects.
     * </p>
     *
     * @return {@link List<FormMetaUi>} The method `findByIsDeletedFalseAndIsActiveTrue()` is returning a list of `FormMetaUi`
     * objects where the `isDeleted` property is false and the `isActive` property is true.
     */
    List<FormMetaUi> findByIsDeletedFalseAndIsActiveTrue();

}
