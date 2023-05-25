package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.model.dto.spice.CultureDTO;
import com.mdtlabs.coreplatform.common.model.entity.Culture;
import com.mdtlabs.coreplatform.common.model.entity.spice.CultureValues;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.common.repository.CultureValuesRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.CultureRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CultureService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This service class contain all the business logic for culture module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class CultureServiceImpl extends GenericServiceImpl<Culture> implements CultureService {

    @Autowired
    private CultureRepository cultureRepository;

    @Autowired
    private CultureValuesRepository cultureValuesRepository;

    /**
     * {@inheritDoc}
     */
    private static Map<String, Map<Long, String>> getCultureValues(Map<Long, Map<String, Map<Long, String>>> cultureValuesMap, CultureValues cultureValue, Map<String, Map<Long, String>> cultureValues) {
        if (cultureValuesMap.containsKey(cultureValue.getCultureId())) {
            cultureValues = cultureValuesMap.get(cultureValue.getCultureId());
        }
        if (cultureValues.containsKey(cultureValue.getFormName())) {
            cultureValues.get(cultureValue.getFormName()).put(cultureValue.getFormDataId(),
                    cultureValue.getCultureValue());
        } else {
            Map<Long, String> cultureMap = new HashMap<>();
            cultureMap.put(cultureValue.getFormDataId(), cultureValue.getCultureValue());
            cultureValues.put(cultureValue.getFormName(), cultureMap);
        }
        return cultureValues;
    }

    /**
     * {@inheritDoc}
     */
    public List<CultureDTO> getAllCultures() {
        if (Constants.CULTURE_LIST.isEmpty()) {
            ModelMapper modelMapper = new ModelMapper();
            List<Culture> cultures = cultureRepository.findByIsDeletedFalseAndIsActiveTrue();
            if (!Objects.isNull(cultures) && !cultures.isEmpty()) {
                Constants.CULTURE_LIST.addAll(cultures.stream().map(culture ->
                    modelMapper.map(culture, CultureDTO.class)
                ).toList());
            }
        }
        return Constants.CULTURE_LIST;
    }

    /**
     * {@inheritDoc}
     */
    public Culture getCultureByName(String name) {
        return cultureRepository.findByNameIgnoreCase(name);
    }

    /**
     * {@inheritDoc}
     */
    public void getCultureValues() {
        List<CultureValues> cultureValuesList = cultureValuesRepository.findAll();
        Map<Long, Map<String, Map<Long, String>>> cultureValuesMap = new HashMap<>();
        Map<Long, Map<String, Map<Long, Object>>> jsonCultureValuesMap = new HashMap<>();
        cultureValuesList.forEach(cultureValue -> {
            if (!Objects.isNull(cultureValue.getCultureValue())) {
                Map<String, Map<Long, String>> cultureValues = new HashMap<>();
                cultureValues = getCultureValues(cultureValuesMap, cultureValue, cultureValues);
                cultureValuesMap.put(cultureValue.getCultureId(), cultureValues);
            } else if (!Objects.isNull(cultureValue.getJsonCultureValue())) {
                Map<String, Map<Long, Object>> cultureValues = new HashMap<>();
                if (jsonCultureValuesMap.containsKey(cultureValue.getCultureId())) {
                    cultureValues = jsonCultureValuesMap.get(cultureValue.getCultureId());
                }
                if (cultureValues.containsKey(cultureValue.getFormName())) {
                    cultureValues.get(cultureValue.getFormName()).put(cultureValue.getFormDataId(),
                            cultureValue.getJsonCultureValue());
                } else {
                    Map<Long, Object> cultureMap = new HashMap<>();
                    cultureMap.put(cultureValue.getFormDataId(), cultureValue.getJsonCultureValue());
                    cultureValues.put(cultureValue.getFormName(), cultureMap);
                }
                jsonCultureValuesMap.put(cultureValue.getCultureId(), cultureValues);
            }
        });
        Constants.CULTURE_VALUES_MAP.clear();
        Constants.CULTURE_VALUES_MAP.putAll(cultureValuesMap);
        Constants.JSON_CULTURE_VALUES_MAP.clear();
        Constants.JSON_CULTURE_VALUES_MAP.putAll(jsonCultureValuesMap);
    }

    /**
     * {@inheritDoc}
     */
    public Long loadCultureValues() {
        if (Constants.CULTURE_VALUES_MAP.isEmpty()) {
            getCultureValues();
        }
        Long cultureId = UserContextHolder.getUserDto().getCultureId();
        if (null == cultureId) {
            cultureId = getCultureByName(Constants.DEFAULT_CULTURE_VALUE).getId();
        }
        return cultureId;
    }

}
