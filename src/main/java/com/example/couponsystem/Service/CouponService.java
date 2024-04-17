package com.example.couponsystem.Service;

import com.example.couponsystem.domain.Coupon;
import com.example.couponsystem.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponRepository repository;

    public CouponService(CouponRepository repository) {
        this.repository = repository;
    }

    public void issue(Long userId){
        long count = repository.count();

        if(count > 100){
            return;
        }

        repository.save(new Coupon(userId));
    }
}
