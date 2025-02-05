package org.demo.toxiproxy.mapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.toxiproxy.avro.OrderEventMessage;
import org.demo.toxiproxy.model.OrderEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class OrderEventMapper {

    public OrderEvent mapToOrderEvent(OrderEventMessage message) {
        return OrderEvent.builder()
                .id(message.getMessageId())
                .orderId(message.getOrderId())
                .item(message.getItem())
                .quantity(message.getQuantity())
                .userInfo(message.getUserInfo())
                .status(message.getStatus())
                .build();
    }
}
