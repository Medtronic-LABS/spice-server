package com.mdtlabs.coreplatform.spiceservice.staticdata.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.UserContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserSelectedTenantContextHolder;
import com.mdtlabs.coreplatform.common.contexts.UserTenantsContextHolder;
import com.mdtlabs.coreplatform.common.exception.Validation;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.dto.RoleDTO;
import com.mdtlabs.coreplatform.common.model.dto.UserDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewStaticDataDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MetaFormDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SiteResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.StaticDataDTO;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountCustomization;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountWorkflow;
import com.mdtlabs.coreplatform.common.model.entity.spice.Comorbidity;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complaints;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complication;
import com.mdtlabs.coreplatform.common.model.entity.spice.CurrentMedication;
import com.mdtlabs.coreplatform.common.model.entity.spice.Diagnosis;
import com.mdtlabs.coreplatform.common.model.entity.spice.DosageForm;
import com.mdtlabs.coreplatform.common.model.entity.spice.FormMetaUi;
import com.mdtlabs.coreplatform.common.model.entity.spice.Frequency;
import com.mdtlabs.coreplatform.common.model.entity.spice.Lifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.MedicalCompliance;
import com.mdtlabs.coreplatform.common.model.entity.spice.ModelQuestions;
import com.mdtlabs.coreplatform.common.model.entity.spice.NutritionLifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PhysicalExamination;
import com.mdtlabs.coreplatform.common.model.entity.spice.Reason;
import com.mdtlabs.coreplatform.common.model.entity.spice.RegionCustomization;
import com.mdtlabs.coreplatform.common.model.entity.spice.SideMenu;
import com.mdtlabs.coreplatform.common.model.entity.spice.Symptom;
import com.mdtlabs.coreplatform.common.util.CommonUtil;
import com.mdtlabs.coreplatform.spiceservice.AdminApiInterface;
import com.mdtlabs.coreplatform.spiceservice.frequency.service.FrequencyService;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.UnitRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ComorbidityService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ComplaintsService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ComplicationService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CultureService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CurrentMedicationService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.DiagnosisService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.DosageFormService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.DosageFrequencyService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.FormMetaService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.LifestyleService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.MedicalComplianceService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ModelQuestionsService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.NutritionLifestyleService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.PhysicalExaminationService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ReasonService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.RiskAlgorithmService;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.SideMenuService;
import com.mdtlabs.coreplatform.spiceservice.staticdata.service.StaticDataService;
import com.mdtlabs.coreplatform.spiceservice.symptom.service.SymptomService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * StaticDataServiceImpl class implements various methods for retrieving and clearing static data, and also
 * consists business logic that needed to get meta data related to the service.
 * </p>
 *
 * @author Niraimathi S created on Jun 30, 2022
 */
@Service
public class StaticDataServiceImpl implements StaticDataService {

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private AdminApiInterface adminApiInterface;

    @Autowired
    private ComorbidityService comorbidityService;

    @Autowired
    private ComplaintsService complaintsService;

    @Autowired
    private ComplicationService complicationService;

    @Autowired
    private CultureService cultureService;

    @Autowired
    private CurrentMedicationService currentMedicationService;

    @Autowired
    private DiagnosisService diagnosisService;

    @Autowired
    private DosageFormService dosageFormService;

    @Autowired
    private DosageFrequencyService dosageFrequencyService;

    @Autowired
    private FormMetaService formMetaService;

    @Autowired
    private FrequencyService frequencyService;

    @Autowired
    private LifestyleService lifestyleService;

    @Autowired
    private MedicalComplianceService medicalComplianceService;

    @Autowired
    private ModelQuestionsService modelQuestionsService;

    @Autowired
    private NutritionLifestyleService nutritionLifestyleService;

    @Autowired
    private PhysicalExaminationService physicalExaminationService;

    @Autowired
    private ReasonService reasonService;

    @Autowired
    private RiskAlgorithmService riskAlgorithmService;

    @Autowired
    private SideMenuService sideMenuService;

    @Autowired
    private SymptomService symptomService;

    @Autowired
    private UnitRepository unitRepository;

    private Map<Long, Object> jsonCultureMap = new HashMap<>();

    private Map<Long, String> cultureMap = new HashMap<>();

    @Value("${app.app-version}")
    private String appVersion;

