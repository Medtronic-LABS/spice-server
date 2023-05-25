package com.mdtlabs.coreplatform.spiceservice.metadata.service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complaints;
import com.mdtlabs.coreplatform.spiceservice.common.repository.ComplaintsRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.ComplaintsServiceImpl;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

/**
 * <p>
 * Complaint Service Test is used to test each method by providing
 * fake value as input and verify the output value, which is
 * expected.
 * </p>
 *
 * @author JohnKennedy created on Feb 21, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ComplaintsServiceTest {

    @InjectMocks
    private ComplaintsServiceImpl complaintsService;

    @Mock
    private ComplaintsRepository complaintsRepository;

    @Test
    void findByIsDeletedFalseAndIsActiveTrue() {
        //given
        Constants.COMPLAINTS = new ArrayList<>();
        List<Complaints> complaints = List.of(TestDataProvider.getComplaints());

        //when
        when(complaintsRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(complaints);

        //then
        List<Complaints> complaintsList = complaintsService.findByIsDeletedFalseAndIsActiveTrue();
        Assertions.assertNotNull(complaintsList);

        //then
        complaintsList = complaintsService.findByIsDeletedFalseAndIsActiveTrue();
        Assertions.assertFalse(Constants.COMPLAINTS.isEmpty());
    }

    @Test
    void getComplaintsByIds() {
        //given
        Set<Long> ids = Set.of(1L, 2L);
        Set<Complaints> complaints = Set.of(TestDataProvider.getComplaints());

        //when
        when(complaintsRepository.findByIdInAndIsDeletedFalseAndIsActiveTrue(ids)).thenReturn(complaints);

        //then
        Set<Complaints> complaintsSet = complaintsService.getComplaintsByIds(ids);
        Assertions.assertNotNull(complaintsSet);
    }

    @Test
    void getCompliants() {
        //given
        Constants.COMPLAINTS = new ArrayList<>();
        List<Complaints> complaints = List.of(TestDataProvider.getComplaints());

        //when
        when(complaintsRepository.findByIsDeletedFalseAndIsActiveTrue()).thenReturn(complaints);

        //then
        List<Complaints> complaintsList = complaintsService.getCompliantList();
        Assertions.assertNotNull(complaintsList);

        //then
        complaintsList = complaintsService.getCompliantList();
        Assertions.assertFalse(Constants.COMPLAINTS.isEmpty());
    }
}