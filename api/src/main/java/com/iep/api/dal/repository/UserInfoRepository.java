package com.iep.api.dal.repository;

import com.iep.api.dal.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
