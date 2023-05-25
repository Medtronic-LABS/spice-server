package com.mdtlabs.coreplatform.common.model.dto.spice;

import com.google.common.reflect.TypeToken;
import com.mdtlabs.coreplatform.common.model.entity.spice.Comorbidity;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complaints;
import com.mdtlabs.coreplatform.common.model.entity.spice.Complication;
import com.mdtlabs.coreplatform.common.model.entity.spice.CurrentMedication;
import com.mdtlabs.coreplatform.common.model.entity.spice.Lifestyle;
import com.mdtlabs.coreplatform.common.model.entity.spice.PhysicalExamination;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * The Java class `MedicalReviewStaticDataDTO` contains methods that use `ModelMapper` to map lists of
 * objects to lists of DTOs.
 * </p>
 */
@Data
public class MedicalReviewStaticDataDTO {
    private List<Comorbidity> comorbidity;

    private List<Complication> complications;

    private List<CurrentMedication> currentMedication;

    private List<PhysicalExamination> physicalExamination;

    private List<Lifestyle> lifestyle;

    private List<Complaints> complaints;

    private Map<String, Object> treatmentPlanFormData;

    /**
     * <p>
     * This function returns a list of MedicalReviewStaticDataListDTO objects mapped from a list of
     * comorbidity data using ModelMapper.
     * </p>
     *
     * @return {@link List<MedicalReviewStaticDataListDTO>} objects is being returned
     */
    public List<MedicalReviewStaticDataListDTO> getComorbidity() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(comorbidity, new TypeToken<List<MedicalReviewStaticDataListDTO>>() {
        }.getType());
    }

    /**
     * <p>
     * This Java function maps a list of lifestyle objects to a list of lifestyle DTOs using ModelMapper.     *
     * </p>
     *
     * @return {@link List<LifestyleListDTO>} The method uses a `ModelMapper` to
     * map the `lifestyle` object to a list of `LifestyleListDTO` objects.
     */
    public List<LifestyleListDTO> getLifestyle() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(lifestyle, new TypeToken<List<LifestyleListDTO>>() {
        }.getType());
    }

    /**
     * <p>
     * This function returns a list of MedicalReviewStaticDataListDTO objects mapped from a list of
     * complaints using ModelMapper.
     * </p>
     *
     * @return {@link List<MedicalReviewStaticDataListDTO>} which are mapped from the "complaints"
     * object using a ModelMapper.
     */
    public List<MedicalReviewStaticDataListDTO> getComplaints() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(complaints, new TypeToken<List<MedicalReviewStaticDataListDTO>>() {
        }.getType());
    }

}
