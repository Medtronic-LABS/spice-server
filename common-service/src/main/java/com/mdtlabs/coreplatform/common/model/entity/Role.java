package com.mdtlabs.coreplatform.common.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;

/**
 * <p>
 * Role is a Java class representing a Role entity with properties such as name and level, implementing the Serializable
 * and GrantedAuthority interfaces.
 * </p>
 *
 * @author VigneshKumar created on Jun 30, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_ROLE)
public class Role extends BaseEntity implements Serializable, GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 2254198222527717773L;

    @Column(name = FieldConstants.NAME)
    private String name;

    @Column(name = FieldConstants.LEVEL)
    private String level;

    @Transient
    private String authority;


    public Role(Long id, String name) {
        super(id);
        this.name = name;
    }

    public Role() {
    }

    /**
     * <p>
     * The function returns the name of the authority.
     * </p>
     *
     * @return The name of the authority is returned
     */
    public String getAuthority() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof Role))
            return false;
        Role role = (Role) object;
        return !Objects.isNull(this.getId()) && this.getId() == role.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(authority, level, name);
        return result;
    }
}
