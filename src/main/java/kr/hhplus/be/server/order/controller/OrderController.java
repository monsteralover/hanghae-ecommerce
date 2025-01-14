package kr.hhplus.be.server.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kr.hhplus.be.server.ApiResponse;
import kr.hhplus.be.server.order.facade.OrderFacade;
import kr.hhplus.be.server.order.facade.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderFacade orderFacade;

    @Operation(summary = "상품 주문", description = "사용자가 선택한 상품들을 주문합니다")
    @PostMapping("/{userId}")
    public ApiResponse<OrderResponse> orderProducts(@PathVariable Long userId,
                                                    @RequestBody @Valid OrderRequest orderRequest) {

        return ApiResponse.ok(orderFacade.order(orderRequest.toFacadeRequest(userId)));
    }

}
