package kr.hhplus.be.server.product.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.BaseEntity;
import lombok.*;

@Entity
@Getter
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int price;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ProductStock productStock;

}
