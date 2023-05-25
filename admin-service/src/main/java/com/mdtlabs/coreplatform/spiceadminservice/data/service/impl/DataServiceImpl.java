package com.mdtlabs.coreplatform.spiceadminservice.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.County;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.Subcounty;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.spiceadminservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceadminservice.account.service.AccountService;
import com.mdtlabs.coreplatform.spiceadminservice.data.repository.CountryRepository;
import com.mdtlabs.coreplatform.spiceadminservice.data.repository.CountyRepository;
import com.mdtlabs.coreplatform.spiceadminservice.data.repository.SubCountyRepository;
import com.mdtlabs.coreplatform.spiceadminservice.data.service.DataService;
import com.mdtlabs.coreplatform.spiceadminservice.operatingunit.service.OperatingUnitService;
import com.mdtlabs.coreplatform.spiceadminservice.site.service.SiteService;

/**
 * <p>
 * DataServiceImpl class provides implementation for various methods related to managing countries, counties, and
 * subcounties.
 * </p>
 *
 * @author Niraimathi S created on feb 09, 2023
 */
@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountyRepository countyRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private OperatingUnitService operatingUnitService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private SubCountyRepository subCountyRepository;

    @Autowired
    private UserApiInterface userApiInterface;

    /**
     * {@inheritDoc}
     */
    @Transactional
    public Country createCountry(Country country) {
        if (Objects.isNull(country)) {
            throw new BadRequestException(1003);
        }
        List<Country> existingCountryByCodeOrName = countryRepository
                .findByCountryCodeOrNameIgnoreCase(country.getCountryCode(), country.getName().strip());
        if (!existingCountryByCodeOrName.isEmpty()) {
            throw new DataConflictException(19001);
        }
        country.setName(country.getName().strip());
        return countryRepository.save(country);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public Country updateCountry(Country country) {
        if (Objects.isNull(country)) {
            throw new BadRequestException(1003);
        }
        Country countryById = countryRepository.findByIdAndIsDeleted(country.getId(), false);
        if (Objects.isNull(countryById)) {
            throw new DataNotFoundException(19004);
        }
        List<Country> existingCountryByCodeOrName = countryRepository
                .findByCountryCodeOrNameIgnoreCase(country.getCountryCode(), country.getName().strip());
        Map<String, Long> nameMap = new HashMap<>();
        Map<String, Long> codeMap = new HashMap<>();
        for (Country element : existingCountryByCodeOrName) {
            nameMap.put(element.getName(), element.getId());
            codeMap.put(element.getCountryCode(), element.getId());
        }
        if (nameMap.containsKey(country.getName())
                && !Objects.equals(nameMap.get(country.getName()), country.getId())) {
            throw new DataConflictException(19001);
        }
        if (codeMap.containsKey(country.getCountryCode())
                && !Objects.equals(codeMap.get(country.getCountryCode()), country.getId())) {
            throw new DataConflictException(19007);
        }
        Organization organization = new Organization();
        organization.setId(country.getTenantId());
        organization.setName(country.getName());
        userApiInterface.updateOrganization(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                modelMapper.map(organization, OrganizationDTO.class));
        return countryRepository.save(country);
    }

    /**
     * {@inheritDoc}
     */
    public List<Country> getAllCountries(RequestDTO requestObject) {
        Pageable pageable = Pagination.setPagination(requestObject.getSkip(), requestObject.getLimit(),
                FieldConstants.NAME, Constants.BOOLEAN_TRUE);
        Page<Country> countries;
        if (!Objects.isNull(requestObject.getSearchTerm()) && 0 < requestObject.getSearchTerm().length()) {
            String formattedSearchTerm = requestObject.getSearchTerm().replaceAll(Constants.SEARCH_TERM,
                    Constants.EMPTY);
            countries = countryRepository.searchCountries(formattedSearchTerm, pageable);
            return countries.stream().toList();
        }
        countries = countryRepository.getAllCountries(pageable);
        return countries.stream().toList();

    }

    /**
     * {@inheritDoc}
     */
    public List<Country> getAllCountries(Boolean isActive) {
        return countryRepository.findByIsActive(isActive);
    }

    /**
     * {@inheritDoc}
     */
    public County addCounty(County county) {
        if (Objects.isNull(county)) {
            throw new BadRequestException(1003);
        }
        County existingCounty = countyRepository.findByCountryIdAndName(county.getCountryId(), county.getName());
        if (!Objects.isNull(existingCounty)) {
            throw new DataConflictException(19002);
        }
        return countyRepository.save(county);
    }

    /**
     * {@inheritDoc}}
     */
    public County getCountyById(long id) {
        County county = countyRepository.findByIdAndIsDeleted(id, false);
        if (Objects.isNull(county)) {
            throw new DataNotFoundException(19005);
        }
        return county;
    }

    /**
     * {@inheritDoc}}
     */
    public List<County> getAllCountyByCountryId(long id) {
        return countyRepository.findByCountryId(id);
    }

    /**
     * {@inheritDoc}}
     */
    public CountryOrganizationDTO getCountryById(CommonRequestDTO request) {
        Country country = countryRepository.findByIdAndTenantIdAndIsDeletedFalse(request.getId(), request.getTenantId());
        if (Objects.isNull(country)) {
            throw new DataNotFoundException(19004);
        }
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        CountryOrganizationDTO countryOrganizationDto = modelMapper.map(country, CountryOrganizationDTO.class);
        countryOrganizationDto.setUsers(userApiInterface.getUsersByTenantIds(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), List.of(country.getTenantId())));
        return countryOrganizationDto;
    }

    /**
     * {@inheritDoc}
     */
    public County updateCounty(County county) {
        getCountyById(county.getId());
        County countyDetails = countyRepository.findByCountryIdAndName(county.getCountryId(), county.getName());
        if (!Objects.isNull(countyDetails) && !Objects.equals(county.getId(), countyDetails.getId())) {
            throw new DataConflictException(19002);
        }
        return countyRepository.save(county);
    }

    /**
     * {@inheritDoc}
     */
    public Subcounty createSubCounty(Subcounty subCounty) {
        if (Objects.isNull(subCounty)) {
            throw new BadRequestException(1003);
        }
        Subcounty existingSubCounty = subCountyRepository.findByName(subCounty.getName());
        if (!Objects.isNull(existingSubCounty)) {
            throw new DataConflictException(19003);
        }
        return subCountyRepository.save(subCounty);
    }

    /**
     * {@inheritDoc}
     */
    public Subcounty updateSubCounty(Subcounty subCounty) {
        getSubCountyById(subCounty.getId());
        Subcounty subCountyDetails = subCountyRepository.findByCountryIdAndCountyIdAndName(subCounty.getCountryId(),
                subCounty.getCountyId(), subCounty.getName());
        if (!Objects.isNull(subCountyDetails) && !Objects.equals(subCountyDetails.getId(), subCounty.getId())) {
            throw new DataConflictException(19003);
        }
        return subCountyRepository.save(subCounty);
    }

    /**
     * {@inheritDoc}
     */
    public Subcounty getSubCountyById(long id) {
        Subcounty subCounty = subCountyRepository.findByIdAndIsDeleted(id, false);
        if (Objects.isNull(subCounty)) {
            throw new DataNotFoundException(19006);
        }
        return subCounty;
    }

    /**
     * {@inheritDoc}
     */
    public List<Subcounty> getAllSubCounty(long countryId, long countyId) {
        return subCountyRepository.getAllSubCounty(countryId, countyId);
    }

    /**
     * {@inheritDoc}
     */
    public List<Subcounty> getAllSubCountyByCountryId(Long countryId) {
        return subCountyRepository.findByCountryId(countryId);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, Object> getCountryList(RequestDTO requestDto) {
        String searchTerm = requestDto.getSearchTerm();
        int totalCount = Constants.ZERO;
        List<CountryListDTO> countryListDTOs = new ArrayList<>();
        if (!CommonUtil.isValidSearchData(searchTerm, Constants.SEARCH_TERM)) {
            return Map.of(Constants.COUNT, totalCount, Constants.DATA, countryListDTOs);
        }
        if (Constants.ZERO == requestDto.getSkip()) {
            totalCount = countryRepository.countByIsDeletedFalse();
        }
        Pageable pageable = Pagination.setPagination(requestDto.getSkip(), requestDto.getLimit(), Constants.CREATED_AT,
                Constants.BOOLEAN_FALSE);
        List<Country> countries = countryRepository.searchCountries(searchTerm, pageable).stream()
                .toList();
        if (Constants.ZERO == requestDto.getSkip()) {
            totalCount = countryRepository.getCountryCountByName(searchTerm);
        }
        constructCountryListResponse(countries, countryListDTOs);
        return Map.of(Constants.COUNT, totalCount, Constants.DATA, countryListDTOs);
    }

    /**
     * {@inheritDoc}
     */
    public Country findCountryById(Long countryId) {
        Country country = countryRepository.findByIdAndIsDeleted(countryId, false);
        if (Objects.isNull(country)) {
            throw new DataNotFoundException(19004);
        }
        return country;
    }

    /**
     * {@inheritDoc}
     */
    public User addRegionAdmin(User user) {
        Role role = new Role();
        role.setName(Constants.ROLE_REGION_ADMIN);
        user.setRoles(Set.of(role));
        ResponseEntity<User> response = userApiInterface.addAdminUser(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), modelMapper.map(user, UserDetailsDTO.class), Boolean.FALSE);
        if (!Objects.isNull(response.getBody())) {
            user = response.getBody();
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    public User updateRegionAdmin(User user) {
        ResponseEntity<User> response = userApiInterface.updateAdminUser(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), modelMapper.map(user, UserDTO.class));
        if (!Objects.isNull(response.getBody())) {
            user = response.getBody();
        }
        return user;

    }

    /**
     * {@inheritDoc}
     */
    public Boolean deleteRegionAdmin(CommonRequestDTO requestDto) {
        ResponseEntity<Boolean> response = userApiInterface.deleteAdminUser(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), requestDto);
        Boolean isUserDeleted = true;
        if (!Objects.isNull(response.getBody())) {
            isUserDeleted = response.getBody();
        }
        return isUserDeleted;
    }

    /**
     * {@inheritDoc}
     */
    public Country getCountryByTenantId(Long tenantId) {
        return countryRepository.findByTenantIdAndIsDeletedFalse(tenantId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public boolean activateOrInactiveRegion(Long tenantId, Boolean isActive) {
        Country country = countryRepository.findByTenantIdAndIsDeletedFalseAndIsActive(tenantId, !isActive);
        if (Objects.isNull(country)) {
            throw new DataNotFoundException(19004);
        }
        country.setActive(isActive);
        countryRepository.save(country);
        List<Long> tenantIds = new ArrayList<>();
        tenantIds.addAll(accountService.activateOrDeactivateAccounts(List.of(country.getId()), isActive));
        tenantIds.addAll(operatingUnitService.activateOrDeactivateOperatingUnits(country.getId(), null, isActive));
        tenantIds.addAll(siteService.activateOrDeactivateSites(country.getId(), null, null, isActive));
        tenantIds.add(tenantId);
        userApiInterface.activateOrDeactivateOrg(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                tenantIds, isActive);
        userApiInterface.activateOrDeactivateUser(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                tenantIds, isActive);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public List<Subcounty> getAllSubCountyByCountyId(long countyId) {
        return subCountyRepository.findByCountyIdAndIsDeletedFalse(countyId);
    }

    /**
     * <p>
     * This method is used to construct a response list of country list DTOs using the list of countries.
     * </p>
     *
     * @param countryListDTOs {@link List<CountryListDTO>} A list of country list DTOs to add
     *                        the constructed country list DTO is given
     * @param countries       {@link List<Country>} A list of countries that provide the data like
     *                        ID, tenant ID, name etc., is given
     */
    private void constructCountryListResponse(List<Country> countries, List<CountryListDTO> countryListDTOs) {
        if (!countries.isEmpty()) {
            Map<Long, Long> noOfSites = siteService.getSiteCountByCountryIds(
                    countries.stream().map(BaseEntity::getId).toList());
            Map<Long, Long> noOfOperatingUnits = operatingUnitService.getOperatingUnitCountByCountryIds(
                    countries.stream().map(BaseEntity::getId).toList());
            Map<Long, Long> noOfAccounts = accountService.getAccountCountByCountryIds(
                    countries.stream().map(BaseEntity::getId).toList());
            for (Country country : countries) {
                CountryListDTO countryListDto = new CountryListDTO();
                countryListDto.setId(country.getId());
                countryListDto.setName(country.getName());
                countryListDto.setTenantId(country.getTenantId());
                countryListDto.setAccountsCount(noOfAccounts.get(country.getId()));
                countryListDto.setOuCount(noOfOperatingUnits.get(country.getId()));
                countryListDto.setSiteCount(noOfSites.get(country.getId()));
                countryListDTOs.add(countryListDto);
            }
        }
    }
}
