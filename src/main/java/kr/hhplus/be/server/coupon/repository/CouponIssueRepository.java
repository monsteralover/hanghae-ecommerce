package kr.hhplus.be.server.coupon.repository;

import kr.hhplus.be.server.coupon.domain.CouponIssue;

import java.util.List;

public interface CouponIssueRepository {
    List<CouponIssue> getUsableCouponsForUser(Long userId);

    CouponIssue save(CouponIssue couponIssue);
}
