package kr.hhplus.be.server.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "사용 가능한 쿠폰 목록 조회", description = "사용자가 사용할 수 있는 쿠폰 목록을 조회합니다")
    @GetMapping("/{userId}")
    public ApiResponse<List<CouponUserResponse>> getUsableCouponsForUser(@Parameter(description = "사용자 ID") @PathVariable Long userId) {
        return ApiResponse.ok(couponFacade.getUsableCouponsForUser(userId));
    }

    @Operation(summary = "쿠폰 발급", description = "사용자에게 특정 쿠폰을 발급합니다")
    @PostMapping("/{userId}")
    public ApiResponse<CouponIssueResponse> issueUserCoupon(@Parameter(description = "사용자 ID") @PathVariable Long userId,
                                                            @Parameter(description = "발급할 쿠폰 ID") @RequestParam Long couponId
    ) {

        return ApiResponse.ok(couponFacade.issueCouponForUser(userId, couponId));
    }
}
