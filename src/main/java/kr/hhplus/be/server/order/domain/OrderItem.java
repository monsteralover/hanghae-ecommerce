package kr.hhplus.be.server.order.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import kr.hhplus.be.server.BaseEntity;
import kr.hhplus.be.server.product.domain.Product;
import lombok.*;

@Entity
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    @Min(1)
    private Integer quantity;

    @Column(nullable = false)
    @Getter
    private int unitPrice;

    public static OrderItem create(final Order order, final Product product, final Integer quantity) {
        return OrderItem.builder()
                .product(product)
                .order(order)
                .quantity(quantity)
                .unitPrice(product.getPrice() * quantity)
                .build();
    }

    public void setOrder(final Order order) {
        this.order = order;
    }
}
