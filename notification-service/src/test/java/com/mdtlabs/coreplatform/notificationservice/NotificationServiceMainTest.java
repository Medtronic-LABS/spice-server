package com.mdtlabs.coreplatform.notificationservice;

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
 * This class has the test methods for the Notification service main application class.
 * </p>
 *
 * @author Divya S created on Jan 31, 2023
 */
@ExtendWith(MockitoExtension.class)
class NotificationServiceMainTest {

    private static final String ARG = "";
    private static final String[] ARGS = new String[]{ARG};

    @InjectMocks
    NotificationServiceMain notificationServiceMain;

    @Autowired
    ConfigurableApplicationContext context;

    @Test
    void main() {
        try (MockedStatic<NotificationServiceMain> notificationServiceMainMockedStatic = Mockito
                .mockStatic(NotificationServiceMain.class);
             MockedStatic<SpringApplication> springStatic = Mockito.mockStatic(
                     SpringApplication.class)) {
            notificationServiceMainMockedStatic.when(() -> NotificationServiceMain.main(ARGS))
                    .thenCallRealMethod();
            springStatic.when(() -> SpringApplication.run(NotificationServiceMain.class, ARGS))
                    .thenReturn(context);

            // when
            NotificationServiceMain.main(ARGS);

            //then
            notificationServiceMainMockedStatic.verify(
                    () -> NotificationServiceMain.main(ARGS),
                    times(1));
            springStatic.verify(
                    () -> SpringApplication.run(NotificationServiceMain.class, ARGS),
                    times(1));
        }
    }
}