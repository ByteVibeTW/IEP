package com.iep.api.exception;

import com.iep.api.dal.dto.common.ErrorResp;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CommonException.class)
    protected ResponseEntity<ErrorResp> handleCarbonFootprintException(CommonException e, HttpServletResponse response) {

        ErrorResp errorResp = new ErrorResp();
        errorResp.setErrorCode(e.getErrorCode());
        errorResp.setMessage(e.getMessage());


        log.error("CarbonFootprintException: ", e);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return ResponseEntity.status(e.getHttpStatusCode())
                .body(errorResp);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    protected ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        log.error("SQLIntegrityConstraintViolationException: ", e);

        ErrorResp errorResp = new ErrorResp();
        errorResp.setErrorCode(ErrorCode.UNDEFINED);
        errorResp.setMessage(e.getMessage());
        return ResponseEntity.status(400).body(errorResp);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException: ", e);

        ErrorResp errorResp = new ErrorResp();
        errorResp.setErrorCode(ErrorCode.UNDEFINED);
        errorResp.setMessage("還有相關資料存在，無法進行當前操作。");
        return ResponseEntity.status(400).body(errorResp);
    }


    /**
     * 未經過處理的 Exception
     *
     * @param e Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception e) {
        log.error("Unhandled exception: ", e);

        ErrorResp errorResp = new ErrorResp();
        errorResp.setErrorCode(ErrorCode.UNDEFINED);
        errorResp.setMessage("系統解析資料時發現錯誤。");
        return ResponseEntity.status(500).body(errorResp);
    }
}