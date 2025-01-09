package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponReadService {
    private final CouponRepository couponRepository;

}
