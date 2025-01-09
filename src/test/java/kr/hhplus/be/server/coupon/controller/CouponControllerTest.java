package kr.hhplus.be.server.coupon.controller;

import kr.hhplus.be.server.ApiResponse;
import kr.hhplus.be.server.coupon.facade.CouponFacade;
import kr.hhplus.be.server.coupon.service.dto.CouponIssueResponse;
import kr.hhplus.be.server.coupon.service.dto.CouponUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponControllerTest {

    @Mock
    private CouponFacade couponFacade;

    @InjectMocks
    private CouponController couponController;

    private static final Long USER_ID = 1L;
    private static final Long COUPON_ID = 100L;

    @Test
    @DisplayName("사용 가능한 쿠폰 목록을 정상적으로 조회한다")
    void getUsableCouponsForUser_ShouldReturnCoupons() {
        // Arrange
        List<CouponUserResponse> expectedCoupons = Arrays.asList(
                new CouponUserResponse(1L, "1천원 할인", 1000, LocalDate.now().plusDays(7)),
                new CouponUserResponse(2L, "5천원 할인", 5000, LocalDate.now().plusDays(14))
        );

        when(couponFacade.getUsableCouponsForUser(USER_ID))
                .thenReturn(expectedCoupons);

        // Act
        ApiResponse<List<CouponUserResponse>> response =
                couponController.getUsableCouponsForUser(USER_ID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(expectedCoupons, response.getData());

        verify(couponFacade).getUsableCouponsForUser(USER_ID);
    }

    @Test
    @DisplayName("사용자에게 쿠폰을 정상적으로 발급한다")
    void issueUserCoupon_ShouldIssueCoupon() {
        // Arrange
        CouponIssueResponse expectedResponse =
                new CouponIssueResponse(COUPON_ID, "1천원 할인 쿠폰");

        when(couponFacade.issueCouponForUser(USER_ID, COUPON_ID))
                .thenReturn(expectedResponse);

        // Act
        ApiResponse<CouponIssueResponse> response =
                couponController.issueUserCoupon(USER_ID, COUPON_ID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(expectedResponse, response.getData());

        verify(couponFacade).issueCouponForUser(USER_ID, COUPON_ID);
    }

}
