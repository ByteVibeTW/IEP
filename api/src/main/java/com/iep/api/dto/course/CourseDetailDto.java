package com.iep.api.dto.course;

import com.iep.api.dto.section.SectionWithChaptersDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for課程詳細資訊（包含單元與章節）
 */
@Schema(description = "課程詳細資訊 DTO")
@Data
public class CourseDetailDto implements Serializable {
    @Schema(description = "課程 ID", example = "1")
    Long id;

    @Schema(description = "老師名稱", example = "Teacher")
    String teacherUsername;

    @Schema(description = "課程名稱", example = "Vue.js 前端開發實戰")
    String name;

    @Schema(description = "課程類型", example = "程式設計")
    String type;

    @Schema(description = "課程簡介")
    String intro;

    @Schema(description = "課程大綱")
    String outline;

    @Schema(description = "課程圖片 UUID")
    String imageUuid;

    @Schema(description = "課程圖片檔名")
    String imageName;

    @Schema(description = "課程單元列表")
    List<SectionWithChaptersDto> sections;
}