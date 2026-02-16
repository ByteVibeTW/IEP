package com.iep.api.dto.common;

import com.iep.api.exception.ErrorCode;
import lombok.Data;

import java.util.List;

@Data
public class ErrorResp {
    private ErrorCode errorCode;
    private String message;
    private List<Object> params;
}
