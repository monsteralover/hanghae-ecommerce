package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.CouponIssue;
import kr.hhplus.be.server.coupon.repository.CouponIssueRepository;
import kr.hhplus.be.server.coupon.repository.CouponRepository;
import kr.hhplus.be.server.coupon.service.dto.CouponIssueResponse;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CouponIssueCommandServiceTest {

    @InjectMocks
    private CouponIssueCommandService couponIssueCommandService;

    @Mock
    private CouponIssueRepository couponIssueRepository;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("쿠폰 발급이 정상적으로 동작한다")
    void saveCouponIssue_Success() {
        // given
        final Long userId = 1L;
        final Long couponId = 1L;

        final User user = createUser();
        final Coupon coupon = createCoupon();
        final CouponIssue couponIssue = CouponIssue.create(user, coupon);

        given(userRepository.findUserById(userId)).willReturn(Optional.of(user));
        given(couponRepository.getCouponById(couponId)).willReturn(coupon);
        given(couponIssueRepository.save(any(CouponIssue.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        final CouponIssueResponse response = couponIssueCommandService.saveCouponIssue(userId, couponId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.couponId()).isEqualTo(couponId);
        assertThat(response.couponName()).isEqualTo(coupon.getCouponName());
    }

    @Test
    @DisplayName("존재하지 않는 사용자면 INVALID_USER 예외가 발생한다")
    void saveCouponIssue_WithInvalidUser_ThrowsException() {
        // given
        final Long userId = 999L;
        final Long couponId = 1L;

        given(userRepository.findUserById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> couponIssueCommandService.saveCouponIssue(userId, couponId))
                .isInstanceOf(ApiException.class)
                .hasMessage(ApiResponseCodeMessage.INVALID_USER.getMessage());
    }

    private User createUser() {
        final User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);
        return user;
    }

    private Coupon createCoupon() {
        final Coupon coupon = new Coupon();
        ReflectionTestUtils.setField(coupon, "id", 1L);
        return coupon;
    }
}
