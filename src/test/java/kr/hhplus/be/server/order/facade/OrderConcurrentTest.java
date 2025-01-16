package kr.hhplus.be.server.order.facade;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.coupon.repository.CouponIssueRepository;
import kr.hhplus.be.server.coupon.repository.CouponRepository;
import kr.hhplus.be.server.order.controller.OrderRequest;
import kr.hhplus.be.server.order.controller.OrderRequestItems;
import kr.hhplus.be.server.order.repository.OrderRepository;
import kr.hhplus.be.server.point.domain.Point;
import kr.hhplus.be.server.point.repository.PointRepository;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.domain.ProductStock;
import kr.hhplus.be.server.product.repository.ProductRepository;
import kr.hhplus.be.server.product.repository.ProductStockRepository;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("주문 동시성 테스트")
@Import(TestcontainersConfiguration.class)
class OrderConcurrentTest {
    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductStockRepository productStockRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponIssueRepository couponIssueRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private OrderRepository orderRepository;
    private long savedUserId;
    private long savedProductId;
    private long savedCouponIssueId;

    @AfterEach
    void tearDown() {
        savedUserId = 0;
        savedProductId = 0;
        savedCouponIssueId = 0;

        productStockRepository.deleteAll();
        couponIssueRepository.deleteAll();

        productRepository.deleteAll();
        couponRepository.deleteAll();
        pointRepository.deleteAll();
        orderRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("동시에 여러 주문이 들어올 경우, 상품 재고가 부족하면 OUT_OF_STOCK 예외가 발생하고 주문이 처리되지 않는다.")
    @Test
    @Transactional
    void stockConcurrentExceptionTest() throws InterruptedException {
        // given
        int totalStockQuantity = 100;
        int quantityPerOrder = 20;
        long point = 1000000L;
        final int numberOfThreads = 10;

        setupTestData(totalStockQuantity, point);

        Product product = productRepository.getById(savedProductId);
        List<Future<?>> futures = new ArrayList<>();

        TestTransaction.flagForCommit();
        TestTransaction.end();

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger exceptionCount = new AtomicInteger(0);

        // when
        for (int i = 1; i <= numberOfThreads; i++) {
            futures.add(executorService.submit(() -> {
                try {
                    orderFacade.order(createOrderRequest(product.getId(), quantityPerOrder, null)
                            .toFacadeRequest(savedUserId));
                } catch (ApiException e) {
                    if (e.getMessage().equals(ApiResponseCodeMessage.OUT_OF_STOCK.getMessage())) {
                        exceptionCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            }));
        }

        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        TestTransaction.start();

        // then
        assertThat(exceptionCount.get()).isGreaterThan(0);
    }

    @DisplayName("동시에 여러 주문이 들어올 경우, 상품 재고가 충분하면 주문이 처리된다.")
    @Test
    @Transactional
    void stockConcurrentTest() throws InterruptedException {
        // given
        int totalStockQuantity = 100;
        int quantityPerOrder = 5;
        long point = 1000000L;
        final int numberOfThreads = 10;

        setupTestData(totalStockQuantity, point);

        Product product = productRepository.getById(savedProductId);
        List<Future<?>> futures = new ArrayList<>();

        TestTransaction.flagForCommit();
        TestTransaction.end();

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        AtomicInteger successCount = new AtomicInteger(0);

        // when
        for (int i = 1; i <= numberOfThreads; i++) {
            futures.add(executorService.submit(() -> {
                try {
                    orderFacade.order(createOrderRequest(product.getId(), quantityPerOrder, null)
                            .toFacadeRequest(savedUserId));
                    successCount.incrementAndGet();
                } catch (ApiException e) {

                } finally {
                    latch.countDown();
                }
            }));
        }

        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        TestTransaction.start();

        // then
        assertThat(numberOfThreads).isEqualTo(successCount.get());
        assertThat(orderRepository.findAllByUserId(savedUserId).size()).isEqualTo(numberOfThreads);
    }


    private OrderRequest createOrderRequest(Long productId, Integer quantity, Long couponId) {
        OrderRequestItems item = OrderRequestItems.builder()
                .productId(productId).quantity(quantity).build();

        return OrderRequest.builder()
                .orderItems(Collections.singletonList(item)).couponId(couponId).build();
    }

    private void setupTestData(int stockQuantity, long userPoint) {
        User user = new User();
        userRepository.save(user);
        savedUserId = user.getId();

        Product product = Product.builder()
                .name("Test Product")
                .price(1000)
                .build();
        productRepository.save(product);
        savedProductId = product.getId();

        ProductStock productStock = ProductStock.builder()
                .product(product)
                .stockQuantity(stockQuantity)
                .accumulatedSoldCount(10)
                .build();
        productStockRepository.save(productStock);

        Point point = Point.builder()
                .userId(user.getId())
                .point(userPoint)
                .build();
        pointRepository.save(point);
    }

}
