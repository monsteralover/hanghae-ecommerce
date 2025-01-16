package kr.hhplus.be.server.product.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.hhplus.be.server.ApiResponse;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.ProductStock;
import kr.hhplus.be.server.product.repository.ProductRepository;
import kr.hhplus.be.server.product.service.dto.ProductMostSoldResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private ProductController productController;

    @Autowired
    private ProductRepository productRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Product savedProduct1;
    private Product savedProduct2;
    private Product savedProduct3;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 생성
        Product product1 = Product.builder()
                .name("테스트 상품 1")
                .price(10000)
                .build();

        ProductStock stock1 = ProductStock.builder()
                .product(product1)
                .stockQuantity(100)
                .accumulatedSoldCount(50L)
                .build();
        product1.setProductStock(stock1);

        Product product2 = Product.builder()
                .name("테스트 상품 2")
                .price(20000)
                .build();

        ProductStock stock2 = ProductStock.builder()
                .product(product2)
                .stockQuantity(200)
                .accumulatedSoldCount(30L)
                .build();
        product2.setProductStock(stock2);

        Product product3 = Product.builder()
                .name("테스트 상품 3")
                .price(30000)
                .build();

        ProductStock stock3 = ProductStock.builder()
                .product(product3)
                .stockQuantity(300)
                .accumulatedSoldCount(70L)
                .build();
        product3.setProductStock(stock3);

        savedProduct1 = productRepository.save(product1);
        savedProduct2 = productRepository.save(product2);
        savedProduct3 = productRepository.save(product3);

    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("판매량 기준 상위 5개 상품을 조회한다")
    @Transactional
    void getBestProducts_returns_top_five_products() {
        // when
        ApiResponse<List<ProductMostSoldResponse>> response = productController.getBestProducts();

        // then
        List<ProductMostSoldResponse> products = response.getData();
        assertThat(products).hasSize(3); // 테스트 데이터가 3개이므로

        // 판매량 순으로 정렬되어 있는지 확인
        assertThat(products.get(0).accumulatedSoldCount()).isEqualTo(70L);
        assertThat(products.get(1).accumulatedSoldCount()).isEqualTo(50L);
        assertThat(products.get(2).accumulatedSoldCount()).isEqualTo(30L);
    }

}
