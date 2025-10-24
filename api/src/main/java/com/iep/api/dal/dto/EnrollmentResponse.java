package com.iep.api.dal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentResponse {
    private Long id;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private Long courseId;
    private String courseName;
}
