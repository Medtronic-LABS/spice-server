package com.mdtlabs.coreplatform.spiceservice.metadata.controller;

import com.mdtlabs.coreplatform.common.controller.GenericController;
import com.mdtlabs.coreplatform.common.model.dto.spice.CultureDTO;
import com.mdtlabs.coreplatform.common.model.entity.Culture;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CultureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * Culture Controller used to perform any action in the culture module like read and
 * write.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@RestController
@RequestMapping("/culture")
public class CultureController extends GenericController<Culture> {

    @Autowired
    private CultureService cultureService;

    /**
     * <p>
     * To list the cultures.
     * </p>
     *
     * @return {@link SuccessResponse<List<CultureDTO>>} List of culture DTO.
     */
    @GetMapping("/list")
    public SuccessResponse<List<CultureDTO>> getCultures() {
        return new SuccessResponse(SuccessCode.GET_STATIC_DATA, cultureService.getAllCultures(),
                HttpStatus.OK);
    }
}
