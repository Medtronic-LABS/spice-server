package com.mdtlabs.coreplatform.spiceadminservice.site.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ParentOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteUserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.County;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.Subcounty;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.repository.DataRepository;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.common.util.Pagination;
import com.mdtlabs.coreplatform.spiceadminservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceadminservice.site.repository.SiteRepository;
import com.mdtlabs.coreplatform.spiceadminservice.site.service.SiteService;

/**
 * <p>
 * SiteServiceImpl class is a Java service that provides methods for managing sites, including adding, updating, and
 * retrieving site information, as well as managing site administrators and retrieving site counts.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private DataRepository dataRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private UserApiInterface userApiInterface;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * {@inheritDoc}
     */
    @Transactional
    public Site addSite(Site site) {
        validateSite(site);
        return siteRepository.save(site);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseListDTO getSites(RequestDTO requestDto) {
        String searchTerm = requestDto.getSearchTerm();
        Pageable pageable = null;
        ResponseListDTO response = new ResponseListDTO();
        if (!CommonUtil.isValidSearchData(searchTerm, Constants.SEARCH_TERM)) {
            response.setTotalCount(0L);
            return response;
        }
        if (0 < requestDto.getLimit()) {
            pageable = Pagination.setPagination(requestDto.getSkip(), requestDto.getLimit(), Constants.UPDATED_AT,
                    Constants.BOOLEAN_FALSE);
        }
        Organization organization = userApiInterface.getOrganizationById(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), requestDto.getTenantId()).getBody();
        Page<Site> sites = null;
        if (!Objects.isNull(organization)) {
            Long countryId = Constants.COUNTRY.equals(organization.getFormName()) ? organization.getFormDataId() : null;
            Long accountId = Constants.ACCOUNT.equals(organization.getFormName()) ? organization.getFormDataId() : null;
            Long operatingUnitId = Constants.OPERATING_UNIT.equals(organization.getFormName()) ? organization.getFormDataId()
                    : null;
            sites = siteRepository.getAllSite(searchTerm, countryId, accountId, operatingUnitId, pageable);
            response.setTotalCount(sites.getTotalElements());
        }
        if (!Objects.isNull(sites) && !sites.isEmpty()) {
            response.setData(
                    sites.stream().map(site -> new ModelMapper().map(site, SiteListDTO.class))
                            .toList());
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public Site updateSite(Site site) {
        validateSite(site);
        Site existingSite = siteRepository.findByIdAndIsDeletedFalse(site.getId());
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(site, existingSite);
        Organization organization = new Organization(site.getTenantId(), site.getName());
        userApiInterface.updateOrganization(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                modelMapper.map(organization, OrganizationDTO.class));
        return siteRepository.save(existingSite);
    }

    /**
     * {@inheritDoc}
     */
    public Site activateDeactivateSite(long id, boolean isActiveStatus) {
        Site siteToUpdate = siteRepository.findByIdAndIsActive(id, isActiveStatus);
        if (Objects.isNull(siteToUpdate)) {
            throw new BadRequestException(27007);
        }
        siteToUpdate.setActive(isActiveStatus);
        return siteRepository.save(siteToUpdate);
    }

    /**
     * {@inheritDoc}
     */
    public List<Site> getSitesByTenantIds(List<Long> tenants) {
        return siteRepository.findByIsDeletedFalseAndTenantIdIn(tenants);
    }

    /**
     * {@inheritDoc}
     */
    public List<Site> getSitesByOperatingUnitId(Long operatingUnitId) {
        return siteRepository.getByOperatingUnitAndIsDeletedFalse(operatingUnitId);
    }

    /**
     * {@inheritDoc}
     */
    public Site getSiteById(Long siteId) {
        Site site = siteRepository.findByIdAndIsDeletedFalse(siteId);
        if (Objects.isNull(site)) {
            throw new DataNotFoundException(27008);
        }
        return siteRepository.findByIdAndIsDeletedFalse(siteId);
    }

    /**
     * {@inheritDoc}
     */
    public List<Long> activateOrDeactivateSites(Long countryId, Long accountId, Long operatingUnitId,
                                                boolean isActive) {
        List<Site> sites = siteRepository.findSite(countryId, accountId, operatingUnitId, !isActive);
        List<Long> tenantIds = new ArrayList<>();
        if (!sites.isEmpty()) {
            sites.forEach(site -> {
                site.setActive(isActive);
                tenantIds.add(site.getTenantId());
            });
            siteRepository.saveAll(sites);
        }
        return tenantIds;
    }

    /**
     * {@inheritDoc}
     */
    public SiteDetailsDTO getSiteDetails(CommonRequestDTO requestData) {
        if (Objects.isNull(requestData.getId()) || Objects.isNull(requestData.getTenantId())) {
            throw new DataNotAcceptableException(20004);
        }
        Site site = siteRepository.findByIdAndIsDeletedFalseAndTenantId(requestData.getId(), requestData.getTenantId());
        if (Objects.isNull(site)) {
            throw new DataNotFoundException(27007);
        }
        SiteDetailsDTO siteDetails = modelMapper.map(site, SiteDetailsDTO.class);
        return constructSiteDetailsResponse(siteDetails, site);
    }

    /**
     * {@inheritDoc}
     */
    public User addSiteAdmin(SiteUserDTO userDto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        User user = modelMapper.map(userDto, User.class);
        if (userDto.isRedRisk()) {
            Role role = new Role();
            role.setName(Constants.ROLE_RED_RISK_USER);
            user.getRoles().add(role);
        }
        return userApiInterface.addAdminUser(CommonUtil.getAuthToken(), UserSelectedTenantContextHolder.get(),
                modelMapper.map(user, UserDetailsDTO.class), userDto.isRedRisk()).getBody();
    }

    /**
     * {@inheritDoc}
     */
    public User updateSiteAdmin(SiteUserDTO userDto) {
        ResponseEntity<User> response = userApiInterface.updateSiteUser(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), userDto);
        return response.getBody();
    }

    /**
     * {@inheritDoc}
     */
    public Boolean deleteSiteAdmin(CommonRequestDTO requestDto) {
        ResponseEntity<Boolean> response = userApiInterface.deleteAdminUser(CommonUtil.getAuthToken(),
                UserSelectedTenantContextHolder.get(), requestDto);
        return response.getBody();
    }

    /**
     * {@inheritDoc}
     */
    public Set<Site> getSiteByIds(Set<Long> ids) {
        return siteRepository.findByIdInAndIsDeletedFalse(ids);
    }

    /**
     * {@inheritDoc}
     */
    public Map<Long, Long> getSiteCountByOperatingUnitIds(List<Long> operatingUnitIds) {
        Map<Long, Long> siteCountsMap = new HashMap<>();
        List<Map<String, Object>> siteCounts = siteRepository.getSiteCountByOperatingUnitIds(operatingUnitIds);
        siteCounts.forEach(count ->
                siteCountsMap.put((Long) count.get(Constants.OPERATING_UNIT_ID), (Long) count.get(Constants.COUNT)));
        return siteCountsMap;
    }

    /**
     * {@inheritDoc}
     */
    public Map<Long, Long> getCountByAccountIds(List<Long> accountIds) {
        Map<Long, Long> siteCountsMap = new HashMap<>();
        List<Map<String, Object>> siteCounts = siteRepository.getCountByAccountIds(accountIds);
        siteCounts.forEach(count ->
                siteCountsMap.put((Long) count.get(Constants.ACCOUNT_ID), (Long) count.get(Constants.COUNT)));
        return siteCountsMap;
    }

    /**
     * {@inheritDoc}
     */
    public Map<Long, Long> getSiteCountByCountryIds(List<Long> countryIds) {
        Map<Long, Long> siteCountsMap = new HashMap<>();
        List<Map<String, Object>> siteCounts = siteRepository.getSiteCountByCountryIds(countryIds);
        siteCounts.forEach(count ->
                    siteCountsMap.put((Long) count.get(Constants.COUNTRY_ID), (Long) count.get(Constants.COUNT)));
        return siteCountsMap;
    }

    /**
     * {@inheritDoc}
     */
    public List<Site> getSiteByCountry(CommonRequestDTO requestDTO) {
        List<Site> sites;
        sites = siteRepository.getSiteByCountryAndTenant(requestDTO.getSearchTerm(), requestDTO.getTenantId(),
                requestDTO.getCountryId());
        return Objects.isNull(sites) ? new ArrayList<>() : sites;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, String>> getCitiesList(RequestDTO requestDTO) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        List<Map<String, String>> citiesList = new ArrayList<>();
        String url = Constants.OSM_CITY_NAME_URL + requestDTO.getSearchTerm();
        ResponseEntity<String> responseValue = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (HttpStatus.OK.equals(responseValue.getStatusCode())) {
            JSONArray cities = new JSONArray(responseValue.getBody());
            for (int index = 0; index < cities.length(); index++) {
                JSONObject locationObj = cities.getJSONObject(index);
                if (locationObj.getString(Constants.ADDRESSTYPE).equals(Constants.CITY)
                        || locationObj.getString(Constants.ADDRESSTYPE).equals(Constants.STATE)) {
                    Map<String, String> city = new HashMap<>();
                    city.put(Constants.LABEL, String.valueOf(locationObj.getLong(Constants.PLACE_ID)));
                    city.put(Constants.VALUE, locationObj.getString(Constants.NAME));
                    city.put(Constants.DISPLAY_NAME, locationObj.getString(Constants.DISPLAY_NAME));
                    city.put(Constants.LATITUDE, locationObj.getString(Constants.LAT));
                    city.put(Constants.LONGITUDE, locationObj.getString(Constants.LON));
                    citiesList.add(city);
                }
            }
        }
        return citiesList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getCityCoordinates(RequestDTO requestDto) {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        Map<String, Object> responseMap = new HashMap<>();
        Map<String, Number> valuesMap = new HashMap<>();
        ResponseEntity<String> responseValue = restTemplate.exchange(
                Constants.OSM_PLACE_ID_URL + requestDto.getLocationId(),
                HttpMethod.GET, entity, String.class);
        JSONObject response;
        if (HttpStatus.OK.equals(responseValue.getStatusCode())) {
            response = new JSONObject(responseValue.getBody());
            responseMap.put(Constants.LOCALNAME, response.getString(Constants.LOCALNAME));
            JSONObject valuesObj = response.getJSONObject(Constants.GEOMETRY);
            valuesMap.put(Constants.LATITUDE, valuesObj.getJSONArray(Constants.COORDINATES).getNumber(1));
            valuesMap.put(Constants.LONGITUDE, valuesObj.getJSONArray(Constants.COORDINATES).getNumber(0));
            responseMap.put(Constants.VALUE, valuesMap);
        }
        return responseMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Long, String> getAllSiteIdAndName() {
        List<Site> siteList = siteRepository.findAll();
        Map<Long, String> siteMap = new HashMap<>();
        siteList.forEach(site -> siteMap.put(site.getId(), site.getName()));
        return siteMap;
    }

    /**
     * <p>
     * This method is used to validate if a site already exists or not.
     * </p>
     *
     * @param site {@link Site} The site which contains information about a particular site is given
     */
    private void validateSite(Site site) {
        if (Objects.isNull(site)) {
            throw new BadRequestException(1003);
        }
        boolean exists;
        if (Objects.isNull(site.getId()) || Constants.ZERO == site.getId()) {
            exists = siteRepository.existsByNameIgnoreCaseAndIsDeletedFalse(site.getName());
        } else {
            exists = siteRepository.existsByNameIgnoreCaseAndIsDeletedFalseAndIdNot(site.getName(), site.getId());
        }

        if (exists) {
            Logger.logError(site.getName() + " already exist(s)");
            throw new DataConflictException(27006, site.getName());
        }
    }

    /**
     * <p>
     * This method is used to construct a SiteDetailsDTO by mapping various properties of a Site
     * </p>
     *
     * @param siteDetailsDto {@link SiteDetailsDTO} A SiteDetailsDTO that contains details about a site is given
     * @param site           {@link Site} The site which contains details about a particular site is given
     * @return {@link SiteDetailsDTO} The site is constructed using the given site details and then returned
     */
    private SiteDetailsDTO constructSiteDetailsResponse(SiteDetailsDTO siteDetailsDto, Site site) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        siteDetailsDto.setOperatingUnit(modelMapper.map(site.getOperatingUnit(), ParentOrganizationDTO.class));
        Account account = dataRepository.getAccountById(site.getAccountId());
        siteDetailsDto.setAccount(modelMapper.map(account, ParentOrganizationDTO.class));
        Country country = dataRepository.getCountryByIdAndIsDeleted(site.getCountryId());
        siteDetailsDto.setCountry(modelMapper.map(country, ParentOrganizationDTO.class));
        County county = dataRepository.getCountyById(site.getCountyId());
        siteDetailsDto.setCounty(modelMapper.map(county, ParentOrganizationDTO.class));
        Subcounty subCounty = dataRepository.getSubCountyById(site.getSubCountyId());
        siteDetailsDto.setSubCounty(modelMapper.map(subCounty, ParentOrganizationDTO.class));
        siteDetailsDto.setCulture(modelMapper.map(site.getCulture(), ParentOrganizationDTO.class));
        siteDetailsDto.setCity(
                Map.of(Constants.LABEL, Objects.isNull(site.getCity()) ? Constants.EMPTY : site.getCity(),
                        Constants.VALUE,
                        Map.of(Constants.LATITUDE, site.getLatitude(), Constants.LONGITUDE, site.getLongitude())));
        siteDetailsDto
                .setSiteLevel((Map.of(Constants.LABEL, site.getSiteLevel(), Constants.VALUE, site.getSiteLevel())));
        return siteDetailsDto;
    }
}
