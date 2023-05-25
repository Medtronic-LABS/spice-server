package com.mdtlabs.coreplatform.spiceservice.prescription.service;

import com.mdtlabs.coreplatform.common.model.dto.spice.FillPrescriptionRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.FillPrescriptionResponseDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriberDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionHistoryResponse;
import com.mdtlabs.coreplatform.common.model.dto.spice.PrescriptionRequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.RequestDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.SearchRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.Prescription;
import com.mdtlabs.coreplatform.common.model.entity.spice.PrescriptionHistory;

import javax.validation.Valid;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * PrescriptionService is a Java interface for managing CRUD (Create, Read, Update, Delete)
 * operations for prescription customizations. It defines methods for creating, retrieving, updating, and
 * removing prescription customizations, as well as getting a list of prescription customizations based on
 * provided request details.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
public interface PrescriptionService {

    /**
     * This method is used to create or update a prescription based on the provided prescription request.
     *
     * @param prescriptionRequestDto {@link PrescriptionRequestDTO} The prescriptionRequest contains
     *                               the data required to create or update a prescription is given
     */
    void createOrUpdatePrescription(@Valid PrescriptionRequestDTO prescriptionRequestDto) throws IOException;

    /**
     * <p>
     * This method is used to retrieve a list of Prescription objects based on a request.
     * </p>
     *
     * @param prescriptionListRequestDto {@link RequestDTO} prescriptionListRequest contains the necessary information
     *                                   required to retrieve a list of prescriptions is given
     * @return {@link List<PrescriptionDTO>} The list of Prescription for the given prescription list request
     * is returned
     */
    List<PrescriptionDTO> getPrescriptions(RequestDTO prescriptionListRequestDto);

    /**
     * <p>
     * The method is used to return a PrescriptionHistoryResponse object containing prescription history data based on
     * the provided request.
     * </p>
     *
     * @param prescriptionListRequestDto {@link RequestDTO} The prescriptionListRequest contains information about a
     *                                   list of prescriptions that need to be removed is given
     * @return {@link PrescriptionHistoryResponse} The PrescriptionHistoryResponse for the given prescription list
     * request is returned
     */
    PrescriptionHistoryResponse listPrescriptionHistoryData(RequestDTO prescriptionListRequestDto);

    /**
     * <p>
     * The method is used to remove a prescription from a list.
     * </p>
     *
     * @param prescriptionListRequestDto {@link RequestDTO} The prescriptionListRequest contains information about a
     *                                   list of prescriptions that need to be removed is given
     */
    void removePrescription(RequestDTO prescriptionListRequestDto);

    /**
     * <p>
     * This method is used to retrieve a list of FillPrescriptionResponseDTO objects based on a given SearchRequest.
     * </p>
     *
     * @param searchRequestDto {@link SearchRequestDTO} The SearchRequest contains the search criteria for retrieving a
     *                         list of FillPrescriptionResponse objects is given
     * @return {@link List<FillPrescriptionResponseDTO>} The list of FillPrescriptionResponse for the given search
     * request is returned
     */
    List<FillPrescriptionResponseDTO> getFillPrescriptions(SearchRequestDTO searchRequestDto);

    /**
     * <p>
     * This method is used to updates the fill status of a prescription based on the information provided in the
     * FillPrescriptionRequestDTO object and returns a list of updated prescriptions.
     * </p>
     *
     * @param fillPrescriptionRequestDto {@link FillPrescriptionRequestDTO} The FillPrescriptionRequest contains the
     *                                   necessary information to fill a prescription is given
     * @return {@link List<Prescription>} The list of Prescription for the given fillPrescriptionRequest is returned
     */
    List<Prescription> updateFillPrescription(FillPrescriptionRequestDTO fillPrescriptionRequestDto);

    /**
     * <p>
     * This method is used to retrieves the refill prescription history based on the search request.
     * </p>
     *
     * @param searchRequestDto {@link SearchRequestDTO} The SearchRequest contains the search criteria for retrieving
     *                         refill prescription history is given
     * @return {@link List<PrescriptionHistory>} The list of PrescriptionHistory for the given search request is
     * retrieved and returned
     */
    List<PrescriptionHistory> getRefillPrescriptionHistory(SearchRequestDTO searchRequestDto);

    /**
     * <p>
     * This method is used to find prescriptions by patient track ID, patient visit ID, and whether they have
     * been deleted.
     * </p>
     *
     * @param patientTrackId {@link Long} The patientTrack ID is used to track the patient's health information over
     *                       time is given
     * @param patientVisitId {@link Long} The ID of the patient visit for which the prescription is being searched
     *                       is given
     * @param isDeleted      isDeleted is a boolean parameter that indicates whether the Prescription object has been
     *                       marked as deleted or not
     * @return {@link List<Prescription>} This method is returning a list of Prescription objects that match the given
     * patientTrackId, patientVisitId, and isDeleted flag
     */
    List<Prescription> findByPatientTrackIdAndPatientVisitIdAndIsDeleted(Long patientTrackId, Long patientVisitId,
                                                                         boolean isDeleted);

    /**
     * <p>
     * This method is used to  return the number of prescriptions for a given patient track ID that were filled before
     * a specified end date.
     * </p>
     *
     * @param endDate        {@link Date} The date until which the prescription count needs to be calculated is given
     * @param patientTrackId {@link Long} The patientTrackId is used to retrieve the prescription count is given
     * @return The function `getPrescriptionCount` is returning an integer value, which represents the number of
     * prescriptions for a given patient track ID that were issued before or on the specified end date.
     */
    int getPrescriptionCount(Date endDate, Long patientTrackId);

    /**
     * <p>
     * This method is used to retrieve a list of prescription history for a given patient visit ID.
     * </p>
     *
     * @param patientVisitId {@link Long} The patientVisitId is used to retrieve a list of PrescriptionHistory objects
     *                       associated with that particular patient visit is given
     * @return {@link  List<PrescriptionHistory>} The list of PrescriptionHistory objects is being returned
     */
    List<PrescriptionHistory> getPrescriptions(Long patientVisitId);

    /**
     * <p>
     * This method is used to remove a prescription identified by a tracker ID.
     * </p>
     *
     * @param trackerId The tracker ID is used to remove the corresponding prescription is given
     */
    void removePrescription(long trackerId);

    /**
     * <p>
     * This method is used to return the prescription details of a patient identified by their track ID and tenant ID.
     * </p>
     *
     * @param patientTrackId patientTrackId is used to retrieve the prescribed details of a specific patient is given
     * @param tenantId       The tenantId is used to ensure that the correct patient information is retrieved from the
     *                       correct database or data source is given
     * @return {@link PrescriberDTO} The method is returning prescriber which contains the prescribed details of a
     * patient identified by their patientTrackId and the tenantId
     */
    PrescriberDTO getPatientPrescribedDetails(long patientTrackId, long tenantId);
}
