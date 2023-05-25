package com.mdtlabs.coreplatform.common.domain;

import java.util.List;

/**
 * <p>
 * Paged Implementation
 * </p>
 *
 * @author Vigneshkumar created on Jun 30, 2022
 */
public class PagedImpl<T> implements Paged<T> {

    /**
     * <p>
     * List of T
     * </p>
     */
    private List<T> list;

    /**
     * <p>
     * Object used Generically
     * </p>
     */
    private T object;

    /**
     * <p>
     * Total count
     * </p>
     */
    private long count;

    /**
     * <p>
     * It is used to create an instance of the `PagedImpl`
     * class without initializing any of its fields.
     * </p>
     */
    public PagedImpl() {
    }

    /**
     * <p>
     * It sets the `list` field of the `PagedImpl` object to the `List`
     * parameter and the `count` field to the `long` parameter.
     * </p>
     *
     * @param list  {@link List<T>} list param
     * @param count count param
     */
    public PagedImpl(List<T> list, long count) {
        this.list = list;
        this.count = count;
    }

    /**
     * <p>
     * Java constructor for PagedImpl class that sets object field to parameter value, count field to count parameter.
     * </p>
     *
     * @param object the object parameter is given
     * @param count  the count parameter is given
     */
    public PagedImpl(T object, long count) {
        this.object = object;
        this.count = count;
    }

    /**
     * <p>
     * The function returns a list of objects of type T.
     * </p>
     *
     * @return A List of objects of type T is being returned.
     */
    public List<T> getList() {
        return list;
    }

    /**
     * <p>
     * This function sets the value of a list variable in a Java class.
     * </p>
     *
     * @param list The parameter "list" is a List object that contains elements of type T. The method
     *             "setList" sets the value of the instance variable "list" to the value of the parameter "list".
     */
    public void setList(List<T> list) {
        this.list = list;
    }

    /**
     * <p>
     * The function returns the value of a long variable called "count".
     * </p>
     *
     * @return The method `getCount()` is returning a `long` value, which is the value of the variable
     * `count`.
     */
    public long getCount() {
        return count;
    }

    /**
     * <p>
     * The function sets the value of a variable called "count".
     * </p>
     *
     * @param count The parameter "count" is a long data type that represents the value that will be set
     *              to the instance variable "count" of the current object
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * <p>
     * Java code that defines a public method named getObject(), which returns the object field of the PagedImpl class.
     * This method is used when the Paged interface is implemented with a single object instead of a list.
     * </p>
     */
    @Override
    public T getObject() {
        return this.object;
    }

}
