package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.mdtlabs.coreplatform.common.model.entity.spice.PrescriptionHistory;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * The PrescriptionHistoryResponse class contains a list of patient prescriptions and a list of
 * prescription history dates.
 * </p>
 */
@Data
public class PrescriptionHistoryResponse {

    private List<PrescriptionHistory> patientPrescription;
    private List<Map<String, Object>> prescriptionHistoryDates;

}
