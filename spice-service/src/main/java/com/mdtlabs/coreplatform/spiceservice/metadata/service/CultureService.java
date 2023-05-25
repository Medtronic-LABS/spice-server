package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.CultureDTO;
import com.mdtlabs.coreplatform.common.model.entity.Culture;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;

/**
 * <p>
 * This an interface class for culture module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface CultureService extends GenericService<Culture> {

    /**
     * <p>
     * To get all active cultures.
     * </p>
     *
     * @return {@link List<CultureDTO>} List of culture entities.
     */
    List<CultureDTO> getAllCultures();

    /**
     * <p>
     * To get culture by name.
     * </p>
     *
     * @param name {@link String} name
     * @return Culture {@link Culture} entity.
     */
    Culture getCultureByName(String name);

    /**
     * <p>
     * The function "getCultureValues" is declared in Java, but its purpose is not specified.
     * </p>
     */
    void getCultureValues();

    /**
     * <p>
     * The function returns a Long value representing the loaded culture values.
     * </p>
     *
     * @return {@link Long} A long integer value is being returned. The specific value being returned depends on the
     * implementation of the `loadCultureValues()` function.
     */
    Long loadCultureValues();

}
