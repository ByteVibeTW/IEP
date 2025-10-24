package com.iep.api.dal.mapper;

import com.iep.api.dal.entity.Course;
import com.iep.api.dal.dto.CourseDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CourseMapper {
    @Mapping(source = "teacherRoleCode", target = "teacher.roleCode")
    @Mapping(source = "teacherSub", target = "teacher.sub")
    Course toEntity(CourseDto courseDto);

    @InheritInverseConfiguration(name = "toEntity")
    CourseDto toDto(Course course);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Course partialUpdate(CourseDto courseDto, @MappingTarget Course course);
}