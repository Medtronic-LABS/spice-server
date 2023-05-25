package com.mdtlabs.coreplatform.spiceservice.common.mapper;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.BioDataDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.BioMetricsDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.OldScreeningLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ScreeningLogDTO;
import com.mdtlabs.coreplatform.common.model.entity.Site;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.GlucoseLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.ScreeningLog;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <p>
 * This class is for mapping the screening related information based on
 * the required condition check and set to entity.
 * </p>
 *
 * @author Karthick Murugesan created on Jun 30, 2022
 */
@Component
public class ScreeningMapper {

    /**
     * <p>
     * This function maps data from a ScreeningLogDTO object to a ScreeningLog object and sets
     * additional properties based on a Site object.
     * </p>
     *
     * @param screeningLogDto {@link ScreeningLogDTO} A data transfer object (DTO) containing information about a screening
     *                        log.
     * @param screeningLog    {@link ScreeningLog} an object of the ScreeningLog class that will be updated with the values
     *                        from the screeningLogDto parameter.
     * @param site            {@link Site} The site object contains information about the location where the screening log was
     *                        recorded, including the site ID, operating unit ID, country ID, account ID, county ID,
     *                        sub-county ID, and tenant ID.
     */
    public void mapScreeningLog(ScreeningLogDTO screeningLogDto, ScreeningLog screeningLog, Site site) {
        screeningLog.setIsLatest(Constants.BOOLEAN_TRUE);
        screeningLog.setIsReferAssessment(screeningLogDto.getIsReferAssessment());
        screeningLog.setType(screeningLogDto.getType());
        screeningLog.setCvdRiskScore(screeningLogDto.getCvdRiskScore());
        screeningLog.setCvdRiskLevel(screeningLogDto.getCvdRiskLevel());
        screeningLog.setDateOfBirth(screeningLogDto.getDateOfBirth());
        screeningLog.setLatitude(screeningLogDto.getLatitude());
        screeningLog.setLongitude(screeningLogDto.getLongitude());
        screeningLog.setDeviceInfoId(screeningLogDto.getDeviceInfoId());
        screeningLog.setCategory(screeningLogDto.getCategory());
        screeningLog.setScreeningDateTime(screeningLogDto.getScreeningDateTime());
        if (!Objects.isNull(screeningLogDto.getPhq4())) {
            screeningLog.setPhq4score(screeningLogDto.getPhq4().getPhq4Score());
            screeningLog.setPhq4RiskLevel(screeningLogDto.getPhq4().getPhq4RiskLevel());
            screeningLog.setPhq4MentalHealth(screeningLogDto.getPhq4().getPhq4MentalHealth());
        }
        screeningLog.setSiteId(site.getId());
        screeningLog.setOperatingUnitId(site.getOperatingUnit().getId());
        screeningLog.setCountryId(site.getCountryId());
        screeningLog.setAccountId(site.getAccountId());
        screeningLog.setCountyId(site.getCountyId());
        screeningLog.setSubCountyId(site.getSubCountyId());
        screeningLog.setTenantId(site.getTenantId());
        screeningLog.setIsBeforeDiabetesDiagnosis(screeningLogDto.getGlucoseLog().getIsBeforeDiabetesDiagnosis());
    }

    /**
     * <p>
     * The function maps data from a BioDataDTO object to a ScreeningLog object.
     * </p>
     *
     * @param screeningLog {@link ScreeningLog} an object of type ScreeningLog that contains information about a screening
     *                     log
     * @param bioData      {@link BioDataDTO} An object of type BioDataDTO that contains various personal information about a
     *                     person, such as their name, phone number, national ID, etc.
     */
    public void mapBioData(ScreeningLog screeningLog, BioDataDTO bioData) {
        screeningLog.setNationalId(bioData.getNationalId().toUpperCase());
        screeningLog.setPhoneNumber(bioData.getPhoneNumber());
        screeningLog.setLastName(bioData.getLastName().toUpperCase());
        screeningLog.setPhoneNumberCategory(bioData.getPhoneNumberCategory());
        screeningLog.setMiddleName(null == bioData.getMiddleName() ?
                null : bioData.getMiddleName().toUpperCase());
        screeningLog.setFirstName(bioData.getFirstName().toUpperCase());
        screeningLog.setLandmark(bioData.getLandmark());
        screeningLog.setPreferredName(bioData.getPreferredName());
        screeningLog.setIdType(bioData.getIdType());
        screeningLog.setOtherIdType(bioData.getOtherIdType());
    }

    /**
     * The function maps data from a ScreeningLogDTO object to a PatientTracker object.
     *
     * @param patientTracker {@link PatientTracker} an object of the class PatientTracker that will be updated with the values
     *                       from the other parameters.
     * @param screeningLog   {@link ScreeningLogDTO} An object of type ScreeningLogDTO that contains information about a patient's
     *                       screening.
     * @param site           {@link Site} An object of the Site class that contains information about the screening site where
     *                       the patient was screened.
     */
    public void mapPatientTracker(PatientTracker patientTracker, ScreeningLogDTO screeningLog,
                                  Site site) {
        patientTracker.setNationalId(screeningLog.getBioData().getNationalId());
        patientTracker.setFirstName(screeningLog.getBioData().getFirstName());
        patientTracker.setLastName(screeningLog.getBioData().getLastName());
        patientTracker.setDateOfBirth(screeningLog.getDateOfBirth());
        patientTracker.setPhoneNumber(screeningLog.getBioData().getPhoneNumber());
        patientTracker.setPatientStatus(Constants.SCREENED);
        patientTracker.setCountryId(screeningLog.getCountryId());
        patientTracker.setSiteId(site.getId());
        patientTracker.setOperatingUnitId(site.getOperatingUnit().getId());
        patientTracker.setAccountId(site.getAccountId());
        patientTracker.setTenantId(site.getTenantId());
        patientTracker.setAge(screeningLog.getBioMetrics().getAge());
        patientTracker.setGender(screeningLog.getBioMetrics().getGender());
        patientTracker.setIsRegularSmoker(screeningLog.getBioMetrics().getIsRegularSmoker());
        patientTracker.setHeight(screeningLog.getBioMetrics().getHeight());
        patientTracker.setWeight(screeningLog.getBioMetrics().getWeight());
        patientTracker.setBmi(screeningLog.getBioMetrics().getBmi());
        patientTracker.setIsScreening(Constants.BOOLEAN_TRUE);
    }

