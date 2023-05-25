package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.spice.AccountWorkflow;

/**
 * <p>
 * Account is a java class representing an Account entity with various fields and relationships to other entities.
 * </p>
 *
 * @author Jeyaharini T A created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_ACCOUNT)
public class Account extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = ErrorConstants.ACCOUNT_NAME_NOT_NULL)
    @Column(name = FieldConstants.NAME, unique = true)
    private String name;

    @Column(name = FieldConstants.MAX_NO_OF_USERS)
    private int maxNoOfUsers;

    @Column(name = FieldConstants.IS_USERS_RESTRICTED)
    private boolean isUsersRestricted;

    @NotNull(message = ErrorConstants.COUNTRY_ID_NOT_NULL)
    @ManyToOne
    @JoinColumn(name = FieldConstants.COUNTRY_ID)
    private Country country;

    @NotNull(message = ErrorConstants.CLINICAL_WORKFLOW_NOT_NULL)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = TableConstants.TABLE_ACCOUNT_CLINICAL_WORKFLOW, joinColumns = {
            @JoinColumn(name = FieldConstants.ACCOUNT_ID)}, inverseJoinColumns = {
            @JoinColumn(name = FieldConstants.CLINICAL_WORKFLOW_ID)})
    private List<AccountWorkflow> clinicalWorkflows;

    // This code is defining a one-to-many relationship between the `Account` entity and the
    // `AccountWorkflow` entity. Specifically, it is mapping the `customizedWorkflows` field of the
    // `Account` entity to a join table in the database
    // (`TableConstants.TABLE_ACCOUNT_CUSTOMIZED_WORKFLOW`) that contains foreign keys to both the
    // `Account` and `AccountWorkflow` tables. The `joinColumns` and `inverseJoinColumns` attributes of
    // the `@JoinTable` annotation specify the names of the columns in the join table that correspond
    // to the primary keys of the `Account` and `AccountWorkflow` tables, respectively. The `fetch`
    // attribute of the `@OneToMany` annotation specifies that the `customizedWorkflows` collection
    // should be lazily loaded from the database when accessed, and the `cascade` attribute specifies
    // that any changes made to an `Account` entity should be cascaded to its associated
    // `AccountWorkflow` entities.
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = TableConstants.TABLE_ACCOUNT_CUSTOMIZED_WORKFLOW, joinColumns = {
            @JoinColumn(name = FieldConstants.ACCOUNT_ID)}, inverseJoinColumns = {
            @JoinColumn(name = FieldConstants.CUSTOMIZED_WORKFLOW_ID)})
    private List<AccountWorkflow> customizedWorkflows;

    // `@Column(name = FieldConstants.REASON)` is a JPA annotation that maps the `reason` field of the
    // `Account` entity to a column in the database table with the name specified in the
    // `FieldConstants.REASON` constant. This annotation is used to specify the details of the column
    // such as its name, data type, length, etc. In this case, it is mapping the `reason` field to a
    // column in the `TABLE_ACCOUNT` table.
    @Column(name = FieldConstants.REASON)
    private String reason;

    // `@Column(name = FieldConstants.STATUS)` is a JPA annotation that maps the `status` field of the
    // `Account` entity to a column in the database table with the name specified in the
    // `FieldConstants.STATUS` constant. This annotation is used to specify the details of the column
    // such as its name, data type, length, etc. In this case, it is mapping the `status` field to a
    // column in the `TABLE_ACCOUNT` table.
    @Column(name = FieldConstants.STATUS)
    private String status;
}
