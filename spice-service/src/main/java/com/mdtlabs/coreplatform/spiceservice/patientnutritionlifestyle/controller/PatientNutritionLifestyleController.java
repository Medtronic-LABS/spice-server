package com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.controller;

import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientNutritionLifestyleDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PatientNutritionLifestyleRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientNutritionLifestyle;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.patientnutritionlifestyle.service.PatientNutritionLifestyleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * This class is a controller class to perform operation on Patient Nutrition
 * Lifestyle entity.
 * </p>
 *
 * @author Victor Jefferson Created on Feb 08 2023
 */
@RestController
@RequestMapping(value = "/patient-lifestyle")
@Validated
public class PatientNutritionLifestyleController {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private PatientNutritionLifestyleService patientNutritionLifestyleService;

    /**
     * <p>
     * This Java function adds a patient's nutrition and lifestyle information to the database.
     * </p>
     *
     * @param patientNutritionLifestyleRequestDTO {@link SuccessResponse<PatientNutritionLifestyle>} patientNutritionLifestyleRequestDTO is an object of
     *                                            type PatientNutritionLifestyleRequestDTO which is being passed as a request body in the POST
     *                                            request. It contains the data required to create a new PatientNutritionLifestyle object.
     * @return {@link PatientNutritionLifestyleRequestDTO} A SuccessResponse object containing a success code, the result of adding a patient
     * nutrition lifestyle using the patientNutritionLifestyleService, and an HTTP status code of 201
     * (CREATED).
     */
    @PostMapping("/create")
    @TenantValidation
    public SuccessResponse<PatientNutritionLifestyle> addPatientNutritionLifestyle(
            @RequestBody PatientNutritionLifestyleRequestDTO patientNutritionLifestyleRequestDTO) {
        return new SuccessResponse<>(SuccessCode.PATIENT_NUTRITION_LIFESTYLE_SAVE,
                patientNutritionLifestyleService.addPatientNutritionLifestyle(patientNutritionLifestyleRequestDTO),
                HttpStatus.CREATED);
    }

    /**
     * <p>
     * This Java function returns a list of patient nutrition and lifestyle information based on a
     * request
     * </p>
     *
     * @param request {@link RequestDTO} The "request" parameter is of type RequestDTO and is annotated with @RequestBody.
     *                This means that it is expected to receive a JSON object in the request body, which will be
     *                automatically deserialized into a RequestDTO object by Spring
     * @return {@link SuccessResponse<List<PatientNutritionLifestyle>>} A SuccessResponse object containing a list of PatientNutritionLifestyle objects, with a
     * success code and HTTP status of OK
     */
    @PostMapping("/list")
    @TenantValidation
    public SuccessResponse<List<PatientNutritionLifestyle>> getPatientNutritionLifeStyleList(@RequestBody RequestDTO request) {
        return new SuccessResponse(
                SuccessCode.PATIENT_NUTRITION_LIFESTYLE_LIST,
                patientNutritionLifestyleService.getPatientNutritionLifeStyleList(request),
                HttpStatus.OK
        );
    }


    /**
     * <p>
     * This function updates a patient's nutrition and lifestyle information and returns a success
     * resp
     * </p>
     *
     * @param patientNutritionLifestyleRequestDTO {@link PatientNutritionLifestyleRequestDTO} It is a request object that contains the updated
     *                                            information for a patient's nutrition and lifestyle. It is passed as a request body in the PUT
     *                                            request to update the patient's nutrition and lifestyle information.
     * @return {@link SuccessResponse<List<PatientNutritionLifestyle>>} A SuccessResponse object containing a list of PatientNutritionLifestyle objects and a
     * success code with an HTTP status of OK.
     */
    @PutMapping("/update")
    @TenantValidation
    public SuccessResponse<List<PatientNutritionLifestyle>> updatePatientNutritionLifestyle(
            @RequestBody PatientNutritionLifestyleRequestDTO patientNutritionLifestyleRequestDTO) {
        patientNutritionLifestyleService
                .updatePatientNutritionLifestyle(patientNutritionLifestyleRequestDTO);
        return new SuccessResponse<>(SuccessCode.PATIENT_NUTRITION_LIFESTYLE_UPDATE, HttpStatus.OK);
    }

    /**
     * <p>
     * This Java function updates the view status of a patient's nutrition and lifestyle information.
     * </p>
     *
     *  @param request {@link CommonRequestDTO} The request parameter is an object of type CommonRequestDTO which contains the
     * necessary information required to update the view status of a patient's nutrition and lifestyle
     * data. This object is passed as a request body in the PUT request to the "/update-view-status"
     * endpoint.
     * @return {@link SuccessResponse<PatientNutritionLifestyle>} A SuccessResponse object containing a success code, the updated
     * PatientNutritionLifestyle object, and an HTTP status code.
     */
    @PutMapping("/update-view-status")
    @TenantValidation
    public SuccessResponse<PatientNutritionLifestyle> updatePatientNutritionLifestyleView(
            @RequestBody CommonRequestDTO request) {
        return new SuccessResponse<>(SuccessCode.PATIENT_NUTRITION_LIFESTYLE_UPDATE_VIEW,
                patientNutritionLifestyleService.updatePatientNutritionLifestyleView(request), HttpStatus.OK);
    }

    /**
     * <p>
     * This Java function deletes a patient's nutrition and lifestyle information based on the provided
     * DTO.
     * </p>
     *
     * @param patientNutritionLifestyleDto {@link PatientNutritionLifestyleDTO} patientNutritionLifestyleDto is an object of type
     *                                     PatientNutritionLifestyleDTO which is being passed as a request body in the POST request
     * @return {@link SuccessResponse<PatientNutritionLifestyle>} A SuccessResponse object containing a success code, the result of calling the
     * removePatientNutritionLifestyle method of the patientNutritionLifestyleService object with a
     * PatientNutritionLifestyle object created from the patientNutritionLifestyleDto parameter, and an
     * HTTP status code of OK.
     */
    @PostMapping("/remove")
    @TenantValidation
    public SuccessResponse<PatientNutritionLifestyle> deletePatientNutritionLifestyle(
            @RequestBody PatientNutritionLifestyleDTO patientNutritionLifestyleDto) {
        return new SuccessResponse<>(SuccessCode.PATIENT_NUTRITION_LIFESTYLE_DELETE,
                patientNutritionLifestyleService.removePatientNutritionLifestyle(
                        modelMapper.map(patientNutritionLifestyleDto, PatientNutritionLifestyle.class)),
                HttpStatus.OK);
    }
}
