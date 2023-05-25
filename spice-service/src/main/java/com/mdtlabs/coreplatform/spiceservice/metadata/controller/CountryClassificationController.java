package com.mdtlabs.coreplatform.spiceservice.metadata.controller;

import com.mdtlabs.coreplatform.common.controller.GenericTenantController;
import com.mdtlabs.coreplatform.common.model.entity.spice.CountryClassification;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.CountryClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * Country classification Controller used to perform any action in the country classification module like read and
 * write.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@RestController
@RequestMapping("/country-classification")
public class CountryClassificationController extends GenericTenantController<CountryClassification> {

    @Autowired
    private CountryClassificationService countryClassificationService;

    /**
     * <p>
     * This Java function returns a list of country classifications based on a given country ID.
     * </p>
     *
     * @param countryId {@link Long} The "countryId" parameter is a Long type parameter that is passed as a request
     *                  parameter in the URL. It is used to retrieve a list of "CountryClassification" objects based on
     *                  the specified countryId.
     * @return {@link List<CountryClassification>} A list of `CountryClassification` objects is being returned
     */
    @GetMapping("/list")
    public List<CountryClassification> getClassificationsByCountryId(@RequestParam("countryId") Long countryId) {
        return countryClassificationService.getClassificationsByCountryId(countryId);
    }
}
