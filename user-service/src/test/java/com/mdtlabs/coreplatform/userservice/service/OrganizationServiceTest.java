package com.mdtlabs.coreplatform.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.fhir.FhirSiteRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountWorkflowDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteUserDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.County;
import com.mdtlabs.coreplatform.common.model.entity.Culture;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.Subcounty;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.util.UniqueCodeGenerator;
import com.mdtlabs.coreplatform.userservice.AdminApiInterface;
import com.mdtlabs.coreplatform.userservice.mapper.UserMapper;
import com.mdtlabs.coreplatform.userservice.repository.OrganizationRepository;
import com.mdtlabs.coreplatform.userservice.service.impl.OrganizationServiceImpl;
import com.mdtlabs.coreplatform.userservice.service.impl.UserServiceImpl;
import com.mdtlabs.coreplatform.userservice.util.TestConstants;
import com.mdtlabs.coreplatform.userservice.util.TestDataProvider;

/**
 * <p>
 * Organization Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 07, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrganizationServiceTest {

    @InjectMocks
    private OrganizationServiceImpl organizationService;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private RoleService roleService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AdminApiInterface adminApiInterface;

    @Mock
    private RedisTemplate<String, List<Organization>> redisTemplate;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestDataProvider.setUp(OrganizationServiceImpl.class, "modelMapper", organizationService);
    }

    @Test
    void testUpdateOrganization() {
        //given
        Organization organization = TestDataProvider.getOrganization();

        //when
        when(organizationRepository.findByIdAndIsDeletedFalse(organization.getId())).thenReturn(organization);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(organizationRepository.save(organization)).thenReturn(organization);

        //then
        Organization updatedOrganization = organizationService.updateOrganization(organization);
        Assertions.assertEquals(updatedOrganization, organization);
        Assertions.assertNotNull(organization);
    }

    @Test
    void checkNull() {
        //given
        List<Long> formdataIdList = null;
        Organization organization = TestDataProvider.getOrganization();

        //when
        when(organizationRepository.findByIdAndIsDeletedFalse(organization.getId())).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> {
            organizationService.updateOrganization(organization);
        });
        Boolean isActive = organizationService
                .activateOrDeactivateOrganization(formdataIdList, Boolean.TRUE);
        Assertions.assertEquals(Boolean.FALSE, isActive);
    }

    @Test
    void testGetOrganizationById() {
        //given
        Organization organization = TestDataProvider.getOrganization();

        //when
        when(organizationService.getOrganizationById(organization.getId())).thenReturn(organization);

        //then
        Organization organizationById = organizationService.getOrganizationById(organization.getId());
        Assertions.assertNotNull(organization);
        Assertions.assertEquals(organization.getName(), organizationById.getName());
    }

    @Test
    void toVerifyValidateParentOrganization() {
        //given
        User user = TestDataProvider.getUser();

        //then
        assertThrows(DataNotAcceptableException.class, () -> {
            organizationService
                    .validateParentOrganization(TestConstants.ONE, user);
        });
    }

    @Test
    void testValidateParentOrganization() {
        //given
        User user = TestDataProvider.getUser();

        //then
        organizationService.validateParentOrganization(TestConstants.FIVE, user);
    }

    @Test
    void testGetChildOrganizations() {
        //given
        List<Organization> childOrganization = TestDataProvider.getAllOrganization();

        //when
        when(organizationRepository.findByParentOrganizationId(TestConstants.ONE)).thenReturn(childOrganization);

        //then
        Map<String, List<Long>> childOrganizations = organizationService
                .getChildOrganizations(TestConstants.ONE, TestConstants.FORM_NAME);
        Assertions.assertEquals(Constants.THREE, childOrganizations.size());
    }

    @ParameterizedTest
    @ValueSource(strings = {Constants.COUNTRY, Constants.OPERATING_UNIT, Constants.ACCOUNT, Constants.SITE})
    void getChildOrganizations(String formName) {
        //given
        List<Organization> organizations = TestDataProvider.getAllOrganization();
        organizations.get(0).setFormName(formName);
        List<Long> childOrgIds = List.of(TestConstants.ONE);

        //when
        when(organizationRepository.findByParentOrganizationId(TestConstants.ONE)).thenReturn(organizations);
        when(organizationRepository.findByParentOrganizationIdIn(childOrgIds)).thenReturn(organizations);

        //then
        Map<String, List<Long>> actualChildOrganizationIds = organizationService
                .getChildOrganizations(TestConstants.ONE, formName);
        Assertions.assertNotNull(childOrgIds);
        Assertions.assertNotNull(actualChildOrganizationIds);
    }

    @Test
    void testAddAdminUsers() {
        //given
        Organization organization = TestDataProvider.getOrganization();
        User user = TestDataProvider.getUser();
        List<String> roleNames = user.getRoles().stream().map(Role::getName).toList();
        Set<Role> roles = TestDataProvider.getRolesSet();

        //when
        when(organizationRepository.findByIdAndIsDeletedFalse(user.getTenantId())).thenReturn(organization);
        when(roleService.getRolesByName(roleNames)).thenReturn(roles);
        when(userService.getUserById(user.getId())).thenReturn(user);
        doNothing().when(userMapper).setExistingUser(user, user, organization);
        when(userService.addUser(user)).thenReturn(user);

        //then
        User addAdminUsers = organizationService.addAdminUsers(user, Boolean.FALSE);
        Assertions.assertEquals(user.getFirstName(), addAdminUsers.getFirstName());
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(roles);
    }

    @Test
    void toVerifyAddAdminUsers() {
        //given
        User user = TestDataProvider.getUser();

        //when
        when(organizationRepository.findByIdAndIsDeletedFalse(user.getTenantId())).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> {
            organizationService.addAdminUsers(user, Boolean.FALSE);
        });
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0})
    void testAddAdminUsersWithNull(Long id) {
        //given
        Organization organization = TestDataProvider.getOrganization();
        User user = new User();
        user.setId(id);

        //when
        when(organizationRepository.findByIdAndIsDeletedFalse(user.getTenantId())).thenReturn(organization);
        when(userService.addUser(user)).thenReturn(user);

        //then
        User adminUser = organizationService.addAdminUsers(user, Boolean.FALSE);
        Assertions.assertEquals(user.getFirstName(), adminUser.getFirstName());
        Assertions.assertNull(user.getFirstName());
    }

    @Test
    void testUpdateAdminUsers() {
        //given
        Organization organization = TestDataProvider.getOrganization();
        User user = TestDataProvider.getUser();

        //when
        when(organizationRepository.findByIdAndIsDeletedFalse(user.getTenantId())).thenReturn(organization);
        when(userService.updateOrganizationUser(user, Boolean.FALSE, Boolean.FALSE)).thenReturn(user);

        //then
        User updateAdminUsers = organizationService.updateAdminUsers(user);
        Assertions.assertEquals(TestConstants.ORGANIZATION_NAME, organization.getName());
        Assertions.assertEquals(user.getFirstName(), updateAdminUsers.getFirstName());
    }

    @Test
    void toVerifyUpdateAdminUsers() {
        //given
        User user = TestDataProvider.getUser();

        //when
        when(organizationRepository.findByIdAndIsDeletedFalse(user.getTenantId())).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> {
            organizationService.updateAdminUsers(user);
        });
    }

    @Test
    void testDeleteAdminUser() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO();

        //when
        when(userService.deleteOrganizationUser(commonRequestDTO)).thenReturn(Boolean.TRUE);

        //then
        Boolean isDeleted = organizationService.deleteAdminUsers(commonRequestDTO);
        Assertions.assertEquals(Boolean.TRUE, isDeleted);
    }

    @Test
    void testActivateOrDeactivateOrganization() {
        //given
        List<Long> formdataIdList = TestDataProvider.getFormdataIdList();
        Set<Organization> organizations = TestDataProvider.getSetOrganizations();
        List<Organization> organizationList = TestDataProvider.getAllOrganization();

        //when
        when(organizationRepository.findByIsDeletedFalseAndIsActiveAndIdIn(Boolean.FALSE,
                formdataIdList)).thenReturn(organizations);
        when(organizationRepository.saveAll(organizations)).thenReturn(organizationList);

        //then
        Boolean isActive = organizationService
                .activateOrDeactivateOrganization(formdataIdList, Boolean.TRUE);
        Assertions.assertEquals(Boolean.TRUE, isActive);
        Assertions.assertFalse(organizationList.isEmpty());
    }

    @Test
    void testActivateOrDeactivateOrganizationWithNull() {
        //given
        List<Long> formdataIdList = TestDataProvider.getFormdataIdList();
        Set<Organization> organizations = TestDataProvider.getSetOrganizations();
        List<Organization> organizationList = null;

        //when
        when(organizationRepository.findByIsDeletedFalseAndIsActiveAndIdIn(Boolean.FALSE,
                formdataIdList)).thenReturn(organizations);
        when(organizationRepository.saveAll(organizations)).thenReturn(organizationList);

        //then
        Boolean isActive = organizationService
                .activateOrDeactivateOrganization(formdataIdList, Boolean.TRUE);
        Assertions.assertEquals(Boolean.FALSE, isActive);
    }

    @Test
    void toVerifyActivateOrDeactivateOrganization() {
        //given
        List<Long> formdataIdList = TestDataProvider.getFormdataIdList();
        Set<Organization> organizations = null;

        //when
        when(organizationRepository.findByIsDeletedFalseAndIsActiveAndIdIn(Boolean.TRUE,
                formdataIdList)).thenReturn(organizations);

        //then
        Boolean isActive = organizationService
                .activateOrDeactivateOrganization(formdataIdList, Boolean.FALSE);
        Assertions.assertEquals(Boolean.TRUE, isActive);
    }

    @Test
    void testUpdateSiteUser() {
        //given
        Organization organization = TestDataProvider.getOrganization();
        User user = TestDataProvider.getUser();
        SiteUserDTO siteUserDTO = TestDataProvider.getSiteUserDTO();
        user.setOrganizations(Set.of(organization));

        //when
        when(organizationRepository.findByIdAndIsDeletedFalse(siteUserDTO.getTenantId()))
                .thenReturn(organization);
        when(modelMapper.map(siteUserDTO, User.class)).thenReturn(user);
        when(userService.updateOrganizationUser(user, Boolean.TRUE, siteUserDTO.isRedRisk()))
                .thenReturn(user);

        //then
        User updateSiteUser = organizationService.updateSiteUser(siteUserDTO);
        Assertions.assertEquals(TestConstants.ONE, organization.getId());
        Assertions.assertEquals(siteUserDTO.getFirstName(), user.getFirstName());
        Assertions.assertEquals(user.getFirstName(), updateSiteUser.getFirstName());
    }

    @Test
    void toVerifyUpdateSiteUser() {
        //given
        SiteUserDTO siteUserDTO = TestDataProvider.getSiteUserDTO();

        //when
        when(organizationRepository.findByIdAndIsDeletedFalse(siteUserDTO.getTenantId()))
                .thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> {
            organizationService.updateSiteUser(siteUserDTO);
        });
    }

    @Test
    void testGetOrganizationByFormDataIdAndName() {
        //given
        Organization organization = TestDataProvider.getOrganization();

        //when
        when(organizationRepository.findByFormDataIdAndFormNameAndIsDeleted(TestConstants.ONE, TestConstants.FORM_NAME,
                Boolean.FALSE)).thenReturn(organization);

        //then
        Organization organizationByFormDataIdAndName = organizationService
                .getOrganizationByFormDataIdAndName(TestConstants.ONE, TestConstants.FORM_NAME);
        Assertions.assertEquals(organization.getName(), organizationByFormDataIdAndName.getName());
    }
    
    @Test
    void testCreateSite() {
        //given
        SiteOrganizationDTO siteOrganizationDTO = TestDataProvider.getSiteOrganizationDTO();
        Organization siteOrganization = new Organization(Constants.SITE, siteOrganizationDTO.getName(),
                siteOrganizationDTO.getParentOrganizationId(), siteOrganizationDTO.getParentOrganizationId());
        siteOrganization.setSequence(0L);
        Site site = TestDataProvider.getSite();
        site.setCulture(new Culture(siteOrganizationDTO.getCultureId()));
        site.setTenantId(siteOrganization.getId());
        List<String> newUserNames = List.of(TestConstants.USER_NAME.toLowerCase());
        Role role = TestDataProvider.getRole();
        SiteDTO siteDTO = TestDataProvider.getSiteDTO();
        User user = TestDataProvider.getUser();
        TestDataProvider.init();
        ReflectionTestUtils.setField(organizationService, "enableFhir", true);
        ReflectionTestUtils.setField(organizationService, "exchange", "exchange");
        ReflectionTestUtils.setField(organizationService, "routingKey", "key");
        Country country = TestDataProvider.getCountry();
        Subcounty subCounty = TestDataProvider.getSubcounty();
        County county = TestDataProvider.getCounty();
        FhirSiteRequestDTO siteRequestDTO = TestDataProvider.getFhirSiteRequestDTO();
        String jsonMessage = siteRequestDTO.toString();
        //when
        TestDataProvider.getStaticMock();
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(siteOrganizationDTO.getUsers().get(Constants.ZERO), User.class)).thenReturn(user);
        when(modelMapper.map(siteOrganizationDTO, SiteDTO.class)).thenReturn(siteDTO);
        when(modelMapper.map(siteOrganizationDTO, Site.class)).thenReturn(site);
        when(adminApiInterface.createSite(TestConstants.TOKEN, TestConstants.ONE, siteDTO)).thenReturn(site);
        when(adminApiInterface.getCountry(TestConstants.TOKEN,
                TestConstants.ONE, TestConstants.ONE)).thenReturn(country);
        when(adminApiInterface.getCountyById(TestConstants.TOKEN,
                TestConstants.ONE, TestConstants.ONE)).thenReturn(county);
        when(adminApiInterface.getSubCountyById(TestConstants.TOKEN,
                TestConstants.ONE, TestConstants.ONE)).thenReturn(subCounty);
        doNothing().when(userService).validateUsers(newUserNames);
        when(roleService.getRoleByName(role.getName())).thenReturn(role);
        when(organizationRepository.save(siteOrganization)).thenReturn(siteOrganization);
        MockedConstruction<ObjectMapper> objectMapperMockedConstruction =
                Mockito.mockConstruction(ObjectMapper.class, (objectMapper, context) -> {
                    when(objectMapper.writeValueAsString(any(FhirSiteRequestDTO.class))).thenReturn(jsonMessage);
                });
        MockedStatic<UniqueCodeGenerator> uniqueCodeGeneratorMockedStatic = Mockito.mockStatic(UniqueCodeGenerator.class);
        uniqueCodeGeneratorMockedStatic.when(() -> UniqueCodeGenerator.generateUniqueCode(jsonMessage)).thenReturn(Constants.DEDUPLICATION_ID);
        doNothing().when(rabbitTemplate).convertAndSend("exchange", "keyName", jsonMessage);

        //then
        organizationService.createSite(siteOrganizationDTO);
        Assertions.assertNotNull(siteOrganization);
        verify(organizationRepository, atLeastOnce()).save(siteOrganization);
        TestDataProvider.cleanUp();
        objectMapperMockedConstruction.close();
        uniqueCodeGeneratorMockedStatic.close();
    }


    @Test
    void toVerifyCreateSite() {
        //given
        SiteOrganizationDTO siteOrganizationDTO = TestDataProvider.getSiteOrganizationDTO();
        Organization siteOrganization = new Organization(Constants.SITE, siteOrganizationDTO.getName(),
                siteOrganizationDTO.getParentOrganizationId(), siteOrganizationDTO.getParentOrganizationId());
        siteOrganization.setSequence(0L);
        Site site = TestDataProvider.getSite();
        site.setCulture(new Culture(siteOrganizationDTO.getCultureId()));
        site.setTenantId(siteOrganization.getId());
        SiteDTO siteDTO = TestDataProvider.getSiteDTO();
        siteDTO.setCulture(new Culture(siteOrganizationDTO.getCultureId()));
        siteDTO.setTenantId(siteOrganization.getId());
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(organizationRepository.save(siteOrganization)).thenReturn(siteOrganization);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(siteOrganizationDTO, SiteDTO.class)).thenReturn(siteDTO);
        when(modelMapper.map(siteOrganizationDTO, Site.class)).thenReturn(site);
        when(adminApiInterface.createSite(TestConstants.TOKEN, TestConstants.ONE, siteDTO)).thenReturn(null);

        //then
        organizationService.createSite(siteOrganizationDTO);
        Assertions.assertNotNull(siteOrganization);
        verify(organizationRepository, atLeastOnce()).save(siteOrganization);
        TestDataProvider.cleanUp();
    }
    
    @Test
    void testCreateOU() {
        //given
        OperatingUnitOrganizationDTO operatingUnitOrganizationDTO = TestDataProvider.getOperatingUnitOrganizationDTO();
        Organization ouOrganization = new Organization(Constants.OPERATING_UNIT, operatingUnitOrganizationDTO.getName(),
                operatingUnitOrganizationDTO.getParentOrganizationId(),
                operatingUnitOrganizationDTO.getParentOrganizationId());
        Operatingunit operatingunit = TestDataProvider.getOperatingUnit();
        OperatingUnitDTO operatingUnitDTO = TestDataProvider.getOperatingUnitDTO();
        operatingUnitDTO.setTenantId(TestConstants.ONE);
        operatingunit.setTenantId(ouOrganization.getId());
        List<String> newUserNames = List.of(TestConstants.USER_NAME.toLowerCase());
        Role role = TestDataProvider.getRole();
        User user = TestDataProvider.getUser();
        List<User> users = TestDataProvider.getUsers();
        Map<Long, User> existingUsers = Map.of(TestConstants.ONE, user);
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(organizationRepository.save(any())).thenReturn(ouOrganization);
        when(redisTemplate.delete(Constants.ORGANIZATION_REDIS_KEY)).thenReturn(Boolean.TRUE);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(operatingUnitOrganizationDTO, OperatingUnitDTO.class)).thenReturn(operatingUnitDTO);
        when(adminApiInterface.createOperatingUnit(TestConstants.TOKEN,
                TestConstants.ONE, operatingUnitDTO)).thenReturn(operatingunit);
        when(modelMapper.map(operatingUnitOrganizationDTO.getUsers().get(Constants.ZERO), User.class)).thenReturn(user);
        doNothing().when(userService).validateUsers(newUserNames);
        when(roleService.getRoleByName(role.getName())).thenReturn(role);
        when(userService.getUserByIds(Set.of(TestConstants.ONE))).thenReturn(existingUsers);

        //then
        organizationService.createOU(operatingUnitOrganizationDTO);
        Assertions.assertNotNull(ouOrganization);
        Assertions.assertNotNull(role);
        verify(organizationRepository, atLeastOnce()).save(any());
        TestDataProvider.cleanUp();
    }

    @Test
    void toVerifyCreateOU() {
        //given
        OperatingUnitOrganizationDTO operatingUnitOrganizationDTO = TestDataProvider.getOperatingUnitOrganizationDTO();
        Organization ouOrganization = new Organization(Constants.OPERATING_UNIT, operatingUnitOrganizationDTO.getName(),
                operatingUnitOrganizationDTO.getParentOrganizationId(),
                operatingUnitOrganizationDTO.getParentOrganizationId());
        User user = TestDataProvider.getUser();
        Operatingunit operatingunit = TestDataProvider.getOperatingUnit();
        OperatingUnitDTO operatingUnitDTO = TestDataProvider.getOperatingUnitDTO();
        operatingUnitDTO.setTenantId(TestConstants.ONE);
        operatingunit.setTenantId(ouOrganization.getId());
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(organizationRepository.save(any())).thenReturn(ouOrganization);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(operatingUnitOrganizationDTO, OperatingUnitDTO.class)).thenReturn(operatingUnitDTO);
        when(modelMapper.map(operatingUnitOrganizationDTO.getUsers().get(Constants.ZERO), User.class)).thenReturn(user);
        when(adminApiInterface.createOperatingUnit(TestConstants.TOKEN,
                TestConstants.ONE, operatingUnitDTO)).thenReturn(operatingunit);

        //then
        organizationService.createOU(operatingUnitOrganizationDTO);
        Assertions.assertNotNull(ouOrganization);
        verify(organizationRepository, atLeastOnce()).save(any());
        TestDataProvider.cleanUp();
    }

    @Test
    void testCreateAccount() {
        //given
        AccountOrganizationDTO accountOrganizationDTO = TestDataProvider.getAccountOrganizationDTO();
        Organization ouOrganization = new Organization(Constants.ACCOUNT, accountOrganizationDTO.getName(),
                accountOrganizationDTO.getParentOrganizationId(), accountOrganizationDTO.getParentOrganizationId());
        AccountWorkflowDTO accountWorkflowDTO = TestDataProvider.getAccountworkFlowDto();
        accountWorkflowDTO.setTenantId(ouOrganization.getId());
        List<String> newUserNames = List.of(TestConstants.USER_NAME.toLowerCase());
        Role role = TestDataProvider.getRole();
        User user = TestDataProvider.getUser();
        List<User> users = TestDataProvider.getUsers();
        Map<Long, User> existingUsers = Map.of(TestConstants.ONE, user);
        Account account = TestDataProvider.getAccount();
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(organizationRepository.save(any())).thenReturn(ouOrganization);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(accountOrganizationDTO, AccountWorkflowDTO.class)).thenReturn(accountWorkflowDTO);
        when(modelMapper.map(accountOrganizationDTO.getUsers().get(Constants.ZERO), User.class)).thenReturn(user);
        when(adminApiInterface.createAccount(TestConstants.TOKEN, TestConstants.ONE,
                accountWorkflowDTO)).thenReturn(account);
        doNothing().when(userService).validateUsers(newUserNames);
        when(roleService.getRoleByName(role.getName())).thenReturn(role);
        when(roleService.getRolesByName(List.of(role.getName()))).thenReturn(Set.of(role));
        when(userService.getUserByIds(Set.of(TestConstants.ONE))).thenReturn(existingUsers);
        doNothing().when(userMapper).setExistingUser(user, user, ouOrganization);
        doNothing().when(userService).addUsers(users, newUserNames);

        //then
        organizationService.createAccount(accountOrganizationDTO);
        Assertions.assertNotNull(ouOrganization);
        verify(organizationRepository, atLeastOnce()).save(any());
        TestDataProvider.cleanUp();
    }

    @Test
    void testVerifyCreateAccount() {
        //given
        AccountOrganizationDTO accountOrganizationDTO = TestDataProvider.getAccountOrganizationDTO();
        Organization ouOrganization = new Organization(Constants.ACCOUNT, accountOrganizationDTO.getName(),
                accountOrganizationDTO.getParentOrganizationId(), accountOrganizationDTO.getParentOrganizationId());
        AccountWorkflowDTO accountWorkflowDTO = TestDataProvider.getAccountworkFlowDto();
        accountWorkflowDTO.setTenantId(ouOrganization.getId());
        User user = TestDataProvider.getUser();
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(organizationRepository.save(any())).thenReturn(ouOrganization);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(accountOrganizationDTO, AccountWorkflowDTO.class)).thenReturn(accountWorkflowDTO);
        when(modelMapper.map(accountOrganizationDTO.getUsers().get(Constants.ZERO), User.class)).thenReturn(user);
        when(adminApiInterface.createAccount(TestConstants.TOKEN,
                TestConstants.ONE, accountWorkflowDTO)).thenReturn(null);

        //then
        organizationService.createAccount(accountOrganizationDTO);
        Assertions.assertNotNull(ouOrganization);
        verify(organizationRepository, atLeastOnce()).save(any());
        TestDataProvider.cleanUp();
    }

    @Test
    void createCountry() {
        //given
        CountryOrganizationDTO countryOrganizationDTO = TestDataProvider.getCountryOrganizationDTO();
        Organization ouOrganization = new Organization(Constants.COUNTRY,
                countryOrganizationDTO.getName(), null, null);
        Country country = TestDataProvider.getCountry();
        CountryDTO countryDTO = TestDataProvider.getCountryDTO();
        country.setTenantId(ouOrganization.getId());
        List<String> newUserNames = List.of(TestConstants.USER_NAME.toLowerCase());
        Role role = TestDataProvider.getRole();
        User user = TestDataProvider.getUser();
        List<User> users = TestDataProvider.getUsers();
        Map<Long, User> existingUsers = Map.of(TestConstants.ONE, user);
        ResponseEntity<Country> countryResponse = new ResponseEntity<>(country, HttpStatus.OK);
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(organizationRepository.save(any())).thenReturn(ouOrganization);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(countryOrganizationDTO, CountryDTO.class)).thenReturn(countryDTO);
        when(adminApiInterface.createCountry(TestConstants.TOKEN,
                TestConstants.ONE, countryDTO)).thenReturn(countryResponse);
        when(modelMapper.map(countryOrganizationDTO.getUsers().get(Constants.ZERO), User.class)).thenReturn(user);
        doNothing().when(userService).validateUsers(newUserNames);
        when(roleService.getRoleByName(role.getName())).thenReturn(role);
        when(roleService.getRolesByName(List.of(role.getName()))).thenReturn(Set.of(role));
        when(userService.getUserByIds(Set.of(TestConstants.ONE))).thenReturn(existingUsers);
        doNothing().when(userMapper).setExistingUser(user, user, ouOrganization);
        doNothing().when(userService).addUsers(users, newUserNames);

        //then
        organizationService.createCountry(countryOrganizationDTO);
        Assertions.assertNotNull(ouOrganization);
        verify(organizationRepository, atLeastOnce()).save(any());
        TestDataProvider.cleanUp();
    }

    @Test
    void testCreateCountry() {
        //given
        CountryOrganizationDTO countryOrganizationDTO = TestDataProvider.getCountryOrganizationDTO();
        Organization ouOrganization = new Organization(Constants.COUNTRY,
                countryOrganizationDTO.getName(), null, null);
        Country country = null;
        CountryDTO countryDTO = TestDataProvider.getCountryDTO();
        ResponseEntity<Country> countryResponse = new ResponseEntity<>(country, HttpStatus.OK);
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(organizationRepository.save(any())).thenReturn(ouOrganization);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(countryOrganizationDTO, CountryDTO.class)).thenReturn(countryDTO);
        when(adminApiInterface.createCountry(TestConstants.TOKEN,
                TestConstants.ONE, countryDTO)).thenReturn(countryResponse);

        //then
        organizationService.createCountry(countryOrganizationDTO);
        Assertions.assertNotNull(ouOrganization);
        verify(organizationRepository, atLeastOnce()).save(any());
        TestDataProvider.cleanUp();
    }

    @Test
    void toVerifyCreateCountry() {
        //given
        CountryOrganizationDTO countryOrganizationDTO = TestDataProvider.getCountryOrganizationDTO();
        Organization ouOrganization = new Organization(Constants.COUNTRY,
                countryOrganizationDTO.getName(), null, null);
        Country country = TestDataProvider.getCountry();
        CountryDTO countryDTO = TestDataProvider.getCountryDTO();
        country.setTenantId(ouOrganization.getId());
        List<String> newUserNames = List.of(TestConstants.USER_NAME.toLowerCase());
        Role role = TestDataProvider.getRole();
        User user = TestDataProvider.getUser();
        User newUser = new User();
        newUser.setUsername(TestConstants.USER_NAME);
        List<User> users = new ArrayList<>();
        users.add(newUser);
        Map<Long, User> existingUsers = Map.of(TestConstants.ONE, user);
        ResponseEntity<Country> countryResponse = new ResponseEntity<>(country, HttpStatus.OK);
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(organizationRepository.save(any())).thenReturn(ouOrganization);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(countryOrganizationDTO, CountryDTO.class)).thenReturn(countryDTO);
        when(adminApiInterface.createCountry(TestConstants.TOKEN,
                TestConstants.ONE, countryDTO)).thenReturn(countryResponse);
        when(modelMapper.map(countryOrganizationDTO.getUsers().get(Constants.ZERO), User.class)).thenReturn(user);
        doNothing().when(userService).validateUsers(newUserNames);
        when(roleService.getRoleByName(Constants.ROLE_REGION_ADMIN)).thenReturn(role);
        when(userService.getUserByIds(Set.of(TestConstants.ONE))).thenReturn(existingUsers);
        doNothing().when(userService).addUsers(users, newUserNames);

        //then
        organizationService.createCountry(countryOrganizationDTO);
        Assertions.assertNotNull(ouOrganization);
        verify(organizationRepository, atLeastOnce()).save(any());
        TestDataProvider.cleanUp();
    }

    @Test
    void toVerifyAddUsers() {
        //given
        CountryOrganizationDTO countryOrganizationDTO = TestDataProvider.getCountryOrganizationDTO();
        Organization ouOrganization = new Organization(Constants.COUNTRY,
                countryOrganizationDTO.getName(), null, null);
        Country country = TestDataProvider.getCountry();
        CountryDTO countryDTO = TestDataProvider.getCountryDTO();
        country.setTenantId(ouOrganization.getId());
        List<String> newUserNames = List.of(TestConstants.USER_NAME.toLowerCase());
        Role role = TestDataProvider.getRole();
        User user = TestDataProvider.getUser();
        User newUser = new User();
        newUser.setUsername(TestConstants.USER_NAME);
        List<User> users = new ArrayList<>();
        users.add(newUser);
        Map<Long, User> existingUsers = Map.of(TestConstants.ONE, user);
        ResponseEntity<Country> countryResponse = new ResponseEntity<>(country, HttpStatus.OK);
        TestDataProvider.init();

        //when
        TestDataProvider.getStaticMock();
        when(organizationRepository
                .findByNameIgnoreCaseAndIsDeletedFalse(ouOrganization.getName().strip())).thenReturn(null);
        when(organizationRepository.save(any())).thenReturn(ouOrganization);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(countryOrganizationDTO, CountryDTO.class)).thenReturn(countryDTO);
        when(adminApiInterface.createCountry(TestConstants.TOKEN,
                TestConstants.ONE, countryDTO)).thenReturn(countryResponse);
        when(modelMapper.map(countryOrganizationDTO.getUsers().get(Constants.ZERO), User.class)).thenReturn(user);
        doNothing().when(userService).validateUsers(newUserNames);
        when(roleService.getRoleByName(Constants.ROLE_REGION_ADMIN)).thenReturn(role);
        when(userService.getUserByIds(Set.of(TestConstants.ONE))).thenReturn(existingUsers);
        doNothing().when(userService).addUsers(users, newUserNames);

        //then
        organizationService.createCountry(countryOrganizationDTO);
        Assertions.assertNotNull(ouOrganization);
        verify(organizationRepository, atLeastOnce()).save(any());
        TestDataProvider.cleanUp();
    }
    
    @Test
    void testGetOrganizationsByIds() {
        //given
        List<Organization> organizations = TestDataProvider.getAllOrganization();
        List<Long> organizationIds = TestDataProvider.getOrganizationIds();
        
        //when
        when(organizationRepository.findByIsDeletedFalseAndIdIn(organizationIds)).thenReturn(organizations);
        
        //then
        assertEquals(organizationService.getOrganizationsByIds(organizationIds), organizations);
    }
    
}