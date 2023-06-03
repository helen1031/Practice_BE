package com.example.practice.controller;

import com.example.practice.dto.FlowerDTO;
import com.example.practice.dto.ResponseDTO;
import com.example.practice.entity.FlowerEntity;
import com.example.practice.service.FlowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("flower")
public class FlowerController {

    @Autowired
    private FlowerService flowerService;

    @PostMapping("/create")
    public ResponseEntity<?> createFlower(@AuthenticationPrincipal String userId, @RequestBody FlowerDTO dto) {
        try {
            //String temporaryUserId = "temporary-user"; // temporary user_id

            // 1. FlowerEntity 로 변환한다
            FlowerEntity entity = FlowerDTO.toEntity(dto);
            // 2. id를 null로 초기화 한다.
            entity.setId(null);
            // 3. user id를 설정해준다
            //entity.setUserId(temporaryUserId);
            entity.setUserId(userId);
            // 4. service를 이용해 FlowerEntity 생성
            List<FlowerEntity> entities = flowerService.create(entity);
            // 5. 자바 스트림을 이용해 리턴된 엔티티 리스트를 DTO 리스트로 변환
            List<FlowerDTO> dtos = entities.stream().map(FlowerDTO::new).collect(Collectors.toList());
            // 6. 변환된 DTO 리스트를 이용해 responseDTO를 초기화한다
            ResponseDTO<FlowerDTO> response = ResponseDTO.<FlowerDTO>builder().data(dtos).build();
            // 7. ResponseDTO를 리턴한다
            return ResponseEntity.ok().body(response);
        } catch(Exception e) {
            String error = e.getMessage();
            ResponseDTO<FlowerDTO> response = ResponseDTO.<FlowerDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveFlower(@AuthenticationPrincipal String userId) {
        //String temporaryUserId = "temporary-user";

        //List<FlowerEntity> entities = flowerService.retrieve(temporaryUserId);
        List<FlowerEntity> entities = flowerService.retrieve(userId);

        List<FlowerDTO> dtos = entities.stream().map(FlowerDTO::new).collect(Collectors.toList());

        ResponseDTO<FlowerDTO> response = ResponseDTO.<FlowerDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateFlower(@AuthenticationPrincipal String userId, @RequestBody FlowerDTO dto) {
        //String temporaryUserId = "temporary-user";

        FlowerEntity entity = FlowerDTO.toEntity(dto);

        //entity.setUserId(temporaryUserId);
        entity.setUserId(userId);

        List<FlowerEntity> entities = flowerService.update(entity);

        List<FlowerDTO> dtos = entities.stream().map(FlowerDTO::new).collect(Collectors.toList());

        ResponseDTO<FlowerDTO> response = ResponseDTO.<FlowerDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFlower(@AuthenticationPrincipal String userId, @RequestBody FlowerDTO dto) {
        try {
            //String temporaryUserId = "temporary-user";

            FlowerEntity entity = FlowerDTO.toEntity(dto);
            //entity.setUserId(temporaryUserId);
            entity.setUserId(userId);

            List<FlowerEntity> entities = flowerService.delete(entity);

            List<FlowerDTO> dtos = entities.stream().map(FlowerDTO::new).collect(Collectors.toList());
            ResponseDTO<FlowerDTO> response = ResponseDTO.<FlowerDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch(Exception e) {
            String error = e.getMessage();
            ResponseDTO<FlowerDTO> response = ResponseDTO.<FlowerDTO>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }
    }
}
