package com.mdtlabs.coreplatform.spiceservice.metadata.service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.spice.Unit;
import com.mdtlabs.coreplatform.spiceservice.metadata.repository.UnitRepository;
import com.mdtlabs.coreplatform.spiceservice.metadata.service.impl.UnitServiceImpl;
import static org.mockito.Mockito.when;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UnitServiceTest {
    
    @InjectMocks
    UnitServiceImpl unitService;
    
    @Mock
    UnitRepository unitRepository;
    @Test
    void getUnitsByType() {
        Unit unit = new Unit();
        unit.setType(Constants.PRESCRIPTION_ID);
        when(unitRepository.findByType(Constants.PRESCRIPTION_ID.toUpperCase())).thenReturn(List.of(unit));
        
        List<Unit> units = unitService.getUnitsByType(Constants.PRESCRIPTION_ID);
        Assertions.assertNotNull(units);
        Assertions.assertEquals(Constants.PRESCRIPTION_ID, units.get(Constants.ZERO).getType());
    }
}