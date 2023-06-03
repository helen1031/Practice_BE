package com.example.practice.security;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getName();
}
