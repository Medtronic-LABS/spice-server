package com.mdtlabs.coreplatform.authserver.security;

import com.mdtlabs.coreplatform.authservice.repository.RoleRepository;
import com.mdtlabs.coreplatform.authservice.repository.UserRepository;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.CustomDateSerializer;
import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.TimezoneDTO;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.util.DateUtil;
import com.mdtlabs.coreplatform.common.util.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * The class "AuthenticationProvider" implements the Spring Security interface for authentication
 * providers.
 * </p>
 *
 * @author Vigneshkumar
 * @since 30 Jun 2022
 */
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    @Autowired
    CustomDateSerializer customDateSerializer;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Value("${app.login-time-limit-in-hour}")
    private int loginTimeLimitInHour;
    @Value("${app.login-count-limit}")
    private int loginCountLimit;

    /**
     * <p>
     * This is a Java function that authenticates a user's credentials and returns a token with their
     * authorities if authenticated.
     * </p>
     *
     * @param authentication The "authentication" parameter is an object of type Authentication is given
     * @return {@link Authentication} The method is returning a `UsernamePasswordAuthenticationToken` object is given
     */
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = String.valueOf(authentication.getPrincipal()).toLowerCase();
        String password = String.valueOf(authentication.getCredentials());
        User user = authenticationCheck(username, password);
        Set<Role> roles = user.getRoles();
        Map<String, Role> rolesAsMap = getRoleAsMap(roles);
        boolean isActiveRole = Boolean.FALSE;
        List<Role> rolesList = roleRepository.getAllRoles(Boolean.TRUE);
        for (Role role : rolesList) {
            if (null != rolesAsMap.get(role.getName())) {
                isActiveRole = Boolean.TRUE;
            }
        }
        if (!isActiveRole) {
            throw new BadCredentialsException(ErrorConstants.ERROR_INVALID_USER);
        }
        boolean isAuthenticated = Boolean.FALSE;
        if (password.equals(user.getPassword())) {
            isAuthenticated = Boolean.TRUE;
        }
        if (null != user.getTimezone()) {
            ModelMapper modelMapper = new ModelMapper();
            customDateSerializer.setUserZoneId(modelMapper.map(user.getTimezone(), TimezoneDTO.class).getOffset());
        }
        if (isAuthenticated) {
            Set<GrantedAuthority> authorityList = user.getAuthorities();
            user.setRoles(roles);
            return new UsernamePasswordAuthenticationToken(user, password, authorityList);
        }
        Logger.logError(StringUtil.constructString(ErrorConstants.INFO_USER_PASSWORD_NOT_MATCH, username));
        throw new BadCredentialsException(ErrorConstants.ERROR_INVALID_USER);
    }

    /**
     * <p>
     * This method is used to check whether the user account is valid.
     * </p>
     *
     * @param username username of the user is given
     * @param password password of the user is given
     * @return {@link User}  user entity is given
     */
    private User authenticationCheck(String username, String password) {
        if ((isBlank(username)) || (isBlank(password))) {
            throw new BadCredentialsException(ErrorConstants.ERROR_USERNAME_PASSWORD_BLANK);
        }
        User user = userRepository.getUserByUsername(username, Constants.BOOLEAN_TRUE);
        if (null == user) {
            Logger.logError(StringUtil.constructString(ErrorConstants.INFO_USER_NOT_EXIST, username));
            throw new BadCredentialsException(ErrorConstants.ERROR_INVALID_USER);
        }
        if (null == user.getPassword()) {
            Logger.logError(StringUtil.constructString(ErrorConstants.PASSWORD_NOT_EXIST, username));
            throw new BadCredentialsException(ErrorConstants.ERROR_INVALID_USER);
        }

        boolean isLoginWithinTimeLimit = isLoginWithinTimeLimit(user);
        if (isLoginWithinTimeLimit) {
            Logger.logError(StringUtil.constructString(ErrorConstants.ERROR_ACCOUNT_DISABLED));
            throw new BadCredentialsException(ErrorConstants.ERROR_ACCOUNT_DISABLED);
        } else {
            if (user.getPassword().equals(password)) {
                setUserValues(user, Constants.ZERO, Boolean.FALSE, null);
                userRepository.save(user);
            } else if (Boolean.TRUE.equals(user.getIsBlocked()) && (!user.getPassword().equals(password))) {
                setUserValues(user, Constants.ONE, Boolean.FALSE, null);
                userRepository.save(user);
            } else if (updateAccBlockStatus(user)) {
                Logger.logError(StringUtil.constructString(ErrorConstants.ERROR_INVALID_ATTEMPTS));
                throw new BadCredentialsException(ErrorConstants.ERROR_INVALID_ATTEMPTS);
            }
        }
        Logger.logInfo(StringUtil.constructString(Constants.INFO_USER_EXIST, String.valueOf(user.isEnabled())));
        return user;
    }

    /**
     * <p>
     * This method checks the user's login time limit.
     * </p>
     *
     * @param user user entity is given
     * @return {@link  boolean} true or false will be returned based on login limit exceed is given
     */
    public boolean isLoginWithinTimeLimit(User user) {
        boolean isLoginWithinTimeLimit = false;
        if (Boolean.TRUE.equals(user.getIsBlocked())) {
            int invalidLoginAttempts = user.getInvalidLoginAttempts();
            Logger.logInfo("invalidLoginAttempts== isLoginLimitExceed " + invalidLoginAttempts);
            Date invalidLoginTime = DateUtil.formatDate(user.getBlockedDate());
            Date currentDate = DateUtil.formatDate(new Date());
            long differenceInHours = 0;
            if (currentDate != null && invalidLoginTime != null) {
                differenceInHours = DateUtil.getDiffInHours(currentDate, invalidLoginTime);
            }
            if (differenceInHours <= loginTimeLimitInHour) {
                isLoginWithinTimeLimit = true;
            }
        }
        return isLoginWithinTimeLimit;
    }


    /**
     * <p>
     * This method is used to update the account block status of a user based on their number of invalid login
     * attempts.
     * </p>
     *
     * @param user The user object contains information about the user whose account block status needs
     *             to be updated is given
     * @return {@link  boolean} A boolean value indicating whether the user account is blocked or not is given
     */
    private boolean updateAccBlockStatus(User user) {
        int invalidLoginAttempts = user.getInvalidLoginAttempts();
        boolean isBlocked = Boolean.FALSE;
        if (invalidLoginAttempts >= loginCountLimit) {
            Logger.logError(StringUtil.constructString(ErrorConstants.ERROR_INVALID_ATTEMPTS));
            throw new BadCredentialsException(ErrorConstants.ERROR_INVALID_ATTEMPTS);
        }
        if (invalidLoginAttempts >= Constants.ZERO) {
            invalidLoginAttempts++;
            if (invalidLoginAttempts == loginCountLimit) {
                setUserValues(user, invalidLoginAttempts, Boolean.TRUE, DateUtil.formatDate(new Date()));
                isBlocked = Boolean.TRUE;
            } else {
                setUserValues(user, invalidLoginAttempts, Boolean.FALSE, null);
            }
            userRepository.save(user);
        }
        return isBlocked;

    }


    /**
     * <p>
     * This method is used to convert a set of roles into a map with role names as keys and roles as values.
     * </p>
     *
     * @param roles The "roles" parameter is a Set of objects of type "Role" is given
     * @return {@link Map<String, Role>} is given
     */
    private Map<String, Role> getRoleAsMap(Set<Role> roles) {
        Map<String, Role> roleAsMap = new HashMap<>();
        roles.forEach(role -> roleAsMap.put(role.getName(), role));
        return roleAsMap;
    }


    /**
     * <p>
     * This method is used to check if the provided authentication class is assignable from the
     * UsernamePasswordAuthenticationToken class and returns a boolean value accordingly.
     * </p>
     *
     * @param authentication The "authentication" parameter is a Class object that represents the type
     *                       of authentication object that is being checked for support is given
     * @return {@link  boolean} A boolean value indicating whether the given authentication class is supported or
     * not is given
     */
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }


    /**
     * <p>
     * This method is used to check if a given CharSequence is blank or contains only whitespace characters.
     * </p>
     *
     * @param {@link CharSequence} CharSequence is an interface in Java that represents a sequence of
     *               characters is given
     * @return {@link  boolean} A boolean value is being returned. It will be true if the input CharSequence is null or
     * empty or contains only whitespace characters, and false otherwise is given
     */
    private boolean isBlank(CharSequence charSequence) {
        int stringLength;
        if ((charSequence == null) || ((stringLength = charSequence.length()) == Constants.ZERO)) {
            return Boolean.TRUE;
        }
        for (int character = Constants.ZERO; character < stringLength; character++) {
            if (!Character.isWhitespace(charSequence.charAt(character))) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * <p>
     * This method is used to set the values of invalid login attempts, blocked status, and blocked date for a
     * user object and logs the blocked date.
     * </p>
     *
     * @param user                 {@link User} The user object represents a user in the system is given
     * @param invalidLoginAttempts The number of times a user has attempted to log in with invalid
     *                             credentials is given
     * @param isBlocked            A boolean value indicating whether the user is currently blocked or not is given
     * @param blockedDate          {@link Date} The blockedDate parameter is a Date object that represents the date and time
     *                             when the user was blocked is given
     */
    private void setUserValues(User user, int invalidLoginAttempts, boolean isBlocked, Date blockedDate) {
        user.setInvalidLoginAttempts(invalidLoginAttempts);
        user.setIsBlocked(isBlocked);
        user.setBlockedDate(blockedDate);
        Logger.logInfo("blocked date " + user.getBlockedDate());
    }
}
