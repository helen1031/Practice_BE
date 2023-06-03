package com.example.practice.service;

import com.example.practice.entity.FlowerEntity;
import com.example.practice.repository.FlowerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FlowerService {

    @Autowired
    private FlowerRepository repository;

    public List<FlowerEntity> create(final FlowerEntity entity) {
        validate(entity);

        repository.save(entity);
        log.info("FlowerEntity Id : {} is saved.", entity.getId());

        return repository.findByUserId(entity.getUserId());
    }

    public List<FlowerEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
    }

    public List<FlowerEntity> update(final FlowerEntity entity) {
        validate(entity);

        final Optional<FlowerEntity> original = repository.findById(entity.getId());
        original.ifPresent(flower -> {
            flower.setName(entity.getName());
            flower.setDescription(entity.getDescription());
            flower.setQuantity(entity.getQuantity());

            repository.save(flower);
        });

        return retrieve(entity.getUserId());
    }

    public List<FlowerEntity> delete(final FlowerEntity entity) {
        validate(entity);

        try {
            repository.delete(entity);
        } catch(Exception e) {
            log.error("error deleting entity ", entity.getId(), e);
            throw new RuntimeException("error deleting entity " + entity.getId());
        }

        return retrieve(entity.getUserId());
    }

    private void validate(final FlowerEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null");
        }

        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }
}
