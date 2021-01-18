package com.example.ex2.repository;

import com.example.ex2.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository<Entity type, @Id type>
// 인터페이스 상속만으로 CRUD, Paging 구현이 끝난다.
public interface MemoRepository extends JpaRepository<Memo, Long> {

    // 쿼리 메소드 정의 => 메소드 이름 자체가 질의문이 된다.
    // Memo 객체의 mno값이 70부터 80사이의 객체들을 구하고, mno 역순으로 정렬
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    // 쿼리메소드와 Pageable의 결합
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    // 쿼리메소드(deleteBy)
    void deleteMemoByMnoLessThan(Long num);
}
