package kr.hhplus.be.server.product.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.BaseEntity;
import lombok.Getter;

@Entity
public class ProductStock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Getter
    @NotNull
    private int stockQuantity;

    @NotNull
    private Long accumulatedSoldStock;
}
