package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.CouponIssue;
import kr.hhplus.be.server.coupon.repository.CouponIssueRepository;
import kr.hhplus.be.server.coupon.service.dto.CouponUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CouponIssueReadServiceTest {

    @InjectMocks
    private CouponIssueReadService couponIssueReadService;

    @Mock
    private CouponIssueRepository couponIssueRepository;

    @Test
    @DisplayName("사용자의 사용 가능한 쿠폰 목록을 조회한다")
    void getUsableCouponsForUser() {
        // given
        final Long userId = 1L;
        final LocalDate today = LocalDate.now();

        final Coupon validCoupon1 = createCoupon("Valid Coupon 1", today);
        final Coupon validCoupon2 = createCoupon("Valid Coupon 2", today.plusDays(5));
        final Coupon expiredCoupon = createCoupon("Expired Coupon", today.minusDays(1));

        final List<CouponIssue> couponIssues = List.of(
                createCouponIssue(validCoupon1),
                createCouponIssue(validCoupon2),
                createCouponIssue(expiredCoupon)
        );

        given(couponIssueRepository.getUsableCouponsForUser(userId)).willReturn(couponIssues);

        // when
        final List<CouponUserResponse> result = couponIssueReadService.getUsableCouponsForUser(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("couponName")
                .containsExactlyInAnyOrder("Valid Coupon 1", "Valid Coupon 2");
    }

    private Coupon createCoupon(String name, LocalDate expireDate) {
        final Coupon coupon = new Coupon();
        ReflectionTestUtils.setField(coupon, "couponName", name);
        ReflectionTestUtils.setField(coupon, "expireDate", expireDate);
        return coupon;
    }

    private CouponIssue createCouponIssue(Coupon coupon) {
        final CouponIssue couponIssue = new CouponIssue();
        ReflectionTestUtils.setField(couponIssue, "coupon", coupon);
        return couponIssue;
    }
}
