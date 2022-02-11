package com.todolist.demo.service;

import com.todolist.demo.model.TodoEntity;
import com.todolist.demo.persistence.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {
    @Autowired
    private TodoRepository repository;

    public String testService() {
        TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
        repository.save(entity);
        TodoEntity savedEntity = repository.findById(entity.getId()).get();
        return savedEntity.getTitle();
    }

    public List<TodoEntity> create(final TodoEntity entity) {
        // 검증
        validate(entity);
        // 저장 & 로그
        repository.save(entity);
        log.info("Entity Id : {} is saved.", entity.getId());
        // 저장된 엔터티를 포함한 신규 리스트 반환
        return repository.findByUserId(entity.getUserId());
    }

    public void validate(final TodoEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }
        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

    public List<TodoEntity> retrieve(final String userId) {
        return repository.findByUserId(userId);
    }

    public List<TodoEntity> update(final TodoEntity entity) {
        // 검증
        validate(entity);
        // 넘겨받은 엔티티 id를 이용해 TodoEntity를 가져온다.(존재하지 않는 엔티티를 업데이트 하는 상황 방지)
        final Optional<TodoEntity> original = repository.findById(entity.getId());

        // 1. lambda 방식
        // 반환된 TodoEntity가 존재하면 값을 새 entity의 값으로 덮어 씌워 DB에 저장
        original.ifPresent(todo -> {
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            repository.save(todo);
        });

        /* 2. 기존 방식
        if(original.isPresent()) {
            final TodoEntity todo = original.get();
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            repository.save(todo);
        }
        */

        // 유저의 모든 Todo 리스트를 리턴
        return retrieve(entity.getUserId());
    }

    public List<TodoEntity> delete(final TodoEntity entity) {
        // 검증
        validate(entity);
        try {
            // 엔티티를 삭제
            repository.delete(entity);
        } catch(Exception e) {
            // id와 exception을 로깅
            log.error("error deleting entity ", entity.getId(), e);
            // 컨트롤러로 exception을 전달
            throw new RuntimeException("error deleting entity " + entity.getId());
        }
        // 새 Todo리스트를 가져와 리턴
        return retrieve(entity.getUserId());
    }
}
