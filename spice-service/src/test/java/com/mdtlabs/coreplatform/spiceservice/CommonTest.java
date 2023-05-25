package com.mdtlabs.coreplatform.spiceservice;

import com.mdtlabs.coreplatform.common.model.dto.spice.EnrollmentRequestDTO;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLog;
import com.mdtlabs.coreplatform.common.model.entity.spice.BpLogDetails;
import com.mdtlabs.coreplatform.spiceservice.util.TestDataProvider;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p>
 * CommonTest class used to test all possible positive
 * and negative cases for all methods and conditions used for all class.
 * </p>
 *
 * @author Jaganathan created on Jan 30, 2023
 */
@ExtendWith(MockitoExtension.class)
class CommonTest {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private static void validate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Stream<Arguments> enrollmentData() {

        return Stream.of(
                Arguments.of("male", 1l, 23, true,
                        TestDataProvider.getBpLog(80, 50, List.of(), 0), 0),
                Arguments.of(null, null, null, null, null, 5),
                Arguments.of("female", null, null, null, null, 4),
                Arguments.of("male", 1l, null, null, null, 3),
                Arguments.of("male", 1l, 23, null, null, 2),
                Arguments.of("male", 1l, 23, true, null, 1),
                Arguments.of("", 1l, null, null, null, 4),
                Arguments.of("", 1l, 23, null, null, 3),
                Arguments.of("", 1l, 23, true, null, 2),
                Arguments.of("", 1l, 23, true,
                        TestDataProvider.getBpLog(80, 50, List.of(), 0), 1)

        );
    }

    private static Stream<Arguments> bpLogData() {
        return Stream.of(
                Arguments.of(80, 50, List.of(1l, 2l), 0),
                Arguments.of(80, null, null, 1),
                Arguments.of(null, 50, List.of(1l, 2l), 1),
                Arguments.of(null, null, List.of(1l, 2l), 2),
                Arguments.of(null, null, List.of(""), 2),
                Arguments.of(null, null, null, 2)
        );
    }

    @ParameterizedTest
    @MethodSource("enrollmentData")
    void getEnrollmentDTOData(String gender,
                                     Long siteId, Integer age, Boolean isRegularSmoker,
                                     BpLog bplog, int violationSize) {
        validate();
        EnrollmentRequestDTO enrollmentRequestDTO = new EnrollmentRequestDTO();
        enrollmentRequestDTO.setGender(gender);
        enrollmentRequestDTO.setSiteId(siteId);
        enrollmentRequestDTO.setAge(age);
        enrollmentRequestDTO.setIsRegularSmoker(isRegularSmoker);
        enrollmentRequestDTO.setBpLog(bplog);
        Set<ConstraintViolation<EnrollmentRequestDTO>> violations = validator.validate(enrollmentRequestDTO);
        assertThat(violations).hasSize(violationSize);
    }

    @ParameterizedTest
    @MethodSource("bpLogData")
    void getBpLogObject(Integer avgSystolic,
                               Integer avgDiastolic, List<BpLogDetails> bpLogDetails, int violationSize) {
        validate();
        BpLog bpLog = new BpLog();
        bpLog.setAvgSystolic(avgSystolic);
        bpLog.setAvgDiastolic(avgDiastolic);
        bpLog.setBpLogDetails(bpLogDetails);
        Set<ConstraintViolation<BpLog>> violations = validator.validate(bpLog);
        assertThat(violations).hasSize(violationSize);

    }
}
