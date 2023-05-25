package com.mdtlabs.coreplatform.userservice.controller;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteUserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserDetailsDTO;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.userservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.userservice.service.impl.OrganizationServiceImpl;
import com.mdtlabs.coreplatform.userservice.util.TestConstants;
import com.mdtlabs.coreplatform.userservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.InheritingConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * <p>
 * Organization Controller Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Jan 25, 2023
 */
@ExtendWith(MockitoExtension.class)
class OrganizationControllerTest {

    @InjectMocks
    private OrganizationController organizationController;

    @Mock
    private OrganizationServiceImpl organizationService;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private Organization organization = TestDataProvider.getOrganization();
    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestDataProvider.setUp(OrganizationController.class, "modelMapper", organizationController);
    }

    @Test
    void testUpdateOrganization() {
        //given
        organization.setName(TestConstants.ORGANIZATION_NAME);
        OrganizationDTO organizationDTO = TestDataProvider.getOrganizationDTO();

        //when
        when(organizationService.updateOrganization(organization)).thenReturn(organization);
        when(modelMapper.map(organizationDTO, Organization.class)).thenReturn(organization);

        //then
        SuccessResponse<Organization> successResponse = organizationController
                .updateOrganization(organizationDTO);
        Assertions.assertEquals(TestConstants.ORGANIZATION_NAME,
                organizationService.updateOrganization(organization).getName());
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void toVerifyUpdateOrganization() {
        //given
        Organization newOrganization = null;
        OrganizationDTO organizationDTO = null;

        //when
        when(organizationService.updateOrganization(newOrganization)).thenReturn(newOrganization);
        when(modelMapper.map(organizationDTO, Organization.class)).thenReturn(newOrganization);

        //then
        SuccessResponse<Organization> successResponse = organizationController
                .updateOrganization(organizationDTO);
        Assertions.assertEquals(HttpStatus.OK, successResponse.getStatusCode());
    }

    @Test
    void testGetOrganizationById() {
        //when
        when(organizationService.getOrganizationById(TestConstants.ONE)).thenReturn(organization);

        //then
        ResponseEntity<Organization> responseEntity = organizationController
                .getOrganizationById(TestConstants.ONE);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(TestConstants.ORGANIZATION_NAME,
                organizationService.getOrganizationById(TestConstants.ONE).getName());
    }

    @Test
    void testAddAdminUsers() {
        //given
        User user = TestDataProvider.getUser();
        UserDetailsDTO userDTO = TestDataProvider.getUserDetailsDTO();

        //when
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(organizationService.addAdminUsers(user, Boolean.FALSE)).thenReturn(user);
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);

        //then
        ResponseEntity<User> responseEntity = organizationController.addAdminUsers(userDTO, Boolean.FALSE);
        Assertions.assertEquals(TestConstants.FIRST_NAME, organizationService.addAdminUsers(user, Boolean.FALSE)
                .getFirstName());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateAdminUsers() {
        //given
        User user = TestDataProvider.getUser();
        UserDTO userDTO = TestDataProvider.getUserDTO();

        //when
        when(organizationService.updateAdminUsers(user)).thenReturn(user);
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);

        //then
        ResponseEntity<User> responseEntity = organizationController.updateAdminUsers(userDTO);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteAdminUser() {
        //given
        CommonRequestDTO requestDTO = TestDataProvider.getCommonRequestDTO();

        //when
        when(organizationService.deleteAdminUsers(requestDTO)).thenReturn(Boolean.TRUE);

        //then
        ResponseEntity<Boolean> responseEntity = organizationController.deleteAdminUser(requestDTO);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testActivateOrDeactivateOrganization() {
        //given
        List<Long> tenantIds = TestDataProvider.getTenantIds();

        //when
        when(organizationService.activateOrDeactivateOrganization(tenantIds, Boolean.TRUE))
                .thenReturn(Boolean.FALSE);

        //then
        Boolean isActive = organizationController.activateOrDeactivateOrganization(tenantIds,
                Boolean.TRUE);
        Assertions.assertEquals(Boolean.FALSE, isActive.booleanValue());
    }

    @ParameterizedTest
    @MethodSource("countryOrganization")
    void testCreateCountry(Long id, String name, String countryCode, String unitMeasurement, int violationSize) {
        validate();
        CountryOrganizationDTO countryOrganizationDTO = new CountryOrganizationDTO();
        countryOrganizationDTO.setId(id);
        countryOrganizationDTO.setName(name);
        countryOrganizationDTO.setCountryCode(countryCode);
        countryOrganizationDTO.setUnitMeasurement(unitMeasurement);
        Set<ConstraintViolation<CountryOrganizationDTO>> violations = validator.validate(countryOrganizationDTO);
        assertThat(violations).hasSize(violationSize);
        SuccessResponse<CountryOrganizationDTO> successResponse = organizationController
                .createCountry(countryOrganizationDTO);
        Assertions.assertEquals(HttpStatus.CREATED, successResponse.getStatusCode());
    }

    public static Stream<Arguments> countryOrganization() {
        return Stream.of(Arguments.of(TestConstants.ONE, TestConstants.COUNTRY_NAME,
                        TestConstants.COUNTRY_CODE, TestConstants.UNIT_MEASUREMENT, Constants.ZERO),
                Arguments.of(TestConstants.ONE, null, null, null, TestConstants.THREE),
                Arguments.of(TestConstants.ONE, TestConstants.COUNTRY_NAME, null, null, Constants.TWO),
                Arguments.of(TestConstants.ONE, TestConstants.COUNTRY_NAME, TestConstants.COUNTRY_CODE,
                        null, Constants.ONE));
    }

    @ParameterizedTest
    @MethodSource("accountOrganization")
    void testCreateAccount(String name, Long countryId, List<Long> clinicalWorkflow, Long parentOrganizationId,
                           int violationSize) {
        validate();
        AccountOrganizationDTO accountOrganizationDTO = new AccountOrganizationDTO();
        accountOrganizationDTO.setName(name);
        accountOrganizationDTO.setCountryId(countryId);
        accountOrganizationDTO.setClinicalWorkflow(clinicalWorkflow);
        accountOrganizationDTO.setParentOrganizationId(parentOrganizationId);
        Set<ConstraintViolation<AccountOrganizationDTO>> violations = validator.validate(accountOrganizationDTO);
        assertThat(violations).hasSize(violationSize);
        SuccessResponse<AccountOrganizationDTO> successResponse = organizationController
                .createAccount(accountOrganizationDTO);
        Assertions.assertEquals(HttpStatus.CREATED, successResponse.getStatusCode());
    }

    public static Stream<Arguments> accountOrganization() {
        return Stream.of(Arguments.of(TestConstants.NAME, TestConstants.ONE, List.of(TestConstants.ONE,
                        TestConstants.TWO), TestConstants.ONE, Constants.ZERO),
                Arguments.of(null, null, null, null, Constants.FOUR),
                Arguments.of(TestConstants.NAME, TestConstants.ONE, List.of(TestConstants.ONE,
                        TestConstants.TWO), null, Constants.ONE),
                Arguments.of(TestConstants.NAME, TestConstants.ONE, null, null, Constants.TWO));
    }


    @ParameterizedTest
    @MethodSource("operatingUnitOrganization")
    void testCreateOperatingUnit(String name, int violationSize) {
        validate();
        OperatingUnitOrganizationDTO operatingUnitOrganizationDTO = new OperatingUnitOrganizationDTO();
        operatingUnitOrganizationDTO.setName(name);
        Set<ConstraintViolation<OperatingUnitOrganizationDTO>> violations = validator
                .validate(operatingUnitOrganizationDTO);
        assertThat(violations).hasSize(violationSize);
        SuccessResponse<OperatingUnitOrganizationDTO> successResponse = organizationController
                .createOperatingUnit(operatingUnitOrganizationDTO);
        Assertions.assertEquals(HttpStatus.CREATED, successResponse.getStatusCode());
    }

    public static Stream<Arguments> operatingUnitOrganization() {
        return Stream.of(Arguments.of(TestConstants.NAME, Constants.ZERO),
                Arguments.of(null, Constants.ONE));
    }

    @ParameterizedTest
    @MethodSource("siteOrganization")
    void testCreateSite(String name, String addressType, String addressUse, String address1, String latitude,
                        String longitude, String city, String phoneNumber, String siteType, Long countryId,
                        Long countyId, Long subCountyId, Long accountId, Operatingunit operatingunit,
                        Long cultureId, String postalCode, int violationSize) {
        validate();
        SiteOrganizationDTO siteOrganizationDTO = new SiteOrganizationDTO();
        siteOrganizationDTO.setName(name);
        siteOrganizationDTO.setAddressType(addressType);
        siteOrganizationDTO.setAddressUse(addressUse);
        siteOrganizationDTO.setAddress1(address1);
        siteOrganizationDTO.setLatitude(latitude);
        siteOrganizationDTO.setLongitude(longitude);
        siteOrganizationDTO.setCity(city);
        siteOrganizationDTO.setPhoneNumber(phoneNumber);
        siteOrganizationDTO.setSiteType(siteType);
        siteOrganizationDTO.setCountryId(countryId);
        siteOrganizationDTO.setCountyId(countyId);
        siteOrganizationDTO.setSubCountyId(subCountyId);
        siteOrganizationDTO.setAccountId(accountId);
        siteOrganizationDTO.setOperatingUnit(operatingunit);
        siteOrganizationDTO.setCultureId(cultureId);
        siteOrganizationDTO.setPostalCode(postalCode);
        Set<ConstraintViolation<SiteOrganizationDTO>> violations = validator.validate(siteOrganizationDTO);
        assertThat(violations).hasSize(violationSize);
        SuccessResponse<SiteOrganizationDTO> successResponse = organizationController
                .createSite(siteOrganizationDTO);
        Assertions.assertEquals(HttpStatus.CREATED, successResponse.getStatusCode());
    }

    public static Stream<Arguments> siteOrganization() {
        Operatingunit operatingunit = new Operatingunit();
        operatingunit.setName(TestConstants.NAME);
        return Stream.of(Arguments.of(TestConstants.NAME, TestConstants.ADDRESS_TYPE,
                        TestConstants.ADDRESS_USE, TestConstants.ADDRESS1, TestConstants.LATITUDE,
                        TestConstants.LONGITUDE, TestConstants.CITY, TestConstants.PHONE_NUMBER,
                        TestConstants.SITE_TYPE, TestConstants.ONE, TestConstants.ONE, TestConstants.ONE,
                        TestConstants.ONE, operatingunit, TestConstants.ONE, TestConstants.POSTAL_CODE, Constants.ZERO),
                Arguments.of(null, null, null, null, null, null, null, null, null, TestConstants.ZERO,
                        TestConstants.ZERO, TestConstants.ZERO, TestConstants.ZERO, null, TestConstants.ZERO, null, 8));
    }

    @Test
    void testUpdateSiteUsers() {
        //given
        User user = TestDataProvider.getUser();
        SiteUserDTO siteUserDTO = TestDataProvider.getSiteUserDTO();

        //when
        when(organizationService.updateSiteUser(siteUserDTO)).thenReturn(user);

        //then
        ResponseEntity<User> responseEntity = organizationController.updateSiteUsers(siteUserDTO);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testOrganizationByFormDataIdAndName() {
        //given
        String formName = organization.getFormName();

        //when
        when(organizationService.getOrganizationByFormDataIdAndName(organization.getId(), organization.getFormName()))
                .thenReturn(organization);

        //then
        ResponseEntity<Organization> responseEntity = organizationController
                .getOrganizationByFormDataIdAndName(TestConstants.ONE, formName);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    private void validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
}