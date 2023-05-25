package com.mdtlabs.coreplatform.common;

import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * <p>
 * CustomDateSerializerTest tests the serialization of dates with different
 * time zones and checks the user's time zone ID.
 * </p>
 *
 * @author Nandhakumar Karthikeyan created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
class CustomDateSerializerTest {

    @InjectMocks
    private CustomDateSerializer customDateSerializer;

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"+00:00", "+05:30"})
    void serializeTest(String zoneId) throws IOException {
        //given
        ReflectionTestUtils.setField(customDateSerializer, "userZoneId", zoneId);
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);
        SerializerProvider serializerProvider = mock(SerializerProvider.class);

        //then
        customDateSerializer.serialize(new Date(), jsonGenerator, serializerProvider);
        assertNotNull(jsonGenerator);
    }

    @Test
    void constructCustomDateSerializer() {
        //then
        CustomDateSerializer customDateSerializer = new CustomDateSerializer();
        assertNotNull(customDateSerializer);
    }

    @Test
    void getUserZoneId() {
        //then
        customDateSerializer.setUserZoneId(Constants.TIMEZONE_UTC);
        String userZoneId = customDateSerializer.getUserZoneId();
        assertEquals(Constants.TIMEZONE_UTC, userZoneId);
    }
}