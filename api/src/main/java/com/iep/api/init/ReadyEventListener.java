package com.iep.api.init;

import com.iep.api.dal.dto.UserInfoDto;
import com.iep.api.dal.mapper.UserInfoMapper;
import com.iep.api.dal.repository.UserInfoRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
public class ReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    private final UserInfoMapper userInfoMapper;
    private final UserInfoRepository userInfoRepository;

    public ReadyEventListener(UserInfoMapper userInfoMapper, UserInfoRepository userInfoRepository) {
        this.userInfoMapper = userInfoMapper;
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {

        log.info("檢查初始化管理員帳號.");
        createDefaultOrganizationAndAdminUser();
    }

    /**
     * 建立預設組織與預設管理員帳號
     */
    public void createDefaultOrganizationAndAdminUser() {
        log.info("建立預設管理員帳號");
        if (!userInfoRepository.existsByUsername("AI Tutor")) {
            UserInfoDto userInfoDto = new UserInfoDto();
            userInfoDto.setSub(UUID.randomUUID().toString());
            userInfoDto.setEmail("ai@yang-lin.dev");
            userInfoDto.setUsername("AI Tutor");
            userInfoDto.setRoleCode("TEACHER");
            userInfoRepository.save(userInfoMapper.toEntity(userInfoDto));
        }

    }
}
