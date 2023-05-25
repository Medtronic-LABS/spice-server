package com.mdtlabs.coreplatform.spiceservice.metadata.service.impl;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complaints;
import com.mdtlabs.coreplatform.common.service.impl.GenericServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.common.repository.ComplaintsRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.ComplaintsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * This service class contain all the business logic for complaints module and
 * perform all the user operation here.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Service
public class ComplaintsServiceImpl extends GenericServiceImpl<Complaints> implements ComplaintsService {

    @Autowired
    private ComplaintsRepository complaintsRepository;

    /**
     * {@inheritDoc}
     */
    public List<Complaints> findByIsDeletedFalseAndIsActiveTrue() {
        if (Constants.COMPLAINTS.isEmpty()) {
            Constants.COMPLAINTS.addAll(complaintsRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.COMPLAINTS;
    }

    /**
     * {@inheritDoc}
     */
    public Set<Complaints> getComplaintsByIds(Set<Long> ids) {
        return complaintsRepository.findByIdInAndIsDeletedFalseAndIsActiveTrue(ids);
    }

    /**
     * {@inheritDoc}
     */
    public List<Complaints> getCompliantList() {
        if (Constants.COMPLAINTS.isEmpty()) {
            Constants.COMPLAINTS.addAll(complaintsRepository.findByIsDeletedFalseAndIsActiveTrue());
        }
        return Constants.COMPLAINTS;

    }
}
