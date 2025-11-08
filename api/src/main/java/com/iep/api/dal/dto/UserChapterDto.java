package com.iep.api.dal.dto;

import lombok.Data;

@Data
public class UserChapterDto {
    Long chapterId;
    String chapterName;
    String courseName;
    String intro;
    String sectionName;
}
