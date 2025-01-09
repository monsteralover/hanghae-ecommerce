package kr.hhplus.be.server.coupon.repository;

import kr.hhplus.be.server.coupon.domain.CouponIssue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponIssueRepositoryImpl implements CouponIssueRepository {
    private final CouponIssueJpaRepository couponIssueJpaRepository;

    @Override
    public List<CouponIssue> getUsableCouponsForUser(final Long userId) {
        return couponIssueJpaRepository.findAllByUserIdAndUsed(userId, false);
    }
}
