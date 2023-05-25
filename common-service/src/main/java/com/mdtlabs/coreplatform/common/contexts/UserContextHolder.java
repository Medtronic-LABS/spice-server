package com.mdtlabs.coreplatform.common.contexts;

import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * When the end user submits any request after successful login, the jwt
 * token is required to determine which user currently logged in. This used to
 * <p>
 * <br/>
 * <br/>
 * <b>Explanation:</b> Thread Local can be considered as a scope of access, like
 * a request scope or session scope. It’s a thread scope. You can set any object
 * in Thread Local and this object will be global and local to the specific
 * thread which is accessing this object. Global and local at the same time?
 *
 * <ul>
 * <li>Values stored in Thread Local are global to the thread, meaning that they
 * can be accessed from anywhere inside that thread. If a thread calls methods
 * from several classes, then all the methods can see the Thread Local variable
 * set by other methods (because they are executing in same thread). The value
 * need not be passed explicitly. It’s like how you use global variables.</li>
 * <li>Values stored in Thread Local are local to the thread, meaning that each
 * thread will have it’s own Thread Local variable. One thread can not
 * access/modify other thread’s Thread Local variables.</li>
 * </ul>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserContextHolder {

    private static final ThreadLocal<UserDTO> USER_CONTEXT = new ThreadLocal<>();

    /**
     * <p>
     * The function returns the UserDTO object stored in the USER_CONTEXT thread-local variable.
     * </p>
     *
     * @return A {@link UserDTO} object is being returned.
     */
    public static UserDTO getUserDto() {
        return USER_CONTEXT.get();
    }

    /**
     * <p>
     * This function sets the UserDTO object in the USER_CONTEXT thread-local variable.
     * </p>
     *
     * @param userDto {@link UserDTO} userDto is an object of type UserDTO which contains information
     *                about a user is given.
     */
    public static void setUserDto(UserDTO userDto) {
        USER_CONTEXT.set(userDto);
    }

    /**
     * <p>
     * The "clear" function removes the user context.
     * </p>
     */
    public static void clear() {
        USER_CONTEXT.remove();
    }
}
