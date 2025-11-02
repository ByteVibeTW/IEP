package com.iep.api.dal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.iep.api.dal.entity.Enrollment}
 */
@Schema(description = "課程綁定 DTO")
@Data
public class EnrollmentDto implements Serializable {
    @Schema(description = "課程綁定 ID", example = "1")
    Long id;
    @Schema(description = "學生 sub", example = "7349e783-e2d8-4787-90a0-e44e4240ae44")
    String studentSub;
    @Schema(description = "課程 ID", example = "1")
    Long courseId;
}