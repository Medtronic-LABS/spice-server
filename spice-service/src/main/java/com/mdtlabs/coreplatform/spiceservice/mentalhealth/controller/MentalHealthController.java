package com.mdtlabs.coreplatform.spiceservice.mentalhealth.controller;

import com.mdtlabs.coreplatform.common.annotations.TenantValidation;
import com.mdtlabs.coreplatform.common.model.dto.spice.MentalHealthDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.MentalHealth;
import com.mdtlabs.coreplatform.spiceservice.mentalhealth.service.MentalHealthService;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * This class is a controller class to perform operation on mental health entity.
 *
 * @author Karthick Murugesan created on Feb 07, 2023
 */
@RestController
@RequestMapping("/mentalhealth")
public class MentalHealthController {

    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    private MentalHealthService mentalHealthService;

    /**
     * This method is used to add a new mental health for a patient.
     *
     * @param mentalHealthDto {@link MentalHealthDTO} entity
     * @return {@link SuccessResponse<MentalHealthDTO>} MentalHealth Entity.
     */
    @PostMapping("/create")
    @TenantValidation
    public SuccessResponse<MentalHealthDTO> createMentalHealth(@RequestBody MentalHealthDTO mentalHealthDto) {
        MentalHealth mentalHealth = mapper.map(mentalHealthDto, MentalHealth.class);
        mentalHealth = mentalHealthService.createOrUpdateMentalHealth(mentalHealth);
        MentalHealthDTO mentalHealthResponse = null;
        if (!Objects.isNull(mentalHealth)) {
            mentalHealthResponse = mapper.map(mentalHealth, MentalHealthDTO.class);
        }
        return new SuccessResponse<>(SuccessCode.MENTAL_HEALTH_SAVE, mentalHealthResponse, HttpStatus.CREATED);
    }

    /**
     * This method is used to update a mental health for a patient.
     *
     * @param mentalHealthDto {@link MentalHealthDTO} mentalHealthDto
     * @return {@link SuccessResponse<MentalHealthDTO>} MentalHealth Entity.
     */
    @PostMapping("/update")
    @TenantValidation
    public SuccessResponse<MentalHealthDTO> updateMentalHealth(@RequestBody MentalHealthDTO mentalHealthDto) {
        MentalHealth mentalHealth = mapper.map(mentalHealthDto, MentalHealth.class);
        mentalHealth = mentalHealthService.createOrUpdateMentalHealth(mentalHealth);
        MentalHealthDTO mentalHealthResponse = null;
        if (!Objects.isNull(mentalHealth)) {
            mentalHealthResponse = mapper.map(mentalHealth, MentalHealthDTO.class);
        }
        return new SuccessResponse<>(SuccessCode.MENTAL_HEALTH_UPDATE, mentalHealthResponse, HttpStatus.OK);
    }

    /**
     * This method is used to get a mental health details of a patient.
     *
     * @param requestData {@link RequestDTO} request DTO
     * @return {@link SuccessResponse<MentalHealthDTO>} MentalHealth Entity.
     */
    @PostMapping("/details")
    @TenantValidation
    public SuccessResponse<MentalHealthDTO> getMentalHealthDetails(@RequestBody RequestDTO requestData) {
        return new SuccessResponse<>(SuccessCode.MENTAL_HEALTH_DETAILS,
                mentalHealthService.getMentalHealthDetails(requestData), HttpStatus.OK);
    }
}