    /**
     * {@inheritDoc}
     */
    public StaticDataDTO getStaticData(Long cultureId) {
        String token = CommonUtil.getAuthToken();
        Long tenantId = UserSelectedTenantContextHolder.get();
        cultureId = validateCulture(cultureId);
        List<Site> sites = adminApiInterface.getSitesByTenantIds(token, tenantId, UserTenantsContextHolder.get());
        Account account = adminApiInterface.getAccountById(token, tenantId, sites.get(0).getAccountId());
        StaticDataDTO response = new StaticDataDTO();
        UserDTO userDto = UserContextHolder.getUserDto();
        List<String> userRoles = userDto.getRoles().stream().map(RoleDTO::getName).toList();
        List<Long> siteIds = sites.stream().map(Site::getId).toList();
        response.setPrograms(adminApiInterface.getPrograms(token, tenantId, siteIds));
        response.setAccountId(account.getId());
        response.setOperatingUnitId(sites.get(0).getOperatingUnit().getId());
        constructSiteAndUserRole(sites, userDto, userRoles, response, cultureId);
        Site defaultSite = getDefaultSite(tenantId, sites);
        response.setDefaultSite(defaultSite);
        response.setCultures(cultureService.getAllCultures());
        response.setOperatingSites(
                adminApiInterface.getSitesByOperatingUnitId(token, tenantId, sites.get(0).getOperatingUnit().getId()));
        setMetaData(response, userDto.getCountry().getId(), token, tenantId,
                cultureId);
        if (defaultSite != null) {
            constructFormCustomizations(defaultSite, response, token, tenantId, account, cultureId);
        }
        return response;
    }

    /**
     * <p>
     * This method is used to returns the default site from a list of sites based on a given tenant ID.
     * </p>
     *
     * @param tenantId {@link Long} The ID of the tenant for which we want to retrieve the default site is given
     * @param sites    {@link List}   The list of Site objects is given
     * @return {@link Site} The method returns a site or null
     */
    private Site getDefaultSite(Long tenantId, List<Site> sites) {
        for (Site site : sites) {
            if (tenantId.equals(site.getTenantId())) {
                return site;
            }
        }
        return null;
    }

    /**
     * <p>
     * This method is used to constructs a site and user role based on input parameters and sets the menus and sites
     * in the response object.
     * </p>
     *
     * @param sites     {@link List} The list of Site objects is given
     * @param userDto   {@link UserDTO} The userDTO containing information such as their username, password, and roles
     *                  is given
     * @param userRoles {@link List} The list of roles assigned to a user is given
     * @param response  {@link StaticDataDTO} The StaticData is used to store various data related to a user's account,
     *                  such as their sites and menus is given
     * @param cultureId {@link Long} The ID of the culture for which the site and user role are being constructed
     *                  is given
     */
    private void constructSiteAndUserRole(List<Site> sites, UserDTO userDto, List<String> userRoles,
                                          StaticDataDTO response, Long cultureId) {
        List<String> roleDisplayNames = userDto.getRoles().stream().map(role -> Constants.ROLES.get(role.getName())).toList();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<SiteResponseDTO> siteDtoList = mapper.map(sites, new TypeToken<List<SiteResponseDTO>>() {
        }.getType());
        siteDtoList.forEach(siteDto -> {
            siteDto.setRoleName(userRoles);
            siteDto.setDisplayName(roleDisplayNames);
        });
        List<SideMenu> sideMenus = sideMenuService.getSideMenus(userRoles);
        Map<String, Map<Long, Object>> jsonCultureValuesMap = Constants.JSON_CULTURE_VALUES_MAP.get(cultureId);
        jsonCultureMap = jsonCultureValuesMap.get(Constants.CULTURE_VALUE_SIDE_MENU);
        sideMenus.forEach(sideMenu -> sideMenu.setCultureValues(jsonCultureMap.get(sideMenu.getId())));
        response.setMenus(sideMenus);
        response.setSites(siteDtoList);
    }

