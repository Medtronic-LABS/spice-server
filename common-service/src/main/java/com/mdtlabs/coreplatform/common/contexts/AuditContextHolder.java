package com.mdtlabs.coreplatform.common.contexts;

import com.mdtlabs.coreplatform.common.model.entity.Audit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The audit information created before create or update the entity. That created
 * information stored in this context. Once the operation will execute the audit
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
public class AuditContextHolder {

    private static final ThreadLocal<List<Audit>> AUDIT_CONTEXT = new ThreadLocal<>();
/**
 * <p>
 * This function sets a list of Audit objects in the AUDIT_CONTEXT.
 * </p>
 * 
 * @param audit {@link List<Audit>}The parameter "audit" is a List of objects of type "Audit".
 */

    public static void set(List<Audit> audit) {
        AUDIT_CONTEXT.set(audit);
    }

   /**
    * <p>
    * The function returns a list of Audit objects from the AUDIT_CONTEXT.
    * </p>
    * 
    * @return {@link List<Audit>} A List of Audit objects is being returned.
    */
    public static List<Audit> get() {
        return AUDIT_CONTEXT.get();
    }

    /**
     * <p>
     * The function clears the audit context.
     * </p>
     */
    public static void clear() {
        AUDIT_CONTEXT.remove();
    }
}
