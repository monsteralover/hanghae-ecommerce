package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static kr.hhplus.be.server.ApiResponseCodeMessage.*;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @GetMapping
    public ApiResponse<List<CouponResponse>> getCoupons() {
        return ApiResponse.ok(List.of
                (new CouponResponse(1L, "1000원 쿠폰", 1000L, LocalDate.now().plusDays(1)),
                        new CouponResponse(2L, "2000원 쿠폰", 2000L, LocalDate.now().plusDays(1))));
    }

    @PostMapping("/{userId}")
    public ApiResponse<CouponResponse> issueUserCoupon(@PathVariable Long userId,
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
        return ApiResponse.ok(new CouponResponse(couponId, "1000원 쿠폰", 1000L, LocalDate.now().plusDays(1)));
    }
}
