package com.mdtlabs.coreplatform.spiceservice.customizedmodules.repository;

import com.mdtlabs.coreplatform.common.model.entity.spice.CustomizedModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <p>
 * This interface is used for maintaining connection between server and database
 * for CustomizedModules Entity.
 * </p>
 *
 * @author Niraimathi S
 * @since Jun 30, 2022
 */
public interface CustomizedModuleRepository extends JpaRepository<CustomizedModule, Long> {

    /**
     * <p>
     * Get list of CustomizedModule by patientTracker.
     * </p>
     *
     * @param patientTrackId {@link long} patient track id
     * @return {@link List<CustomizedModule> } CustomizedModule entity
     */
    List<CustomizedModule> findByPatientTrackId(long patientTrackId);
}
