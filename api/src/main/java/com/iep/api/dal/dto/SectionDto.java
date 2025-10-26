package com.iep.api.dal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.iep.api.dal.entity.Section}
 */
@Schema(description = "課程單元 DTO")
@Data
public class SectionDto implements Serializable {
    @Schema(description = "單元 ID", example = "1")
    Long id;

    @Schema(description = "課程 ID", example = "1")
    Long courseId;

    @Schema(description = "單元名稱", example = "第一單元：基礎概念")
    String sectionName;

    @Schema(description = "排序索引", example = "1")
    Integer orderIndex;
}
