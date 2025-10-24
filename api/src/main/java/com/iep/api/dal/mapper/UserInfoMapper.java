package com.iep.api.dal.mapper;

import com.iep.api.dal.dto.UserInfoDto;
import com.iep.api.dal.entity.UserInfo;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserInfoMapper {
    UserInfo toEntity(UserInfoDto userInfoDto);

    UserInfoDto toDto(UserInfo userInfo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserInfo partialUpdate(UserInfoDto userInfoDto, @MappingTarget UserInfo userInfo);
}