package com.mdtlabs.coreplatform.common.util.spice;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.util.ConversionUtil;

/**
 * <p>
 * This is used to set the bp log details.
 * </p>
 *
 * @author Prabu Created on 07 Feb 2023
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpiceUtil {

    /**
     * <p>
     * Converts bp log values with respect to the unit given.
     * </p>
     *
     * @param bpLog {@link BpLog} entity
     * @param unit   unit
     * @return {@link BpLog} entity
     */
    public static BpLog convertBpLogUnits(BpLog bpLog, String unit) {
        Logger.logInfo("In BpLogServiceImpl, convert bp log units");

        bpLog.setHeight(ConversionUtil.convertHeight(bpLog.getHeight(), unit));
        bpLog.setWeight(ConversionUtil.convertWeight(bpLog.getWeight(), unit));
        bpLog.setTemperature(ConversionUtil.convertTemperature(bpLog.getTemperature(), unit));
        return bpLog;
    }
}
