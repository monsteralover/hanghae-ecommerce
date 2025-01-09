package kr.hhplus.be.server.coupon.service.dto;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.CouponIssue;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record CouponUserResponse(
        Long couponId,
        String couponName,
        Integer discountAmount,
        LocalDate couponExpireDate) {

    public static List<CouponUserResponse> from(List<CouponIssue> couponIssues) {
        return couponIssues.stream().map(couponIssue -> {
            final Coupon coupon = couponIssue.getCoupon();
            return new CouponUserResponse(coupon.getId(), coupon.getCouponName(), coupon.getDiscountAmount(),
                    coupon.getExpireDate());
        }).collect(Collectors.toList());

    }
}
