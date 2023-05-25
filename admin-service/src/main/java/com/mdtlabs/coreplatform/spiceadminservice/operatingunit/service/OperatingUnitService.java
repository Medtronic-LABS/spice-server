package com.mdtlabs.coreplatform.spiceadminservice.operatingunit.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;
import com.mdtlabs.coreplatform.common.model.entity.User;

/**
 * <p>
 * OperatingUnitService is defining an interface called OperatingUnitService which contains several methods for managing
 * operating units.
 * </p>
 *
 * @author VigneshKumar created on Jun 20, 2022
 */
public interface OperatingUnitService {

    /**
     * <p>
     * This method is used to get list of operating unit details using the given request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The search request contains necessary information
     *                   to get the list of operating units is given
     * @return {@link ResponseListDTO} The retrieved list of operating units and total count is returned
     */
    ResponseListDTO getOperatingUnitList(RequestDTO requestDto);

    /**
     * <p>
     * This method is used to update an operating unit admin with the provided details.
     * </p>
     *
     * @param user {@link User} The operating unit admin need to be added is given
     * @return {@link User} The user is created for given user details and returned
     */
    User addOperatingUnitAdmin(User user);

    /**
     * <p>
     * This method is used to update an operating unit admin with the provided details.
     * </p>
     *
     * @param user {@link User} The operating unit admin need to be updated is given
     * @return {@link User} The user is updated for given user details and returned
     */
    User updateOperatingUnitAdmin(@Valid User user);

    /**
     * <p>
     * This method is used to delete an operating unit admin for the given request
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The parameters required to delete the admin is given
     * @return {@link User} The user for the given request is deleted and corresponding boolean value is returned
     */
    Boolean deleteOperatingUnitAdmin(CommonRequestDTO requestDto);

    /**
     * <p>
     * This method is used to get list of operating units and count as ResponseListDTO using the given request.
     * </p>
     *
     * @param requestDto {@link SearchRequestDTO} The search request contains necessary information
     *                   to get the list of operating unit DTOs is given
     * @return {@link ResponseListDTO} The retrieved list of operating units DTOs and total count for the
     * given search request is returned
     */
    ResponseListDTO getAllOperatingUnits(SearchRequestDTO requestDto);

    /**
     * <p>
     * This method is used to get list of operating unit details based on given request
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The search request contains necessary information
     *                   to get the list of operating unit details is given
     * @return {@link OperatingUnitDetailsDTO} The retrieved list of operating unit details
     * for the given request is returned
     */
    OperatingUnitDetailsDTO getOperatingUnitDetails(CommonRequestDTO requestDto);

    /**
     * <p>
     * This method is used to activate or deactivate operating units based on country ID and account ID.
     * </p>
     *
     * @param countryId {@link Long} The ID of the country for which the operating units
     *                  are being searched is given
     * @param accountId {@link Long} The ID of the country for which the operating units
     *                  are being searched is given
     * @param isActive  The boolean value that is used to filter the results of the query based
     *                  on whether the operating units have been marked as active or not is given
     * @return {@link List} The list of IDs associated with activated or deactivated operating units
     * of given country ID and account ID
     */
    List<Long> activateOrDeactivateOperatingUnits(Long countryId, Long accountId, boolean isActive);

    /**
     * <p>
     * This method is used to activate or deactivate an operating unit by its ID.
     * </p>
     *
     * @param id       {@link Long} The ID for which the operating unit is being searched is given
     * @param isActive {@link Boolean} The boolean value indicating whether the operating unit is active or not is given
     */
    void activateOrDeactivateOperatingUnit(Long id, boolean isActive);

    /**
     * <p>
     * This method is used to create a new operating unit using the operating unit data.
     * </p>
     *
     * @param operatingUnit {@link Operatingunit} The operating unit dto that contains necessary information
     *                      to create operating unit is given
     * @return {@link Operatingunit} The operating unit is created using the provided operating unit
     * details and returned
     */
    Operatingunit createOperatingUnit(Operatingunit operatingUnit);

    /**
     * <p>
     * This method is used to update a existing operating unit using the operating unit Dto.
     * </p>
     *
     * @param operatingUnit {@link Operatingunit} The operating unit dto that contains necessary information
     *                      to update operating unit is given
     * @return {@link Operatingunit} The operating unit is updated using the provided operating unit
     * details and returned
     */
    Operatingunit updateOperatingUnit(Operatingunit operatingUnit);

    /**
     * <p>
     * This method is used to get a list of map containing account IDs with corresponding count of operating units that
     * is searched using the given account IDs.
     * </p>
     *
     * @param accountIds {@link List} The list of account IDs associated with the operating units that
     *                   are being searched is given
     * @return {@link Map} A map containing key as account IDs and value as count of operating units
     * for the corresponding account IDs provided is returned
     */
    Map<Long, Long> getOperatingUnitCountByAccountIds(List<Long> accountIds);

    /**
     * <p>
     * This method is used to get a list of map containing country IDs with corresponding count of operating units that
     * is searched using the given country IDs.
     * </p>
     *
     * @param countryIds {@link List} The list of country IDs associated with the operating units that
     *                   are being searched is given
     * @return {@link Map} A map containing key as country IDs and value as count of operating units
     * for the corresponding country IDs provided returned
     */
    Map<Long, Long> getOperatingUnitCountByCountryIds(List<Long> countryIds);
}
