package com.iep.api.service;

import com.iep.api.exception.CommonException;
import com.iep.api.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Value("${app.upload-file.dir}")
    private String uploadDir;

    private Path getUploadPath() {
        Path path = Paths.get(uploadDir);
        try {
            Files.createDirectories(path); // 這個方法本身就會檢查是否存在
            return path;
        } catch (IOException e) {
            log.error("Failed to create upload directory", e);
            throw new CommonException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    private Path findFileByUuid(String uuid) {
        try {
            return Files.list(getUploadPath())
                    .filter(path -> path.getFileName().toString().startsWith(uuid + "."))
                    .findFirst()
                    .orElseThrow(() -> new CommonException(ErrorCode.FILE_NOT_FOUND));
        } catch (IOException e) {
            log.error("Failed to search file: {}", uuid, e);
            throw new CommonException(ErrorCode.FILE_NOT_FOUND);
        }
    }

    public String uploadFile(MultipartFile file) {
        // 驗證文件類型
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new CommonException(ErrorCode.INVALID_FILE_TYPE);
        }

        // 驗證文件大小
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new CommonException(ErrorCode.FILE_SIZE_TOO_LARGE);
        }

        // 生成檔案名
        String uuid = UUID.randomUUID().toString();
        String extension = getFileExtension(file.getOriginalFilename());
        String filename = uuid + extension;

        // 保存文件
        try {
            Path targetPath = getUploadPath().resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("File uploaded successfully: {}", filename);
            return uuid;
        } catch (IOException e) {
            log.error("Failed to upload file", e);
            throw new CommonException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    public Resource downloadFile(String uuid) {
        try {
            Path filePath = findFileByUuid(uuid);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.isReadable()) {
                throw new CommonException(ErrorCode.FILE_NOT_FOUND);
            }
            return resource;
        } catch (MalformedURLException e) {
            log.error("Failed to load file: {}", uuid, e);
            throw new CommonException(ErrorCode.FILE_NOT_FOUND);
        }
    }

    public String getContentType(String uuid) {
        try {
            Path filePath = findFileByUuid(uuid);
            String contentType = Files.probeContentType(filePath);
            return contentType != null ? contentType : "application/octet-stream";
        } catch (IOException e) {
            log.error("Failed to determine content type for file: {}", uuid, e);
            return "application/octet-stream";
        }
    }
}