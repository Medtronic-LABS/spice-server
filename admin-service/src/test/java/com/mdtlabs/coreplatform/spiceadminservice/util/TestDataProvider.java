package com.mdtlabs.coreplatform.spiceadminservice.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountCustomizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountDetailsWorkflowDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.AccountWorkflowDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountryOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CountyDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.CustomizationRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OperatingUnitDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ParentOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ProgramRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RegionCustomizationDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ResponseListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteListDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteUserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SubCountyDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserDetailsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.UserOrganizationDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.Country;
import com.mdtlabs.coreplatform.common.model.entity.County;
import com.mdtlabs.coreplatform.common.model.entity.Culture;
import com.mdtlabs.coreplatform.common.model.entity.Operatingunit;
import com.mdtlabs.coreplatform.common.model.entity.Organization;
import com.mdtlabs.coreplatform.common.model.entity.Program;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.Subcounty;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import com.mdtlabs.coreplatform.common.model.entity.User;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountCustomization;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountWorkflow;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTest;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResult;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResultRange;
import com.mdtlabs.coreplatform.common.model.entity.spice.Medication;
import com.mdtlabs.coreplatform.common.model.entity.spice.RegionCustomization;

/**
 * <p>
 * This class provides the data for the test classes.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
public class TestDataProvider {

    public static Account getAccount() {
        Account account = new Account();
        account.setName(TestConstants.ACCOUNT_NAME);
        return account;
    }

    public static RequestDTO getRequestDto(boolean isActive, long tenantId) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setIsActive(isActive);
        requestDTO.setTenantId(tenantId);
        return requestDTO;
    }

    public static RequestDTO getRequestDtoForPagination(String searchTerm, int skip, int limit) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setLimit(limit);
        requestDTO.setSkip(skip);
        requestDTO.setSearchTerm(searchTerm);
        return requestDTO;
    }

    public static SearchRequestDTO getSearchRequestDto(String searchTerm, int skip, int limit) {
        SearchRequestDTO searchRequestDTO = new SearchRequestDTO();
        searchRequestDTO.setSearchTerm(searchTerm);
        searchRequestDTO.setSkip(skip);
        searchRequestDTO.setLimit(limit);
        return searchRequestDTO;
    }

    public static CommonRequestDTO getCommonRequestDTO(Long id, Long tenantId) {
        CommonRequestDTO commonRequestDTO = new CommonRequestDTO();
        commonRequestDTO.setId(id);
        commonRequestDTO.setTenantId(tenantId);
        return commonRequestDTO;
    }

    public static ResponseListDTO getResponseListDTO() {
        ResponseListDTO responseListDTO = new ResponseListDTO();
        return responseListDTO;
    }

    public static AccountListDTO getAccountListDTO(long id, String name, long tenantId) {
        AccountListDTO accountListDTO = new AccountListDTO(id, name, tenantId);
        return accountListDTO;
    }

    public static User getUser() {
        User user = new User();
        user.setUsername(TestConstants.USER_NAME);
        return user;
    }

    public static UserDTO getUserDto() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(TestConstants.USER_NAME);
        return userDTO;
    }

    public static List<Account> getAccounts() {
        Account account = getAccount();
        account.setId(TestConstants.ONE);
        account.setTenantId(Constants.THREE);
        account.setCountry(new Country(TestConstants.ONE));
        Account secondAccount = getAccount();
        secondAccount.setName(TestConstants.SECOND_ACCOUNT_NAME);
        secondAccount.setId(TestConstants.TWO);
        secondAccount.setTenantId(TestConstants.FOUR);
        account.setCountry(new Country(TestConstants.TWO));
        return List.of(account, secondAccount);
    }

    public static AccountWorkflowDTO getAccountworkFlowDto() {
        AccountWorkflowDTO accountWorkflowDTO = new AccountWorkflowDTO();
        accountWorkflowDTO.setName(TestConstants.ACCOUNT_NAME);
        accountWorkflowDTO.setClinicalWorkflow(List.of(TestConstants.ONE));
        accountWorkflowDTO.setCountryId(Constants.THREE);
        return accountWorkflowDTO;
    }

    public static List<AccountWorkflow> getAccountWorkFlows() {
        AccountWorkflow accountWorkflow = new AccountWorkflow();
        accountWorkflow.setWorkflow(Constants.HYPERTENSION);
        accountWorkflow.setId(TestConstants.ONE);
        AccountWorkflow secondAccountWorkflow = new AccountWorkflow();
        secondAccountWorkflow.setWorkflow(Constants.DIABETES);
        secondAccountWorkflow.setId(TestConstants.TWO);
        List<AccountWorkflow> accountWorkflows = new ArrayList<AccountWorkflow>();
        accountWorkflows.add(accountWorkflow);
        accountWorkflows.add(secondAccountWorkflow);
        return accountWorkflows;
    }

    public static CountryDTO getCountryDto() {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setCountryCode(TestConstants.COUNTRY_CODE_VALUE);
        return countryDTO;
    }

    public static RegionCustomization getRegionCustomization() {
        RegionCustomization regionCustomization = new RegionCustomization();
        regionCustomization.setCountryId(TestConstants.ONE);
        regionCustomization.setFormInput(TestConstants.FORM_INPUT);
        regionCustomization.setType(Constants.SCREENING);
        regionCustomization.setCategory(Constants.CONSENT_FORM);
        regionCustomization.setTenantId(TestConstants.ONE);
        return regionCustomization;
    }

    public static CustomizationRequestDTO getCustomizationRequestDTO() {
        CustomizationRequestDTO customizationRequestDTO = new CustomizationRequestDTO();
        customizationRequestDTO.setCountryId(TestConstants.ONE);
        customizationRequestDTO.setType(Constants.SCREENING);
        customizationRequestDTO.setCategory(Constants.CONSENT_FORM);
        customizationRequestDTO.setAccountId(TestConstants.ONE);
        customizationRequestDTO.setTenantId(TestConstants.ONE);
        return customizationRequestDTO;
    }

    public static Organization getOrganization() {
        Organization organization = new Organization();
        organization.setId(TestConstants.ONE);
        organization.setParentOrganizationId(TestConstants.ONE);
        organization.setFormDataId(TestConstants.ONE);
        return organization;
    }

    public static AccountCustomization getAccountCustomization() {
        AccountCustomization accountCustomization = new AccountCustomization();
        accountCustomization.setId(TestConstants.ONE);
        accountCustomization.setTenantId(TestConstants.ONE);
        accountCustomization.setType(Constants.TYPE);
        return accountCustomization;
    }

    public static List<AccountCustomization> getAccountCustomizations() {
        AccountCustomization accountCustomization = getAccountCustomization();
        accountCustomization.setId(TestConstants.ONE);
        accountCustomization.setClinicalWorkflowId(TestConstants.TWO);
        AccountCustomization secondAccountCustomization = getAccountCustomization();
        secondAccountCustomization.setId(TestConstants.TWO);
        secondAccountCustomization.setClinicalWorkflowId(TestConstants.TWO);
        List<AccountCustomization> accountCustomizations = new ArrayList<>();
        accountCustomizations.add(accountCustomization);
        accountCustomizations.add(secondAccountCustomization);
        return accountCustomizations;
    }

    public static AccountWorkflow getAccountWorkflow() {
        AccountWorkflow accountWorkflow = new AccountWorkflow();
        accountWorkflow.setName(Constants.HYPERTENSION);
        accountWorkflow.setViewScreens(List.of(Constants.SCREENING, Constants.ASSESSMENT, Constants.ENROLLMENT));
        return accountWorkflow;
    }

    public static AccountWorkflowDTO getAccountWorkflowDTO() {
        AccountWorkflowDTO accountWorkflowDTO = new AccountWorkflowDTO();
        accountWorkflowDTO.setName(Constants.HYPERTENSION);
        return accountWorkflowDTO;
    }

    public static List<AccountWorkflow> getAccountWorkflows() {
        AccountWorkflow accountWorkflow = new AccountWorkflow();
        AccountWorkflow secondAccountWorkflow = new AccountWorkflow();
        accountWorkflow.setId(TestConstants.ONE);
        secondAccountWorkflow.setId(TestConstants.TWO);
        accountWorkflow.setModuleType(Constants.MODULE_TYPE);
        secondAccountWorkflow.setModuleType(Constants.MODULE_TYPE);
        return List.of(accountWorkflow, secondAccountWorkflow);
    }

    public static AccountDetailsDTO getAccountDetailsDTO() {
        AccountDetailsDTO accountDetailsDTO = new AccountDetailsDTO();
        accountDetailsDTO.setCountryCode(TestConstants.COUNTRY_CODE_VALUE);
        return accountDetailsDTO;
    }

    public static ProgramRequestDTO getProgramRequestDTO() {
        ProgramRequestDTO programRequestDTO = new ProgramRequestDTO();
        programRequestDTO.setTenantId(TestConstants.ONE);
        programRequestDTO.setName(TestConstants.PROGRAM_NAME);
        return programRequestDTO;
    }

    public static Program getProgram() {
        Program program = new Program();
        program.setName(TestConstants.PROGRAM_NAME);
        return program;
    }

    public static List<Program> getPrograms() {
        Program program = getProgram();
        program.setSites(Set.of(new Site(TestConstants.ONE)));
        Program secondProgram = getProgram();
        secondProgram.setName(TestConstants.PROGRAM_NAME);
        program.setSites(Set.of(new Site(TestConstants.TWO)));
        return List.of(program, secondProgram);
    }

    public static Site getSite() {
        Site site = new Site();
        site.setCountyId(TestConstants.ONE);
        return site;
    }

    public static Culture getCulture() {
        Culture culture = new Culture(TestConstants.ONE);
        return culture;
    }

    public static Set<Site> getSites() {
        Site site = new Site(TestConstants.ONE);
        site.setName(TestConstants.SITE_NAME);
        Site secondSite = new Site(TestConstants.TWO);
        secondSite.setName(TestConstants.SECOND_SITE_NAME);
        return Set.of(site, secondSite);
    }

    public static List<Site> getSiteList() {
        Site site = new Site(TestConstants.ONE);
        site.setName(TestConstants.SITE_NAME);
        site.setTenantId(TestConstants.ONE);
        Site secondSite = new Site(TestConstants.TWO);
        secondSite.setName(TestConstants.SECOND_SITE_NAME);
        secondSite.setTenantId(TestConstants.TWO);
        return List.of(site, secondSite);
    }

    public static SiteDetailsDTO getSiteDetailsDTO() {
        SiteDetailsDTO siteDetailsDTO = new SiteDetailsDTO();
        siteDetailsDTO.setId(TestConstants.ONE);
        siteDetailsDTO.setOperatingUnit(getParentOrganizationDTO());
        siteDetailsDTO.setAccount(getParentOrganizationDTO());
        siteDetailsDTO.setCounty(getParentOrganizationDTO());
        siteDetailsDTO.setCountry(getParentOrganizationDTO());
        siteDetailsDTO.setSubCounty(getParentOrganizationDTO());
        siteDetailsDTO.setCulture(getParentOrganizationDTO());
        return siteDetailsDTO;
    }

    public static SiteUserDTO getSiteUserDTO() {
        SiteUserDTO siteUserDTO = new SiteUserDTO();
        siteUserDTO.setId(TestConstants.ONE);
        return siteUserDTO;
    }

    public static LabTest getLabTest() {
        LabTest labTest = new LabTest();
        labTest.setName(TestConstants.LAB_TEST_NAME);
        labTest.setCountryId(TestConstants.ONE);
        return labTest;
    }

    public static List<LabTest> getLabTests() {
        LabTest labTest = TestDataProvider.getLabTest();
        labTest.setId(TestConstants.ONE);
        LabTest secondLabTest = TestDataProvider.getLabTest();
        secondLabTest.setId(TestConstants.TWO);
        secondLabTest.setName(TestConstants.SECOND_LAB_TEST_NAME);
        return List.of(labTest, secondLabTest);
    }

    public static LabTestResult getLabTestResult() {
        LabTestResult labTestResult = new LabTestResult();
        labTestResult.setId(TestConstants.ONE);
        labTestResult.setLabTestId(TestConstants.ONE);
        return labTestResult;
    }

    public static Set<LabTestResult> getLabTestResults() {
        LabTestResult labTestResult = TestDataProvider.getLabTestResult();
        labTestResult.setId(TestConstants.ONE);
        labTestResult.setName(TestConstants.LAB_TEST_RESULT_NAME);
        LabTestResult secondLabTestResult = TestDataProvider.getLabTestResult();
        secondLabTestResult.setId(TestConstants.TWO);
        secondLabTestResult.setName(TestConstants.SECOND_LAB_TEST_RESULT_NAME);
        return Set.of(labTestResult, secondLabTestResult);
    }

    public static LabTestResultRangeRequestDTO getLabTestResultRangeRequestDto() {
        LabTestResultRangeRequestDTO labTestResultRangeRequestDTO = new LabTestResultRangeRequestDTO();
        labTestResultRangeRequestDTO.setLabTestResultId(TestConstants.ONE);
        labTestResultRangeRequestDTO.setTenantId(TestConstants.ONE);
        labTestResultRangeRequestDTO.setLabTestResultRanges(getLabTestResultRangeDTOs());
        return labTestResultRangeRequestDTO;
    }

    public static List<LabTestResultRangeDTO> getLabTestResultRangeDTOs() {
        List<LabTestResultRangeDTO> labTestResultRangeDTOS = new ArrayList<>();
        labTestResultRangeDTOS.add(getLabTestResultRangeDTO());
        return labTestResultRangeDTOS;
    }

    public static LabTestResultRangeDTO getLabTestResultRangeDTO() {
        LabTestResultRangeDTO labTestResultRangeDTO = new LabTestResultRangeDTO();
        labTestResultRangeDTO.setId(TestConstants.ONE);
        return labTestResultRangeDTO;
    }

    public static LabTestResultRange getLabTestResultRange() {
        LabTestResultRange labTestResultRange = new LabTestResultRange();
        return labTestResultRange;
    }

    public static List<LabTestResultRange> getLabTestResultRanges() {
        LabTestResultRange labTestResultRange = getLabTestResultRange();
        labTestResultRange.setId(TestConstants.ONE);
        labTestResultRange.setLabTestId(TestConstants.ONE);
        labTestResultRange.setLabTestResultId(TestConstants.ONE);
        LabTestResultRange secondLabTestResultRange = getLabTestResultRange();
        secondLabTestResultRange.setId(TestConstants.TWO);
        secondLabTestResultRange.setLabTestId(TestConstants.ONE);
        secondLabTestResultRange.setLabTestResultId(TestConstants.ONE);
        return List.of(labTestResultRange, secondLabTestResultRange);
    }

    public static List<LabTestResultRangeRequestDTO> getLabTestResultRangeRequestDTOs() {
        LabTestResultRangeRequestDTO labTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();
        labTestResultRangeRequestDTO.setId(TestConstants.ONE);
        LabTestResultRangeRequestDTO secondLabTestResultRangeRequestDTO = TestDataProvider.getLabTestResultRangeRequestDto();
        secondLabTestResultRangeRequestDTO.setId(TestConstants.TWO);
        return List.of(labTestResultRangeRequestDTO, secondLabTestResultRangeRequestDTO);
    }

    public static Operatingunit getOperatingUnit() {
        Operatingunit operatingunit = new Operatingunit();
        operatingunit.setTenantId(TestConstants.ONE);
        return operatingunit;
    }

    public static OperatingUnitDetailsDTO getOperatingUnitDetailsDTO() {
        OperatingUnitDetailsDTO operatingUnitDetailsDTO = new OperatingUnitDetailsDTO();
        return operatingUnitDetailsDTO;
    }

    public static UserOrganizationDTO getUserOrganizationDTO() {
        UserOrganizationDTO userOrganizationDTO = new UserOrganizationDTO();
        userOrganizationDTO.setUsername(TestConstants.USER_NAME);
        userOrganizationDTO.setFirstName(TestConstants.USER_FIRST_NAME);
        userOrganizationDTO.setLastName(TestConstants.USER_LAST_NAME);
        userOrganizationDTO.setPhoneNumber(TestConstants.PHONE_NUMBER);
        return userOrganizationDTO;
    }

    public static List<Country> getCountries() {
        Country country = new Country(TestConstants.ONE);
        country.setId(TestConstants.ONE);
        country.setName(TestConstants.COUNTRY_NAME);
        country.setTenantId(TestConstants.ONE);
        Country secondCountry = new Country(TestConstants.TWO);
        secondCountry.setId(TestConstants.TWO);
        secondCountry.setName(TestConstants.SECOND_COUNTRY_NAME);
        secondCountry.setTenantId(TestConstants.ONE);
        return List.of(country, secondCountry);
    }

    public static List<CountryDTO> getListCountryDTO() {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setId(TestConstants.ONE);
        countryDTO.setName(TestConstants.COUNTRY_NAME);
        CountryDTO secondCountryDTO = new CountryDTO();
        secondCountryDTO.setId(TestConstants.TWO);
        secondCountryDTO.setName(TestConstants.SECOND_COUNTRY_NAME);
        return List.of(countryDTO, secondCountryDTO);
    }

    public static Country getCountry() {
        Country country = new Country(TestConstants.ONE);
        country.setName(TestConstants.COUNTRY_NAME);
        return country;
    }

    public static Subcounty getSubCounty() {
        Subcounty subcounty = new Subcounty();
        subcounty.setId(TestConstants.ONE);
        return subcounty;
    }

    public static County getCounty() {
        County county = new County();
        county.setId(TestConstants.ONE);
        return county;
    }

    public static List<County> getCounties() {
        List<County> counties = new ArrayList<>();
        counties.add(getCounty());
        return counties;
    }

    public static List<Subcounty> getSubCounties() {
        List<Subcounty> subCounties = new ArrayList<>();
        subCounties.add(getSubCounty());
        return subCounties;
    }

    public static CountryOrganizationDTO getCountryOrganizationDTO() {
        CountryOrganizationDTO countryOrganizationDTO = new CountryOrganizationDTO();
        countryOrganizationDTO.setId(TestConstants.ONE);
        return countryOrganizationDTO;
    }

    public static Medication getMedication() {
        Medication medication = new Medication();
        medication.setClassificationId(TestConstants.ONE);
        medication.setTenantId(TestConstants.ONE);
        medication.setBrandId(TestConstants.ONE);
        medication.setCountryId(TestConstants.ONE);
        medication.setDosageFormId(TestConstants.ONE);
        medication.setMedicationName(TestConstants.MEDICATION_NAME);
        return medication;
    }

    public static List<Medication> getMedications() {
        List<Medication> medications = new ArrayList<>();
        medications.add(getMedication());
        return medications;
    }

    public static UserDTO getUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setCountry(new Country(TestConstants.ONE));
        return userDTO;
    }

    public static List<SiteListDTO> getSiteListDTOs() {
        SiteListDTO siteListDTO = new SiteListDTO();
        siteListDTO.setId(TestConstants.ONE);
        siteListDTO.setName(TestConstants.SITE_NAME);
        siteListDTO.setTenantId(TestConstants.ONE);
        SiteListDTO secondSiteListDTO = new SiteListDTO();
        secondSiteListDTO.setId(TestConstants.TWO);
        secondSiteListDTO.setName(TestConstants.SECOND_SITE_NAME);
        secondSiteListDTO.setTenantId(TestConstants.TWO);
        return List.of(siteListDTO, secondSiteListDTO);
    }

    public static ParentOrganizationDTO getParentOrganizationDTO() {
        ParentOrganizationDTO parentOrganizationDTO = new ParentOrganizationDTO();
        parentOrganizationDTO.setId(TestConstants.ONE);
        return parentOrganizationDTO;
    }

    public static List<Operatingunit> getOperatingUnits() {
        Operatingunit operatingunit = TestDataProvider.getOperatingUnit();
        operatingunit.setId(TestConstants.ONE);
        operatingunit.setTenantId(TestConstants.ONE);
        Operatingunit secondOperatingunit = TestDataProvider.getOperatingUnit();
        secondOperatingunit.setId(TestConstants.TWO);
        secondOperatingunit.setTenantId(TestConstants.TWO);
        return List.of(operatingunit, secondOperatingunit);
    }


    public static List<OperatingUnitDTO> getOperatingUnitDTOs() {
        OperatingUnitDTO operatingUnitDTO = TestDataProvider.getOperatingUnitDTO();
        operatingUnitDTO.setId(TestConstants.ONE);
        operatingUnitDTO.setTenantId(TestConstants.ONE);
        OperatingUnitDTO secondOperatingunitDTO = TestDataProvider.getOperatingUnitDTO();
        secondOperatingunitDTO.setId(TestConstants.TWO);
        secondOperatingunitDTO.setTenantId(TestConstants.TWO);
        return List.of(operatingUnitDTO, secondOperatingunitDTO);
    }

    public static OperatingUnitDTO getOperatingUnitDTO() {
        OperatingUnitDTO operatingUnitDTO = new OperatingUnitDTO();
        return operatingUnitDTO;
    }


    public static List<MedicationDTO> getMedicationDTOs() {
        MedicationDTO medicationDTO = getMedicationDTO();
        medicationDTO.setId(TestConstants.ONE);
        medicationDTO.setMedicationName(TestConstants.MEDICATION_NAME);
        return List.of(medicationDTO);
    }

    private static MedicationDTO getMedicationDTO() {
        MedicationDTO medicationDTO = new MedicationDTO();
        medicationDTO.setClassificationId(TestConstants.ONE);
        medicationDTO.setTenantId(TestConstants.ONE);
        medicationDTO.setId(TestConstants.ONE);
        medicationDTO.setBrandId(TestConstants.ONE);
        medicationDTO.setCountryId(TestConstants.ONE);
        medicationDTO.setDosageFormId(TestConstants.ONE);
        return medicationDTO;
    }

    public static CountyDTO getCountyDTO() {
        CountyDTO countyDTO = new CountyDTO();
        countyDTO.setId(TestConstants.ONE);
        return countyDTO;
    }

    public static SubCountyDTO getSubCountyDto() {
        SubCountyDTO subCountyDTO = new SubCountyDTO();
        subCountyDTO.setId(TestConstants.ONE);
        return subCountyDTO;
    }

    public static SiteDTO getSiteDTO() {
        SiteDTO siteDTO = new SiteDTO();
        siteDTO.setCountyId(TestConstants.ONE);
        return siteDTO;
    }

    public static LabTestDTO getLabTestDTO() {
        LabTestDTO labTestDTO = new LabTestDTO();
        labTestDTO.setName(TestConstants.LAB_TEST_NAME);
        labTestDTO.setCountryId(TestConstants.ONE);
        return labTestDTO;
    }

    public static AccountCustomizationDTO getAccountCustomizationDto() {
        AccountCustomizationDTO accountCustomizationDTO = new AccountCustomizationDTO();
        accountCustomizationDTO.setId(TestConstants.ONE);
        accountCustomizationDTO.setType(Constants.TYPE);
        return accountCustomizationDTO;
    }

    public static RegionCustomizationDTO getRegionCustomizationDTO() {
        RegionCustomizationDTO regionCustomizationDTO = new RegionCustomizationDTO();
        regionCustomizationDTO.setCountryId(TestConstants.ONE);
        regionCustomizationDTO.setFormInput(TestConstants.FORM_INPUT);
        regionCustomizationDTO.setType(Constants.SCREENING);
        regionCustomizationDTO.setCategory(Constants.CONSENT_FORM);
        regionCustomizationDTO.setTenantId(TestConstants.ONE);
        return regionCustomizationDTO;
    }

    public static CountryListDTO getCountryListDTO() {
        CountryListDTO countryListDTO = new CountryListDTO();
        countryListDTO.setId(TestConstants.ONE);
        countryListDTO.setTenantId(TestConstants.ONE);
        countryListDTO.setName(TestConstants.COUNTRY_NAME);
        return countryListDTO;
    }

    public static UserDetailsDTO getUserDetailsDTO() {

        ModelMapper mapper = new ModelMapper();
        UserDetailsDTO userDetailsDTO = mapper.map(getUser(), UserDetailsDTO.class);
        userDetailsDTO.setTimezone(new Timezone());
        return userDetailsDTO;

    }

    public static AccountDetailsWorkflowDTO getAccountDetailsWorkflowDTO() {
        AccountDetailsWorkflowDTO accountDetailsWorkflowDTO = new AccountDetailsWorkflowDTO();
        return accountDetailsWorkflowDTO;
    }

    public static List<LabTestDTO> getLabTestDTOs() {
        LabTestDTO labTestDTO = new LabTestDTO();
        labTestDTO.setId(TestConstants.ONE);
        labTestDTO.setCountryId(TestConstants.ONE);
        LabTestDTO secondLabTestDTO = new LabTestDTO();
        secondLabTestDTO.setId(TestConstants.TWO);
        secondLabTestDTO.setName(TestConstants.SECOND_LAB_TEST_NAME);
        secondLabTestDTO.setCountryId(TestConstants.ONE);
        return List.of(labTestDTO, secondLabTestDTO);
    }
}
