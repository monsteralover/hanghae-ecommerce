package kr.hhplus.be.server.product.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductStock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Getter
    @NotNull
    private int stockQuantity;

    @NotNull
    @Getter
    private long accumulatedSoldCount;

    public void processSoldStock(final Integer quantity) {
        final int stockBalance = this.stockQuantity - quantity;
        if (stockBalance < 0) {
            throw new ApiException(ApiResponseCodeMessage.OUT_OF_STOCK);
        }
        this.stockQuantity = stockBalance;

        this.accumulatedSoldCount += quantity;
    }
}
