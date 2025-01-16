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

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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

    @Test
    @DisplayName("쿠폰 사용이 정상적으로 동작한다")
    void useCoupon_Success() {
        // given
        final Long userId = 1L;
        final Long couponId = 1L;
        final int discountAmount = 10000;

        final User user = createUser();
        final Coupon coupon = createCoupon(discountAmount);
        ReflectionTestUtils.setField(coupon, "expireDate", LocalDate.now().plusDays(30));
        final CouponIssue couponIssue = createCouponIssue(user, coupon);

        given(couponIssueRepository.getById(couponId)).willReturn(couponIssue);

        // when
        final int result = couponIssueCommandService.useCoupon(userId, couponId);

        // then
        assertThat(result).isEqualTo(discountAmount);
        assertThat(couponIssue.isUsed()).isTrue();
        verify(couponIssueRepository).save(couponIssue);
    }

    @Test
    @DisplayName("다른 사용자의 쿠폰을 사용하려고 하면 예외가 발생한다")
    void useCoupon_WithDifferentUser_ThrowsException() {
        // given
        final Long ownerId = 1L;
        final Long differentUserId = 2L;
        final Long couponId = 1L;

        final User owner = createUser();
        final Coupon coupon = createCoupon(10000);
        final CouponIssue couponIssue = createCouponIssue(owner, coupon);

        given(couponIssueRepository.getById(couponId)).willReturn(couponIssue);

        // when & then
        assertThatThrownBy(() -> couponIssueCommandService.useCoupon(differentUserId, couponId))
                .isInstanceOf(ApiException.class)
                .hasMessage(ApiResponseCodeMessage.INVALID_CHARGE_AMOUNT.getMessage());

        assertThat(couponIssue.isUsed()).isFalse();
        verify(couponIssueRepository, never()).save(any());
    }

    private CouponIssue createCouponIssue(User user, Coupon coupon) {
        final CouponIssue couponIssue = CouponIssue.create(user, coupon);
        ReflectionTestUtils.setField(couponIssue, "id", 1L);
        return couponIssue;
    }

    private Coupon createCoupon(int discountAmount) {
        final Coupon coupon = new Coupon();
        ReflectionTestUtils.setField(coupon, "id", 1L);
        ReflectionTestUtils.setField(coupon, "discountAmount", discountAmount);
        return coupon;
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
