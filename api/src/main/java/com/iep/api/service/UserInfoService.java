package com.iep.api.service;

import com.iep.api.dal.repository.UserInfoRepository;
import com.iep.api.dal.dto.UserResponse;
import com.iep.api.dal.mapper.UserMapper;
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
    private final UserMapper userMapper;
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserBySub(String sub) {
        return userInfoRepository.findById(sub)
                .map(userMapper::toResponse);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserByEmail(String email) {
        return userInfoRepository.findByEmail(email)
                .map(userMapper::toResponse);
    }
    
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userInfoRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }
    
    public void deleteUser(String sub) {
        if (!userInfoRepository.existsById(sub)) {
            throw new RuntimeException("User not found");
        }
        userInfoRepository.deleteById(sub);
    }
}
