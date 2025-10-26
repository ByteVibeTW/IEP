package com.iep.api.dal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.iep.api.dal.entity.Chapter}
 */
@Schema(description = "課程章節 DTO")
@Data
public class ChapterDto implements Serializable {
    @Schema(description = "章節 ID", example = "1")
    Long id;
    
    @Schema(description = "單元 ID", example = "1")
    Long sectionId;
    
    @Schema(description = "章節名稱", example = "第一章：基礎概念")
    String chapterName;
    
    @Schema(description = "章節內容", example = "本章節介紹...")
    String content;
    
    @Schema(description = "排序索引", example = "1")
    Integer orderIndex;
}

