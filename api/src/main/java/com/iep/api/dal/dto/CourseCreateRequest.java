package com.iep.api.dal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCreateRequest {
    private String teacherId;
    private String name;
    private String type;
    private String intro;
    private String outline;
    private String imageUuid;
}
