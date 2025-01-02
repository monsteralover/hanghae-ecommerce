package kr.hhplus.be.server.balance;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BalanceResponse {
    private Long userId;
    private Long userBalance;
}
