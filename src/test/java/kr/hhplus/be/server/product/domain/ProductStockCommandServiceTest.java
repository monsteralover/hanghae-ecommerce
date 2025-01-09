package kr.hhplus.be.server.product.domain;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.order.controller.OrderRequestItems;
import kr.hhplus.be.server.product.repository.ProductStockRepository;
import kr.hhplus.be.server.product.service.ProductStockCommandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductStockCommandServiceTest {
    @Mock
    private ProductStockRepository productStockRepository;

    @InjectMocks
    private ProductStockCommandService productStockCommandService;

    @Test
    @DisplayName("재고가 충분할 때 정상적으로 주문이 처리되어야 한다")
    void processSoldStock_WhenStockIsAvailable_ShouldUpdateStock() {
        // Given
        ProductStock productStock = ProductStock.builder()
                .stockQuantity(10)
                .accumulatedSoldCount(0)
                .build();

        OrderRequestItems orderItem = OrderRequestItems.builder()
                .productId(1L)
                .quantity(5)
                .build();

        List<OrderRequestItems> orderItems = Arrays.asList(orderItem);

        when(productStockRepository.getByProductId(1L)).thenReturn(productStock);

        // When
        productStockCommandService.processSoldStock(orderItems);

        // Then
        assertThat(productStock.getStockQuantity()).isEqualTo(5);
        assertThat(productStock.getAccumulatedSoldCount()).isEqualTo(5);
        verify(productStockRepository).save(productStock);
    }

    @Test
    @DisplayName("재고가 부족할 때 예외가 발생해야 한다")
    void processSoldStock_WhenOutOfStock_ShouldThrowException() {
        // Given
        ProductStock productStock = ProductStock.builder()
                .stockQuantity(3)
                .accumulatedSoldCount(0)
                .build();

        OrderRequestItems orderItem = OrderRequestItems.builder()
                .productId(1L)
                .quantity(5)
                .build();

        List<OrderRequestItems> orderItems = Arrays.asList(orderItem);

        when(productStockRepository.getByProductId(1L)).thenReturn(productStock);

        // When & Then
        assertThrows(ApiException.class, () -> productStockCommandService.processSoldStock(orderItems));
        assertThat(productStock.getStockQuantity()).isEqualTo(3);
        assertThat(productStock.getAccumulatedSoldCount()).isEqualTo(0);
        verify(productStockRepository, never()).save(any());
    }

}
