package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.coupon.domain.CouponIssue;
import kr.hhplus.be.server.coupon.repository.CouponIssueRepository;
import kr.hhplus.be.server.coupon.service.dto.CouponUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponIssueReadService {
    private final CouponIssueRepository couponIssueRepository;

    public List<CouponUserResponse> getUsableCouponsForUser(final Long userId) {
        final List<CouponIssue> couponIssues = couponIssueRepository.getUsableCouponsForUser(userId).stream()
                .filter(issue -> issue.getCoupon().isCouponExpired())
                .collect(Collectors.toList());
        return CouponUserResponse.from(couponIssues);
    }

}
