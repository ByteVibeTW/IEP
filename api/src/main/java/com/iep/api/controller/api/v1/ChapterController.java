package com.iep.api.controller.api.v1;

import com.iep.api.dto.ChapterCreateRequest;
import com.iep.api.dto.ChapterResponse;
import com.iep.api.service.ChapterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/chapters")
@Tag(name = "章節模組", description = "章節模組")
@RequiredArgsConstructor
public class ChapterController {
    
    private final ChapterService chapterService;
    
    @PostMapping
    @Operation(summary = "新增供應商模組", description = "建立新的章節模組")
    public ResponseEntity<ChapterResponse> createChapter(@RequestBody ChapterCreateRequest request) {
        try {
            ChapterResponse response = chapterService.createChapter(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "依ID取得章節", description = "根據章節ID取得章節詳細資訊")
    public ResponseEntity<ChapterResponse> getChapterById(@PathVariable Long id) {
        Optional<ChapterResponse> chapter = chapterService.getChapterById(id);
        return chapter.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "取得所有章節", description = "取得所有章節的列表")
    public ResponseEntity<List<ChapterResponse>> getAllChapters() {
        List<ChapterResponse> chapters = chapterService.getAllChapters();
        return ResponseEntity.ok(chapters);
    }
    
    @GetMapping("/course/{courseId}")
    @Operation(summary = "依課程ID取得章節", description = "根據課程ID取得課程的所有章節")
    public ResponseEntity<List<ChapterResponse>> getChaptersByCourseId(@PathVariable Long courseId) {
        List<ChapterResponse> chapters = chapterService.getChaptersByCourseId(courseId);
        return ResponseEntity.ok(chapters);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新章節", description = "根據章節ID更新章節資訊")
    public ResponseEntity<ChapterResponse> updateChapter(@PathVariable Long id, @RequestBody ChapterCreateRequest request) {
        try {
            ChapterResponse response = chapterService.updateChapter(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "刪除章節", description = "根據章節ID刪除章節")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        try {
            chapterService.deleteChapter(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
