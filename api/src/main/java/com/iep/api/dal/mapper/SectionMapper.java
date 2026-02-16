package com.iep.api.dal.mapper;

import com.iep.api.dto.section.SectionDto;
import com.iep.api.dal.entity.Section;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SectionMapper {
    Section toEntity(SectionDto sectionDto);

    SectionDto toDto(Section section);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Section partialUpdate(SectionDto sectionDto, @MappingTarget Section section);
}

