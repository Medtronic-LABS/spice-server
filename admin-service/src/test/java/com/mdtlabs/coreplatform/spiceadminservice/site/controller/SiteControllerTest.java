package com.mdtlabs.coreplatform.spiceadminservice.site.controller;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteUserDTO;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceadminservice.site.service.impl.SiteServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Site controller.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class SiteControllerTest {

    @InjectMocks
    private SiteController siteController;

    @Mock
    private SiteServiceImpl siteService;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(SiteController.class, "modelMapper", siteController);
    }

    @Test
    void testAddSite() {
        //given
        Site site = TestDataProvider.getSite();
        SiteDTO siteDTO = TestDataProvider.getSiteDTO();

        //when
        when(siteService.addSite(site)).thenReturn(site);
        when(modelMapper.map(siteDTO, Site.class)).thenReturn(site);

        //then
        Site addedSite = siteController.addSite(siteDTO);
        assertNotNull(addedSite);
        assertEquals(site.getCountryId(), addedSite.getCountryId());
    }

    @Test
    void testUpdateSite() {
        //given
        Site site = TestDataProvider.getSite();
        site.setId(TestConstants.ONE);
        SiteDTO siteDTO = TestDataProvider.getSiteDTO();

        //when
        when(siteService.updateSite(site)).thenReturn(site);
        when(modelMapper.map(siteDTO, Site.class)).thenReturn(site);

        //then
        SuccessResponse<Site> updatedSite = siteController.updateSite(siteDTO);
        assertNotNull(updatedSite);
        assertEquals(HttpStatus.OK, updatedSite.getStatusCode());
    }

    @Test
    void testGetSiteList() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        ResponseListDTO response = TestDataProvider.getResponseListDTO();
        response.setTotalCount(TestConstants.ONE);

        //when
        when(siteService.getSites(requestDTO)).thenReturn(response);

        //then
        SuccessResponse<List<SiteListDTO>> actualSites = siteController.getSiteList(requestDTO);
        assertNotNull(actualSites);
        assertEquals(HttpStatus.OK, actualSites.getStatusCode());
    }

    @Test
    void testGetSitesByOperatingUnitId() {
        //given
        List<Site> sites = TestDataProvider.getSiteList();

        //when
        when(siteService.getSitesByOperatingUnitId(TestConstants.ONE)).thenReturn(sites);

        //then
        List<Site> actualSites = siteController.getSitesByOperatingUnitId(TestConstants.ONE);
        assertNotNull(actualSites);
        assertEquals(sites.size(), actualSites.size());
        assertFalse(actualSites.isEmpty());
    }

    @Test
    void testGetSiteDetails() {
        //given
        SiteDetailsDTO siteDetailsDTO = TestDataProvider.getSiteDetailsDTO();
        CommonRequestDTO requestDto = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);

        //when
        when(siteService.getSiteDetails(requestDto)).thenReturn(siteDetailsDTO);

        //then
        SuccessResponse<SiteDetailsDTO> actualSites = siteController.getSiteDetails(requestDto);
        assertNotNull(actualSites);
        assertEquals(HttpStatus.OK, actualSites.getStatusCode());
    }

    @Test
    void testAddAccountAdmin() {
        //given
        SiteUserDTO siteUserDTO = TestDataProvider.getSiteUserDTO();
        User user = TestDataProvider.getUser();

        //when
        when(siteService.addSiteAdmin(siteUserDTO)).thenReturn(user);

        //then
        SuccessResponse<User> addedAccountAdmin = siteController.addSiteUser(siteUserDTO);
        assertNotNull(addedAccountAdmin);
        assertEquals(HttpStatus.CREATED, addedAccountAdmin.getStatusCode());
    }

    @Test
    void testGetSiteById() {
        //given
        Site site = TestDataProvider.getSite();

        //when
        when(siteService.getSiteById(TestConstants.ONE)).thenReturn(site);

        //then
        ResponseEntity<Site> actualSite = siteController.getSiteById(TestConstants.ONE);
        assertNotNull(actualSite);
        assertEquals(HttpStatus.OK, actualSite.getStatusCode());
    }

    @Test
    void testUpdateAccountAdmin() {
        //given
        SiteUserDTO siteUserDTO = TestDataProvider.getSiteUserDTO();
        User user = TestDataProvider.getUser();

        //when
        when(siteService.updateSiteAdmin(siteUserDTO)).thenReturn(user);

        //then
        SuccessResponse<User> updatedAccountAdmin = siteController.updateAccountAdmin(siteUserDTO);
        assertNotNull(updatedAccountAdmin);
        assertEquals(HttpStatus.OK, updatedAccountAdmin.getStatusCode());
    }

    @Test
    void testDeleteAccountAdmin() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);

        //when
        when(siteService.deleteSiteAdmin(commonRequestDTO)).thenReturn(Boolean.TRUE);

        //then
        SuccessResponse<User> deletedAccountAdmin = siteController.deleteAccountAdmin(commonRequestDTO);
        assertNotNull(deletedAccountAdmin);
        assertEquals(HttpStatus.OK, deletedAccountAdmin.getStatusCode());
    }

    @Test
    void testGetSitesByTenantIds() {
        //given
        List<Long> tenants = List.of(TestConstants.ONE, TestConstants.TWO);
        List<Site> sites = TestDataProvider.getSiteList();

        //when
        when(siteService.getSitesByTenantIds(tenants)).thenReturn(sites);

        //then
        List<Site> actualSites = siteController.getSitesByTenantIds(tenants);
        assertNotNull(actualSites);
        assertEquals(sites.size(), actualSites.size());
    }

    @Test
    void testGetSiteByCountry() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        List<Site> sites = TestDataProvider.getSiteList();

        //when
        when(siteService.getSiteByCountry(commonRequestDTO)).thenReturn(sites);

        //then
        SuccessResponse<List<Site>> actualSites = siteController.getSiteByCountry(commonRequestDTO);
        assertNotNull(actualSites);
        assertEquals(HttpStatus.OK, actualSites.getStatusCode());
    }

    @Test
    void testGetCitiesList() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        List<Map<String, String>> cities = List.of(Map.of(TestConstants.CITY_NAME, TestConstants.CITY_NAME));

        //when
        when(siteService.getCitiesList(requestDTO)).thenReturn(cities);

        //then
        List<Map<String, String>> actualCities = siteController.getCitiesList(requestDTO);
        assertNotNull(actualCities);
        assertFalse(actualCities.isEmpty());
        assertEquals(TestConstants.ONE, actualCities.size());
        assertEquals(cities.size(), actualCities.size());
    }

    @Test
    void testGetCityCoordinates() {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDto(Boolean.TRUE, TestConstants.ONE);
        Map<String, Object> cityCoordinates = Map.of(TestConstants.CITY_NAME, TestConstants.CITY_NAME);

        //when
        when(siteService.getCityCoordinates(requestDTO)).thenReturn(cityCoordinates);

        //then
        Map<String, Object> actualCityCoordinates = siteController.getCityCoordinates(requestDTO);
        assertNotNull(actualCityCoordinates);
        assertFalse(actualCityCoordinates.isEmpty());
        assertEquals(TestConstants.ONE, actualCityCoordinates.size());
        assertEquals(cityCoordinates.size(), actualCityCoordinates.size());
    }
}
