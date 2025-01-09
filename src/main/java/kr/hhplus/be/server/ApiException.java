package kr.hhplus.be.server;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ApiResponseCodeMessage apiResponseCodeMessage;

    public ApiException(final ApiResponseCodeMessage apiResponseCodeMessage) {
        super(apiResponseCodeMessage.getMessage());
        this.apiResponseCodeMessage = apiResponseCodeMessage;
    }
}
