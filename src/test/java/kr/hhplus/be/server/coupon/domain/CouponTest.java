package kr.hhplus.be.server.coupon.domain;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CouponTest {

    @Nested
    @DisplayName("쿠폰 발급 검증 테스트")
    class ValidateCouponIssueTest {
        private Coupon coupon;

        @BeforeEach
        void setUp() {
            coupon = new Coupon();
            ReflectionTestUtils.setField(coupon, "couponName", "Test Coupon");
            ReflectionTestUtils.setField(coupon, "discountAmount", 1000);
            ReflectionTestUtils.setField(coupon, "totalQuantity", 10);
            ReflectionTestUtils.setField(coupon, "remainingQuantity", 5);
            ReflectionTestUtils.setField(coupon, "expireDate", LocalDate.now().plusDays(7));
        }

        @Test
        @DisplayName("쿠폰이 만료되지 않았으면 정상 처리된다")
        void validateCouponIssueSuccess() {
            // when & then
            assertThatCode(() -> coupon.validateCouponIssue())
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("쿠폰이 만료되면 COUPON_EXPIRED 예외가 발생한다")
        void validateCouponIssueWhenExpiredThrowsException() {
            // given
            ReflectionTestUtils.setField(coupon, "expireDate", LocalDate.now().minusDays(1));

            // when & then
            assertThatThrownBy(() -> coupon.validateCouponIssue())
                    .isInstanceOf(ApiException.class)
                    .hasMessage(ApiResponseCodeMessage.COUPON_EXPIRED.getMessage());

        }

    }

    @Nested
    @DisplayName("쿠폰 수량 차감 테스트")
    class DeductQuantityTest {
        private Coupon coupon;

        @BeforeEach
        void setUp() {
            coupon = new Coupon();
            ReflectionTestUtils.setField(coupon, "remainingQuantity", 5);
        }

        @Test
        @DisplayName("쿠폰 수량이 정상적으로 차감된다")
        void deductQuantity_Success() {
            // when
            coupon.deductQuantity();

            // then
            assertThat(ReflectionTestUtils.getField(coupon, "remainingQuantity"))
                    .isEqualTo(4);
        }
    }

}
