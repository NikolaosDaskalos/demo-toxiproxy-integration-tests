package org.demo.toxiproxy.kafka.consumer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.demo.toxiproxy.avro.OrderEventMessage;
import org.demo.toxiproxy.service.KafkaConsumerService;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@Profile({"consumer", "test"})
@AllArgsConstructor
public class OrderConsumer {
    private final KafkaConsumerService consumerService;

    @KafkaListener(topics = "${kafka.topics.order-events.name}", groupId = "orders")
    public void receiveOrderMessage(ConsumerRecord<UUID, OrderEventMessage> consumerRecord, Acknowledgment ack) {
        log.info("Receiving message: partition={}, offset={}, key={}", consumerRecord.partition(), consumerRecord.offset(), consumerRecord.key());
        consumerService.processOrderEventMessage(consumerRecord.value());
        ack.acknowledge();
    }
}
