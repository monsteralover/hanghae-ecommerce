package kr.hhplus.be.server.point.api;

import kr.hhplus.be.server.ApiResponse;
import kr.hhplus.be.server.point.domain.Point;
import kr.hhplus.be.server.point.repository.PointRepository;
import kr.hhplus.be.server.point.service.dto.PointResponse;
import kr.hhplus.be.server.user.service.UserReadService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Transactional
class PointControllerTest {

    @Autowired
    private PointController pointController;

    @Autowired
    private PointRepository pointRepository;

    @MockBean
    private UserReadService userReadService;

    private Long testUserId;

    @BeforeEach
    void setUp() {
        testUserId = 1L;
        doNothing().when(userReadService).checkUserExistsById(testUserId);

    }

    @AfterEach
    void tearDown() {
        pointRepository.deleteAll();
    }


    @Test
    @DisplayName("포인트가 없는 사용자의 포인트 조회시 0을 반환한다")
    void getUserPoint_WhenUserHasNoPoint_ShouldReturnZeroPoint() {
        // When
        ApiResponse<PointResponse> response = pointController.getUserPoint(testUserId);

        // Then
        assertNotNull(response);

        PointResponse pointResponse = response.getData();
        assertNotNull(pointResponse);
        assertEquals(testUserId, pointResponse.getUserId());
        assertEquals(0L, pointResponse.getUserPoint());
    }

    @Test
    @DisplayName("여러번 포인트 충전시 포인트가 누적된다")
    void multipleCharges_ShouldAccumulatePoints() {
        // Given
        Long userId = 10L;
        doNothing().when(userReadService).checkUserExistsById(userId);

        PointChargeRequest firstCharge = new PointChargeRequest(5000L);
        PointChargeRequest secondCharge = new PointChargeRequest(3000L);

        // When
        pointController.chargeUserPoint(userId, firstCharge);
        ApiResponse<PointResponse> response = pointController.chargeUserPoint(userId, secondCharge);

        // Then
        assertNotNull(response);
        PointResponse pointResponse = response.getData();
        assertEquals(8000L, pointResponse.getUserPoint());

        Point savedPoint = pointRepository.getByUserId(userId).orElse(null);
        assertNotNull(savedPoint);
        assertEquals(8000L, savedPoint.getPoint());
    }
}
