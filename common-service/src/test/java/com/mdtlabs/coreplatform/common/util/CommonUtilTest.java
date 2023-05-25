package com.mdtlabs.coreplatform.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.TestConstants;
import com.mdtlabs.coreplatform.common.TestDataProvider;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;

/**
 * <p>
 * CommonUtilTest class used to test all possible positive
 * and negative cases for all methods and conditions used in CommonUtil
 * class.
 * </p
 *
 * @author Nandhakumar Karthikeyan created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CommonUtilTest {

    @Test
    void isNotNull() {
        //then
        boolean response = CommonUtil.isNotNull(null);
        Assertions.assertFalse(response);
    }

    @Test
    void verifyNotNull() {
        //then
        boolean response = CommonUtil.isNotNull(new Object());
        Assertions.assertTrue(response);
    }

    @Test
    void isNullString() {
        //then
        boolean response = CommonUtil.isNull("");
        Assertions.assertTrue(response);
    }

    @Test
    void isNullCollection() {
        //then
        boolean response = CommonUtil.isNull(new ArrayList<>());
        Assertions.assertTrue(response);
    }

    @Test
    void isNullMap() {
        //then
        boolean response = CommonUtil.isNull(new HashMap<>());
        Assertions.assertTrue(response);
    }

    @Test
    void isNullMapWithData() {
        //then
        boolean response = CommonUtil.isNull(Map.of(Constants.ONE, Constants.ONE));
        Assertions.assertFalse(response);
    }

    @Test
    void isNullObject() {
        //then
        boolean response = CommonUtil.isNull(new Object());
        Assertions.assertFalse(response);
    }

    @Test
    void isNull() {
        //then
        boolean response = CommonUtil.isNull(null);
        Assertions.assertTrue(response);
    }

    @Test
    void getMessage() {
        //given
        Throwable throwable = new Throwable();
        MockedStatic<ExceptionUtils> exceptionUtils = mockStatic(ExceptionUtils.class);

        //when
        exceptionUtils.when(() -> ExceptionUtils.getStackTrace(throwable)).thenReturn("stack trace");

        //then
        String response = CommonUtil.getMessage(throwable);
        exceptionUtils.close();
        assertNotNull(response);
        assertEquals("stack trace", response);
    }

    @Test
    void validatePatientSearchData() {
        //then
        boolean response = CommonUtil.validatePatientSearchData(List.of("123"));
        Assertions.assertFalse(response);
    }

    @Test
    void isGlucoseLogGiven() {
        //given
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        glucoseLog.setGlucoseValue(2.30);
        glucoseLog.setHba1c(3.30);

        //then
        boolean response = CommonUtil.isGlucoseLogGiven(glucoseLog);
        Assertions.assertTrue(response);
    }

    @Test
    void isGlucoseLogGivenNull() {
        //then
        boolean response = CommonUtil.isGlucoseLogGiven(null);
        Assertions.assertFalse(response);
    }

    @Test
    void isGlucoseLogGivenTest() {
        //given
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        glucoseLog.setGlucoseValue(null);
        glucoseLog.setHba1c(3.30);

        //then
        boolean response = CommonUtil.isGlucoseLogGiven(glucoseLog);
        Assertions.assertTrue(response);
    }

    @Test
    void isGlucoseLogGivenNullValueTest() {
        //given
        GlucoseLog glucoseLog = TestDataProvider.getGlucoseLog();
        glucoseLog.setGlucoseValue(null);
        glucoseLog.setHba1c(null);

        //then
        boolean response = CommonUtil.isGlucoseLogGiven(glucoseLog);
        Assertions.assertFalse(response);
    }

    @Test
    void getAuthToken() {
        //given
        UserDTO userDTO = TestDataProvider.getUserDTO();
        userDTO.setId(TestConstants.ONE);
        userDTO.setAuthorization(Constants.AUTHORIZATION);
        MockedStatic<UserContextHolder> userContextHolder = mockStatic(UserContextHolder.class);

        //when
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);

        //then
        String response = CommonUtil.getAuthToken();
        userContextHolder.close();
        assertNotNull(response);
        assertEquals(Constants.BEARER + Constants.AUTHORIZATION, response);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {Constants.TOKEN})
    void isValidSearchData(String searchTerm) {
        //then
        boolean response = CommonUtil.isValidSearchData(searchTerm, Constants.REGEX_SEARCH_PATTERN);
        assertTrue(response);
    }

    @Test
    void verifyValidSearchData() {
        //then
        boolean response = CommonUtil.isValidSearchData("*&(_", Constants.REGEX_SEARCH_PATTERN);
        assertFalse(response);
    }
}
