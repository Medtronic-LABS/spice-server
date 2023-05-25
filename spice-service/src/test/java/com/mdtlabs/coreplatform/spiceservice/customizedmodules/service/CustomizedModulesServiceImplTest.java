package com.mdtlabs.coreplatform.spiceservice.customizedmodules.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.CustomizedModule;
import com.mdtlabs.coreplatform.spiceservice.customizedmodules.repository.CustomizedModuleRepository;
import com.mdtlabs.coreplatform.spiceservice.customizedmodules.service.impl.CustomizedModulesServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * <p>
 * CustomizedModulesServiceTest class used to test all possible positive
 * and negative cases for all methods and conditions used in CustomizedModulesService class.
 * </p>
 *
 * @author Jaganathan created on Mar 3, 2023
 */
@ExtendWith(MockitoExtension.class)
class CustomizedModulesServiceImplTest {

    @InjectMocks
    private CustomizedModulesServiceImpl customizedModulesService;

    @Mock
    private CustomizedModuleRepository customizedModuleRepository;

    @Test
    @DisplayName("CreateCustomizedModules Test")
    void createCustomizedModules() {
        //given
        Long patientTrackId = 1L;
        String type = Constants.TYPE;
        Map<String, Object> module = Map.of(Constants.ID, 1L);
        List<Map<String, Object>> modules = List.of(module);
        List<CustomizedModule> customizedModules = List.of(TestDataProvider.getCustomizedModule());
        customizedModules.get(0).setModuleValue(module);
        //when
        when(customizedModuleRepository.saveAll(customizedModules)).thenReturn(customizedModules);
        //then
        customizedModulesService.createCustomizedModules(modules, type, patientTrackId);
        verify(customizedModuleRepository, atLeastOnce()).saveAll(customizedModules);

    }

    @Test
    @DisplayName("RemoveCustomizedModule Test")
    void removeCustomizedModule() {

        List<CustomizedModule> customizedModules = List.of(TestDataProvider.getCustomizedModule());

        //when
        when(customizedModuleRepository.findByPatientTrackId(1L)).thenReturn(customizedModules);

        customizedModulesService.removeCustomizedModule(1L);
        assertTrue(customizedModules.get(0).isDeleted());
    }

}
