package com.iep.api.dal.mapper;

import com.iep.api.dal.entity.Course;
import com.iep.api.dto.CourseCreateRequest;
import com.iep.api.dto.CourseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    Course toEntity(CourseCreateRequest request);
    
    @Mapping(target = "teacherId", source = "teacher.id")
    @Mapping(target = "teacherName", source = "teacher.username")
    @Mapping(target = "teacherEmail", source = "teacher.email")
    CourseResponse toResponse(Course entity);
}
