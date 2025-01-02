package kr.hhplus.be.server.balance;

import kr.hhplus.be.server.ApiResponse;
import org.springframework.web.bind.annotation.*;

import static kr.hhplus.be.server.ApiResponseCodeMessage.INVALID_CHARGE_AMOUNT;
import static kr.hhplus.be.server.ApiResponseCodeMessage.INVALID_USER;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @GetMapping("/{userId}")
    public ApiResponse<BalanceResponse> getUserBalance(@PathVariable Long userId) {
        if (userId < 1) {
            return ApiResponse.badRequest(INVALID_USER.getCode(), INVALID_USER.getMessage());
        }
        return ApiResponse.ok(new BalanceResponse(userId, 1000L));
    }

    @PostMapping("/{userId}/charge")
    public ApiResponse<BalanceResponse> chargeUserBalance(@PathVariable Long userId,
                                                          @RequestBody BalanceChargeRequest request) {
        if (request.getAmount() > 100000000 || request.getAmount() < 0) {
            return ApiResponse.badRequest(INVALID_CHARGE_AMOUNT.getCode(), INVALID_CHARGE_AMOUNT.getMessage());
        }
        return ApiResponse.ok(new BalanceResponse(userId, 1000L));
    }
}
