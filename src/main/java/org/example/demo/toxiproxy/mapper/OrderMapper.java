package org.example.demo.toxiproxy.mapper;

import org.example.demo.toxiproxy.dto.CreateOrderDto;
import org.example.demo.toxiproxy.dto.ResponseOrderDto;
import org.example.demo.toxiproxy.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public CreateOrderDto mapToOrderDto(Order order) {
        return CreateOrderDto.builder()
                .item(order.getItem())
                .quantity(order.getQuantity())
                .userInfo(order.getUserInfo())
                .build();
    }

    public Order mapToOrder(CreateOrderDto orderDto) {
        return Order.builder()
                .item(orderDto.getItem())
                .quantity(orderDto.getQuantity())
                .userInfo(orderDto.getUserInfo())
                .build();
    }

    public ResponseOrderDto mapToResponseOrderDto(Order order) {
        return ResponseOrderDto.builder()
                .id(order.getId())
                .item(order.getItem())
                .quantity(order.getQuantity())
                .userInfo(order.getUserInfo())
                .status(order.getStatus())
                .build();
    }
}
