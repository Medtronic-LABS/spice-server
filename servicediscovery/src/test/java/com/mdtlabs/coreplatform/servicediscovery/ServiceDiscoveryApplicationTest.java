package com.mdtlabs.coreplatform.servicediscovery;

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
 * This class has the test methods for the ServiceDiscoveryApplication class.
 * </p>
 *
 * @author Divya S created on March 03, 2023 created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
class ServiceDiscoveryApplicationTest {

    private static final String ARG = "";
    private static final String[] ARGS = new String[]{ARG};

    @InjectMocks
    ServiceDiscoveryApplication serviceDiscoveryApplication;

    @Autowired
    ConfigurableApplicationContext context;

    @Test
    void main() {
        try (MockedStatic<ServiceDiscoveryApplication> serviceDiscoveryApplicationMock = Mockito.mockStatic(ServiceDiscoveryApplication.class);
             MockedStatic<SpringApplication> springStatic = Mockito.mockStatic(
                     SpringApplication.class)) {
            serviceDiscoveryApplicationMock.when(() -> ServiceDiscoveryApplication.main(ARGS))
                    .thenCallRealMethod();
            springStatic.when(() -> SpringApplication.run(ServiceDiscoveryApplication.class, ARGS))
                    .thenReturn(context);

            // when
            ServiceDiscoveryApplication.main(ARGS);

            //then
            serviceDiscoveryApplicationMock.verify(
                    () -> ServiceDiscoveryApplication.main(ARGS),
                    times(1));
            springStatic.verify(
                    () -> SpringApplication.run(ServiceDiscoveryApplication.class, ARGS),
                    times(1));
        }
    }
}