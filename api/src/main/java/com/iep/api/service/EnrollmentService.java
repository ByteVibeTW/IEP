package com.iep.api.service;

import com.iep.api.dal.dto.EnrollmentDto;
import com.iep.api.dal.dto.UserInfoDto;
import com.iep.api.dal.entity.Enrollment;
import com.iep.api.dal.repository.EnrollmentRepository;
import com.iep.api.dal.mapper.EnrollmentMapper;
import com.iep.api.exception.CommonException;
import com.iep.api.exception.ErrorCode;
import com.iep.api.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {
    private final UserInfoService userInfoService;

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Transactional
    public EnrollmentDto createEnrollment(EnrollmentDto request) {
        Enrollment enrollment = enrollmentMapper.toEntity(request);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(savedEnrollment);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDto> getAllEnrollments() {
        return enrollmentRepository.findAll()
                .stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnrollmentDto> getCurrentUserEnrollments() {
        String currentUserSub = SecurityUtils.getCurrentUserSubOrThrow();
        UserInfoDto user = userInfoService.getUserBySub(currentUserSub)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));

        return getUserEnrollments(currentUserSub).stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getUserEnrollments(String sub) {
        return enrollmentRepository.findAll()
                .stream()
                .filter(enrollment -> enrollment.getStudent().getSub().equals(sub))
                .collect(Collectors.toList());
    }

}
