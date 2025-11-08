package com.iep.api.dal.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    String questionText;
    List<String> options;
}
