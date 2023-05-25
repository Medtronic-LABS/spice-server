package com.mdtlabs.coreplatform.userservice.util;

import static org.mockito.Mockito.mockStatic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.model.dto.EmailDTO;
import com.mdtlabs.coreplatform.common.model.dto.RoleDTO;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.UserProfileDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountWorkflowDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CultureRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PaginateDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteUserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserSuperAdminDto;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.Culture;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Role;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.util.CommonUtil;

/**
 * <p>
 * To define the common static parameter used all over the application.
 * </p>
 *
 * @author JohnKennedy created on Fed 02, 2023
 */
public class TestDataProvider {

    public static ModelMapper modelMapper = new ModelMapper();

    private static MockedStatic<CommonUtil> commonUtil;

    private static MockedStatic<UserContextHolder> userContextHolder;

    private static MockedStatic<UserSelectedTenantContextHolder> userSelectedTenantContextHolder;

    public static Organization getOrganization() {
        Organization organization = new Organization();
        organization.setId(TestConstants.ONE);
        organization.setFormName(TestConstants.FORM_NAME);
        organization.setFormDataId(TestConstants.ONE);
        organization.setName(TestConstants.ORGANIZATION_NAME);
        organization.setSequence(TestConstants.ZERO);
        organization.setTenantId(TestConstants.ONE);
        organization.setParentOrganizationId(TestConstants.FIVE);
        organization.setFormDataId(TestConstants.ONE);
        organization.setActive(Boolean.TRUE);
        return organization;
    }

    public static List<Organization> getAllOrganization() {
        List<Organization> organizations = new ArrayList<>();
        organizations.add(getOrganization());
        return organizations;
    }

    public static Set<Organization> getAllOrganizationSet() {
        Set<Organization> organizations = new HashSet<>();
        organizations.add(getOrganization());
        return organizations;
    }

    public static List<Long> getUserTenants() {
        List<Long> userTenants = new ArrayList<>();
        userTenants.add(getOrganization().getTenantId());
        return userTenants;
    }

    public static List<Long> getFormdataIdList() {
        List<Long> formdataIdList = new ArrayList<>();
        formdataIdList.add(getOrganization().getFormDataId());
        return formdataIdList;
    }

    public static Map<String, List<Long>> getChildIds() {
        Map<String, List<Long>> childIds = new HashMap<>();
        childIds.put(TestConstants.CHILD, List.of(TestConstants.ONE, TestConstants.TWO));
        return childIds;
    }

    public static Map<Long, User> getMappedUsers() {
        Map<Long, User> users = new HashMap<>();
        users.put(TestConstants.ONE, getUser());
        return users;
    }

    public static List<Long> getOrganizationIds() {
        List<Long> organizationIds = new ArrayList<>();
        organizationIds.add(getOrganization().getId());
        return organizationIds;
    }

    public static List<Long> getParentOrganizationIds() {
        List<Long> parentOrganizationIds = new ArrayList<>();
        parentOrganizationIds.add(getOrganization().getParentOrganizationId());
        return parentOrganizationIds;
    }

    public static OrganizationDTO getOrganizationDTO() {
        OrganizationDTO organizationDTO = new OrganizationDTO();
        organizationDTO.setOrganization(getOrganization());
        organizationDTO.setUsers(getUsers());
        organizationDTO.setRoles(List.of(TestConstants.NAME));
        return organizationDTO;
    }

    public static Set<Organization> getSetOrganizations() {
        Set<Organization> setOrganizations = new HashSet<>();
        setOrganizations.add(getOrganization());
        return setOrganizations;
    }

