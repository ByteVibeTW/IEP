package com.iep.api.controller.v1;

import com.iep.api.dto.chapter.ChapterDto;
import com.iep.api.service.ChapterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/chapters")
@Tag(name = "章節模組", description = "課程章節模組")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    @PostMapping
    @Operation(summary = "新增章節", description = "建立新的課程章節")
    public ResponseEntity<ChapterDto> createChapter(@RequestBody ChapterDto request) {
        ChapterDto response = chapterService.createChapter(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "依ID取得章節", description = "根據章節ID取得章節詳細資訊")
    public ResponseEntity<ChapterDto> getChapterById(@PathVariable Long id) {
        ChapterDto chapter = chapterService.getChapterById(id);
        return ResponseEntity.ok(chapter);
    }

    @GetMapping("/section/{sectionId}")
    @Operation(summary = "取得單元的所有章節", description = "根據單元ID取得所有章節列表")
    public ResponseEntity<List<ChapterDto>> getChaptersBySectionId(@PathVariable Long sectionId) {
        List<ChapterDto> chapters = chapterService.getChaptersBySectionId(sectionId);
        return ResponseEntity.ok(chapters);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新章節", description = "根據章節ID更新章節資訊")
    public ResponseEntity<ChapterDto> updateChapter(@PathVariable Long id, @RequestBody ChapterDto request) {
        ChapterDto response = chapterService.updateChapter(id, request);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping
    @Operation(summary = "刪除章節", description = "根據章節ID刪除章節")
    public ResponseEntity<Void> deleteChapter(@RequestBody List<Long> ids) {
        chapterService.deleteBatch(ids);
        return ResponseEntity.noContent().build();

    }
}

