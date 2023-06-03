package com.example.practice.service;

import com.example.practice.entity.UserEntity;
import com.example.practice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        if (userEntity == null || userEntity.getUsername() == null) {
            throw new RuntimeException("Invalid arguments");
        }
        final String username = userEntity.getUsername();
        if (userRepository.existsByUsername(username)) {
            log.warn("Username already exists {}", username);
            throw new RuntimeException("Username already exists");
        }
    return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String username, final String password, final PasswordEncoder encoder) {
        final UserEntity originalUser = userRepository.findByUsername(username);

        if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }
        return null;
    }
}
