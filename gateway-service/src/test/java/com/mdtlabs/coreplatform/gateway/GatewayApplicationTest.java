package com.mdtlabs.coreplatform.gateway;

import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * <p>
 * This class has the test methods for the GatewayApplication class.
 * </p>
 *
 * @author Divya S created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
class GatewayApplicationTest {

    private static final String ARG = "";
    private static final String[] ARGS = new String[]{ARG};

    @InjectMocks
    GatewayApplication gatewayApplication;

    @Autowired
    ConfigurableApplicationContext context;

    @Test
    void main() {
        try (MockedStatic<GatewayApplication> gatewayApplicationMockedStatic = Mockito.mockStatic(GatewayApplication.class);
             MockedStatic<SpringApplication> springStatic = Mockito.mockStatic(
                     SpringApplication.class)) {
            gatewayApplicationMockedStatic.when(() -> GatewayApplication.main(ARGS))
                    .thenCallRealMethod();
            springStatic.when(() -> SpringApplication.run(GatewayApplication.class, ARGS))
                    .thenReturn(context);

            // when
            GatewayApplication.main(ARGS);

            //then
            gatewayApplicationMockedStatic.verify(
                    () -> GatewayApplication.main(ARGS),
                    times(1));
            springStatic.verify(
                    () -> SpringApplication.run(GatewayApplication.class, ARGS),
                    times(1));
        }
    }
}