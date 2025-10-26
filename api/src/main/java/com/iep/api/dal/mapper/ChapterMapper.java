package com.iep.api.dal.mapper;

import com.iep.api.dal.dto.ChapterDto;
import com.iep.api.dal.entity.Chapter;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChapterMapper {
    @Mapping(source = "sectionId", target = "section.id")
    Chapter toEntity(ChapterDto chapterDto);

    @Mapping(source = "section.id", target = "sectionId")
    ChapterDto toDto(Chapter chapter);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "sectionId", target = "section.id")
    Chapter partialUpdate(ChapterDto chapterDto, @MappingTarget Chapter chapter);
}

