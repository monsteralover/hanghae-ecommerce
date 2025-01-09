package kr.hhplus.be.server.order.controller;

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

    @PostMapping("/{userId}")
    public ApiResponse<OrderResponse> orderProducts(@PathVariable Long userId,
                                                    @RequestBody OrderRequest orderRequest) {

        return ApiResponse.ok(orderFacade.order(orderRequest.toFacadeRequest(userId)));
    }

}
