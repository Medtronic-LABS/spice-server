package com.mdtlabs.coreplatform.authserver;

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
 * This class has the test methods for the AuthServerApplication class.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class AuthServerApplicationTest {

    private static final String ARG = "";
    private static final String[] ARGS = new String[]{ARG};

    @InjectMocks
    AuthServerApplication authServerApplication;

    @Autowired
    ConfigurableApplicationContext context;

    @Test
    void authServerMain() {
        try (MockedStatic<AuthServerApplication> authServerApplicationMock = Mockito.mockStatic(AuthServerApplication.class);
             MockedStatic<SpringApplication> springStatic = Mockito.mockStatic(
                     SpringApplication.class)) {
            authServerApplicationMock.when(() -> AuthServerApplication.main(ARGS))
                    .thenCallRealMethod();
            springStatic.when(() -> SpringApplication.run(AuthServerApplication.class, ARGS))
                    .thenReturn(context);

            // when
            AuthServerApplication.main(ARGS);

            //then
            authServerApplicationMock.verify(
                    () -> AuthServerApplication.main(ARGS),
                    times(1));
            springStatic.verify(
                    () -> SpringApplication.run(AuthServerApplication.class, ARGS),
                    times(1));
        }
    }
}