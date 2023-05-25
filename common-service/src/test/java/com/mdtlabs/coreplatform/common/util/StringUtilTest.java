package com.mdtlabs.coreplatform.common.util;

import java.util.Date;
import java.util.Map;

import static com.mdtlabs.coreplatform.common.util.StringUtil.constructString;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * <p>
 * StringUtilTest class used to test all possible positive
 * and negative cases for all methods and conditions used in StringUtil class.
 * </p>
 *
 * @author Jaganathan created on Apr 3, 2023
 */
@ExtendWith(MockitoExtension.class)
class StringUtilTest {

    @Test
    @DisplayName("ConstructString Test")
    void constructStringTest() {
        //given
        String value = "construct String args";

        //then
        String result = constructString("construct", "String", "args");
        Assertions.assertEquals(value, result);
    }

    @Test
    void getDateStringTest() {
        //then
        String response = StringUtil.getDateString(new Date());
        Assertions.assertNotNull(response);
    }

    @Test
    void getDateStringNullTest() {
        //then
        String response = StringUtil.getDateString(null);
        Assertions.assertEquals(null, response);
    }

    @Test
    void getDateStringExceptionTest() {
        //then
        String response = StringUtil.getDateString(new Date(), "dfttt");
        Assertions.assertEquals(null, response);
    }

    @Test
    void parseEmailTemplateNullTest() {
        //then
        String response = StringUtil.parseEmailTemplate(null, Map.of());
        Assertions.assertEquals(null, response);
    }

    @Test
    void parseEmailTemplateNullTestvalue() {
        //then
        String response = StringUtil.parseEmailTemplate(null, null);
        Assertions.assertEquals(null, response);
    }

    @Test
    void parseEmailTemplateTest() {
        //then
        String response = StringUtil.parseEmailTemplate("{{name}}", Map.of("name", "Test"));
        Assertions.assertEquals("Test", response);
    }
}
