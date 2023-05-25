package com.mdtlabs.coreplatform.spiceservice.metadata.controller;

import com.mdtlabs.coreplatform.common.controller.GenericController;
import com.mdtlabs.coreplatform.common.model.entity.Timezone;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Timezone Controller used to perform any action in the timezone module like read and
 * write.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@RestController
@RequestMapping("/timezone")
public class TimezoneController extends GenericController<Timezone> {

}
