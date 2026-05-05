package com.iep.api.dto.generate;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenerateCourseRespDto implements Serializable {
    String status;
    Long courseId;
}
