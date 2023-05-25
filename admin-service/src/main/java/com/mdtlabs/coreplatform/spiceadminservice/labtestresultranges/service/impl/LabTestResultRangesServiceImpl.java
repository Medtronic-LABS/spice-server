package com.mdtlabs.coreplatform.spiceadminservice.labtestresultranges.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdtlabs.coreplatform.common.Constants;
import com.mdtlabs.coreplatform.common.exception.BadRequestException;
import com.mdtlabs.coreplatform.common.exception.DataNotAcceptableException;
import com.mdtlabs.coreplatform.common.exception.DataNotFoundException;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeDTO;
import com.mdtlabs.coreplatform.common.model.dto.spice.LabTestResultRangeRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResult;
import com.mdtlabs.coreplatform.common.model.entity.spice.LabTestResultRange;
import com.mdtlabs.coreplatform.spiceadminservice.labtest.repository.LabTestResultRepository;
import com.mdtlabs.coreplatform.spiceadminservice.labtestresultranges.repository.LabTestResultRangesRepository;
import com.mdtlabs.coreplatform.spiceadminservice.labtestresultranges.service.LabTestResultRangesService;

/**
 * <p>
 * LabTestResultRangesServiceImpl is a Java class that provides methods for adding, updating, and
 * retrieving lab test result ranges, as well as validating input and setting properties of
 * LabTestResultRangeEntity objects.
 * </p>
 *
 * @author Jeyaharini T A created on Feb 7, 2023
 */
@Service
public class LabTestResultRangesServiceImpl implements LabTestResultRangesService {

    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private LabTestResultRangesRepository labTestResultRangesRepository;

    @Autowired
    private LabTestResultRepository labTestResultRepository;

    /**
     * {@inheritDoc}
     */
    public List<LabTestResultRange> addLabTestResultRanges(LabTestResultRangeRequestDTO labTestResultRangeRequestDto) {
        LabTestResult labTestResult = setLabTestResult(labTestResultRangeRequestDto);
        List<LabTestResultRange> listOfLabTestResultRangesToSave = new ArrayList<>();
        List<LabTestResultRangeDTO> labTestResultRanges = labTestResultRangeRequestDto.getLabTestResultRanges();
        labTestResultRanges.forEach(labTestResultRange -> {
            LabTestResultRange labTestResultRangeEntity = new LabTestResultRange();
            labTestResultRangeEntity.setTenantId(labTestResultRangeRequestDto.getTenantId());
            labTestResultRangeEntity.setLabTestId(labTestResult.getLabTestId());
            labTestResultRangeEntity.setLabTestResultId(labTestResult.getId());
            setLabTestResultRangeEntity(labTestResultRangeEntity, labTestResultRange);
            listOfLabTestResultRangesToSave.add(labTestResultRangeEntity);
        });
        return labTestResultRangesRepository.saveAll(listOfLabTestResultRangesToSave);
    }

    /**
     * <p>
     * This method is used to validate the lab test result ranges of given lab test result range dto.
     * </p>
     *
     * @param labTestResultRangeRequestDto {@link LabTestResultRangeRequestDTO} The DTO containing labTest Result
     *                                     *                                     ID and lab test result ranges is given
     * @return {@link List<LabTestResultRange>} The given lab test result range is validated and the list
     * of lab test result ranges is returned
     */
    public List<LabTestResultRange> validateLabTestResultRanges(LabTestResultRangeRequestDTO labTestResultRangeRequestDto) {
        if (Objects.isNull(labTestResultRangeRequestDto)) {
            throw new BadRequestException(1003);
        }
        if (Objects.isNull(labTestResultRangeRequestDto.getLabTestResultId())) {
            throw new BadRequestException(28008);
        }
        List<LabTestResultRange> labTestRangeResult = labTestResultRangesRepository.findByLabTestResultIdAndIsDeletedOrderByDisplayOrderAsc(labTestResultRangeRequestDto.getLabTestResultId(), false);
        for (LabTestResultRangeDTO labTestResultRangeDto : labTestResultRangeRequestDto.getLabTestResultRanges()) {
            if (!Objects.isNull(labTestResultRangeDto.getId())
                    && !Objects.isNull(labTestRangeResult) && labTestRangeResult
                    .stream().noneMatch(labTestRange -> labTestRange.getId().equals(labTestResultRangeDto.getId()))) {
                throw new DataNotAcceptableException(28011);
            }
        }
        return labTestRangeResult;
    }

