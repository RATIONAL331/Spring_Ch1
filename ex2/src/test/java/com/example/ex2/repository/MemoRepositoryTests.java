package com.example.ex2.repository;

import com.example.ex2.entity.Memo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.List;
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


    // PAGING 처리
    // JPA 페이지 처리는 반드시 0부터 시작한다.
    @Test
    public void testPageDefault(){
        // org.springframework.data.domain.Page*...
        // 1페이지의 데이터 10개를 가져온다. page 0, size 10
        Pageable pageable = PageRequest.of(0, 10);
        // findAll에 Pageable타입의 파라미터를 전달하면 페이칭 처리에 관련된 쿼리들을 실행한다.
        // 이 결과들을 이용하여 Page객체로 저장한다.
        // 리턴타입이 Page이면 실제 페이지 처리에 필요한 전체 데이터의 개수를 가져오는 쿼리 역시 같이 처리한다.
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);
        /*
        Hibernate:
            select
                memo0_.mno as mno1_0_,
                memo0_.memo_text as memo_tex2_0_
            from
                tbl_memo memo0_ limit ? // 페이지 처리
        Hibernate:
            select
                count(memo0_.mno) as col_0_0_ // 전체 개수 처리
            from
                tbl_memo memo0_
        Page 1 of 10 containing com.example.ex2.entity.Memo instances
        */
        System.out.println("=======================================");
        System.out.println("Total Pages : " + result.getTotalPages()); // 총 페이지 수
        System.out.println("Total Count : " + result.getTotalElements()); // 전체 개수
        System.out.println("Page Number : " + result.getNumber()); // 현재 페이지 번호
        System.out.println("Page Size : " + result.getSize()); // 페이지당 데이터 개수
        System.out.println("has next Page? : " + result.hasNext()); // 다음 페이지 존재 여부
        System.out.println("fist page? : " + result.isFirst()); // 시작 페이지(0) 여부
        System.out.println("=======================================");
        for(Memo memo : result.getContent()){
            System.out.println(memo);
        }
    }
    @Test
    public void testSort(){
        // org.springframework.data.domain.Sort
        Sort sort1 = Sort.by("mno").descending();
        Pageable pageable = PageRequest.of(0, 10, sort1); // id 역순으로 10개(99~90)
        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }
    @Test
    public void testSort2(){
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);
        Pageable pageable = PageRequest.of(0, 10, sortAll);
        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }
    // Query Method
    @Test
    public void testQueryMethods(){
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);
        for(Memo memo : list){
            System.out.println(memo);
        }
    }
    // Query Method + Pageable
    @Test
    public void testQueryMethodWithPageable(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);
        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }

    // Query Method + Pageable
    @Test
    public void testQueryMethodWithPageable2(){
        Pageable pageable = PageRequest.of(1, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);
        result.get().forEach(memo -> {
            System.out.println(memo);
        });
    }
    // Query Method Delete
    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethod(){
        memoRepository.deleteMemoByMnoLessThan(10L);
    }
}
