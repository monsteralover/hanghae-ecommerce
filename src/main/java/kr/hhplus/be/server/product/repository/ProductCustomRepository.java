package kr.hhplus.be.server.product.repository;

import kr.hhplus.be.server.product.domain.Product;

import java.util.List;

public interface ProductCustomRepository {
    List<Product> getTopFiveProducts();

}
