package kr.hhplus.be.server.user.service;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReadServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserReadService userReadService;

    @DisplayName("존재하지 않는 userId로 조회 시 code 1001, 존재하지 않는 사용자입니다. 메시지의 예외를 반환한다.")
    @Test
    void throwExceptionInvalidUser() {
        final long userId = -1L;
        // given
        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());
        // when
        final Throwable throwable = catchThrowable(() -> userReadService.checkUserExistsById((userId)));
        // then
        assertThat(throwable).isInstanceOf(ApiException.class).hasMessage("존재하지 않는 사용자입니다.");
        assertThat(((ApiException) throwable).getApiResponseCodeMessage().getCode()).isEqualTo(1001);
    }
}
