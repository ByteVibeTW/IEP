package com.iep.api.exception;

import com.iep.api.dal.dto.common.ErrorResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 處理業務異常
     */
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResp> handleCommonException(CommonException e) {
        log.warn("業務異常: {}", e.getMessage());
        ErrorResp errorResp = new ErrorResp(e);
        return ResponseEntity
                .status(e.getHttpStatusCode())
                .body(errorResp);
    }

    /**
     * 處理所有未捕獲的異常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResp> handleException(Exception e) {
        log.error("未預期的異常", e);
        ErrorResp errorResp = new ErrorResp(ErrorCode.UNDEFINED, "發生未預期的錯誤");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResp);
    }
}