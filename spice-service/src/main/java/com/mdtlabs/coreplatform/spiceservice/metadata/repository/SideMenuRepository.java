package com.mdtlabs.coreplatform.spiceservice.metadata.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.SideMenu;
import com.mdtlabs.coreplatform.common.repository.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * This interface is the Repository interface for Entity SideMenu and is
 * responsible for database operations for this entity.
 * </p>
 *
 * @author ubuntu
 */
@Repository
public interface SideMenuRepository extends GenericRepository<SideMenu> {

    /**
     * <p>
     * Gets List of side menus.
     * </p>
     *
     * @return {@link List<SideMenu>} List of SideMenu entity.
     */
    List<SideMenu> findByIsDeletedFalseAndIsActiveTrue();

}
