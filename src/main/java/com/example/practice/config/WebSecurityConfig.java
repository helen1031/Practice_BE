package com.example.practice.config;

import com.example.practice.security.JwtAuthenticationFilter;
import com.example.practice.security.OAuthSuccessHandler;
import com.example.practice.security.OAuthUserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private OAuthUserServiceImpl oAuthUserService;

    @Autowired
    private OAuthSuccessHandler oAuthSuccessHandler;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers("/", "/auth/**", "/oauth2/**").permitAll()
                .anyRequest().authenticated() // /와 /auth/** 이외의 모든 경로는 인증해야함
                .and()
                .oauth2Login()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/**")
                .and()
                .authorizationEndpoint()
                .baseUri("/auth/authorize")
                .and()
                .userInfoEndpoint()
                .userService(oAuthUserService)
                .and()
                .successHandler(oAuthSuccessHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint());
        ;

        // filter 등록
        // 매 요청마다 CorsFilter 실행 후 jwtAuthenticationFilter 실행
        http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);

        return http.build();
    }
}
