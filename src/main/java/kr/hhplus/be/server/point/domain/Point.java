package kr.hhplus.be.server.point.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Point extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long point;

    @Builder
    private Point(final Long userId, final Long point) {
        this.userId = userId;
        this.point = point;
    }

    public static Point create(final Long userId) {
        return Point.builder()
                .userId(userId)
                .point(0L)
                .build();
    }

    public void charge(final Long amount) {
        int maxChargeAmount = 100000000;
        int minChargeAmount = 1000;
        if (amount >= maxChargeAmount) {
            throw new ApiException(ApiResponseCodeMessage.MAX_CHARGE_AMOUNT);
        }
        if (amount < minChargeAmount) {
            throw new ApiException(ApiResponseCodeMessage.MIN_CHARGE_AMOUNT);
        }

        this.point = this.point + amount;
    }
}
