package com.iep.api.controller.v1;

import com.iep.api.dto.file.FileUploadResp;
import com.iep.api.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "文件模組", description = "文件上傳和下載相關API")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "上傳圖片", description = "上傳圖片並返回UUID")
    public ResponseEntity<FileUploadResp> uploadFile(@RequestParam("file") MultipartFile file) {
        String uuid = fileService.uploadFile(file);
        return ResponseEntity.ok(new FileUploadResp(uuid));
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "下載圖片", description = "根據UUID下載圖片")
    public ResponseEntity<Resource> downloadFile(@PathVariable String uuid) {
        Resource resource = fileService.downloadFile(uuid);
        String contentType = fileService.getContentType(uuid);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

