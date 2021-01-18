package com.example.ex2.repository;

import com.example.ex2.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<Entity type, @Id type>
// 인터페이스 상속만으로 구현이 끝난다.
public interface MemoRepository extends JpaRepository<Memo, Long> {
}
