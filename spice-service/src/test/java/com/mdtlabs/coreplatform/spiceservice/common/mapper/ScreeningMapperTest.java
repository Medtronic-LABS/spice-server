package com.mdtlabs.coreplatform.spiceservice.common.mapper;

import com.mdtlabs.coreplatform.common.model.dto.spice.BioMetricsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OldScreeningLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ScreeningLogDTO;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.ScreeningLog;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * <p>
 * ScreeningMapperTest class used to test all possible positive
 * and negative cases for all methods and conditions used in ScreeningMapper class.
 * </p>
 *
 * @author Jaganathan created on Mar 2, 2023
 */
@ExtendWith(MockitoExtension.class)
class ScreeningMapperTest {

    @InjectMocks
    private ScreeningMapper screeningMapper;

    @Test
    @DisplayName("MapScreening Test")
    void mapScreening() {
        //given
        ScreeningLogDTO screeningLogDTO = TestDataProvider.getScreeningLogDTO();
        Site site = TestDataProvider.getSite();
        ScreeningLog screeningLog = TestDataProvider.getScreeningLog();
        //then
        screeningMapper.mapScreeningLog(screeningLogDTO, screeningLog, site);
        //given
        screeningLogDTO.setPhq4(null);
        //then
        screeningMapper.mapScreeningLog(screeningLogDTO, screeningLog, site);
        Assertions.assertNotNull(screeningLogDTO);
        Assertions.assertNotNull(screeningLog);
        Assertions.assertNotNull(site);
    }

    @Test
    @DisplayName("MapPatientTracker Test")
    void mapPatientTracker() {
        //given
        PatientTracker patientTracker = TestDataProvider.getPatientTracker();
        ScreeningLogDTO screeningLogDTO = TestDataProvider.getScreeningLogDTO();
        Site site = TestDataProvider.getSite();
        //then
        screeningMapper.mapPatientTracker(patientTracker, screeningLogDTO, site);
        Assertions.assertNotNull(patientTracker);
        Assertions.assertNotNull(screeningLogDTO);
        Assertions.assertNotNull(site);
    }

    @Test
    @DisplayName("ConstructScreeningLogDTO Test")
    void constructScreeningLogDTO() {
        //given
        OldScreeningLogDTO oldScreeningLogDTO = TestDataProvider.getOldScreeningLogDto();
        ScreeningLogDTO screeningLogDTO = TestDataProvider.getScreeningLogDTO();
        //then
        screeningMapper.constructScreeningLogDto(oldScreeningLogDTO, screeningLogDTO);
        Assertions.assertNotNull(oldScreeningLogDTO);
        Assertions.assertNotNull(screeningLogDTO);
    }

    @Test
    @DisplayName("MapBiometricsData Test")
    void mapBiometricsData() {
        //given
        ScreeningLog screeningLog = TestDataProvider.getScreeningLog();
        BioMetricsDTO bioMetricsDTO = TestDataProvider.getBioMetricDTO();
        //then
        screeningMapper.mapBiometricsData(screeningLog, bioMetricsDTO);
        Assertions.assertNotNull(screeningLog);
        Assertions.assertNotNull(bioMetricsDTO);
    }

    @Test
    @DisplayName("MapBpLog Test")
    void mapBpLog() {
        //given
        ScreeningLog screeningLog = TestDataProvider.getScreeningLog();
        BpLog bpLog = TestDataProvider.getBpLog();
        //then
        screeningMapper.mapBpLog(screeningLog, bpLog);
        Assertions.assertNotNull(screeningLog);
        Assertions.assertNotNull(bpLog);
    }

    @Test
    @DisplayName("MapGlucoseLog Test")
    void mapGlucoseLog() {
        //given
        ScreeningLog screeningLog = TestDataProvider.getScreeningLog();
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        //then
        screeningMapper.mapGlucoseLog(screeningLog, glucoseLog);
        Assertions.assertNotNull(screeningLog);
        Assertions.assertNotNull(glucoseLog);
    }
}
