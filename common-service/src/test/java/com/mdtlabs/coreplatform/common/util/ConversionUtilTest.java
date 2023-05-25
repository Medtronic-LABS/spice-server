package com.mdtlabs.coreplatform.common.util;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.mdtlabs.coreplatform.common.Constants;

/**
 * <p>
 * ConversionUtilTest class used to test all possible positive and negative cases for
 * all methods and conditions used in ConversionUtil class.
 * </p>
 *
 * @author Jaganathan created on Apr 3, 2023
 */
class ConversionUtilTest {

    @ParameterizedTest
    @CsvSource({"10,imperial,25.4", "10,metric,3.94", ",imperial,"})
    void convertHeightTest(Double value, String unit, Double expectedValue) {
        //then
        Double actualValue = ConversionUtil.convertHeight(value, unit);
        assertEquals(expectedValue, actualValue);
    }

    @ParameterizedTest
    @CsvSource({"10,imperial,-12.22", "10,metric,50.0", ",imperial,"})
    void convertTemperature(Double value, String unit, Double expectedValue) {
        //then
        Double actualValue = ConversionUtil.convertTemperature(value, unit);
        assertEquals(expectedValue, actualValue);
    }

    @ParameterizedTest
    @CsvSource({"10,imperial,4.54", "10,metric,22.05", ",imperial,"})
    void convertWeight(Double value, String unit, Double expectedValue) {
        //then
        Double actualValue = ConversionUtil.convertWeight(value, unit);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void convertMgDlToMmol() {
        //then
        Double actualValue = ConversionUtil.convertMgDlToMmol(10D);
        assertNotNull(actualValue);
        assertEquals(0.56, actualValue);
    }

    @Test
    void calculatePatientAge() {
        //given
        LocalDate localDate = LocalDate.of(2023, 3, 8);
        Date date = java.sql.Date.valueOf(localDate);

        //then
        int actualValue = ConversionUtil.calculatePatientAge(22, date);
        assertNotEquals(Constants.ZERO, actualValue);
    }
}