package com.iep.api.controller.v1;

import com.iep.api.dto.course.CourseReq;
import com.iep.api.dto.course.CourseResp;
import com.iep.api.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@Tag(name = "課程模組", description = "課程模組")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @Operation(summary = "新增課程", description = "建立新的課程")
    public ResponseEntity<CourseResp> createCourse(@RequestBody CourseReq request) {
        CourseResp response = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "取得所有課程", description = "取得所有課程")
    public ResponseEntity<List<CourseResp>> getAllCourses() {
        List<CourseResp> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/current")
    @Operation(summary = "取得當前用戶開設課程", description = "取得當前用戶開設課程")
    public ResponseEntity<List<CourseResp>> getCurrentCourses() {
        List<CourseResp> courses = courseService.getCurrentCourses();
        return ResponseEntity.ok(courses);
    }

//    @GetMapping("/select")
//    @Operation(summary = "取得當前用戶已選的課程", description = "取得當前用戶已選的課程")
//    public ResponseEntity<List<CourseDto>> getSelectedCourses() {
//        List<CourseDto> courses = courseService.getSelectedCourses();
//        return ResponseEntity.ok(courses);
//    }

    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "依教師ID取得課程", description = "根據教師ID取得教師所授的課程")
    public ResponseEntity<List<CourseResp>> getCoursesByTeacherId(@PathVariable Long teacherId) {
        List<CourseResp> courses = courseService.getCourseByTeacherId(teacherId);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "依ID取得課程", description = "根據課程ID取得課程詳細資訊")
    public ResponseEntity<CourseResp> getCourseById(@PathVariable Long id) {
        CourseResp course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新課程", description = "根據課程ID更新課程資訊")
    public ResponseEntity<CourseResp> updateCourse(@PathVariable Long id, @RequestBody CourseReq request) {
        CourseResp response = courseService.updateCourse(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(summary = "刪除課程", description = "根據課程ID刪除課程")
    public ResponseEntity<Void> deleteCourse(@RequestBody List<Long> ids) {
        courseService.deleteBatch(ids);
        return ResponseEntity.noContent().build();
    }
}
