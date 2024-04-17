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

    /** 쿠폰 100개 생성테스트 케이스 (실패)
     *  실패원인
     *  레이스 컨디션 발생
     *  레이스 컨디션이란 두개 이상의 쓰레드가 공유 데이터에 접속하고 동시에 작업하려고 할때 문제발생!
     *
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
    */
}
