package com.example.practice.security;

import com.example.practice.entity.UserEntity;
import com.example.practice.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    public OAuthUserServiceImpl() {
        super();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            log.info("OAuth2User attributes {} ", new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        OAuth2UserInfo oAuth2UserInfo = null;

        String provider = userRequest.getClientRegistration().getRegistrationId();

        if(provider.equals("kakao")) {
            log.info("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo( (Map)oAuth2User.getAttributes() );
        } else if(provider.equals("naver")) {
            log.info("네이버 로그인 요청");
            oAuth2UserInfo = new NaverUserInfo( (Map)oAuth2User.getAttributes().get("response") );
        }

        final String providerId = oAuth2UserInfo.getProviderId();
        final String loginId = provider + "_" + providerId;
        final String username = oAuth2UserInfo.getName();

        UserEntity userEntity = null;

        if (!userRepository.existsByUsername(username)) {
            userEntity = UserEntity.builder().username(username).authProvider(providerId).build();
            userEntity = userRepository.save(userEntity);
        } else {
            userEntity = userRepository.findByUsername(username);
        }

        log.info("Successfully pulled user info username {} authProvider {}", username , providerId);

        return new ApplicationOAuth2User(userEntity.getId(), oAuth2User.getAttributes());
    }

}
