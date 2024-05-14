package com.mdtlabs.coreplatform.spiceadminservice.data.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;

import com.mdtlabs.coreplatform.common.Constants;
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
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.data.service.impl.DataServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Data controller.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DataControllerTest {

    @InjectMocks
    private DataController dataController;

    @Mock
    private DataServiceImpl dataService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(DataController.class, "modelMapper", dataController);
    }

    @Test
    void testGetAllCountries() {
        //given
        List<Country> countries = TestDataProvider.getCountries();
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        List<CountryDTO> countryDtos = TestDataProvider.getListCountryDTO();

        //when
        when(dataService.getAllCountries(requestDTO)).thenReturn(countries);
        when(modelMapper.map(countries, new TypeToken<List<CountryDTO>>() {
        }.getType())).thenReturn(countryDtos);

        //then
        SuccessResponse<List<CountryDTO>> actualCountries = dataController.getAllCountries(requestDTO);
        assertNotNull(actualCountries);
        assertEquals(HttpStatus.OK, actualCountries.getStatusCode());
    }

    @Test
    void testGetAllCountriesEmpty() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);

        //when
        when(dataService.getAllCountries(requestDTO)).thenReturn(new ArrayList<>());

        //then
        SuccessResponse<List<CountryDTO>> actualCountries = dataController.getAllCountries(requestDTO);
        assertNotNull(actualCountries);
        assertEquals(HttpStatus.OK, actualCountries.getStatusCode());
    }

    @Test
    void testAddCounty() {
        //given
        CountyDTO countyDTO = TestDataProvider.getCountyDTO();
        County county = TestDataProvider.getCounty();

        //when
        when(modelMapper.map(countyDTO, County.class)).thenReturn(county);
        when(dataService.addCounty(county)).thenReturn(county);

        //then
        SuccessResponse<County> actualCounty = dataController.addCounty(countyDTO);
        assertNotNull(actualCounty);
        assertEquals(HttpStatus.CREATED, actualCounty.getStatusCode());
    }

    @Test
    void testGetCountyById() {
        //given
        County county = TestDataProvider.getCounty();

        //when
        when(dataService.getCountyById(TestConstants.ONE)).thenReturn(county);

        //then
        County actualCounty = dataController.getCountyById(TestConstants.ONE);
        assertNotNull(actualCounty);
        assertEquals(county, actualCounty);
    }

    @Test
    void testGetAllCountyByCountryId() {
        //given
        List<County> counties = TestDataProvider.getCounties();

        //when
        when(dataService.getAllCountyByCountryId(TestConstants.ONE)).thenReturn(counties);

        //then
        List<County> actualCounties = dataController.getAllCountyByCountryId(TestConstants.ONE);
        assertNotNull(actualCounties);
        assertEquals(counties.size(), actualCounties.size());
    }

    @Test
    void testCreateCountry() {
        //given
        Country country = TestDataProvider.getCountry();
        CountryDTO countryDTO = TestDataProvider.getCountryDto();

        //when
        when(dataService.createCountry(country)).thenReturn(country);
        when(modelMapper.map(countryDTO, Country.class)).thenReturn(country);

        //then
        Country actualCountry = dataController.createCountry(countryDTO);
        assertNotNull(actualCountry);
        assertEquals(country.getName(), actualCountry.getName());
    }

    @Test
    void testUpdateCountry() {
        //given
        Country country = TestDataProvider.getCountry();
        CountryDTO countryDTO = TestDataProvider.getCountryDto();

        //when
        when(dataService.updateCountry(country)).thenReturn(country);
        when(modelMapper.map(countryDTO, Country.class)).thenReturn(country);

        //then
        SuccessResponse<Country> actualCountry = dataController.updateCountry(countryDTO);
        assertNotNull(actualCountry);
        assertEquals(HttpStatus.OK, actualCountry.getStatusCode());
    }

    @Test
    void testCreateSubCounty() {
        //given
        Subcounty subcounty = TestDataProvider.getSubCounty();
        SubCountyDTO subCountyDTO = TestDataProvider.getSubCountyDto();

        //when
        when(dataService.createSubCounty(subcounty)).thenReturn(subcounty);
        when(modelMapper.map(subCountyDTO, Subcounty.class)).thenReturn(subcounty);

        //then
        SuccessResponse<SubCountyDTO> actualSubCounty = dataController.createSubCounty(subCountyDTO);
        assertNotNull(actualSubCounty);
        assertEquals(HttpStatus.OK, actualSubCounty.getStatusCode());
    }

    @Test
    void testGetCountryById() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        CountryOrganizationDTO countryOrganizationDTO = TestDataProvider.getCountryOrganizationDTO();

        //when
        when(dataService.getCountryById(commonRequestDTO)).thenReturn(countryOrganizationDTO);

        //then
        SuccessResponse<CountryOrganizationDTO> actualCountryOrganization = dataController
                .getCountryById(commonRequestDTO);
        assertNotNull(actualCountryOrganization);
        assertEquals(HttpStatus.OK, actualCountryOrganization.getStatusCode());
    }

    @Test
    void testUpdateSubCountry() {
        //given
        Subcounty subcounty = TestDataProvider.getSubCounty();
        SubCountyDTO subCountyDTO = TestDataProvider.getSubCountyDto();

        //when
        when(dataService.updateSubCounty(subcounty)).thenReturn(subcounty);
        when(modelMapper.map(subCountyDTO, Subcounty.class)).thenReturn(subcounty);

        //then
        SuccessResponse<Subcounty> actualSubCounty = dataController.updateSubCountry(subCountyDTO);
        assertNotNull(actualSubCounty);
        assertEquals(HttpStatus.OK, actualSubCounty.getStatusCode());
    }

    @Test
    void testGetSubCountyById() {
        //given
        Subcounty subcounty = TestDataProvider.getSubCounty();

        //when
        when(dataService.getSubCountyById(TestConstants.ONE)).thenReturn(subcounty);

        //then
        Subcounty actualSubCounty = dataController.getSubCountyById(TestConstants.ONE);
        assertNotNull(actualSubCounty);
        assertEquals(subcounty, actualSubCounty);
    }

    @Test
    void testGetAllSubCounty() {
        //given
        List<Subcounty> subCounties = TestDataProvider.getSubCounties();

        //when
        when(dataService.getAllSubCounty(TestConstants.ONE, TestConstants.ONE)).thenReturn(subCounties);

        //then
        SuccessResponse<List<Subcounty>> actualSubcounties = dataController.getAllSubCounty(TestConstants.ONE,
                TestConstants.ONE);
        assertNotNull(actualSubcounties);
        assertEquals(HttpStatus.OK, actualSubcounties.getStatusCode());
    }

    @Test
    void testGetAllSubCountyEmpty() {
        //given
        List<Subcounty> subCounties = new ArrayList<>();

        //when
        when(dataService.getAllSubCounty(TestConstants.ONE, TestConstants.ONE)).thenReturn(subCounties);

        //then
        SuccessResponse<List<Subcounty>> actualSubCounties = dataController.getAllSubCounty(TestConstants.ONE,
                TestConstants.ONE);
        assertNotNull(actualSubCounties);
        assertEquals(HttpStatus.OK, actualSubCounties.getStatusCode());
    }

    @Test
    void testGetCountryList() {
        //given
        RequestDTO requestDto = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        List<Country> countries = TestDataProvider.getCountries();
        Map<String, Object> response = new HashMap<>();
        response.put(Constants.DATA, countries);
        response.put(Constants.COUNT, Constants.TWO);

        //when
        when(dataService.getCountryList(requestDto)).thenReturn(response);

        //then
        SuccessResponse<List<CountryListDTO>> actualCountryList = dataController.getCountryList(requestDto);
        assertNotNull(actualCountryList);
        assertEquals(HttpStatus.OK, actualCountryList.getStatusCode());
    }

    @Test
    void testGetEmptyCountryList() {
        //given
        RequestDTO requestDto = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        Map<String, Object> response = new HashMap<>();

        //when
        when(dataService.getCountryList(requestDto)).thenReturn(response);

        //then
        SuccessResponse<List<CountryListDTO>> actualCountryList = dataController.getCountryList(requestDto);
        assertNotNull(actualCountryList);
        assertEquals(HttpStatus.OK, actualCountryList.getStatusCode());
    }

    @Test
    void testGetAllSubCountyByCountryId() {
        //given
        List<Subcounty> subcounties = TestDataProvider.getSubCounties();

        //when
        when(dataService.getAllSubCountyByCountryId(TestConstants.ONE)).thenReturn(subcounties);

        //then
        List<Subcounty> actualSubcounties = dataController.getAllSubCountyByCountryId(TestConstants.ONE);
        assertNotNull(actualSubcounties);
        assertEquals(subcounties.size(), actualSubcounties.size());
    }

    @Test
    void testGetCountry() {
        //given
        Country country = TestDataProvider.getCountry();

        //when
        when(dataService.findCountryById(TestConstants.ONE)).thenReturn(country);

        //then
        Country actualCountry = dataController.getCountry(TestConstants.ONE);
        assertNotNull(actualCountry);
        assertEquals(country.getName(), actualCountry.getName());
    }

    @Test
    void testAddRegionAdmin() {
        //given
        User user = TestDataProvider.getUser();
        UserDetailsDTO userDTO = TestDataProvider.getUserDetailsDTO();

        //when
        when(dataService.addRegionAdmin(user)).thenReturn(user);

        //then
        SuccessResponse<User> addedRegionAdmin = dataController.addRegionAdmin(userDTO);
        assertNotNull(addedRegionAdmin);
        assertEquals(HttpStatus.CREATED, addedRegionAdmin.getStatusCode());
    }

    @Test
    void testUpdateRegionAdmin() {
        //given
        User user = TestDataProvider.getUser();
        UserDetailsDTO userDTO = TestDataProvider.getUserDetailsDTO();

        //when
        when(dataService.updateRegionAdmin(user)).thenReturn(user);

        //then
        SuccessResponse<User> updatedRegionAdmin = dataController.updateRegionAdmin(userDTO);
        assertNotNull(updatedRegionAdmin);
        assertEquals(HttpStatus.OK, updatedRegionAdmin.getStatusCode());
    }

    @Test
    void testRemoveRegionAdmin() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);

        //when
        when(dataService.deleteRegionAdmin(commonRequestDTO)).thenReturn(Boolean.TRUE);

        //then
        SuccessResponse<User> removedRegionAdmin = dataController.removeRegionAdmin(commonRequestDTO);
        assertNotNull(removedRegionAdmin);
        assertEquals(HttpStatus.OK, removedRegionAdmin.getStatusCode());
    }

    @Test
    void testGetCountries() {
        //given
        List<Country> countries = TestDataProvider.getCountries();

        //when
        when(dataService.getAllCountries(Boolean.TRUE)).thenReturn(countries);

        //then
        SuccessResponse<List<Country>> actualCountries = dataController.getCountries(Boolean.TRUE);
        assertNotNull(actualCountries);
        assertEquals(HttpStatus.OK, actualCountries.getStatusCode());
    }

    @Test
    void testGetEmptyCountries() {
        //given
        List<Country> countries = new ArrayList<>();

        //when
        when(dataService.getAllCountries(Boolean.TRUE)).thenReturn(countries);

        //then
        SuccessResponse<List<Country>> actualCountries = dataController.getCountries(Boolean.TRUE);
        assertNotNull(actualCountries);
        assertEquals(HttpStatus.OK, actualCountries.getStatusCode());
    }

    @Test
    void testDeactivateRegion() {
        //when
        when(dataService.activateOrInactiveRegion(TestConstants.ONE, Boolean.FALSE)).thenReturn(Boolean.TRUE);

        //then
        SuccessResponse deactivatedRegion = dataController.deactivateRegion(TestConstants.ONE);
        assertNotNull(deactivatedRegion);
        assertEquals(HttpStatus.OK, deactivatedRegion.getStatusCode());
    }

    @Test
    void testGetAllSubCountyByCountyId() {
        //given
        List<Subcounty> subCounties = TestDataProvider.getSubCounties();

        //when
        when(dataService.getAllSubCountyByCountyId(TestConstants.ONE)).thenReturn(subCounties);

        //then
        SuccessResponse<List<Subcounty>> actualSubCounties = dataController.getAllSubCountyByCountyId(TestConstants.ONE);
        assertNotNull(actualSubCounties);
        assertEquals(HttpStatus.OK, actualSubCounties.getStatusCode());
    }

    @Test
    void testGetAllSubCountyByCountyIdNull() {
        //when
        when(dataService.getAllSubCountyByCountyId(TestConstants.ONE)).thenReturn(null);

        //then
        SuccessResponse<List<Subcounty>> actualSubCounties = dataController
                .getAllSubCountyByCountyId(TestConstants.ONE);
        assertNotNull(actualSubCounties);
        assertEquals(HttpStatus.OK, actualSubCounties.getStatusCode());
    }

    @Test
    void healthCheck() {
        //then
        SuccessResponse<Boolean> result = dataController.healthCheck();
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}