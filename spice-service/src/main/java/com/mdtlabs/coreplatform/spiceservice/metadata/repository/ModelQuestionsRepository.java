package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.ModelQuestions;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;

import java.util.List;

/**
 * This interface is responsible for performing database operations between
 * server and ModelQuestions entity.
 *
 * @author Niraimathi S
 */
public interface ModelQuestionsRepository extends GenericRepository<ModelQuestions> {

    /**
     * <p>
     * Gets list of ModelQuestions entities based on isDefault and isDeleted Fields.
     * </p>
     *
     * @param isDefault {@link boolean} isDefault value
     * @param isDeleted {@link boolean} isDeleted value
     * @return {@link List<ModelQuestions>} List of ModelQuestions entities
     */
    List<ModelQuestions> findByIsDefaultAndIsDeleted(boolean isDefault, boolean isDeleted);

    /**
     * <p>
     * Gets list of active model questions.
     * </p>
     *
     * @return {@link List<ModelQuestions>} List of ModelQuestions entities
     */
    List<ModelQuestions> findByIsDeletedFalseAndIsActiveTrue();

    /**
     * <p>
     * Gets list of model questions by country id.
     * </p>
     *
     * @param countryId       {@link Long} countryId
     * @param booleanValueOne {@link Boolean} false1
     * @param booleanValueTwo {@link Boolean} booleanValueTwo
     * @return {@link List<ModelQuestions>} List of ModelQuestions entities.
     */
    List<ModelQuestions> findByCountryIdAndIsDeletedAndIsActiveOrderByDisplayOrderAsc(Long countryId, Boolean booleanValueOne, Boolean booleanValueTwo);

}
