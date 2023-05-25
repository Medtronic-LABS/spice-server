package com.mdtlabs.coreplatform.spiceadminservice.regioncustomization.service;

import java.util.List;

import com.mdtlabs.coreplatform.common.model.dto.spice.CustomizationRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.RegionCustomization;

/**
 * <p>
 * RegionCustomizationService is a Java interface for a service class that maintains
 * CRUD (Create, Read, Update, Delete) operations for region customization.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 30, 2022
 */
public interface RegionCustomizationService {

    /**
     * <p>
     * This method is used to create a region customization using the given region customization details.
     * </p>
     *
     * @param regionCustomization {@link RegionCustomization} The region customization that need to be
     *                            created is given
     * @return {@link RegionCustomization} The created region customization for given region customization
     * details is returned
     */
    RegionCustomization createCustomization(RegionCustomization regionCustomization);

    /**
     * <p>
     * This method is used to get the region customization data details such as screening, enrollment and
     * consent forms based on region organization id.
     * </p>
     *
     * @param customizationRequestDto {@link CustomizationRequestDTO} The customization request contains
     *                                necessary information to get the region
     *                                customization is given
     * @return {@link RegionCustomization} Returns the retrieved region customization for the given customization request
     */
    RegionCustomization getCustomization(CustomizationRequestDTO customizationRequestDto);

    /**
     * <p>
     * This method is used to update a region customization using the given region customization details.
     * </p>
     *
     * @param regionCustomization {@link RegionCustomization} The region customization that need to be
     *                            updated is given
     * @return {@link RegionCustomization} The updated region customization for given region customization
     * details is returned
     */
    RegionCustomization updateCustomization(RegionCustomization regionCustomization);

    /**
     * <p>
     * This method is used to get a list of region customizations based on a given culture ID.
     * </p>
     *
     * @param cultureId The culture id of the region customizations that need to be retrieved is given
     * @return {@link List} The list of RegionCustomizations for the given culture id is returned
     */
    List<RegionCustomization> getRegionCustomizations(long cultureId);
}