    /**
     * <p>
     * This method is used to set metadata for a StaticDataDTO object based on various parameters.
     * </p>
     *
     * @param response  {@link StaticDataDTO} The StaticData contains static data for a particular country and culture
     *                  is given
     * @param countryId {@link Long} The ID of the country for which the metadata is being set is given
     * @param token     {@link String}  The authentication token or access token used for making API requests is given
     * @param tenantId  {@link Long} The ID of the tenant or organization that the data belongs to
     * @param cultureId {@link Long} The culture for which the metadata is being set is given
     */
    private void setMetaData(StaticDataDTO response, Long countryId, String token,
                             Long tenantId, Long cultureId) {
        Map<String, Map<Long, String>> cultureValuesMap = Constants.CULTURE_VALUES_MAP.get(cultureId);
        setMedicationMeta(cultureValuesMap, response);
        setOtherMeta(cultureValuesMap, response);
        setCountryBasedMeta(token, tenantId, countryId, response);
        response.setMentalHealth(getMentalHealthStaticData(countryId, cultureId));
    }

    /**
     * <p>
     * This method is used to set medication metadata by retrieving dosage forms, units, and dosage frequency from
     * various services and assigning them to the response object.
     * </p>
     *
     * @param cultureValuesMap {@link Map} The map containing culture values for different attributes of medication,
     *                         where the keys are attribute names and the values are maps containing culture values for
     *                         each attribute value is given
     * @param response         {@link StaticDataDTO}        The StaticData which is being modified and updated
     *                         within the method is given
     */
    private void setMedicationMeta(Map<String, Map<Long, String>> cultureValuesMap, StaticDataDTO response) {
        cultureMap = cultureValuesMap.get(Constants.DOSAGE_FORM);
        List<DosageForm> dosageForms = dosageFormService.getDosageFormNotOther();
        dosageForms.forEach(form -> form.setCultureValue(cultureMap.get(form.getId())));
        response.setDosageForm(dosageForms);
        response.setUnits(unitRepository.findByNameNotLike(Constants.OTHER));
        response.setDosageFrequency(
                dosageFrequencyService.getDosageFrequency(Sort.by(Sort.Direction.ASC, Constants.DISPLAY_ORDER)));

    }

    /**
     * <p>
     * This method is used to set country-based metadata for a given token, tenant ID, country ID, and response object.
     * </p>
     *
     * @param token     {@link String} The authentication token required to access the API is given
     * @param tenantId  {@link Long} The ID of the tenant for which the data is being retrieved is given
     * @param countryId {@link Long} The ID of the country for which the metadata is being set is given
     * @param response  {@link StaticDataDTO} The StaticData is being modified by setting its properties based
     *                  on the values returned from the methods being called is given
     */
    private void setCountryBasedMeta(String token, Long tenantId, Long countryId, StaticDataDTO response) {
        response.setCvdRiskAlgorithms(riskAlgorithmService.getRiskAlgorithms(countryId).getCvdRiskAlgorithm());
        response.setCountries(List.of(adminApiInterface.getCountryById(token, tenantId, countryId)));
        response.setCounties(adminApiInterface.getAllCountyByCountryId(token, tenantId, countryId));
        response.setSubcounties(adminApiInterface.getAllSubCountyByCountryId(token, tenantId, countryId));
    }

    /**
     * <p>
     * This method is used to set culture values for various objects in a response object using data from a map.
     * </p>
     *
     * @param cultureValuesMap {@link Map} The map contains culture-specific values for different categories such as
     *                         nutrition, lifestyle, symptoms, medical compliance's, diagnosis, and reasons is given
     * @param response         {@link StaticDataDTO} The StaticData will be populated with the data retrieved from the
     *                         services is given
     */
    private void setOtherMeta(Map<String, Map<Long, String>> cultureValuesMap, StaticDataDTO response) {
        cultureMap = cultureValuesMap.get(Constants.NUTRITION_LIFESTYLE);
        Set<NutritionLifestyle> nutritionLifestyles = nutritionLifestyleService
                .getNutritionLifestyleByIds(cultureMap.keySet());
        nutritionLifestyles.forEach(lifestyle -> lifestyle.setCultureValue(cultureMap.get(lifestyle.getId())));
        response.setNutritionLifestyle(nutritionLifestyles.stream().toList());

        cultureMap = cultureValuesMap.get(Constants.CULTURE_VALUE_SYMPTOMS);
        List<Symptom> symptoms = symptomService.getSymptoms();
        symptoms.forEach(symptom -> symptom.setCultureValue(cultureMap.get(symptom.getId())));
        response.setSymptoms(symptoms);

        cultureMap = cultureValuesMap.get(Constants.CULTURE_VALUE_MEDICAL_COMPLIANCES);
        List<MedicalCompliance> medicalComplianceList = medicalComplianceService.getMedicalComplianceList();
        medicalComplianceList.forEach(compliance -> compliance.setCultureValue(cultureMap.get(compliance.getId())));
        response.setMedicalCompliances(medicalComplianceList);

        cultureMap = cultureValuesMap.get(Constants.CULTURE_VALUE_DIAGNOSIS);
        List<Diagnosis> diagnosisList = diagnosisService.getDiagnosis();
        diagnosisList.forEach(diagnosis -> diagnosis.setCultureValue(cultureMap.get(diagnosis.getId())));
        response.setDiagnosis(diagnosisList);

        cultureMap = cultureValuesMap.get(Constants.CULTURE_VALUE_REASONS);
        List<Reason> reasons = reasonService.getReasons();
        reasons.forEach(reason -> reason.setCultureValue(cultureMap.get(reason.getId())));
        response.setReasons(reasons);
    }

