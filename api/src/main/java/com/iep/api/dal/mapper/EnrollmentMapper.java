package com.iep.api.dal.mapper;

import com.iep.api.dal.entity.Enrollment;
import com.iep.api.dto.enrollment.EnrollmentDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface EnrollmentMapper {
    Enrollment toEntity(EnrollmentDto enrollmentDto);

    EnrollmentDto toDto(Enrollment enrollment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Enrollment partialUpdate(EnrollmentDto enrollmentDto, @MappingTarget Enrollment enrollment);
}
