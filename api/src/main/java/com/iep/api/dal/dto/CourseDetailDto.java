package com.iep.api.dal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for detailed course information including sections and chapters
 */
@Schema(description = "課程詳細資訊 DTO（包含單元和章節）")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailDto implements Serializable {
    @Schema(description = "課程 ID", example = "1")
    Long id;

    @Schema(description = "老師權限", example = "TEACHER")
    String teacherRoleCode;

    @Schema(description = "老師名稱", example = "AI Tutor")
    String teacherUsername;

    @Schema(description = "課程名稱", example = "Java 程式設計")
    String name;

    @Schema(description = "課程類型", example = "基礎課程")
    String type;

    @Schema(description = "課程簡介", example = "本課程將教授 Java 程式設計基礎")
    String intro;

    @Schema(description = "課程大綱", example = "1. Java 基礎語法\n2. 物件導向程式設計\n3. 資料結構與演算法")
    String outline;

    @Schema(description = "課程圖片 UUID", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    String imageUuid;

    @Schema(description = "課程圖片檔名", example = "course_image.jpg")
    String imageName;

    @Schema(description = "課程單元列表")
    private List<SectionWithChaptersDto> sections = new ArrayList<>();
}
