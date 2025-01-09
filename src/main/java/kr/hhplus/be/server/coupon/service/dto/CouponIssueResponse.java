package kr.hhplus.be.server.coupon.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.coupon.domain.CouponIssue;

@Schema(description = "쿠폰 발급 응답")
public record CouponIssueResponse(
        @Schema(description = "발급된 쿠폰 ID")
        Long couponId,
        @Schema(description = "발급된 쿠폰명")
        String couponName) {
    public static CouponIssueResponse from(CouponIssue couponIssue) {
        return new CouponIssueResponse(couponIssue.getCoupon().getId(), couponIssue.getCoupon().getCouponName());
    }
}
