package com.mdtlabs.coreplatform.common.util;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.mdtlabs.coreplatform.common.Constants;

/**
 * <p>
 * The PaginationTest class contains parameterized tests for testing the Pagination class.
 * </p>
 *
 * @author Divya S created on Feb 9, 2023
 */
class PaginationTest {

    @ParameterizedTest
    @CsvSource({"10,10", "0,10", "1,1"})
    void setPagination(int limit, int expectedLimit) {
        //then
        Pageable pageable = Pagination.setPagination(Constants.ZERO, limit);
        assertEquals(expectedLimit, pageable.getPageSize());
        assertEquals(Constants.ZERO, pageable.getPageNumber());
    }

    @ParameterizedTest
    @CsvSource({"10,10", "0,10", "1,1"})
    void testSetPagination(int limit, int expectedLimit) {
        Sort sort = Sort.by(Constants.UPDATED_AT).ascending();
        //then
        Pageable pageable = Pagination.setPagination(Constants.ZERO, limit, sort);
        assertEquals(expectedLimit, pageable.getPageSize());
        assertEquals(Constants.ZERO, pageable.getPageNumber());
        assertEquals(sort, pageable.getSort());
    }

    @ParameterizedTest
    @CsvSource({"10,10,0,0,true", "0,10,10,1,false", "1,1,20,20,false"})
    void testSetPaginationWithSort(int limit, int expectedLimit, int skip, int expectedSkip, boolean sortByAscending) {
        //then
        Pageable pageable = Pagination.setPagination(skip, limit, Constants.UPDATED_AT, sortByAscending);
        assertEquals(expectedLimit, pageable.getPageSize());
        assertEquals(expectedSkip, pageable.getPageNumber());
    }

    @ParameterizedTest
    @CsvSource({"10,10,0,0", "1,1,20,20", "0, 10, 0, 0"})
    void testSetPaginationWithSorts(int limit, int expectedLimit, int skip, int expectedSkip) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(new Sort.Order(Sort.Direction.DESC, Constants.IS_RED_RISK_PATIENT));

        //then
        Pageable pageable = Pagination.setPagination(skip, limit, sorts);
        assertEquals(expectedLimit, pageable.getPageSize());
        assertEquals(expectedSkip, pageable.getPageNumber());
    }
}