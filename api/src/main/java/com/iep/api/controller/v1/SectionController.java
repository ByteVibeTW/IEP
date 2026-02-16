package com.iep.api.controller.v1;

import com.iep.api.dto.section.SectionDto;
import com.iep.api.service.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/sections")
@Tag(name = "單元模組", description = "課程單元模組")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    @Operation(summary = "新增單元", description = "建立新的課程單元")
    public ResponseEntity<SectionDto> createSection(@RequestBody SectionDto request) {
        SectionDto response = sectionService.createSection(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "依ID取得單元", description = "根據單元ID取得單元詳細資訊")
    public ResponseEntity<SectionDto> getSectionById(@PathVariable Long id) {
        SectionDto response = sectionService.getSectionById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "取得課程的所有單元", description = "根據課程ID取得所有單元列表")
    public ResponseEntity<List<SectionDto>> getSectionsByCourseId(@PathVariable Long courseId) {
        List<SectionDto> sections = sectionService.getSectionsByCourseId(courseId);
        return ResponseEntity.ok(sections);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新單元", description = "根據單元ID更新單元資訊")
    public ResponseEntity<SectionDto> updateSection(@PathVariable Long id, @RequestBody SectionDto request) {
        SectionDto response = sectionService.updateSection(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(summary = "刪除單元", description = "根據單元ID刪除單元")
    public ResponseEntity<Void> deleteSection(@RequestBody List<Long> ids) {
        sectionService.deleteBatch(ids);
        return ResponseEntity.noContent().build();
    }
}

