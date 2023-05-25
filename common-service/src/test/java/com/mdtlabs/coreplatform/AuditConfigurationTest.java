package com.mdtlabs.coreplatform;

import java.util.Map;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.model.entity.Account;
import com.mdtlabs.coreplatform.common.model.entity.Audit;

/**
 * <p>
 * AuditConfigurationTest class used to test all possible positive
 * and negative cases for all methods and conditions used in AuditConfiguration
 * class.
 * </p>
 *
 * @author Nandhakumar Karthikeyan created on Feb 9, 2023
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuditConfigurationTest {

    @InjectMocks
    private AuditConfiguration auditConfiguration;

    @Test
    void authenticationFilterTest() {
        AuthenticationFilter response = auditConfiguration.authenticationFilter();
        assertNotNull(response);
    }

    @Test
    void hibernateInterceptorTest() {
        Object[] object = new Object[1];
        object[0] = Constants.TOKEN;
        Object[] secondObject = new Object[1];
        secondObject[0] = Constants.ACCOUNT;
        String[] str = {Constants.TOKEN};
        boolean response = auditConfiguration.hibernateInterceptor().onFlushDirty(auditConfiguration, 1L, object,
                secondObject, str, null);
        Assertions.assertTrue(response);
    }

    @Test
    void hibernateInterceptorTestOnLoad() {
        Object[] object = new Object[1];
        object[0] = Constants.TOKEN;
        Object[] secondObject = new Object[1];
        secondObject[0] = Constants.ACCOUNT;
        String[] str = {Constants.TOKEN};
        boolean response = auditConfiguration.hibernateInterceptor().onLoad(object, secondObject, str, str, null);
        Assertions.assertFalse(response);
    }

    @Test
    void hibernateInterceptorTestOnSave() {
        Account audit = new Account();
        boolean response = auditConfiguration.hibernateInterceptor().onSave(audit, null, null, null, null);
        Assertions.assertTrue(response);
    }

    @Test
    void hibernateInterceptorTestOnSaveFalse() {
        Audit audit = new Audit();
        boolean response = auditConfiguration.hibernateInterceptor().onSave(audit, getClass(), null, null, null);
        Assertions.assertFalse(response);
    }

    @Test
    void entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean localContainerEntityManager = new LocalContainerEntityManagerFactoryBean();
        EntityManagerFactoryBuilder factory = mock(EntityManagerFactoryBuilder.class);
        DataSource dataSource = mock(DataSource.class);
        JpaProperties jpaProperties = mock(JpaProperties.class);
        HibernateProperties hibernateProperties = mock(HibernateProperties.class);
        EntityManagerFactoryBuilder.Builder builder = mock(EntityManagerFactoryBuilder.Builder.class);

        //when
        when(jpaProperties.getProperties()).thenReturn(Map.of(Constants.LANDMARK, Constants.AUDIT));
        when(hibernateProperties.determineHibernateProperties(Map.of(Constants.LANDMARK, Constants.AUDIT),
                new HibernateSettings())).thenReturn(Map.of(Constants.LANDMARK, Constants.AUDIT));
        when(factory.dataSource(dataSource)).thenReturn(builder);
        when(builder.properties(any())).thenReturn(builder);
        when(builder.packages(Constants.PACKAGE_CORE_PLATFORM)).thenReturn(builder);
        when(builder.build()).thenReturn(localContainerEntityManager);

        //then
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean
                = auditConfiguration.entityManagerFactory(factory, dataSource,
                jpaProperties, hibernateProperties);
        assertNotNull(localContainerEntityManagerFactoryBean);
        assertEquals(localContainerEntityManager, localContainerEntityManagerFactoryBean);
    }
}
