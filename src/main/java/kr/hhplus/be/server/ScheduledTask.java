package kr.hhplus.be.server;

import kr.hhplus.be.server.order.service.ResendDataPlatform;
import kr.hhplus.be.server.product.service.ProductStockCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTask {
    private final ProductStockCommandService productStockCommandService;
    private final ResendDataPlatform resendDataPlatform;

    //3일 주기 자정에 한 번
    @Scheduled(cron = "0 0 0 */3 * *")
    public void resetAccumulatedSoldCountEveryThreeDays() {
        productStockCommandService.resetAccumulatedSoldCountEveryThreeDays();
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void resendPlatformServiceData() {
        resendDataPlatform.resendData();
    }
}
