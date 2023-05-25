package com.mdtlabs.coreplatform.spiceservice.metadata.controller;

import com.mdtlabs.coreplatform.common.controller.GenericTenantController;
import com.mdtlabs.coreplatform.common.model.entity.spice.ClassificationBrand;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ClassificationBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * Classification Brand Controller used to perform any action in the classification brand module like read and
 * write.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@RestController
@RequestMapping(value = "/classification-brand")
public class ClassificationBrandController extends GenericTenantController<ClassificationBrand> {

    @Autowired
    private ClassificationBrandService classificationBrandService;

    /**
     * <p>
     * This Java function returns a list of ClassificationBrand objects based on the provided countryId
     * and classificationId parameters.
     * </p>
     *
     * @param countryId        {@link Long} The countryId parameter is a Long type variable that represents the unique
     *                         identifier of a country
     * @param classificationId {@link Long} The parameter "classificationId" is a Long data type and it is used to
     *                         identify the classification for which the list of ClassificationBrand objects needs to be
     *                         retrieved.
     * @return {@link List<ClassificationBrand>} A list of `ClassificationBrand` objects is being returned
     */
    @GetMapping("/list")
    public List<ClassificationBrand> getByCountryAndClassificationId(
            @RequestParam("countryId") Long countryId, @RequestParam("classificationId") Long classificationId) {
        return classificationBrandService.getByCountryAndClassificationId(countryId, classificationId);
    }
}