    /**
     * {@inheritDoc}
     */
    public List<LabTestResultRange> updateLabTestResultRanges(
            LabTestResultRangeRequestDTO labTestResultRangeRequestDto) {
        List<LabTestResultRange> labTestResultRangesToUpdate = new ArrayList<>();
        List<LabTestResultRange> existingLabTestResultRanges = new ArrayList<>();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        List<Long> labTestResultRangeIds = labTestResultRangeRequestDto.getLabTestResultRanges().stream()
                .map(LabTestResultRangeDTO::getId).filter(id -> !Objects.isNull(id))
                .toList();
        if (!labTestResultRangeIds.isEmpty()) {
            existingLabTestResultRanges = labTestResultRangesRepository.findByIdsAndIsDeletedAndTenantId(
                    labTestResultRangeIds, Boolean.FALSE, labTestResultRangeRequestDto.getTenantId());
            if (Objects.isNull(existingLabTestResultRanges) || existingLabTestResultRanges.isEmpty()
                    || existingLabTestResultRanges.size() != labTestResultRangeIds.size()) {
                throw new DataNotFoundException(28006);
            }
        }
        List<LabTestResultRange> labTestResult = validateLabTestResultRanges(labTestResultRangeRequestDto);
        setLabTestResultRange(labTestResultRangeRequestDto, labTestResultRangesToUpdate, existingLabTestResultRanges,
                labTestResult);
        return labTestResultRangesRepository.saveAll(labTestResultRangesToUpdate);
    }

