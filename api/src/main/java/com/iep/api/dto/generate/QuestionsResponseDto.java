package com.iep.api.dto.generate;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionsResponseDto implements Serializable {
    List<QuestionSectionDto> questions;
}
