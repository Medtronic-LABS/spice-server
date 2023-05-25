package com.mdtlabs.coreplatform.spiceservice.metadata.controller;

import com.mdtlabs.coreplatform.common.model.entity.spice.Complaints;
import com.mdtlabs.coreplatform.common.service.GenericService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComplaintsControllerTest {

    @InjectMocks
    private ComplaintsController complaintsController;

    @Mock
    private GenericService genericService;

    @Test
    void testLine() {
        //when
        when(genericService.findById(anyLong())).thenReturn(null);

        //then
        ResponseEntity<Complaints> response = complaintsController.findById(1L);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


}