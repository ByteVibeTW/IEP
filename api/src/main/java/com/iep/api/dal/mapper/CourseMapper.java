package com.iep.api.dal.mapper;

import com.iep.api.dal.entity.Course;
import com.iep.api.dto.course.CourseReq;
import com.iep.api.dto.course.CourseResp;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {
    Course toEntity(CourseReq courseReq);

    CourseResp toDto(Course course);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Course partialUpdate(CourseReq courseReq, @MappingTarget Course course);
}