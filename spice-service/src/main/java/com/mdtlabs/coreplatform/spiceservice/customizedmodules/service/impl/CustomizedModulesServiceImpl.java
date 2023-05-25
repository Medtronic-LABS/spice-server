package com.mdtlabs.coreplatform.spiceservice.customizedmodules.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.entity.spice.CustomizedModule;
import com.mdtlabs.coreplatform.spiceservice.customizedmodules.repository.CustomizedModuleRepository;
import com.mdtlabs.coreplatform.spiceservice.customizedmodules.service.CustomizedModulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class contains business logic for CustomizedModules entity.
 *
 * @author Niraimathi S
 * @since Jun 30, 2022
 */
@Service
public class CustomizedModulesServiceImpl implements CustomizedModulesService {

    @Autowired
    private CustomizedModuleRepository customizedModuleRepository;

    /**
     * {@inheritDoc}
     */
    public void createCustomizedModules(List<Map<String, Object>> modules, String type, Long patientTrackId) {
        if (!Objects.isNull(modules) && !modules.isEmpty()) {
            List<CustomizedModule> updatedModules = modules.stream().map(module -> {
                CustomizedModule customizedModule = new CustomizedModule();
                customizedModule.setModuleValue(module);
                customizedModule.setScreenType(type);
                customizedModule.setPatientTrackId(patientTrackId);
                customizedModule.setClinicalworkflowId((Long.parseLong(module.get(Constants.ID).toString())));
                return customizedModule;
            }).filter(obj -> Constants.BOOLEAN_TRUE).toList();
            Logger.logInfo("Saving the customized modules");
            customizedModuleRepository.saveAll(updatedModules);
        }
    }

    public void removeCustomizedModule(long trackerId) {
        List<CustomizedModule> customizedModules = customizedModuleRepository.findByPatientTrackId(trackerId);
        if (!Objects.isNull(customizedModules)) {
            customizedModules.forEach(customizedModule -> {
                customizedModule.setActive(false);
                customizedModule.setDeleted(true);
            });
            customizedModuleRepository.saveAll(customizedModules);
        }
    }
}
