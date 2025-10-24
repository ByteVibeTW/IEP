package com.iep.api.dal.dto.common;

import com.iep.api.exception.CommonException;
import com.iep.api.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResp {
    private int code;
    private String message;
    private String httpStatus;
    private List<Object> params;
    
    public ErrorResp(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.httpStatus = errorCode.getHttpStatus().toString();
        this.params = null;
    }
    
    public ErrorResp(ErrorCode errorCode, String customMessage) {
        this.code = errorCode.getCode();
        this.message = customMessage;
        this.httpStatus = errorCode.getHttpStatus().toString();
        this.params = null;
    }
    
    public ErrorResp(CommonException exception) {
        this.code = exception.getErrorCode().getCode();
        this.message = exception.getMessage();
        this.httpStatus = exception.getHttpStatusCode().toString();
        this.params = exception.getParams() != null && exception.getParams().length > 0 
                ? Arrays.asList(exception.getParams()) 
                : null;
    }
}