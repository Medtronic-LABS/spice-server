package com.mdtlabs.coreplatform;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.web.filter.CorsFilter;

/**
 * <p>
 * This class has the test methods for the Common service main class.
 * </p>
 *
 * @author Divya S created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
class CommonServiceMainTest {

    private static final String ARG = "";
    private static final String[] ARGS = new String[]{ARG};

    @InjectMocks
    private CommonServiceMain commonServiceMain;

    @Autowired
    private ConfigurableApplicationContext context;

    @Test
    void main() {
        try (MockedStatic<CommonServiceMain> commonApplicationMockedStatic = Mockito
                .mockStatic(CommonServiceMain.class);
             MockedStatic<SpringApplication> springStatic = Mockito.mockStatic(
                     SpringApplication.class)) {
            commonApplicationMockedStatic.when(() -> CommonServiceMain.main(ARGS))
                    .thenCallRealMethod();
            springStatic.when(() -> SpringApplication.run(CommonServiceMain.class, ARGS))
                    .thenReturn(context);

            // when
            CommonServiceMain.main(ARGS);

            //then
            commonApplicationMockedStatic.verify(() -> CommonServiceMain.main(ARGS), times(1));
            springStatic.verify(() -> SpringApplication.run(CommonServiceMain.class, ARGS), times(1));
        }
    }

    @Test
    void corsFilter() {
        //then
        CorsFilter actualCorsFilter = commonServiceMain.corsFilter();
        assertNotNull(actualCorsFilter);
    }
}