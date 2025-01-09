package kr.hhplus.be.server.product.service;

import kr.hhplus.be.server.product.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductStockCommandService {
    private final ProductStockRepository productStockRepository;

    public void resetAccumulatedSoldCountEveryThreeDays() {
        productStockRepository.resetAccumulatedSoldCountEveryThreeDays();
    }
}