    /**
     * <p>
     * This method is used to constructs customizations for a form based on various parameters and API calls.
     * </p>
     *
     * @param defaultSite {@link Site} The default site for the account is given
     * @param response    {@link StaticDataDTO} The StaticData contains static data related to the application is given
     * @param token       {@link String} The token is likely an authentication token or access token that is used to
     *                    authenticate the user and authorize their access to certain resources or APIs is given
     * @param tenantId    {@link Long} The ID of the tenant for which the form customizations are being constructed
     *                    is given
     * @param account     {@link Account} The account represents the user account for whom the form customizations are
     *                    being constructed is given
     * @param cultureId   {@link Long} cultureId is a Long variable representing the ID of the culture or language
     *                    being used in the application is given
     */
    private void constructFormCustomizations(Site defaultSite, StaticDataDTO response, String token,
                                             Long tenantId, Account account, Long cultureId) {
        List<String> unselectedWorkflowNames = setClinicalWorkflowData(account, token, tenantId, response);

        List<String> screenTypes = new ArrayList<>(Arrays.asList(Constants.WORKFLOW_ENROLLMENT,
                Constants.WORKFLOW_SCREENING, Constants.WORKFLOW_ASSESSMENT, Constants.MODULE));
        List<String> category = List.of(Constants.INPUT_FORM, Constants.CONSENT_FORM);
        List<AccountCustomization> accountCustomizations = adminApiInterface.getAccountCustomization(token, tenantId,
                Map.of(Constants.SCREEN_TYPES, screenTypes, Constants.CATEGORY, category, Constants.COUNTRY_ID, defaultSite.getCountryId()));

        List<AccountCustomization> accountConsentForms = setAccountCustomizationsData(accountCustomizations, account, defaultSite, response);
        if (!screenTypes.isEmpty()) {
            List<RegionCustomization> regionCustomizations = adminApiInterface.getRegionCustomizations(token, tenantId, cultureId);
            constructRegionCustomization(unselectedWorkflowNames, response, accountConsentForms, regionCustomizations);
        }
    }

    /**
     * <p>
     * This method is used to set data for account customizations, including consent forms and customized workflows.
     * </p>
     *
     * @param accountCustomizations {@link List} The list of AccountCustomization contains customization data for an
     *                              account is given
     * @param account               {@link Account} The Account which contains information about an account is given
     * @param defaultSite           {@link Site} The default site for the account is given
     * @param response              {@link StaticDataDTO} The StaticData which is being modified and returned by
     *                              the method is given
     * @return {@link List<AccountCustomization>} The method is returning a List of AccountCustomization objects
     */
    private List<AccountCustomization> setAccountCustomizationsData(List<AccountCustomization> accountCustomizations, Account account, Site defaultSite, StaticDataDTO response) {
        List<AccountWorkflow> customizedWorkflows = account.getCustomizedWorkflows();
        List<AccountCustomization> accountConsentForms = new ArrayList<>();
        List<Map<String, Object>> customizedModules = new ArrayList<>();
        if (null != accountCustomizations && !accountCustomizations.isEmpty()) {
            accountConsentForms = accountCustomizations.stream().filter(
                    customization -> !Objects.isNull(customization.getAccountId()) &&
                            customization.getAccountId().equals(defaultSite.getAccountId()) &&
                            customization.getCategory().equals("Consent_form")
            ).toList();
            for (AccountCustomization form : accountCustomizations) {
                if (form.getType().equals(Constants.MODULE) && customizedWorkflows.stream().map(BaseEntity::getId).toList().contains(form.getClinicalWorkflowId())) {
                    Map<String, Object> customForm = new HashMap<>();
                    AccountWorkflow accountWorkflow = customizedWorkflows.stream().filter(flow -> flow.getId().equals(form.getClinicalWorkflowId())).findAny().orElse(null);
                    if (Objects.nonNull(accountWorkflow)) {
                        customForm.put(Constants.VIEW_SCREENS, accountWorkflow.getViewScreens());
                    }
                    customForm.put(Constants.FORM_INPUT, form.getFormInput());
                    customForm.put(Constants.ID, form.getClinicalWorkflowId());
                    customizedModules.add(customForm);
                }
            }
        }
        response.setCustomizedWorkflow(customizedModules);
        return accountConsentForms;
    }

