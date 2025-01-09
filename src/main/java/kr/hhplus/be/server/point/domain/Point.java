package kr.hhplus.be.server.point.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "point", uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_id")
})
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

        final long updatedPoint = this.point + amount;
        if (updatedPoint > maxChargeAmount) {
            throw new ApiException(ApiResponseCodeMessage.MAX_CHARGE_AMOUNT);
        }
        if (amount < minChargeAmount) {
            throw new ApiException(ApiResponseCodeMessage.MIN_CHARGE_AMOUNT);
        }

        this.point = updatedPoint;
    }

    public void usePoint(final long amount) {
        final long balance = this.point - amount;
        if (balance < 0) {
            throw new ApiException(ApiResponseCodeMessage.LACK_OF_BALANCE);
        }
        this.point = balance;
    }
}
