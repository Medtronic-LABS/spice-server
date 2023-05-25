package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.FormMetaUi;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.FormMetaRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.FormMetaServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * <p>
 * FormMeta Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FormMetaServiceTest {

    @InjectMocks
    private FormMetaServiceImpl formMetaService;

    @Mock
    private FormMetaRepository formMetaRepository;

    @Test
    void getMetaForms() {
        //given
        List<FormMetaUi> formMetaUis = List.of(TestDataProvider.getFormMetaUi());

        //when
        when(formMetaRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(formMetaUis);

        //then
        List<FormMetaUi> metaForms = formMetaService.getMetaForms();
        Assertions.assertEquals(formMetaUis.size(), metaForms.size());

        //then
        metaForms = formMetaService.getMetaForms();
        Assertions.assertFalse(Constants.FORM_META_UIS.isEmpty());
    }

    @Test
    void testGetMetaForms() {
        //given
        Constants.FORM_META_UIS = new ArrayList<>();
        List<FormMetaUi> formMetaUis = List.of(TestDataProvider.getFormMetaUi());

        //when
        when(formMetaRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(formMetaUis);

        //then
        FormMetaUi metaForms = formMetaService.getMetaForms("testFormName");
        Assertions.assertEquals(formMetaUis.get(0).getFormName(), metaForms.getFormName());

        //then
        metaForms = formMetaService.getMetaForms("testFormName");
        Assertions.assertFalse(Constants.FORM_META_UIS.isEmpty());
    }
}