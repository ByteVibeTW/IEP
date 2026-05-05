package com.iep.api.dto.generate;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GenerateCourseReqDto implements Serializable {
    String userId;
    List<UserAnswerDto> userAnswer;
}
