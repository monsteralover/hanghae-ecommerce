package kr.hhplus.be.server.order.repository;

import kr.hhplus.be.server.order.domain.Order;

public interface OrderRepository {

    Long save(Order finalOrder);

    void flush();
}
