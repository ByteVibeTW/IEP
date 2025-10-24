package com.iep.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 業務異常類
 * 用於處理業務邏輯相關的異常
 */
@Getter
public class CommonException extends RuntimeException {

    /**
     * HTTP 狀態碼
     */
    protected HttpStatus httpStatusCode;
    /**
     * 錯誤代碼
     */
    protected ErrorCode errorCode;

    /**
     * 工程用錯誤訊息
     */
    protected String message;

    /**
     * 錯誤訊息參數
     */
    protected Object[] params;

    public CommonException(HttpStatus httpStatusCode, ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.params = new Object[]{};
    }

    public CommonException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatusCode = errorCode.getHttpStatus();
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.params = new Object[]{};
    }

    public CommonException(ErrorCode errorCode, Object... params) {
        super(errorCode.getMessage());
        this.httpStatusCode = errorCode.getHttpStatus();
        this.errorCode = errorCode;
        this.params = params;
        this.message = String.format(errorCode.getMessage(), params);
    }

    public CommonException(HttpStatus httpStatusCode, ErrorCode errorCode, Object... params) {
        super(errorCode.getMessage());
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.params = params;
        this.message = String.format(errorCode.getMessage(), params);
    }

    public CommonException(HttpStatus httpStatusCode, ErrorCode errorCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.message = message;
        this.params = new Object[]{};
    }

    public CommonException(HttpStatus httpStatusCode, ErrorCode errorCode, String message, Object... params) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.errorCode = errorCode;
        this.message = message;
        this.params = params;
    }
}
