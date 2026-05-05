package com.iep.api.init;

import com.iep.api.dal.entity.user.UserInfo;
import com.iep.api.dal.entity.user.UserRole;
import com.iep.api.dal.repository.UserInfoRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    private final UserInfoRepository userInfoRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.enabled:false}")
    private boolean seedEnabled;

    @Override
    @Transactional
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        log.info("Application is ready. Performing initialization tasks...");

        if (!seedEnabled) {
            log.info("Seed disabled. Set app.seed.enabled=true to initialize demo data.");
            return;
        }

        log.info("開始初始化假資料（若已存在則跳過建立）...");

        log.info("檢查初始化帳號.");
        UserInfo admin = createUser("bigred", "bigred", "bigred@yang-lin.dev", "bigred", UserRole.ADMIN);
        log.info("✓ 創建管理員: {}", admin.getUsername());
        UserInfo aiTutor = createUser("AI Tutor", "ai", "ai@yang-lin.dev", "AI Tutor", UserRole.TEACHER);
        log.info("✓ 創建AI Tutor: {}", aiTutor.getUsername());
        UserInfo teacher = createUser("Teacher", "teacher", "teacher@yang-lin.dev", "Teacher", UserRole.TEACHER);
        log.info("✓ 創建老師: {}", teacher.getUsername());
    }

    private UserInfo createUser(String username, String password, String email, String nickname,
            UserRole role) {
        // 檢查用戶是否已存在
        var existing = userInfoRepository.findByUsername(username);
        if (existing.isPresent()) {
            UserInfo user = existing.get();
            if (!StringUtils.hasText(user.getNickname()) && StringUtils.hasText(nickname)) {
                user.setNickname(nickname);
                user = userInfoRepository.save(user);
                log.info("用戶 {} 已存在，已補上 nickname", username);
            } else {
                log.info("用戶 {} 已存在，跳過創建", username);
            }
            return user;
        }

        UserInfo user = new UserInfo();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setNickname(nickname);
        user.setRole(role);
        return userInfoRepository.save(user);
    }
}
