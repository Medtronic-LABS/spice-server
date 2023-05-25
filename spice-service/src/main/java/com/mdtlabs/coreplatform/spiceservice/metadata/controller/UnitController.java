package com.mdtlabs.coreplatform.spiceservice.metadata.controller;

import com.mdtlabs.coreplatform.common.controller.GenericController;
import com.mdtlabs.coreplatform.common.model.entity.spice.Unit;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.UnitService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Unit Controller used to perform any action in the unit module like read and
 * write.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@RestController
@RequestMapping("/unit")
public class UnitController extends GenericController<Unit> {

    @Autowired
    UnitService unitService;

    /**
     * <p>
     * To list the units.
     * </p>
     *
     * @param {@link String} type - Unit type.
     * @return {@link SuccessResponse<List<Unit>>} List of unit.
     */
    @GetMapping(value = "/list/{type}")
    public List<Unit> getUnitsByType(@PathVariable("type") String type) {
        return unitService.getUnitsByType(type);
    }

}
