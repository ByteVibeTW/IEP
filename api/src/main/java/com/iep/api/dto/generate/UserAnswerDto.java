package com.iep.api.dto.generate;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAnswerDto implements Serializable {
    String questionText;
    String option;
}
