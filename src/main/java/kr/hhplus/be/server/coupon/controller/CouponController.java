package kr.hhplus.be.server.coupon.controller;

import kr.hhplus.be.server.ApiResponse;
import kr.hhplus.be.server.coupon.facade.CouponFacade;
import kr.hhplus.be.server.coupon.service.dto.CouponIssueResponse;
import kr.hhplus.be.server.coupon.service.dto.CouponUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ApiResponse<CouponIssueResponse> issueUserCoupon(@PathVariable Long userId,
                                                            @RequestParam Long couponId) {

        return ApiResponse.ok(couponFacade.issueCouponForUser(userId, couponId));
    }
}
