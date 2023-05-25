package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.FormMetaUi;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;

/**
 * <p>
 * This an interface class for FormMetaUi module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface FormMetaService extends GenericService<FormMetaUi> {

    /**
     * <p>
     * Gets a all meta forms
     * </p>
     *
     * @return {@link List<FormMetaUi>} list of meta forms
     */
    List<FormMetaUi> getMetaForms();

    /**
     * <p>
     * Gets a meta forms by forename
     * </p>
     *
     * @param formName {@link String} formName
     * @return {@link FormMetaUi} list of meta forms
     */
    FormMetaUi getMetaForms(String formName);

}
