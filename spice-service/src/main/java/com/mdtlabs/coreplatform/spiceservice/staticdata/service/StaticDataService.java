package com.mdtlabs.coreplatform.spiceservice.staticdata.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.MedicalReviewStaticDataDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.MetaFormDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.StaticDataDTO;

/**
 * <p>
 * StaticDataService interface defines methods for retrieving and clearing static data.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface StaticDataService {

    /**
     * <p>
     * This method is used to retrieve a StaticData based on a given culture ID.
     * </p>
     *
     * @param cultureId {@link Long} The culture ID of the static data need to retrieve from
     *                  the database is given
     * @return {@link StaticDataDTO} The StaticData for the given culture ID is retrieved
     */
    StaticDataDTO getStaticData(Long cultureId);

    /**
     * <p>
     * This method is used to retrieve a MedicalReviewStaticData based on a given culture ID.
     * </p>
     *
     * @param cultureId {@link Long} The culture ID of the medical review static data need to retrieve from
     *                  the database is given
     * @return {@link MedicalReviewStaticDataDTO} The MedicalReviewStaticData for the given culture ID is retrieved
     */
    MedicalReviewStaticDataDTO getMedicalReviewStaticData(Long cultureId);

    /**
     * <p>
     * This method is used to retrieve meta form base on a given form.
     * </p>
     *
     * @param form {@link String} The form of the meta form that need to retrieve from the database is given
     * @return {@link MetaFormDTO} The meta form for the given form is retrieved
     */
    MetaFormDTO getMetaFormData(String form);

    /**
     * <p>
     * This method is used to clear static data.
     * </p>
     */
    void clearStaticData();

    /**
     * <p>
     * Validate app version for mobile API
     * </p>
     *
     * @param appVersion - app version from mobile
     * @return Boolean
     */
    boolean checkAppVersion(String appVersion);
}
