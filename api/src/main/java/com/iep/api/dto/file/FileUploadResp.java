package com.iep.api.dto.file;

import lombok.Data;

@Data
public class FileUploadResp {
    private String uuid;
    private String message;

    public FileUploadResp(String uuid) {
        this.uuid = uuid;
        this.message = "文件上傳成功";
    }
}