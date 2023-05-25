package com.mdtlabs.coreplatform.common.contexts;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The end user tenant id are stored in this context.
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
public class UserTenantsContextHolder {

    private static final ThreadLocal<List<Long>> USER_TENANTS_CONTEXT = new ThreadLocal<>();

   /**
    * <p>
    * This function sets a list of tenant IDs in a user tenants context.
    * </p>
    * 
    * @param tenantIds The parameter `tenantIds` is a List of Long values that represents the IDs of
    * tenants is given
    */
    public static void set(List<Long> tenantIds) {
        USER_TENANTS_CONTEXT.set(tenantIds);
    }

    /**
     * <p>
     * The function returns a list of Long values stored in a thread-local variable.
     * </p>
     * 
     * @return A List of Long values is being returned. The method `get()` retrieves the value of the
     * `USER_TENANTS_CONTEXT` thread-local variable, which is expected to be a List of Long values.
     */
    public static List<Long> get() {
        return USER_TENANTS_CONTEXT.get();
    }

    /**
     * <p>
     * The function clears the user tenants context.
     * </p>
     */
    public static void clear() {
        USER_TENANTS_CONTEXT.remove();
    }
}