    /**
     * <p>
     * This method is used to set clinical workflow data for an account and returns a list of unselected workflow names.
     * </p>
     *
     * @param account  {@link Account} The Account contains information about the user's account and their selected
     *                 clinical workflows is given
     * @param token    {@link String} The token is likely an authentication token that is used to authorize the API
     *                 requests being made to the server is given
     * @param tenantId {@link Long} The ID of the tenant for which the clinical workflows are being set is given
     * @param response {@link StaticDataDTO} The StaticData is being modified and returned by the method is given
     * @return {@link List} The method returns a List of unselected workflow names
     */
    private List<String> setClinicalWorkflowData(Account account, String token, Long tenantId, StaticDataDTO response) {
        Map<String, Boolean> clinicalWorkflowResponse = new HashMap<>();
        for (AccountWorkflow accountWorkflow : account.getClinicalWorkflows()) {
            clinicalWorkflowResponse.put(accountWorkflow.getWorkflow(), true);
        }
        List<AccountWorkflow> workflows = adminApiInterface.getAllAccountWorkFlows(token, tenantId);
        List<String> unselectedWorkflowNames = new ArrayList<>();
        for (AccountWorkflow workflow : workflows) {
            if (!clinicalWorkflowResponse.containsKey(workflow.getWorkflow())) {
                unselectedWorkflowNames.add(workflow.getWorkflow());
            }
        }
        response.setClinicalWorkflow(clinicalWorkflowResponse);
        return unselectedWorkflowNames;
    }

    /**
     * <p>
     * This method is used to constructs region customization by setting form input data based on account consent forms
     * and removing unselected form data.
     * </p>
     *
     * @param unselectedWorkflowNames {@link List} The list of workflow names that have been unselected by the user is
     *                                given
     * @param response                {@link StaticDataDTO} The StaticData contains data to be customized is given
     * @param accountConsentForms     {@link List} The list of AccountCustomization representing consent forms for an
     *                                account is given
     * @param regionCustomizations    {@link List} The list of RegionCustomization contains customization data for
     *                                different regions is given
     */
    private void constructRegionCustomization(List<String> unselectedWorkflowNames, StaticDataDTO response,
                                              List<AccountCustomization> accountConsentForms, List<RegionCustomization> regionCustomizations) {
        Map<String, String> accountConsentFormTypes = new HashMap<>();
        if (!accountConsentForms.isEmpty()) {
            accountConsentForms.forEach(consentForms ->
                    accountConsentFormTypes.put(consentForms.getType(), consentForms.getFormInput())
            );
        }
        Map<String, String> assessment = new HashMap<>();
        Map<String, String> enrollment = new HashMap<>();
        Map<String, String> screening = new HashMap<>();
        if (!Objects.isNull(regionCustomizations) && !regionCustomizations.isEmpty()) {
            for (RegionCustomization customization : regionCustomizations) {
                if (customization.getCategory().equals(Constants.INPUT_FORM)) {
                    if (!unselectedWorkflowNames.isEmpty()) {
                        removeUnselectedFormData(customization, unselectedWorkflowNames);
                    }
                } else if (accountConsentFormTypes.containsKey(customization.getType())) {
                    customization.setFormInput(
                            accountConsentFormTypes.get(customization.getType())
                    );
                }
                setFormInputData(assessment, enrollment, screening, customization);
            }
        }
        response.setEnrollment(enrollment);
        response.setAssessment(assessment);
        response.setScreening(screening);
    }

