package com.mdtlabs.coreplatform.spiceadminservice.data.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.County;
import com.mdtlabs.coreplatform.common.model.entity.Subcounty;
import com.mdtlabs.coreplatform.common.model.entity.User;

/**
 * <p>
 * DataService is defining an interface named DataService which contains several methods for creating, updating, and
 * retrieving information related to countries, counties, sub-counties, and region admins.
 * </p>
 *
 * @author Karthick M created on feb 09, 2023
 */
public interface DataService {

    /**
     * <p>
     * This method is used to create a new country using the provided country details.
     * </p>
     *
     * @param country {@link Country} The country which needs to be created is given
     * @return {@link Country} The country which is created for given country details is returned
     */
    Country createCountry(Country country);

    /**
     * <p>
     * This method is used to update a existing country using the provided country details.
     * </p>
     *
     * @param country {@link Country} The country which needs to be updated is given
     * @return {@link Country} The country which is updated for given country details is returned
     */
    Country updateCountry(Country country);

    /**
     * <p>
     * This method is used to create a new county using the provided county details.
     * </p>
     *
     * @param county {@link County} The county which needs to be created is given
     * @return {@link County} The county which is created for given county details is returned
     */
    County addCounty(County county);

    /**
     * <p>
     * This method is to get the county based on given ID.
     * </p>
     *
     * @param id The ID for which the county is being searched is given
     * @return {@link County} The retrieved county for the given ID is returned
     */
    County getCountyById(long id);

    /**
     * <p>
     * This method is to get the list of counties based on given country ID.
     * </p>
     *
     * @param id The country ID for which the counties are being searched is given
     * @return {@link List<County>} The list of counties for given country ID is returned
     */
    List<County> getAllCountyByCountryId(long id);

    /**
     * <p>
     * This method is used to update a existing county using the provided county details.
     * </p>
     *
     * @param county {@link County} The county which needs to be updated is given
     * @return {@link County} The county which is updated for given county details is returned
     */
    County updateCounty(County county);

    /**
     * <p>
     * This method is used to retrieve a list of countries based on given active status.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The search request contains necessary information
     *                   like active status to get the list of countries is given
     * @return {@link List<Country>} The retrieved list of countries for the given request is returned
     */
    List<Country> getAllCountries(RequestDTO requestDto);

    /**
     * <p>
     * This method is used to retrieve a list of countries based on given active status.
     * </p>
     *
     * @param isActive {@link Boolean} The boolean value that is used to filter the results of the query based
     *                 on whether the country has been marked as active or not is given
     * @return {@link List<Country>} The retrieved list of countries for the given active status is returned
     */
    List<Country> getAllCountries(Boolean isActive);

    /**
     * <p>
     * This method is used to create a new sub county using the provided sub county details.
     * </p>
     *
     * @param subCounty {@link Subcounty} The sub county that contains necessary information to create sub county is given
     * @return {@link Subcounty} The sub county is created using the provided sub county details and returned
     */
    Subcounty createSubCounty(Subcounty subCounty);

    /**
     * <p>
     * This method is to get the country organization dto based on given request.
     * </p>
     *
     * @param request {@link CommonRequestDTO} The common request contains necessary information like ID to
     *                get the country organization dto is given
     * @return {@link CountryOrganizationDTO} The retrieved country organization dto for the given request is returned
     */
    CountryOrganizationDTO getCountryById(CommonRequestDTO request);

    /**
     * <p>
     * This method is used to update a existing sub county using the provided sub county details.
     * </p>
     *
     * @param subCounty {@link Subcounty} The sub county that contains necessary information to update sub county is given
     * @return {@link Subcounty} The sub county is updated using the provided sub county details and returned
     */
    Subcounty updateSubCounty(Subcounty subCounty);

    /**
     * <p>
     * This method is to get all sub counties based on country ID and county ID.
     * </p>
     *
     * @param countryId The country ID for which the sub counties are being searched is given
     * @param countyId  The county ID for which the sub counties are being searched is given
     * @return {@link List<Subcounty>} The list of sub counties for the given country ID and county ID is returned
     */
    List<Subcounty> getAllSubCounty(long countryId, long countyId);

    /**
     * <p>
     * This method is to get the sub county based on given ID.
     * </p>
     *
     * @param id The ID for which the sub county is being searched is given
     * @return {@link Subcounty} The retrieved sub county for the given ID is returned
     */
    Subcounty getSubCountyById(long id);

    /**
     * <p>
     * This method is to get the list of sub counties based on given country ID.
     * </p>
     *
     * @param countryId The country ID for which the sub counties are being searched is given
     * @return {@link List<Subcounty>} The list of sub counties for given country ID is returned
     */
    List<Subcounty> getAllSubCountyByCountryId(Long countryId);

    /**
     * <p>
     * This method is used to get list of country list DTOs with child organization counts.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The search request contains necessary information
     *                   to get the list of country list DTOs is given
     * @return {@link Map} A map containing the retrieved list of country list DTOs and
     * total count for the given request
     */
    Map<String, Object> getCountryList(RequestDTO requestDto);

    /**
     * <p>
     * This method is to get the country based on given ID.
     * </p>
     *
     * @param countryId The ID for which the country is being searched is given
     * @return {@link Country} Returns a success message and status with the retrieved country
     */
    Country findCountryById(Long countryId);

    /**
     * <p>
     * This method is used to create region admin with the provided details.
     * </p>
     *
     * @param user {@link User} The region admin need to be added is given
     * @return {@link User} The user is created using given user details and then returned
     */
    User addRegionAdmin(User user);

    /**
     * <p>
     * This method is used to update region admin with the provided details.
     * </p>
     *
     * @param user {@link User} The region admin need to be added is given
     * @return {@link User} The user is updated using given user details and then returned
     */
    User updateRegionAdmin(@Valid User user);

    /**
     * <p>
     * This method is used to delete an region admin for the given request
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The parameters required to delete the
     *                   admin is given
     * @return {@link Boolean} The user for the given request is deleted and a boolean value
     * indicating whether the user is deleted is returned
     */
    Boolean deleteRegionAdmin(CommonRequestDTO requestDto);

    /**
     * <p>
     * This method is to get the list of sub counties based on given county ID.
     * </p>
     *
     * @param countyId The county ID for which the sub counties are being searched is given
     * @return {@link List<Subcounty>} The list of sub counties for given county ID is returned
     */
    List<Subcounty> getAllSubCountyByCountyId(long countyId);

    /**
     * <p>
     * This method is used to activate or deactivate a region based on the given ID.
     * </p>
     *
     * @param tenantId {@link Long} The tenant ID for which the region need to be activated or deactivated is given
     * @return A boolean value indicating whether the region is activated or Deactivated is returned
     */
    boolean activateOrInactiveRegion(Long tenantId, Boolean isActive);
}
