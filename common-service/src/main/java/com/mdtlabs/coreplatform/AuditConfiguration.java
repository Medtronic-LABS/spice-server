package com.mdtlabs.coreplatform;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.contexts.AuditContextHolder;
import com.mdtlabs.coreplatform.common.logger.Logger;
import com.mdtlabs.coreplatform.common.model.entity.Audit;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

@Configuration
public class AuditConfiguration {

    /**
     * <p>
     * This function creates a new instance of the AuthenticationFilter class.
     * </p>
     *
     * @return A new instance of the AuthenticationFilter class is being returned.
     */
    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }

    /**
     * <p>
     * This method is used to create a LocalContainerEntityManagerFactoryBean with specified properties and
     * packages.
     * </p>
     *
     * @param factory             {@link EntityManagerFactoryBuilder} is a builder for creating EntityManagerFactory
     *                            instances is given
     * @param dataSource          {@link DataSource} The dataSource parameter is an object that provides a connection to the
     *                            database is given
     * @param jpaProperties       {@link JpaProperties}  is a class that contains properties related to the Java
     *                            Persistence API (JPA), which is a specification for managing relational data in Java
     *                            applications is given
     * @param hibernateProperties {@link HibernateProperties} parameter is an object of type
     *                            HibernateProperties, which contains properties specific to Hibernate ORM (Object-Relational
     *                            Mapping) framework is given
     * @return {@link LocalContainerEntityManagerFactoryBean} object is being returned is givem
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factory,
                                                                       DataSource dataSource, JpaProperties jpaProperties, HibernateProperties hibernateProperties) {
        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(),
                new HibernateSettings());
        properties.put(Constants.HIBERNATE_EJB_INTERCEPTOR, hibernateInterceptor());
        properties.put(Constants.HIBERNATE_SESSION_FACTORY, hibernateInterceptor());
        return factory.dataSource(dataSource).properties(properties).packages(Constants.PACKAGE_CORE_PLATFORM).build();
    }

    /**
     * <p>
     * This is a Hibernate interceptor that creates audit records for entities being saved or updated
     * in a Hibernate session.
     * </p>
     *
     * @return {@link EmptyInterceptor} A Spring Bean of type `EmptyInterceptor` is being returned.
     */
    @Bean
    public EmptyInterceptor hibernateInterceptor() {

        return new EmptyInterceptor() {

            private final Map<String, List<Audit>> insertAudit = new HashMap<>();
            private final List<Audit> updateAudit = new ArrayList<>();

            /**
             * <p>
             * This is an implementation of the onLoad method in Java that always returns false.
             * </p>
             *
             * @param entity {@link Object} The entity object being loaded from the database.
             * @param id {@link Serializable} The id parameter is a Serializable object that represents the unique
             * identifier of the entity being loaded
             * @param state {@link Object[]} The state parameter in the onLoad method refers to the current state of the
             * entity being loaded from the database
             * @param propertyNames {@link String[]} An array of strings representing the names of the properties of the
             * entity being loaded
             * @param types {@link Type[]} The `types` parameter is an array of `Type` objects that represent the
             * Hibernate types of the properties in the entity
             * @return The method is returning a boolean value of `false`.
             */
            @Override
            public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames,
                                  Type[] types) {
                return false;
            }

            /**
             * <p>
             * This is an implementation of the onSave method in Java that creates an audit object for
             * an entity and returns true if successful.
             * </p>
             *
             * @param entity  {@link Object} The object being saved to the database.
             * @param id {@link Serializable} The id parameter is a Serializable object representing the unique 
             * identifier of the entity being saved.
             * @param state {@link Object[]} The `state` parameter in this method refers to an array of the current
             * property values of the entity being saved
             * @param propertyNames {@link String[]} An array of strings representing the names of the properties of the
             * entity being saved
             * @param types {@link Type[]} The `types` parameter is an array of `Type` objects that represent the
             * Hibernate types of the properties being saved
             * @return A boolean value is being returned.
             */
            @Override
            public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)
                    throws CallbackException {
                Audit audit = new Audit();
                if (entity.getClass().getSimpleName().equals(Constants.AUDIT)) {
                    return false;
                } else {
                    try {
                        createAudit(entity, audit);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        Logger.logError(e);
                    }
                }
                return true;
            }

            /**
             * <p>
             * This method is used to set the audit values.
             * </p>
             *
             * @param entity {@link Object}entity to be audited
             * @param audit  {@link Audit} audit entity
             */
            private void createAudit(Object entity, Audit audit)
                    throws IllegalAccessException, InvocationTargetException {
                if (!entity.getClass().getSimpleName().equals(Constants.AUDIT)) {
                    Class userClass = entity.getClass();
                    Method[] userClassMethods = userClass.getMethods();
                    List<Audit> auditListToBeSaved = new ArrayList<>();
                    for (Method userClassMethod : userClassMethods) {
                        if (userClassMethod.getName().startsWith(Constants.GET)
                                && ((userClassMethod.getParameterTypes()).length == Constants.ZERO)
                                && (!(userClassMethod.getName().equals(Constants.GET_CLASS)))) {
                            Object userClassValue;
                            userClassValue = userClassMethod.invoke(entity, null);
                            if (userClassValue != null) {
                                audit.setAction(Constants.CREATE.toUpperCase());
                                audit.setColumnName(userClassMethod.getName().substring(Constants.NUMBER_THREE));
                                audit.setEntity(entity.getClass().getSimpleName());
                                audit.setNewValue(userClassValue.toString());
                                auditListToBeSaved.add(audit);
                            }
                        }
                    }
                    if (null == insertAudit.get(entity.getClass().getSimpleName())) {
                        insertAudit.put(entity.getClass().getSimpleName(), auditListToBeSaved);
                    } else {
                        insertAudit.get(entity.getClass().getSimpleName()).addAll(auditListToBeSaved);
                    }
                }

            }

            /**
             * <p>
             * This function creates a list of audit records for updated entities and adds them to an
             * existing list.
             * </p>
             *
             * @param entity {@link Object} The entity object being updated in the Hibernate session.
             * @param id {@link Serializable} The unique identifier of the entity being audited. It is of type Serializable.
             * @param currentState {@link Object[]} An array of the current state of the entity's properties after the
             * changes have been made.
             * @param previousState {@link Object[]}  The previous state of the entity's properties before they were
             * updated.
             * @param propertyNames {@link String[] } An array of strings representing the names of the properties of the
             * entity being audited.
             * @param types {@link Type[]} An array of Type objects representing the Hibernate types of the properties
             * being checked for changes.
             * @return The method is returning a boolean value of true.
             */
            @Override
            public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
                                        String[] propertyNames, Type[] types) {
                List<Audit> updateAuditListToBeSaved = new ArrayList<>();
                for (int iterator = Constants.ZERO; iterator < currentState.length; iterator++) {
                    if (!Objects.deepEquals(currentState[iterator], previousState[iterator])
                            && Objects.nonNull(currentState[iterator])
                            && (Objects.nonNull(currentState[iterator]) && Objects.nonNull(previousState[iterator]))) {
                        Audit audit = new Audit();
                        audit.setAction(Constants.UPDATE.toUpperCase());
                        audit.setColumnName(propertyNames[iterator]);
                        audit.setEntity(entity.getClass().getSimpleName());
                        audit.setOldValue(previousState[iterator].toString());
                        audit.setNewValue(currentState[iterator].toString());
                        audit.setEntityId(Long.parseLong(id.toString()));
                        updateAuditListToBeSaved.add(audit);
                    }
                }
                updateAudit.addAll(updateAuditListToBeSaved);
                return true;
            }

            /**
             * <p>
             * This function sets the entity ID for audit records before flushing them to the database.
             * </p>
             *
             * @param iterator {@link Iterator} The parameter "iterator" is an Iterator object that is used to iterate
             * over a collection of objects
             */
            @Override
            public void preFlush(Iterator iterator) {
                if (!insertAudit.isEmpty()) {
                    while (iterator.hasNext()) {
                        long entityId;
                        Object obj = iterator.next();
                        if (obj instanceof BaseEntity baseEntity
                                && (null != insertAudit.get(obj.getClass().getSimpleName()))) {
                            entityId = baseEntity.getId();
                            for (Audit audit : insertAudit.get(obj.getClass().getSimpleName())) {
                                audit.setEntityId(entityId);
                            }
                        }
                    }
                }
            }

            /**
             * <p>
             * This function saves audit records to a repository after a flush operation, clearing the
             * update and insert audit lists afterwards.
             * </p>
             *
             * @param iterator {@link Iterator}  The `iterator` parameter is an iterator over the entities that have been
             * flushed to the database
             */
            @Override
            public void postFlush(Iterator iterator) {
                try {
                    if (!insertAudit.isEmpty()) {
                        for (Entry<String, List<Audit>> key : insertAudit.entrySet()) {
                            authenticationFilter().commonRepository.saveAll(key.getValue());
                        }
                    }
                    if (!updateAudit.isEmpty()) {
                        authenticationFilter().commonRepository.saveAll(updateAudit);
                        AuditContextHolder.clear();
                    }
                } finally {
                    updateAudit.clear();
                    insertAudit.clear();
                }
            }
        };
    }
}
