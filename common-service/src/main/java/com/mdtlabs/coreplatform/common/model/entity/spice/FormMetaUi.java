package com.mdtlabs.coreplatform.common.model.entity.spice;

import java.io.IOException;
import java.io.Serial;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import org.hibernate.annotations.Type;

import com.mdtlabs.coreplatform.common.FieldConstants;
import com.mdtlabs.coreplatform.common.TableConstants;
import com.mdtlabs.coreplatform.common.model.entity.BaseEntity;

/**
 * <p>
 * FormMetaUi is a Java class representing a form's metadata and UI components, with serialization methods implemented.
 * </p>
 *
 * @author Karthick M created on Jun 20, 2022
 */
@Data
@Entity
@Table(name = TableConstants.TABLE_FORM_META_UI)
public class FormMetaUi extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = FieldConstants.FORM_NAME)
    private String formName;

    @Column(name = FieldConstants.COMPONENTS)
    @Type(type = FieldConstants.JSONB)
    private List<Map<String, Object>> components;

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
