package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.mdtlabs.coreplatform.common.ErrorConstants;
import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;

/**
 * <p>
 * Program is a Java class representing a program entity with properties such as name, country,
 * and sets of sites and deleted sites.
 * <p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_PROGRAM)
public class Program extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = ErrorConstants.PROGRAM_NAME_NOT_EMPTY)
    @Column(name = FieldConstants.NAME)
    private String name;

    @NotNull(message = ErrorConstants.COUNTRY_ID_NOT_NULL)
    @JoinColumn(name = FieldConstants.COUNTRY_ID)
    @ManyToOne
    private Country country;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = TableConstants.TABLE_SITE_PROGRAM, joinColumns = {
            @JoinColumn(name = FieldConstants.PROGRAM_ID)}, inverseJoinColumns = {
            @JoinColumn(name = FieldConstants.SITE_ID)})
    private Set<Site> sites;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = TableConstants.TABLE_DELETED_SITE_PROGRAM, joinColumns = {
            @JoinColumn(name = FieldConstants.PROGRAM_ID)}, inverseJoinColumns = {
            @JoinColumn(name = FieldConstants.SITE_ID)})
    private Set<Site> deletedSites;

    public Program() {

    }

    public Program(String name, Long tenantId, Country country) {
        this.name = name;
        this.tenantId = tenantId;
        this.country = country;
    }
}
