package com.mdtlabs.coreplatform.spiceadminservice.data.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.annotations.UserTenantValidation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountyDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SubCountyDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.County;
import com.mdtlabs.coreplatform.common.model.entity.Subcounty;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.spiceadminservice.data.service.DataService;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;

/**
 * <p>
 * Data controller class contains methods for retrieving, creating, updating, and deleting data related to countries,
 * counties, and sub-counties.
 * </p>
 *
 * @author Niraimathi S created on feb 09, 2023
 */
@RestController
@RequestMapping("/data")
@Validated
public class DataController {

    private static final List<String> noDataList = List.of(Constants.NO_DATA_FOUND);
    @Autowired
    private DataService dataService;
    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * <p>
     * This method is used to retrieve a list of accounts based on a search request.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The request contains necessary information
     *                   to get the list of countries is given
     * @return {@link SuccessResponse} Returns a success message and status with the retrieved
     * list of countries and total count
     */
    @GetMapping("/country-list")
    public SuccessResponse<List<CountryDTO>> getAllCountries(@RequestBody RequestDTO requestDto) {
        Logger.logInfo("Getting All Country Details");
        List<Country> countries = dataService.getAllCountries(requestDto);
        if (!countries.isEmpty()) {
            List<CountryDTO> countryDTOList = modelMapper.map(countries, new TypeToken<List<CountryDTO>>() {
            }.getType());
            return new SuccessResponse(SuccessCode.GET_COUNTRIES, countryDTOList, countryDTOList.size(),
                    HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.GET_COUNTRIES, noDataList, Constants.ZERO, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to create a new county using the provided county details.
     * </p>
     *
     * @param countyDto {@link CountyDTO} The county dto that contains
     *                  necessary information to create county is given
     * @return {@link SuccessResponse<County>} Returns a success message and status after updating the county
     */
    @PostMapping("/county/create")
    public SuccessResponse<County> addCounty(@Valid @RequestBody CountyDTO countyDto) {
        Logger.logInfo("Adding a new country ");
        dataService.addCounty(modelMapper.map(countyDto, County.class));
        return new SuccessResponse<>(SuccessCode.COUNTY_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is to get the county based on given ID.
     * </p>
     *
     * @param id The ID for which the county is being searched is given
     * @return {@link SuccessResponse<County>} Returns a success message and status with the retrieved county
     */
    @GetMapping("/county/get/{id}")
    public SuccessResponse<County> getCountyById(@PathVariable(value = Constants.ID) long id) {
        Logger.logInfo("Get a county by ID");
        return new SuccessResponse<>(SuccessCode.GET_COUNTY, dataService.getCountyById(id), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is to get the list of counties based on given country ID.
     * </p>
     *
     * @param id The country ID for which the counties are being searched is given
     * @return {@link List<County>} The list of counties for given country ID is returned
     */
    @GetMapping("/county-list/{id}")
    public List<County> getAllCountyByCountryId(@PathVariable(value = Constants.ID) long id) {
        Logger.logInfo("Getting all County based on Country");
        return dataService.getAllCountyByCountryId(id);
    }

    /**
     * <p>
     * This method is used to create a new country using the provided country details.
     * </p>
     *
     * @param countryDto {@link CountryDTO} The country dto that contains
     *                   necessary information to create country is given
     * @return {@link Country} The country which is created for given country details is returned
     */
    @PostMapping("/country/create")
    public Country createCountry(@Valid @RequestBody CountryDTO countryDto) {
        return dataService.createCountry(modelMapper.map(countryDto, Country.class));
    }

    /**
     * <p>
     * This method is used to update a existing country using the provided country details.
     * </p>
     *
     * @param countryDto {@link CountryDTO} The country dto that contains
     *                   necessary information to update country is given
     * @return {@link SuccessResponse<Country>} Returns a success message and status after updating
     * the country
     */
    @UserTenantValidation
    @PutMapping("/country/update")
    public SuccessResponse<Country> updateCountry(@Valid @RequestBody CountryDTO countryDto) {
        Logger.logInfo("Updates a Country");
        dataService.updateCountry(modelMapper.map(countryDto, Country.class));
        return new SuccessResponse<>(SuccessCode.COUNTRY_UPDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to create a new sub county using the provided sub county details.
     * </p>
     *
     * @param subCountyDto {@link SubCountyDTO} The sub county dto that contains
     *                     necessary information to create sub county is given
     * @return {@link SuccessResponse<SubCountyDTO>} Returns a success message and status after creating the sub county
     */
    @PostMapping("/subcounty")
    public SuccessResponse<SubCountyDTO> createSubCounty(@Valid @RequestBody SubCountyDTO subCountyDto) {
        Logger.logInfo("Creates a SubCounty based on Request");
        dataService.createSubCounty(modelMapper.map(subCountyDto, Subcounty.class));
        return new SuccessResponse<>(SuccessCode.SUB_COUNTY_SAVE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is to get the country organization dto based on given request.
     * </p>
     *
     * @param request {@link CommonRequestDTO} The common request contains necessary information like ID to
     *                get the country organization dto is given
     * @return {@link SuccessResponse<CountryOrganizationDTO>} Returns a success message and status with the retrieved
     * country organization dto
     */
    @UserTenantValidation
    @PostMapping("/country/details")
    public SuccessResponse<CountryOrganizationDTO> getCountryById(@RequestBody CommonRequestDTO request) {
        Logger.logInfo("Getting a Country details by on ID");
        return new SuccessResponse<>(SuccessCode.GET_COUNTRY,
                dataService.getCountryById(request), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to update existing sub county using the provided sub county details.
     * </p>
     *
     * @param subCountyDto {@link SubCountyDTO} The sub county dto that contains
     *                     necessary information to update sub county is given
     * @return {@link SuccessResponse<SubCountyDTO>} Returns a success message and status after updating the sub county
     */
    @PutMapping("/subcounty")
    public SuccessResponse<Subcounty> updateSubCountry(@Valid @RequestBody SubCountyDTO subCountyDto) {
        Logger.logInfo("Updates a SUbCounty");
        dataService.updateSubCounty(modelMapper.map(subCountyDto, Subcounty.class));
        return new SuccessResponse<>(SuccessCode.SUB_COUNTY_UPDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is to get the sub county based on given ID.
     * </p>
     *
     * @param subCountyId The ID for which the sub county is being searched is given
     * @return {@link SuccessResponse<Subcounty>} Returns a success message and status with the retrieved sub county
     */
    @GetMapping("/subcounty/{id}")
    public SuccessResponse<Subcounty> getSubCountyById(@PathVariable(value = Constants.ID) long subCountyId) {
        Logger.logInfo("Getting a list of County based on Country");
        return new SuccessResponse<>(SuccessCode.GET_SUB_COUNTY, dataService.getSubCountyById(subCountyId),
                HttpStatus.OK);
    }


    /**
     * <p>
     * This method is used to get list of sub country details based on given country ID and county ID.
     * </p>
     *
     * @param countryId The country ID for which the sub counties are being searched is given
     * @param countyId  The county ID for which the sub counties are being searched is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with the retrieved
     * list of sub counties and total count
     */
    @GetMapping("/subcounty-list/{countryid}/{countyid}")
    public SuccessResponse<List<Subcounty>> getAllSubCounty(@PathVariable(value = "countryid") long countryId,
                                                            @PathVariable(value = "countyid") long countyId) {
        Logger.logInfo("Getting a SubCountry based on CountryId and countyId");
        List<Subcounty> subCounties = dataService.getAllSubCounty(countryId, countyId);
        if (subCounties.isEmpty()) {
            return new SuccessResponse(SuccessCode.GET_SUB_COUNTIES, noDataList, Constants.ZERO, HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.GET_SUB_COUNTIES, subCounties, subCounties.size(),
                HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get list of country list DTOs with child organization counts.
     * </p>
     *
     * @param requestDto {@link RequestDTO} The search request contains necessary information
     *                   to get the list of country list DTOs is given
     * @return {@link SuccessResponse<List>} Returns a success message and status with the retrieved
     * list of country list DTOs and total count
     */
    @PostMapping("/country/list")
    public SuccessResponse<List<CountryListDTO>> getCountryList(@RequestBody RequestDTO requestDto) {
        Map<String, Object> response = dataService.getCountryList(requestDto);
        int totalCount = (Objects.isNull(response.get(Constants.COUNT))) ? Constants.ZERO
                : Integer.parseInt(response.get(Constants.COUNT).toString());
        if (Constants.ZERO == totalCount) {
            return new SuccessResponse(SuccessCode.GET_COUNTRY, (List<CountryListDTO>) response.get(Constants.DATA),
                    HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.GET_COUNTRY, (List<CountryListDTO>) response.get(Constants.DATA),
                totalCount, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is to get the list of sub counties based on given country ID.
     * </p>
     *
     * @param countryId The country ID for which the sub counties are being searched is given
     * @return {@link List<Subcounty>} The list of sub counties for given country ID is returned
     */
    @GetMapping("/subcounty-list/{id}")
    public List<Subcounty> getAllSubCountyByCountryId(@PathVariable(value = Constants.ID) long countryId) {
        return dataService.getAllSubCountyByCountryId(countryId);
    }

    /**
     * <p>
     * This method is to get the country based on given ID.
     * </p>
     *
     * @param countryId The ID for which the country is being searched is given
     * @return {@link Country} Returns a success message and status with the retrieved country
     */
    @GetMapping("/get-country/{id}")
    public Country getCountry(@PathVariable(value = Constants.ID) long countryId) {
        return dataService.findCountryById(countryId);
    }

    /**
     * <p>
     * This method is used to create region admin with the provided details.
     * </p>
     *
     * @param userDto {@link UserDTO} The region admin need to be added is given
     * @return {@link SuccessResponse<User>} The user is added and a success message with the
     * and status is returned
     */
    @UserTenantValidation
    @PostMapping("/country/user-add")
    public SuccessResponse<User> addRegionAdmin(@RequestBody @Valid UserDetailsDTO userDto) {
        dataService.addRegionAdmin(modelMapper.map(userDto, User.class));
        return new SuccessResponse<>(SuccessCode.REGION_ADMIN_SAVE, HttpStatus.CREATED);
    }

    /**
     * <p>
     * This method is used to update region admin with the provided details.
     * </p>
     *
     * @param userDto {@link UserDTO} The region admin need to be updated is given
     * @return {@link SuccessResponse<User>} The user is updated and a success message with the
     * and status is returned
     */
    @UserTenantValidation
    @PutMapping("/country/user-update")
    public SuccessResponse<User> updateRegionAdmin(@RequestBody @Valid UserDetailsDTO userDto) {
        dataService.updateRegionAdmin(modelMapper.map(userDto, User.class));
        return new SuccessResponse<>(SuccessCode.REGION_ADMIN_UPDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to delete an region admin for the given request
     * </p>
     *
     * @param requestDto {@link CommonRequestDTO} The parameters required to delete the
     *                   admin is given
     * @return {@link SuccessResponse<User>} The user for the given request is
     * deleted and success message with status is returned
     */
    @UserTenantValidation
    @DeleteMapping("/country/user-remove")
    public SuccessResponse<User> removeRegionAdmin(@RequestBody CommonRequestDTO requestDto) {
        dataService.deleteRegionAdmin(requestDto);
        return new SuccessResponse<>(SuccessCode.REGION_ADMIN_DELETE, HttpStatus.OK);

    }

    /**
     * <p>
     * This method is used to retrieve a list of countries based on given active status.
     * </p>
     *
     * @param isActive {@link Boolean} The boolean value that is used to filter the results of the query based
     *                 on whether the country has been marked as active or not is given
     * @return {@link SuccessResponse} Returns a success message and status with the retrieved
     * list of countries
     */
    @UserTenantValidation
    @GetMapping("/countries")
    public SuccessResponse<List<Country>> getCountries(@RequestParam Boolean isActive) {
        Logger.logInfo("Getting All Country Details");
        List<Country> countries = dataService.getAllCountries(isActive);
        return new SuccessResponse(SuccessCode.GET_COUNTRIES, countries, Constants.ZERO, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to deactivate a region based on the given ID.
     * </p>
     *
     * @param id {@link Long} The ID for which the region need to be deactivated is given
     * @return {@link SuccessResponse} Returns a success message and status
     * after deactivating the region for the given id
     */
    @GetMapping("/country/deactivate/{id}")
    public SuccessResponse<String> deactivateRegion(@PathVariable Long id) {
        dataService.activateOrInactiveRegion(id, Constants.BOOLEAN_FALSE);
        return new SuccessResponse<>(SuccessCode.DEACTIVATE_COUNTRY, HttpStatus.OK);
    }

    /**
     * <p>
     * This method is to get the list of sub counties based on given county ID.
     * </p>
     *
     * @param countyId The county ID for which the sub counties are being searched is given
     * @return {@link List<Subcounty>} The list of sub counties for given county ID is returned
     */
    @GetMapping("/subcounty-by-county-id/{id}")
    public SuccessResponse<List<Subcounty>> getAllSubCountyByCountyId(@PathVariable(FieldConstants.ID) long countyId) {
        List<Subcounty> subCounties = dataService.getAllSubCountyByCountyId(countyId);
        if (Objects.isNull(subCounties)) {
            subCounties = new ArrayList<>();
        }
        return new SuccessResponse(SuccessCode.GET_COUNTRY, subCounties,
                subCounties.size(), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to return a success response with a boolean value indicating the health status of the application.
     * </p>
     *
     * @return {@link SuccessResponse<Boolean>} Returns a boolean value of TRUE and status indicating that
     * the health check was successful
     */
    @GetMapping("/health-check")
    public SuccessResponse<Boolean> healthCheck() {
        return new SuccessResponse(SuccessCode.HEALTH_CHECK,
                Boolean.TRUE, HttpStatus.OK);
    }
}
