package com.iep.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCreateRequest {
    private Long teacherId;
    private String name;
    private String type;
    private String intro;
    private String outline;
    private String imageUuid;
}
