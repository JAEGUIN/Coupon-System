package com.example.couponsystem.Service;

import com.example.couponsystem.domain.Coupon;
import com.example.couponsystem.repository.CouponCountRepository;
import com.example.couponsystem.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    private final CouponCountRepository couponCountRepository;

    public CouponService(CouponRepository couponRepository, CouponCountRepository couponCountRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
    }


    public void issue(Long userId){

        //redis에는 incr명령어가 존재. 이 명령어는 key:value
        //incr coupona_count 를 한다면 integer가 1씩 증가시키고 그 값을 리턴하는 명령어다.

        //long count = couponRepository.count();
        Long count = couponCountRepository.increment();

        if(count > 100){
            return;
        }

        couponRepository.save(new Coupon(userId));
    }
}
