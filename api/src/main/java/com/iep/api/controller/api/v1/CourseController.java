package com.iep.api.controller.api.v1;

import com.iep.api.dto.CourseCreateRequest;
import com.iep.api.dto.CourseResponse;
import com.iep.api.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "課程模組", description = "課程模組")
@RequiredArgsConstructor
public class CourseController {
    
    private final CourseService courseService;
    
    @PostMapping
    @Operation(summary = "新增課程", description = "建立新的課程")
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseCreateRequest request) {
        try {
            CourseResponse response = courseService.createCourse(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "依ID取得課程", description = "根據課程ID取得課程詳細資訊")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        Optional<CourseResponse> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "取得所有課程", description = "取得所有課程的列表")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "依教師ID取得課程", description = "根據教師ID取得教師所授的課程")
    public ResponseEntity<List<CourseResponse>> getCoursesByTeacherId(@PathVariable Long teacherId) {
        try {
            List<CourseResponse> courses = courseService.getCoursesByTeacherId(teacherId);
            return ResponseEntity.ok(courses);
        } catch (RuntimeException e) {
            if ("Teacher not found".equals(e.getMessage())) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新課程", description = "根據課程ID更新課程資訊")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable Long id, @RequestBody CourseCreateRequest request) {
        try {
            CourseResponse response = courseService.updateCourse(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "刪除課程", description = "根據課程ID刪除課程")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
