package com.iep.api.dal.mapper;

import com.iep.api.dal.dto.SectionsDto;
import com.iep.api.dal.entity.Sections;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SectionsMapper {
    @Mapping(source = "courseId", target = "course.id")
    Sections toEntity(SectionsDto sectionsDto);

    @Mapping(source = "course.id", target = "courseId")
    SectionsDto toDto(Sections sections);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "courseId", target = "course.id")
    Sections partialUpdate(SectionsDto sectionsDto, @MappingTarget Sections sections);
}