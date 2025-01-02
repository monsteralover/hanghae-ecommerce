package kr.hhplus.be.server.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String productName;
    private Integer price;
    private Integer remainingStock;
}
