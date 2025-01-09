package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private String couponName;

    @Getter
    private int discountAmount;

    private int totalQuantity;

    private int remainingQuantity;

    @Getter
    private LocalDate expireDate;

    public void validateCouponIssue() {
        validateCouponExpired();
        validateRemainingQuantity();
    }

    private void validateRemainingQuantity() {
        if (this.remainingQuantity < 1) {
            throw new ApiException(ApiResponseCodeMessage.OUT_OF_COUPON);
        }
    }

    public void validateCouponExpired() {
        if (this.getExpireDate().isBefore(LocalDate.now())) {
            throw new ApiException(ApiResponseCodeMessage.COUPON_EXPIRED);
        }
    }

    public void deductQuantity() {
        this.remainingQuantity = this.remainingQuantity - 1;
    }
}
