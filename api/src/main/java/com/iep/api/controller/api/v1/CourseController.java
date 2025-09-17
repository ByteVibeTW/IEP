package com.iep.api.controller.api.v1;

import com.iep.api.dto.CourseCreateRequest;
import com.iep.api.dto.CourseResponse;
import com.iep.api.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    
    private final CourseService courseService;
    
    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseCreateRequest request) {
        try {
            CourseResponse response = courseService.createCourse(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        Optional<CourseResponse> course = courseService.getCourseById(id);
        return course.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
    
    @GetMapping("/teacher/{teacherId}")
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
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable Long id, @RequestBody CourseCreateRequest request) {
        try {
            CourseResponse response = courseService.updateCourse(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
