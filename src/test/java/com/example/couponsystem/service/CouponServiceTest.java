package com.example.couponsystem.service;

import com.example.couponsystem.Service.CouponService;
import com.example.couponsystem.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
