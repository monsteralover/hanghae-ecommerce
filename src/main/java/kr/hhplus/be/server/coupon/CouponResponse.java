package kr.hhplus.be.server.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CouponResponse {
    private Long couponId;
    private String couponName;
    private Long discountAmount;
    private LocalDate couponExpireDate;
}
