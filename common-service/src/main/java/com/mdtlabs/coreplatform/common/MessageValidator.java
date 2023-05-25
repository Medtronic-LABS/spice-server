package com.mdtlabs.coreplatform.common;

import com.mdtlabs.coreplatform.common.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;

/**
 * <p>
 * Used to fetch the frequent changes messages like error / success messages
 * from the application.properties file.
 * </p>
 *
 * @author Vigneshkumar created on Jun 30, 2022
 */
public class MessageValidator {

    public static final String SUCCESS_PROPERTIES = "/success_messages.properties";
    public static final String ERROR_PROPERTIES = "/error_messages.properties";
    private static MessageValidator messageValidator;

    /**
     * <p>
     * Message validator singleton.
     * </p>
     * <p>
     * Creating singleton for message validator.
     * </p>
     *
     * @return returns the message validator.
     */
    public static synchronized MessageValidator getInstance() {
        if (messageValidator == null) {
            messageValidator = new MessageValidator();
        }
        return messageValidator;
    }

    /**
     * <p>
     * To get property file with respect to type
     * </p>
     *
     * @param type success or error type is passed
     * @return property file name
     */
    private String getPropertyFileByType(String type) {
        String propFileName = ERROR_PROPERTIES;
        if (type.equals(Constants.SUCCESS)) {
            propFileName = SUCCESS_PROPERTIES;
        }
        return propFileName;
    }

    /**
     * <p>
     * Get Message from SUCCESS_ERROR_PROPERTIES file based on the message key.
     * </p>
     *
     * @param messageKey message key is passed in this attribute.
     * @return returns the constructed message.
     */
    public String getMessage(String messageKey, String type) {
        final Properties configProp = new Properties();
        final String propFileName = getPropertyFileByType(type);
        try {
            InputStream in = getClass().getResourceAsStream(propFileName);
            configProp.load(in);

        } catch (IOException ioe) {
            Logger.logError(ioe.getMessage(), ioe);
        }
        return configProp.getProperty(messageKey);
    }

    /**
     * <p>
     * Get Message from SUCCESS_ERROR_PROPERTIES file based on the message key.
     * </p>
     *
     * @param messageKey code or message key is passed in this attribute.
     * @param arg        value to be appended with the chat message is passed in
     *                   this attribute.
     * @return returns the message constructed as response for chat.
     */
    public String getMessage(String messageKey, String type, String... arg) {
        final Properties configProp = new Properties();
        String arguments = null;
        try {
            InputStream in = getClass().getResourceAsStream(getPropertyFileByType(type));
            configProp.load(in);
            arguments = MessageFormat.format(configProp.getProperty(messageKey), arg);
        } catch (IOException ioe) {
            Logger.logError(ioe.getMessage(), ioe);
        }
        return arguments;
    }

    /**
     * <p>
     * Get Message from SUCCESS_ERROR_PROPERTIES file based on the message key.
     * </p>
     *
     * @param messageKey code or message key is passed in this attribute.
     * @param arg        value to be appended with the chat message is passed in
     *                   this attribute.
     * @return returns the message constructed as response for chat.
     */
    public String getMessage(String messageKey, String type, List<String> arg) {
        final Properties configProp = new Properties();
        String arguments = null;
        String[] stringArg = arg.toArray(new String[0]);
        try {
            InputStream in = getClass().getResourceAsStream(getPropertyFileByType(type));
            configProp.load(in);
            arguments = MessageFormat.format(configProp.getProperty(messageKey), stringArg);
        } catch (IOException ioe) {
            Logger.logError(ioe.getMessage(), ioe);
        }
        return arguments;
    }

    /**
     * <p>
     * Getting Error Message With the Arguments.
     * </p>
     *
     * @param messageKey message key is passed in this attribute.
     * @param arg        value to be appended with the error message is passed in
     *                   this attribute.
     * @return returns the constructed message.
     */
    public String getMessage(String messageKey, String type, String arg) {
        final Properties configProp = new Properties();
        String arguments = null;
        final String propFileName = getPropertyFileByType(type);
        try {
            InputStream in = getClass().getResourceAsStream(propFileName);
            configProp.load(in);
            arguments = MessageFormat.format(configProp.getProperty(messageKey), arg);
        } catch (IOException ioe) {
            Logger.logError(ioe.getMessage(), ioe);
        }
        return arguments;
    }

}
