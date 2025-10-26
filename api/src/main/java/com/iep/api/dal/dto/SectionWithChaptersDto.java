package com.iep.api.dal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Section with chapters DTO
 */
@Schema(description = "單元詳細資訊 DTO（包含章節）")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionWithChaptersDto implements Serializable {
    @Schema(description = "單元 ID", example = "1")
    Long id;

    @Schema(description = "課程 ID", example = "1")
    Long courseId;

    @Schema(description = "單元名稱", example = "第一單元：基礎概念")
    String sectionName;

    @Schema(description = "單元描述", example = "本單元介紹...")
    String description;

    @Schema(description = "排序索引", example = "1")
    Integer orderIndex;

    @Schema(description = "章節列表")
    private List<ChapterDto> chapters = new ArrayList<>();
}

