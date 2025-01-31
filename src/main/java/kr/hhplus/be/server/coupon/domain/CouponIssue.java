package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.BaseEntity;
import kr.hhplus.be.server.user.domain.User;
import lombok.*;

@Entity
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_coupon_issue_coupon_user",
                        columnNames = {"coupon_id", "user_id"}
                )
        }
)
public class CouponIssue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    @Getter
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    private boolean used;

    public static CouponIssue create(final User user, final Coupon coupon) {
        return CouponIssue.builder()
                .user(user)
                .coupon(coupon)
                .used(false).build();
    }

    public void useCoupon(long userId) {
        validateCouponForUser(userId);
        coupon.validateCouponExpired();
        this.used = true;
    }

    private void validateCouponForUser(long userId) {
        if (!this.user.getId().equals(userId)) {
            throw new ApiException(ApiResponseCodeMessage.INVALID_CHARGE_AMOUNT);
        }
    }
}
