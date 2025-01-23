package kr.hhplus.be.server;

public enum ApiResponseCodeMessage {

    //user 1000
    INVALID_USER(1001, "존재하지 않는 사용자입니다."),

    //balance 2000
    INVALID_CHARGE_AMOUNT(2001, "충전액이 올바르지 않습니다."),
    MAX_CHARGE_AMOUNT(2002, "최대 충전 금액을 초과하였습니다."),
    MIN_CHARGE_AMOUNT(2003, "최소 충전금액보다 부족합니다."),
    CHARGE_RECORD_NOT_EXIST(2004, "충전기록이 존재하지 않습니다."),
    LACK_OF_BALANCE(2005, "충전액이 부족합니다."),

    //coupon 3000
    OUT_OF_COUPON(3001, "쿠폰이 모두 소진되었습니다."),
    INVALID_COUPON(3002, "존재하지 않는 쿠폰입니다."),
    COUPON_EXPIRED(3002, "사용기한이 지난 쿠폰입니다."),
    COUPON_USER_UNMATCHED(3003, "해당 사용자에게 부여된 쿠폰이 아닙니다."),
    FAILED_TO_ISSUE_COUPON(3004, "쿠폰 발급에 실패했습니다."),
    ALREADY_ISSUED_COUPON(3005, "이미 발급된 쿠폰입니다"),

    //order 4000
    OUT_OF_STOCK(4001, "상품의 재고가 소진되었습니다."),
    INVALID_PAYMENT_AMOUNT(4002, "쿠폰 할인액이 결재액보다 더 클 수 없습니다."),


    //product 5000
    INVALID_PRODUCT_ID(4002, "존재하지 않는 상품입니다."),

    //etc 6000
    INVALID_PAGING_INFO(6001, "존재하지 않는 페이지입니다."),
    MAX_PAGE_SIZE(6002, "페이지 당 최대 출력 수를 초과하였습니다.");

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
