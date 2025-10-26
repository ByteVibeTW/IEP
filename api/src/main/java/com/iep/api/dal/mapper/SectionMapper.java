package com.iep.api.dal.mapper;

import com.iep.api.dal.dto.SectionDto;
import com.iep.api.dal.entity.Section;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SectionMapper {
    @Mapping(source = "courseId", target = "course.id")
    Section toEntity(SectionDto sectionDto);

    @Mapping(source = "course.id", target = "courseId")
    SectionDto toDto(Section section);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "courseId", target = "course.id")
    Section partialUpdate(SectionDto sectionDto, @MappingTarget Section section);
}

