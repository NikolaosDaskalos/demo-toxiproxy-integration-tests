package org.demo.toxiproxy.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.toxiproxy.avro.OrderEventMessage;
import org.demo.toxiproxy.mapper.MessageMapper;
import org.demo.toxiproxy.mapper.OrderEventMapper;
import org.demo.toxiproxy.model.OrderEvent;
import org.demo.toxiproxy.repository.OrderEventRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"consumer", "test"})
@Component
@AllArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final OrderEventRepository orderEventRepository;
    private final OrderEventMapper orderEventMapper;
    private final MessageMapper messageMapper;

    public void processOrderEventMessage(OrderEventMessage orderEventMessage) {
        OrderEvent orderEvent = orderEventMapper.mapToOrderEvent(orderEventMessage);
        if (orderEventRepository.findById(orderEvent.getId()).isPresent()) {
            log.info("Order event with id {} exists", orderEvent.getId());
            orderEventRepository.save(orderEvent);
            log.info("Order event with id {} has been updated", orderEvent.getId());
        } else {
            orderEventRepository.save(orderEvent);
            log.info("Saved order event with id {}", orderEvent.getId());
        }
    }
}
