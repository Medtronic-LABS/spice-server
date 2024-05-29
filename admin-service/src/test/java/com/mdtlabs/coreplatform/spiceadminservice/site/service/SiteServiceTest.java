package com.mdtlabs.coreplatform.spiceadminservice.site.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.InheritingConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
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
import com.mdtlabs.coreplatform.spiceadminservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.site.repository.SiteRepository;
import com.mdtlabs.coreplatform.spiceadminservice.site.service.impl.SiteServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Site service implementation.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SiteServiceTest {

    @InjectMocks
    private SiteServiceImpl siteService;

    @Mock
    private SiteRepository siteRepository;

    @Mock
    private UserApiInterface userApiInterface;

    @Mock
    private DataRepository dataRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RedisTemplate<String, List<Organization>> redisTemplate;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(SiteServiceImpl.class, "modelMapper", siteService);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0, 1})
    void testAddSite(Long id) {
        //given
        Site site = TestDataProvider.getSite();
        site.setId(id);

        //when
        when(siteRepository.existsByNameIgnoreCaseAndIsDeletedFalse(site.getName())).thenReturn(Boolean.FALSE);
        when(siteRepository.existsByNameIgnoreCaseAndIsDeletedFalseAndIdNot(site.getName(), site.getId())).thenReturn(Boolean.FALSE);
        when(siteRepository.save(site)).thenReturn(site);
        when(redisTemplate.delete(Constants.ORGANIZATION_REDIS_KEY)).thenReturn(Boolean.TRUE);

        //then
        Site actualSite = siteService.addSite(site);
        assertNotNull(site);
        assertEquals(site, actualSite);
        assertEquals(site.getCountryId(), actualSite.getCountryId());
    }

    @Test
    void updateSite() {
        //given
        Site site = TestDataProvider.getSite();
        site.setId(TestConstants.ONE);
        Organization organization = new Organization(site.getTenantId(), site.getName());
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(siteRepository.existsByNameIgnoreCaseAndIsDeletedFalse(site.getName())).thenReturn(Boolean.FALSE);
        when(siteRepository.findByIdAndIsDeletedFalse(site.getId())).thenReturn(site);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        doNothing().when(modelMapper).map(site, site);
        when(userApiInterface.updateOrganization(TestConstants.TEST_TOKEN, TestConstants.ONE, new OrganizationDTO())).thenReturn(organization);
        when(siteRepository.save(site)).thenReturn(site);

        //then
        Site actualSite = siteService.updateSite(site);
        TestCommonMethods.cleanUp();
        assertNotNull(site);
        assertEquals(site, actualSite);
        assertEquals(site.getCountryId(), actualSite.getCountryId());
    }

    @Test
    void testActivateDeactivateSite() {
        //given
        Site site = TestDataProvider.getSite();
        site.setActive(Boolean.TRUE);
        Long siteId = TestConstants.ONE;

        //when
        when(siteRepository.findByIdAndIsActive(siteId, Boolean.TRUE)).thenReturn(site);
        when(siteRepository.save(site)).thenReturn(site);

        //then
        Site actualSite = siteService.activateDeactivateSite(siteId, Boolean.TRUE);
        assertNotNull(site);
        assertEquals(site, actualSite);
        assertTrue(actualSite.isActive());
        assertEquals(site.getCountryId(), actualSite.getCountryId());
    }

    @Test
    void testGetSitesByTenantIds() {
        //given
        List<Long> tenantIds = List.of(TestConstants.ONE, TestConstants.TWO);
        List<Site> sites = TestDataProvider.getSiteList();

        //when
        when(siteRepository.findByIsDeletedFalseAndTenantIdIn(tenantIds)).thenReturn(sites);
        //then
        List<Site> actualSites = siteService.getSitesByTenantIds(tenantIds);
        assertNotNull(actualSites);
        assertFalse(actualSites.isEmpty());
        assertEquals(sites.size(), actualSites.size());
        assertEquals(sites.get(0).getId(), actualSites.get(0).getId());
    }

    @Test
    void testGetSitesByOperatingUnitId() {
        //given
        Long operatingUnitId = TestConstants.ONE;
        List<Site> sites = TestDataProvider.getSiteList();

        //when
        when(siteRepository.getByOperatingUnitAndIsDeletedFalse(operatingUnitId)).thenReturn(sites);

        //then
        List<Site> actualSites = siteService.getSitesByOperatingUnitId(operatingUnitId);
        assertNotNull(actualSites);
        assertFalse(actualSites.isEmpty());
        assertEquals(sites.size(), actualSites.size());
        assertEquals(sites.get(0).getId(), actualSites.get(0).getId());
    }

    @Test
    void testGetSiteById() {
        //given
        Site site = TestDataProvider.getSite();
        Long siteId = TestConstants.ONE;

        //when
        when(siteRepository.findByIdAndIsDeletedFalse(siteId)).thenReturn(site);

        //then
        Site actualSite = siteService.getSiteById(siteId);
        assertNotNull(site);
        assertEquals(site, actualSite);
        assertEquals(site.getCountryId(), actualSite.getCountryId());
    }

    @Test
    void testActivateOrDeactivateSites() {
        //given
        List<Site> sites = TestDataProvider.getSiteList();
        sites.get(0).setActive(true);
        sites.get(1).setActive(true);
        List<Long> tenantIds = List.of(TestConstants.ONE, TestConstants.TWO);

        //when
        when(siteRepository.findSite(TestConstants.ONE, TestConstants.ONE, TestConstants.ONE, Boolean.TRUE))
                .thenReturn(sites);
        when(siteRepository.saveAll(sites)).thenReturn(sites);

        //then
        List<Long> actualIds = siteService.activateOrDeactivateSites(TestConstants.ONE, TestConstants.ONE, TestConstants.ONE, Boolean.FALSE);
        assertNotNull(actualIds);
        assertFalse(actualIds.isEmpty());
        assertEquals(tenantIds, actualIds);
        assertEquals(tenantIds.size(), actualIds.size());
    }

    @Test
    void testGetSiteDetails() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        Site site = TestDataProvider.getSite();
        site.setAccountId(TestConstants.ONE);
        site.setOperatingUnit(TestDataProvider.getOperatingUnit());
        site.setCountyId(TestConstants.ONE);
        site.setSubCountyId(TestConstants.ONE);
        site.setLatitude(TestConstants.SIXTY);
        site.setLongitude(TestConstants.EIGHTY);
        site.setCulture(TestDataProvider.getCulture());
        site.setCity(TestConstants.CITY_NAME);
        site.setSiteLevel(TestConstants.SITE_LEVEL);
        SiteDetailsDTO siteDetailsDTO = TestDataProvider.getSiteDetailsDTO();
        siteDetailsDTO.setCity(Map.of(Constants.LABEL, site.getCity(), Constants.VALUE, Map.of(Constants.LATITUDE, site.getLatitude(), Constants.LONGITUDE, site.getLongitude())));
        siteDetailsDTO.setSiteLevel(Map.of(Constants.LABEL, site.getSiteLevel(), Constants.VALUE, site.getSiteLevel()));
        ParentOrganizationDTO parentOrganizationDTO = TestDataProvider.getParentOrganizationDTO();
        Account account = TestDataProvider.getAccount();
        Country country = TestDataProvider.getCountry();
        County county = TestDataProvider.getCounty();
        Subcounty subCounty = TestDataProvider.getSubCounty();

        //when
        when(siteRepository.findByIdAndIsDeletedFalseAndTenantId(commonRequestDTO.getId(), commonRequestDTO.getTenantId())).thenReturn(site);
        when(modelMapper.map(site, SiteDetailsDTO.class)).thenReturn(siteDetailsDTO);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(site.getOperatingUnit(), ParentOrganizationDTO.class)).thenReturn(parentOrganizationDTO);
        when(dataRepository.getAccountById(site.getAccountId())).thenReturn(account);
        when(modelMapper.map(account, ParentOrganizationDTO.class)).thenReturn(parentOrganizationDTO);
        when(dataRepository.getCountryByIdAndIsDeleted(site.getCountryId())).thenReturn(country);
        when(modelMapper.map(country, ParentOrganizationDTO.class)).thenReturn(parentOrganizationDTO);
        when(dataRepository.getCountyById(site.getCountyId())).thenReturn(county);
        when(modelMapper.map(county, ParentOrganizationDTO.class)).thenReturn(parentOrganizationDTO);
        when(dataRepository.getSubCountyById(site.getSubCountyId())).thenReturn(subCounty);
        when(modelMapper.map(subCounty, ParentOrganizationDTO.class)).thenReturn(parentOrganizationDTO);
        when(modelMapper.map(site.getCulture(), ParentOrganizationDTO.class)).thenReturn(parentOrganizationDTO);

        //then
        SiteDetailsDTO actualResponse = siteService.getSiteDetails(commonRequestDTO);
        assertNotNull(actualResponse);
        assertEquals(siteDetailsDTO, actualResponse);
        assertEquals(siteDetailsDTO.getAccount(), actualResponse.getAccount());
        assertEquals(siteDetailsDTO.getSiteLevel(), actualResponse.getSiteLevel());
        assertEquals(siteDetailsDTO.getCity(), actualResponse.getCity());
    }

    @Test
    void testAddSiteAdmin() {
        //given
        SiteUserDTO siteUserDTO = TestDataProvider.getSiteUserDTO();
        siteUserDTO.setRedRisk(true);
        Role role = new Role();
        role.setName(Constants.ROLE_RED_RISK_USER);
        User user = TestDataProvider.getUser();
        user.getRoles().add(role);
        ResponseEntity<User> response = new ResponseEntity<>(user, HttpStatus.OK);
        UserDetailsDTO userDTO = TestDataProvider.getUserDetailsDTO();

        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(siteUserDTO, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDetailsDTO.class)).thenReturn(userDTO);
        when(userApiInterface.addAdminUser(TestConstants.TEST_TOKEN, TestConstants.ONE, userDTO, Boolean.TRUE))
                .thenReturn(response);

        //then
        User actualUser = siteService.addSiteAdmin(siteUserDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(actualUser);
        assertEquals(user.getUsername(), actualUser.getUsername());
        assertFalse(actualUser.getRoles().isEmpty());
        assertEquals(user.getRoles(), actualUser.getRoles());
    }

    @Test
    void testUpdateSiteAdmin() {
        //given
        SiteUserDTO siteUserDTO = TestDataProvider.getSiteUserDTO();
        User user = TestDataProvider.getUser();
        ResponseEntity<User> response = new ResponseEntity<>(user, HttpStatus.OK);
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(userApiInterface.updateSiteUser(TestConstants.TEST_TOKEN, TestConstants.ONE, siteUserDTO))
                .thenReturn(response);

        //then
        User actualUser = siteService.updateSiteAdmin(siteUserDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(actualUser);
        assertEquals(user.getUsername(), actualUser.getUsername());
    }

    @Test
    void testDeleteSiteAdmin() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        ResponseEntity<Boolean> response = new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(userApiInterface.deleteAdminUser(TestConstants.TEST_TOKEN, TestConstants.ONE, commonRequestDTO))
                .thenReturn(response);

        //then
        Boolean isDeleted = siteService.deleteSiteAdmin(commonRequestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(isDeleted);
        assertTrue(isDeleted);
    }

    @Test
    void testGetSiteByIds() {
        //given
        Set<Long> ids = Set.of(TestConstants.ONE, TestConstants.TWO);
        Set<Site> sites = TestDataProvider.getSites();

        //when
        when(siteRepository.findByIdInAndIsDeletedFalse(ids)).thenReturn(sites);

        //then
        Set<Site> actualSites = siteService.getSiteByIds(ids);
        assertNotNull(actualSites);
        assertFalse(actualSites.isEmpty());
        assertEquals(sites.size(), actualSites.size());
    }

    @Test
    void testGetSiteCountByOperatingUnitIds() {
        //given
        List<Long> ids = List.of(TestConstants.ONE);
        List<Map<String, Object>> response = List.of(Map.of("1", Constants.ONE));

        //when
        when(siteRepository.getSiteCountByOperatingUnitIds(ids)).thenReturn(response);

        //then
        Map<Long, Long> actualResponse = siteService.getSiteCountByOperatingUnitIds(ids);
        assertNotNull(actualResponse);
        assertFalse(actualResponse.isEmpty());
        assertEquals(Constants.ONE, actualResponse.size());
    }

    @Test
    void testGetCountByAccountIds() {
        //given
        List<Long> ids = List.of(TestConstants.ONE);
        List<Map<String, Object>> response = List.of(Map.of("1", Constants.ONE));

        //when
        when(siteRepository.getCountByAccountIds(ids)).thenReturn(response);

        //then
        Map<Long, Long> actualResponse = siteService.getCountByAccountIds(ids);
        assertNotNull(actualResponse);
        assertFalse(actualResponse.isEmpty());
        assertEquals(Constants.ONE, actualResponse.size());
    }

    @Test
    void testGetSiteCountByCountryIds() {
        //given
        List<Long> ids = List.of(TestConstants.ONE);
        List<Map<String, Object>> response = List.of(Map.of("1", Constants.ONE));

        //when
        when(siteRepository.getSiteCountByCountryIds(ids)).thenReturn(response);

        //then
        Map<Long, Long> actualResponse = siteService.getSiteCountByCountryIds(ids);
        assertNotNull(actualResponse);
        assertFalse(actualResponse.isEmpty());
        assertEquals(Constants.ONE, actualResponse.size());
    }

    @Test
    void testGetSiteByCountry() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        commonRequestDTO.setCountryId(TestConstants.ONE);
        commonRequestDTO.setSearchTerm(Constants.EMPTY);
        List<Site> sites = TestDataProvider.getSiteList();

        //when
        when(siteRepository.getSiteByCountryAndTenant(commonRequestDTO.getSearchTerm(), commonRequestDTO.getTenantId(),
                commonRequestDTO.getCountryId())).thenReturn(sites);
        //then
        List<Site> actualSites = siteService.getSiteByCountry(commonRequestDTO);
        assertNotNull(actualSites);
        assertFalse(actualSites.isEmpty());
        assertEquals(sites.size(), actualSites.size());
        assertEquals(sites.get(0).getId(), actualSites.get(0).getId());
    }

    @Test
    void testGetSiteByCountryEmpty() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        commonRequestDTO.setCountryId(TestConstants.ONE);
        commonRequestDTO.setSearchTerm(Constants.EMPTY);

        //when
        when(siteRepository.getSiteByCountryAndTenant(commonRequestDTO.getSearchTerm(), commonRequestDTO.getTenantId(),
                commonRequestDTO.getCountryId())).thenReturn(null);

        //then
        List<Site> actualSites = siteService.getSiteByCountry(commonRequestDTO);
        assertNotNull(actualSites);
        assertTrue(actualSites.isEmpty());
    }

    @Test
    void throwBadRequestException() {
        //when
        when(siteRepository.findByIdAndIsActive(TestConstants.ONE, Boolean.TRUE)).thenReturn(null);

        //then
        assertThrows(BadRequestException.class, () -> siteService.activateDeactivateSite(TestConstants.ONE, Boolean.TRUE));
        assertThrows(BadRequestException.class, () -> siteService.addSite(null));
    }

    @Test
    void throwDataConflictException() {
        //given
        Site site = TestDataProvider.getSite();
        site.setName(TestConstants.SITE_NAME);
        site.setId(TestConstants.ONE);

        //when
        when(siteRepository.existsByNameIgnoreCaseAndIsDeletedFalseAndIdNot(site.getName(), site.getId())).thenReturn(Boolean.TRUE);

        //then
        assertThrows(DataConflictException.class, () -> siteService.addSite(site));
    }

    @ParameterizedTest
    @CsvSource({"1,", ",1"})
    void getSiteDetailsException(Long id, Long tenantId) {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(id, tenantId);
        CommonRequestDTO requestData = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        //when
        when(siteRepository.findByIdAndIsDeletedFalseAndTenantId(requestData.getId(), requestData.getTenantId())).thenReturn(null);

        //then
        assertThrows(DataNotAcceptableException.class, () -> siteService.getSiteDetails(commonRequestDTO));
        assertThrows(DataNotFoundException.class, () -> siteService.getSiteDetails(requestData));
    }

    @Test
    void testGetSites() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(Constants.EMPTY, Constants.ZERO, TestConstants.TEN);
        requestDTO.setTenantId(TestConstants.ONE);
        Organization organization = TestDataProvider.getOrganization();
        organization.setFormName(Constants.COUNTRY);
        ResponseEntity<Organization> organizationResponseEntity = new ResponseEntity<>(organization, HttpStatus.OK);
        List<Site> sites = TestDataProvider.getSiteList();
        List<SiteListDTO> siteListDTOs = TestDataProvider.getSiteListDTOs();
        Page<Site> sitePage = new PageImpl<>(sites);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(new String[]{Constants.UPDATED_AT}).descending());
        ResponseListDTO response = TestDataProvider.getResponseListDTO();
        response.setTotalCount(sitePage.getTotalElements());
        response.setData(siteListDTOs);
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        TestCommonMethods.getStaticMockValidation(Constants.EMPTY);
        when(userApiInterface.getOrganizationById("BearerTest", TestConstants.ONE, TestConstants.ONE)).thenReturn(organizationResponseEntity);
        when(siteRepository.getAllSite(Constants.EMPTY, TestConstants.ONE, null, null, pageable)).thenReturn(sitePage);

        //then
        ResponseListDTO actualResponse = siteService.getSites(requestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(actualResponse);
        assertEquals(TestConstants.TWO, actualResponse.getTotalCount());
        assertEquals(siteListDTOs, actualResponse.getData());
        assertEquals(siteListDTOs.get(0), actualResponse.getData().get(0));
    }

    @Test
    void getSitesWithInvalidSearchTerm() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(Constants.EMPTY, Constants.ZERO, TestConstants.TEN);
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMockValidationFalse(Constants.EMPTY);

        //then
        ResponseListDTO actualResponse = siteService.getSites(requestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(actualResponse);
        assertEquals(Constants.ZERO, actualResponse.getTotalCount());
        assertNull(actualResponse.getData());
    }

    @Test
    void testGetCitiesList() {
        //given
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        List<Map<String, String>> citiesList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        Map<String, String> map = Map.of("display_name", "Dhaka",
                "lat", "23.7644025",
                "place_id", "3442474911",
                "name", "Dhaka",
                "lon", "90.389015",
                "addresstype", "city");
        JSONObject jsonObject = new JSONObject(map);
        jsonArray.put(jsonObject);
        jsonArray.put(new JSONObject(Map.of("display_name", "Chennai",
                "lat", "21.2443432",
                "place_id", "3443424912",
                "name", "Chennai",
                "lon", "94.389215",
                "addresstype", "city")));
        jsonArray.put(new JSONObject(Map.of("display_name", "Tamil Nadu",
                "lat", "11.24233432",
                "place_id", "3443424913",
                "name", "Tamil Nadu",
                "lon", "92.349215",
                "addresstype", "state")));
        ResponseEntity<String> responseValue = new ResponseEntity<>(jsonArray.toString(), HttpStatus.OK);
        ResponseEntity<String> responseValueNotOK = new ResponseEntity<>(jsonArray.toString(), HttpStatus.BAD_REQUEST);
        RequestDTO requestDTO = TestDataProvider.getRequestDto(true, TestConstants.ONE);
        requestDTO.setSearchTerm(Constants.SEARCH_TERM);
        String url = Constants.OSM_CITY_NAME_URL + requestDTO.getSearchTerm();
        citiesList.add(Map.of("value", "3442474911",
                "label", "Dhaka"));
        citiesList.add(Map.of(
                "value", "3443424912",
                "label", "Chennai"));
        citiesList.add(Map.of(
                "value", "3443424913",
                "label", "Tamil Nadu"));

        //when
        when(restTemplate.exchange(url, HttpMethod.GET, entity, String.class)).thenReturn(responseValue);

        //then
        List<Map<String, String>> actualCities = siteService.getCitiesList(requestDTO);
        assertNotNull(actualCities);
        assertEquals("city", jsonObject.get("addresstype"));
        assertFalse(actualCities.isEmpty());
        assertEquals(citiesList.size(), actualCities.size());
        assertEquals(citiesList.get(0).get("label"), actualCities.get(0).get("label"));
        assertEquals(citiesList.get(0).get("value"), actualCities.get(0).get("value"));
    }

    @Test
    void testGetCityCoordinates() {
        //given
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RequestDTO requestDTO = TestDataProvider.getRequestDto(true, TestConstants.ONE);
        requestDTO.setLocationId("3442474911");
        String url = Constants.OSM_PLACE_ID_URL + requestDTO.getLocationId();

        JSONArray coordinates = new JSONArray();
        coordinates.put("90.389015");
        coordinates.put("23.7644025");
        JSONObject geometry = new JSONObject(Map.of("coordinates", coordinates));
        JSONObject response = new JSONObject(Map.of(Constants.LOCALNAME, "Dhaka", Constants.GEOMETRY, geometry));

        ResponseEntity<String> responseValue = new ResponseEntity<>(response.toJSONString(), HttpStatus.OK);

        //when
        when(restTemplate.exchange(url, HttpMethod.GET, entity, String.class)).thenReturn(responseValue);

        //then
        Map<String, Object> actualCoordinates = siteService.getCityCoordinates(requestDTO);
        assertNotNull(actualCoordinates);
        assertFalse(actualCoordinates.isEmpty());
        assertEquals(response.size(), actualCoordinates.size());
        assertTrue(actualCoordinates.containsKey(Constants.VALUE));
    }

    @Test
    void getAllSiteIdAndName() {
        //given
        Site site = TestDataProvider.getSite();
        site.setId(1l);
        site.setName(Constants.NAME);
        List<Site> sites = List.of(site);
        //when
        when(siteRepository.findAll()).thenReturn(sites);
        //then
        Map<Long, String> response = siteService.getAllSiteIdAndName();
        assertTrue(response.containsKey(1l));
        assertTrue(response.containsValue("name"));
    }
}