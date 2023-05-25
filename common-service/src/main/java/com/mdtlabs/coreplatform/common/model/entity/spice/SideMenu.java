package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.Serial;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;

/**
 * <p>
 * SideMenu is a Java class representing a side menu with a role name, a map of menus in JSON format, and a transient culture
 * values object.
 * </p>
 *
 * @author Niraimathi S created on Jun 20, 2023
 */
@Data
@Table(name = TableConstants.TABLE_SIDE_MENU)
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class SideMenu extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.ROLE_NAME)
    private String roleName;

    @Column(name = FieldConstants.MENU, columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private Map<String, String> menus;

    @Transient
    private transient Object cultureValues;
}
