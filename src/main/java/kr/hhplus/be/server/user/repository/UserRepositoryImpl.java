package kr.hhplus.be.server.user.repository;

import kr.hhplus.be.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findUserById(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public void save(final User user) {
        userJpaRepository.save(user);
    }
}
