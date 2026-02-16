package com.iep.api.dto.enrollment;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.iep.api.dal.entity.Enrollment}
 */
@Data
public class EnrollmentDto implements Serializable {
    Long id;
    Long studentId;
    Long courseId;
}