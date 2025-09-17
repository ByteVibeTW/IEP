package com.iep.api.controller.api.v1;

import com.iep.api.dto.EnrollmentCreateRequest;
import com.iep.api.dto.EnrollmentResponse;
import com.iep.api.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/enrollments")
@Tag(name = "報名模組", description = "報名模組")
@RequiredArgsConstructor
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    
    @PostMapping
    @Operation(summary = "建立報名", description = "建立新的課程報名")
    public ResponseEntity<EnrollmentResponse> createEnrollment(@RequestBody EnrollmentCreateRequest request) {
        try {
            EnrollmentResponse response = enrollmentService.createEnrollment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "依ID取得報名", description = "根據報名ID取得報名詳細資訊")
    public ResponseEntity<EnrollmentResponse> getEnrollmentById(@PathVariable Long id) {
        Optional<EnrollmentResponse> enrollment = enrollmentService.getEnrollmentById(id);
        return enrollment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "取得所有報名", description = "取得所有報名的列表")
    public ResponseEntity<List<EnrollmentResponse>> getAllEnrollments() {
        List<EnrollmentResponse> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/student/{studentId}")
    @Operation(summary = "依學生ID取得報名", description = "根據學生ID取得學生的所有報名")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByStudentId(@PathVariable Long studentId) {
        List<EnrollmentResponse> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(enrollments);
    }
    
    @GetMapping("/course/{courseId}")
    @Operation(summary = "依課程ID取得報名", description = "根據課程ID取得課程的所有報名")
    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByCourseId(@PathVariable Long courseId) {
        List<EnrollmentResponse> enrollments = enrollmentService.getEnrollmentsByCourseId(courseId);
        return ResponseEntity.ok(enrollments);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "刪除報名", description = "根據報名ID刪除報名")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        try {
            enrollmentService.deleteEnrollment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
