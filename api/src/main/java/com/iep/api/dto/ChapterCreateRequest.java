package com.iep.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ChapterCreateRequest {
    private Long courseId;
    private Date date;
    private String name;
    private String stFile;
    private String thFile;
}
