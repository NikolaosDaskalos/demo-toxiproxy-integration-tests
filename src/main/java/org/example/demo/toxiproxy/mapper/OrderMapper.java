package org.example.demo.toxiproxy.mapper;

import org.example.demo.toxiproxy.dto.OrderDto;
import org.example.demo.toxiproxy.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDto mapToOrderDto(Order order) {
        return OrderDto.builder()
                .item(order.getItem())
                .quantity(order.getQuantity())
                .userInfo(order.getUserInfo())
                .status(order.getStatus())
                .build();
    }
}
