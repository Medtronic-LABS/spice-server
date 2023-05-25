package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.FormMetaUi;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.FormMetaRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.FormMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * This service class contain all the business logic for ModelQuestions module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class FormMetaServiceImpl extends GenericServiceImpl<FormMetaUi> implements FormMetaService {

    @Autowired
    private FormMetaRepository formMetaRepository;

    /**
     * {@inheritDoc}
     */
    public List<FormMetaUi> getMetaForms() {
        if (Constants.FORM_META_UIS.isEmpty()) {
            Constants.FORM_META_UIS.addAll(formMetaRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.FORM_META_UIS;
    }

    /**
     * {@inheritDoc}
     */
    public FormMetaUi getMetaForms(String formName) {
        if (Constants.FORM_META_UIS.isEmpty()) {
            Constants.FORM_META_UIS.addAll(formMetaRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        Optional<FormMetaUi> formMetaUiOptional = Constants.FORM_META_UIS.stream().filter(obj -> formName.equals(obj.getFormName())).findFirst();
        return formMetaUiOptional.orElse(null);
    }
}
