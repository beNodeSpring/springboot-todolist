package com.todolist.demo.persistence;

import com.todolist.demo.model.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
//    기본쿼리를 사용할 경우에는 아래처럼 작성 할 필요 없음
//    @Query("select * from Todo t where t.userId = ?1")
//    List<TodoEntity> findByUserId(String userId);
}
