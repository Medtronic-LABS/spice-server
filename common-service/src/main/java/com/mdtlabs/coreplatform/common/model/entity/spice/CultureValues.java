package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.IOException;
import java.io.Serial;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;

/**
 * <p>
 * CultureValues is a Java class representing a table called
 * "CultureValues" with various columns including a JSON column.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_CULTURE_VALUES)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class CultureValues extends BaseEntity {

    @Column(name = FieldConstants.FORM_DATA_ID)
    private long formDataId;

    @Column(name = FieldConstants.FORM_NAME)
    private String formName;

    @Column(name = FieldConstants.CULTURE_ID)
    private long cultureId;

    @Column(name = FieldConstants.CULTURE_VALUE)
    private String cultureValue;

    @Column(name = FieldConstants.JSON_CULTURE_VALUE, columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private Object jsonCultureValue;

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
