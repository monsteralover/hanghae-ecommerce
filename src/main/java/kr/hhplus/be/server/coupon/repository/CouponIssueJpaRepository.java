package kr.hhplus.be.server.coupon.repository;

import kr.hhplus.be.server.coupon.domain.CouponIssue;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue, Long> {
    @EntityGraph(attributePaths = {"coupon", "user"})
    List<CouponIssue> findAllByUserIdAndUsed(long userId, boolean usePossible);
}
