package kr.hhplus.be.server.order.service;


import kr.hhplus.be.server.ApiException;
import kr.hhplus.be.server.order.OrderCalledEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventHandler {
    private final DataPlatform dataPlatform;

    @Async
    @EventListener
    public void handleOrdered(OrderCalledEvent event) {
        try {
            dataPlatform.sendData(event.request());
            log.info("sent data to ");
        } catch (ApiException e) {
            log.error("Failed to send data to DataPlatform", e);
        }
    }

}