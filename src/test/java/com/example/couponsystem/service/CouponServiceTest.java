package com.example.couponsystem.service;

import com.example.couponsystem.Service.CouponService;
import com.example.couponsystem.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CouponServiceTest {

    @Autowired
    private CouponService service;

    @Autowired
    private CouponRepository repository;

    @Test
    public void onceIssue(){
        service.issue(1L);

        long count = repository.count();

        assertThat(count).isEqualTo(1);
    }

    /** 쿠폰 100개 생성테스트 케이스 (해결)
     *  실패원인
     *  레이스 컨디션 발생
     *  레이스 컨디션이란 두개 이상의 쓰레드가 공유 데이터에 접속하고 동시에 작업하려고 할때 문제발생!
     *  redis로 key value 방식으로 해결!
     *  redis는 싱글 쓰레드 기반으로 동작
     *  쓰레드 1에서 6시에 쿠폰 카운트를 증가 시키는 명령어를 실행하고 6시 2분에 완료 된다고 하면
     *  쓰레드 2에서 6시 1분에 쿠폰 쿠폰 카운트를 증가시키는 명령어를 실행하면
     *  쓰레드 1의 작업이 끝날때까지 기다렸다가 6시 2분에 작업을 시작함.
     *  그래서 모든 쓰레드에서는 언제나 최신값을 가져갈 수 있어서 중복이 사라짐.
     */
    @Test
    public void multipleIssue() throws InterruptedException {
        int threadCount = 1000;
        int coreCount = Runtime.getRuntime().availableProcessors();

        ExecutorService executorService = Executors.newWorkStealingPool(coreCount);
        //CountDownLatch 는 다른 thread에서 수행하는 작업을 기다리게 도와주는 클래스
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i=0; i<threadCount; i++){
            long userId = i;
            executorService.submit(()->{
                try {
                    service.issue(userId);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long count = repository.count();

        assertThat(count).isEqualTo(100);
    }

}
