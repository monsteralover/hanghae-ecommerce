package kr.hhplus.be.server;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private int code;
    private HttpStatus status;
    private String message;
    private T data;

    public ApiResponse(HttpStatus status, int code, String message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(final HttpStatus status, int code, final String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public ApiResponse(final HttpStatus httpStatus, final String message, final T data) {
        this.status = httpStatus;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, int code, String message) {
        return new ApiResponse<>(httpStatus, code, message);
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
        return new ApiResponse<>(httpStatus, message, data);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static <T> ApiResponse<T> badRequest(int code, String message) {
        return new ApiResponse<>(HttpStatus.BAD_REQUEST, code, message, null);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static <T> ApiResponse<T> conflict(int code, String message) {
        return new ApiResponse<>(HttpStatus.CONFLICT, code, message, null);
    }

    @ResponseStatus(HttpStatus.OK)
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(HttpStatus.OK, 0, "OK", data);
    }


}
