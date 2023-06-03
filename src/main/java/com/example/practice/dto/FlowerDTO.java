package com.example.practice.dto;

import com.example.practice.entity.FlowerEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FlowerDTO {
    private String id;
    private String userId;
    private String name;
    private String description;
    private int quantity;

    public FlowerDTO(final FlowerEntity flowerEntity) {
        this.id = flowerEntity.getId();
        this.userId = flowerEntity.getUserId();
        this.name = flowerEntity.getName();
        this.description = flowerEntity.getDescription();
        this.quantity = flowerEntity.getQuantity();
    }

    public static FlowerEntity toEntity(final FlowerDTO dto) {
        return FlowerEntity.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .name(dto.getName())
                .description(dto.getDescription())
                .quantity(dto.getQuantity())
                .build();
    }
}
