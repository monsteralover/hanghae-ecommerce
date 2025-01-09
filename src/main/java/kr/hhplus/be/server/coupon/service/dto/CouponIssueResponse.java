package kr.hhplus.be.server.coupon.service.dto;

import kr.hhplus.be.server.coupon.domain.CouponIssue;

public record CouponIssueResponse(
        Long couponId,
        String couponName) {
    public static CouponIssueResponse from(CouponIssue couponIssue) {
        return new CouponIssueResponse(couponIssue.getCoupon().getId(), couponIssue.getCoupon().getCouponName());
    }
}
