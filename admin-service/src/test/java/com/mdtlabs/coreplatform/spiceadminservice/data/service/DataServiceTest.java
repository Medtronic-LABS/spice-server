package com.mdtlabs.coreplatform.spiceadminservice.data.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.FieldConstants;
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
import com.mdtlabs.coreplatform.common.model.dto.spice.UserOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.County;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.Subcounty;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.spiceadminservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceadminservice.account.service.AccountService;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.data.repository.CountryRepository;
import com.mdtlabs.coreplatform.spiceadminservice.data.repository.CountyRepository;
import com.mdtlabs.coreplatform.spiceadminservice.data.repository.SubCountyRepository;
import com.mdtlabs.coreplatform.spiceadminservice.data.service.impl.DataServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.operatingunit.service.OperatingUnitService;
import com.mdtlabs.coreplatform.spiceadminservice.site.service.SiteService;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Data service test.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DataServiceTest {

    @InjectMocks
    private DataServiceImpl dataService;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountyRepository countyRepository;

    @Mock
    private UserApiInterface userApiInterface;

    @Mock
    private SubCountyRepository subCountyRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private OperatingUnitService operatingUnitService;

    @Mock
    private SiteService siteService;

    @Mock
    private RedisTemplate<String, List<Organization>> redisTemplate;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(DataServiceImpl.class, "modelMapper", dataService);
    }

    @Test
    void testCreateCountry() {
        //given
        Country country = TestDataProvider.getCountry();
        country.setCountryCode(TestConstants.COUNTRY_CODE_VALUE);
        List<Country> emptyCountries = new ArrayList<>();

        //when
        when(countryRepository.findByCountryCodeOrNameIgnoreCase(country.getCountryCode(), country.getName()))
                .thenReturn(emptyCountries);
        when(countryRepository.save(country)).thenReturn(country);
        when(redisTemplate.delete(Constants.ORGANIZATION_REDIS_KEY)).thenReturn(Boolean.TRUE);

        //then
        Country actualCountry = dataService.createCountry(country);
        assertNotNull(actualCountry);
        assertEquals(country, actualCountry);
        assertEquals(country.getName(), actualCountry.getName());
        assertEquals(country.getName(), actualCountry.getName());
    }

    @Test
    void throwBadRequestException() {
        //then
        assertThrows(BadRequestException.class, () -> dataService.createCountry(null));
        assertThrows(BadRequestException.class, () -> dataService.updateCountry(null));
        assertThrows(BadRequestException.class, () -> dataService.addCounty(null));
        assertThrows(BadRequestException.class, () -> dataService.createSubCounty(null));
    }

    @Test
    void throwDataConflictException() {
        //given
        Country country = TestDataProvider.getCountry();
        country.setCountryCode(TestConstants.COUNTRY_CODE_VALUE);
        List<Country> countries = List.of(country);
        County county = TestDataProvider.getCounty();
        county.setCountryId(TestConstants.ONE);
        county.setName(TestConstants.SECOND_COUNTRY_NAME);
        Subcounty subcounty = TestDataProvider.getSubCounty();
        subcounty.setName(TestConstants.COUNTRY_NAME);
        subcounty.setCountryId(TestConstants.ONE);
        subcounty.setCountyId(TestConstants.ONE);
        Subcounty emptySubcounty = new Subcounty();
        Subcounty newSubcounty = TestDataProvider.getSubCounty();
        newSubcounty.setId(TestConstants.TWO);
        newSubcounty.setCountyId(TestConstants.TWO);
        newSubcounty.setCountryId(TestConstants.TWO);
        newSubcounty.setName(TestConstants.SECOND_COUNTRY_NAME);

        Country countryToUpdate = TestDataProvider.getCountry();
        countryToUpdate.setId(TestConstants.TWO);
        countryToUpdate.setName(TestConstants.SECOND_COUNTRY_NAME);
        Country secondCountry = TestDataProvider.getCountry();
        secondCountry.setName(TestConstants.SECOND_COUNTRY_NAME);
        secondCountry.setId(Constants.THREE);
        List<Country> existingCountries = List.of(secondCountry);

        Country newCountry = TestDataProvider.getCountry();
        newCountry.setId(TestConstants.FIVE);
        newCountry.setName(TestConstants.SECOND_COUNTRY_NAME);
        newCountry.setCountryCode(TestConstants.COUNTRY_CODE_VALUE);

        Country thirdCountry = TestDataProvider.getCountry();
        thirdCountry.setName(TestConstants.SECOND_COUNTRY_NAME);
        thirdCountry.setId(TestConstants.FIVE);
        Country fourthCountry = TestDataProvider.getCountry();
        fourthCountry.setCountryCode(TestConstants.COUNTRY_CODE_VALUE);
        fourthCountry.setName(TestConstants.COUNTRY_NAME);
        fourthCountry.setId(TestConstants.FOUR);
        List<Country> countryList = List.of(thirdCountry, fourthCountry);

        County secondCounty = TestDataProvider.getCounty();
        secondCounty.setId(Constants.THREE);
        secondCounty.setCountryId(Constants.THREE);
        secondCounty.setName(TestConstants.COUNTRY_NAME);
        County countyDetails = TestDataProvider.getCounty();
        countyDetails.setId(TestConstants.ONE);

        //when
        when(countryRepository.findByCountryCodeOrNameIgnoreCase(country.getCountryCode(), country.getName()))
                .thenReturn(countries);
        when(countyRepository.findByCountryIdAndName(county.getCountryId(), county.getName())).thenReturn(county);
        when(subCountyRepository.findByName(subcounty.getName())).thenReturn(emptySubcounty);
        when(subCountyRepository.findByIdAndIsDeleted(TestConstants.ONE, Boolean.FALSE)).thenReturn(subcounty);
        when(subCountyRepository.findByCountryIdAndCountyIdAndName(TestConstants.ONE, TestConstants.ONE,
                TestConstants.COUNTRY_NAME)).thenReturn(newSubcounty);

        when(countryRepository.findByIdAndIsDeleted(countryToUpdate.getId(), Boolean.FALSE)).thenReturn(countryToUpdate);
        when(countryRepository.findByCountryCodeOrNameIgnoreCase(countryToUpdate.getCountryCode(), countryToUpdate.getName()))
                .thenReturn(existingCountries);

        when(countryRepository.findByIdAndIsDeleted(newCountry.getId(), Boolean.FALSE)).thenReturn(newCountry);
        when(countryRepository.findByCountryCodeOrNameIgnoreCase(newCountry.getCountryCode(), newCountry.getName()))
                .thenReturn(countryList);

        when(countyRepository.findByIdAndIsDeleted(Constants.THREE, Boolean.FALSE)).thenReturn(secondCounty);
        when(countyRepository.findByCountryIdAndName(secondCounty.getCountryId(), secondCounty.getName())).thenReturn(countyDetails);

        //then
        assertThrows(DataConflictException.class, () -> dataService.createCountry(country));
        assertThrows(DataConflictException.class, () -> dataService.addCounty(county));
        assertThrows(DataConflictException.class, () -> dataService.createSubCounty(subcounty));
        assertThrows(DataConflictException.class, () -> dataService.updateSubCounty(subcounty));
        assertThrows(DataConflictException.class, () -> dataService.updateCountry(countryToUpdate));
        assertThrows(DataConflictException.class, () -> dataService.updateCountry(newCountry));
        assertThrows(DataConflictException.class, () -> dataService.updateCounty(secondCounty));
    }

    @Test
    void testUpdateCountry() {
        //given
        Country country = TestDataProvider.getCountry();
        country.setCountryCode(TestConstants.COUNTRY_CODE_VALUE);
        List<Country> countries = List.of(country);
        Organization organization = TestDataProvider.getOrganization();
        organization.setName(TestConstants.COUNTRY_NAME);
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(countryRepository.findByIdAndIsDeleted(country.getId(), Boolean.FALSE)).thenReturn(country);
        when(countryRepository.findByCountryCodeOrNameIgnoreCase(country.getCountryCode(), country.getName()))
                .thenReturn(countries);
        when(userApiInterface.updateOrganization(TestConstants.TEST_TOKEN, TestConstants.ONE, new OrganizationDTO()))
                .thenReturn(organization);
        when(countryRepository.save(country)).thenReturn(country);

        //then
        Country actualCountry = dataService.updateCountry(country);
        TestCommonMethods.cleanUp();
        assertNotNull(actualCountry);
        assertEquals(country.getName(), actualCountry.getName());
        assertEquals(country, actualCountry);
    }

    @Test
    void throwDataNotFoundException() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        Country country = TestDataProvider.getCountry();

        //when
        when(countryRepository.findByIdAndIsDeleted(country.getId(), Boolean.FALSE)).thenReturn(null);
        when(countyRepository.findByIdAndIsDeleted(TestConstants.ONE, Boolean.FALSE)).thenReturn(null);
        when(subCountyRepository.findByIdAndIsDeleted(TestConstants.ONE, Boolean.FALSE)).thenReturn(null);
        when(countryRepository.findByTenantIdAndIsDeletedFalseAndIsActive(TestConstants.ONE, Boolean.FALSE))
                .thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> dataService.updateCountry(country));
        assertThrows(DataNotFoundException.class, () -> dataService.getCountryById(commonRequestDTO));
        assertThrows(DataNotFoundException.class, () -> dataService.getCountryById(commonRequestDTO));
        assertThrows(DataNotFoundException.class, () -> dataService.getSubCountyById(TestConstants.ONE));
        assertThrows(DataNotFoundException.class, () -> dataService.findCountryById(TestConstants.ONE));
        assertThrows(DataNotFoundException.class, () -> dataService.activateOrInactiveRegion(TestConstants.ONE,
                Boolean.TRUE));
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = {"", "a"})
    void testGetAllCountries(String searchTerm) {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(searchTerm, Constants.ZERO,
                TestConstants.TEN);
        Pageable pageable = PageRequest.of(Constants.ZERO, TestConstants.TEN, Sort.by(FieldConstants.NAME)
                .ascending());
        String formattedSearchTerm = searchTerm.replaceAll(Constants.SEARCH_TERM,
                Constants.EMPTY);
        List<Country> countries = List.of(TestDataProvider.getCountry());
        Page<Country> countryPage = new PageImpl<>(countries);

        //when
        when(countryRepository.searchCountries(formattedSearchTerm, pageable)).thenReturn(countryPage);
        when(countryRepository.getAllCountries(pageable)).thenReturn(countryPage);

        //then
        List<Country> actualCountries = dataService.getAllCountries(requestDTO);
        assertNotNull(actualCountries);
        assertFalse(actualCountries.isEmpty());
        assertEquals(countries.size(), actualCountries.size());
    }

    @Test
    void testGetAllCountries() {
        //given
        List<Country> countries = List.of(TestDataProvider.getCountry());

        //when
        when(countryRepository.findByIsActive(Boolean.TRUE)).thenReturn(countries);

        //then
        List<Country> actualCountries = dataService.getAllCountries(Boolean.TRUE);
        assertNotNull(actualCountries);
        assertFalse(actualCountries.isEmpty());
        assertEquals(countries.size(), actualCountries.size());
    }

    @Test
    void testAddCounty() {
        //given
        County county = TestDataProvider.getCounty();
        county.setCountryId(TestConstants.ONE);
        county.setName(TestConstants.SECOND_COUNTRY_NAME);

        //when
        when(countyRepository.findByCountryIdAndName(county.getCountryId(), county.getName())).thenReturn(null);
        when(countyRepository.save(county)).thenReturn(county);

        //then
        County actualCounty = dataService.addCounty(county);
        assertNotNull(actualCounty);
        assertEquals(county, actualCounty);
        assertEquals(county.getName(), actualCounty.getName());
    }

    @Test
    void testGetCountyById() {
        //given
        County county = TestDataProvider.getCounty();
        county.setCountryId(TestConstants.ONE);
        county.setName(TestConstants.SECOND_COUNTRY_NAME);

        //when
        when(countyRepository.findByIdAndIsDeleted(TestConstants.ONE, Boolean.FALSE)).thenReturn(county);

        //then
        County actualCounty = dataService.getCountyById(TestConstants.ONE);
        assertNotNull(actualCounty);
        assertEquals(county, actualCounty);
        assertEquals(county.getName(), actualCounty.getName());
    }

    @Test
    void testGetAllCountyByCountryId() {
        //given
        List<County> counties = List.of(TestDataProvider.getCounty());

        //when
        when(countyRepository.findByCountryId(TestConstants.ONE)).thenReturn(counties);

        //then
        List<County> actualCounties = dataService.getAllCountyByCountryId(TestConstants.ONE);
        assertNotNull(actualCounties);
        assertFalse(actualCounties.isEmpty());
        assertEquals(counties.size(), actualCounties.size());
    }

    @Test
    void testGetCountryById() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        Country country = TestDataProvider.getCountry();
        country.setCountryCode(TestConstants.COUNTRY_CODE_VALUE);
        country.setTenantId(TestConstants.ONE);
        CountryOrganizationDTO countryOrganizationDTO = TestDataProvider.getCountryOrganizationDTO();
        UserOrganizationDTO userOrganizationDTO = TestDataProvider.getUserOrganizationDTO();
        userOrganizationDTO.setId(TestConstants.ONE);
        TestCommonMethods.init();

        //when
        when(countryRepository.findByIdAndTenantIdAndIsDeletedFalse(commonRequestDTO.getId(), commonRequestDTO.getTenantId())).thenReturn(country);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(country, CountryOrganizationDTO.class)).thenReturn(countryOrganizationDTO);
        when(userApiInterface.getUsersByTenantIds(TestConstants.TEST_TOKEN, TestConstants.ONE, List.of(country.getTenantId()))).thenReturn(List.of(userOrganizationDTO));
        TestCommonMethods.getStaticMock();

        //then
        CountryOrganizationDTO actualCountryOrganizationDTO = dataService.getCountryById(commonRequestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(actualCountryOrganizationDTO);
        assertEquals(countryOrganizationDTO, actualCountryOrganizationDTO);
        assertEquals(countryOrganizationDTO.getId(), actualCountryOrganizationDTO.getId());
    }

    @Test
    void testUpdateCounty() {
        //given
        County county = TestDataProvider.getCounty();

        //when
        when(countyRepository.findByIdAndIsDeleted(county.getId(), Boolean.FALSE)).thenReturn(county);
        when(countyRepository.findByCountryIdAndName(county.getCountryId(), county.getName())).thenReturn(county);
        when(countyRepository.save(county)).thenReturn(county);

        //then
        County updateCounty = dataService.updateCounty(county);
        assertNotNull(updateCounty);
        assertEquals(county.getName(), updateCounty.getName());
    }

    @Test
    void testCreateSubCounty() {
        //given
        Subcounty subcounty = TestDataProvider.getSubCounty();
        subcounty.setName(TestConstants.COUNTRY_NAME);

        //when
        when(subCountyRepository.findByName(subcounty.getName())).thenReturn(null);
        when(subCountyRepository.save(subcounty)).thenReturn(subcounty);

        //then
        Subcounty actualSubCounty = dataService.createSubCounty(subcounty);
        assertNotNull(actualSubCounty);
        assertEquals(subcounty.getName(), actualSubCounty.getName());
        assertEquals(subcounty, actualSubCounty);
    }

    @Test
    void testUpdateSubCounty() {
        //given
        Subcounty subcounty = TestDataProvider.getSubCounty();
        subcounty.setName(TestConstants.COUNTRY_NAME);
        subcounty.setCountryId(TestConstants.ONE);
        subcounty.setCountyId(TestConstants.ONE);

        //when
        when(subCountyRepository.findByIdAndIsDeleted(TestConstants.ONE, Boolean.FALSE)).thenReturn(subcounty);
        when(subCountyRepository.findByCountryIdAndCountyIdAndName(TestConstants.ONE, TestConstants.ONE, subcounty.getName())).thenReturn(null);
        when(subCountyRepository.save(subcounty)).thenReturn(subcounty);

        //then
        Subcounty actualSubCounty = dataService.updateSubCounty(subcounty);
        assertNotNull(subcounty);
        assertEquals(subcounty, actualSubCounty);
        assertEquals(subcounty.getName(), actualSubCounty.getName());
    }

    @Test
    void testGetSubCountyById() {
        //given
        Subcounty subcounty = TestDataProvider.getSubCounty();
        subcounty.setName(TestConstants.COUNTRY_NAME);

        //when
        when(subCountyRepository.findByIdAndIsDeleted(TestConstants.ONE, Boolean.FALSE)).thenReturn(subcounty);

        //then
        Subcounty actualSubcounty = dataService.getSubCountyById(TestConstants.ONE);
        assertNotNull(actualSubcounty);
        assertEquals(subcounty, actualSubcounty);
        assertEquals(subcounty.getName(), actualSubcounty.getName());
    }

    @Test
    void testGetAllSubCounty() {
        //given
        List<Subcounty> subCounties = List.of(TestDataProvider.getSubCounty());

        //when
        when(subCountyRepository.getAllSubCounty(TestConstants.ONE, TestConstants.ONE)).thenReturn(subCounties);

        //then
        List<Subcounty> actualSubCounties = dataService.getAllSubCounty(TestConstants.ONE, TestConstants.ONE);
        assertNotNull(actualSubCounties);
        assertFalse(actualSubCounties.isEmpty());
        assertEquals(subCounties.size(), actualSubCounties.size());
        assertEquals(subCounties.get(0), actualSubCounties.get(0));
    }

    @Test
    void testGetAllSubCountyByCountryId() {
        //given
        List<Subcounty> subCounties = List.of(TestDataProvider.getSubCounty());

        //when
        when(subCountyRepository.findByCountryId(TestConstants.ONE)).thenReturn(subCounties);

        //then
        List<Subcounty> actualSubCounties = dataService.getAllSubCountyByCountryId(TestConstants.ONE);

        assertNotNull(actualSubCounties);
        assertFalse(actualSubCounties.isEmpty());
        assertEquals(subCounties.size(), actualSubCounties.size());
        assertEquals(subCounties.get(0), actualSubCounties.get(0));
    }

    @Test
    void testFindCountryById() {
        //given
        Country country = TestDataProvider.getCountry();

        //when
        when(countryRepository.findByIdAndIsDeleted(TestConstants.ONE, Boolean.FALSE)).thenReturn(country);

        //then
        Country actualCountry = dataService.findCountryById(TestConstants.ONE);
        assertNotNull(actualCountry);
        assertEquals(country.getId(), actualCountry.getId());
    }

    @Test
    void testAddRegionAdmin() {
        //given
        Role role = new Role();
        role.setName(Constants.ROLE_REGION_ADMIN);
        User user = TestDataProvider.getUser();
        ResponseEntity<User> response = new ResponseEntity<>(user, HttpStatus.OK);
        UserDetailsDTO userDTO = TestDataProvider.getUserDetailsDTO();
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(userApiInterface.addAdminUser(TestConstants.TEST_TOKEN, TestConstants.ONE, userDTO, Boolean.FALSE)).thenReturn(response);
        when(modelMapper.map(user, UserDetailsDTO.class)).thenReturn(userDTO);

        //then
        User actualRegionAdmin = dataService.addRegionAdmin(user);
        TestCommonMethods.cleanUp();
        assertNotNull(actualRegionAdmin);
        assertEquals(user, actualRegionAdmin);
        assertEquals(user.getUsername(), actualRegionAdmin.getUsername());
    }

    @Test
    void testUpdateRegionAdmin() {
        //given
        Role role = new Role();
        role.setName(Constants.ROLE_REGION_ADMIN);
        User user = TestDataProvider.getUser();
        ResponseEntity<User> response = new ResponseEntity<>(user, HttpStatus.OK);
        UserDTO userDTO = TestDataProvider.getUserDTO();
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(userApiInterface.updateAdminUser(TestConstants.TEST_TOKEN, TestConstants.ONE, userDTO)).thenReturn(response);

        //then
        User actualRegionAdmin = dataService.updateRegionAdmin(user);
        TestCommonMethods.cleanUp();
        assertNotNull(actualRegionAdmin);
        assertEquals(user, actualRegionAdmin);
        assertEquals(user.getUsername(), actualRegionAdmin.getUsername());
    }

    @Test
    void testDeleteRegionAdmin() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        ResponseEntity<Boolean> response = new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(userApiInterface.deleteAdminUser(TestConstants.TEST_TOKEN, TestConstants.ONE, commonRequestDTO)).thenReturn(response);

        //then
        Boolean deletedRegionAdmin = dataService.deleteRegionAdmin(commonRequestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(deletedRegionAdmin);
        assertTrue(deletedRegionAdmin);
    }

    @Test
    void testGetCountryByTenantId() {
        //given
        Country country = TestDataProvider.getCountry();

        //when
        when(countryRepository.findByTenantIdAndIsDeletedFalse(TestConstants.ONE)).thenReturn(country);

        //then
        Country actualCountry = dataService.getCountryByTenantId(TestConstants.ONE);
        assertNotNull(actualCountry);
        assertEquals(country, actualCountry);
        assertEquals(country.getId(), actualCountry.getId());
    }

    @Test
    void testActivateOrDeactivateRegion() {
        //given
        Country country = TestDataProvider.getCountry();
        List<Long> tenantIds = new ArrayList<>(List.of(TestConstants.ONE, TestConstants.TWO, Constants.THREE));
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(countryRepository.findByTenantIdAndIsDeletedFalseAndIsActive(TestConstants.ONE, Boolean.FALSE))
                .thenReturn(country);
        when(countryRepository.save(country)).thenReturn(country);
        when(accountService.activateOrDeactivateAccounts(List.of(country.getId()), Boolean.TRUE))
                .thenReturn(List.of(TestConstants.ONE));
        when(operatingUnitService.activateOrDeactivateOperatingUnits(country.getId(), null, Boolean.TRUE))
                .thenReturn(List.of(TestConstants.TWO));
        when(siteService.activateOrDeactivateSites(country.getId(), null, null, Boolean.TRUE))
                .thenReturn(List.of(Constants.THREE));
        when(userApiInterface.activateOrDeactivateOrg(TestConstants.TEST_TOKEN, TestConstants.ONE, tenantIds,
                Boolean.TRUE))
                .thenReturn(Boolean.TRUE);
        doNothing().when(userApiInterface).activateOrDeactivateUser(TestConstants.TEST_TOKEN,
                TestConstants.ONE, tenantIds, Boolean.TRUE);

        //then
        boolean isActivatedOrDeactivated = dataService.activateOrInactiveRegion(TestConstants.ONE, Boolean.TRUE);
        TestCommonMethods.cleanUp();
        assertTrue(isActivatedOrDeactivated);
    }

    @Test
    void testGetAllSubCountyByCountyId() {
        //given
        List<Subcounty> subCounties = List.of(TestDataProvider.getSubCounty());

        //when
        when(subCountyRepository.findByCountyIdAndIsDeletedFalse(TestConstants.ONE)).thenReturn(subCounties);

        //then
        List<Subcounty> actualSubCounties = dataService.getAllSubCountyByCountyId(TestConstants.ONE);
        assertNotNull(actualSubCounties);
        assertFalse(actualSubCounties.isEmpty());
        assertEquals(subCounties.size(), actualSubCounties.size());
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @NullSource    // pass a null value
    @ValueSource(strings = {"", "a"})
    void testGetCountryList(String searchTerm) {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(searchTerm, Constants.ZERO,
                TestConstants.TEN);
        requestDTO.setTenantId(TestConstants.ONE);
        String formattedSearchTerm = "a".replaceAll(Constants.SEARCH_TERM, Constants.EMPTY);
        Pageable pageable = PageRequest.of(Constants.ZERO, TestConstants.TEN, Sort.by(Constants.CREATED_AT).descending());
        Page<Country> countries = new PageImpl<>(TestDataProvider.getCountries());
        Map<Long, Long> sitesCount = Map.of(1L, 1L, 2L, 1L);
        Map<Long, Long> accountsCount = Map.of(1L, 1L, 2L, 1L);
        Map<Long, Long> operatingUnitsCount = Map.of(1L, 1L, 2L, 1L);

        CountryListDTO countryListDTO = TestDataProvider.getCountryListDTO();
        countryListDTO.setId(TestConstants.ONE);
        countryListDTO.setOuCount(1L);
        countryListDTO.setSiteCount(1L);
        countryListDTO.setAccountsCount(1L);
        CountryListDTO secondCountryListDto = TestDataProvider.getCountryListDTO();
        secondCountryListDto.setId(TestConstants.TWO);
        secondCountryListDto.setName(TestConstants.SECOND_COUNTRY_NAME);
        secondCountryListDto.setSiteCount(1L);
        secondCountryListDto.setOuCount(1L);
        secondCountryListDto.setAccountsCount(1L);
        List<CountryListDTO> countryListDTOs = new ArrayList<>();
        countryListDTOs.add(countryListDTO);
        countryListDTOs.add(secondCountryListDto);
        List<Long> countryIds = List.of(TestConstants.ONE, TestConstants.TWO);

        //when
        when(countryRepository.countByIsDeletedFalse()).thenReturn(Constants.ONE);
        when(countryRepository.searchCountries(searchTerm, pageable)).thenReturn(countries);
        when(countryRepository.searchCountries(formattedSearchTerm, pageable)).thenReturn(countries);
        when(countryRepository.getCountryCountByName(searchTerm)).thenReturn(Constants.TWO);
        when(siteService.getSiteCountByCountryIds(
                countries.stream().map(BaseEntity::getId).toList())).thenReturn(sitesCount);
        when(operatingUnitService.getOperatingUnitCountByCountryIds(
                countries.stream().map(BaseEntity::getId).toList())).thenReturn(operatingUnitsCount);
        when(accountService.getAccountCountByCountryIds(
                countries.stream().map(BaseEntity::getId).toList())).thenReturn(accountsCount);

        //then
        Map<String, Object> actualCountries = dataService.getCountryList(requestDTO);
        assertNotNull(actualCountries);
        assertFalse(actualCountries.isEmpty());
        assertTrue(actualCountries.containsKey(Constants.COUNT));
        assertTrue(actualCountries.containsKey(Constants.DATA));
        assertEquals(countryListDTOs, actualCountries.get(Constants.DATA));
        assertEquals(countryIds.size(), actualCountries.get(Constants.COUNT));
    }
}