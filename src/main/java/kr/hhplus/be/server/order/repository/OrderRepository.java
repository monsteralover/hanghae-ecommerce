package kr.hhplus.be.server.order.repository;

import kr.hhplus.be.server.order.domain.Order;

import java.util.List;

public interface OrderRepository {

    Long save(Order finalOrder);

    void flush();

    Order findLatestOrderByUserId(Long id);

    void deleteAll();

    List<Order> findAllByUserId(long savedUserId);
}