    /**
     * <p>
     * This method is used to removes unselected form data from a JSON object based on a list of unselected
     * workflow names.
     * </p>
     *
     * @param customization           {@link RegionCustomization} The RegionCustomization object contains form input
     *                                data in JSON format is given
     * @param unselectedWorkflowNames {@link List} The list of String values representing the names of the workflows
     *                                that have been unselected by the user is given
     */
    protected void removeUnselectedFormData(RegionCustomization customization, List<String> unselectedWorkflowNames) {
        try {
            JSONObject json = new JSONObject(customization.getFormInput());
            JSONArray formLayoutArray = json.optJSONArray(Constants.FORM_LAYOUT);
            JSONArray updatedFormLayoutArray = new JSONArray();
            for (int i = 0; i < formLayoutArray.length(); i++) {        // List<Map<String,>>

                JSONObject explorerObject = formLayoutArray.getJSONObject(i);
                if (!(unselectedWorkflowNames.contains(explorerObject.optString(Constants.ID, Constants.EMPTY))
                        || unselectedWorkflowNames
                        .contains(explorerObject.optString(Constants.FAMILY, Constants.EMPTY)))) {
                    updatedFormLayoutArray.put(explorerObject);
                }
            }
            json.put(Constants.FORM_LAYOUT, updatedFormLayoutArray);
            customization.setFormInput(json.toString());
        } catch (Exception e) {
            Logger.logError(" Error while constructing JSON object : " + e.getMessage());
            throw new Validation(1001);
        }
    }

    /**
     * <p>
     * This method is used to set form input data for assessment, enrollment, and screening based on the
     * customization type.
     * </p>
     *
     * @param assessment    {@link Map} The map containing data related to an assessment form is given
     * @param enrollment    {@link Map} The map contains key-value pairs of enrollment data is given
     * @param screening     {@link Map} The map containing screening data is given
     * @param customization {@link RegionCustomization} The RegionCustomization contains information about a specific
     *                      customization for a region is given
     */
    protected void setFormInputData(Map<String, String> assessment, Map<String, String> enrollment,
                                    Map<String, String> screening, RegionCustomization customization) {
        if (customization.getType().equalsIgnoreCase(Constants.SCREENING)) {
            screening.put(customization.getCategory(), customization.getFormInput());
        }
        if (customization.getType().equalsIgnoreCase(Constants.ENROLLMENT)) {
            enrollment.put(customization.getCategory(), customization.getFormInput());
        }
        if (customization.getType().equalsIgnoreCase(Constants.ASSESSMENT)) {
            assessment.put(customization.getCategory(), customization.getFormInput());
        }
    }

    /**
     * <p>
     * This method is used to retrieves mental health static data based on country and culture IDs, constructs a map of
     * questions, and returns a list of maps containing the question type and JSON string of the questions.
     * </p>
     *
     * @param countryId {@link Long} The ID of the country that need to set is given
     * @param cultureId {@link Long} The ID of the culture that need to set is given
     * @return {@link List} The method returns a list of maps, where each map contains
     * two key-value pairs: "type" and "questions"
     */
    public List<Map<String, String>> getMentalHealthStaticData(Long countryId, Long cultureId) {
        Map<String, Map<Long, String>> cultureValuesMap;
        cultureValuesMap = Constants.CULTURE_VALUES_MAP.get(cultureId);
        cultureMap = cultureValuesMap.get(Constants.CULTURE_VALUE_MODEL_QUESTIONS);
        List<ModelQuestions> modelQuestions = modelQuestionsService.getModelQuestions(countryId);
        if (Objects.isNull(modelQuestions) || modelQuestions.isEmpty()) {
            modelQuestions = modelQuestionsService.getModelQuestionsByIsDefault();
        }
        modelQuestions.forEach(question -> question.setCultureValue(cultureMap.get(question.getId())));
        Map<String, List<ModelQuestions>> questions = new HashMap<>();
        for (ModelQuestions question : modelQuestions) {
            if (!questions.containsKey(question.getType())) {
                questions.put(question.getType(), new ArrayList<>());
            }
            questions.get(question.getType()).add(question);
        }

        List<Map<String, String>> response = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (List<ModelQuestions> question : questions.values()) {
            try {
                response.add(Map.of(Constants.TYPE, question.get(Constants.ZERO).getType(), Constants.QUESTIONS,
                        objectMapper.writeValueAsString(question)));
            } catch (JsonProcessingException e) {
                Logger.logError(" Error while construct JSON object : " + e.getMessage());
                throw new Validation(1001);
            }
        }
        return response;
    }

