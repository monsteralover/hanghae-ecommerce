package kr.hhplus.be.server.user.service;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import kr.hhplus.be.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserReadService {
    private final UserRepository userRepository;

    public void checkUserExistsById(Long userId) {
        userRepository.findUserById(userId)
                .orElseThrow(() -> new ApiException(ApiResponseCodeMessage.INVALID_USER));
    }
}
