package org.demo.toxiproxy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.demo.toxiproxy.avro.OrderEventMessage;
import org.demo.toxiproxy.mapper.MessageMapper;
import org.demo.toxiproxy.model.Order;
import org.demo.toxiproxy.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
@RequiredArgsConstructor
@Profile({"producer", "test"})
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<UUID, OrderEventMessage> kafkaTemplate;
    private final OrderRepository orderRepository;
    private final MessageMapper messageMapper;
    private final Clock clock;

    @Value("${kafka.topics.order-events.name}")
    private String orderEventsTopicName;

    @Transactional
    public void sendOrderEventMessages() {
        List<Order> orderList = orderRepository.findNotPublishedOrders();

        if (orderList.isEmpty()) {
            log.info("No new order events to publish");
            return;
        }

        orderList.forEach(order -> {
            OrderEventMessage orderEventMessage = messageMapper.mapToOrderEventMessage(order);
            CompletableFuture<SendResult<UUID, OrderEventMessage>> future = kafkaTemplate.send(orderEventsTopicName, orderEventMessage.getMessageId(), orderEventMessage);
            RecordMetadata metadata;
            try {
                SendResult<UUID, OrderEventMessage> sendResult = future.get(100L, TimeUnit.MILLISECONDS);
                metadata = sendResult.getRecordMetadata();
            } catch (InterruptedException | TimeoutException | ExecutionException e) {
                log.error("Error occurred while sending order event to kafka topic {}", orderEventsTopicName, e);
                throw new RuntimeException(e);
            }

            order.setPublishedToKafka(LocalDateTime.now(clock));
            orderRepository.save(order);
            log.info("Order event with id: {} was send to topic: {}, partition: {}, offset: {}", orderEventMessage.getMessageId(), metadata.topic(), metadata.partition(), metadata.offset());
        });
    }
}