    /**
     * {@inheritDoc}
     */
    public boolean removeLabTestResultRange(long id, Long tenantId) {
        Optional<LabTestResultRange> labTestResultRange = labTestResultRangesRepository.findByIdAndTenantId(id, tenantId);
        if (labTestResultRange.isPresent()) {
            LabTestResultRange resultRange = labTestResultRange.get();
            resultRange.setDeleted(true);
            resultRange.setActive(false);
            labTestResultRangesRepository.save(resultRange);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public List<LabTestResultRange> getLabTestResultRange(long labTestResultId) {
        return labTestResultRangesRepository.findByLabTestResultIdAndIsDeletedOrderByDisplayOrderAsc(labTestResultId, false);
    }

    /**
     * {@inheritDoc}
     */
    public Map<Long, List<LabTestResultRangeDTO>> getLabTestResultRange(List<Long> labTestResultIds) {
        List<LabTestResultRange> labTestRanges = labTestResultRangesRepository.findByLabTestResultIdInAndIsDeletedOrderByDisplayOrderAsc(labTestResultIds, false);
        Map<Long, List<LabTestResultRangeDTO>> labtestRangesMap = new HashMap<>();
        labTestRanges.forEach(ranges -> {
            if (!labtestRangesMap.containsKey(ranges.getLabTestResultId())) {
                labtestRangesMap.put(ranges.getLabTestResultId(), new ArrayList<>());
            }
            labtestRangesMap.get(ranges.getLabTestResultId()).add(modelMapper.map(ranges, LabTestResultRangeDTO.class));
        });
        return labtestRangesMap;
    }

    /**
     * <p>
     * This method is used to set the properties of a LabTestResultRangeEntity object based on the values of a
     * LabTestResultRangeDTO object.
     * </p>
     *
     * @param labTestResultRangeEntity {@link LabTestResultRange} The range of values for a lab test result is given
     * @param labTestResultRangeDto    {@link LabTestResultRangeDTO} The LabTestResultRangeDTO which contains the data to be set
     *                                 in the LabTestResultRange is given
     */
    private void setLabTestResultRangeEntity(LabTestResultRange labTestResultRangeEntity,
                                             LabTestResultRangeDTO labTestResultRangeDto) {
        if (!Objects.isNull(labTestResultRangeDto.getMinimumValue())) {
            labTestResultRangeEntity.setMinimumValue(labTestResultRangeDto.getMinimumValue());
        }
        if (!Objects.isNull(labTestResultRangeDto.getMaximumValue())) {
            labTestResultRangeEntity.setMaximumValue(labTestResultRangeDto.getMaximumValue());
        }
        if (!Objects.isNull(labTestResultRangeDto.getDisplayName())) {
            labTestResultRangeEntity.setDisplayName(labTestResultRangeDto.getDisplayName());
        }
        if (!Objects.isNull(labTestResultRangeDto.getDisplayOrder())) {
            labTestResultRangeEntity.setDisplayOrder(labTestResultRangeDto.getDisplayOrder());
        }
        if (!Objects.isNull(labTestResultRangeDto.getUnit())) {
            labTestResultRangeEntity.setUnit(labTestResultRangeDto.getUnit());
        }
        if (!Objects.isNull(labTestResultRangeDto.getUnitId())) {
            labTestResultRangeEntity.setUnitId(labTestResultRangeDto.getUnitId());
        }
    }

    /**
     * <p>
     * This method is used to set a lab test result and throws exceptions if the input is invalid or if the lab
     * test result does not exist.
     * </p>
     *
     * @param labTestResultRangeRequestDto {@link LabTestResultRangeRequestDTO} A DTO that contains information about a
     *                                     lab test result range, including the lab test result ID,
     *                                     the lab test result ranges, and the tenant ID is given
     * @return {@link LabTestResultRangeRequestDTO} The lab test result range request is set and returned
     */
    private LabTestResult setLabTestResult(LabTestResultRangeRequestDTO labTestResultRangeRequestDto) {
        if (Objects.isNull(labTestResultRangeRequestDto)
                || Objects.isNull(labTestResultRangeRequestDto.getLabTestResultRanges())
                || labTestResultRangeRequestDto.getLabTestResultRanges().isEmpty()) {
            throw new BadRequestException(1003);
        }
        if (Objects.isNull(labTestResultRangeRequestDto.getLabTestResultId())) {
            throw new BadRequestException(28008);
        }
        LabTestResult labTestResult = labTestResultRepository.findByIdAndIsDeletedAndTenantId(
                labTestResultRangeRequestDto.getLabTestResultId(), false, labTestResultRangeRequestDto.getTenantId());
        if (Objects.isNull(labTestResult)) {
            throw new DataNotAcceptableException(28010);
        }
        return labTestResult;
    }

    /**
     * <p>
     * This method is used to update lab test result ranges based on the provided request DTO and existing lab test
     * result ranges.
     * </p>
     *
     * @param labTestResultRangeRequestDto {@link LabTestResultRangeRequestDTO} The dto has the lab test result ranges
     *                                     that need to set lab test result range
     *                                     is given
     * @param labTestResultRangesToUpdate  {@link List<LabTestResultRange>} A list of LabTestResultRanges that need to
     *                                     be updated with new or modified
     *                                     LabTestResultRangeDTOs is given
     * @param existingLabTestResultRanges  {@link List<LabTestResultRange>} A list of existing LabTestResultRanges
     *                                     that are already stored in the database and
     *                                     need to be updated with new information is given
     * @param labTestResult                {@link List<LabTestResultRange>} A list of LabTestResult objects that used to set lab test
     *                                     result range is given
     */
    private void setLabTestResultRange(LabTestResultRangeRequestDTO labTestResultRangeRequestDto,
                                       List<LabTestResultRange> labTestResultRangesToUpdate, List<LabTestResultRange> existingLabTestResultRanges,
                                       List<LabTestResultRange> labTestResult) {
        LabTestResultRange labTestResultRange;
        for (LabTestResultRangeDTO labTestResultRangeDto : labTestResultRangeRequestDto.getLabTestResultRanges()) {
            if (Objects.isNull(labTestResultRangeDto.getId()) || Constants.ZERO == labTestResultRangeDto.getId()) {
                labTestResultRange = modelMapper.map(labTestResultRangeDto, LabTestResultRange.class);
                labTestResultRange.setLabTestResultId(labTestResultRangeRequestDto.getLabTestResultId());
                labTestResultRange.setLabTestId(labTestResult.get(0).getLabTestId());
                labTestResultRange.setTenantId(labTestResultRangeRequestDto.getTenantId());
                labTestResultRangesToUpdate.add(labTestResultRange);
            } else {
                LabTestResultRange existingLabTestResultRangeEntity = existingLabTestResultRanges.stream().filter(
                                existingLabTestResultRange -> (Objects.equals(existingLabTestResultRange.getId(), labTestResultRangeDto.getId())))
                        .findFirst().orElseThrow(() -> new DataNotFoundException(28006));
                modelMapper.map(labTestResultRangeDto, existingLabTestResultRangeEntity);
                existingLabTestResultRangeEntity.setLabTestId(labTestResult.get(0).getLabTestId());
                existingLabTestResultRangeEntity.setLabTestResultId(labTestResultRangeRequestDto.getLabTestResultId());
                existingLabTestResultRangeEntity.setTenantId(labTestResultRangeRequestDto.getTenantId());
                labTestResultRangesToUpdate.add(existingLabTestResultRangeEntity);
            }
        }
    }
}
