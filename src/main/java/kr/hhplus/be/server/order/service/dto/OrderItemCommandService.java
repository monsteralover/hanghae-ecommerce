package kr.hhplus.be.server.order.service.dto;

import kr.hhplus.be.server.order.controller.OrderRequestItems;
import kr.hhplus.be.server.order.domain.Order;
import kr.hhplus.be.server.order.repository.OrderRepository;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemCommandService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public SaveOrderResponse saveOrder(final OrderServiceRequest request) {
        final Set<Long> productIds =
                request.getOrderItems().stream()
                        .map(OrderRequestItems::getProductId)
                        .collect(Collectors.toSet());

        final List<Product> products = productRepository.getAllById(productIds);
        final Order order = Order.create(request, products);

        final Long savedOrderId = orderRepository.save(order);
        return createSaveOrderResponse(savedOrderId, order.getTotalAmount());
    }

    private SaveOrderResponse createSaveOrderResponse(Long orderId, long paymentAmount) {
        return SaveOrderResponse.builder()
                .orderId(orderId)
                .paymentAmount(paymentAmount)
                .build();
    }
}