    /**
     * {@inheritDoc}
     */
    public MedicalReviewStaticDataDTO getMedicalReviewStaticData(Long cultureId) {
        cultureId = validateCulture(cultureId);
        MedicalReviewStaticDataDTO response = new MedicalReviewStaticDataDTO();
        Map<String, Map<Long, String>> cultureValuesMap = Constants.CULTURE_VALUES_MAP.get(cultureId);
        setMedicalReviewMeta(cultureValuesMap, response);
        setLifestyleMeta(cultureValuesMap, response, cultureId);
        setFrequencyMeta(cultureValuesMap, response);
        return response;
    }

    /**
     * <p>
     * This method is used to set frequency meta data for a medical review static data DTO by retrieving
     * frequencies and culture values from a map and setting them in the DTO.
     * </p>
     *
     * @param cultureValuesMap {@link Map} The map containing culture-specific values for different types of data
     *                         is given
     * @param response         {@link MedicalReviewStaticDataDTO} The MedicalReviewStaticData is used to store and
     *                         retrieve medical review static data is given
     */
    private void setFrequencyMeta(Map<String, Map<Long, String>> cultureValuesMap,
                                  MedicalReviewStaticDataDTO response) {
        cultureMap = cultureValuesMap.get(Constants.CULTURE_VALUE_FREQUENCY);
        List<Frequency> frequencies = frequencyService.getFrequencies();
        frequencies.forEach(frequency -> frequency.setCultureValue(cultureMap.get(frequency.getId())));

        List<Map<String, String>> forms = Constants.FREQUENCIES.values().stream()
                .map(frequency -> Map.of(Constants.LABLE_NAME, frequency.get(Constants.LABEL.toUpperCase()),
                        Constants.FREQ_KEY, frequency.get(Constants.FREQ_KEY.toUpperCase()))).toList();

        List<String> options = frequencies.stream().filter(frequency -> frequency.getType().equals(Constants.DEFAULT))
                .map(Frequency::getCultureValue).toList();
        response.setTreatmentPlanFormData(Map.of(Constants.FORMS, forms, Constants.OPTIONS, options));
    }

    /**
     * <p>
     * This method is used to set lifestyle meta data for a medical review response based on culture values and
     * lifestyle questions and answers.
     * </p>
     *
     * @param cultureValuesMap {@link Map} The map containing culture-specific values for different categories, such as
     *                         lifestyle questions and answers is given
     * @param response         {@link MedicalReviewStaticDataDTO} The MedicalReviewStaticData will be updated with
     *                         lifestyle information is given
     * @param cultureId        {@link Long} The ID of a culture that need to set is given
     */
    private void setLifestyleMeta(Map<String, Map<Long, String>> cultureValuesMap,
                                  MedicalReviewStaticDataDTO response, Long cultureId) {
        Map<String, Map<Long, Object>> jsonCultureValuesMap = Constants.JSON_CULTURE_VALUES_MAP.get(cultureId);
        cultureMap = cultureValuesMap.get(Constants.CULTURE_VALUE_LIFESTYLE_QUESTIONS);
        List<Lifestyle> lifestyles = lifestyleService.getLifestyles();
        lifestyles.forEach(lifestyle -> lifestyle.setCultureValue(cultureMap.get(lifestyle.getId())));

        jsonCultureMap = jsonCultureValuesMap.get(Constants.CULTURE_VALUE_LIFESTYLE_ANSWERS);
        lifestyles.forEach(lifestyle -> lifestyle.setCultureValueAnswers((jsonCultureMap.get(lifestyle.getId()))));
        response.setLifestyle(lifestyles);
    }

