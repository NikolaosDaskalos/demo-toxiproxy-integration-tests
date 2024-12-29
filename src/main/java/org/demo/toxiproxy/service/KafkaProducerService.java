package org.demo.toxiproxy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.demo.toxiproxy.mapper.MessageMapper;
import org.demo.toxiproxy.message.OrderEventMessage;
import org.demo.toxiproxy.model.Order;
import org.demo.toxiproxy.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderRepository orderRepository;
    private final MessageMapper messageMapper;
    private final Clock clock;

    @Value("${kafka.topics.order-events.name}")
    private String orderEventsTopicName;

    @Transactional
    public void sendOrderEventMessages() {
        List<Order> orders = orderRepository.findNotPublishedOrders();

        if (orders.isEmpty()) {
            log.info("No new order events to publish");
            return;
        }

        orders.forEach(order -> {
            OrderEventMessage orderEventMessage = messageMapper.mapToOrderEventMessage(order);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(orderEventsTopicName, orderEventMessage.getId().toString(), messageMapper.toJson(orderEventMessage));
            RecordMetadata metadata;
            try {
                SendResult<String, String> sendResult = future.get(100L, TimeUnit.MILLISECONDS);
                metadata = sendResult.getRecordMetadata();
            } catch (InterruptedException | TimeoutException | ExecutionException e) {
                log.error("Error occurred while sending order event to kafka topic {}", orderEventsTopicName, e);
                throw new RuntimeException(e);
            }

            order.setPublishedToKafka(LocalDateTime.now(clock));
            orderRepository.save(order);
            log.info("Order event with id: {} was send to topic: {}, partition: {}, offset: {}", orderEventMessage.getId(), metadata.topic(), metadata.partition(), metadata.offset());
        });
    }
}
