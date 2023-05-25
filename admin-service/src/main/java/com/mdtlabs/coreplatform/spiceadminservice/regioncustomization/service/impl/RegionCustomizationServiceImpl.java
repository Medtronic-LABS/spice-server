package com.mdtlabs.coreplatform.spiceadminservice.regioncustomization.service.impl;

import java.util.List;
import java.util.Objects;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.CustomizationRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.RegionCustomization;
import com.mdtlabs.coreplatform.spiceadminservice.regioncustomization.repository.RegionCustomizationRepository;
import com.mdtlabs.coreplatform.spiceadminservice.regioncustomization.service.RegionCustomizationService;

/**
 * <p>
 * RegionCustomizationServiceImpl class that implements methods for creating, retrieving, updating,
 * and getting a list of region customizations using a repository and a model mapper.
 * </p>
 *
 * @author Jeyaharini T A created on Jun30, 2022
 */
@Service
public class RegionCustomizationServiceImpl implements RegionCustomizationService {

    @Autowired
    private RegionCustomizationRepository regionCustomizationRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * {@inheritDoc}
     */
    public RegionCustomization createCustomization(RegionCustomization regionCustomization) {
        return regionCustomizationRepository.save(regionCustomization);
    }

    /**
     * {@inheritDoc}
     */
    public RegionCustomization getCustomization(CustomizationRequestDTO customizationRequestDto) {
        List<RegionCustomization> regionCustomization = regionCustomizationRepository.findByCountryIdAndCategoryAndTypeAndTenantId(
                customizationRequestDto.getCountryId(), customizationRequestDto.getCategory(),
                customizationRequestDto.getType(), Constants.BOOLEAN_FALSE, customizationRequestDto.getTenantId(), customizationRequestDto.getCultureId());
        if (Objects.isNull(regionCustomization) || regionCustomization.isEmpty()) {
            throw new DataNotFoundException(2251);
        }
        return regionCustomization.get(Constants.ZERO);
    }

    /**
     * {@inheritDoc}
     */
    public RegionCustomization updateCustomization(RegionCustomization regionCustomization) {
        RegionCustomization existingRegionCustomization = regionCustomizationRepository
                .findByIdAndTenantIdAndIsDeletedFalse(regionCustomization.getId(), regionCustomization.getTenantId());
        if (Objects.isNull(existingRegionCustomization)) {
            throw new DataNotFoundException(2151);
        }
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(regionCustomization, existingRegionCustomization);
        return regionCustomizationRepository.save(existingRegionCustomization);
    }

    /**
     * {@inheritDoc}
     */
    public List<RegionCustomization> getRegionCustomizations(long cultureId) {
        Long countryId = UserContextHolder.getUserDto().getCountry().getId();
        return regionCustomizationRepository.findByCountryIdAndCultureId(countryId, cultureId);
    }
}