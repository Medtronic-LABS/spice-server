package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.CultureDTO;
import com.mdtlabs.coreplatform.common.model.entity.Culture;
import com.mdtlabs.coreplatform.common.model.entity.spice.CultureValues;
import com.mdtlabs.coreplatform.spiceservice.common.repository.CultureValuesRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.CultureRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.CultureServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>
 * Culture Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Mar 09, 2023
 */
@ExtendWith(MockitoExtension.class)
class CultureServiceImplTest {

    @InjectMocks
    private CultureServiceImpl cultureService;

    @Mock
    private CultureRepository cultureRepository;

    @Mock
    private CultureValuesRepository cultureValuesRepository;

    @Test
    void getAllCultures() {
        //given
        Culture culture = TestDataProvider.getCulture();
        List<Culture> cultures = List.of(culture);

        //when
        when(cultureRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(cultures);

        //then
        List<CultureDTO> cultureList = cultureService.getAllCultures();
        Assertions.assertEquals(cultures.size(), cultureList.size());
        Assertions.assertNotNull(culture);
    }

    @Test
    void getCultureByName() {
        //given
        Culture culture = TestDataProvider.getCulture();

        //when
        when(cultureRepository.findByNameIgnoreCase("culture")).thenReturn(culture);

        //then
        Culture result = cultureService.getCultureByName("culture");
        Assertions.assertEquals(culture.getName(), result.getName());
        Assertions.assertNotNull(culture);
    }

    @Test
    void getCultureValues() {
        //given
        CultureValues cultureValues = TestDataProvider.getCultureValues();
        cultureValues.setId(1L);
        CultureValues cultureValuesOne = TestDataProvider.getCultureValues();
        cultureValuesOne.setId(2L);
        List<CultureValues> cultureValuesList = new ArrayList<>();
        cultureValuesList.add(cultureValues);
        cultureValuesList.add(cultureValuesOne);

        //when
        when(cultureValuesRepository.findAll()).thenReturn(cultureValuesList);

        //then
        cultureService.getCultureValues();
        verify(cultureValuesRepository, atLeastOnce()).findAll();
        Assertions.assertNotNull(cultureValues);
    }

    @Test
    void testGetCultureValues() {
        //given
        CultureValues cultureValues = new CultureValues();
        cultureValues.setId(1L);
        cultureValues.setJsonCultureValue("testJsonCulture");
        cultureValues.setFormDataId(1L);
        cultureValues.setFormName(Constants.FORM_NAME);
        cultureValues.setCultureId(1L);
        CultureValues cultureValuesOne = new CultureValues();
        cultureValuesOne.setId(2L);
        cultureValuesOne.setJsonCultureValue("testJsonCulture");
        cultureValuesOne.setFormDataId(1L);
        cultureValuesOne.setFormName(Constants.FORM_NAME);
        cultureValuesOne.setCultureId(1L);
        List<CultureValues> cultureValuesList = new ArrayList<>();
        cultureValuesList.add(cultureValues);
        cultureValuesList.add(cultureValuesOne);
        setCultureValuesMap();

        //when
        when(cultureValuesRepository.findAll()).thenReturn(cultureValuesList);

        //then
        cultureService.getCultureValues();
        verify(cultureValuesRepository, atLeastOnce()).findAll();
        Assertions.assertNotNull(cultureValues);
    }

    @Test
    void getCultureValuesWithNull() {
        //given
        CultureValues cultureValues = new CultureValues();
        cultureValues.setId(1L);
        cultureValues.setJsonCultureValue(null);
        List<CultureValues> cultureValuesList = new ArrayList<>();
        cultureValuesList.add(cultureValues);
        setCultureValuesMap();

        //when
        when(cultureValuesRepository.findAll()).thenReturn(cultureValuesList);

        //then
        cultureService.getCultureValues();
        verify(cultureValuesRepository, atLeastOnce()).findAll();
    }

    private static void setCultureValuesMap() {
        Map<String, Map<Long, String>> cultureMap = Map.of(Constants.CULTURE_VALUE_PHYSICAL_EXAMINATION,
                Map.of(1L, Constants.CULTURE_VALUE), Constants.CULTURE_VALUE_COMPLAINTS,
                Map.of(1L, Constants.CULTURE_VALUE));
        Constants.CULTURE_VALUES_MAP = new HashMap<>(Map.of(1L, cultureMap));
    }
}