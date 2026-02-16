package com.iep.api.dto.course;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.iep.api.dal.entity.Course}
 */
@Value
public class CourseReq implements Serializable {
    Long id;
    String name;
    String type;
    String intro;
    String outline;
    String imageUuid;
    String imageName;
}