package com.mdtlabs.coreplatform.spiceservice.customizedmodules.service;

import java.util.List;
import java.util.Map;

/**
 * This interface is responsible for performing actions in CustomizedModules Entity.
 *
 * @author Niraimathi S
 * @since Jun 30, 2022
 */
public interface CustomizedModulesService {

    /**
     * <p>
     * Creates customized modules.
     * </p>
     *
     * @param modules        {@link List<Map<String, Object>>} customized modules with dynamic fields and its values.
     * @param type           {@link String} type of workflow like Screening, Enrollment or Assessment
     * @param patientTrackId {@link Long} patientTrackId
     */
    void createCustomizedModules(List<Map<String, Object>> modules, String type, Long patientTrackId);

    /**
     * <p>
     * The function removes a customized module identified by a tracker ID.
     * </p>
     *
     * @param trackerId {@link long} The parameter `trackerId` is a `long` data type that represents the unique
     *                  identifier of a customized module that needs to be removed.
     */
    void removeCustomizedModule(long trackerId);
}