    public static AccountOrganizationDTO getAccountOrganizationDTO() {
        AccountOrganizationDTO accountOrganizationDTO = new AccountOrganizationDTO();
        accountOrganizationDTO.setId(TestConstants.ONE);
        accountOrganizationDTO.setName(TestConstants.NAME);
        accountOrganizationDTO.setCountryId(TestConstants.ONE);
        accountOrganizationDTO.setClinicalWorkflow(List.of(TestConstants.ONE, TestConstants.TWO));
        accountOrganizationDTO.setParentOrganizationId(TestConstants.ONE);
        accountOrganizationDTO.setUsers(getListUserOrganizationDTO());
        return accountOrganizationDTO;
    }

    public static UserOrganizationDTO getUserOrganizationDTO() {
        UserOrganizationDTO userOrganizationDTO = new UserOrganizationDTO();
        userOrganizationDTO.setUsername(TestConstants.USER_NAME);
        userOrganizationDTO.setFirstName(TestConstants.FIRST_NAME);
        userOrganizationDTO.setLastName(TestConstants.LAST_NAME);
        userOrganizationDTO.setPhoneNumber(TestConstants.PHONE_NUMBER);
        return userOrganizationDTO;
    }

    public static List<UserOrganizationDTO> getListUserOrganizationDTO() {
        List<UserOrganizationDTO> userOrganizationDTOS = new ArrayList<>();
        userOrganizationDTOS.add(getUserOrganizationDTO());
        return userOrganizationDTOS;
    }

    public static User getUser() {
        User user = new User();
        user.setId(TestConstants.ONE);
        user.setPassword(TestConstants.PASSWORD);
        user.setFirstName(TestConstants.FIRST_NAME);
        user.setLastName(TestConstants.LAST_NAME);
        user.setPhoneNumber(TestConstants.PHONE_NUMBER);
        user.setUsername(TestConstants.USER_NAME);
        user.setForgetPasswordCount(Constants.ONE);
        user.setForgetPasswordTime(new Date());
        user.setInvalidLoginAttempts(Constants.ONE);
        user.setTenantId(TestConstants.FIVE);
        user.setCountryCode(TestConstants.COUNTRY_CODE);
        user.setRoles(Set.of(getRole()));
        user.setOrganizations(getSetOrganizations());
        return user;
    }

    public static SiteOrganizationDTO getSiteOrganizationDTO() {
        SiteOrganizationDTO siteOrganizationDTO = new SiteOrganizationDTO();
        siteOrganizationDTO.setName("Site");
        siteOrganizationDTO.setCultureId(TestConstants.ONE);
        siteOrganizationDTO.setUsers(List.of(getSiteUserDTO()));
        siteOrganizationDTO.setParentOrganizationId(TestConstants.ONE);
        return siteOrganizationDTO;
    }

    public static List<Long> getUserTenantIds() {
        List<Long> tenantIds = new ArrayList<>();
        tenantIds.add(getUser().getTenantId());
        return tenantIds;
    }

    public static List<User> getUsers() {
        List<User> users = new ArrayList<>();
        users.add(getUser());
        return users;
    }

    public static CommonRequestDTO getCommonRequestDTO() {
        CommonRequestDTO requestDTO = new CommonRequestDTO();
        requestDTO.setId(5L);
        requestDTO.setTenantId(15L);
        requestDTO.setPatientVisitId(10L);
        requestDTO.setTenantId(TestConstants.ONE);
        return requestDTO;
    }

    public static List<Long> getTenantIds() {
        List<Long> tenantIds = new ArrayList<>();
        tenantIds.add(getOrganization().getTenantId());
        return tenantIds;
    }

    public static SiteUserDTO getSiteUserDTO() {
        SiteUserDTO siteUserDTO = new SiteUserDTO();
        siteUserDTO.setId(TestConstants.ONE);
        siteUserDTO.setFirstName(TestConstants.FIRST_NAME);
        siteUserDTO.setLastName(TestConstants.LAST_NAME);
        siteUserDTO.isRedRisk();
        return siteUserDTO;
    }

    public static Role getRole() {
        Role role = new Role();
        role.setId(TestConstants.ONE);
        role.setName(TestConstants.NAME);
        role.setLevel(TestConstants.LEVEL);
        return role;
    }

