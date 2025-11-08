package com.iep.api.dal.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionsResponseDto {
    private List<QuestionDto> questions;
}
