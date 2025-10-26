package com.iep.api.dal.dto;

import lombok.Data;

@Data
public class FileUploadResponse {
    private String uuid;
    private String message;

    public FileUploadResponse(String uuid) {
        this.uuid = uuid;
        this.message = "文件上傳成功";
    }
}

