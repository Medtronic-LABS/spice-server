package com.mdtlabs.coreplatform.common.util.spice;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mdtlabs.coreplatform.common.TestDataProvider;
import com.mdtlabs.coreplatform.common.UnitConstants;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.util.ConversionUtil;

/**
 * <p>
 * SpiceUtilTest class used to test all possible positive
 * and negative cases for all methods and conditions used in SpiceUtil class.
 * </p>
 *
 * @author Jaganathan created on Apr 3, 2023
 */
@ExtendWith(MockitoExtension.class)
class SpiceUtilTest {

    @Test
    @DisplayName("ConvertBpLogUnits Test")
    void convertBpLogUnitsTest() {
        //given
        String unit = UnitConstants.METRIC;
        BpLog bpLog = TestDataProvider.getBpLog();
        MockedStatic<ConversionUtil> conversionUtil = mockStatic(ConversionUtil.class);

        //when
        conversionUtil.when(() -> ConversionUtil.convertHeight(bpLog.getHeight(), unit)).thenReturn(bpLog.getHeight());
        conversionUtil.when(() -> ConversionUtil.convertWeight(bpLog.getWeight(), unit)).thenReturn(bpLog.getWeight());
        conversionUtil.when(() -> ConversionUtil.convertTemperature(bpLog.getTemperature(),
                unit)).thenReturn(bpLog.getTemperature());
        //then
        BpLog actualBpLog = SpiceUtil.convertBpLogUnits(bpLog, unit);
        conversionUtil.close();
        Assertions.assertEquals(bpLog, actualBpLog);
        Assertions.assertEquals(bpLog.getTemperature(), actualBpLog.getTemperature());
        Assertions.assertEquals(bpLog.getHeight(), actualBpLog.getHeight());
        Assertions.assertEquals(bpLog.getWeight(), actualBpLog.getWeight());

        //given
        bpLog.setWeight(null);
        bpLog.setHeight(null);
        bpLog.setTemperature(null);

        //then
        BpLog actualBpLogNull = SpiceUtil.convertBpLogUnits(bpLog, unit);
        Assertions.assertEquals(bpLog, actualBpLog);
        Assertions.assertEquals(bpLog.getTemperature(), actualBpLogNull.getTemperature());
        Assertions.assertEquals(bpLog.getHeight(), actualBpLogNull.getHeight());
        Assertions.assertEquals(bpLog.getWeight(), actualBpLogNull.getWeight());
    }
}
