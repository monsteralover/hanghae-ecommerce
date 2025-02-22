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
    public User save(final User user) {
        userJpaRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> findByUserIdWithLock(final long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public void deleteAll() {
        userJpaRepository.deleteAll();
    }
}
