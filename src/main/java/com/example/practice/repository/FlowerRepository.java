package com.example.practice.repository;

import com.example.practice.entity.FlowerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowerRepository extends JpaRepository<FlowerEntity, String> {
    List<FlowerEntity> findByUserId(String userId);
}