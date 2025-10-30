package com.iep.api.dal.mapper;

import com.iep.api.dal.dto.EnrollmentDto;
import com.iep.api.dal.entity.Enrollment;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EnrollmentMapper {
    @Mapping(source = "courseId", target = "course.id")
    @Mapping(source = "studentSub", target = "student.sub")
    Enrollment toEntity(EnrollmentDto enrollmentDto);

    @InheritInverseConfiguration(name = "toEntity")
    EnrollmentDto toDto(Enrollment enrollment);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Enrollment partialUpdate(EnrollmentDto enrollmentDto, @MappingTarget Enrollment enrollment);
}
