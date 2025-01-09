package kr.hhplus.be.server.coupon.controller;

import kr.hhplus.be.server.ApiResponse;
import kr.hhplus.be.server.coupon.facade.CouponFacade;
import kr.hhplus.be.server.coupon.facade.CouponUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static kr.hhplus.be.server.ApiResponseCodeMessage.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    private final CouponFacade couponFacade;

    @GetMapping("/{userId}")
    public ApiResponse<List<CouponUserResponse>> getUsableCouponsForUser(@PathVariable Long userId) {
        return ApiResponse.ok(couponFacade.getUsableCouponsForUser(userId));
    }

    @PostMapping("/{userId}")
    public ApiResponse<CouponUserResponse> issueUserCoupon(@PathVariable Long userId,
                                                           @RequestParam Long couponId) {
        if (userId < 1) {
            return ApiResponse.badRequest(INVALID_USER.getCode(), INVALID_USER.getMessage());
        }
        if (couponId < 1) {
            return ApiResponse.badRequest(INVALID_CHARGE_AMOUNT.getCode(), INVALID_CHARGE_AMOUNT.getMessage());
        }
        if (couponId.equals(200L)) {
            return ApiResponse.conflict(OUT_OF_COUPON.getCode(), OUT_OF_COUPON.getMessage());
        }
        return ApiResponse.ok(new CouponUserResponse(couponId, "1000원 쿠폰", 1000, LocalDate.now().plusDays(1)));
    }
}
