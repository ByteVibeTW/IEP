package com.iep.api.service;

import com.iep.api.dal.entity.Enrollment;
import com.iep.api.dal.repository.EnrollmentRepository;
import com.iep.api.dal.mapper.EnrollmentMapper;
import com.iep.api.dto.enrollment.EnrollmentDto;
import com.iep.api.exception.CommonException;
import com.iep.api.exception.ErrorCode;
import com.iep.api.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Transactional
    public EnrollmentDto createEnrollment(EnrollmentDto request) {
        Enrollment enrollment = enrollmentMapper.toEntity(request);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(savedEnrollment);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDto> getCurrentUserEnrollments() {
        Long currentUserId = getCurrentUserId();
        return enrollmentRepository.findByStudentId(currentUserId)
                .stream()
                .map(enrollmentMapper::toDto)
                .toList();
    }

    private Long getCurrentUserId() {
        return JwtUtils.getCurrentUserId()
                .orElseThrow(() -> new CommonException(ErrorCode.UNDEFINED));
    }
}
