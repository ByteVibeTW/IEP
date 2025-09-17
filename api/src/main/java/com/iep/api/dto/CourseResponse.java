package com.iep.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseResponse {
    private Long id;
    private Long teacherId;
    private String teacherName;
    private String teacherEmail;
    private String name;
    private String type;
    private String intro;
    private String outline;
    private String imageUuid;
}
