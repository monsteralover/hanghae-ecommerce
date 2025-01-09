package kr.hhplus.be.server.product.service;

import kr.hhplus.be.server.order.controller.OrderRequestItems;
import kr.hhplus.be.server.product.domain.ProductStock;
import kr.hhplus.be.server.product.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductStockCommandService {
    private final ProductStockRepository productStockRepository;

    public void resetAccumulatedSoldCountEveryThreeDays() {
        productStockRepository.resetAccumulatedSoldCountEveryThreeDays();
    }

    public void processSoldStock(final List<OrderRequestItems> orderItems) {
        orderItems.stream().forEach(item -> {
            final ProductStock productStock = productStockRepository.getByProductId(item.getProductId());
            productStock.processSoldStock(item.getQuantity());
            productStockRepository.save(productStock);
        });
    }
}
