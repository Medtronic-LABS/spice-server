package com.mdtlabs.coreplatform.spiceadminservice;

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

/**
 * <p>
 * This class has the test methods for AdminServiceApplication class.
 * </p>
 *
 * @author Divya S created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class AdminServiceApplicationTest {

    private static final String ARG = "";
    private static final String[] ARGS = new String[]{ARG};

    @InjectMocks
    AdminServiceApplication adminServiceApplication;

    @Autowired
    ConfigurableApplicationContext context;

    @Test
    void main() {
        try (MockedStatic<AdminServiceApplication> adminServiceApplicationMock = Mockito.mockStatic(AdminServiceApplication.class);
             MockedStatic<SpringApplication> springStatic = Mockito.mockStatic(
                     SpringApplication.class)) {
            adminServiceApplicationMock.when(() -> AdminServiceApplication.main(ARGS))
                    .thenCallRealMethod();
            springStatic.when(() -> SpringApplication.run(AdminServiceApplication.class, ARGS))
                    .thenReturn(context);

            // when
            AdminServiceApplication.main(ARGS);

            //then
            adminServiceApplicationMock.verify(
                    () -> AdminServiceApplication.main(ARGS),
                    times(1));
            springStatic.verify(
                    () -> SpringApplication.run(AdminServiceApplication.class, ARGS),
                    times(1));
        }
    }
}