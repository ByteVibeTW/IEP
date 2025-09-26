package com.iep.api.service;

import com.iep.api.dal.entity.Enrollment;
import com.iep.api.dal.entity.UserInfo;
import com.iep.api.dal.entity.Course;
import com.iep.api.dal.repository.EnrollmentRepository;
import com.iep.api.dal.repository.UserInfoRepository;
import com.iep.api.dal.repository.CourseRepository;
import com.iep.api.dal.mapper.EnrollmentMapper;
import com.iep.api.dto.EnrollmentCreateRequest;
import com.iep.api.dto.EnrollmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final UserInfoRepository userInfoRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;
    
    public EnrollmentResponse createEnrollment(EnrollmentCreateRequest request) {
        UserInfo student = userInfoRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        // 驗證用戶是否為學生
        if (!student.isStudent()) {
            throw new RuntimeException("User is not a student");
        }
        
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        Enrollment enrollment = enrollmentMapper.toEntity(request);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toResponse(savedEnrollment);
    }
    
    @Transactional(readOnly = true)
    public Optional<EnrollmentResponse> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .map(enrollmentMapper::toResponse);
    }
    
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getAllEnrollments() {
        return enrollmentRepository.findAll()
                .stream()
                .map(enrollmentMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsByStudentId(Long studentId) {
        return enrollmentRepository.findAll()
                .stream()
                .filter(enrollment -> enrollment.getStudent().getId().equals(studentId))
                .map(enrollmentMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.findAll()
                .stream()
                .filter(enrollment -> enrollment.getCourse().getId().equals(courseId))
                .map(enrollmentMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public void deleteEnrollment(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new RuntimeException("Enrollment not found");
        }
        enrollmentRepository.deleteById(id);
    }
    
}
