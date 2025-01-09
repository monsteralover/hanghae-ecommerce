package kr.hhplus.be.server.point.service;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.point.domain.Point;
import kr.hhplus.be.server.point.repository.PointRepository;
import kr.hhplus.be.server.point.service.dto.PointChargeServiceRequest;
import kr.hhplus.be.server.point.service.dto.PointResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointCommandServiceTest {
    @Mock
    PointRepository pointRepository;

    @InjectMocks
    PointCommandService pointCommandService;

    @DisplayName("point가 존재하지 않는 userId로 충전을 요청한 경우 새로운 point를 생성하고 PointResponse로 값을 반환한다.")
    @Test
    void chargeUserPointNewUserTest() {
        // given
        long userId = 1L;
        long amount = 10000L;
        BDDMockito.given(pointRepository.getByUserId(userId)).willReturn(Optional.empty());
        ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);
        BDDMockito.given(pointRepository.save(pointCaptor.capture()))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        final PointResponse pointResponse = pointCommandService.chargeUserPoint(userId,
                PointChargeServiceRequest.builder().amount(amount).build());
        // then
        assertThat(pointResponse.getUserPoint()).isEqualTo(amount);
        assertThat(pointResponse.getUserId()).isEqualTo(userId);

        Point savedPoint = pointCaptor.getValue();
        assertThat(savedPoint.getUserId()).isEqualTo(userId);
        assertThat(savedPoint.getPoint()).isEqualTo(amount);

        verify(pointRepository).getByUserId(userId);
        verify(pointRepository).save(any(Point.class));
    }

    @DisplayName("point가 존재하는 userId로 충전 시 기존의 point에 요청금액을 더하여 충전하고 PointResponse로 값을 반환한다.")
    @Test
    void chargeUserPointExistingUserTest() {
        // given
        long userId = 1L;
        long existingAmount = 10000L;
        long chargeAmount = 5000L;

        final Point point = Point.create(userId);
        point.charge(existingAmount);
        BDDMockito.given(pointRepository.getByUserId(userId)).willReturn(Optional.of(point));

        ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);
        BDDMockito.given(pointRepository.save(pointCaptor.capture()))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        final PointResponse pointResponse = pointCommandService.chargeUserPoint(userId,
                PointChargeServiceRequest.builder().amount(chargeAmount).build());
        // then
        assertThat(pointResponse.getUserPoint()).isEqualTo(chargeAmount + existingAmount);
        assertThat(pointResponse.getUserId()).isEqualTo(userId);

        Point savedPoint = pointCaptor.getValue();
        assertThat(savedPoint.getUserId()).isEqualTo(userId);
        assertThat(savedPoint.getPoint()).isEqualTo(chargeAmount + existingAmount);

        verify(pointRepository).getByUserId(userId);
        verify(pointRepository).save(any(Point.class));
    }


    @Test
    @DisplayName("포인트 사용시 잔액이 충분할 경우 포인트가 차감되어야 한다")
    void usePoint_ShouldDeductPoints_WhenSufficientBalance() {

        Long userId = 1L;
        Long initialPoints = 10000L;
        Long deductAmount = 5000L;

        Point point = Point.builder()
                .userId(userId)
                .point(initialPoints)
                .build();

        when(pointRepository.getByUserId(userId)).thenReturn(Optional.of(point));


        pointCommandService.usePoint(userId, deductAmount);


        assertEquals(5000L, point.getPoint());
        verify(pointRepository).save(point);
    }

    @Test
    @DisplayName("포인트 사용시 잔액이 부족할 경우 LACK_OF_BALANCE 예외가 발생해야 한다")
    void usePoint_ShouldThrowException_WhenInsufficientBalance() {
        // given
        Long userId = 1L;
        Long initialPoints = 1000L;
        Long deductAmount = 2000L;

        Point point = Point.builder()
                .userId(userId)
                .point(initialPoints)
                .build();

        when(pointRepository.getByUserId(userId)).thenReturn(Optional.of(point));

        //then
        assertThatThrownBy(() -> pointCommandService.usePoint(userId, deductAmount))
                .isInstanceOf(ApiException.class)
                .hasMessage(ApiResponseCodeMessage.LACK_OF_BALANCE.getMessage());
    }


}
