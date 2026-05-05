package com.iep.api.dto.generate;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QuestionSectionDto implements Serializable {
    String questionText;
    List<String> options;
}
