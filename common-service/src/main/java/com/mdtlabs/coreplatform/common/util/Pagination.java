package com.mdtlabs.coreplatform.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.List;

/**
 * <p>
 * Common class for pagination
 * </p>
 *
 * @author Jeyaharini T A
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Pagination {

    /**
     * <p>
     * This Java function sets pagination and sorting options for a pageable object.
     * </p>
     *
     * @param skip            The number of items to skip before starting to return results.
     * @param limit           The maximum number of items to be returned per page.
     * @param fieldToSort     The field on which the pagination needs to be sorted.
     * @param sortByAscending A boolean value indicating whether the results should be sorted in
     *                        ascending order or not is given
     * @return {@link Pageable} object is being returned.
     */
    public static Pageable setPagination(int skip, int limit, String fieldToSort, boolean sortByAscending) {
        limit = 0 != limit ? limit : 10;
        int pageNumber = (0 != skip) ? (skip / limit) : 0;
        return PageRequest.of(pageNumber, limit,
                (sortByAscending ? Sort.by(fieldToSort).ascending() : Sort.by(fieldToSort).descending()));
    }

    /**
     * <p>
     * This function sets pagination for a list of items based on the skip, limit, and sorting
     * parameters provided.
     * </p>
     *
     * @param skip  The number of items to skip before starting to return results.
     * @param limit The "limit" parameter in this method refers to the maximum number of items that
     *              should be returned per page in a paginated result set is given
     * @param sorts {@link List<Order>} A list of sorting criteria to be applied to the results is given
     * @return {@link Pageable} object is being returned.
     */
    public static Pageable setPagination(int skip, int limit, List<Order> sorts) {
        int pageNumber = 0 != skip ? (skip / limit) : 0;
        limit = 0 != limit ? limit : 10;
        return PageRequest.of(pageNumber, limit, Sort.by(sorts));
    }

    /**
     * This Java function sets pagination for a page request based on the skip and limit parameters.
     *
     * @param skip  The number of items to skip before starting to return results.
     * @param limit The "limit" parameter is the maximum number of items that should be returned per
     *              page in a paginated result set. If the limit is not provided or is set to 0, the default value
     *              of 10 will be used.
     * @return {@link Pageable} object is being returned.
     */
    public static Pageable setPagination(int skip, int limit) {
        int pageNumber;
        limit = 0 != limit ? limit : 10;
        pageNumber = skip / limit;
        return PageRequest.of(pageNumber, limit);
    }

    /**
     * <p>
     * This Java function sets pagination for a page request with a given skip, limit, and sort.
     * </p>
     *
     * @param skip  The number of items to skip before starting to return results is given
     * @param limit The "limit" parameter is the maximum number of items that should be returned per
     *              page in a paginated result set is given
     * @param sort  Sort is an object that specifies the sorting order for the results of a query is given
     * @return {@link Pageable} object is being returned.
     */
    public static Pageable setPagination(int skip, int limit, Sort sort) {
        int pageNumber;
        limit = 0 != limit ? limit : 10;
        pageNumber = skip / limit;
        return PageRequest.of(pageNumber, limit, sort);
    }

}
