package com.iep.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentCreateRequest {
    private Long studentId;
    private Long courseId;
}
