package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.*;
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
}
