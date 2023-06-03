package com.example.practice.repository;

import com.example.practice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByUsername(String username);
    boolean existsByUsername(String username);
    UserEntity findByUsernameAndPassword(String username, String password);
}
