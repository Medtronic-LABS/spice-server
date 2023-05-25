package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.SideMenu;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.SideMenuRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.SideMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * This service class contain all the business logic for SideMenu module and perform
 * all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class SideMenuServiceImpl extends GenericServiceImpl<SideMenu> implements SideMenuService {

    @Autowired
    private SideMenuRepository sideMenuRepository;

    /**
     * {@inheritDoc}
     */
    public List<SideMenu> getSideMenus() {
        if (Constants.SIDE_MENUS.isEmpty()) {
            Constants.SIDE_MENUS.addAll(sideMenuRepository.findByIsDeletedFalseAndIsActiveTrue());
        }

        return Constants.SIDE_MENUS;
    }

    /**
     * {@inheritDoc}
     */
    public List<SideMenu> getSideMenus(List<String> userRoles) {
        if (Constants.SIDE_MENUS.isEmpty()) {
            Constants.SIDE_MENUS.addAll(sideMenuRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.SIDE_MENUS.stream().filter(obj -> userRoles.contains(obj.getRoleName())).toList();
    }
}
