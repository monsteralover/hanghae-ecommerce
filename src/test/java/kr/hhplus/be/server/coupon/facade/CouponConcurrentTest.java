package kr.hhplus.be.server.coupon.facade;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.repository.CouponIssueRepository;
import kr.hhplus.be.server.coupon.repository.CouponRepository;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.transaction.TestTransaction;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("쿠폰 동시성 테스트")
@Import(TestcontainersConfiguration.class)
class CouponConcurrentTest {

    @Autowired
    private CouponFacade couponFacade;

    @Autowired
    private CouponIssueRepository couponIssueRepository;

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        couponIssueRepository.deleteAll();
        couponRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("선착순 쿠폰의 발급수와 실제 발급받은 유저수가 동일해야한다")
    void concurrentCouponIssue() throws InterruptedException {
        // Given
        final int totalQuantity = 10;
        final int numberOfThreads = 20;
        final List<Long> successfulIssues = new ArrayList<>();

        List<Long> userIds = new ArrayList<>();
        for (int i = 1; i <= numberOfThreads; i++) {
            User user = new User();
            User savedUser = userRepository.save(user);
            userIds.add(savedUser.getId());
        }

        Coupon coupon = createTestCoupon(totalQuantity);

        //유저 생성을 위해 커밋한다.
        TestTransaction.flagForCommit();
        TestTransaction.end();

        System.out.println("Created coupon with ID: " + coupon.getId() +
                " total quantity: " + totalQuantity +
                " remaining: " + coupon.getRemainingQuantity());
        System.out.println("Created " + userIds.size() + " test users");

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        // When
        for (int i = 0; i < numberOfThreads; i++) {
            final Long userId = userIds.get(i);
            executorService.submit(() -> {
                try {
                    couponFacade.issueCouponForUser(userId, coupon.getId());
                    synchronized (successfulIssues) {
                        successfulIssues.add(userId);
                        System.out.println("User " + userId + " successfully got a coupon");
                    }
                } catch (Exception e) {
                    System.out.println("User " + userId + " failed to get coupon: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
        Thread.sleep(500);

        // assertion을 위한 새 트랜잭션 시작
        TestTransaction.start();

        // Then
        long actualIssuedCount = couponIssueRepository.countByCouponId(coupon.getId());
        Coupon updatedCoupon = couponRepository.getCouponById(coupon.getId());

        System.out.println("Final issued count: " + actualIssuedCount);
        System.out.println("Successful issues: " + successfulIssues.size());
        System.out.println("Final remaining quantity: " + updatedCoupon.getRemainingQuantity());

        assertThat(actualIssuedCount)
                .as("발급된 쿠폰 수가 초기 수량과 일치해야 합니다")
                .isEqualTo(totalQuantity);

        assertThat(updatedCoupon.getRemainingQuantity())
                .as("남은 쿠폰 수량이 0이어야 합니다")
                .isZero();

        assertThat(successfulIssues)
                .as("성공적으로 발급된 횟수가 초기 수량과 일치해야 합니다")
                .hasSize(totalQuantity);
    }

    private Coupon createTestCoupon(int quantity) {
        Coupon coupon = Coupon.builder()
                .couponName("1천원 할인")
                .discountAmount(1000)
                .remainingQuantity(quantity)
                .expireDate(LocalDate.now().plusDays(7))
                .build();
        return couponRepository.save(coupon);
    }
}
