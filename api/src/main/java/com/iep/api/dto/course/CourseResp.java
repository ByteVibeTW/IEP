package com.iep.api.dto.course;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.iep.api.dal.entity.Course}
 */
@Data
public class CourseResp implements Serializable {
    Long id;
    String teacherUsername;
    String name;
    String type;
    String intro;
    String outline;
    String imageUuid;
    String imageName;
}