package com.mdtlabs.coreplatform.common.util;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.mdtlabs.coreplatform.common.Constants;

/**
 * <p>
 * DateUtilTest class used to test all possible positive
 * and negative cases for all methods and conditions used in DateUtil
 * class.
 * </p>
 *
 * @author Nandhakumar Karthikeyan created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DateUtilTest {

    @Test
    void daysSincePastTest() {
        long response = DateUtil.daysSincePast(new Date());
        Assertions.assertEquals(0L, response);
    }

    @Test
    void daysSincePastNullTest() {
        long response = DateUtil.daysSincePast(null);
        Assertions.assertEquals(0L, response);
    }

    @Test
    void yearsSincePastTest() {
        int response = DateUtil.yearsSincePast(new Date());
        Assertions.assertEquals(Constants.ZERO, response);
    }

    @Test
    void yearsSincePastNullTest() {
        int response = DateUtil.yearsSincePast("12/12/2002");
        Assertions.assertTrue(response > 0);
    }

    @Test
    void yearsSincePastBlankTest() {
        int response = DateUtil.yearsSincePast("");
        Assertions.assertEquals(0, response);
    }

    @Test
    void getEndOfDay() {
        String response = DateUtil.getEndOfDay();
        Assertions.assertNotNull(response);
    }

    @Test
    void getStartOfDay() {
        String response = DateUtil.getStartOfDay();
        Assertions.assertNotNull(response);
    }

    @Test
    void getStartOfDayTest() {
        String response = DateUtil.getStartOfDay();
        Assertions.assertNotNull(response);
    }

    @Test
    void formatDateTest() {
        Date dateResponse = DateUtil.formatDate("12-12-2020", Constants.JSON_DATE_FORMAT);
        Assertions.assertNull(dateResponse);
    }

    @Test
    void formatDateNullTest() {
        Date dateResponse = DateUtil.formatDate("12-12-2020", null);
        Assertions.assertNull(dateResponse);
    }

    @Test
    void formatDateTestNewDate() {
        Date dateResponse = DateUtil.formatDate(new Date());
        Assertions.assertNotNull(dateResponse);
        Assertions.assertEquals(Date.class, dateResponse.getClass());
    }

    @Test
    void getTreatmentPlanFollowupDate() {
        Date dateResponse = DateUtil.getTreatmentPlanFollowupDate(Constants.WEEK, 2);
        Assertions.assertNotNull(dateResponse);
        Assertions.assertEquals(Date.class, dateResponse.getClass());
    }

    @Test
    void getTreatmentPlanFollowupDay() {
        Date dateResponse = DateUtil.getTreatmentPlanFollowupDate(Constants.DAY, 2);
        Assertions.assertNotNull(dateResponse);
        Assertions.assertEquals(Date.class, dateResponse.getClass());
    }

    @Test
    void getTreatmentPlanFollowupDayEmpty() {
        Date dateResponse = DateUtil.getTreatmentPlanFollowupDate("", 2);
        Assertions.assertNotNull(dateResponse);
        Assertions.assertEquals(Date.class, dateResponse.getClass());
    }

    @Test
    void getDateDiffInMinutesNullTest() {
        long response = DateUtil.getDateDiffInMinutes(null, null);
        Assertions.assertEquals(0L, response);
    }

    @Test
    void getDateDiffInMinutesNullTests() {
        long response = DateUtil.getDateDiffInMinutes(new Date(), null);
        Assertions.assertEquals(0L, response);
    }

    @Test
    void getDateDiffInMinutesTest() {
        long response = DateUtil.getDateDiffInMinutes(new Date(), new Date());
        Assertions.assertEquals(0L, response);
    }

    @Test
    void addDateTest() {
        Date dateResponse = DateUtil.addDate(new Date(), 2);
        Assertions.assertNotNull(dateResponse);
    }

    @Test
    void subtractDates() {
        Date dateResponse = DateUtil.subtractDates(new Date(), 2);
        Assertions.assertNotNull(dateResponse);
    }

    @Test
    void formatDateString() {
        Date formatDate = DateUtil.formatDate(null);
        Assertions.assertNull(formatDate);
    }

    @Test
    void getCurrentDay() {
        Date response = DateUtil.getCurrentDay();
        Assertions.assertNotNull(response);
    }

    @Test
    void getDiffInHours() {
        Long response = DateUtil.getDiffInHours(new Date(), new Date());
        Assertions.assertNotNull(response);
    }
}