    /**
     * <p>
     * This method is used to set culture values for various medical review data objects in a response object.
     * </p>
     *
     * @param cultureValuesMap {@link Map} The map that contains culture-specific values for different medical review
     *                         categories such as comorbidity, complaints, current medication, complications, and
     *                         physical examination is given
     * @param response         {@link MedicalReviewStaticDataDTO} The MedicalReviewStaticData will be populated with
     *                         data from the cultureValuesMap is given
     */
    private void setMedicalReviewMeta(Map<String, Map<Long, String>> cultureValuesMap,
                                      MedicalReviewStaticDataDTO response) {
        cultureMap = cultureValuesMap.get(Constants.CULTURE_VALUE_COMORBIDITIES);
        List<Comorbidity> comorbidityList = comorbidityService.getComorbidityList();
        comorbidityList.forEach(comorbidity -> comorbidity.setCultureValue(cultureMap.get(comorbidity.getId())));
        response.setComorbidity(comorbidityList);

        cultureMap = cultureValuesMap.get(Constants.CULTURE_VALUE_COMPLAINTS);
        List<Complaints> compliantList = complaintsService.getCompliantList();
        compliantList.forEach(compliant -> compliant.setCultureValue(cultureMap.get(compliant.getId())));
        response.setComplaints(compliantList);

        cultureMap = cultureValuesMap.get(Constants.CULTURE_VALUE_CURRENT_MEDICATION);
        List<CurrentMedication> currentMedications = currentMedicationService
                .getCurrentMedicationByIds(cultureMap.keySet());
        currentMedications.forEach(medication -> medication.setCultureValue(cultureMap.get(medication.getId())));
        response.setCurrentMedication(currentMedications);

        cultureMap = cultureValuesMap.get(Constants.CULTURE_VALUE_COMPLICATIONS);
        List<Complication> complications = complicationService.getComplications();
        complications.forEach(complication -> complication.setCultureValue(cultureMap.get(complication.getId())));
        response.setComplications(complications);

        cultureMap = cultureValuesMap.get(Constants.CULTURE_VALUE_PHYSICAL_EXAMINATION);
        List<PhysicalExamination> physicalExaminations = physicalExaminationService.getPhysicalExamination();
        physicalExaminations.forEach(physicalExam -> physicalExam.setCultureValue(cultureMap.get(physicalExam.getId())));
        response.setPhysicalExamination(physicalExaminations);
    }

    /**
     * {@inheritDoc}
     */
    public MetaFormDTO getMetaFormData(String form) {
        FormMetaUi obj = formMetaService.getMetaForms(form);
        if (null == obj) {
            return null;
        }
        return new MetaFormDTO(obj.getId(), obj.getFormName(),
                obj.getComponents());
    }

    /**
     * {@inheritDoc}
     */
    public void clearStaticData() {
        Constants.COMPLAINTS.clear();
        Constants.NUTRITION_LIFESTYLES.clear();
        Constants.PHYSICAL_EXAMINATIONS.clear();
        Constants.FREQUENCY_LIST.clear();
        Constants.LIFESTYLES.clear();
        Constants.DOSAGE_FORMS.clear();
        Constants.DIAGNOSIS.clear();
        Constants.DOSAGE_FREQUENCIES.clear();
        Constants.MEDICAL_COMPLIANCES.clear();
        Constants.MODEL_QUESTIONS.clear();
        Constants.RISK_ALGORITHMS.clear();
        Constants.REASONS.clear();
        Constants.SYMPTOMS.clear();
        Constants.SIDE_MENUS.clear();
        Constants.COMORBIDITIES.clear();
        Constants.COMPLICATIONS.clear();
        Constants.FORM_META_UIS.clear();
        Constants.CULTURE_VALUES_MAP.clear();
        Constants.JSON_CULTURE_VALUES_MAP.clear();
    }

    /**
     * <p>
     * This method is used to validate a culture ID and returns a default culture ID if the input is null or zero.
     * </p>
     *
     * @param cultureId {@link Long} The id of the culture that need to be validated is given
     * @return cultureId {@link Long} The validated culture id is returned
     */
    private Long validateCulture(Long cultureId) {
        cultureId = (Objects.isNull(cultureId) || Constants.ZERO == cultureId)
                ? cultureService.getCultureByName(Constants.DEFAULT_CULTURE_VALUE).getId()
                : cultureId;
        if (Constants.CULTURE_VALUES_MAP.isEmpty() || Constants.JSON_CULTURE_VALUES_MAP.isEmpty()) {
            cultureService.getCultureValues();
        }
        return cultureId;
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkAppVersion(String appVersionReq) {
        return (appVersion.equals(appVersionReq));
    }
}
