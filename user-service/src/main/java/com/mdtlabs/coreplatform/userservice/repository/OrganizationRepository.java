package com.mdtlabs.coreplatform.userservice.repository;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * This is the repository class for communicate link between server side and
 * database. This class used to perform all the organization module action in
 * database. In query annotation (nativeQuery = true) the below query perform
 * like SQL. Otherwise its perform like HQL default value for nativeQuery FALSE.
 * </p>
 *
 * @author VigneshKumar created on Jan 30, 2022
 */
@Repository
public interface OrganizationRepository
        extends JpaRepository<Organization, Long>, PagingAndSortingRepository<Organization, Long> {

    String GET_ORGANIZATION = "select organization from Organization  as organization ";

    String GET_ORGANIZATION_BY_ID = GET_ORGANIZATION + "where organization.id =:id ";

    /**
     * <p>
     * This method used to get the organization detail using id.
     * </p>
     *
     * @param id The id of the organization need to retrieve from the
     *           database is given
     * @return {@link Organization} The organization for the given id is retrieved
     */
    @Query(value = GET_ORGANIZATION_BY_ID)
    Organization getOrganizationById(@Param(Constants.ID) long id);

    /**
     * <p>
     * This method is used to get Organization by its name that has not been deleted.
     * </p>
     *
     * @param name {@link String} The name of the organization need to retrieve from the
     *             database is given
     * @return {@link Organization} The organization that matches the given name (ignoring case)
     * and has not been marked as deleted is retrieved
     */
    Organization findByNameIgnoreCaseAndIsDeletedFalse(String name);

    /**
     * <p>
     * This method is used to get the organization that have a given parent organization ID.
     * </p>
     *
     * @param parentOrganizationId The parentOrganization id of the organization need to retrieve from the
     *                             database is given
     * @return {@link List<Organization>} The list of organizations for the given
     * parent organization is retrieved
     */
    List<Organization> findByParentOrganizationId(long parentOrganizationId);

    /**
     * <p>
     * This method is used to get the list of organizations that have a parent organization ID in a given list of IDs.
     * </p>
     *
     * @param childOrgIds {@link List<Long>} The list of parentOrganizationIds of the
     *                    organizations need to retrieve from the
     *                    database is given
     * @return {@link List<Organization>} The list of organizations for the given
     * parent organization ids is retrieved
     */
    List<Organization> findByParentOrganizationIdIn(List<Long> childOrgIds);

    /**
     * ids
     * <p>
     * This method is used to get Organization by its ID that has not been deleted.
     * </p>
     *
     * @param id {@link Long} The id of the organization need to retrieve from the
     *           database is given
     * @return {@link Organization} The organization for the given id which is not
     * deleted is retrieved
     */
    Organization findByIdAndIsDeletedFalse(Long id);

    /**
     * <p>
     * This method is used to get the set of organizations with specified IDs
     * that have not been deleted for the given active status.
     * </p>
     *
     * @param isActive The active status of the organizations need to retrieve from the
     *                 database is given
     * @param idList   {@link List<Long>} The ids of the organizations need to retrieve from the
     *                 database is given
     * @return {@link List<Organization>} The set of organizations for the given list of
     * ids and given active status which are all not deleted is retrieved
     */
    Set<Organization> findByIsDeletedFalseAndIsActiveAndIdIn(boolean isActive, List<Long> idList);

    /**
     * <p>
     * This method is used to get the organization by its form data ID, country name, and deleted status.
     * </p>
     *
     * @param formDataId   The form data id of the organization that need to retrieve from the
     *                     database is given
     * @param country      {@link String} The form name of the organization need to retrieve from the
     *                     database is given
     * @param booleanFalse {@link Boolean} It is used as a filter to search for
     *                     records that have a specific value for the "isDeleted" field is given.
     * @return {@link Organization} The organization for the given form data id and
     * country with the given deleted status is retrieved
     */
    Organization findByFormDataIdAndFormNameAndIsDeleted(long formDataId, String country, Boolean booleanFalse);

    /**
     * <p>
     * This method is used to get the list of organizations that are not deleted and have IDs in a given list.
     * </p>
     *
     * @param organizationIds A list of organization IDs that need to retrieve from the
     *                        database is given
     * @return {@link List<Long>} The list of organizations that have not been deleted and whose IDs are present in the
     * provided list of organization IDs is returned
     */
    List<Organization> findByIsDeletedFalseAndIdIn(List<Long> organizationIds);
}
