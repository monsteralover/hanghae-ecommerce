package kr.hhplus.be.server.point.domain;

import kr.hhplus.be.server.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class PointTest {

    @Test
    @DisplayName("point 생성 시 입력받은 userId와 포인트가 0인 포인트 객체를 생성한다.")
    void create() {
        long userId = 1L;
        final Point point = Point.create(userId);
        assertThat(point.getUserId()).isEqualTo(userId);
        assertThat(point.getPoint()).isEqualTo(0L);
    }

    @Test
    @DisplayName("입력 받은 금액만큼 point를 증가시킨다.")
    void charge() {
        long userId = 1L;
        long amount = 2000L;
        final Point point = Point.create(userId);
        point.charge(amount);
        assertThat(point.getPoint()).isEqualTo(amount);
    }

    @Test
    @DisplayName("충전 금액이 최대충전액 이상인 경우 code 2002, 최대 충전 금액을 초과하였습니다. 메시지의 예외를 반환한다.")
    void throwExceptionMaxPoint() {
        long userId = 1L;
        long amount = 100000001L;
        final Point point = Point.create(userId);
        final Throwable throwable = catchThrowable(() -> point.charge(amount));
        assertThat(throwable).isInstanceOf(ApiException.class).hasMessage("최대 충전 금액을 초과하였습니다.");
        assertThat(((ApiException) throwable).getApiResponseCodeMessage().getCode()).isEqualTo(2002);
    }

    @Test
    @DisplayName("충전 금액이 최소충전액 이하인 경우 code 2003, 최소 충전금액보다 부족합니다. 메시지의 예외를 반환한다.")
    void throwExceptionMinPoint() {
        long userId = 1L;
        long amount = 100L;
        final Point point = Point.create(userId);
        final Throwable throwable = catchThrowable(() -> point.charge(amount));
        assertThat(throwable).isInstanceOf(ApiException.class).hasMessage("최소 충전금액보다 부족합니다.");
        assertThat(((ApiException) throwable).getApiResponseCodeMessage().getCode()).isEqualTo(2003);
    }
}
