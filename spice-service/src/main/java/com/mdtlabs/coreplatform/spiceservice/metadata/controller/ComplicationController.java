package com.mdtlabs.coreplatform.spiceservice.metadata.controller;

import com.mdtlabs.coreplatform.common.controller.GenericController;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Complication Controller used to perform any action in the complication module like read and
 * write.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@RestController
@RequestMapping("/complication")
public class ComplicationController extends GenericController<Complication> {

}
