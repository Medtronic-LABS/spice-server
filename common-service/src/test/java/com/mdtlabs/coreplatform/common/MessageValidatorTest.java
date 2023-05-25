package com.mdtlabs.coreplatform.common;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * <p>
 * MessageValidatorTest tests various methods for getting
 * error messages with different parameters.
 * </p>
 *
 * @author Divya S created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
class MessageValidatorTest {

    @InjectMocks
    private MessageValidator messageValidator;

    @ParameterizedTest
    @CsvSource({"ERROR, Invalid token.", "SUCCESS, User successfully created."})
    void getMessage(String filename, String expectedMessage) {
        //then
        String actualMessage = messageValidator.getMessage(TestConstants.STATUS_CODE, filename);
        assertNotNull(actualMessage);
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testGetMessageWithArgs() {
        //then
        TestDataProvider.init();
        TestDataProvider.getMessageValidatorMock();
        String actualMessage = messageValidator.getMessage(TestConstants.STATUS_CODE,
                Constants.ERROR, TestConstants.ARGUMENT, TestConstants.MESSAGE);
        TestDataProvider.cleanUp();
        assertNotNull(actualMessage);
        assertEquals(TestConstants.MESSAGE, actualMessage);
    }

    @Test
    void testGetMessage() {
        //then
        TestDataProvider.init();
        TestDataProvider.getMessageValidatorMock();
        String actualMessage = messageValidator.getMessage(TestConstants.STATUS_CODE,
                Constants.ERROR, List.of(TestConstants.ARGUMENT, TestConstants.MESSAGE));
        TestDataProvider.cleanUp();
        assertNotNull(actualMessage);
        assertEquals(TestConstants.MESSAGE, actualMessage);
    }

    @Test
    void testGetMessageWithArg() {
        //then
        TestDataProvider.init();
        TestDataProvider.getMessageValidatorMock();
        String actualMessage = messageValidator.getMessage(TestConstants.STATUS_CODE, Constants.ERROR,
                TestConstants.ARGUMENT);
        TestDataProvider.cleanUp();
        assertNotNull(actualMessage);
        assertEquals(TestConstants.MESSAGE, actualMessage);
    }
}