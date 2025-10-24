package com.iep.api.dal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentCreateRequest {
    private String studentId;
    private Long courseId;
}
