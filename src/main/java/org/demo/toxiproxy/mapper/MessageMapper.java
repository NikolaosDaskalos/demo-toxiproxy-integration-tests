package org.demo.toxiproxy.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.toxiproxy.avro.OrderEventMessage;
import org.demo.toxiproxy.model.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class MessageMapper {
    private final ObjectMapper objectMapper;

    public OrderEventMessage mapToOrderEventMessage(Order order) {
        return OrderEventMessage.newBuilder()
                .setMessageId(UUID.randomUUID())
                .setOrderId(order.getId())
                .setItem(order.getItem())
                .setQuantity(order.getQuantity())
                .setUserInfo(order.getUserInfo())
                .setStatus(order.getStatus())
                .build();
    }

    public OrderEventMessage mapToOrderEventMessage(String json) throws JsonProcessingException {
        try {
            return objectMapper.readValue(json, OrderEventMessage.class);
        } catch (JsonProcessingException e) {
            log.error("deserialization error of {} to OrderEventMessage", json, e);
            throw e;
        } catch (Exception e) {
            log.error("Unknown error happened during {} deserialization ", json, e);
            throw e;
        }
    }

    public String toJson(OrderEventMessage orderEventMessage) {
        try {
            return objectMapper.writeValueAsString(orderEventMessage);
        } catch (Exception e) {
            throw new RuntimeException("Error while converting OrderEventMessage to JSON", e);
        }
    }
}
