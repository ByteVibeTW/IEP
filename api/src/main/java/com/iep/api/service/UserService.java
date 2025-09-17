package com.iep.api.service;

import com.iep.api.dal.entity.UserInfo;
import com.iep.api.dal.repository.UserInfoRepository;
import com.iep.api.dto.UserCreateRequest;
import com.iep.api.dto.UserResponse;
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
public class UserService {
    
    private final UserInfoRepository userInfoRepository;
    private final UserMapper userMapper;
    
    public UserResponse createUser(UserCreateRequest request) {
        if (userInfoRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userInfoRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        UserInfo user = userMapper.toEntity(request);
        UserInfo savedUser = userInfoRepository.save(user);
        return userMapper.toResponse(savedUser);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(Long id) {
        return userInfoRepository.findById(id)
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
    
    public UserResponse updateUser(Long id, UserCreateRequest request) {
        UserInfo user = userInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRoleCode(request.getRoleCode());
        
        UserInfo updatedUser = userInfoRepository.save(user);
        return convertToResponse(updatedUser);
    }
    
    public void deleteUser(Long id) {
        if (!userInfoRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userInfoRepository.deleteById(id);
    }
    
    private UserResponse convertToResponse(UserInfo user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setRoleCode(user.getRoleCode());
        return response;
    }
}
