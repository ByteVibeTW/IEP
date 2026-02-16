package com.iep.api.controller.v1;

import com.iep.api.dto.enrollment.EnrollmentDto;
import com.iep.api.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
@Tag(name = "課程選課模組", description = "課程選課模組")
@RequiredArgsConstructor
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    
    @PostMapping
    @Operation(summary = "學生選擇課程", description = "學生選擇課程")
    public ResponseEntity<EnrollmentDto> createEnrollment(@RequestBody EnrollmentDto request) {
        EnrollmentDto response = enrollmentService.createEnrollment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/current")
    @Operation(summary = "獲取當前使用者的選課", description = "獲取當前使用者的選課")
    public ResponseEntity<List<EnrollmentDto>> getCurrentUserEnrollments() {
        List<EnrollmentDto> enrollments = enrollmentService.getCurrentUserEnrollments();
        return ResponseEntity.ok(enrollments);
    }
}
