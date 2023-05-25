package com.mdtlabs.coreplatform.spiceadminservice.program.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.Program;

/**
 * <p>
 * ProgramRepository is a Java interface that extends the JpaRepository interface, which provides basic
 * CRUD operations for the Program entity. It defines several methods that query the database
 * for specific Program entities based on various criteria such as name, ID, country ID, tenant ID,
 * and site IDs.
 * </p>
 *
 * @author Karthick M created on Jun 30, 2022
 */
@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    String GET_ALL_PROGRAMS = "select program from Program as program where (:countryId is null or program.country.id=:countryId) "
            + "AND  (:tenantId is null or program.tenantId=:tenantId) "
            + " and program.isDeleted=false and (:searchTerm is null or lower(program.name)"
            + " LIKE CONCAT('%',lower(:searchTerm),'%')) order by program.updatedAt DESC";
    String GET_PROGRAM_BY_SITE_IDS = "select program from Program "
            + "program join program.sites as site where site.id in (:siteIds) and program.isActive = true and program.isDeleted = false";
    String COUNT_BY_COUNTRY_AND_TENANT_ID = "select count(program.id) from Program as program "
            + "where program.isDeleted=false AND (:countryId is null or program.country.id=:countryId) AND "
            + "program.tenantId=:tenantId";

    /**
     * <p>
     * This method is used to find the program of given name from the database
     * which is either deleted or not deleted.
     * </p>
     *
     * @param name      {@link String} The name of the program to be searched is given
     * @param tenantId  {@link Long} The tenant ID for which the program is being searched is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the program has been marked as deleted or not is given
     * @return {@link Program} The program associated with the given name and tenant ID is retrieved from the database
     */
    Program findByNameAndTenantIdAndIsDeleted(String name, long tenantId, boolean isDeleted);

    /**
     * <p>
     * This method is used to find the program of given ID from the database
     * which is either deleted or not deleted.
     * </p>
     *
     * @param id        The ID for which the program is being searched is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the program has been marked as deleted or not is given
     * @return {@link Program} The program associated with the given ID is retrieved from the database
     */
    Program findByIdAndIsDeleted(long id, boolean isDeleted);

    /**
     * <p>
     * This method is used to get page of programs based on search term, tenant ID and country ID
     * with the given pagination
     * </p>
     *
     * @param searchTerm {@link String} The term or keyword that is being searched for in the query to
     *                   filter the results is given
     * @param countryId  {@link Long} The ID of the country associated with the program that
     *                   are being searched is given
     * @param tenantId   {@link Long} The tenant ID for which the program is being searched is given
     * @param pageable   {@link Pageable} The pagination information that contains information such as the page number,
     *                   page size, sorting criteria, and more is given
     * @return {@link Page<Program>} A Page of programs that match the search criteria with applied
     * pagination is retrieved and returned from the database
     */
    @Query(value = GET_ALL_PROGRAMS)
    Page<Program> getAllProgram(@Param(Constants.SEARCH_TERM_FIELD) String searchTerm,
                                @Param(Constants.COUNTRY_ID) Long countryId, @Param(Constants.TENANT_PARAMETER_NAME) Long tenantId,
                                Pageable pageable);

    /**
     * <p>
     * This method is used to get a list of programs associated to the given list of site IDs.
     * </p>
     *
     * @param siteIds {@link List<Long>} The list of site IDs for which the
     *                programs need to be retrieved is given
     * @return {@link List<Program>} The list of programs associated with the given list of site IDs is returned
     */
    @Query(value = GET_PROGRAM_BY_SITE_IDS)
    List<Program> findProgramsBySiteIds(@Param("siteIds") List<Long> siteIds);

    /**
     * <p>
     * This method is used to get the count of programs by the given country ID and tenant ID.
     * </p>
     *
     * @param countryId {@link Long} The ID of the country for which the program count needs to be retrieved is given
     * @param tenantId  {@link Long} The tenant ID for which the program count needs to be retrieved is given
     * @return {@link Integer} The count of programs in a database table that match the given countryId and
     * tenantId is returned
     */
    @Query(value = COUNT_BY_COUNTRY_AND_TENANT_ID)
    Integer countByCountryIdAndTenantId(Long countryId, Long tenantId);

    /**
     * <p>
     * This method is used to get the program based on its id and tenantId which is either deleted or not deleted.
     * </p>
     *
     * @param id        {@link Long} The ID for which the program is being searched is given
     * @param tenantId  {@link Long} The tenant ID for which the program is being searched is given
     * @param isDeleted The boolean value that is used to filter the results of the query based
     *                  on whether the program has been marked as deleted or not is given
     * @return {@link Program} The program associated with the given ID and tenant ID is retrieved from the database
     */
    Program findByIdAndIsDeletedAndTenantId(Long id, boolean isDeleted, Long tenantId);
}
