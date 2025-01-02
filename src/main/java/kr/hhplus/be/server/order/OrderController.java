package kr.hhplus.be.server.order;

import kr.hhplus.be.server.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kr.hhplus.be.server.ApiResponseCodeMessage.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @PostMapping("/{userId}")
    public ApiResponse<OrderResponse> orderProducts(@PathVariable Long userId,
                                                    @RequestBody List<OrderProductRequest> orderProductRequests) {
        if (userId < 1) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, INVALID_USER.getCode(), INVALID_USER.getMessage());
        }
        if (userId.equals(2L)) {
            return ApiResponse.of(HttpStatus.CONFLICT, LACK_OF_BALANCE.getCode(), LACK_OF_BALANCE.getMessage());
        }
        if (userId.equals(3L)) {
            return ApiResponse.of(HttpStatus.CONFLICT, OUT_OF_STOCK.getCode(), OUT_OF_STOCK.getMessage());
        }
        return ApiResponse.ok(new OrderResponse(100L));
    }

}
