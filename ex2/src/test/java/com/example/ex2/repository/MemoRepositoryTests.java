package com.example.ex2.repository;

import com.example.ex2.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {
    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        // Proxy 객체가 나옴을 확인할 수 있다.
        System.out.println(memoRepository.getClass().getName());
    }
    // INSERT
    @Test
    public void testInsertDummies(){
        IntStream.rangeClosed(1, 100).forEach(i->{
            Memo memo = Memo.builder().memoText("Sample " + i).build();
            memoRepository.save(memo);
        });
    }
    // SELECT findById
    @Test
    public void testSelect(){
        Long mno = 100L;
        Optional<Memo> result = memoRepository.findById(mno);
        // SQL 처리 이후에 실행된다.
        System.out.println("====================================");
        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo);
        }
    }
    // SELECT getOne
    @Transactional
    @Test
    public void testSelect2(){
        Long mno = 100L;
        Memo memo = memoRepository.getOne(mno); // 실체 객체가 필요한 순간까지 SQL을 실행하지 않는다.
        System.out.println("====================================");
        System.out.println(memo); // 실체 객체가 필요한 순간 SQL이 동작한다.
    }
    // UPDATE
    @Test
    public void testUpdate(){
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
        System.out.println(memoRepository.save(memo));
        // 내부적으로 select로 객체를 확인한 후, update한다. 만약에 없다면 insert가 수행된다.
    }
    // DELETE
    @Test
    public void testDelete(){
        Long mno = 100L;
        memoRepository.deleteById(mno);
    }
}
