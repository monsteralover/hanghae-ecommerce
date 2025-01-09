package kr.hhplus.be.server.order.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    private Long userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private long totalAmount;

    private Long couponId;

    public static Order create(Long userId, List<OrderItem> orderItems, long totalAmount, Long couponId) {
        return Order.builder()
                .userId(userId)
                .orderItems(orderItems)
                .couponId(couponId)
                .totalAmount(totalAmount)
                .build();
    }

    public void setOrderItems(final List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void setTotalAmount(final long totalAmount) {
        this.totalAmount = totalAmount;
    }
}
