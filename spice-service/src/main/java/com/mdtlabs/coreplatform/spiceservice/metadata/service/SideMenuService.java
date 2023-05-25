package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.model.entity.spice.SideMenu;
import com.mdtlabs.coreplatform.common.service.GenericService;

import java.util.List;

/**
 * <p>
 * This an interface class for SideMenu module you can implemented this class in any
 * class.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface SideMenuService extends GenericService<SideMenu> {

    /**
     * <p>
     * Gets side menus based on roles
     * </p>
     *
     * @param userRoles {@link List<String>} userRoles is given
     * @return {@link List<SideMenu>} List of side menu entity is given
     */
    List<SideMenu> getSideMenus(List<String> userRoles);

    /**
     * <p>
     * Gets all side menus
     * </p>
     *
     * @return {@link List<SideMenu>} List of side menu entity is given
     */
    List<SideMenu> getSideMenus();
}
