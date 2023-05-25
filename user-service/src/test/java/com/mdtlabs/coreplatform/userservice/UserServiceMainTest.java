package com.mdtlabs.coreplatform.userservice;

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
 *   This class has the test methods for the User service main class.
 * </p>
 *
 * @author Divya S created on March 03, 2023
 */
@ExtendWith(MockitoExtension.class)
class UserServiceMainTest {
    private static final String ARG = "";
    private static final String[] ARGS = new String[]{ARG};

    @InjectMocks
    UserServiceMain userServiceMain;

    @Autowired
    ConfigurableApplicationContext context;

    @Test
    void testMain() {
        try (MockedStatic<UserServiceMain> userServiceMainMockedStatic = Mockito.mockStatic(UserServiceMain.class);
             MockedStatic<SpringApplication> springStatic = Mockito.mockStatic(
                     SpringApplication.class)) {
            userServiceMainMockedStatic.when(() -> UserServiceMain.main(ARGS))
                    .thenCallRealMethod();
            springStatic.when(() -> SpringApplication.run(UserServiceMain.class, ARGS))
                    .thenReturn(context);

            // when
            UserServiceMain.main(ARGS);

            //then
            userServiceMainMockedStatic.verify(
                    () -> UserServiceMain.main(ARGS),
                    times(1));
            springStatic.verify(
                    () -> SpringApplication.run(UserServiceMain.class, ARGS),
                    times(1));
        }
    }
}