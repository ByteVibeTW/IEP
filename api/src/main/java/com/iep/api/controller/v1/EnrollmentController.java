package com.iep.api.controller.v1;

import com.iep.api.dal.dto.EnrollmentDto;
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
@Tag(name = "課程綁定模組", description = "課程綁定模組")
@RequiredArgsConstructor
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    
    @PostMapping
    @Operation(summary = "課程綁定學生", description = "課程綁定學生")
    public ResponseEntity<EnrollmentDto> createEnrollment(@RequestBody EnrollmentDto request) {
        EnrollmentDto response = enrollmentService.createEnrollment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "取得所有綁定", description = "取得所有報名的列表")
    public ResponseEntity<List<EnrollmentDto>> getAllEnrollments() {
        List<EnrollmentDto> enrollments = enrollmentService.getAllEnrollments();
        return ResponseEntity.ok(enrollments);
    }

//    @GetMapping("/student")
//    @Operation(summary = "依學生ID取得報名", description = "根據學生ID取得學生的所有報名")
//    public ResponseEntity<List<EnrollmentResponse>> getEnrollmentsByStudentId() {
//        List<EnrollmentResponse> enrollments = enrollmentService.getEnrollmentsByStudentId();
//        return ResponseEntity.ok(enrollments);
//    }
//
//    @DeleteMapping("/{sub}")
//    @Operation(summary = "刪除報名", description = "根據報名ID刪除報名")
//    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long sub) {
//        try {
//            enrollmentService.deleteEnrollment(sub);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
