package com.todolist.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Builder
@NoArgsConstructor // 매개변수 없는 생성자
@AllArgsConstructor
@Data
@Entity
@Table(name = "Todo") // 매핑 할 테이블 이름 지정
public class TodoEntity { // Todo테이블에 상응하는 클래스
    // 아래 멤버변수들이 곧 테이블 칼럼
    @Id // 기본키가 될 필드
    @GeneratedValue(generator = "system-uuid") // Id를 system-uuid로 자동으로 생성
    @GenericGenerator(name = "system-uuid", strategy = "uuid") // system-uuid는 uuid를 사용해서 생성
    private  String id;
    private String userId;
    private String title;
    private boolean done;
}
