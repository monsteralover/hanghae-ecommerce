package kr.hhplus.be.server.order.service;

import kr.hhplus.be.server.order_finished.domain.EventStatus;
import kr.hhplus.be.server.order_finished.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResendDataPlatform {
    private final OutBoxRepository outBoxRepository;
    private final DataPlatform dataPlatform;

    public void resendData() {
        //todo: 발행된지 5분 후인지 체크 필요
        outBoxRepository.findAllByEventStatus(EventStatus.INITIATED);
    }
}
