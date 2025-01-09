package kr.hhplus.be.server.product.service;

import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.ApiResponseCodeMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationVerification {
    public static Pageable toPageable(int page, int size) {
        if (page < 1 || size < 1) {
            throw new ApiException(ApiResponseCodeMessage.INVALID_PAGING_INFO);
        }
        return PageRequest.of(page - 1, size, Sort.by("id").descending());
    }
}
