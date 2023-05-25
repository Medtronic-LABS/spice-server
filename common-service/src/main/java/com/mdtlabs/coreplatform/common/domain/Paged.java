package com.mdtlabs.coreplatform.common.domain;

import java.util.List;

/**
 * <p>
 * Paged Interface
 * </p>
 *
 * @author Vigneshkumar created on Oct 16, 2020
 */
public interface Paged<T> {

    /**
     * <p>
     * Get list of generic object T.
     * </p>
     *
     * @return {@link List<T>} - list of entity
     */
    List<T> getList();

    /**
     * <p>
     * Total count
     * </p>
     *
     * @return - count as a long type
     */
    long getCount();

    /**
     * <p>
     * gets Generic object T
     * </p>
     *
     * @return Object T - entity object
     */
    T getObject();

}
