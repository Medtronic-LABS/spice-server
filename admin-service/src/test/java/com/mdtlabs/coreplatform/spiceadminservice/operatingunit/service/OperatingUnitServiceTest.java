package com.mdtlabs.coreplatform.spiceadminservice.operatingunit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import com.mdtlabs.coreplatform.common.exception.DataConflictException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ParentOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.spiceadminservice.UserApiInterface;
import com.mdtlabs.coreplatform.spiceadminservice.common.TestCommonMethods;
import com.mdtlabs.coreplatform.spiceadminservice.operatingunit.repository.OperatingUnitRepository;
import com.mdtlabs.coreplatform.spiceadminservice.operatingunit.service.impl.OperatingUnitServiceImpl;
import com.mdtlabs.coreplatform.spiceadminservice.site.service.SiteService;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestConstants;
import com.mdtlabs.coreplatform.spiceadminservice.util.TestDataProvider;

/**
 * <p>
 * This class has the test methods for Operating unit service implementation.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OperatingUnitServiceTest {

    @InjectMocks
    private OperatingUnitServiceImpl operatingUnitService;

    @Mock
    private OperatingUnitRepository operatingUnitRepository;

    @Mock
    private SiteService siteService;

    @Mock
    private UserApiInterface userApiInterface;

    @Mock
    private RedisTemplate<String, List<Organization>> redisTemplate;

    private ModelMapper modelMapper;

    @BeforeEach
    private void setUp() throws NoSuchFieldException, IllegalAccessException {
        modelMapper = TestCommonMethods.setUp(OperatingUnitServiceImpl.class, "modelMapper", operatingUnitService);
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = {"", "a"})
    void testGetOperatingUnitList(String searchTerm) {
        //given
        RequestDTO requestDTO = TestDataProvider.getRequestDtoForPagination(searchTerm, Constants.ZERO,
                TestConstants.TEN);
        requestDTO.setTenantId(TestConstants.ONE);
        String formattedSearchTerm = "a".replaceAll(Constants.SEARCH_TERM, Constants.EMPTY);
        ResponseListDTO responseListDTO = TestDataProvider.getResponseListDTO();
        Organization organization = TestDataProvider.getOrganization();
        organization.setFormName(Constants.COUNTRY);
        ResponseEntity<Organization> expectedOrganization = new ResponseEntity<>(organization, HttpStatus.OK);
        Page<Operatingunit> operatingunits = new PageImpl<>(TestDataProvider.getOperatingUnits());
        responseListDTO.setTotalCount(operatingunits.getTotalElements());
        Pageable pageable = PageRequest.of(Constants.ZERO, TestConstants.TEN);

        List<Long> operatingUnitIds = List.of(TestConstants.ONE, TestConstants.TWO);
        Map<Long, Long> sitesCount = Map.of(1L, 1L, 2L, 1L);

        //when
        TestCommonMethods.init();
        TestCommonMethods.getStaticMock();
        when(userApiInterface.getOrganizationById(TestConstants.TEST_TOKEN, TestConstants.ONE, TestConstants.ONE))
                .thenReturn(expectedOrganization);
        when(operatingUnitRepository.findOperatingUnits(formattedSearchTerm, TestConstants.ONE, null, pageable))
                .thenReturn(operatingunits);
        when(operatingUnitRepository.findOperatingUnits(searchTerm, TestConstants.ONE, null, pageable))
                .thenReturn(operatingunits);
        when(siteService.getSiteCountByOperatingUnitIds(operatingUnitIds)).thenReturn(sitesCount);

        //then
        ResponseListDTO actualAccount = operatingUnitService.getOperatingUnitList(requestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(actualAccount);
        assertEquals(Constants.TWO, actualAccount.getTotalCount());
    }

    @Test
    void testAddOperatingUnitAdmin() {
        //given
        User user = TestDataProvider.getUser();
        Role role = new Role();
        role.setName(Constants.ROLE_OPERATING_UNIT_ADMIN);
        user.setRoles(Set.of(role));
        ResponseEntity<User> userResponseEntity = new ResponseEntity<>(user, HttpStatus.OK);
        UserDetailsDTO userDTO = TestDataProvider.getUserDetailsDTO();
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(modelMapper.map(user, UserDetailsDTO.class)).thenReturn(userDTO);
        when(userApiInterface.addAdminUser(TestConstants.TEST_TOKEN, TestConstants.ONE, userDTO, Boolean.FALSE))
                .thenReturn(userResponseEntity);

        //then
        User actualUser = operatingUnitService.addOperatingUnitAdmin(user);
        TestCommonMethods.cleanUp();
        assertNotNull(actualUser);
        assertEquals(user.getRoles().size(), actualUser.getRoles().size());
        assertEquals(user.getRoles(), actualUser.getRoles());
    }

    @Test
    void testUpdateOperatingUnitAdmin() {
        //given
        User user = TestDataProvider.getUser();
        ResponseEntity<User> userResponseEntity = new ResponseEntity<>(user, HttpStatus.OK);
        UserDTO userDTO = TestDataProvider.getUserDTO();
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(userApiInterface.updateAdminUser(TestConstants.TEST_TOKEN, TestConstants.ONE, userDTO))
                .thenReturn(userResponseEntity);

        //then
        User actualUser = operatingUnitService.updateOperatingUnitAdmin(user);
        TestCommonMethods.cleanUp();
        assertNotNull(actualUser);
        assertEquals(user.getUsername(), actualUser.getUsername());
        assertEquals(user, actualUser);
    }

    @Test
    void testDeleteOperatingUnitAdmin() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        ResponseEntity<Boolean> userResponseEntity = new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(userApiInterface.deleteAdminUser(TestConstants.TEST_TOKEN, TestConstants.ONE, commonRequestDTO))
                .thenReturn(userResponseEntity);

        //then
        Boolean actualUser = operatingUnitService.deleteOperatingUnitAdmin(commonRequestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(actualUser);
        assertTrue(actualUser);
    }

    @ParameterizedTest(name = "#{index} - Run test with args={0}")
    @ValueSource(strings = {"", "a"})
    void testGetAllOperatingUnits(String searchTerm) {
        //given
        SearchRequestDTO searchRequestDTO = TestDataProvider.getSearchRequestDto(searchTerm, Constants.ZERO,
                TestConstants.TEN);
        String formattedSearchTerm = "a".strip();
        searchRequestDTO.setTenantId(TestConstants.ONE);
        ResponseListDTO responseListDTO = TestDataProvider.getResponseListDTO();
        Organization organization = TestDataProvider.getOrganization();
        organization.setFormName(Constants.COUNTRY);
        ResponseEntity<Organization> expectedOrganization = new ResponseEntity<>(organization, HttpStatus.OK);
        List<Operatingunit> operatingunits = TestDataProvider.getOperatingUnits();
        Page<Operatingunit> operatingunitsPage = new PageImpl<>(operatingunits);
        responseListDTO.setTotalCount(operatingunitsPage.getTotalElements());
        Pageable pageable = PageRequest.of(Constants.ZERO, TestConstants.TEN, Sort.by(Constants.UPDATED_AT).descending());

        List<Long> operatingUnitIds = List.of(TestConstants.ONE, TestConstants.TWO);
        Map<Long, Long> sitesCount = Map.of(1L, 1L, 2L, 1L);
        List<OperatingUnitDTO> operatingUnitDTOs = TestDataProvider.getOperatingUnitDTOs();

        //when
        TestCommonMethods.init();
        TestCommonMethods.getStaticMock();
        TestCommonMethods.getStaticMockValidation(searchTerm);
        when(userApiInterface.getOrganizationById(TestConstants.TEST_TOKEN, TestConstants.ONE, TestConstants.ONE))
                .thenReturn(expectedOrganization);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(operatingunits.get(0), OperatingUnitDTO.class)).thenReturn(operatingUnitDTOs.get(0));
        when(modelMapper.map(operatingunits.get(1), OperatingUnitDTO.class)).thenReturn(operatingUnitDTOs.get(1));
        when(operatingUnitRepository.getOperatingUnits(formattedSearchTerm, TestConstants.ONE, null, pageable))
                .thenReturn(operatingunitsPage);
        when(operatingUnitRepository.getOperatingUnits(null, TestConstants.ONE, null, pageable))
                .thenReturn(operatingunitsPage);
        when(siteService.getSiteCountByOperatingUnitIds(operatingUnitIds)).thenReturn(sitesCount);

        //then
        ResponseListDTO actualAccounts = operatingUnitService.getAllOperatingUnits(searchRequestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(actualAccounts);
        assertEquals(operatingUnitDTOs, actualAccounts.getData());
        assertEquals(Constants.TWO, actualAccounts.getTotalCount());
    }

    @Test
    void testActivateOrDeactivateOperatingUnits() {
        //given
        List<Operatingunit> operatingunits = TestDataProvider.getOperatingUnits();
        operatingunits.get(0).setActive(Boolean.TRUE);
        operatingunits.get(1).setActive(Boolean.TRUE);
        List<Long> tenantIds = List.of(TestConstants.ONE, TestConstants.TWO);

        //when
        when(operatingUnitRepository.findByCountryIdAndAccountIdAndIsActive(
                TestConstants.ONE, TestConstants.ONE, Boolean.FALSE)).thenReturn(operatingunits);
        when(operatingUnitRepository.saveAll(operatingunits)).thenReturn(operatingunits);

        //then
        List<Long> actualTenantIds = operatingUnitService.activateOrDeactivateOperatingUnits(TestConstants.ONE,
                TestConstants.ONE, Boolean.TRUE);
        assertNotNull(actualTenantIds);
        assertFalse(actualTenantIds.isEmpty());
        assertEquals(tenantIds, actualTenantIds);
        assertEquals(tenantIds, actualTenantIds);
    }

    @Test
    void testActivateOrDeactivateOperatingUnit() {
        //given
        Operatingunit operatingUnit = TestDataProvider.getOperatingUnit();
        operatingUnit.setActive(Boolean.TRUE);
        List<Long> ids = List.of(TestConstants.ONE);
        List<Long> tenantIds = new ArrayList<>(ids);
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(operatingUnitRepository.findByIdAndIsDeletedFalseAndIsActive(TestConstants.ONE, Boolean.FALSE))
                .thenReturn(operatingUnit);
        when(siteService.activateOrDeactivateSites(null, null, operatingUnit.getId(), Boolean.FALSE))
                .thenReturn(ids);
        when(operatingUnitRepository.save(operatingUnit)).thenReturn(operatingUnit);
        doNothing().when(userApiInterface).activateOrDeactivateUser(TestConstants.TEST_TOKEN, TestConstants.ONE,
                tenantIds, Boolean.FALSE);

        //then
        operatingUnitService.activateOrDeactivateOperatingUnit(TestConstants.ONE, Boolean.FALSE);
        verify(operatingUnitRepository, atLeastOnce()).findByIdAndIsDeletedFalseAndIsActive(TestConstants.ONE,
                Boolean.FALSE);
        verify(siteService, atLeastOnce()).activateOrDeactivateSites(null, null, operatingUnit.getId(),
                Boolean.FALSE);
        verify(userApiInterface, atLeastOnce()).activateOrDeactivateUser(TestConstants.TEST_TOKEN, TestConstants.ONE,
                tenantIds, Boolean.FALSE);
        TestCommonMethods.cleanUp();
    }

    @Test
    void testGetOperatingUnitDetails() {
        //given
        Operatingunit operatingUnit = TestDataProvider.getOperatingUnit();
        Account account = TestDataProvider.getAccount();
        account.setId(TestConstants.ONE);
        operatingUnit.setAccount(account);
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        OperatingUnitDetailsDTO operatingUnitDetailsDTO = TestDataProvider.getOperatingUnitDetailsDTO();
        List<UserOrganizationDTO> userOrganizationDTOS = List.of(TestDataProvider.getUserOrganizationDTO());
        ParentOrganizationDTO parentOrganizationDTO = TestDataProvider.getParentOrganizationDTO();
        TestCommonMethods.init();

        //when
        when(operatingUnitRepository.findByIdAndIsActiveAndIsDeletedAndTenantId(TestConstants.ONE,
                Boolean.TRUE, Boolean.FALSE, TestConstants.ONE)).thenReturn(operatingUnit);
        when(modelMapper.getConfiguration()).thenReturn(new InheritingConfiguration());
        when(modelMapper.map(operatingUnit, OperatingUnitDetailsDTO.class)).thenReturn(operatingUnitDetailsDTO);
        TestCommonMethods.getStaticMock();
        when(userApiInterface.getUsersByTenantIds(CommonUtil.getAuthToken(), TestConstants.ONE, List.of(TestConstants.ONE)))
                .thenReturn(userOrganizationDTOS);
        when(modelMapper.map(operatingUnit.getAccount(), ParentOrganizationDTO.class)).thenReturn(parentOrganizationDTO);

        //then
        OperatingUnitDetailsDTO operatingUnitDetails = operatingUnitService.getOperatingUnitDetails(commonRequestDTO);
        TestCommonMethods.cleanUp();
        assertNotNull(operatingUnitDetails);
        assertEquals(operatingUnit.getName(), operatingUnitDetails.getName());
        assertEquals(parentOrganizationDTO, operatingUnitDetails.getAccount());
    }

    @Test
    void testCreateOperatingUnit() {
        //given
        Operatingunit operatingUnit = TestDataProvider.getOperatingUnit();
        operatingUnit.setId(TestConstants.ONE);
        operatingUnit.setName(TestConstants.OU_NAME);

        //when
        when(operatingUnitRepository
                .findByNameIgnoreCaseAndIsDeletedFalse(operatingUnit.getName().strip())).thenReturn(null);
        when(operatingUnitRepository.save(operatingUnit)).thenReturn(operatingUnit);
        when(redisTemplate.delete(Constants.ORGANIZATION_REDIS_KEY)).thenReturn(Boolean.TRUE);

        //then
        Operatingunit actualOperatingunit = operatingUnitService.createOperatingUnit(operatingUnit);
        assertNotNull(operatingUnit);
        assertEquals(operatingUnit, actualOperatingunit);
        assertEquals(TestConstants.OU_NAME, actualOperatingunit.getName());
    }

    @Test
    void testUpdateOperatingUnit() {
        //given
        Operatingunit operatingUnit = TestDataProvider.getOperatingUnit();
        operatingUnit.setId(TestConstants.ONE);
        operatingUnit.setName(TestConstants.OU_NAME);
        Organization organization = new Organization(operatingUnit.getTenantId(), operatingUnit.getName());
        TestCommonMethods.init();

        //when
        TestCommonMethods.getStaticMock();
        when(operatingUnitRepository
                .findByIdAndIsDeletedFalseAndIsActive(operatingUnit.getId(), Constants.BOOLEAN_TRUE))
                .thenReturn(operatingUnit);
        when(operatingUnitRepository.existsByNameIgnoreCaseAndIsDeletedFalseAndIdNot(operatingUnit.getName(),
                operatingUnit.getId())).thenReturn(Boolean.FALSE);
        when(userApiInterface.updateOrganization(TestConstants.TEST_TOKEN, TestConstants.ONE,
                new OrganizationDTO())).thenReturn(organization);
        when(operatingUnitRepository.save(operatingUnit)).thenReturn(operatingUnit);

        //then
        Operatingunit actualOperatingunit = operatingUnitService.updateOperatingUnit(operatingUnit);
        TestCommonMethods.cleanUp();
        assertNotNull(operatingUnit);
        assertEquals(operatingUnit, actualOperatingunit);
        assertEquals(TestConstants.OU_NAME, actualOperatingunit.getName());
    }

    @Test
    void testGetOperatingUnitCountByAccountIds() {
        //given
        List<Long> ids = List.of(TestConstants.ONE);
        List<Map<String, Object>> response = List.of(Map.of("1", Constants.ONE));

        //when
        when(operatingUnitRepository.getOperatingUnitCountByAccountIds(ids)).thenReturn(response);

        //then
        Map<Long, Long> actualResponse = operatingUnitService.getOperatingUnitCountByAccountIds(ids);
        assertNotNull(actualResponse);
        assertFalse(actualResponse.isEmpty());
        assertEquals(Constants.ONE, actualResponse.size());
    }

    @Test
    void testGetOperatingUnitCountByCountryIds() {
        //given
        List<Long> ids = List.of(TestConstants.ONE);
        List<Map<String, Object>> response = List.of(Map.of("1", Constants.ONE));

        //when
        when(operatingUnitRepository.getOperatingUnitCountByCountryIds(ids)).thenReturn(response);

        //then
        Map<Long, Long> actualResponse = operatingUnitService.getOperatingUnitCountByCountryIds(ids);
        assertNotNull(actualResponse);
        assertFalse(actualResponse.isEmpty());
        assertEquals(Constants.ONE, actualResponse.size());
    }

    @Test
    void throwDataConflictException() {
        //given
        Operatingunit operatingUnit = TestDataProvider.getOperatingUnit();
        operatingUnit.setId(TestConstants.ONE);
        operatingUnit.setName(TestConstants.OU_NAME);

        //when
        when(operatingUnitRepository
                .findByNameIgnoreCaseAndIsDeletedFalse(operatingUnit.getName().strip())).thenReturn(operatingUnit);
        when(operatingUnitRepository
                .findByIdAndIsDeletedFalseAndIsActive(operatingUnit.getId(), Constants.BOOLEAN_TRUE)).thenReturn(operatingUnit);
        when(operatingUnitRepository.existsByNameIgnoreCaseAndIsDeletedFalseAndIdNot(operatingUnit.getName(), operatingUnit.getId())).thenReturn(Boolean.TRUE);

        //then
        assertThrows(DataConflictException.class, () -> operatingUnitService.createOperatingUnit(operatingUnit));
        assertThrows(DataConflictException.class, () -> operatingUnitService.updateOperatingUnit(operatingUnit));
    }

    @Test
    void throwDataNotFoundException() {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        Operatingunit operatingunit = TestDataProvider.getOperatingUnit();
        operatingunit.setId(TestConstants.ONE);

        //when
        when(operatingUnitRepository.findByIdAndIsActiveAndIsDeletedAndTenantId(commonRequestDTO.getId(),
                Constants.BOOLEAN_TRUE, Constants.BOOLEAN_FALSE, commonRequestDTO.getTenantId())).thenReturn(null);
        when(operatingUnitRepository.findByIdAndIsDeletedFalseAndIsActive(TestConstants.ONE, Boolean.FALSE))
                .thenReturn(null);
        when(operatingUnitRepository
                .findByIdAndIsDeletedFalseAndIsActive(operatingunit.getId(), Constants.BOOLEAN_TRUE)).thenReturn(null);

        //then
        assertThrows(DataNotFoundException.class, () -> operatingUnitService
                .activateOrDeactivateOperatingUnit(TestConstants.ONE, Boolean.FALSE));
        assertThrows(DataNotFoundException.class, () -> operatingUnitService
                .activateOrDeactivateOperatingUnit(TestConstants.ONE, Boolean.FALSE));
        assertThrows(DataNotFoundException.class, () -> operatingUnitService
                .getOperatingUnitDetails(commonRequestDTO));
        assertThrows(DataNotFoundException.class, () -> operatingUnitService
                .updateOperatingUnit(operatingunit));
    }

    @ParameterizedTest
    @CsvSource({"1,", ",1", ","})
    void throwDataNotAcceptableException(Long id, Long tenantId) {
        //given
        CommonRequestDTO commonRequestDTO = TestDataProvider.getCommonRequestDTO(TestConstants.ONE, TestConstants.ONE);
        commonRequestDTO.setTenantId(tenantId);
        commonRequestDTO.setId(id);

        //then
        assertThrows(DataNotAcceptableException.class, () -> operatingUnitService
                .getOperatingUnitDetails(commonRequestDTO));
    }
}