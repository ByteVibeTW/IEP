package com.iep.api.dal.mapper;

import com.iep.api.dal.entity.Enrollment;
import com.iep.api.dto.EnrollmentCreateRequest;
import com.iep.api.dto.EnrollmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EnrollmentMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    Enrollment toEntity(EnrollmentCreateRequest request);
    
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentName", source = "student.username")
    @Mapping(target = "studentEmail", source = "student.email")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseName", source = "course.name")
    EnrollmentResponse toResponse(Enrollment entity);
}
