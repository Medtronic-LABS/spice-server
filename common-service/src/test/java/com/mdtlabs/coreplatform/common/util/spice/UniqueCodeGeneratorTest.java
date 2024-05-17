package com.mdtlabs.coreplatform.common.util.spice;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.mdtlabs.coreplatform.common.util.UniqueCodeGenerator;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UniqueCodeGeneratorTest {

    @InjectMocks
    private UniqueCodeGenerator uniqueCodeGenerator;
    @Test
    public void testGenerateUniqueCode() {
        String input = "test";
        String uniqueCode = uniqueCodeGenerator.generateUniqueCode(input);
        assertNotNull(uniqueCode);

        // Test that the same code is returned for the same input
        String sameUniqueCode = UniqueCodeGenerator.generateUniqueCode(input);
        assertEquals(uniqueCode, sameUniqueCode);

        // Test that a different code is returned for a different input
        String differentUniqueCode = uniqueCodeGenerator.generateUniqueCode(input + "1");
        assertNotNull(differentUniqueCode);
        Assertions.assertNotEquals(uniqueCode, differentUniqueCode);
    }
}
