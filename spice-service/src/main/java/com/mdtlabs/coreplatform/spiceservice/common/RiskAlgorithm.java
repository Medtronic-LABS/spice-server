package com.mdtlabs.coreplatform.spiceservice.common;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.RedRiskDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RiskAlgorithmDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientTracker;
import com.mdtlabs.coreplatform.common.model.entity.spice.Symptom;
import com.mdtlabs.coreplatform.spiceservice.symptom.service.SymptomService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * This class is for risk algorithm calculation.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class RiskAlgorithm {

    @Autowired
    private SymptomService symptomService;

    /**
     * <p>
     * This function calculates the risk level based on pregnancy status and blood pressure readings.
     * </p>
     *
     * @param riskAlgorithmDto {@link RiskAlgorithmDTO} It is an object of type RiskAlgorithmDTO which contains information
     *                         related to a patient's risk assessment, such as their blood pressure readings, symptoms, and
     *                         pregnancy status is given
     * @return {@link String} The method is returning a String variable named "riskLevel" is returned
     */
    public String getRiskLevelInAssessmentDbm(RiskAlgorithmDTO riskAlgorithmDto) {
        String riskLevel = null;
        if (!Objects.isNull(riskAlgorithmDto.getIsPregnant())
                && Boolean.TRUE.equals(riskAlgorithmDto.getIsPregnant())) {
            riskLevel = Constants.HIGH;

        } else if (!Objects.isNull(riskAlgorithmDto.getRiskLevel())) {
            riskAlgorithmDto.setRiskLevel(calculateRiskLevelFromBpReading(riskAlgorithmDto.getAvgSystolic(),
                    riskAlgorithmDto.getAvgDiastolic(), riskAlgorithmDto.getRiskLevel()));

            riskLevel = calculateRiskFromSymptomAndBpReading(riskAlgorithmDto);
        }
        return riskLevel;
    }

    /**
     * <p>
     * This Java function calculates the risk level based on average systolic and diastolic blood
     * pressure readings, and updates the risk level if certain conditions are met.
     * </p>
     *
     * @param avgSystolic       {@link Integer} The average systolic blood pressure of a patient is given
     * @param avgDiastolic      {@link Integer} The average diastolic blood pressure of a patient is given
     * @param existingRiskLevel {@link String} The current risk level of the patient, which is a String value is given
     * @return The method returns a String representing the calculated risk level based on the average
     * systolic and diastolic blood pressure readings, along with the existing risk level is returned
     */
    public String calculateRiskLevelFromBpReading(Integer avgSystolic, Integer avgDiastolic, String existingRiskLevel) {
        int systolicThreshold = Constants.BP_THRESHOLD_SYSTOLIC;
        int diastolicThreshold = Constants.BP_THRESHOLD_DIASTOLIC;
        String riskLevel;
        if (!Objects.isNull(avgSystolic) && !Objects.isNull(avgDiastolic)) {
            riskLevel = calculateRiskLevel(avgSystolic, avgDiastolic, existingRiskLevel, systolicThreshold,
                    diastolicThreshold, null);
            if (((avgSystolic >= 161 && avgSystolic <= 179) || (avgDiastolic >= 100 && avgDiastolic <= 109))
                    && StringUtils.equals(existingRiskLevel, Constants.HIGH)) {
                riskLevel = Constants.HIGHER_MODERATE;
            }

            if ((avgSystolic >= 180 && avgSystolic <= 199) || (avgDiastolic >= 110 && avgDiastolic <= 119)) {
                riskLevel = Constants.HIGHER_MODERATE;
            }
        } else {
            riskLevel = existingRiskLevel;
        }
        return riskLevel;
    }

    /**
     * <p>
     * This function calculates the risk level based on average systolic and diastolic blood pressure,
     * existing risk level, and threshold values.
     * </p>
     *
     * @param avgSystolic        {@link Integer} The average systolic blood pressure of a patient is given
     * @param avgDiastolic       {@link Integer} The average diastolic blood pressure of a patient is given
     * @param existingRiskLevel  {@link String} The current risk level of the patient, which is a String value is given
     * @param systolicThreshold  {@link int} The systolic blood pressure threshold used to determine the risk level is given
     * @param diastolicThreshold {@link int} The diastolic blood pressure threshold used to determine the risk level
     *                           of a patient is given
     * @param riskLevel          {@link String} The initial risk level before the method is called is given
     * @return {@link String} The method is returning a String variable named "riskLevel" is reurned
     */
    private String calculateRiskLevel(Integer avgSystolic, Integer avgDiastolic, String existingRiskLevel,
                                      int systolicThreshold, int diastolicThreshold, String riskLevel) {
        if (avgSystolic <= systolicThreshold && avgDiastolic <= diastolicThreshold) {
            riskLevel = Constants.LOW;
        }

        if (((avgSystolic >= 141 && avgSystolic <= 160) || (avgDiastolic >= 91 && avgDiastolic <= 99))
                && StringUtils.equals(existingRiskLevel, Constants.HIGH)) {
            riskLevel = Constants.MODERATE;
        }

        if ((avgSystolic > systolicThreshold && avgSystolic <= 179) || (avgDiastolic > diastolicThreshold
                && avgDiastolic <= 109 && (StringUtils.equals(existingRiskLevel, Constants.MODERATE)
                || StringUtils.equals(existingRiskLevel, Constants.LOW)))) {
            riskLevel = Constants.MODERATE;
        }
        return riskLevel;
    }

    /**
     * <p>
     * This Java function calculates the risk level of a patient based on their symptoms, blood
     * pressure readings, and glucose data.
     * </p>
     *
     * @param riskAlgorithmDto {@link RiskAlgorithmDTO} RiskAlgorithmDTO is a data transfer object that contains information
     *                         about a patient's symptoms, blood pressure readings, and glucose data is given
     * @return {@link String} The method is returning a String which represents the calculated risk level based on the
     * given symptoms and blood pressure readings is returned
     */
    public String calculateRiskFromSymptomAndBpReading(RiskAlgorithmDTO riskAlgorithmDto) {
        if (!Objects.isNull(riskAlgorithmDto.getSymptoms()) && !riskAlgorithmDto.getSymptoms().isEmpty()) {
            Map<String, Integer> patientSymptomsCount = getPatientSymptomsCategoriesCount(riskAlgorithmDto);
            String symptomRiskLevel = calculateRiskLevelFromSymptoms(patientSymptomsCount, riskAlgorithmDto);
            riskAlgorithmDto.setRiskLevel(symptomRiskLevel);
        }

        if (!Objects.isNull(riskAlgorithmDto.getAvgSystolic()) && (riskAlgorithmDto.getAvgSystolic() >= 200
                || riskAlgorithmDto.getAvgDiastolic() >= 120)) {
            riskAlgorithmDto.setRiskLevel(Constants.HIGH);
        }
        if (!Objects.isNull(riskAlgorithmDto.getGlucoseValue())) {
            riskAlgorithmDto.setRiskLevel(calculateRiskLevelFromGlucoseData(riskAlgorithmDto.getGlucoseValue(),
                    riskAlgorithmDto.getGlucoseType(), riskAlgorithmDto.getRiskLevel()));
        }
        return riskAlgorithmDto.getRiskLevel();

    }

    /**
     * <p>
     * This Java function returns a map of the count of symptoms in different categories based on a
     * given risk algorithm DTO.
     * </p>
     *
     * @param riskAlgorithmDTO {@link RiskAlgorithmDTO} It is an object of type RiskAlgorithmDTO which contains information
     *                         related to a risk algorithm. It is used to calculate the risk of a patient based on their
     *                         symptoms is given
     * @return {@link Map<String, Integer>} A Map<String, Integer> containing the count of symptoms in each category (categories 2,
     * 3, 4, and 5) for the given RiskAlgorithmDTO is returned
     */
    public Map<String, Integer> getPatientSymptomsCategoriesCount(RiskAlgorithmDTO riskAlgorithmDTO) {
        Map<String, List<Long>> symptomsCategories = Map.of(Constants.CATEGORY_TWO, new ArrayList<>(),
                Constants.CATEGORY_THREE, new ArrayList<>(), Constants.CATEGORY_FOUR, new ArrayList<>(),
                Constants.CATEGORY_FIVE, new ArrayList<>());
        List<Symptom> symptoms = symptomService.getSymptoms();
        for (Symptom symptom : symptoms) {
            if (!Constants.OTHER.equals(symptom.getName())) {
                addSymptomCategories(symptomsCategories, symptom);
            }
        }
        return calculateSymptomsCategoriesCount(riskAlgorithmDTO.getSymptoms(), symptomsCategories);

    }

    /**
     * <p>
     * The function adds symptom IDs to their respective categories in a map based on the categories
     * specified in the symptom object.
     * </p>
     *
     * @param symptomsCategories {@link Map<String, List<Long>>} A map that contains symptom categories as keys and lists of symptom
     *                           IDs as values is given
     * @param symptom            {@link Symptom} an object of type Symptom that contains information about a particular symptom,
     *                           including its ID and categories is returned
     */
    private void addSymptomCategories(Map<String, List<Long>> symptomsCategories, Symptom symptom) {
        if (!Objects.isNull(symptom.getCategories())
                && Boolean.TRUE.equals(symptom.getCategories().get(Constants.STRING_TWO))) {
            symptomsCategories.get(Constants.CATEGORY_TWO).add(symptom.getId());
        }
        if (!Objects.isNull(symptom.getCategories())
                && Boolean.TRUE.equals(symptom.getCategories().get(Constants.STRING_THREE))) {
            symptomsCategories.get(Constants.CATEGORY_THREE).add(symptom.getId());
        }
        if (!Objects.isNull(symptom.getCategories())
                && Boolean.TRUE.equals(symptom.getCategories().get(Constants.STRING_FOUR))) {
            symptomsCategories.get(Constants.CATEGORY_FOUR).add(symptom.getId());
        }
        if (!Objects.isNull(symptom.getCategories())
                && Boolean.TRUE.equals(symptom.getCategories().get(Constants.STRING_FIVE))) {
            symptomsCategories.get(Constants.CATEGORY_FIVE).add(symptom.getId());
        }
    }

    /**
     * <p>
     * The function calculates the count of symptoms in different categories for a given set of patient
     * symptoms and a map of symptom categories.
     * </p>
     *
     * @param patientSymptoms    {@link Set<Long>} A set of Long values representing the IDs of symptoms that a patient is
     *                           experiencing is given
     * @param symptomsCategories {@link Map<String, List<Long>> } A map where the keys are category names (strings) and the values are
     *                           lists of symptom IDs (longs) that belong to that category is given
     * @return {@link Map<String, Integer>} The method is returning a Map<String, Integer> object which contains the count of
     * patient symptoms in each category defined in the symptomsCategories map is returned
     */
    public Map<String, Integer> calculateSymptomsCategoriesCount(Set<Long> patientSymptoms,
                                                                 Map<String, List<Long>> symptomsCategories) {
        Map<String, Integer> symptomsCategoriesCount = new HashMap<>();
        symptomsCategoriesCount.put(Constants.CATEGORY_TWO_COUNT, Constants.ZERO);
        symptomsCategoriesCount.put(Constants.CATEGORY_THREE_COUNT, Constants.ZERO);
        symptomsCategoriesCount.put(Constants.CATEGORY_FOUR_COUNT, Constants.ZERO);
        symptomsCategoriesCount.put(Constants.CATEGORY_FIVE_COUNT, Constants.ZERO);

        for (Long symptomId : patientSymptoms) {
            if (symptomsCategories.get(Constants.CATEGORY_TWO).contains(symptomId)) {
                symptomsCategoriesCount.replace(Constants.CATEGORY_TWO_COUNT,
                        symptomsCategoriesCount.get(Constants.CATEGORY_TWO_COUNT) + Constants.ONE);
            }
            if (symptomsCategories.get(Constants.CATEGORY_THREE).contains(symptomId)) {
                symptomsCategoriesCount.replace(Constants.CATEGORY_THREE_COUNT,
                        symptomsCategoriesCount.get(Constants.CATEGORY_THREE_COUNT) + Constants.ONE);
            }
            if (symptomsCategories.get(Constants.CATEGORY_FOUR).contains(symptomId)) {
                symptomsCategoriesCount.replace(Constants.CATEGORY_FOUR_COUNT,
                        symptomsCategoriesCount.get(Constants.CATEGORY_FOUR_COUNT) + Constants.ONE);
            }
            if (symptomsCategories.get(Constants.CATEGORY_FIVE).contains(symptomId)) {
                symptomsCategoriesCount.replace(Constants.CATEGORY_FIVE_COUNT,
                        symptomsCategoriesCount.get(Constants.CATEGORY_FIVE_COUNT) + Constants.ONE);
            }

        }
        return symptomsCategoriesCount;
    }

    /**
     * <p>
     * The function calculates the risk level of a patient based on their symptoms and other health
     * data using a set of predefined rules.
     * </p>
     *
     * @param patientSymptomsCount {@link Map<String, Integer>} A map containing the count of different symptom categories for a
     *                             patient is given
     * @param riskAlgorithmDto     {@link RiskAlgorithmDTO} RiskAlgorithmDTO is an object that contains information related to the
     *                             patient's vital signs and other risk factors that can contribute to their overall risk level is given
     * @return {@link String} The method is returning a String value which represents the calculated risk level based
     * on the patient's symptoms count and the risk algorithm DTO is returned
     */
    public String calculateRiskLevelFromSymptoms(Map<String, Integer> patientSymptomsCount,
                                                 RiskAlgorithmDTO riskAlgorithmDto) {
        String symptomRiskLevel = riskAlgorithmDto.getRiskLevel();
        if (patientSymptomsCount.get(Constants.CATEGORY_FOUR_COUNT) >= Constants.ONE) {
            symptomRiskLevel = Constants.HIGHER_MODERATE;
        }
        if (patientSymptomsCount.get(Constants.CATEGORY_FIVE_COUNT) >= Constants.ONE
                || patientSymptomsCount.get(Constants.CATEGORY_TWO_COUNT) >= Constants.ONE) {
            symptomRiskLevel = Constants.HIGH;
        }
        if ((riskAlgorithmDto.getAvgSystolic() <= 90 || riskAlgorithmDto.getAvgDiastolic() <= 60)
                && patientSymptomsCount.get(Constants.CATEGORY_THREE_COUNT) >= Constants.ONE) {
            symptomRiskLevel = Constants.HIGH;
        }
        return symptomRiskLevel;
    }

    /**
     * <p>
     * The function calculates the risk level of a patient based on their glucose data and updates
     * the existing risk level accordingly.
     * </p>
     *
     * @param glucoseValue      {@link Double} A Double value representing the glucose level of a patient is given
     * @param glucoseType       {@link String} A string representing the type of glucose data, which can be either "RBS"
     *                          (Random Blood Sugar) or "FBS" (Fasting Blood Sugar) is given
     * @param existingRiskLevel {@link String} The current risk level of the patient based on their glucose data is given
     * @return {@link String} The method returns the updated risk level based on the glucose data provided is returned
     */
    public String calculateRiskLevelFromGlucoseData(Double glucoseValue, String glucoseType, String existingRiskLevel) {

        if (!existingRiskLevel.equals(Constants.HIGH) 
        && ((glucoseType.equals(Constants.RBS) && (glucoseValue >= 13 || glucoseValue < 4)) 
                || ((glucoseType.equals(Constants.FBS) && (glucoseValue > 11 || glucoseValue < 4)) 
                || (glucoseType.equals(Constants.RBS) && glucoseValue > 11)))) {
            existingRiskLevel = Constants.HIGH;
        }
        if ((glucoseType.equals(Constants.RBS) && glucoseValue > 10 && glucoseValue < 12.9) 
                || (glucoseType.equals(Constants.FBS) && glucoseValue > 7.8 && glucoseValue < 11)
                || (glucoseType.equals(Constants.RBS) && glucoseValue > 7.8 && glucoseValue < 11)) {
            switch (existingRiskLevel) {
                case Constants.LOW -> existingRiskLevel = Constants.GLUCOSE_MODERATE;
                case Constants.MODERATE -> existingRiskLevel = Constants.BOTH_MODERATE;
                case Constants.HIGHER_MODERATE -> existingRiskLevel = Constants.BOTH_HIGHER_MODERATE;
            }
        }
        return existingRiskLevel;
    }

    /**
     * <p>
     * This function calculates the risk level for a new patient based on their medical history and
     * risk factors.
     * </p>
     *
     * @param patientTracker {@link PatientTracker} An object of the class PatientTracker which contains information about a
     *                       patient's medical history and current health status is given
     * @param redRiskDto     {@link RedRiskDTO} A data transfer object containing information about the patient's red risk
     *                       factors, such as the number of comorbidities and the type of diabetes diagnosis control is given
     * @return {@link String} The method is returning a String value which represents the risk level for a new patient
     * based on their medical history and risk factors is returned
     * @author Niraimathi S
     */
    public String getRiskLevelForNewPatient(PatientTracker patientTracker, RedRiskDTO redRiskDto) {
        String riskLevel;
        if (!Objects.isNull(patientTracker.getIsPregnant())) {
            riskLevel = Constants.HIGH;
        } else if (Constants.ONE <= redRiskDto.getComorbiditiesCount() || Objects
                .equals(redRiskDto.getDiabetesDiagControlledType(), Constants.DIABETES_UNCONTROLLED_OR_POORLY_CONTROLLED)) {
            riskLevel = Constants.HIGH;
        } else {
            int riskFactorsCount = getRiskFactorsCount(patientTracker);
            riskLevel = calculateRiskFromBpAndRiskFactors(patientTracker, riskFactorsCount, redRiskDto);
        }
        return riskLevel;
    }

    /**
     * <p>
     * The function calculates the number of risk factors for a patient based on their gender, age,
     * smoking status, glucose levels, and BMI.
     * </p>
     *
     * @param patientTracker {@link PatientTracker} an object of the class PatientTracker which contains information about a
     *                       patient's gender, age, smoking habits, glucose levels, and BMI.
     * @return {@link int} The method is returning an integer value which represents the count of risk factors for
     * a patient based on their gender, age, smoking status, glucose level, and BMI.
     */
    public int getRiskFactorsCount(PatientTracker patientTracker) {
        int riskFactorCount = Constants.ZERO;
        if (patientTracker.getGender().equals(Constants.GENDER_MALE)) {
            riskFactorCount++;
        }

        if ((patientTracker.getGender().equals(Constants.GENDER_MALE) && patientTracker.getAge() >= 55)
                || (patientTracker.getGender().equals(Constants.GENDER_FEMALE) && patientTracker.getAge() >= 65)) {
            riskFactorCount++;
        }

        if (Boolean.TRUE.equals(patientTracker.getIsRegularSmoker())) {
            riskFactorCount++;
        }

        if (!Objects.isNull(patientTracker.getGlucoseType()) && patientTracker.getGlucoseType().equals(Constants.FBS)
                && patientTracker.getGlucoseValue() >= 5.6 && patientTracker.getGlucoseValue() <= 6.9) {
            riskFactorCount++;
        }
        if (patientTracker.getBmi() >= 30) {
            riskFactorCount++;
        }
        return riskFactorCount;
    }

    /**
     * <p>
     * This function calculates the risk level of a patient based on their blood pressure and number of
     * risk factors.
     * </p>
     *
     * @param patientTracker   {@link PatientTracker} an object that contains information about a patient's blood pressure
     *                         readings is given
     * @param riskFactorsCount {@link int} The number of risk factors present in the patient's medical history or
     *                         current condition is given
     * @param redRiskDto       {@link RedRiskDTO} It is an object of type RedRiskDTO which contains information related to the
     *                         patient's risk factors is given
     * @return {@link String}The method returns a String representing the calculated risk level based on the
     * patient's blood pressure and number of risk factors is returned
     */
    private String calculateRiskFromBpAndRiskFactors(PatientTracker patientTracker, int riskFactorsCount,
                                                     RedRiskDTO redRiskDto) {
        String riskLevel;
        if (patientTracker.getAvgSystolic() >= 180 || patientTracker.getAvgDiastolic() >= 110) {
            riskLevel = Constants.HIGH;
        } else if (patientTracker.getAvgSystolic() >= 160 || patientTracker.getAvgDiastolic() >= 100) {
            riskLevel = setsRiskLevel(riskFactorsCount, redRiskDto);
        } else if (patientTracker.getAvgSystolic() >= 140 || patientTracker.getAvgDiastolic() >= 90) {
            riskLevel = setRiskLevel(riskFactorsCount, redRiskDto);
        } else {
            riskLevel = setRiskLevelOnCount(riskFactorsCount, redRiskDto);
        }
        return riskLevel;
    }

    /**
     * <p>
     * The function sets the risk level based on the number of risk factors and diabetes diagnosis
     * control type.
     * </p>
     *
     * @param riskFactorsCount {@link int} An integer representing the number of risk factors present in a
     *                         patient's medical history is given
     * @param redRiskDto       {@link RedRiskDTO} An object of type RedRiskDTO which contains information related to a patient's
     *                         risk factors and diabetes diagnosis control type is given
     * @return {@link String} The method returns a String value representing the risk level, which can be either
     * "HIGH" or "MODERATE" is returned
     */
    private String setsRiskLevel(int riskFactorsCount, RedRiskDTO redRiskDto) {
        String riskLevel;
        if (riskFactorsCount > 0 || Objects.equals(redRiskDto.getDiabetesDiagControlledType(), Constants.DIABETES_WELL_CONTROLLED)
                || Objects
                .equals(redRiskDto.getDiabetesDiagControlledType(), Constants.PRE_DIABETES)) {
            riskLevel = Constants.HIGH;
        } else {
            riskLevel = Constants.MODERATE;
        }
        return riskLevel;
    }

    /**
     * <p>
     * This Java function sets a risk level based on the count of risk factors and a diabetes diagnosis
     * control type.
     * </p>
     *
     * @param riskFactorsCount The number of risk factors present in a patient's medical history or
     *                         current condition is given
     * @param redRiskDto       The "redRiskDto" parameter is likely an object of type "RedRiskDTO", which
     *                         probably contains information related to a patient's risk factors for a particular health
     *                         condition is given
     * @return The method is returning a String value which represents the risk level is returned
     */
    private String setRiskLevelOnCount(int riskFactorsCount, RedRiskDTO redRiskDto) {
        String riskLevel;
        if (riskFactorsCount >= 3
                || Objects
                .equals(redRiskDto.getDiabetesDiagControlledType(), Constants.DIABETES_WELL_CONTROLLED)) {
            riskLevel = Constants.MODERATE;
        } else {
            riskLevel = Constants.LOW;
        }
        return riskLevel;
    }

    /**
     * <p>
     * This function sets the risk level based on the number of risk factors and the diabetes diagnosis
     * control type.
     * </p>
     *
     * @param riskFactorsCount {@link int} The number of risk factors present in the patient's medical history or
     *                         current condition is given
     * @param redRiskDto       {@link RedRiskDTO} A data transfer object (DTO) containing information related to the patient's
     *                         diabetes diagnosis and control is given
     * @return {@link String} The method is returning a String value which represents the risk level of a patient
     * based on their risk factors count and diabetes diagnosis control type is returned
     */
    private String setRiskLevel(int riskFactorsCount, RedRiskDTO redRiskDto) {
        String riskLevel;
        if (riskFactorsCount >= 3
                || Objects
                .equals(redRiskDto.getDiabetesDiagControlledType(), Constants.DIABETES_WELL_CONTROLLED)) {
            riskLevel = Constants.HIGH;
        } else if (riskFactorsCount > 0 || Objects
                .equals(redRiskDto.getDiabetesDiagControlledType(), Constants.PRE_DIABETES)) {
            riskLevel = Constants.MODERATE;
        } else {
            riskLevel = Constants.LOW;
        }
        return riskLevel;
    }
}