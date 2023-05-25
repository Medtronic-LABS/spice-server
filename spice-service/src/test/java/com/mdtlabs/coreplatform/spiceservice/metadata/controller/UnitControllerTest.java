package com.mdtlabs.coreplatform.spiceservice.metadata.controller;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Unit;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.UnitService;
import static org.mockito.Mockito.when;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UnitControllerTest {
    @InjectMocks
    UnitController unitController;
    
    @Mock
    UnitService unitService;
    @Test
    void getUnitsByType() {
        Unit unit = new Unit();
        unit.setType(Constants.PRESCRIPTION_ID);
        when(unitService.getUnitsByType(Constants.PRESCRIPTION_ID)).thenReturn(List.of(unit));
        
        List<Unit> units = unitController.getUnitsByType(Constants.PRESCRIPTION_ID);
        Assertions.assertNotNull(units);
        Assertions.assertEquals(Constants.PRESCRIPTION_ID, units.get(Constants.ZERO).getType());
    }
    
}