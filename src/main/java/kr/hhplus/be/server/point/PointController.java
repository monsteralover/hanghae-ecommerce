package kr.hhplus.be.server.point;

import kr.hhplus.be.server.ApiResponse;
import org.springframework.web.bind.annotation.*;

import static kr.hhplus.be.server.ApiResponseCodeMessage.INVALID_CHARGE_AMOUNT;
import static kr.hhplus.be.server.ApiResponseCodeMessage.INVALID_USER;

@RestController
@RequestMapping("/point")
public class PointController {

    @GetMapping("/{userId}")
    public ApiResponse<PointResponse> getUserPoint(@PathVariable Long userId) {
        if (userId < 1) {
            return ApiResponse.badRequest(INVALID_USER.getCode(), INVALID_USER.getMessage());
        }
        return ApiResponse.ok(new PointResponse(userId, 1000L));
    }

    @PostMapping("/{userId}/charge")
    public ApiResponse<PointResponse> chargeUserPoint(@PathVariable Long userId,
                                                      @RequestBody PointChargeRequest request) {
        if (request.getAmount() > 100000000 || request.getAmount() < 0) {
            return ApiResponse.badRequest(INVALID_CHARGE_AMOUNT.getCode(), INVALID_CHARGE_AMOUNT.getMessage());
        }
        return ApiResponse.ok(new PointResponse(userId, 1000L));
    }
}
