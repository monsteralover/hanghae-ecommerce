package kr.hhplus.be.server;

public enum ApiResponseCodeMessage {

    //user 1000
    INVALID_USER(1001, "존재하지 않는 사용자입니다."),

    //balance 2000
    INVALID_CHARGE_AMOUNT(2001, "충전액이 올바르지 않습니다."),

    //coupon 3000
    OUT_OF_COUPON(3001, "쿠폰이 모두 소진되었습니다."),

    //order 4000
    OUT_OF_STOCK(4001, "상품의 재고가 소진되었습니다."),
    LACK_OF_BALANCE(4002, "충전액이 부족합니다."),


    //product 5000
    INVALID_PRODUCT_ID(4002, "존재하지 않는 상품입니다.");

    private final String message;
    private final Integer code;

    ApiResponseCodeMessage(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
