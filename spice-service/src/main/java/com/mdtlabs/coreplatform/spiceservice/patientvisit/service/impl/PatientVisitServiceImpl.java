package com.mdtlabs.coreplatform.spiceservice.patientvisit.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.CommonRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.PatientVisit;
import com.mdtlabs.coreplatform.common.util.DateUtil;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.repository.PatientVisitRepository;
import com.mdtlabs.coreplatform.spiceservice.patientvisit.service.PatientVisitService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * PatientVisitServiceImpl class implements methods for managing patient visit, including adding, updating, removing
 * and retrieving PatientVisit.
 * </p>
 *
 * @author Karthick Murugesan created on Feb 09, 2023
 */
@Service
public class PatientVisitServiceImpl implements PatientVisitService {

    @Autowired
    private PatientVisitRepository patientVisitRepository;

    /**
     * {@inheritDoc}
     */
    public Map<String, Long> createPatientVisit(CommonRequestDTO patientVisitDto) {
        String startDate = DateUtil.getStartOfDay();
        String endDate = DateUtil.getEndOfDay();

        PatientVisit existingPatientVisit = patientVisitRepository
                .getPatientVisitByTrackId(patientVisitDto.getPatientTrackId(), startDate, endDate);
        PatientVisit patientVisit = existingPatientVisit;
        if (Objects.isNull(existingPatientVisit)) {
            patientVisit = new PatientVisit();
            patientVisit.setTenantId(patientVisitDto.getTenantId());
            patientVisit.setPatientTrackId(patientVisitDto.getPatientTrackId());
            patientVisit.setVisitDate(new Date());
            patientVisit = patientVisitRepository.save(patientVisit);
        }
        return Map.of(Constants.ID, patientVisit.getId());
    }

    /**
     * {@inheritDoc}
     */
    public PatientVisit updatePatientVisit(PatientVisit patientVisit) {
        return patientVisitRepository.save(patientVisit);
    }

    /**
     * {@inheritDoc}
     */
    public List<PatientVisit> getPatientVisitDates(Long patientTrackId, Boolean isInvestigation,
                                                   Boolean isMedicalReview, Boolean isPrescription) {
        List<PatientVisit> visitDates = patientVisitRepository
                .getPatientVisitDates(patientTrackId, isInvestigation, isMedicalReview, isPrescription);
        return Objects.isNull(visitDates) ? new ArrayList<>() : visitDates;
    }

    /**
     * {@inheritDoc}
     */
    public PatientVisit getPatientVisitById(Long id) {
        PatientVisit patientVisit = patientVisitRepository.findByIdAndIsDeleted(id, Constants.BOOLEAN_FALSE);
        if (Objects.isNull(patientVisit)) {
            throw new DataNotFoundException(1509);
        }
        return patientVisit;

    }

    /**
     * {@inheritDoc}
     */
    public List<PatientVisit> getPatientVisitDates(Long patientTrackId) {
        List<PatientVisit> visitDates = patientVisitRepository.getPatientVisitDates(patientTrackId);
        return Objects.isNull(visitDates) ? new ArrayList<>() : visitDates;
    }

    /**
     * {@inheritDoc}
     */
    public void removePatientVisit(long trackerId) {
        List<PatientVisit> patientVisits = patientVisitRepository.findByPatientTrackId(trackerId);
        if (!Objects.isNull(patientVisits)) {
            patientVisits.forEach(patientVisit -> {
                patientVisit.setActive(false);
                patientVisit.setDeleted(true);
            });
            patientVisitRepository.saveAll(patientVisits);
        }
    }
}
