package com.mdtlabs.coreplatform.spiceadminservice.regioncustomization.controller;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.annotations.UserTenantValidation;
import com.mdtlabs.coreplatform.common.model.dto.spice.CustomizationRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RegionCustomizationDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.RegionCustomization;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.regioncustomization.service.RegionCustomizationService;

/**
 * <p>
 * RegionCustomizationController class that defines REST API endpoints for creating, retrieving,
 * and updating region customizations.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
@RestController
@RequestMapping("/region-customization")
public class RegionCustomizationController {

    @Autowired
    private RegionCustomizationService regionCustomizationService;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>
     * This method is used to create a region customization using the given region customization details.
     * </p>
     *
     * @param regionCustomizationDto {@link RegionCustomizationDTO} The region customization that need to be
     *                               created is given
     * @return {@link SuccessResponse<RegionCustomization>} Returns a success message and status with
     * the created region customization
     */
    @UserTenantValidation
    @PostMapping("/create")
    public SuccessResponse<RegionCustomization> addCustomization(@Valid @RequestBody
                                                                 RegionCustomizationDTO regionCustomizationDto) {
        regionCustomizationService
                .createCustomization(modelMapper.map(regionCustomizationDto, RegionCustomization.class));
        return new SuccessResponse<>(SuccessCode.REGION_CUSTOMIZATION_SAVE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get the region customization data details such as screening, enrollment and
     * consent forms based on region organization id.
     * </p>
     *
     * @param customizationRequestDto {@link CustomizationRequestDTO} The customization request contains
     *                                necessary information to get the region customization is given
     * @return {@link SuccessResponse<RegionCustomization>} Returns a success message and status with the retrieved
     * region customization for the given customization request
     */
    @UserTenantValidation
    @PostMapping("/details")
    public SuccessResponse<RegionCustomization> getCustomization(@Valid @RequestBody
                                                                 CustomizationRequestDTO customizationRequestDto) {
        return new SuccessResponse<>(SuccessCode.GET_REGION_CUSTOMIZATION,
                regionCustomizationService.getCustomization(customizationRequestDto), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to update a region customization using the given region customization details.
     * </p>
     *
     * @param regionCustomizationDto {@link RegionCustomizationDTO} The region customization that need to be
     *                               updated is given
     * @return {@link SuccessResponse<RegionCustomization>} Returns a success message and status with
     * the updated region customization
     */
    @UserTenantValidation
    @PutMapping("/update")
    public SuccessResponse<RegionCustomization> updateCustomization(@Valid @RequestBody
                                                                    RegionCustomizationDTO regionCustomizationDto) {
        regionCustomizationService
                .updateCustomization(modelMapper.map(regionCustomizationDto, RegionCustomization.class));
        return new SuccessResponse<>(SuccessCode.REGION_CUSTOMIZATION_UPDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get a list of region customizations based on a given culture ID.
     * </p>
     *
     * @param cultureId The culture id of the region customizations that need to be retrieved is given
     * @return {@link List} The list of RegionCustomizations for the given culture id is returned
     */
    @PostMapping("/static-data/get-list/{cultureId}")
    public List<RegionCustomization> getRegionCustomizations(@PathVariable(Constants.CULTURE_ID) long cultureId) {
        return regionCustomizationService.getRegionCustomizations(cultureId);
    }
}
