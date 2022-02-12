package com.todolist.demo.controller;

import com.todolist.demo.dto.ResponseDTO;
import com.todolist.demo.dto.TodoDTO;
import com.todolist.demo.model.TodoEntity;
import com.todolist.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {
    @Autowired
    private TodoService service;

    @GetMapping("/test")
    public ResponseEntity<?> testTodo() {
        String str = service.testService();
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        try {
            // TodoEntity 변환
            TodoEntity entity = TodoDTO.toEntity(dto);
            // id를 null로 초기화
            entity.setId(null);
            // 임시 유저 아이디를 설정
            entity.setUserId(userId);
            // 서비스를 이용해 Todo엔티티를 생성
            List<TodoEntity> entities = service.create(entity);
            // 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            // 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            // ResponseDTO를 리턴
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {
        // retrieve메서드를 사용해 Todo리스트를 가져온다
        List<TodoEntity> entities = service.retrieve(userId);
        // 스트림을 이용해 리턴된 엔티티 리스트를 TodoDTO리스트로 변환
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        // 변환된 TodoDTO리스트를 이용해ResponseDTO를 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        // ResponseDTO를 리턴
        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        // dto를 entity로 변환
        TodoEntity entity = TodoDTO.toEntity(dto);
        // id를 temporaryUserId로 초기화
        entity.setUserId(userId);
        // entity를 업데이트
        List<TodoEntity> entities = service.update(entity);
        // 리턴된 엔티티 리스트를 TodoDTO리스트로 변환
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        // 변환된 TodoDTO리스트를 이용해 ResponseDTO를 초기화
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
        try {
            // TodoEntity로 변환
            TodoEntity entity = TodoDTO.toEntity(dto);
            // 임시 유저 아이디를 설정
            entity.setUserId(userId);
            // entity를 삭제
            List<TodoEntity> entities = service.delete(entity);
            // 엔티티 리스트를 TodoDTO리스트로 변환
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            // 변환된 TodoDTO리스트로 ResponseDTO를 초기화
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response =  ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}