    /**
     * <p>
     * This function constructs a new ScreeningLogDTO object by copying values from an
     * OldScreeningLogDTO object.
     * </p>
     *
     * @param screeningLogDto            {@likn OldScreeningLogDTO} an object of type OldScreeningLogDTO that contains data related to a
     *                                   screening log
     * @param constructedScreeningLogDto {@link ScreeningLogDTO} The object of type ScreeningLogDTO that will be constructed
     *                                   with the values from the OldScreeningLogDTO object.
     */
    public void constructScreeningLogDto(OldScreeningLogDTO screeningLogDto,
                                         ScreeningLogDTO constructedScreeningLogDto) {
        constructedScreeningLogDto.setCategory(screeningLogDto.getCategory());
        constructedScreeningLogDto.setDateOfBirth(screeningLogDto.getDate_of_birth());
        constructedScreeningLogDto.setLongitude(screeningLogDto.getLongitude());
        constructedScreeningLogDto.setCvdRiskLevel(screeningLogDto.getCvd_risk_level());
        constructedScreeningLogDto.setIsReferAssessment(screeningLogDto.getRefer_assessment());
        constructedScreeningLogDto.setScreeningDateTime(screeningLogDto.getScreening_date_time());
        constructedScreeningLogDto.setLatitude(screeningLogDto.getLatitude());
        constructedScreeningLogDto.setUnitMeasurement(screeningLogDto.getUnit_measurement());
        constructedScreeningLogDto.setType(screeningLogDto.getType());
        constructedScreeningLogDto.setCvdRiskScore(screeningLogDto.getCvd_risk_score());
        constructedScreeningLogDto.setCvdRiskScoreDisplay(screeningLogDto.getCvd_risk_score_display());
    }

    /**
     * <p>
     * The function maps biometric data from a DTO to a screening log object.
     * </p>
     *
     * @param screeningLog {@link ScreeningLog} an object of type ScreeningLog, which is being updated with biometric data.
     * @param biometrics   {@link BioMetricsDTO} A DTO (Data Transfer Object) containing biometric data such as gender, weight,
     *                     age, height, BMI, and other health-related information.
     */
    public void mapBiometricsData(ScreeningLog screeningLog, BioMetricsDTO biometrics) {
        screeningLog.setGender(biometrics.getGender());
        screeningLog.setWeight(biometrics.getWeight());
        screeningLog.setPhysicallyActive(biometrics.getIsphysicallyActive());
        screeningLog.setAge(biometrics.getAge());
        screeningLog.setHeight(biometrics.getHeight());
        screeningLog.setIsRegularSmoker(biometrics.getIsRegularSmoker());
        screeningLog.setIsFamilyDiabetesHistory(biometrics.isFamilyDiabetesHistory());
        screeningLog.setBmi(biometrics.getBmi());
        screeningLog.setBeforeGestationalDiabetes(biometrics.isBeforeGestationalDiabetes());
    }

    /**
     * <p>
     * The function maps the properties of a BpLog object to a ScreeningLog object.
     * </p>
     *
     * @param screeningLog {@link ScreeningLog} an object of the ScreeningLog class
     * @param bpLog        {@link BpLog} An object of type BpLog that contains blood pressure related data such as
     *                     isBeforeHtnDiagnosis, avgDiastolic, avgSystolic, avgPulse, bpArm, bpPosition, and bpLogDetails.
     */
    public void mapBpLog(ScreeningLog screeningLog, BpLog bpLog) {
        screeningLog.setIsBeforeHtnDiagnosis(bpLog.getIsBeforeHtnDiagnosis());
        screeningLog.setAvgDiastolic(bpLog.getAvgDiastolic());
        screeningLog.setAvgSystolic(bpLog.getAvgSystolic());
        screeningLog.setAvgPulse(bpLog.getAvgPulse());
        screeningLog.setBpArm(bpLog.getBpArm());
        screeningLog.setBpPosition(bpLog.getBpPosition());
        screeningLog.setBpLogDetails(bpLog.getBpLogDetails());
    }

    /**
     * <p>
     * The function maps the properties of a GlucoseLog object to a ScreeningLog object.
     * </p>
     *
     * @param screeningLog {@link ScreeningLog} an object of the ScreeningLog class that contains information about a
     *                     screening log.
     * @param glucoseLog   {@link GlucoseLog} An object of the GlucoseLog class that contains information about a glucose
     *                     measurement.
     */
    public void mapGlucoseLog(ScreeningLog screeningLog, GlucoseLog glucoseLog) {
        screeningLog.setIsBeforeDiabetesDiagnosis(glucoseLog.getIsBeforeDiabetesDiagnosis());
        screeningLog.setGlucoseDateTime(glucoseLog.getGlucoseDateTime());
        screeningLog.setLastMealTime(glucoseLog.getLastMealTime());
        screeningLog.setGlucoseType(glucoseLog.getGlucoseType());
        screeningLog.setGlucoseUnit(glucoseLog.getGlucoseUnit());
        screeningLog.setGlucoseValue(glucoseLog.getGlucoseValue());
    }

}
