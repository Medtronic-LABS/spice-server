package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.IOException;
import java.io.Serial;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.TenantBaseEntity;

/**
 * <p>
 * CustomizedModule is a Java class representing a customized module with JSON data stored in a database table.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Table(name = TableConstants.TABLE_CUSTOMIZED_MODULES)
@Entity
@Data
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
public class CustomizedModule extends TenantBaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = FieldConstants.MODULE_VALUE, columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private Map<String, Object> moduleValue;

    @NotBlank
    @Column(name = FieldConstants.SCREEN_TYPE)
    private String screenType;

    @NotNull
    @Column(name = FieldConstants.PATIENT_TRACK_ID)
    private Long patientTrackId;

    @Column(name = FieldConstants.CLINICAL_WORKFLOW_ID)
    private Long clinicalworkflowId;

    /**
     * <p>
     * This method is used to write the default object to an ObjectOutputStream.
     * </p>
     *
     * @param stream {@link java.io.ObjectOutputStream} The parameter, which is used to write objects to an
     *               output stream in a serialized form is given
     */
    @Serial
    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
        stream.defaultWriteObject();
    }

    /**
     * <p>
     * This method is used to read an object from an input stream using default serialization.
     * </p>
     *
     * @param stream {@link java.io.ObjectInputStream} The parameter, which is used to read
     *               objects from an input stream is given
     */
    @Serial
    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
    }
}
