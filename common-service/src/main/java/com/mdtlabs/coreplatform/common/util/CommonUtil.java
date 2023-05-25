package com.mdtlabs.coreplatform.common.util;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Common utils for date diff, data validation etc.
 * </p>
 *
 * @author Vigneshkumar created on Jun 30, 2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtil {

    /**
     * <p>
     * Used to check null condition
     * </p>
     *
     * @param pObj object to be checked
     * @return boolean  true or false on null check
     */
    public static boolean isNull(Object... pObj) {
        boolean isNull = false;
        if (pObj == null) {
            isNull = true;
        } else {
            for (Object lObj : pObj) {
                if (lObj == null) {
                    isNull = true;
                } else if (lObj instanceof String string) {
                    isNull = string.trim().equals(Constants.EMPTY);
                } else if (lObj instanceof Collection<?> collection) {
                    isNull = collection.isEmpty();
                } else if (lObj instanceof Map<?, ?> map) {
                    isNull = map.size() == Constants.ZERO;
                }
                if (Boolean.TRUE.equals(isNull)) {
                    break;
                }
            }
        }
        return isNull;
    }

    /**
     * <p>
     * Used to check not null condition
     * </p>
     *
     * @param pObj object to be checked
     * @return boolean  true or false on null check
     */
    public static boolean isNotNull(Object... pObj) {
        return !isNull(pObj);
    }

    /**
     * <p>
     * This method is used to get message
     * </p>
     *
     * @param throwable throwable object
     * @return String  message trace
     */
    public static String getMessage(Throwable throwable) {
        return ExceptionUtils.getStackTrace(throwable);
    }

    /**
     * <p>
     * Get the logged in employee from the spring {@code SecurityContextHolder} and
     * construct the string with format '[employeeId  employeeCompanyId]'.
     * </p>
     *
     * @return String  Logged in employee as string with format '[employeeId
     * employeeCompanyId]'.
     */
    public static String getLoggedInEmployeeLog() {
        UserDTO user = getLoggedInUser();
        String userId = String.valueOf(user.getId());
        String username = user.getUsername();

        return StringUtil.constructString(Constants.OPEN_BRACKET, userId, Constants.HYPHEN, username,
                Constants.CLOSE_BRACKET);
    }

    /**
     * <p>
     * Get the logged in employee from the spring
     * </p>
     *
     * @return UserDTO  Logged in employee object.
     */
    public static UserDTO getLoggedInUser() {
        return new UserDTO();
    }

    /**
     * <p>
     * Validate the given string has only letters and numbers
     * </p>
     *
     * @param values listValues
     * @return Boolean
     */
    public static boolean validatePatientSearchData(List<String> values) {
        return values.stream()
                .allMatch(value -> (!Objects.isNull(value) && !value.isEmpty() && !Pattern.matches(Constants.PATIENT_SEARCH_REGEX, value)));
    }

    /**
     * <p>
     * To get the user auth token
     * </p>
     *
     * @return String
     */
    public static String getAuthToken() {
        return Constants.BEARER + UserContextHolder.getUserDto().getAuthorization();
    }


    /**
     * <p>
     * Validate the given string with the given regex
     * </p>
     *
     * @param searchTerm  searchTerm
     * @param searchRegex regEx
     * @return Boolean
     */
    public static boolean isValidSearchData(String searchTerm, String searchRegex) {
        boolean isMatches = true;
        if (StringUtils.isNotBlank(searchTerm)) {
            Pattern pattern = Pattern.compile(searchRegex);
            Matcher matcher = pattern.matcher(searchTerm);
            isMatches = !matcher.matches();
        }
        return isMatches;
    }

    /**
     * <p>
     * The function checks if a glucose log object is not null and has either a glucose value or an
     * HbA1c value.
     * </p>
     *
     * @param glucoseLog It is an object of the class GlucoseLog, which contains information about a
     *                   glucose log entry, such as the glucose value and HbA1c level.
     * @return The method `isGlucoseLogGiven` is returning a boolean value
     */
    public static boolean isGlucoseLogGiven(GlucoseLog glucoseLog) {
        return !Objects.isNull(glucoseLog)
                && (!Objects.isNull(glucoseLog.getGlucoseValue())
                || !Objects.isNull(glucoseLog.getHba1c()));
    }
}
