package kr.hhplus.be.server.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.CouponIssue;
import kr.hhplus.be.server.coupon.repository.CouponIssueRepository;
import kr.hhplus.be.server.coupon.repository.CouponRepository;
import kr.hhplus.be.server.order.domain.Order;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Import({TestcontainersConfiguration.class, ObjectMapper.class})
public class OrderControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

        setupTestData();
    }

    @AfterEach
    void tearDown() {
        productStockRepository.deleteAll();
        couponIssueRepository.deleteAll();

        productRepository.deleteAll();
        couponRepository.deleteAll();
        pointRepository.deleteAll();
        orderRepository.deleteAll();
        userRepository.deleteAll();
    }

    private Long savedUserId;
    private Long savedProductId;
    private Long savedCouponIssueId;

    @Transactional
    @Test
    @DisplayName("쿠폰과 주문 시 결제 금액만큼 포인트에서 차감되고 쿠폰은 사용처리가 되고 재고도 차감된다.")
    void orderProductsSuccess() throws Exception {
        // Given
        User user = userRepository.findUserById(savedUserId).orElseThrow();
        Product product = productRepository.getById(savedProductId);
        CouponIssue couponIssue = couponIssueRepository.getById(savedCouponIssueId);

        OrderRequest orderRequest = createOrderRequest(product.getId(), 1, couponIssue.getCoupon().getId());

        // When & Then
        mockMvc.perform(post("/order/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orderId").isNumber())
                .andDo(print());

        ProductStock updatedStock = productStockRepository.getByProductId(product.getId());
        assertEquals(9, updatedStock.getStockQuantity());

        Point updatedPoint = pointRepository.getByUserId(user.getId()).orElseThrow();
        assertTrue(updatedPoint.getPoint() < 100000);

        CouponIssue updatedCoupon = couponIssueRepository.getById(couponIssue.getId());
        assertTrue(updatedCoupon.isUsed());

        Order savedOrder = orderRepository.findLatestOrderByUserId(user.getId());
        assertNotNull(savedOrder);
        assertEquals(1, savedOrder.getOrderItems().size());
    }

    private void setupTestData() {
        User user = new User();
        userRepository.save(user);
        savedUserId = user.getId();

        Product product = Product.builder()
                .name("Test Product")
                .price(10000)
                .build();
        productRepository.save(product);
        savedProductId = product.getId();

        ProductStock productStock = ProductStock.builder()
                .product(product)
                .stockQuantity(10)
                .accumulatedSoldCount(10)
                .build();
        productStockRepository.save(productStock);

        Coupon coupon = Coupon.builder()
                .expireDate(LocalDate.now().plusDays(30))
                .couponName("Test Coupon")
                .discountAmount(1000)
                .totalQuantity(100)
                .remainingQuantity(50)
                .build();
        couponRepository.save(coupon);

        CouponIssue couponIssue = CouponIssue.create(user, coupon);
        couponIssueRepository.save(couponIssue);
        savedCouponIssueId = couponIssue.getId();

        Point point = Point.builder()
                .userId(user.getId())
                .point(100000L)
                .build();
        pointRepository.save(point);
    }

    private OrderRequest createOrderRequest(Long productId, Integer quantity, Long couponId) {
        OrderRequestItems item = OrderRequestItems.builder()
                .productId(productId).quantity(quantity).build();

        return OrderRequest.builder()
                .orderItems(Collections.singletonList(item)).couponId(couponId).build();
    }
}
