package kr.hhplus.be.server.product.service;

import kr.hhplus.be.server.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class PaginationVerificationTest {

    @DisplayName("요청된 page,size 정보와 동일하고 id 오름차순으로 정렬하는 pageable객체를 반환한다.")
    @Test
    void pageableTest() {
        // given
        int page = 1;
        int size = 5;
        // when
        final Pageable pageable = PaginationVerification.toPageable(page, size);

        // then
        assertThat(pageable.getOffset()).isEqualTo(0);
        assertThat(pageable.getPageNumber()).isEqualTo(0);
        assertThat(pageable.getPageSize()).isEqualTo(size);
        assertThat(pageable.getSort()).isEqualTo(Sort.by("id").descending());
    }

    @DisplayName("페이지가 0으로 들어온 경우 code 6001, 존재하지 않는 페이지입니다. 메시지의 예외를 반환한다.")
    @Test
    void pageZeroTest() {
        // given
        int page = 0;
        int size = 5;
        // when
        final Throwable throwable = catchThrowable(() -> PaginationVerification.toPageable(page, size));

        // then
        assertThat(throwable).isInstanceOf(ApiException.class).hasMessage("존재하지 않는 페이지입니다.");
        assertThat(((ApiException) throwable).getApiResponseCodeMessage().getCode()).isEqualTo(6001);
    }

    @DisplayName("페이지 사이즈가 0으로 들어온 경우 code 6001, 존재하지 않는 페이지입니다. 메시지의 예외를 반환한다.")
    @Test
    void pageSizeZeroTest() {
        // given
        int page = 5;
        int size = 0;
        // when
        final Throwable throwable = catchThrowable(() -> PaginationVerification.toPageable(page, size));

        // then
        assertThat(throwable).isInstanceOf(ApiException.class).hasMessage("존재하지 않는 페이지입니다.");
        assertThat(((ApiException) throwable).getApiResponseCodeMessage().getCode()).isEqualTo(6001);
    }

    @DisplayName("페이지 사이즈가 1000 이상으로 들어온 경우 code 6002,페이지 당 최대 출력 수를 초과하였습니다. 메시지의 예외를 반환한다.")
    @Test
    void pageMaxSizeTest() {
        // given
        int page = 1;
        int size = 1001;
        // when
        final Throwable throwable = catchThrowable(() -> PaginationVerification.toPageable(page, size));

        // then
        assertThat(throwable).isInstanceOf(ApiException.class).hasMessage("페이지 당 최대 출력 수를 초과하였습니다.");
        assertThat(((ApiException) throwable).getApiResponseCodeMessage().getCode()).isEqualTo(6002);
    }

}
