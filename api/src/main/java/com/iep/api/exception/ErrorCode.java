package com.iep.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //common
    UNDEFINED(0, "未知的錯誤。", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED(1, "未授權的存取。", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(2, "登入時間已過期。", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(3, "帳號或密碼錯誤", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(4, "沒有權限", HttpStatus.FORBIDDEN),
    NOT_FOUND(5, "資源不存在", HttpStatus.NOT_FOUND),
    BAD_REQUEST(6, "請求錯誤", HttpStatus.BAD_REQUEST),

    //userinfo
    USER_NOT_FOUND(1000, "用戶不存在。", HttpStatus.NOT_FOUND),
    TEACHER_NOT_FOUND(1001, "教師不存在。", HttpStatus.NOT_FOUND),
    STUDENT_NOT_FOUND(1003, "學生不存在。", HttpStatus.NOT_FOUND),
    NOT_A_STUDENT(1004, "用戶不是學生。", HttpStatus.BAD_REQUEST),
    FORBIDDEN_OPERATION(1005, "禁止的操作。", HttpStatus.FORBIDDEN),

    //course
    COURSE_NOT_FOUND(2000, "課程不存在。", HttpStatus.NOT_FOUND),

    //enrollment

    // section
    SECTION_NOT_FOUND(3000, "單元不存在", HttpStatus.NOT_FOUND),

    // chapter
    CHAPTER_NOT_FOUND(4000, "章節不存在", HttpStatus.NOT_FOUND),

    //file
    FILE_NOT_FOUND(10, "文件不存在。", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED(11, "文件上傳失敗。", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_FILE_TYPE(12, "無效的文件類型。", HttpStatus.BAD_REQUEST),
    FILE_SIZE_TOO_LARGE(13, "文件大小超過限制。", HttpStatus.BAD_REQUEST);


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