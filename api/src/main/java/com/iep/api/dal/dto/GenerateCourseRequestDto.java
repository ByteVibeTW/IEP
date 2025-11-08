package com.iep.api.dal.dto;

import lombok.Data;

import java.util.List;

@Data
public class GenerateCourseRequestDto {
    String UserId;
    List<UserAnswerDto> userAnswer;
}
