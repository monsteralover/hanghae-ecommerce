package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.order.controller.OrderRequestItems;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.domain.OrderItem;
import kr.hhplus.be.server.order.repository.OrderRepository;
import kr.hhplus.be.server.order.service.dto.OrderServiceRequest;
import kr.hhplus.be.server.order.service.dto.SaveOrderResponse;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class OrderItemCommandService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public SaveOrderResponse saveOrder(final OrderServiceRequest request) {
        final Order order = Order.create(request.getUserId(), new ArrayList<>(), 0L, request.getCouponId());

        final AtomicLong totalPrice = new AtomicLong(0L);
        final List<OrderItem> orderItems = createOrderItems(request.getOrderItems(), order, totalPrice);

        final long paymentAmount = calculatePaymentAmount(totalPrice.get(), request.getDiscountAmount());
        order.setTotalAmount(paymentAmount);
        order.setOrderItems(orderItems);

        final Long savedOrderId = orderRepository.save(order);
        return createSaveOrderResponse(savedOrderId, paymentAmount);
    }

    private List<OrderItem> createOrderItems(List<OrderRequestItems> orderRequestItems, Order order,
                                             AtomicLong totalPrice) {
        return orderRequestItems.stream()
                .map(item -> {
                    final Product product = productRepository.getById(item.getProductId());
                    final OrderItem orderItem = OrderItem.create(order, product, item.getQuantity());
                    totalPrice.addAndGet(orderItem.getUnitPrice());
                    return orderItem;
                })
                .toList();
    }

    private long calculatePaymentAmount(long totalPrice, int discountAmount) {
        final long totalPayment = totalPrice - discountAmount;
        if (totalPayment < 0) {
            throw new ApiException(ApiResponseCodeMessage.INVALID_PAYMENT_AMOUNT);
        }
        return totalPayment;
    }

    private SaveOrderResponse createSaveOrderResponse(Long orderId, long paymentAmount) {
        return SaveOrderResponse.builder()
                .orderId(orderId)
                .paymentAmount(paymentAmount)
                .build();
    }
}
