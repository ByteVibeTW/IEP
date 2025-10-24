package com.iep.api.dal.mapper;

import com.iep.api.dal.entity.UserInfo;
import com.iep.api.dal.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "sub", source = "sub")
    UserResponse toResponse(UserInfo entity);
}
