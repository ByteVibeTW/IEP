package com.iep.api.dto.generate;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChapterContentReqDto implements Serializable {
    String courseName;
    String intro;
    String sectionName;
    Long chapterId;
    String chapterName;
}
