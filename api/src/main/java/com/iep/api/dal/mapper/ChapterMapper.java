package com.iep.api.dal.mapper;

import com.iep.api.dto.chapter.ChapterDto;
import com.iep.api.dal.entity.Chapter;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChapterMapper {
    Chapter toEntity(ChapterDto chapterDto);

    ChapterDto toDto(Chapter chapter);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Chapter partialUpdate(ChapterDto chapterDto, @MappingTarget Chapter chapter);
}

