package com.iep.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //TODO例外錯誤都改成 ErrorCode
    //userinfo
    USER_NOT_FOUND(5, "用戶不存在。", HttpStatus.NOT_FOUND),
    TEACHER_NOT_FOUND(7, "教師不存在。", HttpStatus.NOT_FOUND),
    STUDENT_NOT_FOUND(8, "學生不存在。", HttpStatus.NOT_FOUND),
    NOT_A_STUDENT(9, "用戶不是學生。", HttpStatus.BAD_REQUEST),
    FORBIDDEN_OPERATION(14, "禁止的操作。", HttpStatus.FORBIDDEN),

    //course
    COURSE_NOT_FOUND(6, "課程不存在。", HttpStatus.NOT_FOUND),

    //enrollment


    //file
    FILE_NOT_FOUND(10, "文件不存在。", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED(11, "文件上傳失敗。", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_FILE_TYPE(12, "無效的文件類型。", HttpStatus.BAD_REQUEST),
    FILE_SIZE_TOO_LARGE(13, "文件大小超過限制。", HttpStatus.BAD_REQUEST),

    //common
    UNDEFINED(0, "未知的錯誤。", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(1, "未授權的存取。", HttpStatus.UNAUTHORIZED),
    RESOURCE_NOT_FOUND(2, "資源不存在。", HttpStatus.NOT_FOUND),
    BAD_REQUEST(3, "請求參數錯誤。", HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR(4, "驗證失敗。", HttpStatus.BAD_REQUEST);







    /**
     * 錯誤碼
     */
    private final int code;

    /**
     * 錯誤訊息
     */
    private final String message;

    /**
     * HTTP 狀態碼
     */
    private final HttpStatus httpStatus;

    /**
     * @param code    錯誤碼
     * @param message 錯誤訊息
     */
    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}