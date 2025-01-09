package kr.hhplus.be.server.product.domain;


import kr.hhplus.be.server.product.repository.ProductRepository;
import kr.hhplus.be.server.product.service.PaginationVerification;
import kr.hhplus.be.server.product.service.ProductReadService;
import kr.hhplus.be.server.product.service.dto.ProductMostSoldResponse;
import kr.hhplus.be.server.product.service.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductReadServiceTest {
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductReadService productReadService;

    @DisplayName("페이지 요청 시 상품아이디, 상품명, 가격, 수량의 정보를 가진 리스트를 반환한다.")
    @Test
    void getProductsTest() {
        // given
        int page = 1;
        int size = 5;
        final Pageable pageable = PaginationVerification.toPageable(page, size);
        final List<Product> products = createProducts();
        final Page<Product> pagedProducts = new PageImpl<>(products, pageable, products.size());
        given(productRepository.getProductsPagination(pageable)).willReturn(pagedProducts);
        // when
        final List<ProductResponse> productResponses = productReadService.getProducts(page, size);
        // then
        assertThat(productResponses).hasSize(5);
        assertThat(productResponses.get(0)).extracting("productId", "productName", "price", "remainingStock")
                .containsExactly(1L, "Product 1", 10000, 10);
    }

    @DisplayName("페이지 요청 시 상품이 없다면 빈 리스트를 반환한다")
    @Test
    void emptyProductsTest() {
        // given
        int page = 1;
        int size = 5;
        final Pageable pageable = PaginationVerification.toPageable(page, size);
        final List<Product> products = List.of();
        final Page<Product> pagedProducts = new PageImpl<>(products, pageable, products.size());
        given(productRepository.getProductsPagination(pageable)).willReturn(pagedProducts);
        // when
        final List<ProductResponse> productResponses = productReadService.getProducts(page, size);
        // then
        assertThat(productResponses).isEmpty();
    }

    @DisplayName("상위상품을 조회 시 가장 많이 팔린 5가지 상품을 반환한다.")
    @Test
    void test() {
        final List<Product> products = createProducts();

        given(productRepository.getTopFiveProducts()).willReturn(products);

        // when
        List<ProductMostSoldResponse> results = productReadService.getMostSoldProducts();

        // then
        assertThat(results).hasSize(5);
        assertThat(results.get(0).accumulatedSoldCount()).isEqualTo(100L);
        assertThat(results.get(4).accumulatedSoldCount()).isEqualTo(1L);

        verify(productRepository).getTopFiveProducts();
    }

    private List<Product> createProducts() {
        return Arrays.asList(
                Product.builder()
                        .id(1L)
                        .name("Product 1")
                        .price(10000)
                        .productStock(ProductStock.builder()
                                .stockQuantity(10)
                                .accumulatedSoldCount(100L)
                                .build())
                        .build(),
                Product.builder()
                        .id(2L)
                        .name("Product 2")
                        .price(20000)
                        .productStock(ProductStock.builder()
                                .stockQuantity(10)
                                .accumulatedSoldCount(90L)
                                .build())
                        .build(),
                Product.builder()
                        .id(3L)
                        .name("Product 3")
                        .price(20000)
                        .productStock(ProductStock.builder()
                                .stockQuantity(10)
                                .accumulatedSoldCount(20L)
                                .build())
                        .build(),
                Product.builder()
                        .id(4L)
                        .name("Product 4")
                        .price(20000)
                        .productStock(ProductStock.builder()
                                .stockQuantity(10)
                                .accumulatedSoldCount(10L)
                                .build())
                        .build(),
                Product.builder()
                        .id(5L)
                        .name("Product 5")
                        .price(20000)
                        .productStock(ProductStock.builder()
                                .stockQuantity(10)
                                .accumulatedSoldCount(1L)
                                .build())
                        .build()
        );
    }
}
