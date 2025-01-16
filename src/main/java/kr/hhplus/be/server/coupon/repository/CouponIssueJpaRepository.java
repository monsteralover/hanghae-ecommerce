package kr.hhplus.be.server.coupon.repository;

import kr.hhplus.be.server.coupon.domain.CouponIssue;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue, Long> {
    @EntityGraph(attributePaths = {"coupon", "user"})
    List<CouponIssue> findAllByUserIdAndUsed(long userId, boolean usePossible);

    Optional<CouponIssue> findById(long couponIssueId);

    @Query("SELECT COUNT(ci) FROM CouponIssue ci WHERE ci.coupon.id = :couponId")
    long countByCouponId(@Param("couponId") Long couponId);
}
