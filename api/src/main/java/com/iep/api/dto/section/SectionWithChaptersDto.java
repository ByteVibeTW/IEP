package com.iep.api.dto.section;

import com.iep.api.dto.chapter.ChapterDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for課程單元與章節
 */
@Schema(description = "課程單元與章節 DTO")
@Data
public class SectionWithChaptersDto implements Serializable {
    @Schema(description = "單元 ID", example = "1")
    Long id;

    @Schema(description = "課程 ID", example = "1")
    Long courseId;

    @Schema(description = "單元名稱", example = "第一單元：基礎概念")
    String sectionName;

    @Schema(description = "排序索引", example = "1")
    Integer orderIndex;

    @Schema(description = "章節列表")
    List<ChapterDto> chapters;
}