    public static RoleDTO getRoleDTO() {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(TestConstants.ONE);
        roleDTO.setName(TestConstants.NAME);
        roleDTO.setLevel(TestConstants.LEVEL);
        return roleDTO;
    }

    public static List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(getRole());
        return roles;
    }

    public static Set<Role> getRolesSet() {
        Set<Role> roles = new HashSet<>();
        roles.add(getRole());
        return roles;
    }

    public static UserDTO getUserDTO() {
        UserDTO userDTO = modelMapper.map(getUser(), UserDTO.class);
        return userDTO;
    }

    public static UserListDTO getUserListDTO() {
        UserListDTO userListDTO = modelMapper.map(getUser(), UserListDTO.class);
        return userListDTO;
    }

    public static List<UserDTO> getUserDTOS() {
        List<UserDTO> userDTOS = modelMapper.map(getUsers(), new TypeToken<List<UserDTO>>() {
        }.getType());
        return userDTOS;
    }

    public static UserResponseDTO getUserResponseDTO() {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        return userResponseDTO;

    }

    public static List<UserResponseDTO> getUserResponseDTOS() {
        UserResponseDTO userResponseDTO = getUserResponseDTO();
        userResponseDTO.setId(TestConstants.ONE);
        UserResponseDTO secondUserResponseDTO = getUserResponseDTO();
        secondUserResponseDTO.setId(TestConstants.TWO);
        return List.of(userResponseDTO, secondUserResponseDTO);
    }

    public static Map<String, String> getUserMails() {
        Map<String, String> userMails = new HashMap<>();
        userMails.put(TestConstants.EMAIL, TestConstants.USER_NAME);
        return userMails;
    }

    public static Map<String, String> getUserInfo() {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put(TestConstants.PASS_WORD, TestConstants.USER_INFO);
        return userInfo;
    }

    public static Map<String, String> getUpdatePassword() {
        Map<String, String> updatePassword = new HashMap<>();
        updatePassword.put(TestConstants.PASS_WORD, TestConstants.PASSWORD);
        return updatePassword;
    }

    public static Map<String, String> getValidateUser() {
        Map<String, String> email = new HashMap<>();
        email.put(TestConstants.EMAIL, TestConstants.USER_NAME);
        email.put(TestConstants.PARENT_ID, TestConstants.PARENT_ORGANIZATION);
        return email;
    }

    public static Map<String, Object> getSuperAdmins() {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put(TestConstants.SEARCH_TERM, getUser());
        responseMap.put(TestConstants.COUNT, Constants.ONE);
        return responseMap;
    }

    public static Map<String, Object> getLockedUsers() {
        List<User> lockedUser = getUsers();
        int totalCount = Constants.ZERO;
        Map<String, Object> lockedUsers = Map.of(Constants.COUNT, totalCount, Constants.DATA, lockedUser);
        return lockedUsers;
    }

    public static RequestDTO getRequestDTO() {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setTenantId(TestConstants.ONE);
        requestDTO.setSearchTerm(TestConstants.SEARCH_TERM);
        requestDTO.setUserId(TestConstants.ONE);
        requestDTO.setEmail(TestConstants.USER_NAME);
        requestDTO.setNewPassword(TestConstants.PASSWORD);
        return requestDTO;
    }

    public static PaginateDTO getPaginateDTO() {
        PaginateDTO paginateDTO = new PaginateDTO();
        paginateDTO.setSearchTerm(TestConstants.SEARCH_TERM);
        paginateDTO.setLimit(Constants.ONE);
        return paginateDTO;
    }

    public static UserSuperAdminDto getUserSuperAdminDto() {
        UserSuperAdminDto userSuperAdminDto = new UserSuperAdminDto();
        userSuperAdminDto.setId(TestConstants.ONE);
        userSuperAdminDto.setFirstName(TestConstants.FIRST_NAME);
        return userSuperAdminDto;
    }

    public static Timezone getTimezone() {
        Timezone timeZone = new Timezone();
        timeZone.setAbbreviation(TestConstants.ABBREVIATION);
        timeZone.setDescription(TestConstants.DESCRIPTION);
        timeZone.setOffset(TestConstants.OFFSET);
        return timeZone;
    }

    public static Map<String, Object> verifySetPassword() {
        Map<String, Object> password = new HashMap<>();
        password.put(TestConstants.PASSWORD_SET, Boolean.TRUE);
        return password;
    }

    public static Set<Long> getUserIds() {
        Set<Long> userIds = new HashSet<>();
        userIds.add(getUser().getId());
        return userIds;
    }

    public static List<Long> getUserIdsList() {
        List<Long> userIds = new ArrayList<>();
        userIds.add(getUser().getId());
        return userIds;
    }

    public static UserProfileDTO getUserProfileDTO() {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId(TestConstants.ONE);
        userProfileDTO.setCountryCode(TestConstants.COUNTRY_CODE);
        return userProfileDTO;
    }

    public static SearchRequestDTO getSearchRequestDTO() {
        SearchRequestDTO searchRequestDTO = new SearchRequestDTO();
        searchRequestDTO.setTenantId(TestConstants.ONE);
        searchRequestDTO.setSearchTerm(TestConstants.SEARCH_TERM);
        return searchRequestDTO;
    }

    public static ResponseListDTO getResponseListDTO() {
        ResponseListDTO response = new ResponseListDTO();
        response.setTotalCount(TestConstants.ONE);
        response.setData(List.of(getUser()));
        return response;
    }

    public static Account getAccount() {
        Account account = new Account();
        account.setName(TestConstants.NAME);
        account.setCountry(getCountry());
        return account;
    }

    public static Country getCountry() {
        Country country = new Country();
        country.setName(TestConstants.COUNTRY_NAME);
        country.setCountryCode(TestConstants.COUNTRY_CODE);
        return country;
    }

    public static CountryDTO getCountryDTO() {
        CountryDTO countryDTO = modelMapper.map(getCountry(), CountryDTO.class);
        return countryDTO;
    }

    public static void init() {
        userContextHolder = mockStatic(UserContextHolder.class);
        userSelectedTenantContextHolder = mockStatic(UserSelectedTenantContextHolder.class);
        commonUtil = mockStatic(CommonUtil.class);
    }

    public static void getStaticMock() {
        UserDTO userDTO = TestDataProvider.getUserDTO();
        userDTO.setId(TestConstants.ONE);
        userContextHolder.when(UserContextHolder::getUserDto).thenReturn(userDTO);
        userSelectedTenantContextHolder.when(UserSelectedTenantContextHolder::get).thenReturn(TestConstants.ONE);
        commonUtil.when(CommonUtil::getAuthToken).thenReturn(TestConstants.TOKEN);
    }

    public static void cleanUp() {
        userSelectedTenantContextHolder.close();
        commonUtil.close();
        userContextHolder.close();
    }

    public static void getStaticMockValidation(String searchTerm) {
        commonUtil.when(() -> CommonUtil.isValidSearchData(searchTerm, Constants.SEARCH_TERM)).thenReturn(true);
        commonUtil.when(() -> CommonUtil.isValidSearchData(searchTerm, Constants.REGEX_SEARCH_PATTERN)).thenReturn(true);
        commonUtil.when(() -> CommonUtil.isValidSearchData(searchTerm, Constants.USER_SEARCH_TERM)).thenReturn(true);
    }

    public static void getStaticMockValidationFalse(String searchTerm) {
        commonUtil.when(() -> CommonUtil.isValidSearchData(searchTerm, Constants.SEARCH_TERM)).thenReturn(false);
        commonUtil.when(() -> CommonUtil.isValidSearchData(searchTerm, Constants.REGEX_SEARCH_PATTERN)).thenReturn(false);
        commonUtil.when(() -> CommonUtil.isValidSearchData(searchTerm, Constants.USER_SEARCH_TERM)).thenReturn(false);
    }

