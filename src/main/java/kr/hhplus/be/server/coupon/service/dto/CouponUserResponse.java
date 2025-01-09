package kr.hhplus.be.server.coupon.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.CouponIssue;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "사용자 쿠폰 조회 응답")
public record CouponUserResponse(
        @Schema(description = "쿠폰 ID")
        Long couponId,
        @Schema(description = "쿠폰명")
        String couponName,
        @Schema(description = "할인 금액")
        Integer discountAmount,
        @Schema(description = "쿠폰 만료일")
        LocalDate couponExpireDate) {

    public static List<CouponUserResponse> from(List<CouponIssue> couponIssues) {
        return couponIssues.stream().map(couponIssue -> {
            final Coupon coupon = couponIssue.getCoupon();
            return new CouponUserResponse(coupon.getId(), coupon.getCouponName(), coupon.getDiscountAmount(),
                    coupon.getExpireDate());
        }).collect(Collectors.toList());

    }
}
