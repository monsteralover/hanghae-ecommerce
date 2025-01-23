package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CouponCommandServiceTest {
    @InjectMocks
    private CouponCommandService couponCommandService;

    @Mock
    private CouponRepository couponRepository;

    private Coupon coupon;

    @BeforeEach
    void setUp() {
        coupon = new Coupon();
        ReflectionTestUtils.setField(coupon, "id", 1L);
        ReflectionTestUtils.setField(coupon, "couponName", "Test Coupon");
        ReflectionTestUtils.setField(coupon, "discountAmount", 1000);
        ReflectionTestUtils.setField(coupon, "totalQuantity", 10);
        ReflectionTestUtils.setField(coupon, "remainingQuantity", 5);
        ReflectionTestUtils.setField(coupon, "expireDate", LocalDate.now().plusDays(7));
    }

    @Test
    @DisplayName("쿠폰 수량 차감이 정상적으로 동작한다")
    void deductQuantityWithLock_Success() {
        // given
        given(couponRepository.getCouponById(1L)).willReturn(coupon);
        willAnswer(invocation -> invocation.getArgument(0)).given(couponRepository).save(any(Coupon.class));

        // when
        couponCommandService.deductQuantity(1L);

        // then
        verify(couponRepository).getCouponById(1L);
        verify(couponRepository).save(coupon);
    }


}
