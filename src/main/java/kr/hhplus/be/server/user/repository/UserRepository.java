package kr.hhplus.be.server.user.repository;

import kr.hhplus.be.server.user.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findUserById(Long userId);
}
