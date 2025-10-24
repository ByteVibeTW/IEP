package com.iep.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{
    private final JwtConverter jwtConverter;
    private final UserInfoInitializationFilter userInfoInitializationFilter;

    public SecurityConfig(JwtConverter jwtConverter, UserInfoInitializationFilter userInfoInitializationFilter) {
        this.jwtConverter = jwtConverter;
        this.userInfoInitializationFilter = userInfoInitializationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

        security.csrf(AbstractHttpConfigurer::disable);
        security.authorizeHttpRequests(authorize ->
        {
            authorize.anyRequest().permitAll();
        });

        security.formLogin(AbstractHttpConfigurer::disable);
        security.httpBasic(AbstractHttpConfigurer::disable);
        security.oauth2ResourceServer(oauth2 -> oauth2.jwt(
                jwt -> jwt.jwtAuthenticationConverter(jwtConverter)
        ));
        security.sessionManagement(session -> session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
        ));
        
        // 添加用戶資訊初始化過濾器
        // 這個過濾器會在 OAuth2 資源服務器認證之後執行
        security.addFilterAfter(userInfoInitializationFilter, BasicAuthenticationFilter.class);

        return security.build();
    }
}