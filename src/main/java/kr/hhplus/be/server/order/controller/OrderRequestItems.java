package kr.hhplus.be.server.order.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestItems {
    @JsonProperty("productId")
    private Long productId;
    @JsonProperty("quantity")
    private Integer quantity;
}
