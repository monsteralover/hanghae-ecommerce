package kr.hhplus.be.server.point.service;

import kr.hhplus.be.server.point.domain.Point;
import kr.hhplus.be.server.point.repository.PointRepository;
import kr.hhplus.be.server.point.service.dto.PointResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PointReadServiceTest {
    @Mock
    PointRepository pointRepository;

    @InjectMocks
    PointReadService pointReadService;

    @DisplayName("point가 없는 사용자Id로 조회 시 충전액 0을 반환한다.")
    @Test
    void noPointUserTest() {
        // given
        long userId = 1L;
        BDDMockito.given(pointRepository.getByUserId(userId)).willReturn(Optional.empty());

        // when
        final PointResponse userPoint = pointReadService.getUserPoint(userId);

        // then
        assertThat(userPoint.getUserId()).isEqualTo(userId);
        assertThat(userPoint.getUserPoint()).isEqualTo(0l);
        verify(pointRepository).getByUserId(userId);

    }

    @DisplayName("point가 존재하는 사용자Id로 조회 시 사용자의 id에 해당하는 충전액을 반환한다.")
    @Test
    void existingPointUserTest() {
        // given
        long userId = 1L;
        long existingAmount = 10000L;

        final Point point = Point.create(userId);
        point.charge(existingAmount);

        BDDMockito.given(pointRepository.getByUserId(userId)).willReturn(Optional.of(point));

        // when
        final PointResponse userPoint = pointReadService.getUserPoint(userId);

        // then
        assertThat(userPoint.getUserId()).isEqualTo(userId);
        assertThat(userPoint.getUserPoint()).isEqualTo(existingAmount);
        verify(pointRepository).getByUserId(userId);

    }
}
