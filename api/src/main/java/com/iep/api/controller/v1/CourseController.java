package com.iep.api.controller.v1;

import com.iep.api.dal.dto.CourseDto;
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
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto request) {
        CourseDto response = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "依ID取得課程", description = "根據課程ID取得課程詳細資訊")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        Optional<CourseDto> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "取得所有課程", description = "取得所有課程的列表")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<CourseDto> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "依教師ID取得課程", description = "根據教師ID取得教師所授的課程")
    public ResponseEntity<List<CourseDto>> getCoursesByTeacherId(@PathVariable String teacherId) {
        List<CourseDto> courses = courseService.getCoursesByTeacherId(teacherId);
        return ResponseEntity.ok(courses);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新課程", description = "根據課程ID更新課程資訊")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @RequestBody CourseDto request) {
        CourseDto response = courseService.updateCourse(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "刪除課程", description = "根據課程ID刪除課程")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
