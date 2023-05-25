package com.mdtlabs.coreplatform.common.util;

import com.mdtlabs.coreplatform.common.UnitConstants;

import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConversionUtil {

    /**
     * <p>
     * Convert Imperial unit height(in) to Metric unit height(cm) and vice versa
     * </p>
     *
     * @param value       Imperial height value
     * @param convertUnit Height unit name
     * @return value metric height or value imperial height
     */
    public static Double convertHeight(Double value, String convertUnit) {
        Double height = null;
        if (!Objects.isNull(value)) {
            if (convertUnit.equals(UnitConstants.IMPERIAL)) {
                height = (value * UnitConstants.DEFAULT_HEIGHT_UNIT_VALUE);
            } else {
                height = (value / UnitConstants.DEFAULT_HEIGHT_UNIT_VALUE);
            }
            height = Double.parseDouble(String.format("%.2f", height));
        }
        return height;
    }

    /**
     * <p>
     * Convert Imperial unit temperature(F) to Metric unit temperature(C) and vice
     * versa
     * </p>
     *
     * @param value       Imperial temperature value
     * @param convertUnit Temperature unit name
     * @return value metric temperature or imperial temperature
     */
    public static Double convertTemperature(Double value, String convertUnit) {
        Double temperature = null;
        if (!Objects.isNull(value)) {
            if (convertUnit.equals(UnitConstants.IMPERIAL)) {
                temperature = (((value - 32) * 5) / 9);
            } else {
                temperature = ((value * 9) / 5 + 32);
            }
            temperature = Double.parseDouble(String.format("%.2f", temperature));
        }
        return temperature;
    }

    /**
     * <p>
     * Convert Imperial unit weight(pounds) to Metric unit weight(kg) and vice versa
     * </p>
     *
     * @param value       Weight value
     * @param convertUnit Weight unit name
     * @return {@link Double} value metric weight or imperial weight
     * @author Shrikanth
     */
    public static Double convertWeight(Double value, String convertUnit) {
        Double weight = null;
        if (!Objects.isNull(value)) {
            if (convertUnit.equals(UnitConstants.IMPERIAL)) {
                weight = (value / UnitConstants.DEFAULT_WEIGHT_UNIT_VALUE);
            } else {
                weight = (value * UnitConstants.DEFAULT_WEIGHT_UNIT_VALUE);
            }
            weight = Double.parseDouble(String.format("%.2f", weight));
        }
        return weight;
    }

    public static double convertMgDlToMmol(double value) {
        return Double.parseDouble(String.format("%.2f", (value / 18)));
    }

    /**
     * <p>
     * Calculates patient Age.
     * </p>
     *
     * @param age  Patient's old age
     * @param date patient enrolled/created date
     * @return patient's new age.
     * @author Niraimathi S
     */
    public static int calculatePatientAge(int age, Date date) {
        return age + DateUtil.getCalendarDiff(date, new Date(), Calendar.YEAR);
    }

    /**
     * Converts bp log values with respect to the unit given.
     *
     * @param bpLog - entity
     * @param unit  - unit
     * @return BpLog - entity
     */
    public static void convertBpLogUnits(BpLog bpLog, String unit) {
        bpLog.setHeight(convertHeight(bpLog.getHeight(), unit));
        bpLog.setWeight(convertWeight(bpLog.getWeight(), unit));
        bpLog.setTemperature(convertTemperature(bpLog.getTemperature(), unit));
    }
}
