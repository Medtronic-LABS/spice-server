package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.ModelQuestions;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;

/**
 * <p>
 * This an interface class for ModelQuestions module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface ModelQuestionsService extends GenericService<ModelQuestions> {

    /**
     * <p>
     * Gets a all model questions.
     * </p>
     *
     * @return {@link List<ModelQuestions>} List of Model Questions
     */
    List<ModelQuestions> getModelQuestions();

    /**
     * <p>
     * Gets a all model questions.
     * </p>
     *
     * @return {@link List<ModelQuestions>} List of Model Questions
     */
    List<ModelQuestions> getModelQuestionsByIsDefault();


    /**
     * <p>
     * Gets a all model questions by countryId.
     * </p>
     *
     * @param countryId {@link Long} countryId
     * @return {@link List<ModelQuestions>} List of Model Questions
     */
    List<ModelQuestions> getModelQuestions(Long countryId);
}