//    public static SiteOrganizationDTO getSiteOrganizationDTO() {
//        SiteOrganizationDTO siteOrganizationDTO = new SiteOrganizationDTO();
//        siteOrganizationDTO.setName("Site");
//        siteOrganizationDTO.setCultureId(TestConstants.ONE);
//        siteOrganizationDTO.setUsers(getUsers());
//        siteOrganizationDTO.setParentOrganizationId(TestConstants.ONE);
//        return siteOrganizationDTO;
//    }

    public static Site getSite() {
        Site site = new Site();
        site.setName("Site");
        site.setCulture(new Culture(TestConstants.ONE));
        return site;
    }

    public static SiteDTO getSiteDTO() {
        SiteDTO siteDTO = new SiteDTO();
        siteDTO.setName("Site");
        siteDTO.setCulture(new Culture(TestConstants.ONE));
        return siteDTO;
    }

    public static OperatingUnitOrganizationDTO getOperatingUnitOrganizationDTO() {
        OperatingUnitOrganizationDTO operatingUnitOrganizationDTO = new OperatingUnitOrganizationDTO();
        operatingUnitOrganizationDTO.setName("OU");
        operatingUnitOrganizationDTO.setUsers(List.of(getUserOrganizationDTO()));
        operatingUnitOrganizationDTO.setParentOrganizationId(TestConstants.ONE);
        return operatingUnitOrganizationDTO;
    }

    public static Operatingunit getOperatingUnit() {
        Operatingunit operatingunit = new Operatingunit();
        operatingunit.setName("OU");
        return operatingunit;
    }

    public static OperatingUnitDTO getOperatingUnitDTO() {
        OperatingUnitDTO operatingUnitDTO = new OperatingUnitDTO();
        operatingUnitDTO.setName("OU");
        return operatingUnitDTO;
    }

    public static AccountWorkflowDTO getAccountworkFlowDto() {
        AccountWorkflowDTO accountWorkflowDTO = new AccountWorkflowDTO();
        accountWorkflowDTO.setName(TestConstants.ACCOUNT_NAME);
        accountWorkflowDTO.setClinicalWorkflow(List.of(TestConstants.ONE));
        accountWorkflowDTO.setCountryId(Constants.THREE);
        return accountWorkflowDTO;
    }

    public static CountryOrganizationDTO getCountryOrganizationDTO() {
        CountryOrganizationDTO countryOrganizationDTO = new CountryOrganizationDTO();
        countryOrganizationDTO.setName(TestConstants.COUNTRY_NAME);
        countryOrganizationDTO.setUsers(List.of(getUserOrganizationDTO()));
        return countryOrganizationDTO;
    }

    public static EmailDTO getEmailDTO() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setBcc(Constants.EMAIL);
        emailDTO.setTo(getUser().getUsername());
        emailDTO.setFormDataId(getUser().getId());
        emailDTO.setFormName(Constants.EMAIL_FORM_USER);
        return emailDTO;
    }

    public static UserDetailsDTO getUserDetailsDTO() {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(getUserDTO(), UserDetailsDTO.class);
    }

    public static ModelMapper setUp(Class<?> injectClass, String map, Object mockedObject) throws NoSuchFieldException, IllegalAccessException {
        ModelMapper modelMapper = Mockito.mock(ModelMapper.class);
        Field mapper = injectClass.getDeclaredField(map);
        mapper.setAccessible(true);
        mapper.set(mockedObject, modelMapper);
        return modelMapper;
    }
    
    public static CultureRequestDTO getCultureRequestDTO() {
        CultureRequestDTO cultureRequestDTO = new CultureRequestDTO();
        cultureRequestDTO.setUserId(TestConstants.ONE);
        cultureRequestDTO.setCultureId(TestConstants.ONE);
        return cultureRequestDTO;
    }
}
