package kr.hhplus.be.server.order.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotNull
    @Positive
    private long productId;

    @JsonProperty("quantity")
    @NotNull
    @Positive
    private int quantity;
}
