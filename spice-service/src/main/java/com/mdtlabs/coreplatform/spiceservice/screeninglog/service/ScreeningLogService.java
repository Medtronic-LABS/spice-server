package com.mdtlabs.coreplatform.spiceservice.screeninglog.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ScreeningLogDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.ScreeningResponseDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.ScreeningLog;

/**
 * <p>
 * ScreeningLogService is a java interface which defines three methods creating and retrieving screening log.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface ScreeningLogService {

    /**
     * <p>
     * This method is used to create a screening log based on the information provided in a ScreeningLogDTO object.
     * </p>
     *
     * @param screeningLogRequest {@link ScreeningLogDTO} The screening log request contains the information needed
     *                            to create a ScreeningLog object, such as the date and time of the screening, the name
     *                            of the patient being screened, the type of screening being performed, and any results
     *                            or notes from the screening is given
     * @return {@link ScreeningLog} The created screening log for given screening log details is returned
     */
    ScreeningLog createScreeningLog(ScreeningLogDTO screeningLogRequest);

    /**
     * <p>
     * This method is used to get the latest screening log for a patient with a given track ID.
     * </p>
     *
     * @param patientTrackId The patientTrackId  is used to retrieve a specific screening log for a patient is given
     * @return {@link ScreeningLog} The screening log for the given patient track ID is returned
     */
    ScreeningLog getByIdAndIsLatest(long patientTrackId);

    /**
     * <p>
     * This method is used to get screening request details.
     * </p>
     *
     * @param requestData {@link RequestDTO} The RequestDTO contains the necessary information required to
     *                    retrieve the screening details, such as the screening ID, user ID, and any other
     *                    relevant data needed to retrieve the screening details is given
     * @return {@link ScreeningResponseDTO} The screening response for the given request is returned
     */
    ScreeningResponseDTO getScreeningDetails(RequestDTO requestData);
}
