package com.iep.api.service;

import com.iep.api.dal.entity.user.UserInfo;
import com.iep.api.dal.entity.user.UserRole;
import com.iep.api.dto.user.UserInfoDto;
import com.iep.api.dal.repository.UserInfoRepository;
import com.iep.api.dal.mapper.UserInfoMapper;
import com.iep.api.exception.CommonException;
import com.iep.api.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserInfoMapper userInfoMapper;

    @Transactional(readOnly = true)
    public List<UserInfoDto> getAllUsers() {
        return userInfoRepository.findAll().stream()
                .map(userInfoMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserInfoDto getUserById(Long id) {
        UserInfo userInfo = userInfoRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
        return userInfoMapper.toDto(userInfo);
    }

    @Transactional
    public void deleteBatch(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        userInfoRepository.deleteAllByIdInBatch(ids);
        log.info("批量刪除用戶: count={}", ids.size());

    }

    public Boolean isTeacher(Long userId) {
        UserInfo userInfo = userInfoRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
        return userInfo.getRole() == UserRole.TEACHER;
    }

    public UserInfo findById(Long id) {
        return userInfoRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));
    }

    public List<UserInfo> findAllByIds(List<Long> ids) {
        return userInfoRepository.findAllById(ids);
    }
}
