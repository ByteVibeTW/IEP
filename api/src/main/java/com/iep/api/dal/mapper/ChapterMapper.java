package com.iep.api.dal.mapper;

import com.iep.api.dal.entity.Chapter;
import com.iep.api.dto.ChapterCreateRequest;
import com.iep.api.dto.ChapterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ChapterMapper {
    
    @Mapping(target = "id", ignore = true)
    Chapter toEntity(ChapterCreateRequest request);
    
    ChapterResponse toResponse(Chapter entity);
}
