package kr.hhplus.be.server.order.service;

import kr.hhplus.be.server.coupon.facade.OrderFacadeRequest;
import org.springframework.stereotype.Component;

@Component
public class DataPlatform {
    public boolean sendData(final OrderFacadeRequest request){
        return true;
    }
}
