package kr.hhplus.be.server.order.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.BaseEntity;
import kr.hhplus.be.server.order.controller.OrderRequestItems;
import kr.hhplus.be.server.order.service.dto.OrderServiceRequest;
import kr.hhplus.be.server.product.domain.Product;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Entity
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    private Long userId;

    @OneToMany(mappedBy = "order")
    @Getter
    private List<OrderItem> orderItems;

    @Getter
    private long totalAmount;

    private Long couponId;

    public static Order create(final OrderServiceRequest request, final List<Product> products) {
        final Order order = Order.builder()
                .userId(request.getUserId())
                .couponId(request.getCouponId())
                .build();

        order.initializeOrderItems(request, products);
        return order;
    }

    private void initializeOrderItems(final OrderServiceRequest request, final List<Product> products) {
        final AtomicLong totalPrice = new AtomicLong(0L);
        final Map<Long, Integer> productIdAndQuantity = getProductIdAndQuantityMap(request);

        final List<OrderItem> orderItems = getOrderItems(products, this, totalPrice, productIdAndQuantity);
        final long totalAmount = calculatePaymentAmount(totalPrice.get(), request.getDiscountAmount());

        this.orderItems = orderItems;
        this.totalAmount = totalAmount;
    }

    private Map<Long, Integer> getProductIdAndQuantityMap(final OrderServiceRequest request) {
        return request.getOrderItems().stream().collect(Collectors
                .toMap(OrderRequestItems::getProductId, OrderRequestItems::getQuantity));
    }

    private List<OrderItem> getOrderItems(final List<Product> products, final Order order,
                                          final AtomicLong totalPrice,
                                          final Map<Long, Integer> productIdAndQuantity) {
        return products.stream().map(product -> {
            final OrderItem orderItem = OrderItem.create(order, product, productIdAndQuantity.get(product.getId()));
            totalPrice.addAndGet(orderItem.getUnitPrice());
            return orderItem;
        }).toList();
    }

    private long calculatePaymentAmount(long totalPrice, int discountAmount) {
        final long totalPayment = totalPrice - discountAmount;
        if (totalPayment < 0) {
            throw new ApiException(ApiResponseCodeMessage.INVALID_PAYMENT_AMOUNT);
        }
        return totalPayment;
    }

}
