package org.demo.toxiproxy.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.demo.toxiproxy.message.OrderEventMessage;
import org.demo.toxiproxy.message.OrderMessage;
import org.demo.toxiproxy.model.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class MessageMapper {
    private final ObjectMapper objectMapper;

    public OrderEventMessage mapToOrderEventMessage(Order order) {
        return OrderEventMessage.builder()
                .id(UUID.randomUUID())
                .order(mapToOrderMessage(order))
                .build();
    }

    public OrderMessage mapToOrderMessage(Order order) {
        return OrderMessage.builder()
                .id(order.getId())
                .item(order.getItem())
                .quantity(order.getQuantity())
                .userInfo(order.getUserInfo())
                .status(order.getStatus())
                .build();
    }

    public String toJson(OrderEventMessage orderEventMessage) {
        try {
            return objectMapper.writeValueAsString(orderEventMessage);
        } catch (Exception e) {
            throw new RuntimeException("Error while converting OrderEventMessage to JSON", e);
        }
    }

}
