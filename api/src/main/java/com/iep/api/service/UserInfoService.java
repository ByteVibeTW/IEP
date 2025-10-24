package com.iep.api.service;

import com.iep.api.dal.dto.UserInfoDto;
import com.iep.api.dal.repository.UserInfoRepository;
import com.iep.api.dal.mapper.UserInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserInfoService {
    
    private final UserInfoRepository userInfoRepository;
    private final UserInfoMapper userInfoMapper;
    
    @Transactional(readOnly = true)
    public Optional<UserInfoDto> getUserBySub(String sub) {
        return userInfoRepository.findById(sub)
                .map(userInfoMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserInfoDto> getUserByEmail(String email) {
        return userInfoRepository.findByEmail(email)
                .map(userInfoMapper::toDto);
    }
    
    @Transactional(readOnly = true)
    public List<UserInfoDto> getAllUsers() {
        return userInfoRepository.findAll()
                .stream()
                .map(userInfoMapper::toDto)
                .collect(Collectors.toList());
    }
    
    public void deleteUser(String sub) {
        if (!userInfoRepository.existsById(sub)) {
            throw new RuntimeException("User not found");
        }
        userInfoRepository.deleteById(sub);
    }
}
