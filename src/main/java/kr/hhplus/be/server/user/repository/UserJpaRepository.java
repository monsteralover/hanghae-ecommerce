package kr.hhplus.be.server.user.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findById(long couponIssueId);


}
