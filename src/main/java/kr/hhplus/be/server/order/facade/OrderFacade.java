package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.coupon.facade.OrderFacadeRequest;
import kr.hhplus.be.server.coupon.service.CouponIssueCommandService;
import kr.hhplus.be.server.order.service.dto.SaveOrderResponse;
import kr.hhplus.be.server.point.service.PointCommandService;
import kr.hhplus.be.server.product.service.ProductStockCommandService;
import kr.hhplus.be.server.user.service.UserReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final UserReadService userReadService;
    private final OrderItemCommandService orderItemCommandService;
    private final CouponIssueCommandService couponIssueCommandService;
    private final PointCommandService pointCommandService;

    private final ProductStockCommandService productStockCommandService;

    @Transactional
    public OrderResponse order(final OrderFacadeRequest request) {
        final long userId = request.getUserId();
        userReadService.checkUserExistsById(userId);
        //쿠폰사용
        final int discountAmount = couponIssueCommandService.useCoupon(userId, request.getCouponId());
        //주문 저장
        final SaveOrderResponse saveOrderResponse =
                orderItemCommandService.saveOrder(request.toOrderServiceRequest(discountAmount));
        //포인트사용
        pointCommandService.usePoint(userId, saveOrderResponse.getPaymentAmount());
        //재고차감
        productStockCommandService.processSoldStock(request.getOrderItems());

        return new OrderResponse(saveOrderResponse.getOrderId());
    }
}
