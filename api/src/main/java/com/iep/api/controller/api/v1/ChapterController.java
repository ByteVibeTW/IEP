package com.iep.api.controller.api.v1;

import com.iep.api.dto.ChapterCreateRequest;
import com.iep.api.dto.ChapterResponse;
import com.iep.api.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/chapters")
@RequiredArgsConstructor
public class ChapterController {
    
    private final ChapterService chapterService;
    
    @PostMapping
    public ResponseEntity<ChapterResponse> createChapter(@RequestBody ChapterCreateRequest request) {
        try {
            ChapterResponse response = chapterService.createChapter(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ChapterResponse> getChapterById(@PathVariable Long id) {
        Optional<ChapterResponse> chapter = chapterService.getChapterById(id);
        return chapter.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<ChapterResponse>> getAllChapters() {
        List<ChapterResponse> chapters = chapterService.getAllChapters();
        return ResponseEntity.ok(chapters);
    }
    
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ChapterResponse>> getChaptersByCourseId(@PathVariable Long courseId) {
        List<ChapterResponse> chapters = chapterService.getChaptersByCourseId(courseId);
        return ResponseEntity.ok(chapters);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ChapterResponse> updateChapter(@PathVariable Long id, @RequestBody ChapterCreateRequest request) {
        try {
            ChapterResponse response = chapterService.updateChapter(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        try {
            chapterService.deleteChapter(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
