package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.coupon.domain.CouponIssue;
import kr.hhplus.be.server.coupon.repository.CouponIssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponIssueReadService {
    private final CouponIssueRepository couponIssueRepository;

    public List<CouponIssue> getUsableCouponsForUser(final Long userId) {
        return couponIssueRepository.getUsableCouponsForUser(userId).stream()
                .filter(issue -> {
                    final LocalDate expireDate = issue.getCoupon().getExpireDate();
                    return expireDate.equals(LocalDate.now()) || expireDate.isAfter(LocalDate.now());
                })
                .collect(Collectors.toList());
    }
}
