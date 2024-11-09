package org.example.demo.toxiproxy.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.demo.toxiproxy.mapper.MessageMapper;
import org.example.demo.toxiproxy.message.OrderEventMessage;
import org.example.demo.toxiproxy.model.Order;
import org.example.demo.toxiproxy.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderRepository orderRepository;
    private final MessageMapper messageMapper;

    @Value("${kafka.order.events.topic.name}")
    private final String orderEventsTopicName;

    public void sendOrderEventMessage() {
        List<Order> orders = orderRepository.findNotPublishedOrders();

        if (orders.isEmpty()) {
            log.info("No new order events to publish");
            return;
        }

        log.info("Start publishing order event messages");

        orders.forEach(order -> {
            OrderEventMessage orderEventMessage = messageMapper.mapToOrderEventMessage(order);
            kafkaTemplate.send(orderEventsTopicName, orderEventMessage.getId().toString(), messageMapper.toJson(orderEventMessage));
            orderRepository.save(order);
        });
    }
}
