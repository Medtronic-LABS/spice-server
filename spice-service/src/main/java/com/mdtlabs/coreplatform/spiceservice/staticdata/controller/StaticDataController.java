package com.mdtlabs.coreplatform.spiceservice.staticdata.controller;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewStaticDataDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MetaFormDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.StaticDataDTO;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessCode;
import com.mdtlabs.coreplatform.spiceservice.message.SuccessResponse;
import com.mdtlabs.coreplatform.spiceservice.staticdata.service.StaticDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * The StaticDataController class contains methods for retrieving and clearing static data, as well as performing
 * a health check.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@RestController
@RequestMapping(value = "/static-data")
public class StaticDataController {

    @Value("${app.app-version}")
    private String appVersion;

    @Autowired
    private StaticDataService staticDataService;

    /**
     * <p>
     * This method is used to get static data based on a specified culture ID.
     * </p>
     *
     * @param cultureId {@link Long} The id is used to specify the culture for which the static data
     *                  is being requested is given
     * @return {@link SuccessResponse<StaticDataDTO>} The static data for the given culture id is returned with status
     */
    @GetMapping("/details")
    public SuccessResponse<StaticDataDTO> getStaticData(@RequestParam("cultureId") Long cultureId) {
        return new SuccessResponse<>(SuccessCode.GET_STATIC_DATA, staticDataService.getStaticData(cultureId),
                HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to get medical review static data based on a specified culture ID.
     * </p>
     *
     * @param cultureId {@link Long} The id is used to specify the culture for which the medical
     *                  review static data is being requested is given
     * @return {@link SuccessResponse<MedicalReviewStaticDataDTO>} The medical review static data for the given
     * culture id is returned with status
     */
    @GetMapping("/medical-review")
    public SuccessResponse<MedicalReviewStaticDataDTO> getMedicalReviewStaticData(@RequestParam("cultureId") Long cultureId) {
        return new SuccessResponse<>(SuccessCode.GET_MEDICAL_REVIEW_STATIC_DATA,
                staticDataService.getMedicalReviewStaticData(cultureId), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to returns metadata for a specified form as a SuccessResponse object.
     * </p>
     *
     * @param form {@link String} The name of the form for which the metadata is being requested is given
     * @return {@link SuccessResponse<MetaFormDTO>} The meta form for the given form name is returned with status
     */
    @GetMapping("/get-meta-form")
    public SuccessResponse<MetaFormDTO> getMetaFormData(@RequestParam(Constants.FORM) String form) {
        return new SuccessResponse<>(SuccessCode.GET_META_DATA,
                staticDataService.getMetaFormData(form), HttpStatus.OK);
    }

    /**
     * <p>
     * This method is used to clear static data
     * </p>
     */
    @GetMapping("/clear")
    public void clearStaticData() {
        staticDataService.clearStaticData();
    }

    /**
     * <p>
     * This method is used to return a success response with a boolean value indicating the
     * health status of the application.
     * </p>
     *
     * @return {@link SuccessResponse<Boolean>} Returns a boolean value of TRUE and status indicating that
     * the health check was successful
     */
    @GetMapping("/health-check")
    public SuccessResponse<Boolean> healthCheck() {
        return new SuccessResponse(SuccessCode.HEALTH_CHECK,
                Boolean.TRUE, HttpStatus.OK);
    }

    /**
     * <p>
     * Validate app version for mobile API
     * </p>
     *
     * @return User - response of the updated user
     */
    @PostMapping("/app-version")
    public SuccessResponse<Boolean> checkAppVersion(@RequestHeader("App-Version") String appVersionReq) {
        boolean response = staticDataService.checkAppVersion(appVersionReq);
        if (response) {
            return new SuccessResponse(SuccessCode.APP_VERSION_UPDATE, response, HttpStatus.OK);
        }
        return new SuccessResponse(SuccessCode.UPDATE_APP_VERSION, response
                , HttpStatus.OK, appVersion);
    }
}
