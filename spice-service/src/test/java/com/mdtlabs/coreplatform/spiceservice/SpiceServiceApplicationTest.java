package com.mdtlabs.coreplatform.spiceservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SpiceServiceApplicationTest {

    private static final String ARG = "";

    private static final String[] ARGS = new String[]{ARG};

    @InjectMocks
    SpiceServiceApplication spiceServiceApplication;

    @Autowired
    ConfigurableApplicationContext context;

    @Test
    void main() {
        try (MockedStatic<SpiceServiceApplication> spiceServiceApplicationMock = Mockito.mockStatic(SpiceServiceApplication.class);
             MockedStatic<SpringApplication> springStatic = Mockito.mockStatic(
                     SpringApplication.class)) {
            spiceServiceApplicationMock.when(() -> SpiceServiceApplication.main(ARGS))
                    .thenCallRealMethod();
            springStatic.when(() -> SpringApplication.run(SpiceServiceApplication.class, ARGS))
                    .thenReturn(context);

            // when
            SpiceServiceApplication.main(ARGS);

            //then
            spiceServiceApplicationMock.verify(
                    () -> SpiceServiceApplication.main(ARGS),
                    times(1));
            springStatic.verify(
                    () -> SpringApplication.run(SpiceServiceApplication.class, ARGS),
                    times(1));
        }
    }
}